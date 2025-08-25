package com.dubu.common.beans.base

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponseBean<T>(
    val code: Int = -1,
    val msg: String?="",
//    val time: String? = "",
//    val is_encrypt :Boolean = true,
    val data: T? = null
)