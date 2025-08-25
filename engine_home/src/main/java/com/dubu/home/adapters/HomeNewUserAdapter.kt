package com.dubu.home.adapters

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.UserBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.manager.UITool
import com.dubu.common.utils.DisplayUiTool
import com.dubu.common.utils.GlideTool
import com.dubu.common.utils.HiLog
import com.dubu.home.R
import com.ruffian.library.widget.RConstraintLayout
import com.ruffian.library.widget.RFrameLayout


class HomeNewUserAdapter :
    BaseQuickAdapter<UserBean, BaseViewHolder>(R.layout.item_home_tab_grid),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            R.id.clHiRoot,
        )
    }

    override fun convert(holder: BaseViewHolder, item: UserBean) {


    }




}