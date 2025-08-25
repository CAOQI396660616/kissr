package com.dubu.main.login

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.RegexUtils
import com.dubu.common.base.BaseActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.manager.LoginManager
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.StatusBarTool
import com.dubu.common.utils.ToastTool
import com.dubu.main.R
import com.dubu.main.home.MainActivity
import com.dubu.rtc.dialogs.LoginEmailDialog


/**
 *  登录界面
 */
//@Route(path = RouteConst.ACTIVITY_LOGIN)
class LoginActivity2 : BaseActivity() {
    companion object {
        private const val SHOW_TIME = 60 * 1000L
        private const val WHAT_COUNT = 0x1
    }


    override fun isNeedDefaultScreenConfig() = false
    override fun getContentLayoutId(): Int {
        return R.layout.activity_login2
    }

    override fun onCreated() {
        StatusBarTool.setTransparent(window, isShowStatusBar = true, false)
        initView()
    }

    private fun initView() {

    }


}