package com.dubu.me.activitys

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.dubu.common.base.BaseBindingActivity
import com.dubu.common.constant.Tag2Common
import com.dubu.common.player.adapter.PagerLayoutManager
import com.dubu.common.player.listener.OnViewPagerListener
import com.dubu.common.utils.HiLog
import com.dubu.common.utils.HiRealCache
import com.dubu.common.utils.hi.HiStatusBarTool
// import com.dubu.hivideo.video.ShortDetailCoverVideo // 暂时注释掉，缺少hivideo模块
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.dubu.me.R
import com.dubu.me.adapters.PhotoListPreviewAdapter
import com.dubu.me.databinding.ActivityMePhotoListPreviewBinding
import com.shuyu.gsyvideoplayer.GSYVideoManager


/**
 * 横向预览相册页面
 *
 * @author cq
 * @date 2025/06/06
 * @constructor 创建[MyPhotoListPreviewActivity]
 */
class MyPhotoListPreviewActivity : BaseBindingActivity<ActivityMePhotoListPreviewBinding>() {

    companion object {
        fun startAction(context: Activity) {
            val intent = Intent(context, MyPhotoListPreviewActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun isNeedDefaultScreenConfig(): Boolean {
        return false
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_me_photo_list_preview
    }

    override fun onCreated() {

        initView()
        initClick()
    }


    private fun initView() {
        findViewById<View>(com.dubu.me.R.id.vTop)?.let {
            initOptBar(it, isStatusBarDarkFont = true, isKeyboardEnable = false)
        }

        initPagerLayoutManager()

        binding?.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = mPagerLayoutManager
            adapter = mRecyclerViewAdapter
        }
        mRecyclerViewAdapter.setList(HiRealCache.photoList)




    }

    private fun initClick() {
        //返回按键
        ClickUtils.applySingleDebouncing(binding.ivBack) {
            finish()
        }


    }



    override fun onStop() {
        super.onStop()

        val videoManager = GSYVideoManager.instance()
        videoManager.pause()
    }

    override fun onResume() {
        super.onResume()

        val videoManager = GSYVideoManager.instance()
        videoManager.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        val videoManager = GSYVideoManager.instance()
        videoManager.releaseMediaPlayer()

        HiRealCache.photoList = null
    }

    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */

    private var mPagerLayoutManager: PagerLayoutManager? = null

    private val mRecyclerViewAdapter: PhotoListPreviewAdapter by lazy {
        PhotoListPreviewAdapter()
    }

    /**
     * 创建layoutManager给RecyclerView使用
     */
    private fun initPagerLayoutManager() {
        if (mPagerLayoutManager == null) {
            mPagerLayoutManager = PagerLayoutManager(
                this@MyPhotoListPreviewActivity)
            mPagerLayoutManager?.isItemPrefetchEnabled = true
        }
        mPagerLayoutManager?.let { layoutManager ->
            layoutManager.setOnViewPagerListener(object : OnViewPagerListener {
                override fun onInitComplete() {
                    val itemCount = mRecyclerViewAdapter?.itemCount ?: 0
                    HiLog.e(Tag2Common.TAG_12312, "     onInitComplete itemCount = $itemCount")

                    binding?.recyclerView?.postDelayed({
                        binding.recyclerView.scrollToPosition(HiRealCache.photoListShowIndex)

//                        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
//                        layoutManager.scrollToPositionWithOffset(HiRealCache.photoListShowIndex, 0)

                        mRecyclerViewAdapter.selected(HiRealCache.photoListShowIndex)

                    },20)
                }

                override fun onPageRelease(isNext: Boolean, position: Int, view: View) {
                    HiLog.e(Tag2Common.TAG_12312, "     onPageRelease position = ${position} = ${view}")
                }

                override fun onPageSelected(position: Int, bottom: Boolean, view: View) {
                    HiLog.e(Tag2Common.TAG_12312, "     onPageSelected position = ${position} = ${view}")
                    val item = mRecyclerViewAdapter.data[position]


                    val gsyVideoPlayer = view.findViewById<StandardGSYVideoPlayer>(R.id.gsyVideoPlayer)
                    val ivPhoto = view.findViewById<ImageView>(R.id.ivPhoto)


                    if (item.type == "video") {

                    }else{
                        val videoManager = GSYVideoManager.instance()
                        videoManager.pause()
                    }

                    if (curPos == position){

                    }else{
                        curPos = position

                        if (item.type == "video") {
                            ivPhoto.visibility = View.GONE
                            gsyVideoPlayer.visibility = View.VISIBLE
                            item.files?.let { videoLink ->
                                HiLog.e(Tag2Common.TAG_12306, "     item.files videoLink = ${videoLink}")
                                gsyVideoPlayer.startPlayLogic()
                            }
                        }

                    }



                }
            })
        }
    }
    private var curPos: Int = -1

}
