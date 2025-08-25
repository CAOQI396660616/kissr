package com.dubu.common.views.bubble

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.dubu.common.R
import kotlin.math.tan

/**
 * Author:v
 * Time:2022/11/12
 */
class BubbleLayout : ConstraintLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttr(context, attrs)
    }


    private var mPadding = 30
    private var mStrokeWidth = 2f
    private var cornerRadius = 30f
    private var shadowColor = Color.argb(100, 0, 0, 0)
    private var bgColor = Color.WHITE
    private var triangleOffset = 0f
    private var _orientation = Gravity.START
    private var _shortTriangle = false

    private lateinit var mPaint: Paint
    private lateinit var bgPaint: Paint
    private lateinit var mPath: Path
    private lateinit var mRect: RectF
    private lateinit var trianglePath: Path


    private fun initAttr(context: Context, attrs: AttributeSet?) {
        attrs?.run {
            val a = context.obtainStyledAttributes(attrs, R.styleable.BubbleLayout)
            try {
                shadowColor = a.getColor(R.styleable.BubbleLayout_shadowColor, shadowColor)
                bgColor = a.getColor(R.styleable.BubbleLayout_bgColor, bgColor)
                mPadding = a.getDimensionPixelSize(
                    R.styleable.BubbleLayout_android_padding, mPadding
                )
                cornerRadius = a.getDimensionPixelSize(
                    R.styleable.BubbleLayout_cornerRadius,
                    cornerRadius.toInt()
                ).toFloat()
            } finally {
                a.recycle()
            }
        }

        setPadding(mPadding, mPadding, mPadding, mPadding)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        mPaint = shadowPaint()
        bgPaint = bgPaint()
        mPath = Path()
        mRect = RectF()
        newTrianglePath()
    }


    private fun newTrianglePath() {
        trianglePath = Path().apply {
            if (_shortTriangle) {
                val len = mPadding * 0.8f
                val hei = len * tan(40 * 2 * Math.PI / 360).toFloat()
                val x0 = len * 0.2f
                val y0 = hei * 0.2f
                moveTo(x0, -y0)
                lineTo(len, -hei)
                lineTo(len, hei)
                lineTo(x0, y0)
                quadTo(len * 0.09f, 0f, x0, -y0)
                close()
            } else {
                val len = mPadding.toFloat()
                val hei = len * tan(Math.PI / 6f).toFloat()
                val x0 = len * 0.15f
                val y0 = hei * 0.15f
                moveTo(x0, -y0)
                lineTo(len, -hei)
                lineTo(len, hei)
                lineTo(x0, y0)
                quadTo(len * 0.08f, 0f, x0, -y0)
                close()
            }
        }
    }


    private fun shadowPaint() =
        Paint().apply {
            color = shadowColor
            style = Paint.Style.FILL
            strokeCap = Paint.Cap.ROUND
            strokeWidth = mStrokeWidth
            isAntiAlias = true
            isDither = true
            strokeJoin = Paint.Join.MITER
            pathEffect = CornerPathEffect(cornerRadius)
            setLayerType(LAYER_TYPE_SOFTWARE, this)
            setShadowLayer(6f, 2f, 5f, shadowColor)
        }


    private fun bgPaint() =
        Paint().apply {
            color = bgColor
            isAntiAlias = true
            isDither = true
            setLayerType(LAYER_TYPE_SOFTWARE, this)
        }


    fun setBubbleOrientation(orientation: Int) {
        this._orientation = orientation
    }

    fun getBubbleOrientation() = _orientation

    fun setShortTriangle(short: Boolean = true) {
        this._shortTriangle = short
        newTrianglePath()
    }

    fun setTriangleOffset(offset: Float) {
        this.triangleOffset = offset
    }

    fun setBubblePadding(padding: Int) {
        this.mPadding = padding
        newTrianglePath()
        setPadding(padding, padding, padding, padding)
    }

    fun setBubbleBgColor(color: Int) {
        this.bgColor = color
        bgPaint.color = bgColor
        postInvalidate()
    }

    fun setBubbleShadowColor(color: Int) {
        this.shadowColor = color
        mPaint.color = color
        if (color == Color.TRANSPARENT) {
            mPaint.clearShadowLayer()
        }
    }

    fun setCornerRadius(cr: Float) {
        this.cornerRadius = cr
    }


    private fun triangleMatrix(w: Int, h: Int): Matrix {
        var dstX = 0f
        var dstY = 0f
        val m = Matrix()
        when {
            _orientation == Gravity.TOP -> {
                dstX = w / 2f + triangleOffset
                dstY = h + if (_shortTriangle) mPadding * -0.2f else 0f
                m.postRotate(270f)
            }
            _orientation == Gravity.BOTTOM -> {
                dstX = w / 2f + triangleOffset
                dstY = if (_shortTriangle) mPadding * 0.2f else 0f
                m.postRotate(90f)
            }
            (_orientation == Gravity.END && layoutDirection == LAYOUT_DIRECTION_LTR)
                    || (_orientation == Gravity.START && layoutDirection == LAYOUT_DIRECTION_RTL) -> {
//                dstX = w.toFloat()
//                dstY = min(offset, h - minLen)
//                m.postRotate(180f)
            }
        }
        m.postTranslate(dstX, dstY)
        return m
    }


    override fun onDraw(canvas: Canvas) {
        val w = width
        val h = height

        // TODO:  LAYOUT_DIRECTION_RTL
        mRect.set(
            mPadding.toFloat(),
            mPadding.toFloat(),
            (w - mPadding).toFloat(),
            (h - mPadding).toFloat()
        )
        mPath.rewind()
        mPath.addRoundRect(mRect, cornerRadius, cornerRadius, Path.Direction.CW)
        mPath.addPath(trianglePath, triangleMatrix(w, h))
        canvas.drawPath(mPath, mPaint)
        /*  canvas.scale(
              (w - mStrokeWidth) / w,
              (h - mStrokeWidth) / h,
              w.shr(1).toFloat(),
              h.shr(1).toFloat()
          )*/

        canvas.drawPath(mPath, bgPaint)
    }

}