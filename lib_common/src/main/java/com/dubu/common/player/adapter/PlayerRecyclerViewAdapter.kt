package com.dubu.common.player.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dubu.common.R
import com.dubu.common.player.bean.SectionDetailInfo

class PlayerRecyclerViewAdapter: RecyclerView.Adapter<PlayerRecyclerViewAdapter.MyPlayerViewHolder> {

    private var mContext: Context? = null
    private var mVideoListBeanItems: MutableList<SectionDetailInfo> = mutableListOf()

    constructor(context: Context) : super(){
        this.mContext = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPlayerViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_list_player_recyclerview_item, parent, false)
        return MyPlayerViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: MyPlayerViewHolder, position: Int) {
        //如果要绑一个默认封面，可以在这里绑定数据
    }

    override fun getItemCount(): Int {
        return mVideoListBeanItems.size
    }

    private fun getItem(position: Int): SectionDetailInfo? {
        return if (mVideoListBeanItems.isNotEmpty() && position < itemCount) {
            mVideoListBeanItems[position]
        } else null
    }


    fun setData(videoListBeanItems: MutableList<SectionDetailInfo>) {
        this.mVideoListBeanItems = videoListBeanItems
    }

    class MyPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView: ViewGroup
        init {
            rootView = itemView.findViewById(R.id.root_view)
        }
    }
}