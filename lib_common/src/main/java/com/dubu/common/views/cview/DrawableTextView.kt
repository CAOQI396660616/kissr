package com.dubu.common.views.cview

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.dubu.common.R
import com.dubu.common.ext.getDimen


/**
 * Author:v
 * Time:2021/1/25
 */
open class DrawableTextView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        defaultV = context.getDimen(R.dimen.dp_20)
        initAttr(context, attrs)
    }

    private var defaultV: Int
    private var mDrawable: Drawable? = null
    private var mScaleHeight = 0f
    private var mScaleWidth = 0f
    private var mPosition = 3
    private var lastDrawableId = 0


    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.DrawableTextView
        )
        mDrawable = typedArray.getDrawable(R.styleable.DrawableTextView_drawable)
        mScaleWidth = typedArray
            .getDimensionPixelOffset(
                R.styleable.DrawableTextView_drawableWidth,
                defaultV
            ).toFloat()
        mScaleHeight = typedArray.getDimensionPixelOffset(
            R.styleable.DrawableTextView_drawableHeight,
            defaultV
        ).toFloat()
        mPosition = typedArray.getInt(R.styleable.DrawableTextView_drawablePosition, 3)
        val medium = typedArray.getBoolean(R.styleable.DrawableTextView_mediumWeight, false)
        if (medium) {
            setMediumTextWeight()
        }
        typedArray.recycle()
        initDrawable()
    }


    private fun initDrawable() {
        mDrawable?.setBounds(0, 0, mScaleWidth.toInt(), mScaleHeight.toInt())
        var p = mPosition
        when (p) {
            1 -> {
                setCompoundDrawables(mDrawable, null, null, null)
            }
            2 -> {
                setCompoundDrawables(null, mDrawable, null, null)
            }
            3 -> {
                setCompoundDrawables(null, null, mDrawable, null)
            }
            4 -> {
                setCompoundDrawables(null, null, null, mDrawable)
            }
        }
    }


    fun setDrawableSize(size: Float) {
        mScaleHeight = size
        mScaleWidth = size
    }

    fun setDrawableSize(width: Float, height: Float) {
        mScaleHeight = height
        mScaleWidth = width
    }

    fun clearDrawable() {
        if (mDrawable == null) return
        mDrawable = null
        lastDrawableId = -1
        setCompoundDrawables(null, null, null, null)
        postInvalidate()
    }


    fun setDrawableLeft(drawable: Drawable) {
        mDrawable = drawable
        mPosition = 1
        initDrawable()
        postInvalidate()
    }


    fun setDrawableLeft(resId: Int) {
        val drawable = ContextCompat.getDrawable(context, resId)
            ?: throw IllegalArgumentException("NULL drawable id")
        if (resId == lastDrawableId) return
        setDrawableLeft(drawable)
        lastDrawableId = resId
    }



    fun setDrawableTop(drawable: Drawable) {
        mDrawable = drawable
        mPosition = 2
        initDrawable()
        postInvalidate()
    }


    fun setDrawableTop(resId: Int) {
        val drawable = ContextCompat.getDrawable(context, resId)
            ?: throw IllegalArgumentException("NULL drawable id")
        if (resId == lastDrawableId) return
        setDrawableTop(drawable)
        lastDrawableId = resId
    }

    fun setDrawableRight(drawable: Drawable) {
        mDrawable = drawable
        mPosition = 3
        initDrawable()
        postInvalidate()
    }


    fun setDrawableRight(resId: Int) {
        val drawable = ContextCompat.getDrawable(context, resId)
            ?: throw IllegalArgumentException("NULL drawable id")
        if (resId == lastDrawableId) return
        setDrawableRight(drawable)
        lastDrawableId = resId
    }


    fun setDrawableBottom(drawable: Drawable) {
        mDrawable = drawable
        mPosition = 4
        initDrawable()
        postInvalidate()
    }

    fun setDrawableBottom(resId: Int) {
        val drawable = ContextCompat.getDrawable(context, resId)
            ?: throw IllegalArgumentException("NULL drawable id")
        if (resId == lastDrawableId) return
        setDrawableBottom(drawable)
        lastDrawableId = resId
    }

    fun setBoldTextWeight() {
        setTypeface(null, Typeface.BOLD)
        paint.strokeWidth = 0.0f
        postInvalidate()
    }

    fun setMediumTextWeight() {
        setTypeface(null, Typeface.NORMAL)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 1.2f
        postInvalidate()
    }

    fun setRegularTextWeight() {
        setTypeface(null, Typeface.NORMAL)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 0.0f
        postInvalidate()
    }

    fun tryMirrorDrawable() {
        mDrawable?.layoutDirection = View.LAYOUT_DIRECTION_RTL
    }

}