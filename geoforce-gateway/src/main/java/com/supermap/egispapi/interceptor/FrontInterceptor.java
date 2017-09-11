package com.supermap.egispapi.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.supermap.egispapi.constants.CodeConstants;
import com.supermap.egispapi.constants.Config;
import com.supermap.egispapi.constants.TemplateNames;
import com.supermap.egispapi.pojo.ResponseCode;
import com.supermap.egispapi.service.IUserInfoCache;

/**
 * 
 * <p>Title: FrontInterceptor</p>
 * Description:	前端控制器
 * 		1：校验用户合法性；
 * 		2：统计用户调用情况；
 * 		3：
 *
 * @author Huasong Huang
 * CreateTime: 2015-4-7 上午11:34:18
 */
public class FrontInterceptor implements HandlerInterceptor {

	private static Logger LOGGER = Logger.getLogger(FrontInterceptor.class);
	
	
	@Autowired
	private Config config;
	
	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception e)
			throws Exception {
		if(e != null){
			System.out.println(e.toString());
		}
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object obj, ModelAndView mv)
			throws Exception {
		Map<String,Object> map = mv.getModel();
		if(map.containsKey(TemplateNames.FINAL_INFO_NAME)){
			map.put(TemplateNames.FINAL_SUCCESS_NAME, false);
			String resultInfo = (String) map.get(TemplateNames.FINAL_INFO_NAME);
			if(!StringUtils.isEmpty(resultInfo) && resultInfo.length() > 50){
				map.put(TemplateNames.FINAL_INFO_NAME, "");
			}
		}else{
			map.put(TemplateNames.FINAL_SUCCESS_NAME, true);
		}
		mv.clear();
		mv.setViewName(TemplateNames.JSON_VIEW_NAME);
		mv.addObject(TemplateNames.FINAL_RESULT_NAME,map);
	}

//	@Autowired
//	private UserService userService;
	
	@Autowired
	private IUserInfoCache userInfoCache;
	
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		String key = req.getParameter("key");
		req.setAttribute("requestTime", System.currentTimeMillis());
		String requestUri = req.getRequestURI();
		LOGGER.info("## 请求URI：" + requestUri);
		if (requestUri.contains("validation")) {
			return true;
		}
		// 检查Key
		if (StringUtils.isEmpty(key)) {
			sendToError(req, resp, "参数Key不允许为空", CodeConstants.PARAM_NOT_ALLOW_NULL);
			return false;
		}
		
		// 检查Param
		String param = req.getParameter("param");
		if(StringUtils.isEmpty(param)){
			sendToError(req, resp, "参数param不允许为空", CodeConstants.PARAM_NOT_ALLOW_NULL);
			return false;
		}
		

		LOGGER.info("key:" + key);
//		Object ue = req.getSession().getAttribute(key);
//		if (ue == null && config.isDoNeedValidation()) {
//			ue = userService.findUserById(key);
//			if (null != ue) {
//				req.getSession().setAttribute(key, ue);
//				return true;
//			} else {
//				LOGGER.warn("## 未找到用户信息[");
//				sendToError(req, resp, "用户信息不存在", CodeConstants.USER_NOT_EXIST);
//				return false;
//			}
//		}
		Object ue = null;
		if(config.isDoNeedValidation()){
			ue = this.userInfoCache.findByKey(key);
			if(null == ue){
				LOGGER.warn("## 未找到用户信息["+key+"]");
				sendToError(req, resp, "用户信息不存在", CodeConstants.USER_NOT_EXIST);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * <p>Title ：sendToError</p>
	 * Description：		跳转到错误处理
	 * @param req
	 * @param resp
	 * @param info
	 * @param code
	 * Author：Huasong Huang
	 * CreateTime：2015-4-10 下午02:59:46
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void sendToError(HttpServletRequest req,HttpServletResponse resp,String info,String code) throws ServletException, IOException{
		ResponseCode codeInfo = new ResponseCode();
		codeInfo.setCode(code);
		codeInfo.setInfo(info);
		req.setAttribute("validation", codeInfo);
		req.getRequestDispatcher(config.getErrorProcessUri()).forward(req, resp);
	}
	
	

}
