package com.supermap.egispboss.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.supermap.egispservice.base.exception.ParameterException;

public class CommonUtil {
	
	/**
	 * 
	 * <p>Title ：isStringEmpty</p>
	 * Description：		检查字符串是否为空
	 * @param str
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-7-25 上午10:28:49
	 */
	public static boolean isStringEmpty(String str){
		boolean isNull = false;
		if(null == str || str.trim().length() <= 0){
			isNull = true;
		}
		return isNull;
	}
	
	/**
	 * 
	 * <p>Title ：checkRequredParam</p>
	 * Description：检查哪些参数是否必须
	 * @param fieldNames	参数名
	 * @param values		参数值
	 * @return				检查结果，空串表示都满足要求
	 * Author：Huasong Huang
	 * CreateTime：2014-7-25 下午03:57:41
	 */
	public static String checkRequredParam(String[] paramNames,String[] values){
		StringBuilder sb = new StringBuilder();
		boolean isSuccess = true;
		sb.append("字段[");
		for(int i=0;i<paramNames.length;i++){
			if(isStringEmpty(values[i])){
				if(isSuccess){
					isSuccess = false;
				}
				sb.append(paramNames[i]);
				if(i < (paramNames.length -1)){
					sb.append(",");
				}
			}
		}
		if(!isSuccess){
			sb.append("不允许为空");
		}else{
			sb = new StringBuilder();
		}
		return sb.toString();
	}
	
	public static final String IMG_SUFFIX_PATTERN = ".*(\\.bmp|\\.png|\\.jpg|\\.gif|\\.jpeg){1}$";
	/**
	 * 
	 * <p>Title ：isImgSuffix</p>
	 * Description：
	 * @param fileName
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-7-28 上午09:10:47
	 */
	public static boolean isImgSuffix(String fileName){
		boolean returnStatus = false;
		if(!isStringEmpty(fileName)){
			String tempName = fileName.toLowerCase();
			returnStatus = tempName.matches(IMG_SUFFIX_PATTERN);
		}
		return returnStatus;
	}
	
	
	/**
	 * 
	 * <p>Title ：dateConvert</p>
	 * Description：		时间戳转换，支持格式：yyyy-MM-dd HH:mm:ss
	 * 					日期：yyyy-MM-dd
	 * 					时间：HH:mm:ss
	 * @param dateStr
	 * @param type
	 * @return
	 * @throws ParameterException
	 * Author：Huasong Huang
	 * CreateTime：2014-8-12 上午11:30:06
	 */
	public static final Date dateConvert(String dateStr,DateType type)throws ParameterException{
		Date date = null;
		try{
			if(DateType.DATE_TYPE.equals(type)){
				date = DATE_FORMAT.parse(dateStr);
			}else if(DateType.TIMESTAMP.equals(type)){
				date = DATESTAMP_FORMAT.parse(dateStr);
			}else if(DateType.TIME.equals(type)){
				date = TIME_FORMAT.parse(dateStr);
			}
		}catch(Exception e){
			throw new ParameterException("["+dateStr+"]转换异常,转换类型["+type+"");
		}
		return date;
	}
	
	/**
	 * 
	 * <p>Title ：dataConvert</p>
	 * Description：		将日期类型转换为字符串
	 * @param date
	 * @param type
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-12 下午05:04:46
	 */
	public static final String dataConvert(Date date,DateType type){
		String result = null;
		try{
			if(DateType.DATE_TYPE.equals(type)){
				result = DATE_FORMAT.format(date);
			}else if(DateType.TIME.equals(type)){
				result = TIME_FORMAT.format(date);
			}else if(DateType.TIMESTAMP.equals(type)){
				result = DATESTAMP_FORMAT.format(date);
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}
	
	private static Logger LOGGER = Logger.getLogger(CommonUtil.class);
	
	// 时间戳转换器
	private static SimpleDateFormat DATESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 时间戳转换器
	private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	// 日期转换器
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public enum DateType{
		DATE_TYPE,
		TIMESTAMP,
		TIME;
	};
	
	
	/**
	 * 
	 * <p>Title ：dateRange</p>
	 * Description：		计算时间跨度的天数
	 * @param start		
	 * @param end
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-3 上午11:36:45
	 */
	public static int dateRange(Date start,Date end){
		return -1;
	}
	
	
	
}
