package com.dubu.common.utils

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver

object ViewDistanceUtils {

    /**
     * 获取View左上角到屏幕底部的距离（包含虚拟导航栏）
     * @param view 目标View
     * @param activity 所在Activity
     * @param callback 回调函数，返回距离值
     */
    fun getViewTopToScreenBottomDistance(
        view: View,
        activity: Activity,
        callback: (Int) -> Unit
    ) {
        // 确保View已经完成布局
        if (view.isLaidOut) {
            calculateDistance(view, activity, callback)
        } else {
            view.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    calculateDistance(view, activity, callback)
                }
            })
        }
    }

    private fun calculateDistance(view: View, activity: Activity, callback: (Int) -> Unit) {
        // 获取屏幕总高度（包含虚拟导航栏）
        val screenHeight = getScreenHeight(activity)

        // 获取View在屏幕中的位置
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewTop = location[1]

        // 计算View顶部到屏幕底部的距离
        val distance = screenHeight - viewTop
        callback(distance)
    }

    /**
     * 获取屏幕总高度（包含虚拟导航栏）
     */
     fun getScreenHeight(activity: Activity): Int {
        val displayMetrics = activity.resources.displayMetrics
        return displayMetrics.heightPixels
    }

    /**
     * 同步版本 - 注意：需要在View完成布局后调用
     */
    fun getViewTopToScreenBottomDistanceSync(view: View, activity: Activity): Int {
        val screenHeight = getRealScreenHeight(activity)
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return screenHeight - location[1]
    }


    @Suppress("DEPRECATION")
    fun getRealScreenHeight(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.windowManager.currentWindowMetrics.bounds.height()
        } else {
            val display = activity.windowManager.defaultDisplay
            val metrics = DisplayMetrics()
            display.getRealMetrics(metrics)
            metrics.heightPixels
        }
    }

}