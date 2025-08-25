package com.dubu.common.beans.rtc

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.dubu.common.beans.common.HttpSysMsgBean
import com.squareup.moshi.JsonClass


data class ChatSysMsgMultiTypeBean(var type: Int, var data: HttpSysMsgBean) :
    MultiItemEntity {
    override val itemType: Int
        get() = type

    companion object {
        const val ITEM_TYPE_SYS_REPORT = 1201//系统消息: 举报消息
        const val ITEM_TYPE_SYS_REPORT_T = "complaint_audit"//系统消息: 举报消息
        const val ITEM_TYPE_SYS_CASH = 1202//系统消息: 提现消息
        const val ITEM_TYPE_SYS_CASH_T = "withdraw"//系统消息: 提现消息
    }
}

@JsonClass(generateAdapter = true)
data class HttpAllSysBean(
    val text: HttpSysBean? = null
)

@JsonClass(generateAdapter = true)
data class HttpSysBean(
    val content: String? = null,
    val msg: String? = null,
    val reason: String? = null,
    val status: String? = null,
    val timestamp: String? = null,
    val type: String? = null
)