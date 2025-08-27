package com.dubu.common.beans.home

import com.squareup.moshi.JsonClass


/*
* 首页顶部轮播视频对应的数据
* */
@JsonClass(generateAdapter = true)
data class HomeTopVideoBean(
    val videoLink: String? = "",//视频播放地址
    val videoFrame: String? = "",//视频首帧图
    val videoTitle: String? = "",//视频标题
    var userChoose: Boolean? = false,//用户选中了 表示 做ui的变化
    var userEdit: Boolean? = false,//用户开启了编辑模式
)

