package com.dubu.rtc.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommonPageBean<T>(

    val current_page: Int? = 0,

    val to: Int? = 0,
    val from: Int? = 0,
    val total: Int? = 0,
    val next_page_url: String? = "",

    val data: List<T>? = null
)