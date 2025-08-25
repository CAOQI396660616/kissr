package com.dubu.common.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dubu.common.R
import com.dubu.common.ext.isValid


/**
 * Author:v
 * Time:2020/11/25
 */
class LoadingDialog : DialogFragment() {

    private var canCancel: Boolean = true
    private var cancelOutside: Boolean = true
    private var text: String? = null

    fun withTipText(txt: String): LoadingDialog {
        this.text = txt
        return this
    }

    fun withTipText(txt: Int): LoadingDialog {
        this.text =  com.dubu.common.base.BaseApp.instance.getString(txt)
        return this
    }

    fun withCancelOutside(cancelOutside: Boolean): LoadingDialog {
        this.cancelOutside = cancelOutside
        return this
    }

    fun withCancelable(canCancel: Boolean): LoadingDialog {
        this.canCancel = canCancel
        return this
    }


    fun show(manager: FragmentManager) {
        activity?.let {
            if (it.isFinishing || it.isDestroyed) return
        }
        if (manager.isDestroyed) return
        try {
            manager.beginTransaction().remove(this).commitAllowingStateLoss()
            super.show(manager, "loading_dialog")
        } catch (e: Exception) {
        }
    }


    override fun dismiss() = try {
        dismissAllowingStateLoss()
    } catch (e: Exception) {
    }


    override fun onStart() {
        super.onStart()
        setWindow()
    }


    private fun setWindow() {
        dialog?.apply {
            window?.run {
                attributes = attributes.apply {
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    width = WindowManager.LayoutParams.MATCH_PARENT
                }


                setGravity(Gravity.CENTER)
//                setBackgroundDrawableResource()
                setDimAmount(0f)
            }
            //设置框背景透明->布局中使用自己的框样式a
            //设置框背景透明->布局中使用自己的框样式a
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(canCancel)
            setCanceledOnTouchOutside(cancelOutside)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.dialog_loading, container, false)
        val tv = root.findViewById<TextView>(R.id.tv_loading)
        if (text.isValid()) {
            tv.text = text
            tv.visibility = View.VISIBLE
        } else {
            tv.visibility = View.GONE
        }
        return root
    }


}