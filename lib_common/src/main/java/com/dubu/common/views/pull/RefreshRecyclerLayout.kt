package com.dubu.common.views.pull

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.dubu.common.R

/**
 * Author:v
 * Time:2021/2/26
 */
open class RefreshRecyclerLayout : SmartRefreshLayout {
    constructor(context: Context) : this(context, null)

    @Suppress("LeakingThis")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }


    lateinit var recyclerView: RecyclerView

    open fun initView(context: Context) {
        recyclerView = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            overScrollMode = OVER_SCROLL_NEVER
        }

        setRefreshHeader(
            MaterialHeader(context)
                .setColorSchemeResources(R.color.cl9749FE)
        )//no need set width and height,maybe
        setEnableLoadMoreWhenContentNotFull(false)
        setEnableAutoLoadMore(true)
        addView(recyclerView, LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}