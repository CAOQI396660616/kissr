package com.dubu.rewards.adapters

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.UserBean
import com.dubu.common.manager.UITool
import com.dubu.common.utils.GlideTool
import com.dubu.design.R
import com.ruffian.library.widget.RFrameLayout


class DesignListAdapter :
    BaseQuickAdapter<UserBean, BaseViewHolder>(R.layout.item_design_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: UserBean) {
        /*val ivAvatar = holder.getView<ImageView>(R.id.ivAvatar)
        GlideTool.loadAvatar(item.avatar,ivAvatar)

        val tvName = holder.getView<TextView>(R.id.tvName)*/

    }


}