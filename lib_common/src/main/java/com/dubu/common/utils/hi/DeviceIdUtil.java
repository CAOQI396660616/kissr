package com.dubu.common.utils.hi;

import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.dubu.common.base.BaseApp;
import com.dubu.common.constant.SpKey2Common;
import com.tencent.mmkv.MMKV;

import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;

/**
 * @Description: 获取唯一id
 * @Author: gstory
 * @CreateDate: 2021/7/21 11:42
 **/
public class DeviceIdUtil {

    public static String getDeviceOptUUid() {

        String uuid = MMKV.defaultMMKV().decodeString(SpKey2Common.DEVICE_OPT_ID, "");
        if (TextUtils.isEmpty(uuid)) {
            String androidId = getDeviceOptId();
            MMKV.defaultMMKV().encode(SpKey2Common.DEVICE_OPT_ID, androidId);
            return androidId;
        }
        return uuid;
    }

    /**
     * 获取唯一id
     *
     * @return
     */
    private static String getDeviceOptId() {
        StringBuilder sbDeviceId = new StringBuilder();

        String androidId = getAndroidOptId();
        String uuid = getDeviceOptInfoUUID();

        //androidId
        if (androidId != null && androidId.length() > 0) {
            sbDeviceId.append(androidId);
            sbDeviceId.append("|");
        }
        //uuid
        if (uuid != null && uuid.length() > 0) {
            sbDeviceId.append(uuid);
        }

        if (sbDeviceId.length() > 0) {
            try {
                byte[] hash = getHashByOptString(sbDeviceId.toString());
                String sha1 = bytesToOptHex(hash);
                if (sha1 != null && sha1.length() > 0) {
                    //返回最终的DeviceId
                    return sha1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //如果都取不到就翻译一个随机数
        return UUID.randomUUID().toString();
    }

    /**
     * 转16进制字符串
     *
     * @param data 数据
     * @return 16进制字符串
     */
    private static String bytesToOptHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        String string;
        for (byte datum : data) {
            string = (Integer.toHexString(datum & 0xFF));
            if (string.length() == 1) {
                sb.append("0");
            }
            sb.append(string);
        }
        return sb.toString().toUpperCase(Locale.CHINA);
    }

    /**
     * 取 SHA1
     *
     * @param data 数据
     * @return 对应的Hash值
     */
    private static byte[] getHashByOptString(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF-8"));
            return messageDigest.digest();
        } catch (Exception e) {
            return "".getBytes();
        }
    }


    /**
     * @return 获取硬件的UUID
     */
    public static String getDeviceOptInfoUUID() {
        String deviceIdRecord = MMKV.defaultMMKV().decodeString(SpKey2Common.DEVICE_OPT_ID_RECORD, "");
        if (TextUtils.isEmpty(deviceIdRecord)) {
            String deviceId = "HITUBER" + Build.ID + // 修订版本列表
                    Build.DEVICE + // 设备参数
                    Build.BOARD + // 主板
                    Build.BRAND + // Android系统定制商
                    Build.HARDWARE + //硬件名
                    Build.PRODUCT + // 手机制造商
                    Build.MANUFACTURER + // 硬件制造商
                    Build.MODEL; // 版本
            String did = new UUID(deviceId.hashCode(), Build.SERIAL.hashCode()).toString().replace("-", "");
            MMKV.defaultMMKV().encode(SpKey2Common.DEVICE_OPT_ID_RECORD, did);
            return did;
        }
        return deviceIdRecord;
    }




    /**
     * 获取AndroidId
     *
     * @return AndroidId
     */
    public static String getAndroidOptId() {
        String androidIdRecord = MMKV.defaultMMKV().decodeString(SpKey2Common.ANDROID_OPT_ID_RECORD, "");
        if (TextUtils.isEmpty(androidIdRecord)) {
            try {
                String aid = Settings.Secure.getString(BaseApp.instance.getContentResolver(), Settings.Secure.ANDROID_ID);
                MMKV.defaultMMKV().encode(SpKey2Common.ANDROID_OPT_ID_RECORD, aid);
                return aid;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        return androidIdRecord;
    }
}
