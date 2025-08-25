package com.dubu.rtc.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.R
import com.dubu.common.beans.rtc.VideoRtcResultBean
import com.dubu.common.manager.UITool
import com.dubu.common.utils.EaseDateUtils
import com.dubu.common.utils.GlideTool
import com.ruffian.library.widget.RFrameLayout


class UserCallDataAdapter :
    BaseQuickAdapter<VideoRtcResultBean, BaseViewHolder>(R.layout.item_user_call_data),
    LoadMoreModule {

    init {
        addChildClickViewIds(
//            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: VideoRtcResultBean) {

        val ivAvatar = holder.getView<ImageView>(R.id.ivAvatar)
        val tvName = holder.getView<TextView>(R.id.tvName)
        val tvDuration = holder.getView<TextView>(R.id.tvDuration)

        GlideTool.loadAvatar(item.avatar,ivAvatar)
        val videoDuration = (item.duration ?: 0.00).toInt()
        UITool.changeVideoDurationUI(videoDuration,item.status?:"",tvDuration)





        val nickname = item.nickname ?:""
        if (nickname.isNotEmpty()){
            tvName.text = nickname

        }else{
            tvName.text = context.getString(R.string.app_name)
        }

        val flDot = holder.getView<RFrameLayout>(R.id.flDot)
        UITool.changeOnlineStateUI(item.onlineStatus,flDot)

    }


}