package com.dubu.common.utils;

import android.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SignatureUtils {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SECRET_KEY = "your_secret_key_here"; // 从服务端获取

    /**
     * 生成签名
     * @param headers Header参数
     * @param bodyParams Body参数
     * @return 签名字符串
     */
    public static String generateSignature(Map<String, String> headers, Map<String, Object> bodyParams) {
        try {
            // 1. 合并所有参数
            Map<String, String> allParams = new TreeMap<>();

            // 添加Header参数（排除signature）
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (!"signature".equals(entry.getKey()) && !isEmptyValue(entry.getValue())) {
                    allParams.put(entry.getKey(), entry.getValue());
                }
            }

            // 添加Body参数
            if (bodyParams != null) {
                for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
                    if (!isEmptyValue(entry.getValue())) {
                        allParams.put(entry.getKey(), convertToString(entry.getValue()));
                    }
                }
            }

            // 2. 生成签名字符串
            String signString = buildSignString(allParams);

            // 3. 生成签名
            return hmacSha256(signString, SECRET_KEY);

        } catch (Exception e) {
            throw new RuntimeException("签名生成失败", e);
        }
    }

    /**
     * 构建签名字符串
     */
    private static String buildSignString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    /**
     * HMAC-SHA256加密
     */
    private static String hmacSha256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(hash, Base64.NO_WRAP);
    }

    /**
     * 检查值是否为空
     */
    private static boolean isEmptyValue(Object value) {
        return value == null || "".equals(value.toString().trim());
    }

    /**
     * 转换为字符串
     */
    private static String convertToString(Object value) {
        if (value instanceof Boolean) {
            return value.toString().toLowerCase();
        }
        return value.toString();
    }

    /**
     * 生成随机字符串
     */
    public static String generateNonceStr() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 获取当前时间戳
     */
    public static String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
}