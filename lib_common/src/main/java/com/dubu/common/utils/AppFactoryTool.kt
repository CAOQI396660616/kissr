package com.dubu.common.utils

import com.squareup.moshi.Moshi
import com.dubu.common.constant.Tag2Common
import com.dubu.common.http.NetConfig
import com.dubu.common.http.RequestInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Author:v
 * Time:2022/5/7
 */
class AppFactoryTool private constructor() {
    private val moshi by lazy { Moshi.Builder().build() }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor())
            .addInterceptor(logInterceptor())
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }


    private fun logInterceptor() = HttpLoggingInterceptor { message ->
        HiLog.w(Tag2Common.TAG_HTTP, message)
    }.also { it.level = HttpLoggingInterceptor.Level.BODY }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetConfig.baseUrl())
            .addConverterFactory(com.dubu.common.http.EncryptFactory.create())
            //            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }


    fun provideHttpClient() = okHttpClient!!

    fun provideMoshi() = moshi!!


    fun provideRetrofit() = retrofit!!


    companion object {
        val instance = Holder.instance
    }

    private object Holder {
        val instance = AppFactoryTool()
    }
}