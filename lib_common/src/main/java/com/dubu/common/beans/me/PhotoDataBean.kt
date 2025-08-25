package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass


/*
* 首页顶部轮播视频对应的数据
* */
@JsonClass(generateAdapter = true)
data class PhotoDataBean(
    var image: String? = "",
    var type: Int? = 0, // 0标识用户选择的本地图片 1标识是后台返回用户早就上传过的主页背景图
)

