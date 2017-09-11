package com.supermap.egispweb.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtil {
	public static DateFormat dateFormat = null;
	public static Calendar calendar = null;

	/**
	 * 此处格式化是位了查询历史轨迹时连接天表
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy_MM_dd");
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
	
	public static Date  formatStringByDate(String date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
    /**   
     * 得到本月的第一天   
     * @return   
     */    
    public static String getMonthFirstDay(String format) {     
        Calendar calendar = Calendar.getInstance();     
        calendar.set(Calendar.DAY_OF_MONTH, calendar     
                .getActualMinimum(Calendar.DAY_OF_MONTH));     
        
        return format(calendar.getTime(),format );     
    }     
        
    /**   
     * 得到本月的最后一天   
     *    
     * @return   
     */    
    public static String getMonthLastDay(String format) {     
        Calendar calendar = Calendar.getInstance();     
        calendar.set(Calendar.DAY_OF_MONTH, calendar     
                .getActualMaximum(Calendar.DAY_OF_MONTH));     
        return format( calendar.getTime(),format);     
    }
    public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }  
	public static void main(String arc[]) {
		DateUtil du=new DateUtil();
		String abc = "20130427122001";
		System.out.println(du.formatStringToDate(abc, "yyyyMMddHHmmss"));
		Date dd=du.formatStringToDate(abc, "yyyyMMddHHmmss");
		System.out.println(du.format(dd, "yyyyMMdd"));
	}
	
    /*
	* 取本周7天的第一天（周一的日期）
	*/
	public static String getNowWeekBegin(String format) {
		int mondayPlus;
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			mondayPlus = 0;
		} else {
			mondayPlus = 1 - dayOfWeek;
		}
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		DateFormat df = new SimpleDateFormat(format);
		String preMonday = df.format(monday);
	
		return preMonday;
	} 
	/**
	 * 时间格式转化 data 
	 * @return
	 */
	public static String dataString(Date date){
		String str=null;
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			str=sdf.format(date);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
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
		startDay = format(formatStringByDate(startDay, "yyyy-MM-dd"), format);
		
		String endDay = DateUtil.formatDateByFormat(date, "yyyy-MM")+"-"
				+c.getActualMaximum(Calendar.DAY_OF_MONTH);
		endDay = format(formatStringByDate(endDay, "yyyy-MM-dd"), format);
		
		return new String[]{startDay,endDay};
	}
	/**
     * 当月第一天
     * @return
     */
	public  static String getFirstDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        return str.toString();

    }
    
    /**
     * 当月最后一天
     * @return
     */
	public static String getLastDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        StringBuffer str = new StringBuffer().append(s).append(" 23:59:59");
        return str.toString();

    }
}
