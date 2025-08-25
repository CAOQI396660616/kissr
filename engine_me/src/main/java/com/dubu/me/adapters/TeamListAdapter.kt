package com.dubu.me.adapters

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.me.KolUnionListBean
import com.dubu.common.manager.UITool
import com.dubu.common.utils.GlideTool
import com.dubu.me.R

/**
 * 我的工会适配器
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[TeamListAdapter]
 */
class TeamListAdapter :
    BaseQuickAdapter<KolUnionListBean, BaseViewHolder>(R.layout.item_team_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: KolUnionListBean) {
        val tvName = holder.getView<TextView>(R.id.tvName)
        tvName.text = item.name

        val ivImage = holder.getView<ImageView>(R.id.iv_avatar)
        GlideTool.loadImageWithDefault(item.cover, ivImage)

        val tvNext = holder.getView<TextView>(R.id.tvNext)
        if (item.member == null) {
            UITool.changeUnionUI("", tvNext)
        } else {
            UITool.changeUnionUI(item.member?.status, tvNext)
        }
    }

    override fun convert(holder: BaseViewHolder, item: KolUnionListBean, payloads: List<Any>) {
        super.convert(holder, item, payloads)

        when (payloads[0]) {
            "joinClick" ->{
                val tvNext = holder.getView<TextView>(R.id.tvNext)
                UITool.changeUnionUI(UITool.UNION_STATUS_REVIEW, tvNext)
            }
        }
    }

    fun changUiForJoin(position: Int) {
        data[position].member?.status = UITool.UNION_STATUS_REVIEW
        notifyItemChanged(position,"joinClick")
    }

}