package com.dubu.common.router

import androidx.collection.ArrayMap
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter

/**
 *  @author  Even
 *  @date   2021/10/19
 */
object ARouterManager {
    private val mProviderMap by lazy {
        ArrayMap<String, IProvider>()
    }


    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : IProvider> provider(path: String): T? {
        var iProvider: IProvider? = mProviderMap[path]
        if (null == iProvider) {
            iProvider = ARouter.getInstance().build(path).navigation() as? IProvider ?: return null
            mProviderMap[path] = iProvider
        }
        return iProvider as? T
    }
}