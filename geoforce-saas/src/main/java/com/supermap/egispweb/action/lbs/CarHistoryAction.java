package com.supermap.egispweb.action.lbs;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.lbs.pojo.CarHistoryGps;
import com.supermap.egispservice.lbs.service.CarHistoryService;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.util.DateUtil;

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
public class CarHistoryAction {
	
	@Autowired
	CarHistoryService carHistoryService;
	
	
	private final static Logger logger = Logger.getLogger(CarHistoryAction.class);
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
		return this.getcarHistory(carId, startDate, endDate);
	}
	
	
	public AjaxResult getcarHistory(String carId, String startDate, String endDate) {
		if(StringUtils.isBlank(carId)){
			return new AjaxResult((short)0, "车辆不存在");
		}
		Date sDate,eDate;
		String day = DateUtil.format(new Date(), "yyyy-MM-dd");
		if(StringUtils.isBlank(startDate)){
			sDate = DateUtil.formatStringByDate(day+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
		}else {
			sDate = DateUtil.formatStringByDate(startDate, "yyyy-MM-dd HH:mm:ss");
		}
		if(StringUtils.isBlank(endDate)){
			eDate = DateUtil.formatStringByDate(day+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
		}else {
			eDate = DateUtil.formatStringByDate(endDate, "yyyy-MM-dd HH:mm:ss");
		}
		
		long diffTime = DateUtil.diffDates(eDate, sDate);
		if(diffTime < 0 || diffTime > 24*60*60*1000){//目前只支持查询24小时内数据
			
			return new AjaxResult((short)0,"一次最多能查询24小时内轨迹数据");
		}
		try {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("carId", carId);
			hm.put("startDate", sDate);
			hm.put("endDate", eDate);
			hm.put("isRuning", true);
			CarHistoryGps carHistoryGps = carHistoryService.carHistory(hm, null);
			if(carHistoryGps == null){
				return new AjaxResult((short)0,"无历史轨迹数据");
			}
			return new AjaxResult((short)1, carHistoryGps);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new AjaxResult((short)0,"无历史轨迹数据");
		}
	}
}
