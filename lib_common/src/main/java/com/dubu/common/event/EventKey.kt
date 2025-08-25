package com.dubu.common.event

/**
 * Author:v
 * Time:2021/9/13
 */
object EventKey {

    /*
    * 通用
    * */
    const val TOKEN_INVALIDED = "token_invalided"
    const val LOGIN_SUCCESS = "login_success"
    const val LOGIN_OUT = "login_out"

    /*
    * 测试key 主要用于通讯测试
    * */
    const val ACTION_TEST = "TEST_TEST"

    /**
     * 播放svga
     */
    const val VOICE_ROOM_SVGA_PLAY = "voice_room_svga_play"
    const val ON_USER_STOP_VIDEO_CAPTURE = "onUserStopVideoCapture"
    const val ON_USER_START_VIDEO_CAPTURE = "onUserStartVideoCapture"

    const val MSG_UN_READ = "MSG_UN_READ" //未读消息
    const val MSG_HIDE = "MSG_HIDE" // web端拒绝接通视频以后 需要消失页面
    const val PAGE_NEED_HIDE = "PAGE_NEED_HIDE" // 当进入直播页面的时候 某一些页面需要重置 消失


    /*
    * 相册AC传递给FG进入什么动作
    * */
    const val ACTION_ALL_SELECTED = "ACTION_ALL_SELECTED" // 相册全选动作
    const val ACTION_ENTER_TOP = "ACTION_ENTER_TOP"       // 相册置顶    这个是进入编辑置顶模式
    const val ACTION_ENTER_EDIT = "ACTION_ENTER_EDIT"     // 相册删除 这个是进入删除模式
    const val ACTION_OVER = "ACTION_OVER"                 // 相册完成
    const val ACTION_REFRESH = "ACTION_REFRESH"           // 相册数据变化了需要刷新

    /*
    * 相册AC传递给FG用户真实的接口访问
    * */
    const val ACTION_DEL = "ACTION_DEL"                   // 相册删除
    const val ACTION_UN_TOP = "ACTION_UN_TOP"             // 相册取消置顶
    const val ACTION_TOP = "ACTION_TOP"                   // 相册置顶


    /*
    * 挂断用户电话上报
    * */
    const val ACTION_REJECT = "ACTION_REJECT"


    /*
    * Router
    * */

    //去往消息页面的 通话记录页面
    const val ROUTER_VIDEOS_HISTORY = "ROUTER_VIDEOS_HISTORY"
    const val ROUTER_VIDEOS_HISTORY_IN = "ROUTER_VIDEOS_HISTORY_IN"

    //测试key 后面可以删除
    const val IS_CAN_GOTO_TEST_UPDATE_APP = "is_Can_Goto_Test_Update_App"
}