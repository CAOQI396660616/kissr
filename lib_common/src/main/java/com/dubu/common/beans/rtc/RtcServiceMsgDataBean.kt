package com.dubu.common.beans.rtc

import com.squareup.moshi.JsonClass


/*
* 自定义游戏消息
* */
@JsonClass(generateAdapter = true)
data class RtcServiceMsgDataBean(
    var type: String? = "", //消息的类型
    val serviceMenuName: String? = "",//服务名称


    val fromNickName: String? = "", // 发送者昵称
    val toNickName: String? = "", // 接收者昵称

    val fromAvatar: String? = "", // 发送者头像
    val toAvatar: String? = "", // 接收者头像

    val from: String? = "", //发送者id
    val to: String? = "", // 接收者id
)
