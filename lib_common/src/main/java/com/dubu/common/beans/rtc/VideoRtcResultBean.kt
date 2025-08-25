package com.dubu.common.beans.rtc

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoRtcResultBean(
    val avatar: String? = "",
    val status: String? = "",
    val duration: Double?= 0.00,
    val end_time: String? = "",
    val nickname: String? = "",
    val room_id: String? = "",
    val start_time: String? = "",
    val user_id: Long? = 0L,
    val to_user_id: Long? = 0L,

    //在线离线
    @Json(name = "online_status")
    var onlineStatus: String? = "",// online offline
)
