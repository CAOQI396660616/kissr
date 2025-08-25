package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KolUnionInfoBean(
    val member: MemberInfo? = null,
    val role: String? = "",
    val union: KolUnionListBean? = null
)

@JsonClass(generateAdapter = true)
data class MemberInfo(
    val id: Long? = 0L,
    val role: String? = "",
    var status: String? = "", //approved pending rejected   通过 待审核 拒绝
)
@JsonClass(generateAdapter = true)
data class ExitUnionInfo(
    val union_id: Long? = 0L,
    val user_id: Long? = 0L,
    val status: String? = "", //approved pending rejected   通过 待审核 拒绝
)


/*
{
        "union": {
            "id": 1,
            "name": "测试公会",
            "cover": "/uploads/images/union-cover.jpg",
            "locale": "中国",
            "member_count": 5
        },
        "member": {
            "id": 1,
            "status": "approved",
            "role": "member",
            "total_income": 5000
        },
        "role": "member"
    }
*
* */