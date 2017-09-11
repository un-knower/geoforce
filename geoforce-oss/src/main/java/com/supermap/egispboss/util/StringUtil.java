package com.supermap.egispboss.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.dubbo.common.utils.StringUtils;
/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.supermap.egispweb.pojo.ResultBean;*/

/**
 * String工具类
 * 
 * @author CaoBin
 */
public class StringUtil {

	/**
	 * 判断字符串编码
	 * 
	 * @param str
	 * @return String
	 */
	public static String getEncoding(String str) {  
       String encode = "UTF-8";    
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
        encode = "GB2312"; 
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
	 * 
	 * @param value
	 * @return boolean
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
	 * 
	 * @param content
	 *            要过滤的内容
	 * @return String
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
	 * 
	 * @param content
	 *            要转换的内容
	 * @return String
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
	 * 
	 * @param str
	 * @return boolean
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
	 * 
	 * @param content
	 * @return String
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
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isNumber(String value) {
	  return isInteger(value) || isDouble(value);
	}
	
	
//	/**
//	 * 判断是否为数值
//	 * @param str
//	 * @return
//	 */
//	private static boolean isNumber(String str)  
//    {  
//        Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]*)?$");  
//        Matcher match=pattern.matcher(str);  
//        return match.matches();  
//    }  
	
	/**
	 * 是否为整数
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isInteger(String value) {
		if(StringUtils.isBlank(value) || value.equals("NaN")){//数字类型的空
			return false;
		}
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 是否为Double
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isDouble(String value) {
		if(StringUtils.isBlank(value) || value.equals("NaN")){//数字类型的空
			return false;
		}
		try {
			Double.parseDouble(value);
		    return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 是否是short
	 * 
	 * @param value
	 * @return boolean
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
	 * 向客户端写入
	 * 
	 * @param str
	 * @param response
	 */
	public static void printOut(String str,HttpServletResponse response) {
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
//			response.addHeader("Cache-Control", "max-age=120");callback[]
//			response.addHeader("Connection", "keep-alive");
//			response.setDateHeader("Expires", System.currentTimeMillis());
			PrintWriter out = response.getWriter();
			out.print(str); 
			out.flush();
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 校验是否为邮箱
	 * 
	 * @param email
	 * @return boolean
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
	 * 
	 * @param zipCode
	 * @return boolean
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
	 * 
	 * @param phone
	 * @return boolean
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
	 * 
	 * @param mobile
	 * @return boolean
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
	 * 
	 * @param phone
	 * @return boolean
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
	 * 
	 * @param node
	 * @return boolean
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
	 * 
	 * @param pwd
	 * @return boolean
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
	 * 
	 * @param str
	 * @return boolean
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
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isStringEmpty(String str){
		return (str == null || str.trim().length() <= 0);
	}
	
//	public static void main(String[] args){
//		System.out.println(isUserName("11aa11_-23as撒"));
//	}
	
	/**
	 * 判断是否为空
	 * 
	 * @param o
	 * @return String
	 */
	public static String convertObjectToString(Object o){
		String s="";
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
}
