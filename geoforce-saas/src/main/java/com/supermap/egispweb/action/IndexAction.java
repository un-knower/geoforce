package com.supermap.egispweb.action;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("index")
public class IndexAction {
	
	
	@RequestMapping("show")
	public String showIndex(){
		return "index";
	}
}
