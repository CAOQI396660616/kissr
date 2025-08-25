package com.dubu.common.beans.rtc

import com.chad.library.adapter.base.entity.MultiItemEntity


data class RtcCallMsgMultiTypeBean(var type: Int, var data: RtcCallMsgDataBean) :
    MultiItemEntity {
    override val itemType: Int
        get() = type

    companion object {
        const val ITEM_TYPE_TEXT = 0//文本消息
        const val ITEM_TYPE_TEXT_T = "text"//文本消息

        const val ITEM_TYPE_GIFT = 1//礼物消息
        const val ITEM_TYPE_GIFT_T = "gift"//礼物消息

        const val ITEM_TYPE_GAME = 2//游戏消息
        const val ITEM_TYPE_GAME_T = "gameLink"//礼物消息 消息类型

        const val ITEM_TYPE_SERVER = 3//点播消息
        const val ITEM_TYPE_SERVER_T = "serviceMenu"//礼物消息 消息类型


    }
}
