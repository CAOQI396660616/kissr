package com.dubu.common.beans.common

import com.squareup.moshi.JsonClass
import java.io.Serializable


@JsonClass(generateAdapter = true)
data class ReportBean(
    val id: Long? = 0L,
)


