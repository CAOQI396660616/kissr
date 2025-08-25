package com.dubu.common.beans.rtc

import com.squareup.moshi.JsonClass


/**
 *
 * 后台发送推送消息 : 打招呼
 *
 * @author cq
 * @date 2025/08/08
 * @constructor 创建[RtcHttpHiMsgDataBean]
 * @param [type]
 * @param [text]
 * @param [videoUrl]
 * @param [imgUrl]
 * @param [today_count]
 * @param [target_user_id]
 * @param [target_user_nickname]
 * @param [target_user_avatar]
 */
@JsonClass(generateAdapter = true)
data class RtcHttpHiMsgDataBean(
    var type: String? = "", //消息的类型  后台的 auto_hello 对应我们前台的 greet
    var text: String? = "", //消息文本
    var videoUrl: String? = "", // 视频地址
    var imgUrl: String? = "", // 图片地址
    var today_count: Int? = 0, // 发送累计次数
    var target_user_id: Long? = 0L, // 需要发送的用户id
    var target_user_nickname: String? = "", // 需要发送的用户昵称
    var target_user_avatar: String? = "", // 需要发送的用户头像

)



