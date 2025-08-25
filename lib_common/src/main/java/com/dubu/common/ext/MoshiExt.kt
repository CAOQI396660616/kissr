package com.dubu.common.ext

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.dubu.common.utils.AppFactoryTool
import java.lang.reflect.ParameterizedType

/**
 *  @author  Even
 *  @date   2021/11/9
 */
object MoshiExt {
    val moshi: Moshi by lazy {
        AppFactoryTool.instance.provideMoshi()
    }

    inline fun <reified K, reified V> jsonToMap(json: String): MutableMap<K, V> {
        return fromAdapterJson(
            Types.newParameterizedType(
                MutableMap::class.java,
                K::class.java,
                V::class.java
            ), json
        ) ?: mutableMapOf()
    }

    inline fun <reified K, reified V> mapToJson(map: MutableMap<K, V>): String {
        return toAdapterJson(
            Types.newParameterizedType(
                MutableMap::class.java,
                K::class.java,
                V::class.java
            ), map
        )
    }

    inline fun <reified T> jsonToList(json: String): List<T> {
        return fromAdapterJson(
            Types.newParameterizedType(List::class.java, T::class.java),
            json
        ) ?: emptyList<T>()
    }


    inline fun <reified T> listToJson(list: List<T>): String {
        return toAdapterJson(Types.newParameterizedType(List::class.java, T::class.java), list)
    }

    //序列化
    inline fun <reified T> fromJson(string: String): T? {
        return moshi.adapter(T::class.java).fromJson(string)
    }

    //序列化
    inline fun <reified T> fromAdapterJson(type: ParameterizedType, string: String): T? {
        return moshi.adapter<T>(type).fromJson(string)
    }


    //反序列话
    inline fun <reified T> toAdapterJson(type: ParameterizedType, t: T): String {
        return moshi.adapter<T>(type).toJson(t)
    }

    //反序列化
    inline fun <reified T> toJson(t: T): String {
        return moshi.adapter<T>(t!!::class.java).toJson(t)
    }
}

//toJson拓展类
fun Any.toJson() = MoshiExt.toJson(this)

//fromJson拓展类
inline fun <reified T : Any> String.fromJson() = MoshiExt.fromJson<T>(this)
