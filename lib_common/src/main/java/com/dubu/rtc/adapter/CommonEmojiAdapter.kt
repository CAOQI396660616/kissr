package com.dubu.rtc.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.R
import com.dubu.rtc.model.EaseEmojicon

class CommonEmojiAdapter :
    BaseQuickAdapter<EaseEmojicon, BaseViewHolder>(R.layout.item_emoji_common),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: EaseEmojicon) {
        val iv = holder.getView<ImageView>(R.id.iv_expression)
        iv.setImageResource(item.icon)
    }

}