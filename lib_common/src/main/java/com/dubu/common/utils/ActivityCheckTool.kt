package com.dubu.common.utils

import android.app.Activity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.router.RouteConst

/**
 * Author:Allen
 * Time:2022/10/21
 *
 *  管理页面Top activity 的判断
 *
 *  主要是用来判断 什么样的 顶部activity 不需要弹下拉消息
 */
object ActivityCheckTool {
    @JvmStatic
    fun isActivityCanShowMsgBanner(activity: Activity, userSn: Long?): Boolean {

        HiLog.e(Tag2Common.TAG_12300, "isActivityCanShowMsgBanner key = ${activity.title} = ${activity.localClassName}")
        if (activity.title == RouteConst.ACTIVITY_RTV_CALL) return false
        if (activity.title ==  RouteConst.ACTIVITY_CALL_INVITE) return false

        return true
    }

    @JvmStatic
    fun isActivityCanPlayCallRolling(activity: Activity, userSn: Long?): Boolean {
        HiLog.e(Tag2Common.TAG_12300, "isActivityCanPlayCallRolling key = ${activity.title} = ${activity.localClassName}")
        if (activity.title == RouteConst.ACTIVITY_RTV_CALL) return false
        return true
    }


    fun isActivityCanShowVoiceCall(title: String): Boolean {
        return true
    }

}