package com.dubu.common.utils

import android.util.Log
import com.dubu.common.BuildConfig

/**
 * Author:v
 * Time:2021/8/5
 */
object HiLog {
    private fun isDebug(): Boolean {

        return BuildConfig.DEBUG
//        return true
//        return false
    }


    @JvmStatic
    fun i(tag: String, msg: String?) {
        if (isDebug()) Log.i(tag, msg!!)
    }

    @JvmStatic
    fun d(tag: String, msg: String?) {
        if (isDebug()) Log.d(tag, msg!!)
    }


    @JvmStatic
    fun w(tag: String, msg: String?) {
        if (isDebug()) Log.w(tag, msg!!)
    }

    @JvmStatic
    fun e(tag: String, msg: String?) {
        Log.e(tag, msg!!)
    }

    @JvmStatic
    fun l(tag: String, longString: String) {
        var msg = longString
        if (!isDebug()) return
        msg = msg.trim { it <= ' ' }
        val maxLength = 2500
        if (msg.length < maxLength) {
            Log.w(tag, msg)
        } else {
            val num = msg.length / maxLength + 1
            for (i in 0 until num) {
                if (num - 1 == i) {
                    Log.w(tag, msg.substring(i * maxLength))
                } else {
                    Log.w(tag, msg.substring(i * maxLength, (i + 1) * maxLength))
                }
            }
        }
    }
}