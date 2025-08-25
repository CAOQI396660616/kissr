package com.dubu.common.beans

import com.dubu.common.beans.config.CountryInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UserBean(

    @Json(name = "id")
    var userSn: Long = 0,

    @Json(name = "kol_id")
    var kolId: Long = 0,

    var avatar: String? = "",

    @Json(name = "token")
    var userToken: String? = "",

    val gender: String? = "",

    var name: String? = "",
    var nickname: String? = "",
    var age: Int = 0,
    val email: String? = "",

    @Json(name = "created_at")
    val createdAt: String? = "",

    @Json(name = "updated_at")
    val updatedAt: String? = "",

    val uuid: String? = "",

    @Json(name = "invite_code")
    val inviteCode: String? = "",

    var birthday: String? = "",

    @Json(name = "country_code")
    var countryCode: String? = "",
    @Json(name = "lang_codes")
    var languageCodes: String? = "",

    /*
    * 下面是临时需要的音视频im相关数据  后面需要对接后台
    * */

    @Json(name = "im_token")
    var imToken: String? = "",

    @Json(name = "rtc_token")
    var rtcToken: String? = "",

    var images: List<String>? = null,

    var country: CountryInfo? = null,

    @Json(name = "is_fresh")
    var isNeedInit: Int? = 1,   //1标识新用户 需要填写资料


    //简介
    var info: String? = "",

    //在线离线
    @Json(name = "online_status")
    var onlineStatus: String? = "", // online offline

    @Json(name = "video_gold_price")
    var videoGoldPrice: Int? = 0, // 视频通话价格

    @Json(name = "service_status")
    var serviceStatus: Int? = 0, // 服务列表是否打开 0关 1开


    //本地字段
    var isClicked: Boolean = false,   //1标识用户 是否被点击 被选中

    //===============================
    @Json(name = "post_videos")
    var userVideo: MutableList<UserVideo>? = null,


    //视频接通次数
    @Json(name = "video_call_count")
    var videoCallCount: Long? = 0,

    //金币
    var money: Long? = 0,


    var vip: Int? = 0,   //1是vip



    //主播等级
    @Json(name = "kol_rank")
    var kolRank: String? = "",


    //member成员 owner会长
    @Json(name = "union_role")
    var unionRole: String? = "",

    /*
    #UITool
    const val UNION_STATUS_APPROVED       = "approved"        // 通过
    const val UNION_STATUS_REVIEW         = "pending"         // 审核中
    const val UNION_STATUS_REJECTED       = "rejected"        // 拒绝
    const val UNION_STATUS_EXIT_REVIEW        = "quit_pending"        // 退会中
    * */
    @Json(name = "union_status")
    var unionStatus: String? = "",
){
    /**
     * 判断用户是否未加入任何工会
     */
    fun isNotInUnion(): Boolean {
        // A. unionRole=null 且 unionStatus=null
        if (unionRole == null && unionStatus == null) {
            return true
        }
        // B. unionRole="member" 且 unionStatus="rejected"
        if (unionRole == "member" && unionStatus == "rejected") {
            return true
        }
        // 其他情况返回 false
        return false
    }

    /**
     * 是否加入公会待审核
     * 条件：unionRole == "member" 且 unionStatus == "pending"
     */
    fun isUnionJoinPending(): Boolean {
        return unionRole == "member" && unionStatus == "pending"
    }

    /**
     * 判断用户是否创建工会成功
     */
    fun isCreateUnionOK(): Boolean {
        // B. unionRole="member" 且 unionStatus="rejected"
        if (unionRole == "owner" && unionStatus == "approved") {
            return true
        }
        // 其他情况返回 false
        return false
    }
}




@JsonClass(generateAdapter = true)
data class UserVideo(
    val files: String? = "",
    val id: Long? = 0L,
    val is_top: Int? = 0,
    val kol_id: Long? = 0L,
    val type: String? = ""
)




