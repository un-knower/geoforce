package com.chaosting.geoforce.saas.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.chaosting.geoforce.saas.bak.exception.BaseException;

//@Controller
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	@ExceptionHandler
	@ResponseBody
	public Map<String, Object> handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) {
		logger.info("BaseController handlerException");
		// logger.info(e.getMessage(), e);
		logger.info(e.getMessage());

		Map<String, Object> map = new HashMap<String, Object>();
		Integer status = 99999;
		String info = "unknow error";
		if (e instanceof BaseException) {
			logger.warn("有自定义异常");
			BaseException be = (BaseException) e;
			status = be.getStatus();
			info = be.getInfo();
		} else if (e instanceof MissingServletRequestParameterException) {
			logger.warn("有参数绑定异常");
			status = 10001;
			info = e.getMessage();
		}
		map.put("status", status);
		map.put("info", info);
		return map;
	}

	protected Map<String, Object> buildSuccessResult(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 10000);
		map.put("info", "OK");
		if (object != null) {
			map.put("result", object);
		}
		return map;
	}

	protected Map<String, Object> buildSuccessResult(String output) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 10000);
		map.put("info", "OK");
		return map;
	}

	@RequestMapping(value = "/test")
	public void test(HttpServletRequest request) {
		System.out.println("============father=============");
		String[] names2 = ContextLoader.getCurrentWebApplicationContext().getBeanDefinitionNames();
		for (String n : names2) {
			System.out.println(n);
		}
		System.out.println("============son=============");
		ServletContext servletContext = request.getSession().getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(
				servletContext, "org.springframework.web.servlet.FrameworkServlet.CONTEXT.springMvc");
		String[] names = webApplicationContext.getBeanDefinitionNames();
		for (String n : names) {
			System.out.println(n);
		}
		System.out.println("============test in son controller=============");
	}
}
