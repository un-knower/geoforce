package com.supermap.egispservice.lbs.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Date date = null;

	public static DateFormat dateFormat = null;

	public static Calendar calendar = null;

	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期
	 * @param format
	 *            String 格式
	 * @return Date 日期
	 */
	public static Date parseDate(String dateStr, String format) {
		try {
			dateFormat = new SimpleDateFormat(format);
			String dt = dateStr.replaceAll("-", "/");
			if ((!dt.equals("")) && (dt.length() < format.length())) {
				dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]",
						"0");
			}
			date = (Date) dateFormat.parse(dt);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期：yyyy/MM/dd 格式
	 * @return Date
	 */
	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：格式化输出日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return 返回字符型日期
	 */
	public static String format(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 功能描述：
	 * 
	 * @param date
	 *            Date 日期
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：返回年份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回年份
	 */
	public static int getYear(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 功能描述：返回月份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回月份
	 */
	public static int getMonth(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 功能描述：返回日份
	 * 
	 * @param date
	 *            Date 日期
	 * 
	 * 
	 * @return 返回日份
	 */
	public static int getDay(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 功能描述：返回小时
	 * 
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 功能描述：返回分钟
	 * 
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 返回秒钟
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 功能描述：返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 功能描述：返回字符型日期
	 * 
	 * @param date
	 *            日期
	 * @return 返回字符型日期 yyyy/MM/dd 格式
	 */
	public static String getDate(Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：返回字符型时间
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回字符型时间 HH:mm:ss 格式
	 */
	public static String getTime(Date date) {
		return format(date, "HH:mm:ss");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
	 */
	public static String getDateTime(Date date) {
		return format(date, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * 功能描述：日期相加
	 * 
	 * @param date
	 *            Date 日期
	 * @param day
	 *            int 天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		calendar = Calendar.getInstance();
		long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * 功能描述：日期相减
	 * 
	 * @param date
	 *            Date 日期
	 * @param date1
	 *            Date 日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 功能描述：取得指定月份的第一天
	 * 
	 * @param strdate
	 *            String 字符型日期
	 * @return String yyyy-MM-dd 格式
	 */
	public static String getMonthBegin(String strdate) {
		date = parseDate(strdate);
		return format(date, "yyyy-MM") + "-01";
	}
	
	/**
	 * 功能描述：格式化输出日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return 返回字符型日期
	 */
	public static Date formatStringToDate(String date, String format) {
		Date result = null;

		if (date != null) {
			dateFormat = new SimpleDateFormat(format);
			try {
				return result = dateFormat.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;

	}

	/**
	 * 功能描述：取得指定月份的最后一天
	 * 
	 * @param strdate
	 *            String 字符型日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String getMonthEnd(String strdate) {
		date = parseDate(getMonthBegin(strdate));
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return formatDate(calendar.getTime());
	}

	/**
	 * 功能描述：常用的格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String formatDate(Date date) {
		return formatDateByFormat(date, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：以指定的格式来格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String 日期字符串
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 日期相减
	 * 
	 * @param date1
	 *            减数
	 * @param date2
	 *            被减数
	 * @return 毫秒
	 */
	public static long diffDates(Date date1, Date date2) {

		return date1.getTime() - date2.getTime();

	}

	/**
	 * 功能描述：以指定的格式来格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String 日期字符串
	 */
	public static Date formatDateByFormat(String date, String format) {

		Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			result = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}
	/**
	 * 日期转换为 [开始天,结束天]
	* @Title: getToDayTime
	* @return
	* String[]
	* @throws
	 */
	public static String[] getToDayTime(Date date,String format){
		return new String[]{formatDateByFormat(date, format),formatDateByFormat(date, format)};
	}
	/**
	 * 日期转换为 本周的[开始天,结束天]
	* @Title: getWeekTime
	* @return
	* String[]
	* @throws
	 */
	public static String[] getWeekTime(Date date,String format){
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // 星期一
		String startDay = DateUtil.formatDateByFormat(c.getTime(), format);
		
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); //星期天
		String endDay = DateUtil.formatDateByFormat(c.getTime(), format);
		return new String[]{startDay,endDay};
	}
	/**
	 * 日期转换为 本月的[开始天,结束天]
	* @Title: getMonthTime
	* @return
	* String[]
	* @throws
	 */
	public static String[] getMonthTime(Date date,String format){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String startDay = DateUtil.formatDateByFormat(date, "yyyy-MM")+"-"
				+c.getActualMinimum(Calendar.DAY_OF_MONTH);
		startDay = format(formatDateByFormat(startDay, "yyyy-MM-dd"), format);
		
		String endDay = DateUtil.formatDateByFormat(date, "yyyy-MM")+"-"
				+c.getActualMaximum(Calendar.DAY_OF_MONTH);
		endDay = format(formatDateByFormat(endDay, "yyyy-MM-dd"), format);
		
		return new String[]{startDay,endDay};
	}
	public static void main(String[] args) {
		 Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");
		System.out.println(df.format(d));

//		try {
//			Date d2 = df.parse("2004_03-26 13:31:01");
//			Date d1 = df.parse("2004-03-26 13:31:31");
//			long a = d1.getTime() - d2.getTime();
//			System.out.println(a);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String abc = "2c8d80a331d7365d0131d73b4de10007";
//		String[] ky = abc.split(",");
//		for (String a : ky) {
//			System.out.println(a + "---kk");
//		}
		// System.out.println(d.toString());
		// System.out.println(formatDate(d).toString());
		// System.out.println(getMonthBegin(formatDate(d).toString()));
		// System.out.println(getMonthBegin("2008/07/19"));
		// System.out.println(getMonthEnd("2008/07/19"));
		// System.out.println(addDate(d, 15).toString());
	}

}
