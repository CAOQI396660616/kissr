package com.dubu.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;


import com.dubu.common.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Description 国际日期与时间的工具类-有新需求继续定制
 * @Author Created by JSZ on 2021/8/17.
 */
public class DateTool {
    /**
     * 外国统一格式 月年
     */
    public static final String DATE_FORMAT_1 = "MM/yyyy";

    /**
     * 外国统一格式 日月时
     */
    public static final String DATE_FORMAT_2 = "MM/dd HH:mm";

    /**
     * 外国统一格式 日月时
     */
    public static final String DATE_FORMAT_19 = "dd/MM HH:mm";

    /**
     * 外国统一格式 月日年时
     */
    public static final String DATE_FORMAT_3 = "dd/MM/yyyy HH:mm";

    public static final String DATE_FORMAT_HI_GO = "MM/dd/yyyy HH:mm";

    /**
     * 外国统一格式 月日 小时分钟 - >只用于聚会
     */
    public static final String DATE_FORMAT_14 = "MM/dd HH:mm";

    /**
     * 外国统一格式 月日 小时分钟
     */
    public static final String DATE_FORMAT_16 = "dd/MM HH:mm";

    /**
     * 外国统一格式 日月年
     */
    public static final String DATE_FORMAT_4 = "dd/MM/yyyy";

    /**
     * 外国统一格式 月日年时秒
     */
    public static final String DATE_FORMAT_5 = "dd/MM/yyyy HH:mm:ss";

    /**
     * 指定日期格式HH:mm
     */
    public static final String DATE_FORMAT_7 = "HH:mm";

    /**
     * 指定日期格式dd
     */
    public static final String DATE_FORMAT_8 = "dd";

    /**
     * 指定日期格式mm:ss
     */
    public static final String DATE_FORMAT_9 = "mm:ss";

    /**
     * 指定日期格式oct dd，mm:ss
     */
    public static final String DATE_FORMAT_10 = "MMM dd, HH:mm";

    /**
     * 指定日期格式oct dd，mm:ss
     */
    public static final String DATE_FORMAT_13 = "MM/dd";

    /**
     * 指定日期格式oct dd，yyyy
     */
    public static final String DATE_FORMAT_11 = "MMM dd, yyyy";

    public static final String DATE_FORMAT_101 = "yyyy";

    public static final long MILLIS = 1000;
    public static final long MINUTE = 60 * MILLIS;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long MONTH = DAY * 30;
    public static final long YEAR = MONTH * 12;

    /**
     * 根据指定格式，获取现在时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowDateFormat(String format) {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }

    /**
     * 根据时间戳转成指定的format格式
     *
     * @param timeMillis 时间戳
     * @param format     格式
     * @return 时间字符串
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(String timeMillis, String format) {
        Date date;
        if (!TextUtils.isEmpty(timeMillis)) {
            try {
                date = new Date(Long.parseLong(timeMillis) * 1000);
            } catch (NumberFormatException e) {
                date = new Date();
            }
        } else {
            date = new Date();
        }

        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 根据时间戳转成指定的format格式
     *
     * @param timeMillis 时间戳
     * @param format     格式
     * @return 时间字符串
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(long timeMillis, String format) {
        Date date;
        if (timeMillis > 0) {
            date = new Date(timeMillis);
        } else {
            date = new Date();
        }
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 是否是今天
     *
     * @param timeInMills 时间戳
     * @return 是否
     */
    public static boolean isToday(long timeInMills) {
        String dest = getYmd(timeInMills);
        String now = getYmd(Calendar.getInstance().getTimeInMillis());
        return dest.equals(now);
    }

    /**
     * 是否是今年
     *
     * @param timeInMills 时间戳
     * @return 是否
     */
    public static boolean isToYear(long timeInMills) {
        String dest = getYear(timeInMills);
        String now = getYear(Calendar.getInstance().getTimeInMillis());
        return dest.equals(now);
    }

    private static String getYmd(long timeInMills) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        return ymd.format(new Date(timeInMills));
    }

    private static String getYear(long timeInMills) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy");
        return ymd.format(new Date(timeInMills));
    }

    /**
     * 根据返回几天前
     *
     * @param timeInMills 开始时间
     * @return x前
     */
    public static String getBeforeTime(Context mContext, String timeInMills) {
        Date date = new Date();
        long diffValue = date.getTime() - (NumFormatUtils.formatToLong(timeInMills) * 1000);
        long yearC = diffValue / YEAR;
        long monthC = diffValue / MONTH;
        long weekC = diffValue / (7 * DAY);
        long dayC = diffValue / DAY;
        long hourC = diffValue / HOUR;
        long minC = diffValue / MINUTE;
        if (yearC >= 1) {
            return String.format(mContext.getResources().getString(yearC == 1 ? R.string.time_before_year_ago : R.string.time_before_years_ago), yearC);
        } else if (monthC >= 1) {
            return String.format(mContext.getResources().getString(monthC == 1 ? R.string.time_before_month_ago : R.string.time_before_months_ago), monthC);
        } else if (weekC >= 1) {
            return String.format(mContext.getResources().getString(weekC == 1 ? R.string.time_before_week_ago : R.string.time_before_weeks_ago), weekC);
        } else if (dayC >= 1) {
            return String.format(mContext.getResources().getString(dayC == 1 ? R.string.time_before_day_ago : R.string.time_before_days_ago), dayC);
        } else if (hourC >= 1) {
            return String.format(mContext.getResources().getString(hourC == 1 ? R.string.time_before_hour_ago : R.string.time_before_hours_ago), hourC);
        } else if (minC >= 1) {
            return String.format(mContext.getResources().getString(minC == 1 ? R.string.time_before_minute_ago : R.string.time_before_minutes_ago), minC);
        } else {
            return mContext.getResources().getString(R.string.time_now);
        }
    }



    /**
     * 获取星期,0-周日,1-周一，2-周二，3-周三，4-周四，5-周五，6-周六
     *
     * @param timeInMills 时间戳
     * @return 星期几
     */
    public static int getCurrWeekDay(long timeInMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMills);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取utc时间与本地时区相差时间小时
     *
     * @return 差值
     */
    public static int getTimeZoneRawOffset() {
        int time = TimeZone.getTimeZone(TimeZone.getDefault().getID()).getRawOffset();
        return time / (3600 * 1000);
    }

    /**
     * @param year 年份
     * @return 是否闰年
     */
    public static boolean isLeap(int year) {
        return ((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0);
    }

    /**
     * @param year  年份
     * @param month 月份
     * @return 某年某月天数
     */
    public static int getDays(int year, int month) {
        int days;
        int febDay = 28;
        if (isLeap(year)) {
            febDay = 29;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = febDay;
                break;
            default:
                days = 0;
                break;
        }
        return days;
    }

    //两个时间戳是否是同一天 时间戳是long型的（11或者13）
    public static boolean isSameData(String currentTime, String lastTime) {
        try {
            Calendar nowCal = Calendar.getInstance();
            Calendar dataCal = Calendar.getInstance();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            Long nowLong = new Long(currentTime);
            Long dataLong = new Long(lastTime);
            String data1 = df1.format(nowLong);
            String data2 = df2.format(dataLong);
            Date now = df1.parse(data1);
            Date date = df2.parse(data2);
            nowCal.setTime(now);
            dataCal.setTime(date);
            return isSameDay(nowCal, dataCal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                    && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        } else {
            return false;
        }
    }

    public static String getTimeStr(long startTime, long endTime) {
        startTime = startTime * 1000L;
        endTime = endTime * 1000L;
        if (DateTool.isSameData(startTime + "", endTime + "")) {
            return DateTool.formatDate(startTime, DATE_FORMAT_14) + "~" + DateTool.formatDate(endTime, DATE_FORMAT_7);
        }
        return DateTool.formatDate(startTime, DATE_FORMAT_14)
                + "~" + DateTool.formatDate(endTime, DATE_FORMAT_14);
    }

    /**
     * 根据返回几天前
     *
     * @param timeInMills 开始时间
     * @return x前
     */
    public static String getBeforeTimeAgoAndTime(Context mContext, String timeInMills) {
        Date date = new Date();
        long diffValue = date.getTime() - (NumFormatUtils.formatToLong(timeInMills) * 1000);
        long yearC = diffValue / YEAR;
        long monthC = diffValue / MONTH;
        long weekC = diffValue / (7 * DAY);
        long dayC = diffValue / DAY;
        long hourC = diffValue / HOUR;
        long minC = diffValue / MINUTE;
        if (yearC >= 1) {
            return DateTool.formatDate(timeInMills, DATE_FORMAT_2);
        } else if (monthC >= 1) {
            return DateTool.formatDate(timeInMills, DATE_FORMAT_2);
        } else if (weekC >= 1) {
            return DateTool.formatDate(timeInMills, DATE_FORMAT_2);
        } else if (dayC >= 1) {
            if (dayC >= 1 && dayC <= 2) {
                return String.format(mContext.getResources().getString(R.string.gkd_date_yesterday));
            } else {
                return DateTool.formatDate(timeInMills, DATE_FORMAT_2);
            }
        } else if (hourC >= 1) {
            return String.format(mContext.getResources().getString(hourC == 1 ? R.string.time_before_hour_ago : R.string.time_before_hours_ago), hourC);
        } else if (minC >= 1) {
            return String.format(mContext.getResources().getString(minC == 1 ? R.string.time_before_minute_ago : R.string.time_before_minutes_ago), minC);
        } else {
            return mContext.getResources().getString(R.string.time_now);
        }
    }
}
