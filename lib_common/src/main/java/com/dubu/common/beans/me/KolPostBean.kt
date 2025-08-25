package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KolPostBean(

    val id: Long? = 0,
    val kol_id: Long? = 0,
//    val status: Int? = 0,

    val audit_status: Int? = 0, //0审核中   1通过   2拒绝  不传递这个字段则是获取全部
    val comment: String? = "",
    val files: String? = "",
    val type: String? = "",
    val is_top: Int? = 0,//0未置顶   1置顶

    //本地字段
    var isSelected:Boolean? = false
)

/*

 {
"id":7,
"kol_id":10,
"type":"video",
"files":"https:\/\/fichat-dev.tos-cn-guangzhou.volces.com\/uploads\/20250619\/6853c43b5cdb7.jpeg%2Cuploads\/20250619\/6853c43c78556.png?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTNDgxYjQ0OWYxYTk0NGEwZGE2N2UzMmE5NDFhMzliMTE%2F20250620%2Fcn-guangzhou%2Ftos%2Frequest&X-Tos-Date=20250620T082637Z&X-Tos-Expires=2592000&X-Tos-SignedHeaders=host&X-Tos-Signature=7d4ab9ad1aed71a10e139800c8915b7c68d83f1ad0be20645a06f46e7380cb8b",
"comment":"",
"audit_status":1,
"status":1,
"created_at":"2025-06-19T00:59:35.000000Z",
"updated_at":"2025-06-19T08:03:11.000000Z"
}

*/