package com.dubu.common.beans.common

import com.squareup.moshi.JsonClass


//des 礼物列表接口 + 自定义消息礼物 所需数据bean
@JsonClass(generateAdapter = true)
data class VoiceRoomEventBean(
    val channelSn: String? = "",//房间外显 id
    val rtcChannelId: String? = "",//房间rtc id
    val voiceRoomCover: String? = "",// vr 封面
    val isMuteVr: Int? = 0, //是否静音
)

