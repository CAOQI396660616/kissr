package com.dubu.common.views.tv

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet

class ScrollableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    private var maxLines = 8

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        this.maxLines = maxLines
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 先测量原始高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 检查是否需要滚动
        if (lineCount > maxLines) {
            // 计算3行高度
            val threeLineHeight = lineHeight * maxLines
            setMeasuredDimension(measuredWidth, threeLineHeight)
            movementMethod = ScrollingMovementMethod.getInstance()
        }
    }
}