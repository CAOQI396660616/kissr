package com.dubu.common.beans.base

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BasePageBean<T>(
    val pageSize: Int = 0,
    val totalCount: Int = 0,
    val totalPage: Int = 0,
    val pageNo: Int = 0,
    val data: List<T>? = null
)