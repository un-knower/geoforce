package com.supermap.egispapi.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.supermap.egispapi.constants.CodeConstants;
import com.supermap.egispapi.constants.TemplateNames;
import com.supermap.egispapi.pojo.ResponseCode;

@Controller
@RequestMapping("/error")
public class ErrorServiceAction {

	
	@RequestMapping("/validation")
	@ResponseBody
	public ModelAndView doError(HttpServletRequest request,HttpServletResponse resp){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(TemplateNames.JSON_VIEW_NAME);
		if(request.getAttribute("validation") != null){
			ResponseCode rc = (ResponseCode) request.getAttribute("validation");
			mav.addObject(TemplateNames.FINAL_INFO_NAME,rc.getInfo());
			mav.addObject(TemplateNames.FINAL_CODE_NAME,rc.getCode());
		}else{
			mav.addObject(TemplateNames.FINAL_INFO_NAME, "未找到可显示的异常信息");
			mav.addObject(TemplateNames.FINAL_CODE_NAME,CodeConstants.SERVER_EXCEPTION);
		}
		return mav;
	}
	
}
