package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadResultBean(
    var url: String? = "",
    var path: String? = "",
    var cover_path: String? = "",
)

