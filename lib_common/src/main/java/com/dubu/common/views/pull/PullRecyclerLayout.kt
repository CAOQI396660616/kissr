package com.dubu.common.views.pull

import android.content.Context
import android.util.AttributeSet

/**
 * Author:v
 * Time:2021/2/26
 */
class PullRecyclerLayout : RefreshRecyclerLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun initView(context: Context) {
        super.initView(context)

        val footer = LoadFooterView(context)
        setRefreshFooter(footer)
        setHeaderMaxDragRate(1.5f)
        setFooterMaxDragRate(2.0f)
        setDragRate(0.7f)
    }

}