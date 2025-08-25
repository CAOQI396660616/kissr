package com.dubu.main.home

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import com.dubu.common.utils.DisplayUiTool
import kotlin.math.abs

/**
 * Author:v
 * Time:2023/4/26
 */
class DragLayout : LinearLayout {
    private val TAG = "DragLayout"


    private val distance: Float by lazy {
        DisplayUiTool.dp2px(context, 20f)
    }

    private val sw: Int by lazy {
        DisplayUiTool.getScreenWidth(context)
    }

    private val sh: Int by lazy {
        DisplayUiTool.getScreenHeight(context)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        super.dispatchTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val dx = abs(ev.rawX - lastX)
        val dy = abs(ev.rawY - lastY)
        return dx > distance || dy > distance
    }


    private var lastX = 0f
    private var lastY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX
        val y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = x - lastX
                val dy = y - lastY
                setX(getX() + dx)
                setY(getY() + dy)
                lastX = x
                lastY = y
            }

            MotionEvent.ACTION_UP -> {
                lastX = 0f
                lastY = 0f
                placeLayout()
            }
        }
        return true
    }

    private fun placeLayout() {

        val loc = IntArray(2)
        getLocationOnScreen(loc)
        val rx = loc[0]
        val ry = loc[1]

        val dx = if (rx < (sw - width).shr(1)) {
            0
        } else {
            sw - width
        }

        val minY = (distance * 7).toInt()
        val maxY = (sh - distance * 3.8f).toInt()

        val dy = if (ry < minY) {
            minY
        } else if (ry > maxY) {
            maxY
        } else ry


        val bx = dx - rx
        val by = dy - ry
        if (bx == 0 && by == 0) return
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 250L
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float
                if (bx != 0) {
                    x = rx + bx * value
                }
                if (by != 0) {
                    y = ry + by * value
                }
            }
            start()
        }

    }


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

}