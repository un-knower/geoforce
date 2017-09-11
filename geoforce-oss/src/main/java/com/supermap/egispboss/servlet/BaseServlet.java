package com.supermap.egispboss.servlet;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.supermap.egispboss.constants.EgispBossConstants;
import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;


public class BaseServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(BaseServlet.class);
	
	private WebApplicationContext webApplicationContext = null;
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
	}
	
	
	/**
	 * 
	 * <p>Title ：getBean</p>
	 * Description：	根据名词获取Bean实例
	 * @param name
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-7-25 下午05:17:06
	 */
	public Object getBean(String name){
		return webApplicationContext.getBean(name);
	}
	
	/**
	 * 
	 * <p>Title ：buildResult</p>
	 * Description：		构建成功结果
	 * @param status
	 * @param obj
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-7-25 上午10:37:29
	 */
	public static String buildResult(String info,Object obj,boolean isSuccess){
		JSONObject jsonObj = new JSONObject();
		jsonObj.accumulate("info", info);
		jsonObj.accumulate("result", obj);
		jsonObj.accumulate("success", isSuccess);
		String resultInfo = jsonObj.toString();
		return resultInfo;
	}
	
	/**
	 * 带日期格式的转换
	 * @param info
	 * @param obj
	 * @param isSuccess
	 * @param dateFormat
	 * @return
	 * @Author Juannyoh
	 * 2016-8-11下午3:12:32
	 */
	public static String buildResult(String info,Object obj,boolean isSuccess,final String dateFormat){
		JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,new JsonValueProcessor() {
            private final String format=dateFormat;
            public Object processObjectValue(String key, Object value,JsonConfig arg2){
              if(value==null)
                    return "";
              if (value instanceof java.util.Date) {
                    String str = new SimpleDateFormat(format).format((java.util.Date) value);
                    return str;
              }
                    return value.toString();
            }
      
            public Object processArrayValue(Object value, JsonConfig arg1){
                       return null;
            }
            
         });
		JSONObject jsonObj = new JSONObject();
		jsonObj.accumulate("info", info);
		jsonObj.accumulate("result", JSONObject.fromObject(obj,jsonConfig));
		jsonObj.accumulate("success", isSuccess);
		String resultInfo = jsonObj.toString();
		return resultInfo;
	}
	
	/**
	 * 
	 * <p>Title ：reply</p>
	 * Description：		向客户端返回结果
	 * @param resp
	 * @param resultInfo
	 * Author：Huasong Huang
	 * CreateTime：2014-7-25 上午10:40:52
	 */
	public static void write(HttpServletResponse resp,String resultInfo){
		resp.setCharacterEncoding("UTF-8");
		try{
			PrintWriter writer = resp.getWriter();
			writer.write(resultInfo);
			writer.flush();
			writer.close();
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 
	 * <p>Title ：write</p>
	 * Description：	向客户端返回结果
	 * @param resp
	 * @param Info
	 * @param obj
	 * @param isSuccess
	 * Author：Huasong Huang
	 * CreateTime：2014-7-25 上午10:49:24
	 */
	public void write(HttpServletResponse resp,String info,Object obj,boolean isSuccess){
		String resultInfo = buildResult(info, obj, isSuccess);
		LOGGER.info("## 返回结果 ：" + (resultInfo.length() > 50?resultInfo.subSequence(0, 50)+"...":resultInfo));
		write(resp, resultInfo);
	}
	/**
	 * 
	 * <p>Title ：write</p>
	 * Description：		带有callbacks的返回
	 * @param req
	 * @param resp
	 * @param info
	 * @param obj
	 * @param isSuccess
	 * Author：Huasong Huang
	 * CreateTime：2014-7-29 上午11:10:18
	 */
	public static void write(HttpServletRequest req,HttpServletResponse resp,String info,Object obj,boolean isSuccess){
		String resultInfo = buildResult(info, obj, isSuccess);
		String callbacks = req.getParameter("callbacks");
		if(!CommonUtil.isStringEmpty(callbacks)){
			resultInfo = callbacks + "(" + resultInfo+")";
		}
		LOGGER.info("## 返回结果 ：" + (resultInfo.length() > 50?resultInfo.subSequence(0, 50)+"...":resultInfo));
		write(resp, resultInfo);
		
	}
	
	/**
	 * 指定日期格式的返回
	 * @param req
	 * @param resp
	 * @param info
	 * @param obj
	 * @param isSuccess
	 * @Author Juannyoh
	 * 2016-8-11下午3:15:56
	 */
	public static void write(HttpServletRequest req,HttpServletResponse resp,String info,Object obj,boolean isSuccess,String dateFormat){
		String resultInfo = buildResult(info, obj, isSuccess,dateFormat);
		String callbacks = req.getParameter("callbacks");
		if(!CommonUtil.isStringEmpty(callbacks)){
			resultInfo = callbacks + "(" + resultInfo+")";
		}
		LOGGER.info("## 返回结果 ：" + (resultInfo.length() > 50?resultInfo.subSequence(0, 50)+"...":resultInfo));
		write(resp, resultInfo);
		
	}
	
	/**
	 * 
	 * <p>Title ：decoder</p>
	 * Description： 
	 * @param value
	 * @param srcCharset
	 * @param targeCharset
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-8 下午03:41:54
	 */
	public String characterConvert(String value,String srcCharset,String targeCharset)throws ParameterException{
		try{
			if(!CommonUtil.isStringEmpty(value)){
//				if(value.contains("%")){
//					value = URLDecoder.decode(value);
//				}else{
					value = new String(value.getBytes(srcCharset),targeCharset);
//				}
			}
		}catch(Exception e){
			throw new ParameterException("字符转换发生异常["+value+","+srcCharset+","+targeCharset+"]");
		}
		return value;
	}
	
	/**
	 * 
	 * <p>Title ：defaultCharacterConvert</p>
	 * Description：	默认的字符转换
	 * @param value
	 * @return
	 * @throws ParameterException
	 * Author：Huasong Huang
	 * CreateTime：2014-8-8 下午03:47:56
	 */
	public String defaultCharacterConvert(String value)throws ParameterException{
//		return characterConvert(value, "iso-8859-1", "utf-8");
		return characterConvert(value, EgispBossConstants.URI_ENCODING, "utf-8");
	}
	
	public  String encodeFileName(HttpServletRequest request,  
            String fileName) throws UnsupportedEncodingException {  
        String agent = request.getHeader("USER-AGENT");  
        if (null != agent && -1 != agent.indexOf("MSIE")) {  
            return URLEncoder.encode(fileName, "UTF-8");  
        } else if (null != agent && -1 != agent.indexOf("Mozilla")) {  
            return "=?UTF-8?B?"  
                    + (new String(Base64.encodeBase64(fileName  
                            .getBytes("UTF-8")))) + "?=";  
        } else {  
            return fileName;  
        }  
    }  
}
