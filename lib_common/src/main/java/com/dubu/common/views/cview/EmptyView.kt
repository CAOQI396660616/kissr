package com.dubu.common.views.cview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.dubu.common.R
import com.dubu.common.ext.getDimen

/**
 * Author:v
 * Time:2021/9/3
 */
class EmptyView : LinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        initView(context)
    }


    private fun initView(context: Context?) {
        context ?: return

        val dp5 = context.getDimen(R.dimen.dp_5)

        val ivIcon = ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_XY
//            setImageResource(R.drawable.img_empty_data_black)
        }
        addView(ivIcon, LinearLayout.LayoutParams(dp5 * 37, dp5 * 20).apply {
            setMargins(0, 0, 0, dp5 * 5)
        })

        val tvHint = TextView(context).apply {
            setTextColor(ContextCompat.getColor(context, R.color.cl847F97))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_15))
//            text = context.getString(R.string.toast_err_service)
        }
        addView(tvHint,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, dp5 * 5)
            })

        val btn = DrawableTextView(context).apply {
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_15))
            setMediumTextWeight()
//            text = context.getString(R.string.retry)
            gravity = Gravity.CENTER
//            background = ContextCompat.getDrawable(context, R.drawable.bg_stroke_r25_555555)
            minWidth = dp5 * 35
            setPadding(dp5.shl(2), dp5 * 3, dp5.shl(2), dp5 * 3)
        }

        addView(
            btn, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                dp5 * 10
            )
        )
    }


    fun withDrawable(@DrawableRes emptyImage: Int) {
        val iv = getChildAt(0) as ImageView
        if (emptyImage == 0) {
            iv.visibility = View.GONE
            return
        }
        iv.setImageResource(emptyImage)
    }


    fun withNoButton() {
        getChildAt(2).visibility = View.GONE
    }

    fun withButtonClick(refreshListener: () -> Unit) {
        val btn = getChildAt(2) as TextView
        btn.setOnClickListener {
            refreshListener()
        }
    }

    fun withButtonStyle(white: Boolean) {
        (getChildAt(2) as TextView).apply {
            if (white) {
                setBackgroundResource(R.drawable.bg_solid_r8_white)
                setTextColor(resources.getColor(R.color.color222222, null))
            } else {
                setBackgroundResource(R.drawable.bg_solid_r8_white)
                setTextColor(Color.WHITE)
            }
        }
    }

    fun withButtonText(@StringRes btnText: Int) {
        (getChildAt(2) as TextView).apply {
            text = context.getString(btnText)
            visibility = View.VISIBLE
        }
    }

    fun withText(@StringRes emptyHint: Int) {
        val tvHint = getChildAt(1) as TextView
        tvHint.text = context.getString(emptyHint)
    }

    fun withText(text: String) {
        val tvHint = getChildAt(1) as TextView
        tvHint.text = text
    }

    fun withButton() = getChildAt(2) as TextView
    fun withHint() = getChildAt(1) as TextView

}