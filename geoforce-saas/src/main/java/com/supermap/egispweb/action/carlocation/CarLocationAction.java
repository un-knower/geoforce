package com.supermap.egispweb.action.carlocation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class CarLocationAction extends BaseAction{
	
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
		
		return carLocationConsumer.carLocation(carIds);
	}
}
