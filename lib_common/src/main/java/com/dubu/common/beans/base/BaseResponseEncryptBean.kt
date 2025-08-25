package com.dubu.common.beans.base

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponseEncryptBean(
    val code: Int = -1,
    val message: String? = "",
//    val time: String? = "",
//    val is_encrypt :Boolean = true,
    val data: Any?
)