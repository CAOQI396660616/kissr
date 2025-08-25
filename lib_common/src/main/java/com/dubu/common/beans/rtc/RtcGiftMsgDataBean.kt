package com.dubu.common.beans.rtc

import com.squareup.moshi.JsonClass


/*
* 自定义礼物消息
* */
@JsonClass(generateAdapter = true)
data class RtcGiftMsgDataBean(
    var type: String? = "", //消息的类型
    val animationUrl: String? = "",
    var giftCount: String? = "",
    val giftId: String? = "",
    val giftName: String? = "",
    val imageUrl: String? = "",


    val fromNickName: String? = "", // 发送者昵称
    val toNickName: String? = "", // 接收者昵称

    val fromAvatar: String? = "", // 发送者头像
    val toAvatar: String? = "", // 接收者头像

    val from: String? = "", //发送者id
    val to: String? = "", // 接收者id
)
