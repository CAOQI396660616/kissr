package com.dubu.common.beans.base

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class EventSimple<T>(
    val data: T? = null,
    val key: String? = "",
    val state: Int? = 0,
    val disposable: Boolean? = false,
)