package com.supermap.egispweb.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.MenuVO;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.DemoService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispweb.util.Config;

@Controller
@RequestMapping("welcome")
public class WelcomeAction {
	
	@Autowired
	private DemoService demoService; 
	@Autowired
	private UserService userService; 
	@Autowired
    private Config config;
	
	@RequestMapping("show")
	public String showIndex(String key,HttpSession session,HttpServletRequest request){
		// 对于离线部署，不走此逻辑
		if(!config.isDeployOffline()){
			session.removeAttribute("user");
			session.removeAttribute("uri");
			UserEntity userEntity=userService.findUserById(key);
			session.setAttribute("user",userEntity );
			session.setAttribute("isLogined",userService.isLogined(key) );
			session.setAttribute("tempLogined",userService.isTempLogined(key));//记录默认城市上线后的第一次登录
			session.setAttribute("uri",request.getRequestURI() );
			List<MenuVO> menu = userService.getMenu(userEntity.getId(), userEntity.getEid().getId(), userEntity.getDeptId().getId(), userEntity.getSourceId());
			boolean istry=true;
			if(menu.size()>0){
				MenuVO vo=menu.get(0);
				session.setAttribute("days",vo.getDays() );
				if(vo.getFuncName()==null || vo.getFuncName().equals("")){
					istry=false;
				}
			}
			session.setAttribute("istry",istry );
		}
		return "index";
	}
	
	
	@RequestMapping("logout")
	public String logout(HttpSession session){
		session.removeAttribute("user");
		if(!config.isDeployOffline()){
			session.setAttribute("portalUrl", config.getPortaltUrl());
			return "logout";
		}else{
			return "redirect:/login";
		}
	}
	@RequestMapping("show/a")
	public String showA(){
		return "a";
	}

	@RequestMapping("data/get")
	@ResponseBody
	public String sayHello(String name){
		String reply =  demoService.sayHello(name);
		System.out.println("comsumer get:"+reply);
		return reply;
	}
	
}
