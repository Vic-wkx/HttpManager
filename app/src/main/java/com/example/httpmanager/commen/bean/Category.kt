package com.example.httpmanager.commen.bean

/**
 * Description:
 * 壁纸分类
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-06
 */
data class Category(
    val category: List<CategoryX>
)

data class CategoryX(
    val atime: Double,
    val count: Int,
    val cover: String,
    val cover_temp: String,
    val ename: String,
    val filter: List<Any>,
    val icover: String,
    val id: String,
    val name: String,
    val picasso_cover: String,
    val rank: Int,
    val rname: String,
    val sn: Int,
    val type: Int
)