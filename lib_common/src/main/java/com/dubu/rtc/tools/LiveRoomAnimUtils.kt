package com.dubu.rtc.tools

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import com.even.commonrv.utils.DisplayUtil
import org.dync.giftlibrary.util.GiftAnimationUtil

/**
 * Created by Allen on 2017/7/7.
 */
object LiveRoomAnimUtils {

    @JvmStatic
    fun startUserComeInLiveRoomAnim(viewAnim: View? , callAnimStart:()->Unit = {},callAnimEnd:()->Unit = {}) {
        if (null == viewAnim)
            return
        //用户名称飞入
//                val userComeInRoomViewFlyInStep1Anim =
//                    GiftAnimationUtil.createFlyFromLtoR(dataBinding.btUserComeIn,
//                        DisplayUtil.getScreenWidth().toFloat(),
//                        80f,
//                        1500,
//                        DecelerateInterpolator(7.5F))
        val userComeInRoomViewFlyInStep1Anim =
            GiftAnimationUtil.createFlyFromLtoR(viewAnim!!,
                DisplayUtil.getScreenWidth().toFloat() - viewAnim!!.measuredWidth - 80,
                80f,
                1200,
                OvershootInterpolator(0.7F)
            )
        userComeInRoomViewFlyInStep1Anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                callAnimStart.invoke()
            }

            override fun onAnimationEnd(animation: Animator) {
            }
        })

        val userComeInRoomViewAlphaStep1Anim =
            GiftAnimationUtil.createAlphaAnimator(viewAnim!!, 300, 0)

        val userComeInRoomViewFlyIOutStep2Anim =
            GiftAnimationUtil.createFlyFromLtoR(viewAnim!!,
                80F,
                0F,
                1200,
                LinearInterpolator()
            )

        val userComeInRoomViewFlyIOutStep3Anim =
            GiftAnimationUtil.createFlyFromLtoR(viewAnim!!,
                0F,
                -DisplayUtil.getScreenWidth().toFloat(),
                500,
                DecelerateInterpolator(8F)
            )

        userComeInRoomViewFlyIOutStep3Anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                callAnimEnd.invoke()
            }
        })

        GiftAnimationUtil.startTogetherAnimation(userComeInRoomViewFlyInStep1Anim,
            userComeInRoomViewAlphaStep1Anim,
            userComeInRoomViewFlyIOutStep2Anim,
            userComeInRoomViewFlyIOutStep3Anim
        )
    }
}