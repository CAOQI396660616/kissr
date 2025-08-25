package com.dubu.common.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Author:v
 * Time:2021/2/5
 */
object DisplayMetricsTool {


    @JvmStatic
    fun px2dp(context: Context, px: Int): Float {
        val scale: Float = context.resources.displayMetrics.density
        return px / scale + 0.5f
    }


    @JvmStatic
    fun px2sp(context: Context, px: Int): Float {
        val scale: Float = context.resources.displayMetrics.scaledDensity
        return px / scale + 0.5f
    }


    @JvmStatic
    fun dp2px(context: Context, dp: Float): Float {
        val metrics = getDisplayMetrics(context) ?: return 0f
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
    }


    @JvmStatic
    fun sp2px(context: Context, sp: Float): Float {
        val metrics = getDisplayMetrics(context) ?: return 0f
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics)
    }


    @JvmStatic
    fun getScreenWH(context: Context): IntArray {
        val metrics = getDisplayMetrics(context)
            ?: return intArrayOf(0, 0)
        return intArrayOf(metrics.widthPixels, metrics.heightPixels)
    }

    @JvmStatic
    private fun getDisplayMetrics(context: Context): DisplayMetrics? {
        return context.resources.displayMetrics
    }


    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val metrics = getDisplayMetrics(context) ?: return 0
        return metrics.widthPixels
    }


    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val dm = getDisplayMetrics(context) ?: return 0
        return dm.heightPixels
    }

    @JvmStatic
    fun getDensityDpi(context: Context): Int {
        return getDisplayMetrics(context)?.densityDpi ?: 0
    }

    @JvmStatic
    fun getScreenWidthDp(context: Context): Int {
        val metrics = getDisplayMetrics(context) ?: return 0
        return (metrics.widthPixels / metrics.density).toInt()
    }

    @JvmStatic
    fun getScreenHeightDp(context: Context): Int {
        val metrics = getDisplayMetrics(context) ?: return 0
        return (metrics.heightPixels / metrics.density).toInt()
    }

}