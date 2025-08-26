package com.dubu.main.app

import android.content.res.Configuration
import android.view.Gravity
import com.alibaba.android.arouter.launcher.ARouter
// BIM相关导入暂时注释掉，避免编译错误
// import com.bytedance.im.core.api.BIMClient
// import com.bytedance.im.core.api.enums.BIMEnv
// import com.bytedance.im.core.api.model.BIMSDKConfig
import com.dubu.common.R
import com.dubu.common.constant.Constants
import com.dubu.common.constant.Tag2Common
import com.dubu.common.http.NetConfig
import com.dubu.common.manager.LanguageManager
import com.dubu.common.utils.HiLog
import com.dubu.common.views.pull.LoadFooterView
import com.dubu.download.msnetwork.NvsServerClient
import com.dubu.rtc.emoji.EmojiConverter
import com.hjq.language.MultiLanguages
import com.hjq.toast.Toaster
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider


class HiRealApplication : com.dubu.common.base.BaseApp() {
    override fun mainWorkEngine() {


    }

    override fun backupWorkEngine() {
        MMKV.initialize(this, MMKVLogLevel.LevelInfo)
        initLang()
        NetConfig.init()
        initGsyVideoPlay()
        initARouter()
        initRefreshLayout()
        // initIm() // 暂时注释掉BIM初始化
        EmojiManager.install(GoogleEmojiProvider())
        EmojiConverter.preload(this)

        // 初始化 Toast 框架
        Toaster.init(this)
        Toaster.setView(R.layout.toast_common)
        Toaster.setGravity(Gravity.CENTER)

        NvsServerClient.get().initConfig(this, "")


        /*
        为了保证运营数据的准确性，建议不要在异步线程初始化Bugly。
        第三个参数为SDK调试模式开关，调试模式的行为特性如下：
        输出详细的Bugly SDK的Log；
        每一条Crash都会被立即上报；
        自定义日志将会在Logcat中输出。
        建议在测试阶段建议设置成true，发布时设置为false。
        * */
        CrashReport.initCrashReport(this, "aaaa6c5f72", true);

    }

    private fun initLang() {
        // 初始化语种切换框架
        MultiLanguages.init(this)
        val lastUserAppLang = LanguageManager.getLastUserAppLang()
        MultiLanguages.setDefaultLanguage(lastUserAppLang)

        HiLog.l(Tag2Common.TAG_12300, "initLang =  lastUserAppLang : ${lastUserAppLang.toString()} ")

    }

    // BIM初始化方法暂时注释掉，避免编译错误
    /*
    private fun initIm() {
        val config = BIMSDKConfig()
        /*config.logListener = BIMLogListener { p0, p1 ->
            HiLog.e(Tag2Common.TAG_RTC_IM2, "火山IM: $p0  : :  :  :  :  :  $p1")
        }*/

        //BIMEnv.I18N.getEnv() 代表海外环境
        //BIMEnv.DEFAULT_ZH.getEnv() 代表国内环境

        //方式 1: 开发者实现日志打印逻辑，如写入文件上报服务等。
        BIMClient.getInstance().initSDK(this, Constants.IM_APP_ID, config, BIMEnv.I18N.env)
        //方式 2: 使用默认方式，日志打印到 logcat
        //IMClient.getInstance().initSDK(this, Constants.IM_APP_ID, null)
    }
    */

    private fun initGsyVideoPlay() {
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT)
    }


    private fun initARouter() {
        if (com.dubu.main.BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        createConfigurationContext(newConfig)
    }


    //初始化刷新空间头
    private fun initRefreshLayout() {

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setEnableFooterFollowWhenNoMoreData(true)
            return@setDefaultRefreshFooterCreator LoadFooterView(context)
        }


        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setHeaderTriggerRate(1f)
            layout.setEnableOverScrollBounce(true)
            layout.setEnableOverScrollDrag(true)
            return@setDefaultRefreshHeaderCreator MaterialHeader(context)
                .setColorSchemeResources(R.color.clFF4E34)
        }

    }


}