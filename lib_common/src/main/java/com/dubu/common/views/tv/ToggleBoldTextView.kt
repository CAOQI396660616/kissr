package com.dubu.common.views.tv

import android.content.Context
import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.MetricAffectingSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.dubu.common.R

class ToggleBoldTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    // 保存原始画笔状态
    private val originalStyle = paint.style
    private val originalStrokeWidth = paint.strokeWidth

    // 设置混合文本
    fun setMixedText(vararg parts: Pair<String, Boolean>) {
        val fullText = buildString {
            parts.forEach { (text, _) -> append(text) }
        }

        val spannable = SpannableString(fullText)
        var currentPosition = 0

        parts.forEach { (text, isBold) ->
            val endPosition = currentPosition + text.length

            if (isBold) {
                spannable.setSpan(
                    MidBoldSpan(originalStyle, originalStrokeWidth),
                    currentPosition,
                    endPosition,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                val color = ContextCompat.getColor(context, R.color.cl16171A)
                spannable.setSpan(
                    ForegroundColorSpan(color),
                    currentPosition,
                    endPosition,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

            } else {
                spannable.setSpan(
                    NormalSpan(),
                    currentPosition,
                    endPosition,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )


                val color = ContextCompat.getColor(context, R.color.cl6E7380)
                spannable.setSpan(
                    ForegroundColorSpan(color),
                    currentPosition,
                    endPosition,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            currentPosition = endPosition
        }

        text = spannable
    }



}


// 中粗样式 Span
private class MidBoldSpan(
    private val originalStyle: Paint.Style,
    private val originalStrokeWidth: Float
) : MetricAffectingSpan() {

    override fun updateDrawState(tp: TextPaint) {
        applyStyle(tp)
    }

    override fun updateMeasureState(tp: TextPaint) {
        applyStyle(tp)
    }

    private fun applyStyle(paint: TextPaint) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = 0.7f
    }
}

// 普通样式 Span
private class NormalSpan : MetricAffectingSpan() {
    override fun updateDrawState(tp: TextPaint) {
        // 默认实现已是普通样式
    }

    override fun updateMeasureState(tp: TextPaint) {
        // 默认实现已是普通样式
    }
}


