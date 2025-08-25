package com.dubu.me.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.me.R

/**
 * 我的服务列表适配器
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[CashCompanyListAdapter]
 */
class CashCompanyListAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_cash_company_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: String) {
    }

}