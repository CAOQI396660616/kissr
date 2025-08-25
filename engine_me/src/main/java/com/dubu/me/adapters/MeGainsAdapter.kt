package com.dubu.me.adapters

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.utils.GlideTool
import com.dubu.me.R

class MeGainsAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_me_gains),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: String) {
    }

}