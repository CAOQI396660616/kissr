package com.dubu.common.beans.config

import com.dubu.common.beans.apk.AppConfigBean
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class JsonList(
    val country_list: MutableList<CountryInfo>? =null,
    val language_list: MutableList<LanguageInfo>? =null,
    val question_list: MutableList<QuestionInfo>? =null,

    @Json(name = "system_setting_config")
    val appConfigBean: AppConfigBean? =null,
)

@JsonClass(generateAdapter = true)
data class CountryInfo(
    val country_code: String? ="",
    val country_image: String? ="",
    val country_name: String? ="",
)

@JsonClass(generateAdapter = true)
data class LanguageInfo(
    var country_code: String? ="",
    var is_current: String? ="",
    var name: String? ="",
    var native_name: String? ="",

    var isSelected:Boolean? = false //本地字段
)

@JsonClass(generateAdapter = true)
data class QuestionInfo(
    val question: String? ="",
    val answer: String? ="",
)
