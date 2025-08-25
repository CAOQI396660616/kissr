package com.dubu.common.beans.me

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LanguageHttpBean(
    @Json(name = "language_code")
    val languageCode: String = "",
    @Json(name = "language")
    val language: String = ""
)


@JsonClass(generateAdapter = true)
data class LanguageHttpListBean(
    val list: List<LanguageHttpBean>? = null
)