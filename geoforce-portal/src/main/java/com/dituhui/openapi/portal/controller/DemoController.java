package com.dituhui.openapi.portal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dituhui.openapi.base.service.IDemoService;
import com.dituhui.openapi.entity.Demo;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@Reference
	private IDemoService demoService;

	@RequestMapping(value = "/save")
	public String save(HttpServletRequest request) {
		getContextInfo(request);

		Demo demo = new Demo("pwd", "real", "user");
		demo = demoService.save(demo);
		System.out.println(demo.getUserId());

		return "index";
	}
	
	@RequestMapping(value = "/get/{userId}")
	public String get(@PathVariable Integer userId, HttpServletRequest request) {
		getContextInfo(request);

		Demo demo = demoService.findByUserId(userId);
		System.out.println(demo.getUserId()+"\t"+
				demo.getUsername()+"\t"+
				demo.getRealname()+"\t"+
				demo.getPassword());

		return "index";
	}

	@RequestMapping(value = "/customDelete2", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> customDelete2(HttpServletRequest request, Integer userId) {
		getContextInfo(request);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		map.put("count", 0);
		try {
			int count = demoService.customDelete2(userId);
			map.put("success", true);
			map.put("count", count);
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}

	private void getContextInfo(HttpServletRequest request) {
		ServletContext servletContext = request.getSession()
				.getServletContext();
		WebApplicationContext springContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		WebApplicationContext mvcContext = (WebApplicationContext) servletContext
				.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.springMvc");
		System.out.println("======springContext=========");
		String[] names = springContext.getBeanDefinitionNames();
		for (String name : names) {
			System.out.println(name);
		}
		System.out.println("=======mvcContext========");
		names = mvcContext.getBeanDefinitionNames();
		for (String name : names) {
			System.out.println(name);
		}
		System.out.println("===============");
	}
}
