package com.dubu.common.beans.rtc

import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * Author:v
 * Time:2022/12/30
 */
@JsonClass(generateAdapter = true)
data class MessageBannerBean(
    var userSn: Long? = 0L,
    var nickname: String? = null,
    var message: String? = null,
    var avatar: String? = null,
    var gender: Int? = 0,
    var birthday: String? = null,
    var age: Int? = 0,

    ) : Serializable



@JsonClass(generateAdapter = true)
data class RTCCallMsg(
    val message: String? = null,
    val userId: String? = null,
)

@JsonClass(generateAdapter = true)
data class RTCCallMsgBean(
    val `data`: RTCCallMsgData? = null,
    val type: String? = null
)


@JsonClass(generateAdapter = true)
data class RTCCallMsgData(
    val fromNickname: String? = null,
    val fromAvatar: String? = null,
    val email: String? = null,
    val roomStateId: String? = null,
    val gender: String? = null,
    val fromAge: Int? = 0,

    val content: String? = null,

    val from: String? = null,
    val to: String? = null,
    val roomId: String? = null,

    val fromCountryImage: String? = null,
    val fromCountryCode: String? = null,



    )

/*

    val fromAvatar: String,
    val fromNickname: String,
    val toAge: Int,
    val toAvatar: String,
    val toCountryCode: String,
    val toCountryImage: String,
    val toGender: String,
    val toNickname: String,
    val video_gold_price: Int
* */












































