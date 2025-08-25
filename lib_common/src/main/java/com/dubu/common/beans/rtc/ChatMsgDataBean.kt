package com.dubu.common.beans.rtc

import com.squareup.moshi.JsonClass


/*
* 首页顶部轮播视频对应的数据
* */
@JsonClass(generateAdapter = true)
data class ChatMsgDataBean(
    var msgJson: String? = "",//消息的json
    val msgType: String? = "",//消息的类型
)

