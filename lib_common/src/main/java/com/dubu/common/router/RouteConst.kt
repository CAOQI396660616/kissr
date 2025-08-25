package com.dubu.common.router

/**
 * Author:v
 * Time:2022/10/17
 */
object RouteConst {

    //--------------------path-------------------------


    /* ----------------------------------------------------------------- 数据传递参数名称*/
    const val P_TITLE = "intent_title"
    const val P_URL = "intent_url"
    const val P_INDEX = "intent_index"
    const val P_JSON = "intent_json"
    const val P_TYPE = "intent_type"

    const val P_ID = "intent_id"
    const val P_NAME = "intent_name"
    const val P_AVATAR = "intent_avatar"

    /* ----------------------------------------------------------------- APP 路由*/
    const val ACTIVITY_LOGIN = "/main/LoginActivity"
    const val ACTIVITY_SPLASH = "/main/SplashActivity"
    const val ACTIVITY_MAIN = "/main/MainActivity"
    const val ACTIVITY_SIMPLE_WEB = "/web/SimpleWebActivity"
    const val ACTIVITY_UNION_WEB = "/web/WebViewUnionActivity"
    const val ACTIVITY_TEST = "/test/TestActivity"
    /* ----------------------------------------------------------------- APP 路由*/


    /* ----------------------------------------------------------------- HOME 路由*/
    private const val HOME = "/home"

    const val ACTIVITY_SEARCH = "$HOME/SearchActivity"
    /* ----------------------------------------------------------------- HOME 路由*/


    /* ----------------------------------------------------------------- HOME 路由*/
    private const val SHORT = "/short"

    const val ACTIVITY_SHORT_DETAILS_PLAY = "$SHORT/ShortDetailsPlayActivity"
    /* ----------------------------------------------------------------- HOME 路由*/

    /* ----------------------------------------------------------------- MINE 路由*/
    private const val MINE = "/mine"

    const val ACTIVITY_MINE_PROFILE = "$MINE/MeProfileActivity"
    const val ACTIVITY_MINE_EDIT_PROFILE = "$MINE/MyEditProfileActivity"
    const val ACTIVITY_MINE_WALLET = "$MINE/MyWalletActivity"
    const val ACTIVITY_MINE_SETTINGS = "$MINE/MySettingsActivity"
    const val ACTIVITY_MINE_INCOME = "$MINE/MyIncomeActivity"
    const val ACTIVITY_MINE_UNION_HISTORY = "$MINE/MyUnionHistoryActivity"

    const val ACTIVITY_MINE_INIT = "$MINE/MyInitActivity"
    const val ACTIVITY_MINE_PUBLISH = "$MINE/MyPublishActivity"
    const val ACTIVITY_MINE_PUBLISH_VIDEO = "$MINE/MyPublishVideoActivity"
    const val ACTIVITY_MINE_PUBLISH_PHOTO = "$MINE/MyPublishPhotoActivity"
    const val ACTIVITY_MINE_PHOTO_LIST = "$MINE/MyPhotoListActivity"
    const val ACTIVITY_MINE_IMPORTANT_SETTING = "$MINE/MyImportantSettingActivity"

    /* ----------------------------------------------------------------- MINE 路由*/



    /* ----------------------------------------------------------------- VIDEO 路由*/
    private const val VIDEO = "/video"

    const val ACTIVITY_RTV_CALL = "$VIDEO/RtcCallActivity"
    const val ACTIVITY_RTV_EFFECT = "$VIDEO/RtcEffectActivity"
    const val ACTIVITY_CALL_INVITE = "$VIDEO/CallInviteActivity"
    const val ACTIVITY_CALL_OVER = "$VIDEO/CallOverActivity"
    const val ACTIVITY_BIM_CHAT = "$VIDEO/BimChatActivity"
    const val ACTIVITY_BIM_CHAT_SYS = "$VIDEO/SystemMsgActivity"
    const val ACTIVITY_VIDEO_PLAY = "$VIDEO/VideoPlayActivity"
    const val ACTIVITY_SYS_CASH = "$VIDEO/SystemMsgCashDetailActivity"
    const val ACTIVITY_IMAGE_PRE = "$VIDEO/ImagePreviewChatActivity"
    /* ----------------------------------------------------------------- VIDEO 路由*/
}
