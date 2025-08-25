package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GiftNBean(

    val id: Long? = 0,
//    val status: Int? = 0,
    val type: String? = "",
//    val echelon_code: String? = "",
    val price: Long? = 0,
    val template: GiftTemplateInfo,
//    val template_code: String? = "",
//    val weight: Long? = 0,
)

@JsonClass(generateAdapter = true)
data class GiftTemplateInfo(
    val cover: String? = "",
    val id: Long? = 0,
//    val image: List<String>? = null,
    val path: String? = "",
//    val status: Int? = 0,
//    val template_code: String? = "",
//    val template_gift_en_name: String? = "",
    val template_gift_name: String? = "",
//    val template_name: String? = "",
//    val weight: Long? = 0,
)

/*
{
"id":1,
"echelon_code":"T1",
"template_code":"huojian",
"price":100,
"type":"normal",
"weight":0,
"status":1,
"created_at":null,
"updated_at":null,
"template":{
    "id":1,
    "template_code":"huojian",
    "template_name":"\u706b\u7bad",
    "template_gift_name":"\u706b\u7bad",
    "template_gift_en_name":"rocket",
    "image":[
        "https:\/\/fichat-dev.tos-cn-guangzhou.volces.com\/upload\/20250619\/rocket.jpeg?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTNDgxYjQ0OWYxYTk0NGEwZGE2N2UzMmE5NDFhMzliMTE%2F20250623%2Fcn-guangzhou%2Ftos%2Frequest&X-Tos-Date=20250623T030053Z&X-Tos-Expires=2592000&X-Tos-SignedHeaders=host&X-Tos-Signature=95c6f8ffc27572f47214137fe77b486b4703dead0dd1267186c456031fd48962"
    ],
    "cover":"https:\/\/fichat-dev.tos-cn-guangzhou.volces.com\/upload\/20250619\/rocket_cover.jpeg?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTNDgxYjQ0OWYxYTk0NGEwZGE2N2UzMmE5NDFhMzliMTE%2F20250623%2Fcn-guangzhou%2Ftos%2Frequest&X-Tos-Date=20250623T030053Z&X-Tos-Expires=2592000&X-Tos-SignedHeaders=host&X-Tos-Signature=9d3f873ee74a760ec0a8aead739554dcf716493411383ca703baf2ca54ece08e",
    "path":"https:\/\/fichat-dev.tos-cn-guangzhou.volces.com\/upload\/20250619\/rocket_cover.svg?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTNDgxYjQ0OWYxYTk0NGEwZGE2N2UzMmE5NDFhMzliMTE%2F20250623%2Fcn-guangzhou%2Ftos%2Frequest&X-Tos-Date=20250623T030053Z&X-Tos-Expires=2592000&X-Tos-SignedHeaders=host&X-Tos-Signature=c3c5ee13afcb8c9d2e2a2265af1b6c62892f01d03f5f806ecd65391e4acc3005",
    "status":1,
    "weight":0,
    "created_at":null,
    "updated_at":null
    }
}
*/