package com.example.httpmanager.commen.bean

/**
 * Description:
 * 壁纸
 *
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2019-05-06
 */
data class Wallpaper(
    val vertical: List<Vertical>
)

data class Vertical(
    val atime: Double,
    val cid: List<String>,
    val cr: Boolean,
    val desc: String,
    val favs: Int,
    val id: String,
    val img: String,
    val ncos: Int,
    val preview: String,
    val rank: Int,
    val rule: String,
    val store: String,
    val tag: List<Any>,
    val thumb: String,
    val url: List<Any>,
    val views: Int,
    val wp: String,
    val xr: Boolean
)