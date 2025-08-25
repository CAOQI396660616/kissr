package com.dubu.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;


import com.dubu.common.R;
import com.dubu.common.beans.rtc.TimeInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EaseDateUtils {

	public static final String FORMAT_Y = "yyyy";
	public static final String FORMAT_M = "MM";
	public static final String FORMAT_D = "dd";
	public static final String FORMAT_S = "ss";
	public static final String FORMAT_YM = "yyyy-MM";
	public static final String FORMAT_YMD = "yyyy-MM-dd";
	public static final String FORMAT_MD = "MMdd";
	public static final String FORMAT_MD_2 = "MM-dd";
	public static final String FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_MDHM = "MM:dd HH:mm";

	private static final long INTERVAL_IN_MILLISECONDS = 60 * 1000;

	//这个是 原本环信逻辑 可以用来借鉴一下
	public static String getTimestampStringOld(Context context, Date messageDate) {
		String format = null;
		String language = Locale.getDefault().getLanguage();
		boolean isZh = language.startsWith("zh");
		long messageTime = messageDate.getTime();
		if (isSameDay(messageTime)) {
			if (is24HourFormat(context)) {
				format = "HH:mm";
			} else {
				if (isZh) {
					format = "aa hh:mm";
				} else {
					format = "hh:mm aa";
				}
			}
		} else if (isYesterday(messageTime)) {
			if (isZh) {
				if (is24HourFormat(context)) {
					format = "昨天 HH:mm";
				} else {
					format = "昨天aa hh:mm";
				}
			} else {
				if (is24HourFormat(context)) {
					return "Yesterday " + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(messageDate);
				} else {
					return "Yesterday " + new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(messageDate);
				}
			}
		} else {
			if (isZh) {
				if (is24HourFormat(context)) {
					format = "M月d日 HH:mm";
				} else {
					format = "M月d日aa hh:mm";
				}
			} else {
				if (is24HourFormat(context)) {
					format = "MMM dd HH:mm";
				} else {
					format = "MMM dd hh:mm aa";
				}
			}
		}
		if (isZh) {
			return new SimpleDateFormat(format, Locale.CHINESE).format(messageDate);
		} else {
			return new SimpleDateFormat(format, Locale.ENGLISH).format(messageDate);
		}
	}

	//这里就是 消息顶上的时间
	public static String getTimestampString(Context context, Date messageDate) {
		String format = null;
		String language = Locale.getDefault().getLanguage();
		boolean isZh = language.startsWith("zh");
		long messageTime = messageDate.getTime();
		if (isSameDay(messageTime)) {
			format = "HH:mm";
		} else if (isYesterday(messageTime)) {
			String contextString = context.getString(R.string.gkd_date_yesterday);
			format = " HH:mm";

			String formatStr;
			if (isZh) {
				formatStr = new SimpleDateFormat(format, Locale.CHINESE).format(messageDate);
			} else {
				formatStr = new SimpleDateFormat(format, Locale.ENGLISH).format(messageDate);
			}
			return contextString + formatStr;

		} else {
			format = "MM/dd/yyyy HH:mm";
		}
		if (isZh) {
			return new SimpleDateFormat(format, Locale.CHINESE).format(messageDate);
		} else {
			return new SimpleDateFormat(format, Locale.ENGLISH).format(messageDate);
		}
	}

	//我们app 的时间显示规则
	public static String getTimestampStringForMine(Context context, Date messageDate) {
		String format = null;
		String language = Locale.getDefault().getLanguage();
		boolean isZh = language.startsWith("zh");
		long messageTime = messageDate.getTime();
		if (isSameDay(messageTime)) {
			format = "HH:mm";
		} else if (isYesterday(messageTime)) {
			return context.getString(R.string.gkd_date_yesterday);
		} else if (checkTimeInSameYear(messageTime)) {
			format = "MM/dd";
		} else {
			format = "MM/dd/yyyy";
		}
		if (isZh) {
			return new SimpleDateFormat(format, Locale.CHINESE).format(messageDate);
		} else {
			return new SimpleDateFormat(format, Locale.ENGLISH).format(messageDate);
		}
	}

	//我们app 的发送空投的时间规则
	public static String getTimestampStringForDrop(Context context, Date messageDate) {
		String format = null;
		String language = Locale.getDefault().getLanguage();
		boolean isZh = language.startsWith("zh");
		long messageTime = messageDate.getTime();
		if (isSameDay(messageTime)) {
			String contextString = context.getString(R.string.message_air_drop_time_today);
			format = " HH:mm";

			String dateStr;
			if (isZh) {
				dateStr = new SimpleDateFormat(format, Locale.CHINESE).format(messageDate);
			} else {
				dateStr = new SimpleDateFormat(format, Locale.ENGLISH).format(messageDate);
			}
			return contextString + dateStr;

		} else if (isYesterday(messageTime)) {
			String contextString = context.getString(R.string.gkd_date_yesterday);
			format = " HH:mm";

			String dateStr;
			if (isZh) {
				dateStr = new SimpleDateFormat(format, Locale.CHINESE).format(messageDate);
			} else {
				dateStr = new SimpleDateFormat(format, Locale.ENGLISH).format(messageDate);
			}
			return contextString + dateStr;

		} else {
			format = "MM/dd/yyyy";
		}
		if (isZh) {
			return new SimpleDateFormat(format, Locale.CHINESE).format(messageDate);
		} else {
			return new SimpleDateFormat(format, Locale.ENGLISH).format(messageDate);
		}
	}


	public static boolean isCloseEnough(long time1, long time2) {
		// long time1 = date1.getTime();
		// long time2 = date2.getTime();
		long delta = time1 - time2;
		if (delta < 0) {
			delta = -delta;
		}
		return delta < INTERVAL_IN_MILLISECONDS;
	}

	public static boolean isSameDay(long inputTime) {

		TimeInfo tStartAndEndTime = getTodayStartAndEndTime();
		if (inputTime > tStartAndEndTime.getStartTime() && inputTime < tStartAndEndTime.getEndTime())
			return true;
		return false;
	}

	private static boolean isYesterday(long inputTime) {
		TimeInfo yStartAndEndTime = getYesterdayStartAndEndTime();
		if (inputTime > yStartAndEndTime.getStartTime() && inputTime < yStartAndEndTime.getEndTime())
			return true;
		return false;
	}

	@SuppressLint("SimpleDateFormat")
	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat format = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @param timeLength Millisecond
	 * @return
	 */
	@SuppressWarnings("UnusedAssignment")
	@SuppressLint("DefaultLocale")
	public static String toTime(int timeLength) {
		timeLength /= 1000;
		int minute = timeLength / 60;
		int hour = 0;
		if (minute >= 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		int second = timeLength % 60;
		// return String.format("%02d:%02d:%02d", hour, minute, second);
		return String.format("%02d:%02d", minute, second);
	}

	/**
	 * @param timeLength second
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String toTimeBySecond(int timeLength) {
//		timeLength /= 1000;
		int minute = timeLength / 60;
		int hour = 0;
		if (minute >= 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		int second = timeLength % 60;
		// return String.format("%02d:%02d:%02d", hour, minute, second);
		return String.format("%02d:%02d", minute, second);
	}


	public static TimeInfo getYesterdayStartAndEndTime() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.DATE, -1);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		Date startDate = calendar1.getTime();
		long startTime = startDate.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DATE, -1);
		calendar2.set(Calendar.HOUR_OF_DAY, 23);
		calendar2.set(Calendar.MINUTE, 59);
		calendar2.set(Calendar.SECOND, 59);
		calendar2.set(Calendar.MILLISECOND, 999);
		Date endDate = calendar2.getTime();
		long endTime = endDate.getTime();
		TimeInfo info = new TimeInfo();
		info.setStartTime(startTime);
		info.setEndTime(endTime);
		return info;
	}

	public static TimeInfo getTodayStartAndEndTime() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);
		Date startDate = calendar1.getTime();
		long startTime = startDate.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(Calendar.HOUR_OF_DAY, 23);
		calendar2.set(Calendar.MINUTE, 59);
		calendar2.set(Calendar.SECOND, 59);
		calendar2.set(Calendar.MILLISECOND, 999);
		Date endDate = calendar2.getTime();
		long endTime = endDate.getTime();
		TimeInfo info = new TimeInfo();
		info.setStartTime(startTime);
		info.setEndTime(endTime);
		return info;
	}

	public static TimeInfo getBeforeYesterdayStartAndEndTime() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.DATE, -2);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);
		Date startDate = calendar1.getTime();
		long startTime = startDate.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DATE, -2);
		calendar2.set(Calendar.HOUR_OF_DAY, 23);
		calendar2.set(Calendar.MINUTE, 59);
		calendar2.set(Calendar.SECOND, 59);
		calendar2.set(Calendar.MILLISECOND, 999);
		Date endDate = calendar2.getTime();
		long endTime = endDate.getTime();
		TimeInfo info = new TimeInfo();
		info.setStartTime(startTime);
		info.setEndTime(endTime);
		return info;
	}

	/**
	 * endtime为今天
	 *
	 * @return
	 */
	public static TimeInfo getCurrentMonthStartAndEndTime() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.DATE, 1);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);
		Date startDate = calendar1.getTime();
		long startTime = startDate.getTime();

		Calendar calendar2 = Calendar.getInstance();
//		calendar2.set(Calendar.HOUR_OF_DAY, 23);
//		calendar2.set(Calendar.MINUTE, 59);
//		calendar2.set(Calendar.SECOND, 59);
//		calendar2.set(Calendar.MILLISECOND, 999);
		Date endDate = calendar2.getTime();
		long endTime = endDate.getTime();
		TimeInfo info = new TimeInfo();
		info.setStartTime(startTime);
		info.setEndTime(endTime);
		return info;
	}

	public static TimeInfo getLastMonthStartAndEndTime() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.MONTH, -1);
		calendar1.set(Calendar.DATE, 1);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);
		Date startDate = calendar1.getTime();
		long startTime = startDate.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.MONTH, -1);
		calendar2.set(Calendar.DATE, 1);
		calendar2.set(Calendar.HOUR_OF_DAY, 23);
		calendar2.set(Calendar.MINUTE, 59);
		calendar2.set(Calendar.SECOND, 59);
		calendar2.set(Calendar.MILLISECOND, 999);
		calendar2.roll(Calendar.DATE, -1);
		Date endDate = calendar2.getTime();
		long endTime = endDate.getTime();
		TimeInfo info = new TimeInfo();
		info.setStartTime(startTime);
		info.setEndTime(endTime);
		return info;
	}

	public static String getTimestampStr() {
		return Long.toString(System.currentTimeMillis());
	}

	/**
	 * 判断是否是24小时制
	 *
	 * @param context
	 * @return
	 */
	public static boolean is24HourFormat(Context context) {
		return android.text.format.DateFormat.is24HourFormat(context);
	}


	public static final int YEAR = 365 * 24 * 60 * 60;// 年
	public static final int MONTH = 30 * 24 * 60 * 60;// 月
	public static final int DAY = 24 * 60 * 60;// 天
	public static final int HOUR = 60 * 60;// 小时
	public static final int MINUTE = 60;// 分钟

	/**
	 * 根据时间戳获取描述性时间，如3分钟前，1天前
	 *
	 * @param timestamp 时间戳 单位为毫秒
	 * @return 时间字符串
	 */
	public static String getDescriptionTimeFromTimestamp(long timestamp) {
		long currentTime = System.currentTimeMillis();
		long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
		System.out.println("timeGap: " + timeGap);
		String timeStr;
		if (timeGap > YEAR) {
			timeStr = timeGap / YEAR + "年前";
		} else if (timeGap > MONTH) {
			timeStr = timeGap / MONTH + "个月前";
		} else if (timeGap > DAY) {// 1天以上
			timeStr = timeGap / DAY + "天前";
		} else if (timeGap > HOUR) {// 1小时-24小时
			timeStr = timeGap / HOUR + "小时前";
		} else if (timeGap > MINUTE) {// 1分钟-59分钟
			timeStr = timeGap / MINUTE + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}


	/**
	 * 根据时间戳获取描述性时间，如3分钟前，1天前
	 *
	 * @param timestamp 时间戳 单位为毫秒
	 * @return 时间字符串
	 */
	public static String getDescriptionTimeFromTimestampV2(long timestamp) {
		long currentTime = System.currentTimeMillis();
		long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
		System.out.println("timeGap: " + timeGap);
		String timeStr;
		if (timeGap > YEAR) {
			timeStr = timeGap / YEAR + "年前";
		} else if (timeGap > MONTH) {
			timeStr = timeGap / MONTH + "个月前";
		} else if (timeGap > DAY) {// 1天以上

			timeStr = timeGap / DAY + "天前";

		} else if (timeGap > HOUR) {// 1小时-24小时
			timeStr = timeGap / HOUR + "小时前";
		} else if (timeGap > MINUTE) {// 1分钟-59分钟
			timeStr = timeGap / MINUTE + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}


	public static boolean checkTimeOverYear(long timestamp) {
		long currentTime = System.currentTimeMillis();
		long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
		if (timeGap > YEAR) {
			return true;
		}
		return false;
	}


	/**
	 * 根据时间戳获取描述性时间，
	 * 专门获取 时间间隔是多少天
	 * 返回天数
	 *
	 * @param timestamp 时间戳 单位为毫秒
	 * @return 时间字符串
	 */
	public static int getDesDayTimeFromTimestampCountDown(long timestamp) {
		long currentTime = System.currentTimeMillis();
		double timeGap = (double) Math.abs(timestamp - currentTime) / 1000;// 与现在时间相差秒数
		int timeStr;
		if (timeGap > DAY) {// 1天以上
			timeStr = (int) Math.ceil(timeGap / DAY);
		} else {// 1秒钟-59秒钟
			timeStr = 1;
		}
		return timeStr;
	}


	/**
	 * 根据时间戳获取描述性时间，
	 * 专门获取 时间间隔是多少小时
	 * 这里是向下取整数的 也就是 计算 3.5小时返回3小时 因为 0.5要显示分钟
	 *
	 * @param timestamp 时间戳 单位为毫秒
	 * @return 时间字符串
	 */
	public static int getDesHourTimeFromTimestampCountDown(long timestamp) {
		long currentTime = System.currentTimeMillis();
		double timeGap = (double) Math.abs(timestamp - currentTime) / 1000;// 与现在时间相差秒数
		int timeStr;
		if (timeGap > HOUR) {// 1小时以上
			timeStr = (int) Math.floor(timeGap / HOUR);
		} else {// 1秒钟-59秒钟
			timeStr = 0;
		}
		return timeStr;
	}

	/**
	 * 根据时间戳获取描述性时间，
	 * 专门获取 时间间隔是多少分钟
	 *
	 * @param timestamp 时间戳 单位为毫秒
	 * @return
	 */
	public static int getDesMinuteTimeFromTimestampCountDown(long timestamp) {
		long currentTime = System.currentTimeMillis();
		double timeGap = (double) Math.abs(timestamp - currentTime) / 1000;// 与现在时间相差秒数
		int timeStr;
		if (timeGap > HOUR) {// 1小时以上
			int floor = (int) Math.floor(timeGap / HOUR);
			int gap = floor * HOUR;
			timeGap -= gap;
		}
		if (timeGap > MINUTE) {// 1小时以上
			timeStr = (int) Math.floor(timeGap / MINUTE); // 舍弃多余 不足一分钟的秒
		} else {// 1秒钟-59秒钟
			timeStr = 1;
		}
		return timeStr;
	}

	public static String getDateStr(long timeMs, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(timeMs);
	}

	//校验(传递时间戳和当前时刻)时间间隔 是不是大于多少小时
	public static boolean checkTimesTampExceededTimePeriod(long timestamp, long hourNum) {
		long currentTime = System.currentTimeMillis();
		long timeGap = Math.abs(timestamp - currentTime) / 1000;// 与现在时间相差秒数
		long l = HOUR * hourNum;
		return (timeGap > l);
	}


	// 检查传入时间戳 和 当前时刻 是不是在同一年
	public static boolean checkTimeInSameYear(long timestamp) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_Y);
		return (dateFormat.format(timestamp).equals(dateFormat.format(System.currentTimeMillis())));
	}



}
