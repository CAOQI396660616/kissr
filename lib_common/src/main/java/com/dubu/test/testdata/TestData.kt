package com.dubu.test.testdata

import com.blankj.utilcode.util.GsonUtils
import com.dubu.common.R
import com.dubu.common.base.BaseApp
import com.dubu.common.beans.UserBean
import com.dubu.common.beans.common.GiftInfoBean
import com.dubu.common.beans.me.HelpDataBean
import com.dubu.common.beans.me.HelpMultiTypeBean
import com.dubu.common.beans.me.QuestionDataBean
import com.dubu.common.beans.rtc.*
import com.dubu.common.constant.Constants
import com.dubu.common.ext.fromJson
import com.dubu.common.utils.HiRealCache


/**
 * @Description
 *  测试数据 脏数据
 */
object TestData {

    //发版前 请都置为 false

    //测试数据总开关
//    const val isCanTest = true
        const val isCanTest = false

    //是否可以去测试页面
//        const val isCanGotoTestPage = true
        const val isCanGotoTestPage = false



    //搞几个头像
    //https://pics0.baidu.com/feed/dc54564e9258d109eaecc5caee9978b16d814d8e.jpeg@f_auto?token=11267675242b7e055de8e1d693b86c81
    //https://pics0.baidu.com/feed/b8014a90f603738d52e9218f8cda045ffa19ecc8.jpeg@f_auto?token=364604795257865a58278cbfdfa12cf5
    //https://pics0.baidu.com/feed/b3119313b07eca80a62bbdeaaee223d3a04483e5.jpeg@f_auto?token=096049547124ad497543cd5150908880

    //美女手机尺寸图片
    //https://b0.bdstatic.com/ugc/img/2024-12-16/925fa7a13f38013d2de6b35195c677ce.png


    @JvmStatic
    fun getStringList(): MutableList<String> {
        val mutableListOf = mutableListOf<String>()
        for (i in 0..50) {
            mutableListOf.add("$i -- ")
        }
        return mutableListOf
    }

    @JvmStatic
    fun getStringEnList(): MutableList<String> {
        val mutableListOf = mutableListOf<String>()
        mutableListOf.add("1, 始终面向阳光，阴影自然落身后。")
        mutableListOf.add("2, 人生苦短，活出自己。")
        mutableListOf.add("3, 闪光的并不都是金子。")
        mutableListOf.add("4, 玫瑰不叫玫瑰，依然芳香如故。")
        mutableListOf.add("5, 黎明前的时分是最黑暗的。")
        mutableListOf.add("6, 心之所愿，无事不成。")
        mutableListOf.add("7, 伟大的理想造就伟大的人。")
        mutableListOf.add("8, 决心取胜者永不言“不可能”。")
        mutableListOf.add("9, 奇迹终会降临，唯拼搏者得见。")
        mutableListOf.add("10, 我走得很慢，但绝不后退。")
        mutableListOf.add("1, 始终面向阳光，阴影自然落身后。")
        mutableListOf.add("2, 人生苦短，活出自己。")
        mutableListOf.add("3, 闪光的并不都是金子。")
        mutableListOf.add("4, 玫瑰不叫玫瑰，依然芳香如故。")
        mutableListOf.add("5, 黎明前的时分是最黑暗的。")
        mutableListOf.add("6, 心之所愿，无事不成。")
        mutableListOf.add("7, 伟大的理想造就伟大的人。")
        mutableListOf.add("8, 决心取胜者永不言“不可能”。")
        mutableListOf.add("9, 奇迹终会降临，唯拼搏者得见。")
        mutableListOf.add("10, 我走得很慢，但绝不后退。")
        mutableListOf.add("1, 始终面向阳光，阴影自然落身后。")
        mutableListOf.add("2, 人生苦短，活出自己。")
        mutableListOf.add("3, 闪光的并不都是金子。")
        mutableListOf.add("4, 玫瑰不叫玫瑰，依然芳香如故。")
        mutableListOf.add("5, 黎明前的时分是最黑暗的。")
        mutableListOf.add("6, 心之所愿，无事不成。")
        mutableListOf.add("7, 伟大的理想造就伟大的人。")
        mutableListOf.add("8, 决心取胜者永不言“不可能”。")
        mutableListOf.add("9, 奇迹终会降临，唯拼搏者得见。")
        mutableListOf.add("10, 我走得很慢，但绝不后退。")
        return mutableListOf
    }




    /*
         mutableListOf.add("1, Keep your face always toward the sunshine - and shadows will fall behind you.")
        mutableListOf.add("2, Your time is limited, so don’t waste it living someone else’s life.")
        mutableListOf.add("3, All that glisters is not gold.")
        mutableListOf.add("4, What’s in a name? That which we call a rose by any other word would smell as sweet.")
        mutableListOf.add("5, The darkest hour is that before the dawn.")
        mutableListOf.add("6, Nothing is impossible to a willing heart.")
        mutableListOf.add("7, Great hopes make great man.")
        mutableListOf.add("8, The man who has made up his mind to win will never say “impossible”.")
        mutableListOf.add("9, Miracles sometimes occur, but one has to work terribly for them.")
        mutableListOf.add("10, I am a slow walker, but I never walk backwards.")
        mutableListOf.add("11, Time is a bird for ever on the wing.")
        mutableListOf.add("12, If you do not learn to think when you are young, you may never learn.")
        mutableListOf.add("13, Education is a progressive discovery of our own ignorance.")
        mutableListOf.add("14, An idle youth, a needy age.")
        mutableListOf.add("15, Constant dripping wears away the stone.")
        mutableListOf.add("16, Love is blind and lovers cannot see the pretty follies that themselves commit.")
        mutableListOf.add("17, The course of true love never did run smooth.")
        mutableListOf.add("18, Everyone is a moon, and has a dark side which he never shows to anybody.")
        mutableListOf.add("19, Remember that no matter how cool you think you may be, you’re not cool enough to look down on anyone.")
        mutableListOf.add("20, Beggars cannot be choosers.")
        mutableListOf.add("21, The reasonable man adapts himself to the world; the unreasonable one persists in trying to adapt the world to himself.")
        mutableListOf.add("22, There is no such thing as darkness; only a failure to see.")
        mutableListOf.add("23, A long dispute means that both parties are wrong.")
        mutableListOf.add("24, It is impossible to defeat an ignorant man in argument.")
        mutableListOf.add("25, Repetition does not transform a lie into a truth.")
    * */




    @JvmStatic
    fun getStringEnForRtcList(): MutableList<RtcCallMsgMultiTypeBean> {
        val mutableListOf = mutableListOf<RtcCallMsgMultiTypeBean>()
        getStringEnList().forEachIndexed { index, s ->
            //
            val rtcCallMsgDataBean1 = getRtcCallMsgDataBeanWeb()
            if (null != rtcCallMsgDataBean1){
                rtcCallMsgDataBean1.text = s
            }
            val element1 = RtcCallMsgDataBean(
                GsonUtils.toJson(rtcCallMsgDataBean1),
                RtcCallMsgMultiTypeBean.ITEM_TYPE_TEXT_T
            )

            val rtcCallMsgDataBean2 = getRtcCallMsgDataBeanAndroid()
            if (null != rtcCallMsgDataBean2){
                rtcCallMsgDataBean2.text = "测试文本 ${index+1}"
            }
            val element2 = RtcCallMsgDataBean(
                GsonUtils.toJson(rtcCallMsgDataBean2),
                RtcCallMsgMultiTypeBean.ITEM_TYPE_TEXT_T
            )

            mutableListOf.add(RtcCallMsgMultiTypeBean(RtcCallMsgMultiTypeBean.ITEM_TYPE_TEXT,element1))
            mutableListOf.add(RtcCallMsgMultiTypeBean(RtcCallMsgMultiTypeBean.ITEM_TYPE_TEXT,element2))
        }
        return mutableListOf
    }
    @JvmStatic
    fun getRtcCallMsgDataBeanWeb(): RtcTextMsgDataBean? {
        val str ="{\"from\":\"10130\",\"fromAvatar\":\"https://fichat-dev.tos-cn-guangzhou.volces.com/uploads/20250702/6864a3b952d2f.jpg?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTNDgxYjQ0OWYxYTk0NGEwZGE2N2UzMmE5NDFhMzliMTE%2F20250702%2Fcn-guangzhou%2Ftos%2Frequest&X-Tos-Date=20250702T041026Z&X-Tos-Expires=3600&X-Tos-SignedHeaders=host&X-Tos-Signature=74bf40c19a9ff4985ba8d14f8d6fcd20b3b0f1195ab476f6cc49ae68d9f7cd97\",\"fromNickName\":\"qqq\",\"text\":\"123\",\"to\":\"10122\",\"toAvatar\":\"https://fichat-dev.tos-cn-guangzhou.volces.com/uploads/20250630/686252a53602a.png?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTNDgxYjQ0OWYxYTk0NGEwZGE2N2UzMmE5NDFhMzliMTE%2F20250702%2Fcn-guangzhou%2Ftos%2Frequest&X-Tos-Date=20250702T041027Z&X-Tos-Expires=2592000&X-Tos-SignedHeaders=host&X-Tos-Signature=f4deafb6142fd10b04fd4cbfe4cf578ef602f996bc1739686046ee2adbb2adb1\",\"toNickName\":\"1122\",\"type\":\"text\"}\n"
            val msgDataBean = str.fromJson<RtcTextMsgDataBean>()
        return msgDataBean
    }
    @JvmStatic
    fun getRtcCallMsgDataBeanAndroid(): RtcTextMsgDataBean? {
        val str ="{\"from\":\"10122\",\"fromAvatar\":\"https://fichat-dev.tos-cn-guangzhou.volces.com/uploads/20250630/686252a53602a.png?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTNDgxYjQ0OWYxYTk0NGEwZGE2N2UzMmE5NDFhMzliMTE%2F20250702%2Fcn-guangzhou%2Ftos%2Frequest&X-Tos-Date=20250702T060906Z&X-Tos-Expires=3600&X-Tos-SignedHeaders=host&X-Tos-Signature=d4f5c425ddb33a810e953a6ce443d8b58cf890a35d43986c3c34a24c2b80a53a\",\"fromNickName\":\"1122\",\"text\":\"12222\",\"to\":\"10130\",\"toAvatar\":\"https://fichat-dev.tos-cn-guangzhou.volces.com/uploads/20250702/6864a3b952d2f.jpg?X-Tos-Algorithm=TOS4-HMAC-SHA256&X-Tos-Credential=AKLTNDgxYjQ0OWYxYTk0NGEwZGE2N2UzMmE5NDFhMzliMTE%2F20250702%2Fcn-guangzhou%2Ftos%2Frequest&X-Tos-Date=20250702T033712Z&X-Tos-Expires=3600&X-Tos-SignedHeaders=host&X-Tos-Signature=46f8a07051af1c5f71859133a303077e84afec9b55b718fe332e907f88b8fd9e\",\"toNickName\":\"qqq\",\"type\":\"text\"}\n"
        val msgDataBean = str.fromJson<RtcTextMsgDataBean>()
        return msgDataBean
    }

    @JvmStatic
    fun getRtcGameMsgDataBeanList(): MutableList<RtcGameMsgDataBean> {
        val mutableListOf = mutableListOf<RtcGameMsgDataBean>()

        for (i in 0..4) {
            mutableListOf.add(
                RtcGameMsgDataBean(
                    link = "https://app.ugameapk.com/game/8-ball-pool.html",
                    gameName = "8 Ball Pool",
                    imageUrl = HiRealCache.user?.avatar ?: "",
                    activeDescription = "点击进入"
                )
            )
        }

        return mutableListOf
    }


    /*
    val avatar: String? = null,
    val content: String? = null,
    val email: String? = null,
    val from: String? = null,
    val gender: Int? = 0,
    val nickname: String? = null,
    val roomId: String? = null,
    val to: String? = null,
    * */
    fun getInviteCallMsg(userBean: UserBean): String {
        val rtcCallMsgBean = RTCCallMsgBean(
            RTCCallMsgData(
                fromAvatar = HiRealCache.user?.avatar,
                content = "这是一条来自${HiRealCache.user?.nickname}的邀请通话的消息",
                email = HiRealCache.user?.email,
                from = (HiRealCache.user?.userSn ?: 0L).toString(),
                fromNickname = HiRealCache.user?.nickname,
                roomId = "${HiRealCache.user?.userSn}DB${userBean.userSn}",
                to = (userBean.userSn).toString(),
            ), Constants.RTC_MSG_INVITE
        )


        return GsonUtils.toJson(rtcCallMsgBean) ?: ""
    }

    fun getAgreeCallMsg(rtcCallMsgData: RTCCallMsgData): String {
        val rtcCallMsgBean = RTCCallMsgBean(
            RTCCallMsgData(
                fromAvatar = HiRealCache.user?.avatar,
                content = "这是一条来自${HiRealCache.user?.nickname}的接受通话的消息",
                email = HiRealCache.user?.email,
                from = (HiRealCache.user?.userSn ?: 0L).toString(),
                fromNickname = HiRealCache.user?.nickname,
                roomId = rtcCallMsgData.roomId,
                to = rtcCallMsgData.from,
            ), Constants.RTC_MSG_AGREE
        )

        return GsonUtils.toJson(rtcCallMsgBean) ?: ""
    }


    @JvmStatic
    fun getGiftList(): MutableList<GiftInfoBean> {
        val mutableListOf = mutableListOf<GiftInfoBean>()

        for (i in 0..30) {
            mutableListOf.add(
                GiftInfoBean(
                    name = "测试",
                    image = "https://pics0.baidu.com/feed/b3119313b07eca80a62bbdeaaee223d3a04483e5.jpeg@f_auto?token=096049547124ad497543cd5150908880",
                    giftSn = -1,
                    value = 10, //金币
                    nums = 10L,
                    isChoose = false,
                )
            )
        }

        return mutableListOf
    }


    @JvmStatic
    fun getHelpList(): MutableList<HelpMultiTypeBean> {
        val mutableListOf = mutableListOf<QuestionDataBean>()

        mutableListOf.add(
            QuestionDataBean(
                BaseApp.instance.getString(R.string.question_1),
                BaseApp.instance.getString(R.string.answer_1)
            )
        )
        mutableListOf.add(
            QuestionDataBean(
                BaseApp.instance.getString(R.string.question_2),
                BaseApp.instance.getString(R.string.answer_2)
            )
        )
        mutableListOf.add(
            QuestionDataBean(
                BaseApp.instance.getString(R.string.question_3),
                BaseApp.instance.getString(R.string.answer_3)
            )
        )
        mutableListOf.add(
            QuestionDataBean(
                BaseApp.instance.getString(R.string.question_4),
                BaseApp.instance.getString(R.string.answer_4)
            )
        )
        mutableListOf.add(
            QuestionDataBean(
                BaseApp.instance.getString(R.string.question_5),
                BaseApp.instance.getString(R.string.answer_5)
            )
        )

        val helpDataBean = HelpDataBean(BaseApp.instance.getString(R.string.question_des), mutableListOf)

        val list = mutableListOf<HelpMultiTypeBean>()
        list.add(HelpMultiTypeBean(HelpMultiTypeBean.ITEM_TYPE_ALL, helpDataBean))
        return list
    }




    @JvmStatic
    fun getImageList(): MutableList<String> {
        val mutableListOf = mutableListOf<String>()
        mutableListOf.add("uploads/20250617/685108a0deedc.jpeg")
        mutableListOf.add("uploads/20250617/685109bcf040f.webp")
        return mutableListOf
    }


    @JvmStatic
    fun getChatUserJson(): String {
        return "{\"id\":10130,\"kol_id\":0,\"avatar\":\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250714/687481cabb2fb.png\",\"token\":\"\",\"gender\":\"\",\"name\":\"\",\"nickname\":\"Mark\",\"age\":0,\"email\":\"\",\"created_at\":\"\",\"updated_at\":\"\",\"uuid\":\"\",\"invite_code\":\"\",\"birthday\":\"\",\"country_code\":\"\",\"lang_codes\":\"\",\"im_token\":\"\",\"rtc_token\":\"\",\"is_fresh\":1,\"info\":\"\",\"online_status\":\"\",\"video_gold_price\":0,\"service_status\":0,\"isClicked\":false}\n"
    }

    @JvmStatic
    fun getChatUserBean(): UserBean? {
        return getChatUserJson().fromJson<UserBean>()
    }

}