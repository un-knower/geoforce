package com.supermap.egispweb.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.util.Config;
/**
 * 
 * @description 拦截器
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-9-19
 * @version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor
{

	@Autowired
    private Config config;
	
	/**
	 * 拦截器调用完成后的回调函数
	 */
	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception ex) throws Exception{
	}

	/**
	 * 发送请求处理（未实现）
	 */
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object obj, ModelAndView mav) throws Exception{
	}

	/**
	 * 预处理用户登录请求
	 */
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception{
		// 其它业务需要判断是否登录
		UserEntity user = (UserEntity) req.getSession().getAttribute("user");
		if (user == null){
			// 只有不为离线部署时，才跳转到门户页面
			if(!config.isDeployOffline()){
				resp.setContentType("text/html;");
				PrintWriter out = resp.getWriter();
				out.println("<script language='javascript' type='text/javascript'>");
				out.println("window.location='"+config.getPortaltUrl()+"'");
				out.println("</script>");
				return false;
			}else{
				resp.sendRedirect("/egispweb/login");
				return false;
			}
		} else{
			return true;
		}
	}
}
