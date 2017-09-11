package com.supermap.egispweb.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.supermap.egispweb.util.Config;

@Controller
public class LoginAction {
	
	@Autowired
	private Config config;
	
	/**
	 * 
	 * <p>Title ：login</p>
	 * Description：		登录页面
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-4-16 下午04:09:33
	 */
	@RequestMapping("login")
	public String login(){
		if(config.isDeployOffline()){
			return "signin";
		}else{
			return "redirect:/welcome/show";
		}
	}
	
	/**
	 * 直接登录页面
	 * @return
	 * @Author Juannyoh
	 * 2016-4-13上午11:30:55
	 */
	@RequestMapping("loginIn")
	public String loginIn(){
		return "login";
	}

}
