package com.dubu.main.login

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.dialog.BaseBottomDialog
import com.dubu.common.manager.LoginManager
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.StatusBarTool
import com.dubu.common.utils.ToastTool
import com.dubu.main.R
import com.dubu.main.api.LoginClient
import com.dubu.main.databinding.ActivityLoginDialogBinding
import com.dubu.me.vm.CommonViewModel
import kotlinx.coroutines.launch

/**
 * 登录弹框页面
 * 以Dialog样式从底部弹出，提供Facebook、Apple、Google三种登录方式
 *
 * @author SOLO Coding
 * @since 2024
 */
@Route(path = RouteConst.ACTIVITY_LOGIN_DIALOG)
class LoginDialogActivity : BaseBindingActivity<ActivityLoginDialogBinding>() {

    /**
     * 通用ViewModel，用于处理登录相关业务逻辑
     */
    private val model: CommonViewModel by viewModels()

    /**
     * 获取布局资源ID
     * @return 布局资源ID
     */
    override fun getContentLayoutId() = R.layout.activity_login_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindow()
    }

    private fun setWindow() {
        window?.run {
            attributes = attributes.apply {
                gravity = Gravity.BOTTOM
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
                // 控制背景遮罩
                dimAmount = 0.8F
            }
            StatusBarTool.setTransparent(this)
        }

    }

    /**
     * Activity创建完成后的初始化操作
     */
    override fun onCreated() {
        HiLog.e(Tag2Common.TAG_12301, "LoginDialogActivity onCreated")

        // 初始化状态栏配置
        initStatusBar()

        // 初始化点击事件
        initClickEvents()
    }

    /**
     * 预创建操作，设置Dialog主题
     * @param savedInstanceState 保存的实例状态
     */
    override fun preCreate(savedInstanceState: Bundle?) {

        // 设置Dialog主题样式
        setTheme(R.style.dialogStyle)

        super.preCreate(savedInstanceState)
    }

    /**
     * 初始化状态栏配置
     */
    private fun initStatusBar() {
        // 设置状态栏透明，深色字体
        findViewById<View>(R.id.vTop)?.let {
            initOptBar(it, isStatusBarDarkFont = true, isKeyboardEnable = false)
        }
    }

    /**
     * 初始化点击事件
     */
    private fun initClickEvents() {
        bindingApply {
            // 点击外部区域关闭弹框
            root.setOnClickListener {
                HiLog.e(Tag2Common.TAG_12301, "点击外部区域关闭弹框")
                finish()
            }

            // 关闭按钮点击事件
            ivClose.setOnClickListener {
                HiLog.e(Tag2Common.TAG_12301, "点击关闭按钮")
                finish()
            }

            // Facebook登录按钮点击事件
            btnFacebook.setOnClickListener {
                HiLog.e(Tag2Common.TAG_12301, "点击Facebook登录")
                handleFacebookLogin()
            }

            // Apple登录按钮点击事件
            btnApple.setOnClickListener {
                HiLog.e(Tag2Common.TAG_12301, "点击Apple登录")
                handleAppleLogin()
            }

            // Google登录按钮点击事件
            btnGoogle.setOnClickListener {
                HiLog.e(Tag2Common.TAG_12301, "点击Google登录")
                handleGoogleLogin()
            }

            // 隐私政策链接点击事件
            tvPrivacyPolicy.setOnClickListener {
                HiLog.e(Tag2Common.TAG_12301, "点击隐私政策")
                handlePrivacyPolicyClick()
            }
        }
    }

    /**
     * 处理Facebook登录
     */
    private fun handleFacebookLogin() {
        HiLog.e(Tag2Common.TAG_12301, "开始Facebook登录流程")
        // TODO: 集成Facebook SDK后实现
        toast("Facebook登录功能开发中...")

        // 暂时使用访客登录作为占位
        performGuestLogin()
    }

    /**
     * 处理Apple登录
     */
    private fun handleAppleLogin() {
        HiLog.e(Tag2Common.TAG_12301, "开始Apple登录流程")
        // TODO: 集成Apple Sign In后实现
        toast("Apple登录功能开发中...")

        // 暂时使用访客登录作为占位
        performGuestLogin()
    }

    /**
     * 处理Google登录
     */
    private fun handleGoogleLogin() {
        HiLog.e(Tag2Common.TAG_12301, "开始Google登录流程")
        // TODO: 集成Google Sign In后实现
        toast("Google登录功能开发中...")

        // 暂时使用访客登录作为占位
        performGuestLogin()
    }

    /**
     * 执行访客登录
     */
    private fun performGuestLogin() {
        HiLog.e(Tag2Common.TAG_12301, "开始访客登录流程")

        lifecycleScope.launch {
            try {
                // 显示加载状态
                showLoadingDialog()

                val loginClient = LoginClient()
                val user = loginClient.visitorLogin(
                    uuid = HiRealCache.deviceId,
                    failed = { code, msg ->
                        HiLog.e(Tag2Common.TAG_12301, "访客登录失败: code=$code, msg=$msg")
                        dismissLoadingDialog()
                        toast("登录失败")
                    }
                )

                dismissLoadingDialog()

                if (user != null) {
                    HiLog.e(Tag2Common.TAG_12301, "访客登录成功: ${user.nickname}")
                    toast("登录成功")

                    // 登录成功后关闭弹框并跳转到主页
                    Router.toMainActivity()
                    finish()
                } else {
                    HiLog.e(Tag2Common.TAG_12301, "访客登录失败: 返回用户信息为空")
                    toast("登录失败")
                }

            } catch (e: Exception) {
                HiLog.e(Tag2Common.TAG_12301, "访客登录异常: ${e.message}")
                dismissLoadingDialog()
                toast("登录失败")
            }
        }
    }

    /**
     * 处理隐私政策点击
     */
    private fun handlePrivacyPolicyClick() {
        HiLog.e(Tag2Common.TAG_12301, "跳转到隐私政策页面")
        // 跳转到隐私政策页面
        Router.toWebViewActivity("Privacy Policy", "https://example.com/privacy-policy")
    }


    /**
     * Activity销毁时的清理操作
     */
    override fun onDestroy() {
        super.onDestroy()
        HiLog.e(Tag2Common.TAG_12301, "LoginDialogActivity onDestroy")
    }
}