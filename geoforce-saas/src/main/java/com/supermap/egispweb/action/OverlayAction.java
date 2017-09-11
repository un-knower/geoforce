package com.supermap.egispweb.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 网点 zone/point.html
 * 责任区 zone/region.html
 * 地址匹配 zone/geocode.html
 * 分单管理 zone/fendan.html
 * @description 覆盖物（网点，区划）
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-9-10
 * @version 1.0
 */
@Controller
@RequestMapping("overlay")
public class OverlayAction {
	
	@RequestMapping("point/show")
	public String showPoint(String orderItemId,boolean isLogined,HttpServletRequest request){
		request.setAttribute("orderItemId", orderItemId);
		request.setAttribute("isLogined", isLogined);
		return "point";
	}
	@RequestMapping("region/show")
	public String showArea(String orderItemId,boolean isLogined,HttpServletRequest request){
		request.setAttribute("orderItemId", orderItemId);
		request.setAttribute("isLogined", isLogined);
		return "region";
	}
	@RequestMapping("geocode/show")
	public String showGeocode(String orderItemId,boolean isLogined,HttpServletRequest request){
		request.setAttribute("orderItemId", orderItemId);
		request.setAttribute("isLogined", isLogined);
		return "geocode";
	}
	@RequestMapping("fendan/show")
	public String showFendan(String orderItemId,boolean isLogined,HttpServletRequest request){
		request.setAttribute("orderItemId", orderItemId);
		request.setAttribute("isLogined", isLogined);
		return "fendan";
	}

	
}
