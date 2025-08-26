package com.dubu.main.login

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.RegexUtils
import com.dubu.common.base.BaseActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.http.NetConfig.RS_DATA_ERROR
import com.dubu.common.http.NetWorkConst
import com.dubu.common.manager.LoginManager
import com.dubu.common.router.RouteConst
import com.dubu.common.router.Router
import com.dubu.common.utils.*
import com.dubu.main.BuildConfig
import com.dubu.main.R
import com.dubu.main.home.MainActivity
import com.dubu.test.testdata.TestData


/**
 *  登录界面
 */
@Route(path = RouteConst.ACTIVITY_LOGIN)
class LoginActivity : BaseActivity() {
    private val model: LoginViewModel by viewModels()

    companion object {
        private const val SHOW_TIME = 60 * 1000L
        private const val WHAT_COUNT = 0x1
    }

    private var isCanSendCode: Boolean = true

    private var downCount = 60
    private var handler: Handler? = null
    private var tvDesSum: TextView? = null

    // 396660616@qq.com
    private var userInputEmail: String = ""
    private var userInputCode: String = ""

    override fun isNeedDefaultScreenConfig() = false
    override fun getContentLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun onCreated() {
        StatusBarTool.setTransparent(window, isShowStatusBar = true, false)
        initView()
    }


    /**
     *
     * 这里判断是要去初始化页面还是首页
     */
    private var mUserNeedInit = false // 用户是否需要初始化
    private fun gotoNextPage() {
        if (mUserNeedInit) {
            Router.toMyInitActivity()
        } else {
            toMainActivity()
        }
    }

    private fun initHandler(): Handler {
        return if (handler != null) handler!!
        else object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (downCount > 0) {
                    setTime()
                    downCount -= 1
                    sendEmptyMessageDelayed(WHAT_COUNT, 1000L)
                } else {
                    resetUi()
                }
            }
        }.also { handler = it }
    }

    private var llMM: LinearLayout? = null
    private var etEmail: EditText? = null
    private var etCode: EditText? = null

    private fun initView() {
        llMM = findViewById<LinearLayout>(R.id.llMM)
        etEmail = findViewById<EditText>(R.id.etEmail)
        etCode = findViewById<EditText>(R.id.etDes)
        val tvPolicy = findViewById<TextView>(R.id.tvPolicy)
        setProtocolLinkAction(tvPolicy)

        if (!LoginManager.checkUserTokenIsValid()) {
            ToastTool.toastError(R.string.toast_login_expired)
        }


        findViewById<View>(R.id.ivBack).setOnClickListener {
            finish()
        }

        etEmail?.addTextChangedListener { eb ->
            val trim = eb.toString().trim()
            val en = ((trim.length) > 0)
            userInputEmail = if (en) {
                eb.toString().trim()
            } else {
                ""
            }

        }

        etCode?.addTextChangedListener { eb ->
            val trim = eb.toString().trim()
            val en = ((trim.length) > 0)
            userInputCode = if (en) {
                eb.toString().trim()
            } else {
                ""
            }

        }

        //=============


        tvDesSum = findViewById<TextView>(R.id.tvDesSum)
        ClickUtils.applySingleDebouncing(tvDesSum) {

            handleSendCode()


        }

        val tvNext = findViewById<View>(R.id.tvNext)

        ClickUtils.applySingleDebouncing(tvNext) {
            // 处理回车事件
            handleEnterKeyPressed()
        }


        etCode?.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // 处理回车事件
                handleEnterKeyPressed()

                // 返回 true 表示已处理事件
                true
            } else {
                false
            }
        }

        if (TestData.isCanGotoTestPage && BuildConfig.DEBUG) {
            etEmail?.setText("396660616@qq.com")
            userInputEmail = "396660616@qq.com"
            //handleSendCode()
        }

    }

    private fun handleSendCode() {

        if (!isCanSendCode)
            return

        if (userInputEmail.isNotEmpty() && RegexUtils.isEmail(userInputEmail) && etEmail != null) {
            KeyboardUtils.hideSoftInput(etEmail!!)

            showLoadingDialog()
            model.emailSendCode(userInputEmail, success = { data ->
                dismissLoadingDialog()
                HiLog.e(Tag2Common.TAG_12300, "emailLogin 11--> ${GsonUtils.toJson(data)}")

                isCanSendCode = false

                initHandler().let {
                    it.removeMessages(WHAT_COUNT)
                    it.sendEmptyMessageDelayed(WHAT_COUNT, 0L)
                }

            }, failed = { code, msg ->

                if (code == RS_DATA_ERROR && NetworkUtils.isConnected()) {
                    ToastTool.toastError(com.dubu.common.R.string.toast_err_service)
                } else {
                    ToastTool.toastError(com.dubu.common.R.string.toast_err_net)
                }

                dismissLoadingDialog()
            })

            tvDesSum?.postDelayed({ dismissLoadingDialog() }, 5000L)


        } else {
            ToastTool.toastError(com.dubu.common.R.string.plz_enter_correct_email)
        }
    }

    private fun handleEnterKeyPressed() {

        if (userInputEmail.isEmpty() || userInputCode.isEmpty()) {
            ToastTool.toastError(R.string.plz_valid_email_and_code)
            return
        }


        KeyboardUtils.hideSoftInput(this@LoginActivity)

        showLoadingDialog()
        model.emailCodeLogin(userInputEmail, userInputCode, success = { data ->
            dismissLoadingDialog()



        }, failed = { code, _ ->
            if (code == RS_DATA_ERROR && NetworkUtils.isConnected()) {
                ToastTool.toastError(com.dubu.common.R.string.toast_err_service)
            } else {
                ToastTool.toastError(com.dubu.common.R.string.toast_err_net)
            }
            dismissLoadingDialog()
        })

        tvDesSum?.postDelayed({ dismissLoadingDialog() }, 5000L)
    }


    private fun toMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun setTime() {
        tvDesSum?.text = "${downCount}s"
    }

    private fun resetUi() {
        isCanSendCode = true
        tvDesSum?.text = getString(com.dubu.common.R.string.send_verification_code)
        initHandler().let {
            it.removeMessages(WHAT_COUNT)
        }
    }


    private fun setProtocolLinkAction(tv: TextView) {
        val txt = getString(R.string.sign_in_hint)
        val sb = SpannableStringBuilder(txt)

        val color = ContextCompat.getColor(tv.context, R.color.white)
        val title2 = getString(R.string.privacy_policy)
        val cs2 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                (widget as TextView).highlightColor = Color.TRANSPARENT
                if (AdvancedClickUtils.canClick(widget))
                    Router.toWebViewActivity("123", NetWorkConst.BASE_URL_PRIVACY)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = false
            }
        }
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_white_alpha_40))
        tv.movementMethod = LinkMovementMethod.getInstance()
        val s2 = txt.length + 1

        sb.append(" ")
        sb.append(title2)

        HiLog.e(Tag2Common.TAG_12300, "key = = $title2 = $txt")

        sb.setSpan(cs2, s2, s2 + title2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.setSpan(UnderlineSpan(), s2, s2 + title2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = sb
    }


    private fun setProtocolLinkAction2(tv: TextView) {
        val txt = getString(R.string.sign_in_hint)
        val sb = SpannableStringBuilder(txt)
        val color = ContextCompat.getColor(tv.context, R.color.white)
        val title1 = getString(R.string.user_terms)
        val cs1 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                (widget as TextView).highlightColor = Color.TRANSPARENT
                ToastTool.toast("点击用户协议")
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = false
            }
        }

        val title2 = getString(R.string.privacy_policy)
        val cs2 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                (widget as TextView).highlightColor = Color.TRANSPARENT
                ToastTool.toast("点击隐私协议")
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = false
            }
        }
        tv.setTextColor(ContextCompat.getColor(tv.context, R.color.color_white_alpha_40))
        tv.movementMethod = LinkMovementMethod.getInstance()
        val s1 = txt.indexOf(title1) - 1
        val s2 = txt.indexOf(title2) - 1

        HiLog.e(Tag2Common.TAG_12300, "key = $title1 = $title2 = $txt")

        sb.setSpan(cs1, s1, s1 + title1.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.setSpan(cs2, s2, s2 + title2.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = sb
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (null != llMM) {
                if (!isTouchInsideViewLine(ev, llMM!!)) {
                    KeyboardUtils.hideSoftInput(this@LoginActivity)
                }
            }


        }
        return super.dispatchTouchEvent(ev)
    }


    /**
     * 判断点击事件的y是不是在view之上
     *  适用于底部输入框 消息键盘
     * @param [event]
     * @param [targetView]
     * @return [Boolean]
     */
    private fun isTouchAboveView(event: MotionEvent, targetView: View): Boolean {
        val location = IntArray(2)
        targetView.getLocationOnScreen(location)
        return event.rawY.toInt() < location[1]
    }

    /**
     * 判断点击的y是不是在view 所在的那一行  并不是判断是不是在内部
     * @param [event]
     * @param [view]
     * @return [Boolean]
     */
    private fun isTouchInsideViewLine(event: MotionEvent, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val y = event.rawY.toInt()
        val viewH = kotlin.math.abs(view.top - view.bottom)
        val startY = location[1]
        val endY = location[1] + viewH
        return y in startY..endY
    }


}