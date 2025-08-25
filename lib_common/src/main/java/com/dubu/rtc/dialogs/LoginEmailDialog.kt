package com.dubu.rtc.dialogs

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.RegexUtils
import com.dubu.common.R
import com.dubu.common.base.BaseBottomSheetDialogFragment
import com.dubu.common.utils.ToastTool
import com.dubu.test.testdata.TestData


/**
 * 礼物新弹框
 * @author cq
 * @date 2025/06/12
 * @constructor 创建[LoginEmailDialog]
 */
class LoginEmailDialog : BaseBottomSheetDialogFragment() {
    companion object {
        private const val SHOW_TIME = 60 * 1000L
        private const val WHAT_COUNT = 0x1
    }

    private var onActionCode: ((email: String) -> Unit)? = null
    private var onActionLogin: ((email: String, code: String) -> Unit)? = null

    private var isCanSendCode: Boolean = true

    private var downCount = 60
    private var handler: Handler? = null
    private var tvDesSum: TextView? = null

    // 396660616@qq.com
    private var userInputEmail: String = ""
    private var userInputCode: String = ""

    fun withActionCodeListener(listener: ((email: String) -> Unit)): LoginEmailDialog {
        onActionCode = listener
        return this
    }

    fun withActionLoginListener(listener: ((email: String, code: String) -> Unit)): LoginEmailDialog {
        onActionLogin = listener
        return this
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

    private fun resetUi() {
        isCanSendCode = true
        tvDesSum?.text = context?.getString(R.string.send_verification_code)
        initHandler().let {
            it.removeMessages(WHAT_COUNT)
        }
    }

    private fun setTime() {
        tvDesSum?.text = "${downCount}s"
    }


    override fun getLayoutId(): Int {
        return R.layout.dialog_login_email
    }

    override fun initialize(root: View) {

        val etEmail = root.findViewById<EditText>(R.id.etEmail)
        val etCode = root.findViewById<EditText>(R.id.etDes)
        root.findViewById<View>(R.id.ivClose).setOnClickListener {
            dismiss()
        }
        tvDesSum = root.findViewById<TextView>(R.id.tvDesSum)
        tvDesSum?.setOnClickListener {

            if (!isCanSendCode)
                return@setOnClickListener

            if (userInputEmail.isNotEmpty() && RegexUtils.isEmail(userInputEmail)) {
                KeyboardUtils.hideSoftInput(etEmail)
                onActionCode?.invoke(userInputEmail)
                isCanSendCode = false

                initHandler().let {
                    it.removeMessages(WHAT_COUNT)
                    it.sendEmptyMessageDelayed(WHAT_COUNT, 0L)
                }

            } else {
                ToastTool.toastError(R.string.plz_enter_correct_email)
            }


        }
        root.findViewById<View>(R.id.tvNext).setOnClickListener {

            if (userInputCode.isNotEmpty()) {
                KeyboardUtils.hideSoftInput(etCode)
                onActionLogin?.invoke(userInputEmail, userInputCode)
            } else {
                ToastTool.toastError(R.string.plz_enter_correct_code)
            }


        }

        etEmail.addTextChangedListener { eb ->
            val trim = eb.toString().trim()
            val en = ((trim.length) > 0)
            userInputEmail = if (en) {
                eb.toString().trim()
            } else {
                ""
            }

        }

        etCode.addTextChangedListener { eb ->
            val trim = eb.toString().trim()
            val en = ((trim.length) > 0)
            userInputCode = if (en) {
                eb.toString().trim()
            } else {
                ""
            }

        }
    }

    override fun loadData() {

    }


    /*
      ╔════════════════════════════════════════════════════════════════════════════════════════╗
      ║   PS:
      ╚════════════════════════════════════════════════════════════════════════════════════════╝
   */


}