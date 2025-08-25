package com.dubu.common.utils

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Author:v
 * Time:2023/6/21
 */
class RotateAnimHelper {
    var anim: ObjectAnimator? = null
    var iv: View? = null

    fun setup(iv: View) {
        this.iv = iv
    }

    fun start() {
        val v = iv ?: return

        if (anim == null) {
            anim = ObjectAnimator.ofFloat(v, View.ROTATION, 0f, 360f).apply {
                interpolator = LinearInterpolator()
                repeatCount = ValueAnimator.INFINITE
                duration = 4000L
            }
        }

        anim!!.start()
    }


    fun pauseAnim() {
        anim?.pause()
    }

    fun resumeAnim() {
        anim?.resume()
    }


    fun destory() {
        try {
            anim?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        anim = null
        iv = null
    }


}