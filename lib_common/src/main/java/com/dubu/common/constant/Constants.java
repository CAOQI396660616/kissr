package com.dubu.common.constant;


/*
*
* rtc 相关的本地化数据
* */
public class Constants {

    //测试时候所需的 app id 和秘钥   火山音视频

    /*
    //国内版本
    火山音视频
    public static final String RTC_APP_ID = "67bee1ff570e09017bb7970b";
    public static final String RTC_APP_KEY = "9deff80cc6f646c7a9decbcbe238e4b6";
    IM
    public static final String IM_APP_ID = "895366";
    public static final String IM_APP_KEY = "cM3c3l5E/N6NgRxX9UDvbrMOV05fuzbTuJTOaIrysDA=";

    //海外版本
    火山音视频
    public static final String RTC_APP_ID = "68778226b996380175060aa0";
    public static final String RTC_APP_KEY = "c50bbd0c1747463ba1f01a3c1ea23e3a";
    IM
    public static final String IM_APP_ID = "908686";
    public static final String IM_APP_KEY = "Il0oIKOWiq3um4zAdJx/yqgDmiFoam4zEPtfRa9xgSc=";
     */

    //国内版本
//    public static final String APP_ID = "67bee1ff570e09017bb7970b";
//    public static final String APP_KEY = "9deff80cc6f646c7a9decbcbe238e4b6";


    //海外版本
    public static final String APP_ID = "68778226b996380175060aa0";
    public static final String APP_KEY = "c50bbd0c1747463ba1f01a3c1ea23e3a";

    public static final String BYTE_EFFECT_LIC_NAME = "xianyu_test_20250725_20250825_com.dabai.kiss.chat_4.7.2_3193.licbag";

    //测试时候所需的 app id 和秘钥   火山IM
    //public static final int IM_APP_ID = 895366; //国内appID
    public static final int IM_APP_ID = 908686; //海外appID

    /*      web端
     * 发送房间外消息的类型 这个是RTC邀请通话相关的
     * */

    //邀请
    public static final String RTC_MSG_INVITE = "InviteVideoCall";
    //挂断
    public static final String RTC_MSG_REJECT = "RejectVideoCall";
    //接通
    public static final String RTC_MSG_AGREE = "AgreeVideoCall";

    /*      后台
     * 发送房间外消息的类型 这个是 自动打招呼
     * */
    public static final String RTC_MSG_HI = "auto_hello";



    /*
    * 这个是系统消息 im 发送
    * */
    //  举报结果
    public static final String IM_SYS_REPORT = "SystemMsgReport";
    //  提现结果
    public static final String IM_SYS_CASH = "SystemMsgCash";



    // 现在这里作为 系统消息会话对象的id来使用0-10000是后台预留的特殊id
    public static final String RTC_UID_FOR_SYSTEM_MSG = "10000"; //系统消息
    public static final String RTC_UID_FOR_ORDER_MSG = "10001"; //透传消息
    public static final String RTC_UID_FOR_KE_FU = "1000010000";


}