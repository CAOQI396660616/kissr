package com.dubu.common.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 *  数字处理的一个工具类
 *  比如 数字10000 - 1w
 *  随机数等
 * @author cq
 * @date 2025/07/09
 */
public class NumberTool {

    /**
     *  随机数
     */
    public static int random(int numberS, int numberE) {
        int min = numberS;
        int max = numberE;
        Random random = new Random();
        int num = random.nextInt(max) % (max - min + 1) + min;
        return num;
    }


    /**
     * 保留两位小数
     *
     * @param number
     * @return
     */
    public static String change2Two(double number) {
        DecimalFormat df = new DecimalFormat("######0.00");
        String f1 = df.format(number);
        return f1;
    }

    public static String toTenThousand(long number) {
        String str = "";
        if (number <= 0) {
            str = "0";
        } else if (number < 10000) { //1万以内，显示数字；
            str = number + "";
        } else if (number > 10000 && number < 100000) { //大于等于1万，显示以万为单位，保留一位小数（取0不显示）
            double d = (double) number;
            double num = d / 10000;//1.将数字转换成以万为单位的数字

            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
            str = f1 + "w";
        } else { //大于10万，不保留小数，以万为单位显示
            str = number / 10000 + "w";
        }
        return str;
    }

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale <= 0) {
            throw new IllegalAccessException("The accuracy cannot be less than 0"); //精确度不能小于0
        }

        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        if (value2 == 0) {
            return value1;
        }
        //默认保留两位会有错误，这里设置保留小数点后2位  BigDecimal.ROUND_HALF_UP 四舍五入
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /*获取任务 完成度 */
    public static int getTaskProgress(double value1, double value2) throws IllegalAccessException {
        if (value1 <= 0 || value2 <= 0) {
            return 0;
        }

        if (value1 >= value2) {
            return 100;
        }

        int progress = (int) (div(value1, value2, 2) * 100);
        if (progress >= 100) {
            return 100;
        } else if (progress <= 0) {
            return 0;
        } else {
            return progress;
        }
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
}
