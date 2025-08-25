package com.dubu.common.beans.rtc

// import com.bytedance.im.core.api.model.BIMMessage // 暂时注释掉，缺少字节跳动IM SDK依赖
import com.chad.library.adapter.base.entity.MultiItemEntity


data class ChatMsgMultiTypeBean(var type: Int, var data: Any) : // 暂时改为Any类型
    MultiItemEntity {
    override val itemType: Int
        get() = type

    companion object {
        const val ITEM_TYPE_TEXT_M = 11//文本消息 me
        const val ITEM_TYPE_TEXT_Y = 12//文本消息 you
        const val ITEM_TYPE_TEXT_T = "text"//文本消息 消息类型


        const val ITEM_TYPE_IMAGE_M = 21//图片消息 me
        const val ITEM_TYPE_IMAGE_Y = 22//图片消息 you
        const val ITEM_TYPE_IMAGE_T = "image"//图片消息 消息类型

        const val ITEM_TYPE_GIFT_M = 31//礼物消息 me
        const val ITEM_TYPE_GIFT_Y = 32//礼物消息 you
        const val ITEM_TYPE_GIFT_T = "gift"//礼物消息 消息类型

        const val ITEM_TYPE_GAME_M = 41//礼物消息 me
        const val ITEM_TYPE_GAME_Y = 42//礼物消息 you
        const val ITEM_TYPE_GAME_T = "gameLink"//礼物消息 消息类型

        const val ITEM_TYPE_HI = 51//打招呼 me
        const val ITEM_TYPE_HI_T = "greet"//打招呼 消息类型


        const val ITEM_TYPE_SYS_REPORT = 101//系统消息: 举报消息
        const val ITEM_TYPE_SYS_REPORT_T = "systemReport"//系统消息: 举报消息
        const val ITEM_TYPE_SYS_CASH = 102//系统消息: 提现消息
        const val ITEM_TYPE_SYS_CASH_T = "systemCash"//系统消息: 提现消息



        const val ITEM_TYPE_EMPTY = 1001//空的占位消息
        const val ITEM_TYPE_EMPTY_T = "empty"//空的占位消息
    }
}
