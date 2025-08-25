package com.dubu.common.views.recycler

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Author:v
 * Time:2020/12/10
 */
class GridDecoration : RecyclerView.ItemDecoration {
    private val TAG = "GridDecoration"

    private val mPaint: Paint
    private var dividerH: Int = 0
    private var dividerW: Int = 0
    private var spanCount = 0


    constructor(
        divideW: Int,
        divideH: Int = divideW,
        @ColorInt dividerColor: Int = Color.TRANSPARENT
    ) {
        this.dividerW = divideW
        this.dividerH = divideH

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.color = dividerColor
            it.style = Paint.Style.FILL
        }
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (spanCount <= 1) {
            spanCount = getSpanCount(parent)
        }
        if (spanCount <= 1) return

        val pos = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val childCount = parent.adapter?.itemCount ?: 0
        when {
            isLastColumn(parent, pos) -> {
                outRect.set(0, 0, 0, dividerH)
            }
              isLastRow(parent, childCount, pos) -> {
                  outRect.set(0, 0, dividerW, 0)
              }
            else -> {
                outRect.set(0, 0, dividerW, dividerH)
            }
        }

    }

    private fun isLastColumn(parent: RecyclerView, pos: Int): Boolean {
        parent.layoutManager?.let {
            if (it is GridLayoutManager) {
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }


    private fun isLastRow(parent: RecyclerView, childCount: Int, pos: Int): Boolean {
        parent.layoutManager?.let {

            if (it is GridLayoutManager) {
                val lines = if (childCount % spanCount == 0) {
                    childCount / spanCount
                } else {
                    childCount / spanCount + 1
                }
                return lines == pos / spanCount + 1
            }
        }
        return false
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (spanCount <= 1) return
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            drawHorizontalDivider(c, child, params)
            drawVerticalDivider(c, child, params)
        }
    }

    private fun drawHorizontalDivider(
        canvas: Canvas,
        child: View,
        params: RecyclerView.LayoutParams
    ) {
        val left = child.left - params.leftMargin
        val right = child.right + params.rightMargin + dividerW
        val top = child.bottom + params.bottomMargin
        val bottom = top + dividerH
        canvas.drawRect(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            bottom.toFloat(),
            mPaint
        )
    }

    private fun drawVerticalDivider(
        canvas: Canvas,
        child: View,
        params: RecyclerView.LayoutParams
    ) {
        val top = child.top - params.topMargin
        val bottom = child.bottom + params.bottomMargin
        val left = child.right + params.rightMargin
        val right = left + dividerW
        canvas.drawRect(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            bottom.toFloat(),
            mPaint
        )
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        return when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> layoutManager.spanCount
            is StaggeredGridLayoutManager -> layoutManager.spanCount
            else -> -1
        }
    }
}