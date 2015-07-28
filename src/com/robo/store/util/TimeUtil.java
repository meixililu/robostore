package com.robo.store.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;

public class TimeUtil {
	
	public static final String DateFormat = "yyyy-MM-dd HH:mm:ss";
	public static final String DateMinuteFormat = "yyyy-MM-dd HH:mm";
	public static final String WeekFormat = "yyyy-MM-dd EEEE";
	public static final String DayHHMMFormat = "yyyy-MM-dd HH:mm";
	public static final String DayFormat = "yyyy-MM-dd";
	public static final String Day2Format = "yyyyMMdd";
	public static final String TimeFormat = "HH:mm:ss";
	public static final String MinuteFormat = "HH:mm";
	public static final String MonthFormat = "MM-dd";
	public static final String MonthFormat2 = "MM月dd日";
	public static final String MonthMinuteFormat = "MM-dd HH:mm";
	public static final String OneDay = "23:59:59";
	
	/**
	 * 返回时间格式�?12-28 19:28
	 */
	public static String formatLongTimeForCustom(long time, String format) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 返回时间格式：星期一
	 */
	public static String getOnlyWeek(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		return sdf.format(date);
	}
	
	/**
	 * 返回时间格式�?20111228
	 */
	public static String getTimeDateLong(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	
	/**
	 * 返回时间格式�?2011-12-28
	 */
	public static String getTimeDate(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	/**
	 * 返回时间格式�?201203281122
	 */
	public static String getTimeAsNumber(long time) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(date);
	}

	/**
	 * 格式化日�?
	 * 
	 * @return
	 */
	public static String formatDate(String dateStr, String style) {
		if (TextUtils.isEmpty(dateStr)) {
			return "";
		}
		try {
			SimpleDateFormat simpleDateormat = new SimpleDateFormat(style);
			Date date = simpleDateormat.parse(dateStr);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM");
			return sdf.format(date);
		} catch (ParseException e) {
			return "";
		}
	}

	/***
	 * 日期格式：yyyy-MM-dd HH:mm:ss 转成时间�?
	 */
	public static long getDateToTime(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		long t = 0;
		try {
			date = format.parse(time);
			t = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// System.out.println("确认转换数据没错");
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 根据�?个日期，返回是星期几的字符串,2012-10-26 星期�?
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getWeek(String sdate) {
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		Calendar c = Calendar.getInstance();
		try {
			date = sdf.parse(sdate);
			c.setTime(date);
		} catch (ParseException e) {
			return "";
		}
		String result = new SimpleDateFormat("yyyy-MM-dd EEEE").format(c.getTime());
		return result;
	}
	
	public static String getyyMMddHHmm(String dateStr){
		return customFormatDate(dateStr, "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm");
	}

	/**自定义时间格�?
	 * @param dateStr  时间字符串；
	 * @param oldStyle 传进来的时间字符串的格式�?
	 * @param newStyle 希望得到的时间格�?
	 * @return
	 */
	public static String customFormatDate(String dateStr, String oldStyle, String newStyle) {
		if (TextUtils.isEmpty(dateStr)) {
			return "";
		}
		try {
			SimpleDateFormat simpleDateormat = new SimpleDateFormat(oldStyle);
			Date date = simpleDateormat.parse(dateStr);
			SimpleDateFormat sdf = new SimpleDateFormat(newStyle);
			return sdf.format(date);
		} catch (ParseException e) {
			LogUtil.ExceptionLog("TimeUtils-customFormatDate()");
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 返回时间格式�?12-28 19:28
	 */
	public static String getLongtimeToShorttime(String time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		Date d = null;
		try {
			d = sf.parse(time);
		} catch (ParseException e) {
			LogUtil.ExceptionLog("TimeUtils-getLongtimeToShorttime()");
			e.printStackTrace();
		}
		return sdf.format(d);
	}
	
	/**将时分转成�?�共多少分钟，用于推送比较时间大�?
	 * @param time
	 * @return
	 */
	public static int getTimeForMilliscond(String time){
		try{
			String[] times = time.split(":");
			if(times.length == 2){
				int hour = Integer.parseInt(times[0]);
				int minute = Integer.parseInt(times[1]);
				return hour * 60 + minute;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static Date getStringTime(String time){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sf.parse(time);
		} catch (ParseException e) {
			LogUtil.ExceptionLog("TimeUtils-getLongtimeToShorttime()");
			e.printStackTrace();
		}
		return d;
	}
	
	public static String compareDate(String endDate){
		DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		try{
		    Date d1 = df.parse(endDate);
		    Date d2 = new Date();
		    long diff = d1.getTime() - d2.getTime();
		    long days = diff/(24*60*60*1000);
		    long hour = (diff/(60*60*1000)-days*24);
		    long min = ((diff/(60*1000))-days*24*60-hour*60);
		    long s = (diff/1000-days*24*60*60-hour*60*60-min*60);
		    if(days > 0){
		    	return days +  "天";
		    }else if(hour > 0){
		    	return hour +  "小时";
		    }else if(min > 0){
		    	return min +  "分钟";
		    }else if(s > 0){
		    	return s +  "秒";
		    }
		} catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
}
