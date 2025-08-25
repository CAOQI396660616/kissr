package com.dubu.me.adapters

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.me.KolServiceBean
import com.dubu.common.utils.TextViewUtils
import com.dubu.me.R

/**
 * 我的服务列表适配器
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[ServiceListAdapter]
 */
class ServiceListAdapter :
    BaseQuickAdapter<KolServiceBean, BaseViewHolder>(R.layout.item_service_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(
                        R.id.ivEdit,
                        R.id.ivDelete,
        )
    }

    override fun convert(holder: BaseViewHolder, item: KolServiceBean) {
        val sName = holder.getView<TextView>(R.id.tv1)
        sName.text = item.service_name
        TextViewUtils.setMidBold(sName)
        val sPrice = holder.getView<TextView>(R.id.tv2)
        sPrice.text = item.gold_price.toString()
    }

}