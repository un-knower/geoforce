package com.supermap.egispweb.action.lbs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.lbs.pojo.CarGps;
import com.supermap.egispservice.lbs.service.CarLocationService;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;

/**
 * 
* ClassName：CarLocationConsumer   
* 类描述：   车辆监控-定位跟踪
* 操作人：wangshuang   
* 操作时间：2014-9-18 下午05:34:54     
* @version 1.0
 */
//@Controller
//@RequestMapping("carMonitor")
public class CarLocationAction {
	
	@Autowired
	CarLocationService carLocationService;
	
	private final static Logger logger = Logger.getLogger(CarLocationAction.class);
	
	@RequestMapping("index")
	public String init(){
		return "monitor/car/carMonitor";
	}
	/**
	 * 
	* 方法名: carLocation
	* 描述:定位跟踪方法
	* @param carIds
	* @return
	 */
	@RequestMapping(value = "locate",method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult carLocation(@RequestParam("carIds") String carIds){
		return this.getcarLocation(carIds);
	}
	
	
	public AjaxResult getcarLocation(String carIds) {
		if(StringUtils.isBlank(carIds)){
			return new AjaxResult((short)0, "未找到对应车辆");
		}
		String[] carArray = carIds.split(",");
		List<String> carList = Arrays.asList(carArray);
		try {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("carIds", carList);
			List<CarGps> list = carLocationService.carLocation(hm);
			if(list == null || list.isEmpty()){
				return new AjaxResult((short)0, "未找到对应车辆");
			}
			return new AjaxResult((short)1,list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new AjaxResult((short)0, "定位失败");
	}
}
