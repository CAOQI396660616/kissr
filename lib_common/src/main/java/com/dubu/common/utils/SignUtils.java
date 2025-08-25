package com.dubu.common.utils;

import com.dubu.common.constant.Tag2Common;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignUtils {
    public static String generateSign(Map<String, Object> params, String secret, String time) {
        // 过滤null值参数
        Map<String, Object> filteredParams = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                filteredParams.put(entry.getKey(), entry.getValue());
            }
        }
        filteredParams.put("timestamp",time);
        // 参数排序
        List<String> keys = new ArrayList<>(filteredParams.keySet());
        Collections.sort(keys);
        // 构建签名字符串
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (sb.length() > 0) sb.append("&");
            sb.append(String.format("%s=%s", key, filteredParams.get(key).toString()));
        }
        sb.append("&");
        sb.append(String.format("%s=%s", "timestamp", time));
        sb.append("&");
        sb.append(String.format("%s=%s", "key", HiRealCache.INSTANCE.getSign()));
        String signString = sb.toString().replace("+", "%20"); // PHP的urlencode空格处理
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hashBytes = sha256_HMAC.doFinal(signString.getBytes(StandardCharsets.UTF_8));
            /*for (int i = 0; i < hashBytes.length; i++) {
                System.out.printf("Byte at index %d: %02X%n", i, hashBytes[i] & 0xFF);
            }*/
            String s = bytesToHex(hashBytes);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    //------------------------------------------------------


    public static String generateSignMD5(Map<String, Object> params, String secret, String time) {
        // 过滤null值参数
        Map<String, Object> filteredParams = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                filteredParams.put(entry.getKey(), entry.getValue());
            }
        }
        filteredParams.put("timestamp",time);
        // 参数排序
        List<String> keys = new ArrayList<>(filteredParams.keySet());
        Collections.sort(keys);
        // 构建签名字符串
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (sb.length() > 0) sb.append("&");
            sb.append(String.format("%s=%s", key, filteredParams.get(key).toString()));
        }
        sb.append("&");
        sb.append(String.format("%s=%s", "key", HiRealCache.INSTANCE.getSign()));
        String signString = sb.toString().replace("+", "%20"); // PHP的urlencode空格处理
        HiLog.l(Tag2Common.TAG_HTTP, "signString = " + signString);
        HiLog.e(Tag2Common.TAG_HTTP, "secret = " + secret);
        String string2MD5 = EncryptUtils.INSTANCE.string2MD5(signString);
        HiLog.e(Tag2Common.TAG_HTTP, "string2MD5 = " + string2MD5);
        return string2MD5;
    }

}

