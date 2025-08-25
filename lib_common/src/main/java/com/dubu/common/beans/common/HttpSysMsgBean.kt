package com.dubu.common.beans.common

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class HttpSysMsgBean(
    val id: Long? = 0L,
    val type:  String? = "",
    val updated_at:  String? = "",
    val extra_data: ExtraDataBean,
)

@JsonClass(generateAdapter = true)
data class ExtraDataBean(
    val kol_id: Long? = 0L,

    val content: String? = "",
    val kol_name: String? = "",
    val reason: String? = "",
    val status: String? = "",
)
