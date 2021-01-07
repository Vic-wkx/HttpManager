package com.example.httpmanager.commen.bean

data class Articles(
    val curPage: Int = 0,
    val datas: MutableList<ArticleBean> = mutableListOf(),
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0
) {
    fun refresh(articles: Articles) {
        datas.clear()
        datas.addAll(articles.datas)
    }

    fun addMore(articles: Articles) {
        datas.addAll(articles.datas)
    }
}