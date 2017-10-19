package com.chaosting.geoforce.saas.controller;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;
//import com.chaosting.geoforce.entity.Application;
import com.chaosting.geoforce.saas.controller.util.JedisUtil;
import com.chaosting.geoforce.saas.interceptor.AuthInterceptor;
//import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chaosting.geoforce.saas.shiro.AuthRealm;

import java.util.List;

@Component
public class InitInject {

	//@Reference
	//private IApplicationService applicationService;

	@Autowired
	private AuthRealm authRealm;

	@Autowired
	private JedisUtil jedisUtil;

	@Value("${saas.test.open}")
	private String open;

	/**
	 * qiuchao 2017.03.30 由于AuthRealm在父容器，ApplicationService在子容器，且无法调整，
	 * AuthRealm无法通过依赖注入的方式得到ApplicationService，所以手动注入
	 */
	/*@PostConstruct
	public void initInject() {
		authRealm.setApplicationService(applicationService);
		if (StringUtils.isBlank(open) || !open.equals(Boolean.TRUE.toString())) {
			authRealm.setJedisUtil(jedisUtil);
			loadApplications();
		}
	}*/

	/**
	 * 加载权限信息到redis
	 */
	/*private void loadApplications () {
		try {
			List<Application> applications = applicationService.findAllApplication();
			for (Application application : applications) {
				jedisUtil.set(application.getAppKey(), JSON.toJSONString(application));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
