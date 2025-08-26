package com.dubu.common.router

import android.content.Intent
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.dubu.common.constant.Tag2Common
import com.dubu.common.utils.HiLog

/**
 * Author:v
 * Time:2022/10/17
 */
object Router {

    fun toSplashActivity() {
        val p = ARouter.getInstance().build(RouteConst.ACTIVITY_SPLASH)
            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        p.navigation()
    }

    fun toLoginActivity(block: (Postcard.() -> Unit)? = null) {
        val p = ARouter.getInstance().build(RouteConst.ACTIVITY_LOGIN)
            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        block?.let { p.apply(it) }
        p.navigation()
    }

    /**
     * 跳转到登录弹框页面
     */
    fun toLoginDialogActivity(json: String, type: Int) {
        HiLog.e(Tag2Common.TAG_12300, "toLoginDialogActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_LOGIN_DIALOG)
            .withString(RouteConst.P_JSON, json)
            .withInt(RouteConst.P_TYPE, type)
            .navigation()
    }

    fun toMainActivity(block: (Postcard.() -> Unit)? = null) {
        val p = ARouter.getInstance().build(RouteConst.ACTIVITY_MAIN)
            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        block?.let { p.apply(it) }
        p.navigation()
    }


    //用户 1v1打视频 直播页面
    fun toRtcCallActivity(json: String, type: Int) {
        HiLog.e(Tag2Common.TAG_12300, "toRtcCallActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_RTV_CALL)
            .withString(RouteConst.P_JSON, json)
            .withInt(RouteConst.P_TYPE, type)
            .navigation()
    }
    fun toRtcEffectActivity(json: String, type: Int) {
        HiLog.e(Tag2Common.TAG_12300, "toRtcEffectActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_RTV_EFFECT)
            .withString(RouteConst.P_JSON, json)
            .withInt(RouteConst.P_TYPE, type)
            .navigation()
    }

    fun toCallInviteActivity(json: String, type: Int) {
        HiLog.e(Tag2Common.TAG_12300, "toCallInviteActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_CALL_INVITE)
            .withString(RouteConst.P_JSON, json)
            .withInt(RouteConst.P_TYPE, type)
            .navigation()
    }

    fun toCallOverActivity(json: String, type: Int) {
        HiLog.e(Tag2Common.TAG_12300, "toCallOverActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_CALL_OVER)
            .withString(RouteConst.P_JSON, json)
            .withInt(RouteConst.P_TYPE, type)
            .navigation()
    }

    fun toWebViewActivity(title: String, url: String) {
        HiLog.e(Tag2Common.TAG_12300, "toWebViewActivity  json = $title = $url")
        ARouter.getInstance().build(RouteConst.ACTIVITY_SIMPLE_WEB)
            .withString(RouteConst.P_TITLE, title)
            .withString(RouteConst.P_URL, url)
            .navigation()
    }

    fun toWebViewUnionActivity(title: String, url: String) {
        HiLog.e(Tag2Common.TAG_12300, "toWebViewUnionActivity  json = $title = $url")
        ARouter.getInstance().build(RouteConst.ACTIVITY_UNION_WEB)
            .withString(RouteConst.P_TITLE, title)
            .withString(RouteConst.P_URL, url)
            .navigation()
    }


    //用户  聊天页面
    fun toBimChatActivity(conversationID: String, type: Int , json: String? = "") {
        HiLog.e(Tag2Common.TAG_12300, "toBimChatActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_BIM_CHAT)
            .withString(RouteConst.P_ID, conversationID)
            .withInt(RouteConst.P_TYPE, type)
            .withString(RouteConst.P_JSON, json)
            .navigation()
    }

    fun toTestActivity(json: String, type: Int) {
        HiLog.e(Tag2Common.TAG_12300, "toTestActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_TEST)
            .withString(RouteConst.P_JSON, json)
            .withInt(RouteConst.P_TYPE, type)
            .navigation()
    }

    fun toSystemMsgActivity(json: String, type: Int) {
        HiLog.e(Tag2Common.TAG_12300, "toSystemMsgActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_BIM_CHAT_SYS)
            .withString(RouteConst.P_JSON, json)
            .withInt(RouteConst.P_TYPE, type)
            .navigation()
    }

    fun toVideoPlayActivity(json: String, type: Int) {
        HiLog.e(Tag2Common.TAG_12300, "toVideoPlayActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_VIDEO_PLAY)
            .withString(RouteConst.P_JSON, json)
            .withInt(RouteConst.P_TYPE, type)
            .navigation()
    }

    fun toSystemMsgCashDetailActivity(json: String, type: Int) {
        HiLog.e(Tag2Common.TAG_12300, "toSystemMsgCashDetailActivity  json = $json")
        ARouter.getInstance().build(RouteConst.ACTIVITY_SYS_CASH)
            .withString(RouteConst.P_JSON, json)
            .withInt(RouteConst.P_TYPE, type)
            .navigation()
    }

    fun toImagePreviewChatActivity(image: String) {
        ARouter.getInstance().build(RouteConst.ACTIVITY_IMAGE_PRE)
            .withString(RouteConst.P_JSON, image)
            .navigation()
    }

    /*
    *
    * 下面的都是遗留代码 暂时留作测试
    *
    * */


    fun toSearchShortActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_SEARCH)
            .navigation()
    }

    fun toShortDetailsPlayActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_SHORT_DETAILS_PLAY)
            .navigation()
    }

    fun toMeProfileActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_PROFILE)
            .navigation()
    }

    fun toMeEditProfileActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_EDIT_PROFILE)
            .navigation()
    }

    fun toMyWalletActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_WALLET)
            .navigation()
    }

    fun toMySettingsActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_SETTINGS)
            .navigation()
    }
    fun toMyIncomeActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_INCOME)
            .navigation()
    }

    fun toMyUnionHistoryActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_UNION_HISTORY)
            .navigation()
    }


    fun toMyInitActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_INIT)
            .navigation()
    }

    fun toMyPublishActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_PUBLISH)
            .navigation()
    }

    fun toMyPublishVideoActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_PUBLISH_VIDEO)
            .navigation()
    }

    fun toMyPublishPhotoActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_PUBLISH_PHOTO)
            .navigation()
    }

    fun toMyPhotoListActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_PHOTO_LIST)
            .navigation()
    }

    fun toMyImportantSettingActivity() {
        ARouter.getInstance()
            .build(RouteConst.ACTIVITY_MINE_IMPORTANT_SETTING)
            .navigation()
    }
}