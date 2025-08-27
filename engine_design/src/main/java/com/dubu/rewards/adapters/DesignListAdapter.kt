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
        // 绑定头像
        val ivAvatar = holder.getView<ImageView>(R.id.ivAvatar)
        GlideTool.loadAvatar(item.avatar, ivAvatar)

        // 绑定用户名
        val tvName = holder.getView<TextView>(R.id.tvName)
        tvName.text = item.nickname ?: "Unknown"

        // 绑定描述信息
        val tvTip = holder.getView<TextView>(R.id.tvTip)
        tvTip.text = when {
            !item.email.isNullOrEmpty() -> item.email
            !item.usagePurpose.isNullOrEmpty() -> {
                when (item.usagePurpose) {
                    "1" -> "与朋友聊天"
                    "2" -> "搭讪女孩"
                    "3" -> "玩社交平台"
                    else -> "ID:${item.userSn}"
                }
            }
            else -> "ID:${item.userSn}"
        }

        // 绑定在线状态点
        val flDot = holder.getView<RFrameLayout>(R.id.flDot)
        // 这里可以根据用户在线状态设置可见性，暂时默认显示
        flDot.visibility = android.view.View.VISIBLE

        // 绑定Hi按钮点击事件
        val clHi = holder.getView<com.ruffian.library.widget.RConstraintLayout>(R.id.clHi)
        clHi.setOnClickListener {
            // 处理Hi按钮点击事件
            com.dubu.common.utils.HiLog.d("DesignListAdapter", "Hi button clicked for user: ${item.nickname}")
            // 这里可以添加具体的Hi功能，比如发送消息等
        }
    }


}