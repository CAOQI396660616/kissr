package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KolUnionHistoryListBean(
    val created_at: String? = "",
    val status: String? = "",
    val updated_at: String? = "",
    val union: KolUnionListBean? = null,
)


/*
   {
"id":11,
"union_id":5,
"user_id":10841,
"kol_id":71,
"activity_type":"join",
"status":"approved",
"description":"\u52a0\u5165\u516c\u4f1a",
"reason":null,
"activity_time":"2025-08-20T07:54:43.000000Z",
"created_at":"2025-08-20T07:54:43.000000Z",
"updated_at":"2025-08-20T07:54:43.000000Z"
}
*/