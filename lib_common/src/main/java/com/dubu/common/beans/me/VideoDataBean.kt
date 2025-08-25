package com.dubu.common.beans.me

import com.squareup.moshi.JsonClass


/**
 *
 * 后台视频发布接口对应的参数bean
 * @author cq
 * @date 2025/06/26
 * @constructor 创建[VideoDataBean]
 * @param [video]
 * @param [cover]
 */
@JsonClass(generateAdapter = true)
data class VideoDataBean(
    var video: String? = "",
    var cover: String? = "",

)

