package com.dituhui.openapi.portal.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dituhui.openapi.base.service.IPortalUserService;
import com.dituhui.openapi.entity.PortalUser;
import com.dituhui.openapi.portal.shiro.PortalUserRealm;

@RestController
@RequestMapping("/pUser")
public class PortalUserController {

	/*@Reference
	private ISmsService smsService;*/

	@Reference
	private IPortalUserService portalUserService;
	
	@Autowired
	private PortalUserRealm portalUserRealm;
	
	/**
	 * qiuchao
	 * 2017.03.30
	 * 由于PortalUserRealm在父容器，PortalUserService在子容器，且无法调整，PortalUserRealm无法通过依赖注入的方式得到PortalUserService，所以手动设置
	 */
	@PostConstruct
	public void setPortalUserService4PortalUserRealm() {
		portalUserRealm.setPortalUserService(portalUserService);
	}

	/*@RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
	public Map<String, Object> getCaptcha(String phone) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		String captcha = smsService.getCaptcha(phone);
		System.out.println(captcha);
		result.put("success", true);
		return result;
	}*/

	@RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
	public Map<String, Object> getCurrentUser() {
		try {
			PortalUser pUser = (PortalUser)SecurityUtils.getSubject().getPrincipal();
			if(pUser == null) {
				return buildResponseMap(true, 200, null, null);
			} else {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("total", 1);
				result.put("data", pUser);
				return buildResponseMap(true, 200, null, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Map<String, Object> register(PortalUser pUser, String captcha) {
		try {
			pUser.setAvatarId("test");
			pUser.setCreateDate(new Date());
			pUser.setNickname("test");
			portalUserService.save(pUser);
			return buildResponseMap(true, 200, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}
	
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public Map<String, Object> doLogin(PortalUser pUser) {
		try {
			UsernamePasswordToken upt = new UsernamePasswordToken(pUser.getPhone(), pUser.getPassword());
			Subject subject = SecurityUtils.getSubject();
			subject.login(upt);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", 1);
			result.put("data", (PortalUser)subject.getPrincipal());
			return buildResponseMap(true, 200, null, result);
		} catch (UnknownAccountException e) {
			return buildResponseMap(true, 401, "用户名或密码错误", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}
	
	@RequestMapping(value = "/doLogout", method = RequestMethod.GET)
	public Map<String, Object> doLogout() {
		try {
			SecurityUtils.getSubject().logout();
			return buildResponseMap(true, 200, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map<String, Object> update(PortalUser pUser) {
		try {
			portalUserService.update(pUser);
			return buildResponseMap(true, 200, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}
	
	private Map<String, Object> buildResponseMap(boolean success, int status,
			String info, Map<String, Object> result) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", success);
		map.put("status", status);
		map.put("info", info);
		map.put("result", result);
		return map;
	}
}
