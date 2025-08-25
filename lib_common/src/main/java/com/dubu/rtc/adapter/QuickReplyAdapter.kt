package com.dubu.rtc.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.R

class QuickReplyAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_quick_replay),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            R.id.tvTitle,
            R.id.viewEdit,
        )
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        val tvTitle = holder.getView<TextView>(R.id.tvTitle)
        tvTitle.text = item
    }

}