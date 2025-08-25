package com.dubu.common.beans.rtc

import com.squareup.moshi.JsonClass


/*
* 首页顶部轮播视频对应的数据
* */
@JsonClass(generateAdapter = true)
data class RtcTextMsgDataBean(
    var type: String? = "", //消息的类型
    var text: String? = "", //消息文本



    val fromNickName: String? = "", // 发送者昵称
    val toNickName: String? = "", // 接收者昵称

    val fromAvatar: String? = "", // 发送者头像
    val toAvatar: String? = "", // 接收者头像

    val from: String? = "", //发送者id
    val to: String? = "", // 接收者id

)

