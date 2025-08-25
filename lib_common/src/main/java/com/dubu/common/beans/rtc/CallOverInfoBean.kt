package com.dubu.common.beans.rtc

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class CallOverInfoBean(
    var duration: Int? = 0,//
    val total_earnings: String? = "",//
)

