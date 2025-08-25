package com.dubu.common.views.pull

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.dubu.common.R
import com.dubu.common.ext.getDimen

/**
 * Author:v
 * Time:2021/3/1
 */
@SuppressLint("RestrictedApi")
class LoadFooterView : LinearLayout, RefreshFooter {


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context!!)
    }

    private lateinit var tvText: TextView
    private lateinit var ivLoad: ImageView
    private var anim: ObjectAnimator? = null
    private var noMoreData = false


    private fun initView(context: Context) {
        val dp20 = context.getDimen(R.dimen.dp_20)
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        tvText = TextView(context).apply {
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, R.color.color_black_alpha_50))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_12))
            text = getString(R.string.loading)
            setPadding(dp20 / 3, 0, 0, 0)
        }


        ivLoad = ImageView(context).apply {
            setImageResource(R.drawable.ic_default_loading)
            scaleType = ImageView.ScaleType.FIT_XY
        }

        anim = ObjectAnimator.ofFloat(ivLoad, View.ROTATION, 0f, 360f).also {
            it.duration = 800L
            it.repeatMode = ObjectAnimator.RESTART
            it.repeatCount = ObjectAnimator.INFINITE
        }

        addView(ivLoad, dp20, dp20)
        addView(tvText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        minimumHeight = dp20 * 3
    }

    private fun getString(idRes: Int): String {
        return context.getString(idRes)
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        if (noMoreData) return
        when (newState) {
            RefreshState.Loading -> {
                tvText.text = getString(R.string.loading)
                ivLoad.visibility = View.VISIBLE
                anim?.start()
            }

            RefreshState.PullUpToLoad -> {
                ivLoad.visibility = View.GONE
                tvText.text = getString(R.string.pull_load_more)
            }
            else -> {
                //nothing
            }
        }
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        ivLoad.visibility = View.VISIBLE
        anim?.start()
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        ivLoad.visibility = View.GONE
        anim?.cancel()
        return if (success) {
            tvText.text = getString(R.string.load_complete)
            200
        } else {
            tvText.text = getString(R.string.load_failed)
            1000
        }
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun isSupportHorizontalDrag(): Boolean = false

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        this.noMoreData = noMoreData
        ivLoad.visibility = View.GONE
        tvText.text = getString(R.string.load_no_more)
        return true
    }
}