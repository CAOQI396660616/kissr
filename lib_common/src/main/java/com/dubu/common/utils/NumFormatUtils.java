package com.dubu.common.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Description 数格式化
 * @Author Created by JSZ on 2021/8/17.
 */
public class NumFormatUtils {
    private static final long K = 1000;
    private static final long M = 1000000;
    private static final long B = 1000000000;
    private static final long T = B * 1000;
    private static final long W = 10000;

    private static String getWords2K(long length) {
        return (length< K ? "1" : new DecimalFormat("#.0").format((float) length / K)) + "K";
    }

    private static String getWords2M(long length) {
        return (length < M ? "1" : new DecimalFormat("#.0").format((float) length / M)) + "M";
    }

    private static String getWords2B(long length) {
        return (length < B ? "1" : new DecimalFormat("#.0").format((float) length / B)) + "B";
    }

    private static String getWords2T(long length) {
        return (length< T ? "1" : new DecimalFormat("#.0").format((float) length / T)) + "T";
    }

    public static String getWords2W(long length) {
        return (length < W ? "1" : new DecimalFormat("#.0").format((float) length / W));
    }

    /**
     * 字数格式化
     *
     * @param currLength 当前长度
     * @return xxK
     */
    public static String getWordsAuto(String currLength) {
        long length = formatToLong(currLength);
        if (length > T) {
            return getWords2T(length).replace(",", ".");
        } else if (length > B) {
            return getWords2B(length).replace(",", ".");
        } else if (length > M) {
            return getWords2M(length).replace(",", ".");
        } else if (length > K) {
            return getWords2K(length).replace(",", ".");
        } else {
            return length + "";
        }
    }

    /**
     * 格式化数带小数
     *
     * @param currNum 当前数
     * @return 2位数
     */
    public static String formatDecimal(String currNum) {
        String transStr;
        DecimalFormat df = new DecimalFormat("#.00");
        if (df.format(formatToDouble(currNum)).startsWith(".")) {
            transStr = "0" + df.format(formatToDouble(currNum)).replace(",", ".");
        } else {
            transStr = df.format(formatToDouble(currNum)).replace(",", ".");
        }
        BigDecimal value = new BigDecimal(transStr);
        BigDecimal noZeros = value.stripTrailingZeros();
        return noZeros.toPlainString();
    }

    /**
     * 格式化整型
     *
     * @param intStr 当前整型
     * @return 整型
     */
    public static int formatToInt(String intStr) {
        if (TextUtils.isEmpty(intStr)) {
            return 0;
        }
        try {
            return Integer.parseInt(intStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 格式化长整型
     *
     * @param longStr 当前长整型
     * @return 长整型
     */
    public static long formatToLong(String longStr) {
        if (TextUtils.isEmpty(longStr)) {
            return 0;
        }
        try {
            return Long.parseLong(longStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 格式化浮点型
     *
     * @param floatStr 当前浮点型
     * @return 浮点型
     */
    public static float formatToFloat(String floatStr) {
        if (TextUtils.isEmpty(floatStr)) {
            return 0;
        }
        try {
            return Float.parseFloat(floatStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 格式化浮点型
     *
     * @param doubleStr 当前浮点型
     * @return 浮点型
     */
    public static double formatToDouble(String doubleStr) {
        if (TextUtils.isEmpty(doubleStr)) {
            return 0;
        }
        try {
            return Double.parseDouble(doubleStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
