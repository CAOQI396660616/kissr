package com.dubu.common.views.recycler

import android.view.View
import android.view.ViewGroup
import com.dubu.common.views.cview.EmptyView

/**
 * Author:v
 * Time:2020/12/7
 *
 */
@Suppress("UNCHECKED_CAST")
abstract class BasePullAdapter<T, VH : BaseHolder> : BaseAdapter<T, BaseHolder>() {
    private val VIEW_TYPE_EMPTY = Integer.MAX_VALUE - 1

    private var loading = true

    fun showEmpty() {
        loading = false
        notifyItemChanged(0)
    }

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

        return createRealViewHolder(parent, viewType)
    }

    abstract fun createRealViewHolder(parent: ViewGroup, viewType: Int): VH


    final override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (holder is EmptyHolder) {
            if (loading) {
                holder.itemView.visibility = View.INVISIBLE
            } else {
                holder.itemView.visibility = View.VISIBLE
                onBindEmptyHolder(holder.itemView as EmptyView, position)
            }
        } else bindRealHolder(holder as VH, position)
    }


    abstract fun bindRealHolder(holder: VH, position: Int)
    open fun onBindEmptyHolder(view: EmptyView, position: Int) {}


    final override fun getItemCount(): Int {
        return if (useEmpty() && items.size == 0) {
            1
        } else {
            items.size
        }
    }

    open fun useEmpty(): Boolean = true

    fun isShowEmpty() = items.size == 0

    fun getRealItemCount() = items.size

    final override fun getItemViewType(position: Int): Int {
        if (useEmpty() && items.size == 0) {
            return VIEW_TYPE_EMPTY
        }

        return getRealItemViewType(position)
    }

    /**
     * NOTE:don't conflict with @see [VIEW_TYPE_EMPTY]
     */
    open fun getRealItemViewType(position: Int): Int = 0x1


    fun refresh(list: List<T>?) {
        val os = items.size
        if (list.isNullOrEmpty()) {
            loading = false
            if (os > 0) {
                items.clear()
                notifyItemRangeRemoved(0, os)
            } else {
                notifyItemChanged(0)
            }
            return
        }
        items.clear()
        items.addAll(list)
        val ns = list.size
        if (os == 0) {
            if (useEmpty()) {
                notifyItemChanged(0)
                notifyItemRangeInserted(1, ns)
            } else {
                notifyItemRangeInserted(0, ns)
            }
        } else if (os < ns) {
            notifyItemRangeChanged(0, os)
            notifyItemRangeInserted(os, ns)
        } else if (os > ns) {
            notifyItemRangeRemoved(ns, os)
            notifyItemRangeChanged(0, os)
        } else {
            notifyItemRangeChanged(0, ns)
        }
    }

    fun loadMore(list: List<T>?) {
        if (list.isNullOrEmpty()) return
        addData(list)
    }
}