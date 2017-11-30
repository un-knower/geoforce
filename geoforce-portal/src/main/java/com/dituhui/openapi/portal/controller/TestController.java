package com.dituhui.openapi.portal.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dituhui.openapi.base.service.IPortalUserService;

@Controller
@RequestMapping("/aa")
public class TestController {
	
	@Reference
	private IPortalUserService portalUserService;

	@RequestMapping(value="/test")
	public void test(HttpServletRequest request) {
		System.out.println("============father=============");
		String[] names2 = ContextLoader.getCurrentWebApplicationContext().getBeanDefinitionNames();
		for(String n:names2) {
			System.out.println(n);
		}
		System.out.println("============son=============");
		ServletContext servletContext = request.getSession().getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext,"org.springframework.web.servlet.FrameworkServlet.CONTEXT.springMvc" );
		String[] names = webApplicationContext.getBeanDefinitionNames();
		for(String n:names) {
			System.out.println(n);
		}
		System.out.println("============test in son controller=============");
		System.out.println(ContextLoader.getCurrentWebApplicationContext().getBean("portalUserRealm"));
		System.out.println(webApplicationContext.getBean("portalUserRealm"));
		System.out.println(portalUserService);
		System.out.println(portalUserService.queryCount());
	}
}
