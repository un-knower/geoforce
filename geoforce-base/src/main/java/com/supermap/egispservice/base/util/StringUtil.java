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

}
