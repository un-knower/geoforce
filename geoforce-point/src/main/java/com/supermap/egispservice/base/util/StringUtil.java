package com.supermap.egispservice.base.util;

import com.alibaba.dubbo.common.utils.StringUtils;

public class StringUtil {
	
	/**
	 * 补齐字符串
	 * @param from  原字符串
	 * @param length  长度
	 * @param fix  补全的字符
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3下午2:33:29
	 */
	public static String fromatString(String from,int length,String fix){
		StringBuilder sb=new StringBuilder(from);
		if(StringUtils.isNotEmpty(from)){
			for(int i=from.length();i<length;i++){
				sb.append(fix);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 判断是否为空
	 * 
	 * @param o
	 * @return String
	 */
	public static String convertObjectToString(Object o){
		String s=null;
		if(o!=null&&o.toString().length()>0){
			s=o.toString().trim();
		}
		return s;
	}
	/**
	 * @param o
	 * @return int
	 */
	public static int convertObjectToInt(Object o){
		int s=-1;
		if(o!=null&&o.toString().length()>0){
			s=Integer.parseInt(o.toString().trim());
		}
		return s;
	}
	
	public static double convertObjectToDouble(Object o){
		double d=0;
		if(o!=null&&o.toString().length()>0){
			d=Double.parseDouble(o.toString().trim());
		}
		return d;
	}

}
