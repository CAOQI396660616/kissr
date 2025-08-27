package com.dubu.rewards.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.UserBean
import com.dubu.design.R


class DesignListAdapter :
    BaseQuickAdapter<UserBean, BaseViewHolder>(R.layout.item_design_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: UserBean) {

    }


}