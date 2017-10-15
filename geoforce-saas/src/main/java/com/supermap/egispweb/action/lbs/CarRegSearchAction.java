package com.supermap.egispweb.action.lbs;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.lbs.pojo.CarGps;
import com.supermap.egispservice.lbs.service.CarRegSearchService;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;

/**
 * 
* ClassName：CarRegSearchAction   
* 类描述：   区域查车
* 操作人：wangshuang   
* 操作时间：2014-9-26 下午05:41:55     
* @version 1.0
 */
//@Controller
//@RequestMapping("carMonitor")
public class CarRegSearchAction {
	
	@Autowired
	CarRegSearchService carRegSearchService;
	
	private final static Logger logger = Logger.getLogger(CarRegSearchAction.class);

	@RequestMapping(value = "regSearch", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult carRegSearch(HttpSession session,String lngLats,String mapType){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String deptCode =  userEntity.getDeptId().getCode();
		return this.getcarRegSearch(lngLats, mapType,deptCode);
	}
	
	
	public AjaxResult getcarRegSearch(String lngLats,String mapType,
			String deptCode) {
		
		if(StringUtils.isBlank(lngLats)){
			return new AjaxResult((short)0, "无效的区域");
		}
		if(StringUtils.isBlank(mapType)){
			mapType = "supermap";
		}
		//区域范围：两个经纬度之间","分隔，经度和纬度之间","分隔
		String[] regionArray = lngLats.split(";");
		if (regionArray.length < 3){
			return new AjaxResult((short)0, "无效的区域");
		}
		try {
			List<CarGps> list = this.carRegSearchService.queryCarsByRegion(lngLats,mapType,deptCode);
			if(list == null || list.isEmpty()){
				return new AjaxResult((short)0,"区域内无车辆");
			}
			return new AjaxResult((short)1,list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new AjaxResult((short)0,"区域内无车辆");
		}
	}
}
