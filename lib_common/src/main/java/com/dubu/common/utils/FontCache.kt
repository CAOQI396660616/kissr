package com.dubu.common.utils

import android.content.Context
import android.graphics.Typeface

object FontCache {
    private val cache = HashMap<String, Typeface>()

    fun getTypeface(context: Context, fontPath: String): Typeface? {
        return cache[fontPath] ?: try {
            val typeface = Typeface.createFromAsset(context.assets, fontPath)
            cache[fontPath] = typeface
            typeface
        } catch (e: Exception) {
            null
        }
    }

    // 清空缓存（可选，用于动态切换时释放旧字体）
    fun clearCache() {
        cache.clear()
    }
}