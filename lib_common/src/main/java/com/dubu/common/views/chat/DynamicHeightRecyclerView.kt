package com.dubu.common.views.chat

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.dubu.common.R

/**
 *
 * 自适应高度的Rv
 * 低于maxItemCount 自适应高度
 * 高于maxItemCount 固定高度
 * @author cq
 * @date 2025/07/02
 * @constructor 创建[DynamicHeightRecyclerView]
 * @param [context]
 * @param [attrs]
 * @param [defStyleAttr]
 */
class DynamicHeightRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var maxHeight: Int = 0
    private var minHeight: Int = 0
    private var maxItemCount = 3

    init {
        // 从 XML 属性中获取值
        val a = context.obtainStyledAttributes(attrs, R.styleable.DynamicHeightRecyclerView)
        maxHeight = a.getDimensionPixelSize(
            R.styleable.DynamicHeightRecyclerView_maxHeight,
            resources.getDimensionPixelSize(R.dimen.dp_300)
        )
        minHeight = a.getDimensionPixelSize(
            R.styleable.DynamicHeightRecyclerView_minHeight,
            resources.getDimensionPixelSize(R.dimen.dp_80)
        )
        maxItemCount = a.getInt(R.styleable.DynamicHeightRecyclerView_maxItemCount, 3)
        a.recycle()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var newHeightSpec = heightSpec

        // 获取适配器中的项目数量
        val adapter = adapter
        if (adapter != null) {
            val itemCount = adapter.itemCount

            when {
                itemCount == 0 -> {
                    // 没有项目时使用最小高度
                    newHeightSpec = MeasureSpec.makeMeasureSpec(minHeight, MeasureSpec.EXACTLY)
                }
                itemCount <= maxItemCount -> {
                    // 项目数小于等于阈值时使用自适应高度
                    super.onMeasure(widthSpec, heightSpec)
                    val measuredHeight = measuredHeight

                    // 确保不低于最小高度
                    if (measuredHeight < minHeight) {
                        newHeightSpec = MeasureSpec.makeMeasureSpec(minHeight, MeasureSpec.EXACTLY)
                    } else {
                        return // 已经测量完成
                    }
                }
                else -> {
                    // 超过阈值时使用最大高度
                    newHeightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY)
                }
            }
        }

        super.onMeasure(widthSpec, newHeightSpec)
    }
}