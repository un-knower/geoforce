package com.supermap.egispservice.lbs.util;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


public class StringUtil {

	/**
	 * 判断字符串编码
	* @Title: getEncoding
	* @param str
	* @return
	* String
	* @throws
	 */
	public static String getEncoding(String str) {  
        String encode = "GB2312";    
       try {    
           if (str.equals(new String(str.getBytes(encode), encode))) {    
                String s = encode;    
               return s;    
            }    
        } catch (Exception exception) {    
        }    
        encode = "ISO-8859-1";    
       try {    
           if (str.equals(new String(str.getBytes(encode), encode))) {    
                String s1 = encode;    
               return s1;    
            }    
        } catch (Exception exception1) {    
        }    
        encode = "UTF-8";    
       try {    
           if (str.equals(new String(str.getBytes(encode), encode))) {    
                String s2 = encode;    
               return s2;    
            }    
        } catch (Exception exception2) {    
        }    
        encode = "GBK";    
       try {    
           if (str.equals(new String(str.getBytes(encode), encode))) {    
                String s3 = encode;    
               return s3;    
            }    
        } catch (Exception exception3) {    
        }    
       return "";    
    }
	public static String getUUid(){
		UUID uuid = UUID.randomUUID();
		String tmp = uuid.toString();
		char[] aa = tmp.toCharArray();
		String ret = "";
		for(int i=0;i<aa.length;i++){
			if(aa[i] != '-'){
				ret += aa[i];
			}
		}
		return ret;
	}
	/**
	 * 是否为utf-8编码
	* @Title: isUtf8
	* @param value
	* @return
	* boolean
	* @throws
	 */
	public static boolean isUTF8(String value){
		if(StringUtils.isBlank(value)){
			return true;
		}
		String enCode = getEncoding(value);
		if(enCode.equals("UTF-8")){
			return true;
		}else {
			return false;
		}
	}
	/**
	* 过滤特殊字符
	* @param content 要过滤的内容
	* @return
	*/
	public static String filtrateString(String content) {
		if (content == null || "".equals(content.trim())) {
			return content;
		}
//		content = content.replaceAll("<", "&lt;");
//		content = content.replaceAll(">", "&gt;");
//		content = content.replaceAll("\t", "    ");
//		content = content.replaceAll("\r\n", "\n");
//		content = content.replaceAll("\n", "<br/>");
//		content = content.replaceAll("'", "&#39;");
//		content = content.replaceAll("\\\\", "&#92;");
//		content = content.replaceAll("\"", "&quot;");
		content = content.replaceAll("<", "");
		content = content.replaceAll(">", "");
		content = content.replaceAll("\t", "");
		content = content.replaceAll("\r\n", "");
		content = content.replaceAll("\n", "");
		content = content.replaceAll("'", "");
		content = content.replaceAll("\\\\", "");
		content = content.replaceAll("\"", "");
		content = content.replaceAll("%", "");
		content = content.replaceAll("$", "");
		content = content.replaceAll("&", "");
		content = content.replaceAll("`", "");
		content = content.replaceAll("#", "");
		return content;
	}

	/**
	* 特殊字符转文本
	* @param content 要转换的内容
	* @return
	*/
	public static String reverseString(String content) {
		if (content == null || "".equals(content.trim())) {
			return content;
		}
		content = content.replaceAll("&lt;", "<");
		content = content.replaceAll("&gt;", ">");
		content = content.replaceAll("    ", "\t");
		content = content.replaceAll("\n", "\r\n");
		content = content.replaceAll("<br/>", "\n");
		content = content.replaceAll("&#39;", "'");
		content = content.replaceAll("&#92;", "\\\\");
		content = content.replaceAll("＜", "<");
		content = content.replaceAll("＞", ">");
		content = content.replaceAll("＂", "\"");
		
		return content;
	}
	/**
	* 判断字符串是否为数字
	* @param str
	* @return
	*/
	public static boolean isNumeric(String str) {
		for (int i = 0; i<str.length();i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	/**
	* 替换特殊字符串
	* @Title: ResplaceString
	* @param @param content
	* @param @return    
	* @return String    
	* @throws
	*/
	public static String ResplaceString(String content) {
		if (content == null || "".equals(content.trim())) {
			return content;
		}
		content = content.replaceAll("<", "＜");
		content = content.replaceAll(">", "＞");
		content = content.replaceAll("\"", "＂");
		return content;
    }
	/**
	 * 判断是否为数字
	* @Title: isNumber
	* @param value
	* @return
	* boolean
	* @throws
	 */
	public static boolean isNumber(String value) {
	  return isInteger(value) || isDouble(value);
	}
	/**
	 * 是否为整数
	* @Title: isInteger
	* @param value
	* @return
	* boolean
	* @throws
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	/**
	 * 是否为Double
	* @Title: isDouble
	* @param value
	* @return
	* boolean
	* @throws
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
		    return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	/**
	 * 是否是short
	* @Title: isShort
	* @param value
	* @return
	* boolean
	* @throws
	 */
	public static boolean isShort(String value) {
		if(StringUtils.isBlank(value) || value.equals("NaN")){//数字类型的空
			return false;
		}
		try {
			Short.parseShort(value);
		    return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	/**
	 * 校验是否为邮箱
	* @Title: isEmail
	* @param email
	* @return
	* boolean
	* @throws
	 */
	public static boolean isEmail(String email){
		boolean ret = false;
		if(StringUtils.isBlank(email)){
			return ret;
		}
		try {
			String check = "^[a-zA-Z0-9_\\-]{1,}@[a-zA-Z0-9_\\-]{1,}\\.[a-zA-Z0-9_\\-.]{1,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			ret = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	/**
	 * 是否为邮编
	* @Title: isZipCode
	* @param zipCode
	* @return
	* boolean
	* @throws
	 */
	public static boolean isZipCode(String zipCode){
		boolean ret = false;
		if(StringUtils.isBlank(zipCode)){
			return ret;
		}
		try {
			String check = "^[1-9][0-9]{5}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(zipCode);
			ret = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	/**
	 * 是否为固话
	* @Title: isPhone
	* @param phone
	* @return
	* boolean
	* @throws
	 */
	public static boolean isPhone(String phone){
		boolean ret = false;
		if(StringUtils.isBlank(phone)){
			return ret;
		}
		try {
			String check = "^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(phone);
			ret = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	/**
	 * 是否为手机号
	* @Title: isMobile
	* @param mobile
	* @return
	* boolean
	* @throws
	 */
	public static boolean isMobile(String mobile){
		boolean ret = false;
		if(StringUtils.isBlank(mobile)){
			return ret;
		}
		try {
			String check = "^0?(13[0-9]|15[012356789]|18[02356789]|14[57])[0-9]{8}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(mobile);
			ret = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	/**
	 * 是否为手机或固话
	* @Title: isPhoneOrMobile
	* @param phone
	* @return
	* boolean
	* @throws
	 */
	public static boolean isPhoneOrMobile(String phone){
		boolean ret = false;
		if(StringUtils.isBlank(phone)){
			return ret;
		}
		return isPhone(phone) || isMobile(phone);
	}
	/**
	 * 是否为身份证号
	* @Title: isPersonNode
	* @param node
	* @return
	* boolean
	* @throws
	 */
	public static boolean isPersonNode(String node){
		boolean ret = false;
		if(StringUtils.isBlank(node)){
			return ret;
		}
		try {
			String check = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(node);
			ret = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	/**
	 * 是否为密码（ 6-16位 字母、数字）
	* @Title: isPwd
	* @param pwd
	* @return
	* boolean
	* @throws
	 */
	public static boolean isPwd(String pwd){
		boolean ret = false;
		if(StringUtils.isBlank(pwd)){
			return ret;
		}
		try {
			String check = "^[a-zA-Z\\d]{6,16}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(pwd);
			ret = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	/**
	 * 是否是用户名(0-20 字母 数字 - _)
	* @Title: isUserName
	* @param str
	* @return
	* boolean
	* @throws
	 */
	public static boolean isUserName(String str){
		boolean ret = false;
		if(StringUtils.isBlank(str)){
			return ret;
		}
		try {
			String check = "^[a-zA-Z\\d_-]{0,20}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(str);
			ret = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}
	/**
	 * 对象转json
	* @Title: toJson
	* @param args
	* void
	* @throws
	 */
	/*public static String toJson(Object obj,Type type){
		
		Gson gson = new Gson();
		String json = gson.toJson(obj,type);
		return json;
	}*/
	/**
	 * 向客户端写入
	* @Title: printOut
	* @param str
	* @param response
	* void
	* @throws
	 */
	/*public static void printOut(String str,HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
//			response.addHeader("Cache-Control", "max-age=120");callback[]
//			response.addHeader("Connection", "keep-alive");
//			response.setDateHeader("Expires", System.currentTimeMillis());
			PrintWriter out = response.getWriter();
			String callback = request.getParameter("callback");
			if(StringUtils.isNotBlank(callback)){
				out.print(callback+"("+str+")");
			}else {				
				out.print(str); 
			}
			out.flush();
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}*/
	/**
	 * 获取容器里请求参数并转成string 用于打印日志
	* @Title: getParametersStr
	* @return
	* String
	* @throws
	 */
	public static String getParametersStr(Map<String,Object> parameters){
		String result = "";
		try {
			if(parameters != null){
				StringBuffer str = new StringBuffer(100);
				Set<String> keys = parameters.keySet();
				Iterator<String> it = keys.iterator();
				while (it.hasNext()) {
					String key = it.next();
					Object[] objs = (Object[])parameters.get(key);
					Object obj = null;
					if(objs != null && objs.length > 0){
						obj = objs[0];
					}
					String value = "";
					if(obj == null){
						value = "null";
					}else {
						if(obj instanceof String){
							value = (String)obj;
						}else if(obj instanceof Short){
							value = String.valueOf((Short)obj);
						}else if(obj instanceof Integer){
							value = String.valueOf((Integer)obj);
						}else if(obj instanceof Double){
							value = String.valueOf((Double)obj);
						}else if(obj instanceof Long){
							value = String.valueOf((Long)obj);
						}else if(obj instanceof Date){
							value = String.valueOf((Date)obj);
						}else {
							value = obj.toString();
						}
					}
					str.append("\""+key+"\":"+value+",");
				}
				result = str.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 将struts获取的页面参数自动转成对象的javabean
	* @Title: ParametersToBean
	* @param parameters 容器参数Map（context.getParameters()）
	* @param bean 要转成的bean
	* void
	* @throws
	 */
	public static Object ParametersToBean(Map<String, Object> parameters,Object bean){
		if(parameters == null || parameters.size() == 0)
			return bean;
		if(bean == null)
			return null;
		
		Field[] fs = bean.getClass().getDeclaredFields();
		PropertyDescriptor pd = null;
		Method mth = null;
		for(Field f:fs){
			String property = f.getName().trim();
			Object val = parameters.get(property);
			if(val == null)
				continue;
			try {
				pd = new PropertyDescriptor(property, bean.getClass());
				mth = pd.getWriteMethod();
				if(mth == null)
					continue;
				//set
				mth.invoke(bean, new Object[]{val});
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		return bean;
	}
	/**
	 * 
	* 方法名: doubleFiex
	* 描述:double精度控制
	* @return
	 */
	public static double doubleFiex(Double db,int num){
		
		double ret = 0;
		if(db == null)
			return 0;
		try {
			String result = String.format("%."+num+"f", db);
			ret = Double.valueOf(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}
