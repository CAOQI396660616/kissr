package com.dubu.common.beans.common

import com.dubu.common.beans.UserBean
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class LoginSuccessBean(

    @Json(name = "access_token")
    val accessToken: String = "",

    @Json(name = "refresh_token")
    val refreshToken: String = "",

    val user: UserBean,

    val sign: String = "",

    )