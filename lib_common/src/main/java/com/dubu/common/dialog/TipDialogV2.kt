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


class TipDialogV2 : DialogFragment() {
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
        val root = inflater.inflate(R.layout.dialog_info_2, container, false)
        initView(root)
        return root
    }

    fun withClickListener(onclick: (View) -> Unit): TipDialogV2 {
        clickListener = onclick
        return this
    }

    fun withNoDismissClick(): TipDialogV2 {
        this.dismissWhenClick = false
        return this
    }

    fun withButtonText(@StringRes id: Int): TipDialogV2 {
        this.btnText =  com.dubu.common.base.BaseApp.instance.getString(id)
        return this
    }

    fun withTopRightCancelButton(isVisible:Boolean): TipDialogV2 {
        this.topRightButton = isVisible
        return this
    }

    fun withVisibleButton(isGone:Boolean):TipDialogV2{
        this.isGoneButton = isGone
        return this
    }

    fun withButtonText(text: String): TipDialogV2 {
        this.btnText = text
        return this
    }

    private var contentSP: SpannableString? = null //内容变色
    fun withContentSpannableString(content: SpannableString): TipDialogV2 {
        this.contentSP = content
        return this
    }

    fun withContent(@StringRes id: Int): TipDialogV2 {
        this.content =  com.dubu.common.base.BaseApp.instance.getString(id)
        return this
    }

    fun withTitle(@StringRes id: Int): TipDialogV2 {
        this.title =  com.dubu.common.base.BaseApp.instance.getString(id)
        return this
    }

    fun withContent(content: String): TipDialogV2 {
        this.content = content
        return this
    }

    fun withTitle(title: String): TipDialogV2 {
        this.title = title
        return this
    }

    fun withCancelOutside(cancel: Boolean): TipDialogV2 {
        cancelOut = cancel
        return this
    }

    fun withCancelable(cancel: Boolean): TipDialogV2 {
        canCancel = cancel
        return this
    }

    private fun initView(root: View) {
        val tvTitle: TextView = root.findViewById(R.id.dd_tv_title)
        val tvContent: TextView = root.findViewById(R.id.dd_tv_content)
        val btnRight: TextView = root.findViewById(R.id.dd_btn_end)
        var ivCancel:ImageView = root.findViewById(R.id.iv_cancel)
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
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


        if (btnText.isValid()) {
            btnRight.text = btnText
        }

        if(isGoneButton){
            btnRight.visibility = View.GONE
            tvContent.setPadding(0, 0, 0, DisplayUiTool.dp2px(btnRight.context, 22f).toInt())
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