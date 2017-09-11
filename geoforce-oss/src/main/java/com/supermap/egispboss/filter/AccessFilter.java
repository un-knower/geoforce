package com.supermap.egispboss.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.supermap.egispboss.constants.EgispBossConstants;
import com.supermap.egispboss.servlet.BaseServlet;
import com.supermap.egispboss.shiro.StaffPermission;
import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.StaffFieldNames;

public class AccessFilter extends AccessControlFilter {
	private WebApplicationContext context = null;

	@SuppressWarnings("unused")
	private void init() {
		ServletContext servletContext = this.getServletContext();
		context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		org.apache.shiro.mgt.SecurityManager securtyManager = (SecurityManager) context.getBean("securityManager");
		SecurityUtils.setSecurityManager(securtyManager);

	}

	private static Logger LOGGER = Logger.getLogger(AccessFilter.class);

	@Override
	protected boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object obj) throws Exception {
		String method = req.getParameter("method");
		LOGGER.info("## access filter");
		HttpServletRequest request = (HttpServletRequest) req;
		String requestUri = request.getRequestURI();
		String requestURL = request.getRequestURL().toString();
		if (!CommonUtil.isStringEmpty(method)) {
			requestUri = requestUri + "?method=" + method;
		}
		LOGGER.info("requestURI : " + requestUri);
		LOGGER.info("requestUrl : " + requestURL);
		Subject subject = (Subject) obj;
		subject.checkPermission(new StaffPermission("", requestUri));
		return true;
	}

	@Override
	public void doFilterInternal(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws ServletException, IOException {
		LOGGER.info("## 访问过滤器");
		String method = req.getParameter("method");
		try{
			Subject subject = SecurityUtils.getSubject();
			if(StringUtils.isNotEmpty(method)){
				if(method.equals("login")){
					UsernamePasswordToken token = new UsernamePasswordToken();
					String username = req.getParameter(StaffFieldNames.USERNAME);
					String password = req.getParameter(StaffFieldNames.PASSWORD);
					token.setUsername(username);
					token.setPassword(password.toCharArray());
					subject.login(token);
					BaseServlet.write((HttpServletRequest)req, (HttpServletResponse)resp, "登录成功", null, true);
				}else{
					if(!EgispBossConstants.IS_DEBUG && !isAccessAllowed(req, resp, subject)){
						throw new ParameterException("权限不允许");
					}
					chain.doFilter(req, resp);
				}
			}else{
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
