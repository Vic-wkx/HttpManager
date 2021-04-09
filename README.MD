[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![jitpack](https://jitpack.io/v/wkxjc/HttpManager.svg)](https://jitpack.io/#wkxjc/HttpManager)

# HttpManager [中文文档](https://github.com/wkxjc/HttpManager/blob/master/README_CN.MD) | English Doc

## Description

This is a library that encapsulates Http network requests, HTTP multi-interface merges, and network file downloads. Adapted from [wzgiceman](https://github.com/wzgiceman)'s [RxRetrofit library](https://github.com/wzgiceman/RxjavaRetrofitDemo-string-master).

## Screenshots

<img src="screenshots/singleApi.gif" width=25%><span>&nbsp;&nbsp;&nbsp;&nbsp;</span><img src="screenshots/apis.gif" width=25%><span>&nbsp;&nbsp;&nbsp;&nbsp;</span><img src="screenshots/download.gif" width=25%>

## Download

Step 1. Add it in your root *build.gradle* at the end of repositories:

```groovy
allprojects {
	repositories {
		// ...
		maven { url 'https://jitpack.io' }
	}
}
```

Step 2. Add the dependency

```groovy
implementation 'com.github.wkxjc:HttpManager:2.0'
```

## How to use?

1.Single network request:

```kotlin
private val httpManager by lazy { HttpManager(this) }
// ...
httpManager.request(homeArticlesApi, object : HttpListener() {

    override fun onNext(result: String) {
        tvResult.text = result
    }
    
    override fun onError(error: Throwable) {
        tvResult.text = error.message
    }
})
```

2.Multiple network requests:

```kotlin
private val httpManager by lazy { HttpManager(this) }
// ...
httpManager.request(
    apis = arrayOf(homeArticlesApi, bannerApi),
    listener = object : HttpListListener() {
        
        /**
         * Single api result callback
         */
        override fun onSingleNext(api: BaseApi, result: String): Any {
            when (api) {
                homeArticlesApi -> {
                    Log.d("HttpListActivity", "Single api result callback: homeArticlesApi:$result")
                    // Here you can convert the returned string to any object, generally using Gson/fastJson to pause the object here.
                    return 123
                }
                bannerApi -> Log.d("HttpListActivity", "Single api result callback: bannerApi:$result")
            }
            return super.onSingleNext(api, result)
        }
        
        /**
         * All api result callbacks
         */
        override fun onNext(resultMap: HashMap<BaseApi, Any>) {
            // use "as" method, convert the object saved in resultMap to the type returned by onSingleNext.
            tvResultList.text =
                "homeArticlesApi result: ${resultMap[homeArticlesApi] as Int}\n" +
                        "bannerApi result: ${resultMap[bannerApi].toString()}"
        }
        
        override fun onError(error: Throwable) {
            tvResultList.text = error.message
        }
    }
)
```

3.Download network file:

```kotlin
DownManager.download(DownloadApi().apply {
    url = "https://media.w3.org/2010/05/sintel/trailer.mp4"
})
```

## Configuration

Before using HttpManager for a single api request or multiple api requests, you need some preparation.

Step 1. Create a new MyApiConfig, inherit from ApiConfig, and override the baseUrl variable:

```kotlin
class MyApiConfig : ApiConfig() {
    override var baseUrl = "Your network request BaseUrl"
}
```

Step 2. Initialize RxRetrofitApp in Application, set apiConfig:

```kotlin
RxRetrofitApp.apply {
    application = this@MyApplication
    apiConfig = MyApiConfig()
}
```

Step 3. Similar to using the Retrofit library, create a new ApiService, for example:

```kotlin
interface ApiService {

    @GET("article/list/{page}/json")
    fun getHomeArticles(@Path("page") page: Int): Observable<String>
}
```

Create a new Api, inherited from BaseApi, for example:

```kotlin
class HomeArticlesApi : BaseApi() {

    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getHomeArticles(0)
    }
}
```

OK, now you can use httpManager to get the result of your api.

Before using DownManager, just make sure to initialize the RxRetrofitApp application in your Application.

```kotlin
RxRetrofitApp.apply {
    application = this@MyApplication
}
```

## ResultConverter

In actual development, the data returned by the back-end interface is generally in a fixed format. For example, the fixed format in this Demo is as follows:

```kotlin
data class BaseResult(
    // code == 0 means request succeed
    val errorCode: Int,
    // This variable carries an error message when request failed
    val errorMsg: String,
    // This variable means the returned business data
    val data: String?
)
```

In this library, we can parse the returned data according to the fixed format first, and only deliver the returned business data to the business layer. Create a new ResultConverter:

```kotlin
class ResultConverter : IResultConverter {
    override fun convert(response: String): String {
        // parse the result according to BaseResult here
        val result = JSONObject.parseObject(response, BaseResult::class.java)
        // handle errors through defined error codes
        if (result.errorCode != 0) throw Throwable("errorCode != 0, errorMsg = ${result.errorMsg}")
        return result.data ?: ""
    }
}
```

Then set it in the MyApiConfig:

```kotlin
class MyApiConfig : ApiConfig() {
    // ...
    override var resultConverter: IResultConverter = ResultConverter()
}
```

You can turned off it in the api via the "apiConfig.ignoreResultConverter" configuration.

## HttpResponseProcessor

Sometimes, the background does not return an error message through the errorMessage of the BaseResult structure, but the error code of "404" or "403" requested by the Http request informs us that the request is failed.
In this library, you can process the http response uniformly. Create a new HttpResponseProcessor:

```kotlin
class HttpResponseProcessor : IHttpResponseProcessor {
    override fun handleResponse(response: Response): Response {
        // Here you can handle the http error code: response.code(), this error code is different from the errorCode in BaseResult.
        if (response.code() >= 400) throw Throwable("Http response code = ${response.code()}")
        return response
    }
}
```

Then set it in the MyApiConfig:

```kotlin
class MyApiConfig : ApiConfig() {
    // ...
    override var httpResponseProcessor: IHttpResponseProcessor = HttpResponseProcessor()
}
```

You can turned off it in the api via the "apiConfig.ignoreResponseProcessor" configuration.

## Custom configuration

### 1.Globally modify the network request default configuration
      
In MyApiConfig, you can not only configure baseUrl, but also configure the following parameters. The following are the default values：

```kotlin
open class ApiConfig {
    // BaseUrl requested by Retrofit Network
    open var baseUrl = ""

    // Whether to display the loading dialog
    open var showLoading = true

    // Whether the loading dialog can be cancelled
    open var loadingCancelable = true

    // Cache configuration
    open var cacheConfig = CacheConfig().apply {
        // Do you need cache
        cache = false
        // Cache expiration time when the network is available
        onlineCacheTime = 30
        // Cache expiration time when the network is unavailable
        offlineCacheTime = 60 * 60 * 24 * 30
    }

    // Whether to ignore the BaseResult converter
    open var ignoreResultConverter: Boolean = false

    // Whether to ignore the ResponseProcessor
    open var ignoreResponseProcessor: Boolean = false

    // Retry configuration
    open var retry = RetryConfig().apply {
        // times of retry
        count = 5
        // Retry delay time
        delay = 100L
        // increase the delay time each time
        increaseDelay = 500L
    }

    // timeout configuration
    open var timeOutConfig = TimeoutConfig().apply {
        connectionTime = 10L
        readTime = 10L
        writeTime = 10L
    }

    // Http request header information
    open var headers: Headers? = null

    // Result converter
    open var resultConverter: IResultConverter = DefaultResultConverter()

    // Http response processor
    open var httpResponseProcessor: IHttpResponseProcessor = DefaultHttpResponseProcessor()
}
```

The configuration here is valid for all network requests.

### 2. Modify single network request default configuration

The configurable parameters of single configuration is the same as the global configuration. For example:

```kotlin
class HomeArticlesApi : BaseApi() {

    init {
        apiConfig.baseUrl = "config its own baseUrl if it's different from Default ApiConfig"
        apiConfig.showLoading = true
        apiConfig.loadingCancelable = true
        apiConfig.cacheConfig = CacheConfig().apply {
            cache = false
            onlineCacheTime = 30
            offlineCacheTime = 60 * 60 * 24 * 30
        }
        apiConfig.ignoreResultConverter = false
        apiConfig.ignoreResponseProcessor = false
        apiConfig.retry = RetryConfig().apply {
            count = 5
            delay = 100L
            increaseDelay = 500L
        }
        apiConfig.timeOutConfig = TimeoutConfig().apply {
            connectionTime = 10L
            readTime = 10L
            writeTime = 10L
        }
        apiConfig.headers = Headers.of(mapOf("name1" to "value1", "name2" to "value2"))
    }
    
    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getHomeArticles(0)
    }
}
```

### 3.Http multi-interface request global configuration
    
When you request an array of apis, the configuration of every single api is still valid. Only the two parameters, showLoading and loadingCancelable, are invalid.
The configurable parameters of multi-interface request are as follows, the following are the default values:

```kotlin
open class HttpListConfig {
    // Whether to display the loading dialog
    open var showLoading: Boolean = true

    //  Whether the loading dialog can be cancelled
    open var loadingCancelable: Boolean = true

    // Whether to request apis in order
    open var order: Boolean = true
}
```

If you need to modify the configuration of the Http multi-interface request globally, create a new HttpListConfig class, which inherits from HttpListConfig:

```kotlin
class MyHttpListApiConfig : HttpListConfig() {
    override var showLoading = false
    override var order = false
}
```

Then set it in the Application:

```kotlin
RxRetrofitApp.apply {
    // ...
    httpListConfig = MyHttpListApiConfig()
}
```

### 4.Separately configure multi-interface request 

The configurable parameters of single configuration is the same as the global configuration. For example:

```kotlin
httpManager.request(
    apis, listener,
    HttpListConfig().apply {
        showLoading = true
        loadingCancelable = true
        order = false
    },
)
```

### 5.DownManager global configuration

When you use DownManager downloading files, the global default configuration is as follows:

```kotlin
open class DownloadConfig {
    companion object {
        /**Update the progress after downloading progressStep bits. Use [PROGRESS_BY_PERCENT] to update by percentage*/
        const val PROGRESS_BY_PERCENT = -1
    }

    /**The saved folder path, if not set, the default path is "app cache path/download". If it's set to external path, you need to ensure that the app have read and write permissions*/
    open var saveDir: String = ""
        get() {
            if (field.isNotEmpty()) return field
            val cacheDir = RxRetrofitApp.application.externalCacheDir?.absolutePath
                ?: throw Throwable("application is null")
            return "$cacheDir/download/"
        }

    /**Update the progress after downloading progressStep bits. Use [PROGRESS_BY_PERCENT] to update by percentage*/
    open var progressStep = 1024 * 4

    /**Retry configuration*/
    open var retry = RetryConfig()

    /**Http request header information*/
    open var headers: Headers? = null
}
```

If you need to modify the global configuration, create a new MyDownConfig class, inheriting from the DownConfig class:

```kotlin
class MyDownConfig : DownConfig(){
    override var saveDir: String = ""
        get() {
            if (field.isNotEmpty()) return field
            val cacheDir = RxRetrofitApp.application.externalCacheDir?.absolutePath
                ?: throw Throwable("application is null")
            return "$cacheDir/download/"
        }
    override var progressStep = 1024 * 4
    override var retry = RetryConfig()
    override var headers: Headers? = null
}
```

Then set it in the Application:

```kotlin
RxRetrofitApp.apply {
    // ...
    downConfig = MyDownConfig()
}
```

### 6.Configure DownManager separately

The configurable parameters of single configuration is the same as the global configuration. For example:

```kotlin
DownManager.down(DownConfig().apply {
    url = "download_link"
    /**The name of the saved file. If not set, the default name is the last path segment of the url.*/
    saveFileName = "file_name.mp4"
    downloadConfig.saveDir = "${RxRetrofitApp.application.externalCacheDir?.absolutePath}/download/"
    downloadConfig.progressStep = 1024 * 128
    downloadConfig.retry = RetryConfig()
    downloadConfig.headers = Headers.of(mapOf("name1" to "value1", "name2" to "value2"))
})
```

## Bug Feedback

If you have any questions or suggestions, feel free to create an [issue](https://github.com/wkxjc/HttpManager/issues).

## LICENSE

    Copyright (c) 2021-present, Alpinist Wang Contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
