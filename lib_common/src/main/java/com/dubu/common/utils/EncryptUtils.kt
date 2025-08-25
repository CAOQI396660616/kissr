package com.dubu.common.utils

import java.security.MessageDigest


/**
 *  @author  Even
 *  @date   2021/11/15
 *  加密工具类
 */
object EncryptUtils {
    /**
     * md5加密
     */
    fun string2MD5(str: String): String {
        try {

            val instance = MessageDigest.getInstance("MD5")
            val digest = instance.digest(str.toByteArray())

            val buf = StringBuffer()
            digest.forEach {
                val toHexString = Integer.toHexString(0xFF and it.toInt())
                if (toHexString.length == 1) {
                    buf.append("0")
                }
                buf.append(toHexString)
            }
            return buf.toString()
        } catch (e: Exception) {

        }
        return ""
    }
}