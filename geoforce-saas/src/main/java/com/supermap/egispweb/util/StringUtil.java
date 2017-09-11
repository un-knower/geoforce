package com.supermap.egispweb.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.mapping.Array;

import com.alibaba.dubbo.common.utils.StringUtils;
/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.supermap.egispweb.pojo.ResultBean;*/





import javassist.expr.NewArray;

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
	* @Title: isInteger
	* @param value
	* @return
	* boolean
	* @throws
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
	* @Title: isDouble
	* @param value
	* @return
	* boolean
	* @throws
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
	 * 对象转json
	* @Title: toJson
	* @param args
	* void
	* @throws
	 */
	/*public static String toJson(Object obj,Type type,String dateFormat){
		
		Gson gson = new GsonBuilder().setDateFormat("").create();
		String json = gson.toJson(obj,type);
		return json;
	}
	public static String toJson(Object obj,Type type){
		
		Gson gson = new Gson();
		String json = gson.toJson(obj,type);
		return json;
	}
	public static String toJson(Object obj,String dateFormat){
		
		Gson gson = new GsonBuilder().setDateFormat("").create();
		String json = gson.toJson(obj);
		return json;
	}
	public static String toJson(Object obj){
		
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		return json;
	}*/
//	/**
//	 * 封转错误信息bean
//	* @Title: setError
//	* @param code 错误代码 参见ApiError.java
//	* @param msg 错误描述
//	* @return
//	* ErrorBean
//	* @throws
//	 */
//	public static ErrorBean setError(String code,String msg){
//		ErrorBean errorBean = new ErrorBean();
//		errorBean.setCode(code);
//		errorBean.setMsg(msg);
//		return errorBean;
//	}
//	/**
//	 * 错误信息对象以json格式返回
//	* @Title: errorToJson
//	* @param code
//	* @param msg
//	* @return
//	* String
//	* @throws
//	 */
//	public static String errorToJson(String code,String msg){
//		try {
//			ErrorBean errorBean = setError(code,msg);
//			String json = toResultJson(0,errorBean);
//			return json;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "";
//	}
	/**
	 * 接口统一的返回结果tojson
	* @Title: toResultJson
	* @return
	* String
	* @throws
	 */
	/*public static String toResultJson(int result,Object info,String dateFormat){
		try {
			ResultBean bean = new ResultBean();
			bean.setResult(result);//0表示失败1表示成功
			bean.setInfo(info);
			String json = toJson(bean, new TypeToken<ResultBean>(){}.getType(),dateFormat);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String toResultJson(int result,Object info){
		try {
			ResultBean bean = new ResultBean();
			bean.setResult(result);//0表示失败1表示成功
			bean.setInfo(info);
			String json = toJson(bean, new TypeToken<ResultBean>(){}.getType());
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}*/
//	/**
//	 * 向客户端写入
//	* @Title: printOut
//	* @param str
//	* @param response
//	* void
//	* @throws
//	 */
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
	 * 
	 * <p>Title ：isStringEmpty</p>
	 * Description：		判断字符串是否为空
	 * @param str
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-15 下午03:55:21
	 */
	public static boolean isStringEmpty(String str){
		return (str == null || str.trim().length() <= 0);
	}
	
	public static void main(String[] args){
		System.out.println(isUserName("11aa11_-23as撒"));
	}
	
	/**
	 * object转String
	 * 
	 * @param o
	 * @return String
	 */
	public static String convertObjectToString(Object o){
		String s="";
		if(o!=null && !"null".equals(o) &&o.toString().length()>0){
			s=o.toString().trim();
		}
		return s;
	}
	/**
	 * Object 转int
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
