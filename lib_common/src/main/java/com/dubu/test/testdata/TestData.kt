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




    @JvmStatic
    fun getChatUserJson(): String {
        return "{\"id\":10130,\"kol_id\":0,\"avatar\":\"https://fichat-alpha.tos-cn-guangzhou.volces.com/uploads/20250714/687481cabb2fb.png\",\"token\":\"\",\"gender\":\"\",\"name\":\"\",\"nickname\":\"Mark\",\"age\":0,\"email\":\"\",\"created_at\":\"\",\"updated_at\":\"\",\"uuid\":\"\",\"invite_code\":\"\",\"birthday\":\"\",\"country_code\":\"\",\"lang_codes\":\"\",\"im_token\":\"\",\"rtc_token\":\"\",\"is_fresh\":1,\"info\":\"\",\"online_status\":\"\",\"video_gold_price\":0,\"service_status\":0,\"isClicked\":false}\n"
    }


}