package com.dubu.common.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dubu.common.R
import com.dubu.common.utils.StatusBarTool

abstract class BaseBottomDialog : DialogFragment() {

    companion object {
        // 默认背景遮罩透明度 (0.0f - 1.0f)
        private const val DEFAULT_DIM_AMOUNT = 0.5f
    }

    abstract val TAG: String

    private var canCancel: Boolean = true
    private var cancelOutside: Boolean = true
    fun withCancelOnBack(cancel: Boolean): BaseBottomDialog {
        canCancel = cancel
        return this
    }

    fun withCancelOutside(cancelOutside: Boolean): BaseBottomDialog {
        this.cancelOutside = cancelOutside
        return this
    }

    // 在 BaseBottomDialog 中添加：
    private var dimAmount: Float = DEFAULT_DIM_AMOUNT
    private var dimBackground: Boolean = true  // 默认显示黑色背景遮罩
    /**
     * 自定义背景遮罩透明度
     * @param amount 透明度值 (0.0f - 1.0f, 0=完全透明，1=完全不透明)
     */
    fun withDimAmount(amount: Float): BaseBottomDialog {
        dimBackground = true
        dimAmount = amount.coerceIn(0f, 1f)
        return this
    }

    // 新增方法：控制是否显示黑色背景遮罩
    fun withDimBackground(dim: Boolean): BaseBottomDialog {
        dimBackground = dim
        return this
    }

    // ✅ 新增方法：是否全屏（子类可覆盖）
    open fun isFullScreen(): Boolean = false


    override fun getTheme() = R.style.Theme_HiReal_BottomDialog

    private fun setWindow() {
        dialog?.apply {
            window?.run {
                attributes = attributes.apply {
                    gravity = Gravity.BOTTOM
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = if (isFullScreen()) {
                        WindowManager.LayoutParams.MATCH_PARENT
                    } else {
                        WindowManager.LayoutParams.WRAP_CONTENT
                    }

                    // 控制背景遮罩
                    dimAmount = if (dimBackground) {
                        // 默认值：显示半透明黑色背景
                        DEFAULT_DIM_AMOUNT
                    } else {
                        // 完全透明，不显示背景遮罩
                        0f
                    }
                }
                // 根据设置添加或移除背景遮罩标志
                if (dimBackground) {
                    addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                } else {
                    clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                }
                StatusBarTool.setTransparent(this)
            }
            setCancelable(canCancel)
            setCanceledOnTouchOutside(cancelOutside)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(getLayoutId(), container, false)
        initView(root)
        return root
    }

    open fun getLayoutId(): Int = 0


    override fun onStart() {
        super.onStart()
        setWindow()
    }


    open fun initView(root: View) {
    }


    fun show(manager: FragmentManager) {
        activity?.let {
            if (it.isFinishing || it.isDestroyed) return
        }
        val f = manager.findFragmentByTag(TAG)
        if (f?.isAdded == true) {
            manager.beginTransaction().show(f)
        } else {
            super.show(manager, TAG)
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