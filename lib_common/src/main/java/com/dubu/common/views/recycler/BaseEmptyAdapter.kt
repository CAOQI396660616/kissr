package com.dubu.common.views.recycler

import android.view.ViewGroup
import com.dubu.common.views.cview.EmptyView

/**
 * Author:v
 * Time:2020/12/7
 *
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseEmptyAdapter<T, VH : BaseHolder> : BaseAdapter<T, BaseHolder>() {
    private val VIEW_TYPE_EMPTY = Integer.MAX_VALUE - 1


    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        if (viewType == VIEW_TYPE_EMPTY) {
            return EmptyHolder(
                EmptyView(parent.context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                })
        }

        return getRealViewHolder(parent, viewType)
    }

    abstract fun getRealViewHolder(parent: ViewGroup, viewType: Int): VH


    final override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (holder is EmptyHolder) {
            onBindEmptyHolder(holder.itemView as EmptyView, position)
        } else bindRealHolder(holder as VH, position)
    }


    abstract fun bindRealHolder(holder: VH, position: Int)
    open fun onBindEmptyHolder(view: EmptyView, position: Int) {}


    final override fun getItemCount(): Int {
        return if (items.size == 0) {
            1
        } else {
            items.size
        }
    }

    fun isShowEmpty() = items.size == 0

    fun getRealItemCount() = items.size

    final override fun getItemViewType(position: Int): Int {
        if (items.size == 0) {
            return VIEW_TYPE_EMPTY
        }

        return getRealItemViewType(position)
    }

    /**
     * NOTE:don't conflict with @see #VIEW_TYPE_EMPTY
     */
    open fun getRealItemViewType(position: Int): Int = 0x1
}