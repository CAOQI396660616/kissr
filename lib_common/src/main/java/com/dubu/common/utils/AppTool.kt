package com.dubu.common.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import com.dubu.common.base.BaseApp
import com.dubu.common.beans.common.DeviceEntity
import com.dubu.common.utils.hi.DeviceIdUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.gson.Gson
import okhttp3.internal.and
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Enumeration
import java.util.Locale

/**
 * @Author: JSZ
 * @CreateDate: 2022/4/19
 * @Description: 描述
 */
class AppTool {
    fun getDeviceInfo(context: Context): String {
        val deviceEntity = DeviceEntity(
            osVersionCode = getOsVersionCode(),
            osVersionDisplayName = getOsVersionDisplayName(),
            brand = getBrandName(),
            model = getModelName(),
            widthPixels = getWidthPixels(),
            heightPixels = getHeightPixels(),
            netType = getNetType(context),
            host = getHost(),
            androidId = getAndroidId(),
            lang = getLanguage(),
            ipAddress = getIpAddress(),
            macAddress = "",
            sdk = getSdk(),
            abi = getAbi(),
            totalRAM = SdCardUtils.getTotalRAM(context),
            availRAM = SdCardUtils.getAvailableRAM(context),
            totalInternalMemorySize = SdCardUtils.getTotalInternalMemorySize(context),
            availableInternalMemorySize = SdCardUtils.getAvailableInternalMemorySize(context),
            totalExternalMemorySize = SdCardUtils.getTotalExternalMemorySize(context),
            availableExternalMemorySize = SdCardUtils.getAvailableExternalMemorySize(context)
        )
        return Gson().toJson(deviceEntity)
    }

    /**
     * Android版本号
     */
    fun getOsVersionCode(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * Android版本名
     */
    fun getOsVersionDisplayName(): String {
        return Build.DISPLAY
    }

    /**
     * 设备厂商
     */
    fun getBrandName(): String {
        return Build.BRAND
    }

    /**
     * 设备名称
     */
    fun getModelName(): String {
        return Build.MODEL
    }

    /**
     * 手机宽
     */
    fun getWidthPixels(): Int {
        return DeviceUtils.getScreenWidth()
    }

    /**
     * 手机高
     */
    fun getHeightPixels(): Int {
        return DeviceUtils.getScreenHeight()
    }

    /**
     * 主机
     */
    fun getHost(): String {
        return Build.HOST
    }

    /**
     * Sdk
     */
    fun getSdk(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * AndroidId
     */
    fun getAndroidId(): String {
        return DeviceIdUtil.getAndroidOptId()
    }

    /**
     * 系统语言
     */
    fun getLanguage(): String {
        return Locale.getDefault().language
    }

    /**
     * 架构
     */
    fun getAbi(): ArrayList<String> {
        val abiArray: Array<String> = Build.SUPPORTED_ABIS
        val abiList = ArrayList<String>()
        for (abi in abiArray) {
            abiList.add(abi)
        }
        return abiList
    }

    /**
     * 跳转Google商店
     */
    fun startGooglePlay(context: Activity, packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=$packageName")
            intent.setPackage("com.android.vending") // 这里对应的是谷歌商店，跳转别的商店改成对应的即可
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else { // 没有应用市场，通过浏览器跳转到Google Play
                val intent2 = Intent(Intent.ACTION_VIEW)
                intent2.data =
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                if (intent2.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent2)
                } else {
                    // 没有Google Play 也没有浏览器
                }
            }
        } catch (activityNotFoundException1: ActivityNotFoundException) {
            activityNotFoundException1.printStackTrace()
        }
    }

    /**
     * 跳转外部浏览器
     */
    fun jumpBrowser(context: Context, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        // resolveActivity检查避免可能的crash
        // 参考: https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    /**
     * 获取网络类型
     *
     * @param context 上下文
     * @return 网络类型
     */
    fun getNetType(context: Context): String {
        try {
            val connectionManager = context.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectionManager.activeNetworkInfo
            // networkInfo.getDetailedState();//获取详细状态。
            // networkInfo.getExtraInfo();//获取附加信息。
            // networkInfo.getReason();//获取连接失败的原因。
            // networkInfo.getType();//获取网络类型(一般为移动或Wi-Fi)。
            // networkInfo.getTypeName();//获取网络类型名称(一般取值“WIFI”或“MOBILE”)。
            // networkInfo.isAvailable();//判断该网络是否可用。
            // networkInfo.isConnected();//判断是否已经连接。
            // networkInfo.isConnectedOrConnecting();//：判断是否已经连接或正在连接。
            // networkInfo.isFailover();//：判断是否连接失败。
            // networkInfo.isRoaming();//：判断是否漫游
            return if (null != networkInfo) networkInfo.typeName else ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 是否连网
     *
     * @param context 上下文
     * @return 网络类型
     */
    fun isConnectNet(context: Context): Boolean {
        try {
            val connectionManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectionManager.activeNetworkInfo
            // networkInfo.getDetailedState();//获取详细状态。
            // networkInfo.getExtraInfo();//获取附加信息。
            // networkInfo.getReason();//获取连接失败的原因。
            // networkInfo.getType();//获取网络类型(一般为移动或Wi-Fi)。
            // networkInfo.getTypeName();//获取网络类型名称(一般取值“WIFI”或“MOBILE”)。
            // networkInfo.isAvailable();//判断该网络是否可用。
            // networkInfo.isConnected();//判断是否已经连接。
            // networkInfo.isConnectedOrConnecting();//：判断是否已经连接或正在连接。
            // networkInfo.isFailover();//：判断是否连接失败。
            // networkInfo.isRoaming();//：判断是否漫游
            return null != networkInfo && networkInfo.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

//    /**
//     * MAC地址
//     */
//    fun getMacAddress(): String {
//        Log.e("reportDeviceInfo", "getEth0Mac = ${getEth0Mac()}  getWlan0Mac = ${getWlan0Mac()}")
//
//        return if (!TextUtils.isEmpty(getEth0Mac())) {
//            getEth0Mac()
//        } else getWlan0Mac()
//    }

//    /**
//     * 有线网络MAC
//     */
//    private fun getEth0Mac(): String {
//        var mac = ""
//        try {
//            val all: List<NetworkInterface> =
//                Collections.list(NetworkInterface.getNetworkInterfaces())
//            for (nif in all) {
//                if (!nif.name.equals("eth0", ignoreCase = true)) continue
//                val macBytes = nif.hardwareAddress ?: return ""
//                val res = java.lang.StringBuilder()
//                for (b in macBytes) {
//                    res.append(String.format("%02X:", b))
//                }
//                if (res.isNotEmpty()) {
//                    res.deleteCharAt(res.length - 1)
//                    mac = res.toString()
//                }
//                Log.e("reportDeviceInfo", "getEth0Mac $mac")
//                return mac
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("reportDeviceInfo", "getEth0Mac ${e.message}")
//        }
//        return ""
//    }
//
//    /**
//     * 无线网络MAC
//     */
//    private fun getWlan0Mac(): String {
//        var mac = ""
//        try {
//            val all: List<NetworkInterface> =
//                Collections.list(NetworkInterface.getNetworkInterfaces())
//            for (nif in all) {
//                if (!nif.name.equals("wlan0", ignoreCase = true)) continue
//                val macBytes = nif.hardwareAddress ?: return ""
//                val res = java.lang.StringBuilder()
//                for (b in macBytes) {
//                    res.append(String.format("%02X:", b))
//                }
//                if (res.isNotEmpty()) {
//                    res.deleteCharAt(res.length - 1)
//                    mac = res.toString()
//                }
//                Log.e("reportDeviceInfo", "getWlan0Mac $mac")
//                return mac
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("reportDeviceInfo", "getWlan0Mac ${e.message}")
//        }
//        return ""
//    }

    fun getIpAddress(): String {
        var strMacAddress: String? = null
        try {
            // 获得IpD地址
            val ip = getLocalInetAddress()
            val b = NetworkInterface.getByInetAddress(ip)
                .hardwareAddress
            val buffer = StringBuffer()
            for (i in b.indices) {
                if (i != 0) {
                    buffer.append(':')
                }
                val str = Integer.toHexString(b[i] and 0xFF)
                buffer.append(if (str.length == 1) "0$str" else str)
            }
            strMacAddress = buffer.toString().toUpperCase()
        } catch (e: Exception) {
            Log.e("reportDeviceInfo", "getIpAddress ${e.message}")
        }
        return strMacAddress ?: ""
    }

    fun getLocalInetAddress(): InetAddress? {
        var ip: InetAddress? = null
        try {
            // 列举
            val netInterface: Enumeration<NetworkInterface> = NetworkInterface
                .getNetworkInterfaces()
            while (netInterface.hasMoreElements()) { // 是否还有元素
                val ni = netInterface
                    .nextElement() as NetworkInterface // 得到下一个元素
                val inetIp: Enumeration<InetAddress> = ni.inetAddresses // 得到一个ip地址的列举
                while (inetIp.hasMoreElements()) {
                    ip = inetIp.nextElement()
                    /* && ip.getHostAddress().indexOf(":") == -1   IPv6的地址*/
                    ip = if (!ip.isLoopbackAddress) break else null
                }
                if (ip != null) {
                    break
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
            Log.e("reportDeviceInfo", "getLocalInetAddress ${e.message}")
        }
        return ip
    }

    fun getSimCountryCode(): String {
        val telManager: TelephonyManager =
            BaseApp.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telManager.simCountryIso
    }

    fun isSupportGoogleService(): Boolean {
        return if (BaseApp.instance == null) {
            false
        } else {
            GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(BaseApp.instance) == ConnectionResult.SUCCESS
        }
    }



    /**
     * 根据滑动距离，得到透明度数值
     * distance:滚动距离
     * colorString:颜色的数值字符串，不带透明度，例如：121214；ffffff等
     */
    fun getAlphaByDistance(distance: Int,colorString:String): String {
        var alpha10Bit: Int = distance * 255 / DeviceUtils.dpToPx(200)
        if (alpha10Bit < 0) alpha10Bit = 0
        if (alpha10Bit > 255) alpha10Bit = 255
        var alpha16bitStr = Integer.toHexString(alpha10Bit)
        if (alpha10Bit < 16) {
            alpha16bitStr = "0$alpha16bitStr"
        }
        return "#$alpha16bitStr$colorString"
    }
}
