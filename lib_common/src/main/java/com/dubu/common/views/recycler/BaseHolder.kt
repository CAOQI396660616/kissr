package com.dubu.common.views.recycler

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Author:v
 * Time:2020/12/1
 */
open class BaseHolder(view: View) : RecyclerView.ViewHolder(view)

abstract class BaseBindingHolder<T,A:RecyclerView.Adapter<*>>(binding: ViewDataBinding) :
    BaseHolder(binding.root) {
    abstract fun bindData(data: T, adapter: A, position: Int)
}

class EmptyHolder(view: View) : BaseHolder(view)
