package com.dubu.common.beans.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShareBean(

    @Json(name = "share_text")
    val shareText: String? = "",
)
