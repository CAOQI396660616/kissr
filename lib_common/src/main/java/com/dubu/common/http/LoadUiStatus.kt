package com.dubu.common.http

import com.dubu.common.R

/**
 *  @author  Even
 *  @date   2021/10/19
 */
sealed class LoadStatus<out T : Any>(val loadType: LoadType = LoadType.Normal) {
    //正在加载
    class Loading(
        loadType: LoadType = LoadType.Normal,
        loadStr: String? =  com.dubu.common.base.BaseApp.instance.getString(R.string.loading)
    ) : LoadStatus<Nothing>(loadType)

    class Success<out T : Any>(
        val data: T?,
        loadType: LoadType = LoadType.Normal
    ) : LoadStatus<T>(loadType)

    class Error(
        val code: String? = null,
        val message: String? = null,
        loadType: LoadType = LoadType.Normal
    ) : LoadStatus<Nothing>(loadType)

    class Empty(
        val message: String?,
        loadType: LoadType = LoadType.Normal
    ) : LoadStatus<Nothing>(loadType)

    fun getStr(): String? {
        return when (this) {
            is Error -> this.message
            is Success -> this.data.toString()
            is Loading ->  com.dubu.common.base.BaseApp.instance.getString(R.string.loading)
            is Empty -> this.message
        }
    }

}


enum class LoadType {
    Normal,
    Background,
    Refresh,
    LoadMore;

    companion object {
        fun of(loadType: LoadType?): LoadType {
            if (loadType == null) return Normal
            return loadType
        }
    }
}