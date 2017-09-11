package com.supermap.egispweb.consumer.carlocation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.bean.GpsHistoryReport;
import com.supermap.lbsp.provider.common.Page;

/**
 * 
* ClassName：CarHistoryConsumer   
* 类描述：   车辆历史轨迹consumer
* 操作人：wangshuang   
* 操作时间：2014-9-10 下午05:35:30     
* @version 1.0
 */
public interface CarHistoryConsumer {
	
	/**
	 * 里程统计
	 */
	public Page getMileReportList(Page page,HashMap hm);
	
	
	/**
	 * 
	 *  历史轨迹查询
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
	* 描述: 车辆监控-历史轨迹查询
	* @param carId
	* @param startDate
	* @param endDate
	* @return
	 */
	public AjaxResult carHistory(String carId,String startDate,String endDate);

}
