package com.supermap.egispportal.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.supermap.egispportal.access.AccessManager;
import com.supermap.egispportal.constants.PortalConstants;
import com.supermap.egispportal.servlet.BaseServlet;
import com.supermap.egispportal.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BaseAccessInfo;


public class AccessFilter extends AccessControlFilter {
	private WebApplicationContext context = null;
	
	private void init() {
		ServletContext servletContext = this.getServletContext(); 
	    context = WebApplicationContextUtils.getWebApplicationContext(servletContext); 
	    org.apache.shiro.mgt.SecurityManager securtyManager = (SecurityManager) context.getBean("securityManager");
	    SecurityUtils.setSecurityManager(securtyManager);

	}
	
	private static Logger LOGGER = Logger.getLogger(AccessFilter.class);
	
	@Override
	protected boolean isAccessAllowed(ServletRequest req,
			ServletResponse resp, Object obj) throws Exception {
		String method = req.getParameter("method");
		LOGGER.info("## access filter");
		HttpServletRequest request = (HttpServletRequest) req;
		String requestUri = request.getRequestURI();
		if(!CommonUtil.isStringEmpty(method)){
			requestUri = requestUri +"?method="+method;
		}
		LOGGER.info("requestURI : "+requestUri);
		Subject subject = (Subject)obj;
		// 如果请求URI需要用户登录，则判断是否用户已经登录了
		if(AccessManager.isNeedLogin(requestUri)){
			return subject.isAuthenticated();
		}
		return true;
	}

	@Override
	public void doFilterInternal(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws ServletException, IOException {
		LOGGER.info("## 访问过滤器");
		String method = req.getParameter("method");
		HttpServletRequest request = (HttpServletRequest) req;
		try{
			Subject subject = SecurityUtils.getSubject();
			if(StringUtils.isEmpty(method) && !request.getRequestURI().endsWith("logout")){
				chain.doFilter(req, resp);
			}else if(!StringUtils.isEmpty(method) && method.equals("login")){
				UsernamePasswordToken token = new UsernamePasswordToken();
				Object obj =  request.getSession().getAttribute("_const_cas_assertion_");
				String email = null;
				String username = null;
				String tel = null;
				String rid = null;
				int iid = 0;
				String password = "";
				StringBuilder loginInfoBuilder = new StringBuilder();
				if(null != obj){
					
					Assertion assertion = (Assertion)obj;
					email = assertion.getPrincipal().getName();
					Map<String,Object> map = assertion.getPrincipal().getAttributes();
					Set<String> keySets = map.keySet();
					// 获取用户邮箱信息
					if(null != keySets && keySets.contains("email")){
						email = map.get("email").toString();
					}
					if(null != keySets && keySets.contains("nickname")){
						username = map.get("nickname").toString();
					}
					if(null != keySets && keySets.contains("tel")){
						tel = map.get("tel").toString();
					}
					if(null != keySets && keySets.contains("id")){
						rid = map.get("id").toString();
					}
					if(!StringUtils.isEmpty(rid)){
						iid = Integer.parseInt(rid);
					}
					// 添加邮箱
					if(StringUtils.isEmpty(email)){
						loginInfoBuilder.append("#,");
					}else{
						loginInfoBuilder.append(email).append(",");
					}
					// 添加用户名
					if(StringUtils.isEmpty(username)){
						loginInfoBuilder.append("#,");
					}else{
						username = new String(username.getBytes(),"utf-8");
						loginInfoBuilder.append(username).append(",");
					}
					// 添加电话
					if(StringUtils.isEmpty(tel) || !CommonUtil.isTelephone(tel)){
						loginInfoBuilder.append("#");
					}else{
						loginInfoBuilder.append(tel);
					}
					
					
				}else{
					throw new Exception("未找用户回传信息，登录失败");
				}
				String loginInfo = loginInfoBuilder.toString();
				LOGGER.info("## 获取到用户信息[" + loginInfo + "]");

				token.setUsername(loginInfo);
				token.setPassword(password.toCharArray());
				
				
				subject.login(token);
				BaseAccessInfo accessInfo = (BaseAccessInfo) subject.getPrincipal();
//				String source = accessInfo.getSource();
//				if (null != source && source.equals("-1") && !StringUtils.isEmpty(username)) {
//					accessInfo.setUsername(username);
//				}
				accessInfo.setIid(iid);
				accessInfo.setUsername(username);
				accessInfo.setTel(tel);
				subject.getSession().setAttribute(PortalConstants.USER_INFO_CONSTANTS, accessInfo);
				request.getSession().setAttribute(PortalConstants.USER_INFO_CONSTANTS, accessInfo);
				HttpServletResponse response = (HttpServletResponse)resp;
				String serviceUrl = request.getParameter("service");
				if(StringUtils.isEmpty(serviceUrl)){
					serviceUrl = PortalConstants.SEND_REDIRECT;
				}
				serviceUrl = response.encodeRedirectURL(serviceUrl);
				response.sendRedirect(serviceUrl);
//				BaseServlet.write((HttpServletRequest)req, (HttpServletResponse)resp, "登录成功", accessInfo, true);
			}else if(request.getRequestURI().endsWith("logout")){
				if(subject.isAuthenticated()){
					subject.logout();
				}
				Object userinfo = request.getSession().getAttribute(PortalConstants.USER_INFO_CONSTANTS);
				if(null != userinfo){
					request.getSession().removeAttribute(PortalConstants.USER_INFO_CONSTANTS);
				}
				BaseServlet.write((HttpServletRequest)req, (HttpServletResponse)resp, "退出成功", null, true);
				
			}else{
				if(!PortalConstants.IS_DEBUG && !isAccessAllowed(req, resp, subject)){
					throw new ParameterException("权限不允许");
				}
				chain.doFilter(req, resp);
			}
		}catch(Exception e){
			BaseServlet.write((HttpServletRequest)req, (HttpServletResponse)resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest req, ServletResponse resp)
			throws Exception {
		LOGGER.info("## access filter");
		return true;
	}

}
