package com.dubu.main.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alibaba.android.arouter.facade.annotation.Route
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.ext.toJson
import com.dubu.common.manager.LoginManager
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.HiLog
import com.dubu.main.R
import com.dubu.main.databinding.ActivitySplashBinding
import com.dubu.main.home.MainActivity
import com.dubu.me.vm.CommonViewModel


//@SuppressLint("CustomSplashScreen")
@Route(path = RouteConst.ACTIVITY_SPLASH)
class SplashActivity : BaseBindingActivity<ActivitySplashBinding>() {

    private val model: CommonViewModel by viewModels()

    override fun onCreated() {

        //这里是初始化 本地的用户信息 和一些 需要用到的json
        //目前暂时用到的 MMKV 后续可以换成 DB
        LoginManager.checkUserIsLogin()


        findViewById<View>(R.id.vTop)?.let {
            initOptBar(it, isStatusBarDarkFont = true, isKeyboardEnable = false)
        }

        // 测试访客登录功能
        model.guestLogin(
            success = { user ->
                HiLog.e(Tag2Common.TAG_12301, "login ******* = ${user.toJson()}")
                toMainActivity()
            },
            failed = { code, msg ->
            }
        )

    }


    override fun preCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT > 30) {
            installSplashScreen()
        } else {
            setTheme(R.style.AppTheme)
        }
    }


    override fun getContentLayoutId() = R.layout.activity_splash

    private fun toMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun toLoginActivity() {
        Router.toLoginActivity()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }


}