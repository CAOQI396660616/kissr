package com.dubu.common.beans.rtc

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RtcHiMsgDataBean(
    var type: String? = "", //消息的类型  greet
    var text: String? = "", //消息文本
    var videoUrl: String? = "", // 视频地址
    var imageUrl: String? = "", // 图片地址
    var limitPushCount : Int? = 0, //web端需要的参数计数


    val fromNickName: String? = "", // 发送者昵称
    val toNickName: String? = "", // 接收者昵称

    val fromAvatar: String? = "", // 发送者头像
    val toAvatar: String? = "", // 接收者头像

    val from: String? = "", //发送者id
    val to: String? = "", // 接收者id

)

