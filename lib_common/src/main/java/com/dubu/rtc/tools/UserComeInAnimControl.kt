package com.dubu.rtc.tools

import android.content.Context
import com.dubu.common.R
import com.dubu.common.beans.UserBean
import com.dubu.common.event.EventKey
import com.dubu.common.event.EventManager
import com.dubu.rtc.tools.AnimHandler.Companion.EVENT_TYPE_START_ANIM
import com.dubu.rtc.tools.AnimHandler.Companion.EVENT_TYPE_START_SVGA
import com.google.android.material.button.MaterialButton
import com.opensource.svgaplayer.SVGAImageView


/**
 * @author Allen
 * @description 用于控制直播间用户进场动画的 队列
 * @time 2021/12/13 20:02
 */
class UserComeInAnimControl(
    private val mContext: Context,
    private val btUserComeIn: MaterialButton,
    private val ivSVGA: SVGAImageView,
) {

    companion object {
        const val SVGA_IS_START = 0 //开始播放
        const val SVGA_IS_NEXT = 1//继续下一个
        const val SVGA_IS_EMPTY = 2//播放完毕
    }

    /**
     * 队列
     */
    private val mGiftQueue: ArrayList<UserBean> = ArrayList()
    private val mSVGAQueue: ArrayList<String> = ArrayList()

    /*
    *  已经开始播放动画了
    * */
    private var mIsStartedAnim: Boolean = false

    /*
    * 已经开始播放SVGA
    * */
    private var mIsStartedSVGA: Boolean = false

    private val mHandler: AnimHandler by lazy {
        AnimHandler.sharedInstance()
    }

    init {
        mHandler.setContext(mContext)
    }

    fun addAnimQueue(gift: UserBean) {
        if (mGiftQueue != null) {
            mGiftQueue.add(gift)
            showUserComeInAnim()
        }
    }

    fun addSVGAQueue(url: String) {
        if (mSVGAQueue != null) {
            mSVGAQueue.add(url)
        }
    }

    /**
     * 显示
     */
    @Synchronized
    fun showUserComeInAnim() {
        if (isEmpty) {
            runnable?.let { mHandler.removeCallbacks(it) }
            return
        }
        check()
    }


    /**
     * 显示SVGA
     */
    @Synchronized
    fun showSVGA() {
        if (isEmptySVGA) {
            runnableSVGA?.let { mHandler.removeCallbacks(it) }
            EventManager.postSticky(EventKey.VOICE_ROOM_SVGA_PLAY, UserComeInAnimControl.SVGA_IS_EMPTY)
            return
        }
        checkSVGA()
    }

    /**
     * 取出
     */
    @get:Synchronized
    private val mTRTCLiveUserInfo: UserBean?
        private get() {
            var mTRTCLiveUserInfo: UserBean? = null
            if (mGiftQueue!!.size != 0) {
                mTRTCLiveUserInfo = mGiftQueue[0]
                mGiftQueue.removeAt(0)
            }
            return mTRTCLiveUserInfo
        }

    /**
     * 取出
     */
    @get:Synchronized
    val mSVGAUrl: String?
        get() {
            var mSVGAUrl: String? = null
            if (mSVGAQueue!!.size != 0) {
                mSVGAUrl = mSVGAQueue[0]
                mSVGAQueue.removeAt(0)
            }
            return mSVGAUrl
        }

    /**
     * 清除所有
     */
    @Synchronized
    fun cleanAll() {
        mGiftQueue?.clear()
    }

    /**
     * 清除所有
     */
    @Synchronized
    fun cleanAllSVGA() {
        mSVGAQueue?.clear()
    }

    /**
     * 是否为空
     */
    @get:Synchronized
    val isEmpty: Boolean
        get() = mGiftQueue == null || mGiftQueue.size == 0

    /**
     * 是否为空
     */
    @get:Synchronized
    val isEmptySVGA: Boolean
        get() = mSVGAQueue == null || mSVGAQueue.size == 0


    private val runnable: Runnable by lazy {
        Runnable {
            if (!isEmpty) {
                sendMsg()
                mIsStartedAnim = true
                runnable?.let { mHandler.postDelayed(it, 3100) }
            } else {
                runnable?.let { mHandler.removeCallbacks(it) }
                mIsStartedAnim = false
            }
        }
    }


    private val runnableSVGA: Runnable by lazy {
        Runnable {
            if (!isEmptySVGA) {
                sendMsgSVGA()
                mIsStartedSVGA = true
                runnableSVGA?.let { mHandler.postDelayed(it, 5000) }
            } else {
                runnableSVGA?.let { mHandler.removeCallbacks(it) }
                mIsStartedSVGA = false
            }
        }
    }

    /**
     * @description 检查以及执行动画
     * @author Allen
     * @time 2021/12/14 9:43
     */
    private fun check() {
        if (!mIsStartedAnim) {
            runnable?.let { mHandler.postDelayed(it, 0) }
        }
    }

    /**
     * @description 检查以及执行svga动画
     * @author Allen
     * @time 2021/12/14 9:43
     */
    private fun checkSVGA() {
        if (!mIsStartedSVGA && !isEmptySVGA) {
            sendMsgSVGA()
        }
    }

    /*private fun checkSVGA() {
        if (!mIsStartedSVGA) {
            runnableSVGA?.let { mHandler.postDelayed(it, 0) }
        }
    }*/

    fun release() {
        if (mHandler != null) {
            runnable?.let { mHandler.removeCallbacks(it) }
            runnableSVGA?.let { mHandler.removeCallbacks(it) }
            mHandler.removeMessages(EVENT_TYPE_START_ANIM)
            mHandler.removeMessages(EVENT_TYPE_START_SVGA)
            cleanAll()
            cleanAllSVGA()
        }
    }


    private fun sendMsg() {
        mTRTCLiveUserInfo?.nickname.let { _ ->
            btUserComeIn.text =
                mContext.getString(R.string.app_name)
        }

        val obtainMessage = mHandler.obtainMessage(EVENT_TYPE_START_ANIM, btUserComeIn)
        mHandler.sendMessage(obtainMessage)
    }


    private fun sendMsgSVGA() {
        ivSVGA.tag = mSVGAUrl.toString()
        val obtainMessage = mHandler.obtainMessage(EVENT_TYPE_START_SVGA, ivSVGA)
        mHandler.sendMessage(obtainMessage)
    }

}