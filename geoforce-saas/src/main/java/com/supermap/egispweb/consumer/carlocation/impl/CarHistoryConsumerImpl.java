package com.supermap.egispweb.consumer.carlocation.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.consumer.carlocation.CarHistoryConsumer;
import com.supermap.egispweb.util.DateUtil;
import com.supermap.lbsp.provider.bean.GpsHistoryReport;
import com.supermap.lbsp.provider.bean.carlocation.CarHistoryGps;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.service.carlocation.CarHistoryService;

@Component("carHistoryConsumer")
public class CarHistoryConsumerImpl implements CarHistoryConsumer{
	static Logger logger = Logger.getLogger(CarHistoryConsumerImpl.class.getName());
	
	@Reference(version="2.5.3")
	private CarHistoryService carHistoryService;
	@Override
	public Page getMileReportList(Page page, HashMap hm) {
		
		return carHistoryService.getMileReportList(page, hm);
	}
	@Override
	public Page getCarHistoryGpsList(String carId, Date startTime,
			Date endTime, Page page) {
		
		return carHistoryService.getCarHistoryGpsList(carId, startTime, endTime, page);
	}
	
	public AjaxResult carHistory(String carId, String startDate, String endDate) {
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
	@Override
	public List<GpsHistoryReport> getCarHistoryGpsList(String carId,
			Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return carHistoryService.getCarHistoryGpsList(carId, startTime, endTime);
	}
}
