package com.dubu.common.http

import androidx.collection.ArrayMap
import com.alibaba.sdk.android.oss.common.utils.DateUtil
import com.blankj.utilcode.util.LanguageUtils
import com.dubu.common.BuildConfig
import com.dubu.common.constant.Tag2Common
import com.dubu.common.manager.LanguageManager
import com.dubu.common.utils.*
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class RequestInterceptor : Interceptor {


    private fun initHeaderMap(time: String): ArrayMap<String, String> {

        val headMap = ArrayMap<String, String>(14)
        //通用请求头

        headMap["server"] = "xianyu"
        headMap["Accept"] = "application/json"
        // 接口 请求时间戳秒
        headMap["timestamp"] = time
        //Token
        headMap["Authorization"] = "Bearer ${HiRealCache.userToken}"
        headMap["token"] = HiRealCache.userToken


        //安卓唯一设备ID
        headMap["device_id"] = HiRealCache.deviceId
        //手机系统语言
        headMap["Accept-Language"] = LanguageManager.getUserAppLangByMMKV().toString() // APP界面语言
        headMap["lang"] = LanguageUtils.getSystemLanguage().toString()            //手机系统语言
        //访问平台
        headMap["platform"] = "Android"
        // 应用版本
        headMap["appVersion"] = BuildConfig.versionName
        headMap["appVersionCode"] = BuildConfig.versionCode
        // 时区 获取utc时间与本地时区相差时间小时
        headMap["gmtTd"] = "${DateTool.getTimeZoneRawOffset()}"

        //系统版本号 Android版本号
        headMap["systemVersion"] = AppTool().getOsVersionCode()
        //设备型号
        headMap["device"] = AppTool().getModelName()
        //设备品牌
        headMap["phoneBrand"] = AppTool().getBrandName()

        return headMap
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val cur = System.currentTimeMillis()
        val time = (cur / 1000L).toString()
        val headerMap = initHeaderMap(time)


        val request = chain.request()


        val makeMapForGParameterSign = getParameterMap(request)



        val generateSign = if (HiRealCache.sign.isNullOrEmpty()){
            "null"
        }else{
            SignUtils.generateSignMD5(makeMapForGParameterSign, HiRealCache.sign , time)

        }




        val urlBuilder = request.url.newBuilder()
            .scheme(request.url.scheme)
            .host(request.url.host)



        val build = request.newBuilder()
            .method(request.method, request.body)
            .url(urlBuilder.build())
            .build()


        //拦截器 --> 添加头部
        val newBuilder = build.newBuilder()
        headerMap.forEach {
            newBuilder.addHeader(it.key, it.value)
        }
        newBuilder.addHeader("sign", generateSign)



        //重要的2个变量
        val newRequest = newBuilder.build()
        val response = chain.proceed(newRequest)


        val url = newRequest.url.toString()
        //图片上传的时候太大容易炸
        if (!url.contains("fileUpload")){
            // 生成cURL命令
            val curlCmd = buildString {
                append("curl -X ${newRequest.method} ")
                append("'${newRequest.url}' ")

                // 添加headers
                newRequest.headers.forEach { header ->
                    append("-H '${header.first}: ${header.second}' ")
                }

                // 添加请求体
                newRequest.body?.let { body ->
                    val buffer = Buffer()
                    body.writeTo(buffer)
                    val contentType = body.contentType()?.toString() ?: "application/octet-stream"
                    append("-H 'Content-Type: $contentType' ")
                    append("-d '${buffer.readUtf8().replace("'", "'\\''")}' ") // 处理单引号转义
                }
            }.trim().replace("  ", " ") // 清理多余空格

            HiLog.l(Tag2Common.TAG_HTTP, "post man p --> \n $curlCmd") // 在Logcat查看

        }


        return response
    }


    private fun getParameterMap(request: Request): Map<String, Any?> {

        when (request.method) {
            NetWorkConst.GET -> {
                return methodGetMap(request)
            }

            else -> {
                when (val body = request.body) {
                    is FormBody -> {
                        /*** 当参数以 @Field @FieldMap 提交时  */
                        return methodFieldMap(body)
                    }

                    is MultipartBody -> {
                        /*** 当参数以 @MultipartBody 提交时  */
                        return methodMultiBodyMap(body)
                    }

                    else -> {
                        /*** 当参数以 @Body 提交时  */

                        return methodOtherBodyMap(body)
                    }
                }
            }
        }
    }


    private fun methodGetMap(request: Request): HashMap<String, String> {

        val hashMapOf = hashMapOf<String, String>()

        try {
            val split = request.url.toString().split("?")
            if (split.size > 1) {
                split[1].split("&")
                    .toMutableList()
                    .apply {
                        sort()
                        forEach {
                            if (it.contains("=")) {
                                val stringList = it.split("=")
                                hashMapOf[stringList[0]] = stringList[1]
                            }

                        }
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return hashMapOf
        }
        return hashMapOf

    }

    private fun methodFieldMap(body: FormBody): HashMap<String, String> {

        val hashMapOf = hashMapOf<String, String>()

        try {
            for (i in body.size - 1 downTo 0) {
                val name = body.name(i)
                val value = body.value(i)
                hashMapOf[name] = value
            }
        } catch (e: Exception) {
            e.printStackTrace()
             return hashMapOf
        }

        return hashMapOf

    }

    private fun methodOtherBodyMap(body: RequestBody?): Map<String, String> {
        //获取body中的string
        val bodyString = bodyToJson(body)
        return jsonToMap(bodyString)
    }

    private fun methodMultiBodyMap(multipartBody: MultipartBody): ArrayMap<String, String> {
        val params = ArrayMap<String, String>()
        val files = ArrayMap<String, String>()
        try {
            multipartBody.parts.forEach { part ->
                val body = part.body
                part.headers?.let { h ->
                    val header = h.value(0)
                    val split = header.replace(" ", "").replace("\"", "").split(";")
                    when (split.size) {
                        2 -> {
                            //文本参数
                            val keys = split[1].split("=")
                            if (keys.size > 1 && body.contentLength() < 1024) {
                                val key = keys[1]
                                val buffer = Buffer()
                                body.writeTo(buffer)
                                val value = buffer.readUtf8()
                                params[key] = value
                            }
                        }

                        3 -> {
                            //文件
                            val fileKeys = split[1].split("=")
                            val fileKey = if (fileKeys.size > 1) {
                                fileKeys[1]
                            } else ""
                            val nameValue = split[2].split("=")
                            val fileName = if (nameValue.size > 1) nameValue[1] else ""
                            files[fileKey] = fileName
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return params
        }

        return params

    }


    private fun bodyToJson(request: RequestBody?): String {
        if (request == null) {
            return ""
        }
        return try {
            val buffer = okio.Buffer()
            request.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            ""
        }
    }



    /**
     * 将请求的Body中的Kv转化为map
     */
    private fun jsonToMap(json: String): Map<String, String> {
        val result: ArrayMap<String, String> = ArrayMap()
        if (json.isBlank()) return result

        val content = json.trim()
        try {
            val jsonObject = JSONObject(content)
            val iterator = jsonObject.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                when (val value = jsonObject[key]) {

                    //TODO allen 这里没有处理 参数是数组和对象的情况  看我们接口有没有 没有就算了
                    is JSONObject -> {
//                        result[key] = "object"
                    }

                    is JSONArray -> {
//                        result[key] = "array"
                    }

                    else -> {
                        result[key] = value.toString().trim()
                    }
                }
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }


}
