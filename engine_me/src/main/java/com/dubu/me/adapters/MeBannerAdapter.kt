package com.dubu.me.adapters

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.utils.GlideTool
import com.dubu.me.R

class MeBannerAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_me_banner),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        val ivCover = holder.getView<ImageView>(R.id.ivCover)
        GlideTool.loadImage(item,ivCover)
    }

}