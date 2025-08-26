package com.dubu.common.beans

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UserBean(

    @Json(name = "user_id")
    var userSn: Long = 0,


    val sex: String? = "",
    val token: String? = "",
    val avatar: String? = "",
    val birthday: String? = "",
    val email: String? = "",
    val nickname: String? = "",

    @Json(name = "country_code")
    val countryCode: String? = "",
    @Json(name = "created_at")
    val createdAt: String? = "",
    @Json(name = "language_code")
    val languageCode: String? = "",
    @Json(name = "last_login_time")
    val lastLoginTime: String? = "",
    @Json(name = "member_expired_time")
    val memberExpiredTime: String? = "",
    @Json(name = "usage_purpose")
    val usagePurpose: String? = "",


    @Json(name = "remaining_trial_count")
    val remainingTrialCount: Int? = 0,
    @Json(name = "used_trial_count")
    val usedTrialCount: Int? = 0,
    @Json(name = "free_trial_count")
    val freeTrialCount: Int? = 0,
    @Json(name = "is_notification_enabled")
    val isNotificationEnabled: Int? = 0,

    @Json(name = "is_member")
    val isMember: Boolean? = false,

    )
/*
{
        "user_id": "57761805",
        "nickname": "Visitor",
        "avatar": "", // 头像URL
        "sex": "", // 性别： 0-未设置 1-男性, 2-女性, 3-其他
        "birthday": "", // 生日，格式：2000-01-01
         "usage_purpose": "", // 使用目的：0-未设置 1-与朋友聊天, 2-搭讪女孩, 3-玩社交平台
        "email": "",
        "is_member": false, // 是否会员
        "member_expired_time": null, // 会员过期时间
        "member_type": 0, // 会员类型: 1-周 2-月 3-季 4-年 5-两年 6-永久
        "third_party_id": "", // 第三方会员ID
        "third_party_type": 0, // 1- facebook 2-google 3-apple
        "free_trial_count": 5, // 免费试用的额度
        "used_trial_count": 0, // 已经用了多少额度
        "remaining_trial_count": 5, // 剩下多少额度
        "country_code": "US",
        "language_code": "en",
        "is_notification_enabled": true, // 是否开启通知
        "create_time": 1753754757,
        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJraXNzcl9hcHBfYXBpIiwiYXVkIjoia2lzc3JfYXBwX2NsaWVudCIsImlhdCI6MTc1Mzc3MTMwOCwibmJmIjoxNzUzNzcxMzA4LCJleHAiOjE3NTYzNjMzMDgsInVzZXJfaWQiOiI1Nzc2MTgwNSIsImp0aSI6Imp3dF82ODg4NmQyYzYzNDAwNC4yMjk0MDU5NSJ9.u1WLjW6NX5mEhRulNCW-fPiIO6TEw0bM7Ae0poo7nKU"
    }
* */






