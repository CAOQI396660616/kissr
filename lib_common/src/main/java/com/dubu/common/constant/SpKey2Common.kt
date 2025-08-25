package com.dubu.common.constant

/**
 *  @author  Even
 *  @date   2021/11/15
 *  sp key 常量类
 */
object SpKey2Common {

    //user登录相关
    const val CURRENT_USER_INFO = "current_user_info"
    const val LAST_USER_SN = "last_user_id"
    const val CURRENT_USER_SN = "cur_user_sn"
    const val CURRENT_USER_TOKEN = "current_user_token"
    const val CURRENT_USER_TOKEN_TIME = "current_user_token_time"
    const val CURRENT_USER_APP_LANG = "CURRENT_USER_APP_LANG"

    //当前环境  release:true debug:false
    const val ENVIRONMENT_RELEASE = "net_environment"

    //设备id
    const val DEVICE_ID = "device_id_"
    //设备id相关
    const val DEVICE_OPT_ID = "device_id_"
    const val DEVICE_OPT_ID_RECORD = "device_id_record"
    const val ANDROID_OPT_ID_RECORD = "android_id_record"


    //20250528 allen 关于用户最新使用的表情
    const val LAST_USER_CLICK_EMOJI_LIST = "last_user_click_emoji_list"

    //重要设置之4
    //1 价格
    const val IMPORT_SET_PRICES = "IMPORT_SET_PRICES"
    //2 通知
    const val IMPORT_SET_NOTIFY = "IMPORT_SET_NOTIFY"
    //3 擅长的语言
    const val IMPORT_SET_LANGUAGE = "IMPORT_SET_LANGUAGE"
    //4 服务菜单
    const val IMPORT_SET_SERVICE = "IMPORT_SET_SERVICE"


    //互动打招呼 设置
    const val IMPORT_SET_INTERACTIVE = "IMPORT_SET_INTERACTIVE"
}