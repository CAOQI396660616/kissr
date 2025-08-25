package com.dubu.common.views.bubble

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.dubu.common.utils.DisplayUiTool
import com.dubu.common.R
import com.dubu.common.ext.getDimen

/**
 * Author:v
 * Time:2022/11/12
 */
class BubbleWindow : PopupWindow {
    private var bubbleLayout: BubbleLayout? = null
    private var align: Int = 0

    constructor() {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = false
        isClippingEnabled = false

        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun withBubbleView(view: BubbleLayout): BubbleWindow {
        this.bubbleLayout = view
        contentView = view
        return this
    }

    fun withContentView(view: View, black: Boolean = true): BubbleWindow {
        bubbleLayout = initBubbleLayout(black, view.context).apply {
            addView(view)
            contentView = this
        }
        return this
    }

    private fun initBubbleLayout(isBlack: Boolean, context: Context): BubbleLayout {
        val dp10 = context.getDimen(R.dimen.dp_10)
        return BubbleLayout(context).apply {
            setBackgroundColor(Color.TRANSPARENT)
            setBubbleShadowColor(Color.TRANSPARENT)
            setBubblePadding(dp10)
            setBubbleBgColor(context.getColor(R.color.color_black_alpha_85))
        }
    }

    fun withTriangleOffset(offset: Float): BubbleWindow {
        bubbleLayout?.setTriangleOffset(offset)
        return this
    }


    /**
     * @param ort one of
     * [android.view.Gravity.START]
     * [android.view.Gravity.END]
     * [android.view.Gravity.TOP]
     * [android.view.Gravity.BOTTOM]
     */
    fun withDirection(ort: Int): BubbleWindow {
        bubbleLayout?.setBubbleOrientation(ort)
        return this
    }

    /**
     * 0 align center of anchor
     * 1 align end of anchor
     * 2 align start of anchor
     * 3 align screen center
     */
    fun withAlign(align: Int): BubbleWindow {
        this.align = align
        return this
    }

    private fun measureHeight(): Int {
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        return contentView.measuredHeight
    }


    private fun measureWidth(): Int {
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        return contentView.measuredWidth
    }

    fun show(anchor: View) = show(anchor, 0, 0)

    fun show(anchor: View, xo: Int = 0, yo: Int = 0) {
        if (isShowing) return

        val loc = intArrayOf(0, 0)
        anchor.getLocationOnScreen(loc)

        val sW = DisplayUiTool.getScreenWidth(anchor.context)
        val isLtr = anchor.layoutDirection == View.LAYOUT_DIRECTION_LTR
        val ort = bubbleLayout?.getBubbleOrientation() ?: Gravity.START
        val x = loc[0]
        val y = loc[1]
        val w = measureWidth()
        val h = measureHeight()
        val aw = anchor.width
        val ah = anchor.height

//        VLog.d("vvv", "x=$x y=$y w=$w h=$h aw=$aw ah=$ah")

        var ex = x
        var ey = y
        when {
            ort == Gravity.BOTTOM -> {
                ey = y + ah
                if (align == 0) {
                    ex = x - (w - aw) / 2
                } else if (align == 1) {
                    ex = x + aw - w
                } else if (align == 2) {
                    ex = x
                } else {
                    ex = (sW - w) / 2
                }
            }
            ort == Gravity.TOP -> {
                ey = y - h
                if (align == 0) {
                    ex = x - (w - aw) / 2
                } else if (align == 1) {
                    ex = x + aw - w
                } else if (align == 2) {
                    ex = x
                } else {
                    ex = (sW - w) / 2
                }
            }
            (isLtr && ort == Gravity.END) || (!isLtr && ort == Gravity.START) -> {
//                showAtLocation(anchor, Gravity.NO_GRAVITY, x + anchor.width, y - anchor.height / 2)
            }
            else -> {
//                showAtLocation(anchor, Gravity.NO_GRAVITY, x - anchor.width, y - anchor.height / 2)
            }
        }

        showAtLocation(anchor, Gravity.NO_GRAVITY, ex + xo, ey + yo)

    }

    companion object {
        const val ALIGN_SCREEN_CENTER = 3
        const val ALIGN_ANCHOR_END = 1
        const val ALIGN_ANCHOR_START = 2
        const val ALIGN_ANCHOR_CENTER = 0
    }
}