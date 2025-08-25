package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KolUnionListBean(
    val id: Long ? = 0L,
    val cover: String? = "",
//    val locale: String? = "",
    val status: String? = "",
//    val member_count: Long ? = 0L,
    val name: String? = "",
    val approved_at: String? = "",
    val withdraw_record: ExitUnionInfo? = null,

    val member: MemberInfo? = null,
)

/*
"id":1.0,
"name":"测试公会",
"cover":"/uploads/images/default-union-cover.jpg",
"locale":"中国",
"member_count":3.0
*/