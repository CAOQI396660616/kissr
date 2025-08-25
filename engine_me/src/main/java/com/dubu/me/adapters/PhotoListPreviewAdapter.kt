package com.dubu.me.adapters

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dubu.common.beans.me.KolPostBean
import com.dubu.common.constant.Tag2Common
import com.dubu.common.http.NetWorkConst
import com.dubu.common.utils.GlideTool
import com.dubu.common.utils.HiLog
// import com.dubu.hivideo.video.ShortDetailCoverVideo // 暂时注释掉，缺少hivideo模块
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.dubu.me.R
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack

/**
 * 我的相册适配器
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[PhotoListPreviewAdapter]
 */
class PhotoListPreviewAdapter :
    BaseQuickAdapter<KolPostBean, BaseViewHolder>(R.layout.item_photo_list_preview),
    LoadMoreModule {

    init {
        addChildClickViewIds(
            //            R.id.videoPlayer,
        )
    }

    override fun convert(holder: BaseViewHolder, item: KolPostBean) {

        val gsyVideoPlayer = holder.getView<StandardGSYVideoPlayer>(R.id.gsyVideoPlayer)
        val ivPhoto = holder.getView<ImageView>(R.id.ivPhoto)

        HiLog.e(Tag2Common.TAG_12306, "item.files = ${item.files}")

        if (item.type == "video") {
            ivPhoto.visibility = View.GONE
            gsyVideoPlayer.visibility = View.VISIBLE
            item.files?.let { videoLink ->
                HiLog.e(Tag2Common.TAG_12306, "     item.files videoLink = ${videoLink}")
                //                gsyVideoPlayer.isStartAfterPrepared = true
                gsyVideoPlayer.setThumbPlay(true)
                gsyVideoPlayer.isShowPauseCover = true
                gsyVideoPlayer.isNeedShowWifiTip = false

//                gsyVideoPlayer.setUpLazy(videoLink, true, null, null, videoLink)
                gsyVideoPlayer.setUp(videoLink, true,  videoLink)
//                if (getItemPosition(item) == 0) {
//                    gsyVideoPlayer.isStartAfterPrepared = true
//                    gsyVideoPlayer.startPlayLogic()
//                }

//                gsyVideoPlayer.loadCoverImage(videoLink, R.drawable.iv_default_error_data)

                gsyVideoPlayer.setVideoAllCallBack(object : GSYSampleCallBack() {
                    override fun onPrepared(url: String?, vararg objects: Any?) {
                        super.onPrepared(url, *objects)
                        HiLog.e(Tag2Common.TAG_12312, "onPrepared")
                    }

                    override fun onComplete(url: String?, vararg objects: Any?) {
                        super.onComplete(url, *objects)

                        HiLog.e(Tag2Common.TAG_12312, "onComplete")
                    }

                    override fun onAutoComplete(url: String?, vararg objects: Any?) {
                        super.onAutoComplete(url, *objects)
                        HiLog.e(Tag2Common.TAG_12312, "onAutoComplete")

                    }

                })

            }
        } else {
            ivPhoto.visibility = View.VISIBLE
            gsyVideoPlayer.visibility = View.GONE
            item.files?.let { GlideTool.loadImage(it, ivPhoto) }
        }

    }

    override fun convert(holder: BaseViewHolder, item: KolPostBean, payloads: List<Any>) {
        super.convert(holder, item, payloads)

        val gsyVideoPlayer = holder.getView<StandardGSYVideoPlayer>(R.id.gsyVideoPlayer)
        val ivPhoto = holder.getView<ImageView>(R.id.ivPhoto)

        when (payloads[0]) {
            "selected" -> {
                if (item.type == "video") {
                    ivPhoto.visibility = View.GONE
                    gsyVideoPlayer.visibility = View.VISIBLE
                    item.files?.let { videoLink ->
                        HiLog.e(Tag2Common.TAG_12306, "     item.files videoLink = ${videoLink}")

                        if (getItemPosition(item) == curPos) {
                            gsyVideoPlayer.startPlayLogic()
                        }

                    }
                } else {
                    ivPhoto.visibility = View.VISIBLE
                    gsyVideoPlayer.visibility = View.GONE
                    item.files?.let { GlideTool.loadImage(it, ivPhoto) }
                }

            }

        }
    }

    private var curPos: Int = -1
    fun selected(pos: Int) {
        if (data.isNullOrEmpty())
            return
        curPos = pos
        notifyItemChanged(curPos, "selected")

    }
}