package com.supermap.lbsp.provider.service.carlocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.supermap.lbsp.provider.bean.GpsHistoryReport;
import com.supermap.lbsp.provider.bean.carlocation.CarHistoryGps;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.gps.CarGps;

/**
 * 
* ClassName：CarHistoryService   
* 类描述：   车辆历史轨迹
* 操作人：wangshuang   
* 操作时间：2014-9-11 下午01:47:00     
* @version 1.0
 */
public interface CarHistoryService {
	
	/**
	 * 里程统计
	 * @param page
	 * @param hm
	 * @return
	 */
	public Page getMileReportList(Page page,HashMap hm);
	
	
	/**
	 * 得到历史轨迹报表
	 * @param carId
	 * @param startTime
	 * @param endTime
	 * @param page
	 * @return
	 */
	public Page getCarHistoryGpsList(String carId, Date startTime, Date endTime, Page page);
	/**
	 * 历史数据报表导出
	 * @param carId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<GpsHistoryReport> getCarHistoryGpsList(String carId, Date startTime, Date endTime);
	/**
	 * 
	* 方法名: carHistory
	* 描述: 车辆历史轨迹功能
	* @param hm
	* @param page
	* @return
	* @throws Exception
	 */
	public CarHistoryGps carHistory(HashMap<String, Object> hm, Page page) throws Exception; 
}
