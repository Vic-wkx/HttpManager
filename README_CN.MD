[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![jitpack](https://jitpack.io/v/wkxjc/HttpManager.svg)](https://jitpack.io/#wkxjc/HttpManager)

# HttpManager 中文文档 | [English Doc](https://github.com/wkxjc/HttpManager/blob/master/README.MD)

## 描述

这是一个封装了Http网络请求、HTTP多接口合并、网络文件下载的库。改编自[wzgiceman](https://github.com/wzgiceman)的[RxRetrofit库](https://github.com/wzgiceman/RxjavaRetrofitDemo-string-master)。

## 屏幕截图

<img src="screenshots/singleApi.gif" width=25%><span>&nbsp;&nbsp;&nbsp;&nbsp;</span><img src="screenshots/apis.gif" width=25%><span>&nbsp;&nbsp;&nbsp;&nbsp;</span><img src="screenshots/download.gif" width=25%>

## 下载

第一步. 在根目录的*build.gradle*中添加以下代码:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

第二步. 添加gradle依赖

```
implementation 'com.github.wkxjc:HttpManager:1.7'
```

## 如何使用？

1.单个网络请求：

```
private val httpManager by lazy { HttpManager(this) }
...
httpManager.request(randomWallpaperApi, object : HttpListener() {

    override fun onNext(result: String) {
        tvResult.text = result
    }
    
    override fun onError(error: Throwable) {
        tvResult.text = error.message
    }
})
```

2.多个网络请求：

```
private val httpManager by lazy { HttpManager(this) }
...
httpManager.request(
    apis = arrayOf(randomWallpaperApi, categoryApi),
    listener = object : HttpListListener() {
        
        /**
         * 单个api结果回调
         */
        override fun onSingleNext(api: BaseApi, result: String): Any {
            when (api) {
                randomWallpaperApi -> {
                    Log.d("HttpListActivity", "收到单个结果：randomWallpaperApi:$result")
                    // 这里可以将返回的字符串转换为任意对象，一般在这里使用Gson/fastJson解析对象
                    return 123
                }
                categoryApi -> Log.d("HttpListActivity", "收到单个结果：categoryApi:$result")
            }
            return super.onSingleNext(api, result)
        }
        
        /**
         * 所有api结果回调
         */
        override fun onNext(resultMap: HashMap<BaseApi, Any>) {
            // 通过 as 方法，将resultMap中保存的对象取出并转换成onSingleNext返回的类型
            tvResultList.text =
                "randomWallpaperApi result: ${resultMap[randomWallpaperApi] as Int}\n" +
                        "categoryApi result: ${resultMap[categoryApi].toString()}"
        }
        
        override fun onError(error: Throwable) {
            tvResultList.text = error.message
        }
    }
)
```

3.下载网络文件：

```
HttpDownManager.down(DownConfig().apply {
    url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
})
```

## 配置

使用HttpManager进行单个api请求或多个api请求之前，你需要一些准备工作.

第一步. 新建ApiConfig，继承自DefaultApiConfig，重写baseUrl变量：

```
class ApiConfig : DefaultApiConfig() {
    override var baseUrl = "你的网络请求BaseUrl"
}
```

第二步. 在Application中初始化RxRetrofitApp，设置apiConfig:

```
RxRetrofitApp.apply {
    application = this@MyApplication
    apiConfig = ApiConfig()
}
```

第三步. 和使用Retrofit库类似，新建ApiService，例如：

```
interface WallpaperApiService {

    @GET("v1/vertical/vertical")
    fun getRandomWallpaper(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0,
        @Query("adult") adult: Boolean = false,
        @Query("first") first: Int = 0,
        @Query("order") order: String = "hot"
    ): Observable<String>
}
```

新建Api，继承自BaseApi，例如：

```
class RandomWallpaperApi : BaseApi() {

    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(WallpaperApiService::class.java)
        return apiService.getRandomWallpaper()
    }
}
```

OK，这样就能获取到接口请求结果了。

使用HttpDownManager进行网络文件下载之前，只需要确保在Application中初始化了RxRetrofitApp的application即可。

```
RxRetrofitApp.apply {
    application = this@MyApplication
}
```

## 返回结果统一解析

在实际开发中，后台接口返回的数据一般是有统一格式的，例如本Demo中接口返回的数据统一格式如下：

```
class BaseResult {
    // 此变量为0表示请求成功
    var code: Int = 0
    // 请求失败时，此变量携带错误信息
    var msg: String = ""
    // 此变量存储返回的业务数据
    var res: String = ""
}
```

在此框架中，我们可以将返回数据先统一按此结构解析，仅将返回的业务数据传到业务层。新建ResultConverter：

```
class ResultConverter : IResultConverter {
    override fun convert(response: String): String {
        // 在这里对结果统一解析
        val result = JSONObject.parseObject(response, BaseResult::class.java)
        // 通过定义的错误码，统一做错误处理
        if (result.code != 0) throw Throwable("code != 0, msg = ${result.msg}")
        return result.res
    }
}
```

然后在Application中设置即可：

```
RxRetrofitApp.apply {
    ...
    resultConverter = ResultConverter()
}
```

在api中可以通过ignoreResultConverter配置关闭这一层解析

## Http返回码统一处理

有时候，后台并不会通过BaseResult结构的errorMessage返回错误信息，而是通过Http请求的"404"或者"403"等等错误码告知前端请求错误。
在此框架中，我们可以将Http返回码统一处理。新建HttpResponseProcessor：
```
class HttpResponseProcessor : IHttpResponseProcessor {
    override fun handleResponse(response: Response): Response {
        // 在这里可以处理http返回的错误码：response.code()，这里的错误码不同于BaseResult中的errorCode
        if (response.code() >= 400) throw Throwable("Http response code = ${response.code()}")
        return response
    }
}
```

然后在Application中设置即可：

```
RxRetrofitApp.apply {
    ...
    httpResponseProcessor = HttpResponseProcessor()
}
```

## 定制

### 1.全局修改网络请求默认配置

在ApiConfig中，不仅可以配置baseUrl，还可以配置以下参数，以下是默认值：

```
open class DefaultApiConfig {
    // Retrofit网络请求的BaseUrl
    open var baseUrl = ""
    // 是否显示Loading弹窗
    open var showLoading = true
    // Loading弹窗是否可取消
    open var loadingCancelable = true
    // 缓存配置
    open var cacheConfig = CacheConfig().apply {
        // 是否需要缓存处理
        cache = false
        // 有网的时候的缓存过期时间
        onlineCacheTime = 30
        // 没网的时候的缓存过期时间
        offlineCacheTime = 60 * 60 * 24 * 30
    }
    // 是否忽略ResultConverter解析
    open var ignoreResultConverter: Boolean = false
    // 重试配置
    open var retry = RetryConfig().apply {
        // 重试次数
        count = 5
        // 重试延迟时间
        delay = 100L
        // 每次增加延迟的时间
        increaseDelay = 500L
    }
    // 超时时间配置
    open var timeOutConfig = TimeoutConfig().apply {
        // 连接超时时间
        connectionTime = 10L
        // 读取超时时间
        readTime = 10L
        // 写入超时时间
        writeTime = 10L
    }
    // Http请求head信息
    open var headers: Headers? = null
}
```

在这里的配置是对所有的网络请求生效的。

### 2.单个网络请求修改默认配置

单个网络请求可配置参数与全局网络请求可配置参数相同。使用示例：

```
class RandomWallpaperApi : BaseApi() {

    init {
        // Retrofit网络请求的BaseUrl
        baseUrl = "单独配置baseUrl"
        // 是否显示Loading弹窗
        showLoading = true
        // Loading弹窗是否可取消
        loadingCancelable = true
        // 缓存配置
        cacheConfig = CacheConfig().apply {
            // 是否需要缓存处理
            cache = false
            // 有网的时候的缓存过期时间
            onlineCacheTime = 30
            // 没网的时候的缓存过期时间
            offlineCacheTime = 60 * 60 * 24 * 30
        }
        // 是否忽略ResultConverter解析
        ignoreResultConverter = false
        // 重试配置
        retry = RetryConfig().apply {
            // 重试次数
            count = 5
            // 重试延迟时间
            delay = 100L
            // 每次增加延迟的时间
            increaseDelay = 500L
        }
        // 超时时间配置
        timeOutConfig = TimeoutConfig().apply {
            // 连接超时时间
            connectionTime = 10L
            // 读取超时时间
            readTime = 10L
            // 写入超时时间
            writeTime = 10L
        }
        // Http请求head信息，示例如下：
        headers = Headers.of(mapOf("name1" to "value1", "name2" to "value2"))
    }
    
    override fun getObservable(): Observable<String> {
        val apiService = retrofit.create(WallpaperApiService::class.java)
        return apiService.getRandomWallpaper()
    }
}
```

### 3.Http多接口合并请求全局配置

Http多接口合并请求时，单个api配置的参数大多数仍然生效，只有showLoading、loadingCancelable两个参数不再生效，需要单独配置。
多接口合并可配置参数如下，以下是默认值：

```
open class DefaultHttpListConfig {
    // 是否显示Loading弹窗
    open var showLoading: Boolean = true
    // Loading弹窗是否可取消
    open var loadingCancelable: Boolean = true
    // 是否按照顺序请求api
    open var order: Boolean = false
}
```

如果需要全局修改Http多接口请求的配置，新建HttpListConfig类，继承自DefaultHttpListConfig：

```
class HttpListConfig : DefaultHttpListConfig() {
    override var showLoading = true
    override var loadingCancelable = true
    override var order = false
}
```

然后在Application中设置即可：

```
RxRetrofitApp.apply {
    ...
    httpListConfig = HttpListConfig()
}
```
### 4.Http多接口合并请求单独配置

单独配置与全局配置的可配置参数相同。使用示例：

```
httpManager.request(
    apis = apis,
    config = HttpListConfig(showLoading = true, loadingCancelable = true, order = false),
    listener = listener
)
```

### 5.Http下载文件全局配置

下载文件时，全局默认配置如下：

```
open class DefaultDownConfig {
    /**保存的文件夹路径，如果不设置，默认路径是"应用缓存路径/download/",如果设置为外部路径，需要自己确保有读写权限*/
    var saveDir: String = ""
        get() {
            if (field.isNotEmpty()) return field
            val cacheDir = RxRetrofitApp.application.externalCacheDir.absolutePath
                ?: throw Throwable("application is null")
            return "$cacheDir/download/"
        }
    /**下载进度更新频率，即下载多少B之后更新一次，默认4K。使用[PROGRESS_BY_PERCENT]表示按百分比更新*/
    var progressStep = 1024 * 4
    /**重试配置*/
    var retry = RetryConfig()
    /**head信息*/
    var headers: Headers? = null
}
```

如果需要修改全局配置，新建DownConfig类，继承自DefaultDownConfig类：

```
class DownConfig :DefaultDownConfig(){
    /**保存的文件夹路径，如果不设置，默认路径是"应用缓存路径/download/",如果设置为外部路径，需要自己确保有读写权限*/
    override var saveDir: String = ""
        get() {
            if (field.isNotEmpty()) return field
            val cacheDir = RxRetrofitApp.application.externalCacheDir?.absolutePath
                ?: throw Throwable("application is null")
            return "$cacheDir/download/"
        }
    /**下载进度更新频率，即下载多少B之后更新一次，默认4K。使用[PROGRESS_BY_PERCENT]表示按百分比更新*/
    override var progressStep = 1024 * 4
    /**重试配置*/
    override var retry = RetryConfig()
    /**head信息*/
    override var headers: Headers? = null
}
```

然后在Application中设置即可：

```
RxRetrofitApp.apply {
    ...
    downConfig = DownConfig()
}
```

### 6.Http下载文件单独配置

单独配置与全局配置的可配置参数相同。使用示例：

```
HttpDownManager.down(DownConfig().apply {
    url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    saveDir = "${RxRetrofitApp.application.externalCacheDir?.absolutePath}/download/"
    /**保存的文件名字，如果不设置，默认名字是url的最后一段*/
    saveFileName = "big_buck_bunny.mp4"
    /**下载进度更新频率，即下载多少B之后更新一次，使用[PROGRESS_BY_PERCENT]表示按百分比更新*/
    /**进度更新频率，下载多少Byte后更新一次进度。默认每下载4KB更新一次，使用[DownConfig.PROGRESS_BY_PERCENT]表示每下载百分之一更新一次*/
    progressStep = 1024 * 128
    /**重试配置*/
    retry = RetryConfig()
    /**head信息*/
    headers = Headers.of(mapOf("name1" to "value1", "name2" to "value2"))
})
```

## Bug 反馈

如果您有任何反馈或建议，欢迎提交到 [Github issues](https://github.com/wkxjc/HttpManager/issues).

## LICENSE

    Copyright (c) 2019-present, Alpinist Wang Contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.