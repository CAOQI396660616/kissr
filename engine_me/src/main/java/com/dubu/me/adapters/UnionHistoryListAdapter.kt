package com.dubu.me.adapters

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.base.BaseApp
import com.dubu.common.beans.me.KolUnionHistoryListBean
import com.dubu.common.manager.UITool
import com.dubu.common.utils.GlideTool
import com.dubu.me.R

/**
 * 我的入会历史适配器
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[UnionHistoryListAdapter]
 */
class UnionHistoryListAdapter :
    BaseQuickAdapter<KolUnionHistoryListBean, BaseViewHolder>(R.layout.item_union_history_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: KolUnionHistoryListBean) {


        val ivImage = holder.getView<ImageView>(R.id.iv_avatar)
        GlideTool.loadImageWithDefault(item.union?.cover, ivImage)

        val tvTeamName = holder.getView<TextView>(R.id.tvTeamName)
        tvTeamName.text = item.union?.name
        val tvTeamDesc = holder.getView<TextView>(R.id.tvTeamDesc)
        tvTeamDesc.text = "ID:${item.union?.id.toString()}"

        val tvStatus = holder.getView<TextView>(R.id.tvStatus)
        UITool.changeUnionHistoryUI(item.status, tvStatus)


        val tvBegin1 = holder.getView<TextView>(R.id.tvBegin1)
        tvBegin1.text = BaseApp.instance.getString(com.dubu.common.R.string.join_union_apply_time)

        val tvBegin2 = holder.getView<TextView>(R.id.tvBegin2)
        tvBegin2.text = item.created_at

        val tvOver1 = holder.getView<TextView>(R.id.tvOver1)
        when (item.status) {

            UITool.UNION_STATUS_EXIT_INVALID ->{
                tvOver1.text = BaseApp.instance.getString(com.dubu.common.R.string.join_union_invalid_time)
            }

            else->{
                tvOver1.text = BaseApp.instance.getString(com.dubu.common.R.string.join_union_pending_time)
            }
        }


        val tvOver2 = holder.getView<TextView>(R.id.tvOver2)
        tvOver2.text = item.updated_at

    }

}