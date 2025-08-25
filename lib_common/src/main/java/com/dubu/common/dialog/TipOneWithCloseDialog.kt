package com.dubu.common.dialog

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dubu.common.R
import com.dubu.common.base.BaseApp
import com.dubu.common.utils.DisplayUiTool

/**
 * Author:v
 * Time:2020/11/25
 */
@Suppress("unused")
class TipOneWithCloseDialog : DialogFragment() {

    companion object {
        private const val TAG = "TipOneWithCloseDialog"
    }

    private var canCancel: Boolean = false
    private var cancelOutside: Boolean = false
    private var contentTvCenter: Boolean = true //内容文字居中? 默认不居中
    private var title: String? = null
    private var content: String? = null
    private var contentSP: SpannableString? = null //内容变色
    private var startText: String? = null
    private var endText: String? = null
    private var startListener: (() -> Unit)? = null
    private var endListener: (() -> Unit)? = null
    private var endColor: Int = Color.WHITE
    private var startColor: Int = BaseApp.instance.getColor(R.color.cl363638)


    fun withTitle(title: String): TipOneWithCloseDialog {
        this.title = title
        return this
    }


    fun withContent(content: String): TipOneWithCloseDialog {
        this.content = content
        return this
    }

    fun withContentSpannableString(content: SpannableString): TipOneWithCloseDialog {
        this.contentSP = content
        return this
    }

    fun withStartText(txt: String): TipOneWithCloseDialog {
        this.startText = txt
        return this
    }

    fun withEndText(txt: String): TipOneWithCloseDialog {
        this.endText = txt
        return this
    }

    fun withTitle(title: Int): TipOneWithCloseDialog {
        this.title = BaseApp.instance.getString(title)
        return this
    }

    fun withContent(content: Int): TipOneWithCloseDialog {
        this.content = BaseApp.instance.getString(content)
        return this
    }

    fun withStartText(txt: Int): TipOneWithCloseDialog {
        this.startText = BaseApp.instance.getString(txt)
        return this
    }

    fun withEndText(txt: Int): TipOneWithCloseDialog {
        this.endText = BaseApp.instance.getString(txt)
        return this
    }

    fun withEndColor(color: Int): TipOneWithCloseDialog {
        this.endColor = color
        return this
    }

    fun withStartColor(color: Int): TipOneWithCloseDialog {
        this.startColor = color
        return this
    }

    fun withStartListener(action: () -> Unit): TipOneWithCloseDialog {
        startListener = action
        return this
    }


    fun withEndListener(action: () -> Unit): TipOneWithCloseDialog {
        endListener = action
        return this
    }

    fun withCancelOnBack(cancel: Boolean): TipOneWithCloseDialog {
        canCancel = cancel
        return this
    }

    fun withCancelOutside(cancelOutside: Boolean): TipOneWithCloseDialog {
        this.cancelOutside = cancelOutside
        return this
    }

    fun withContentCenter(contentCenter: Boolean): TipOneWithCloseDialog {
        this.contentTvCenter = contentCenter
        return this
    }


    override fun onStart() {
        super.onStart()
        setWindow()
    }


    private fun setWindow() {
        dialog?.apply {
            val w = (DisplayUiTool.getScreenWidth(context) * 0.83f).toInt()
            window?.run {
                attributes = attributes.apply {
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    width = w
                }
                setGravity(Gravity.CENTER)
                setBackgroundDrawableResource(android.R.color.transparent)
            }
            setCanceledOnTouchOutside(cancelOutside)
            setCancelable(canCancel)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.dialog_c_w_c1, container, false)
        initView(root)
        return root
    }

    private fun changeGravity(tv: TextView) {
        tv.viewTreeObserver
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (tv.lineCount == 1 || contentTvCenter) {
                        tv.gravity = Gravity.CENTER
                    }
                    tv.viewTreeObserver.removeOnPreDrawListener(this)
                    return false
                }
            })
    }



    private fun initView(root: View) {
        val tvTitle: TextView = root.findViewById(R.id.dd_tv_title)
        val tvContent: TextView = root.findViewById(R.id.dd_tv_content)
        val btnStart: TextView = root.findViewById(R.id.dd_btn_start)
        val btnEnd: TextView = root.findViewById(R.id.dd_btn_end)
        val ivClose: ImageView = root.findViewById(R.id.ivClose)

        ivClose.setOnClickListener {
            dismiss()
        }

        if (!TextUtils.isEmpty(title)) {
            tvTitle.text = title
        } else {
            tvTitle.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent.text = content
            changeGravity(tvContent)
        } else if (null != contentSP) {
            tvContent.text = contentSP
            changeGravity(tvContent)
        } else {
            tvContent.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(endText)) {
            btnEnd.text = endText
        }
        btnEnd.setTextColor(endColor)
        btnStart.setTextColor(startColor)

        if (!TextUtils.isEmpty(startText)) {
            btnStart.text = startText
        }

        btnStart.setOnClickListener {
            dismiss()
            startListener?.invoke()
        }
        btnEnd.setOnClickListener {
            dismiss()
            endListener?.invoke()
        }
    }


    fun show(manager: FragmentManager) {
        activity?.let {
            if (it.isFinishing || it.isDestroyed) return
        }
        if (manager.isDestroyed) return
        try {
            manager.beginTransaction().remove(this).commit()
            super.show(manager, TAG)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun dismiss() {
        try {
            dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}