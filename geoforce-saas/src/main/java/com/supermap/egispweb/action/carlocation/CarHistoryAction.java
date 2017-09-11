package com.supermap.egispweb.action.carlocation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;

/**
 * 
* ClassName：CarHistoryAction   
* 类描述：   车辆历史轨迹功能
* 操作人：wangshuang   
* 操作时间：2014-9-24 下午04:20:04     
* @version 1.0
 */
//@Controller
//@RequestMapping("carMonitor")
public class CarHistoryAction extends BaseAction{
	/**
	 * 
	* 方法名: carHistory
	* 描述:定位跟踪方法
	* @param carIds
	* @return
	 */
	@RequestMapping(value = "history",method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult carHistory(@RequestParam("carId") String carId,String startDate,String endDate){
		
		return carHistoryConsumer.carHistory(carId, startDate, endDate);
	}
}
