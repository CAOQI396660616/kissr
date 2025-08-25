package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KolServiceBean(
    val gold_price: Int? = 0,
    val id: Long? = 0,
    val kol_id: Long? = 0,
    val service_name: String? = "",
//    val status: Int? = 0,
    val weight: Int? = 0,
)
