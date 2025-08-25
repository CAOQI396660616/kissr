package com.dubu.common.views.recycler

import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

/**
 * Author:v
 * Time:2020/12/1
 */
abstract class BaseAdapter<T, VH : BaseHolder> : RecyclerView.Adapter<VH>() {
    protected var items: ArrayList<T> = ArrayList()


    override fun getItemCount(): Int = items.size


    fun getItem(position: Int): T {
        return items[position]!!
    }

    fun getDataList(): ArrayList<T> = items

    fun addData(items: List<T>) {
        val c = items.size
        this.items.addAll(items)
        notifyItemRangeChanged(c, items.size)
    }


    fun setData(index: Int, data: T) {
        if (index >= items.size || index < 0) {
            return
        }
        items[index] = data
        notifyItemChanged(index)
    }

    fun addData(@IntRange(from = 0) position: Int, data: T) {
        items.add(position, data)
        notifyItemInserted(position)
    }


    fun addData(@NonNull data: T) {
        items.add(data)
        notifyItemInserted(items.size)
    }

    fun remove(position: Int) {
        if (position >= items.size || position < 0) {
            return
        }
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size - position)
    }

    fun remove(item: T) {
        val position = items.indexOf(item)
        if (position == -1) return
        remove(position)
    }


}