package com.dubu.common.dialog

import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dubu.common.utils.DisplayUiTool
import com.dubu.common.R
import com.dubu.common.ext.isValid


class TipDialogV3 : DialogFragment() {
    private var title: String? = null
    private var content: String? = null
    private var btnText: String? = null
    private var clickListener: ((View) -> Unit)? = null
    private var cancelOut = false
    private var canCancel = false
    private var dismissWhenClick = true
    private var isGoneButton = false
    private var topRightButton = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.dialog_info_3, container, false)
        initView(root)
        return root
    }

    fun withClickListener(onclick: (View) -> Unit): TipDialogV3 {
        clickListener = onclick
        return this
    }

    fun withNoDismissClick(): TipDialogV3 {
        this.dismissWhenClick = false
        return this
    }

    fun withButtonText(@StringRes id: Int): TipDialogV3 {
        this.btnText =  com.dubu.common.base.BaseApp.instance.getString(id)
        return this
    }

    fun withTopRightCancelButton(isVisible:Boolean): TipDialogV3 {
        this.topRightButton = isVisible
        return this
    }

    fun withVisibleButton(isGone:Boolean):TipDialogV3{
        this.isGoneButton = isGone
        return this
    }

    fun withButtonText(text: String): TipDialogV3 {
        this.btnText = text
        return this
    }

    private var contentSP1: SpannableString? = null //内容变色
    fun withTv1SpannableString(content: SpannableString): TipDialogV3 {
        this.contentSP1 = content
        return this
    }
    private var contentSP2: SpannableString? = null //内容变色
    fun withTv2SpannableString(content: SpannableString): TipDialogV3 {
        this.contentSP2 = content
        return this
    }
    private var contentSP3: SpannableString? = null //内容变色
    fun withTv3SpannableString(content: SpannableString): TipDialogV3 {
        this.contentSP3 = content
        return this
    }

    fun withContent(@StringRes id: Int): TipDialogV3 {
        this.content =  com.dubu.common.base.BaseApp.instance.getString(id)
        return this
    }

    fun withTitle(@StringRes id: Int): TipDialogV3 {
        this.title =  com.dubu.common.base.BaseApp.instance.getString(id)
        return this
    }

    fun withContent(content: String): TipDialogV3 {
        this.content = content
        return this
    }

    fun withTitle(title: String): TipDialogV3 {
        this.title = title
        return this
    }

    fun withCancelOutside(cancel: Boolean): TipDialogV3 {
        cancelOut = cancel
        return this
    }

    fun withCancelable(cancel: Boolean): TipDialogV3 {
        canCancel = cancel
        return this
    }

    private fun initView(root: View) {
        val tvTitle: TextView = root.findViewById(R.id.dd_tv_title)
        val tvContent1: TextView = root.findViewById(R.id.dd_tv_content_1)
        val tvContent2: TextView = root.findViewById(R.id.dd_tv_content_2)
        val tvContent3: TextView = root.findViewById(R.id.dd_tv_content_3)
        val btnRight: TextView = root.findViewById(R.id.dd_btn_end)
        var ivCancel:ImageView = root.findViewById(R.id.iv_cancel)
        tvContent1.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (!TextUtils.isEmpty(title)) {
            tvTitle.text = title
        } else {
            tvTitle.visibility = View.GONE
        }


        if (null != contentSP1) {
            tvContent1.text = contentSP1
        } else {
            tvContent1.visibility = View.GONE
        }

        if (null != contentSP2) {
            tvContent2.text = contentSP2
        } else {
            tvContent2.visibility = View.GONE
        }
        if (null != contentSP3) {
            tvContent3.text = contentSP3
        } else {
            tvContent3.visibility = View.GONE
        }


        if (btnText.isValid()) {
            btnRight.text = btnText
        }


        if(topRightButton){
            ivCancel.visibility = VISIBLE
            ivCancel.setOnClickListener{
                dismiss()
            }
        }

        btnRight.setOnClickListener {
            clickListener?.invoke(it)
            if (dismissWhenClick) {
                dismiss()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        setWindow()
    }


    private fun setWindow() {
        dialog?.apply {
            val w = (DisplayUiTool.getScreenWidth(context) * 0.73f).toInt()
            window?.run {
                attributes = attributes.apply {
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    width = w
                }
                setGravity(Gravity.CENTER)
                setBackgroundDrawableResource(android.R.color.transparent)
            }
            setCanceledOnTouchOutside(cancelOut)
            setCancelable(canCancel)
        }
    }


    private fun changeGravity(tv: TextView) {
        tv.viewTreeObserver
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (tv.lineCount == 1) {
                        tv.gravity = Gravity.CENTER
                    }
                    tv.viewTreeObserver.removeOnPreDrawListener(this)
                    return false
                }
            })
    }


    fun show(manager: FragmentManager) {
        if (isActivityDead()) return
        try {
            manager.beginTransaction().remove(this).commitAllowingStateLoss()
            super.show(manager, "info_dialog")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun isActivityDead(): Boolean {
        activity?.let {
            return it.isFinishing || it.isDestroyed
        }
        return false
    }

    override fun dismiss() = dismissAllowingStateLoss()


}