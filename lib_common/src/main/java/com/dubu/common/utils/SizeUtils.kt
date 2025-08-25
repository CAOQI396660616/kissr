package com.dubu.common.utils

import android.view.ViewConfiguration
import com.dubu.common.base.BaseApp


/**
 * 根据手机的分辨率将dp转成为px。
 */
fun dp2px(dp: Float): Int {
    return (dp * density + 0.5f).toInt()
}

val density: Float
    get() = BaseApp.instance.resources.displayMetrics.density

/**
 * 获取屏幕宽值。
 */
val screenWidth
    get() = BaseApp.instance.resources.displayMetrics.widthPixels

/**
 * 获取屏幕高值。
 */
val screenHeight
    get() = BaseApp.instance.resources.displayMetrics.heightPixels


val touchThreshold
    get() = ViewConfiguration.get(BaseApp.instance).scaledTouchSlop

/**
 * 状态栏高度
 */
val statusBarHeight
    get() = run {
        val resourceId: Int =
            BaseApp.instance.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            BaseApp.instance.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

/**
 * 导航栏高度
 */
val navigationBarHeight
    get() = run {
        val resourceId: Int =
            BaseApp.instance.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            BaseApp.instance.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

val densitySp: Float
    get() = BaseApp.instance.resources.displayMetrics.scaledDensity

fun sp2px(sp: Float): Int {
    return (sp * densitySp + 0.5f).toInt()
}

