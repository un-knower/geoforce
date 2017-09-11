package com.supermap.egispservice.lbs.dao;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mongodb.MongoException;
import com.supermap.egispservice.lbs.pojo.CarGps;
import com.supermap.egispservice.lbs.util.Pagination;


/**
 * 车辆GPS数据
 * @author Administrator
 *
 */
public interface CarGpsDao {

	/**
	 * 
	* 方法名: queryCarCurrentGps
	* 描述: 获取车辆当前GPS信息
	* @return
	* @throws Exception
	 */
	public List<CarGps> queryCarCurrentGps(HashMap<String, Object> hm) throws Exception;
	
	/**
	 * 
	* 方法名: saveCarCurrentGps
	* 描述: 添加或修改车辆当前轨迹
	* @param carGps
	* @return
	* @throws Exception
	 */
	public int saveCarCurrentGps(CarGps carGps) throws Exception;
	/**
	 * 
	* 方法名: queryCarHistoryGps
	* 描述: 查询车辆历史轨迹
	* @param hm
	* @param page
	* @return
	* @throws Exception
	 */
	public List<CarGps> queryCarHistoryGps(HashMap<String, Object> hm,Pagination page) throws Exception;
	/**
	 * 获取一辆车在某一时间段内的行驶里程
	 * 包括 第一条GPS数据和最后一条GPS数据
	* @Title: getCarHistoryByTime
	* @param carId
	* @param startTime
	* @param endTime
	* @return
	* Double
	* @throws
	 */
	public List<CarGps> getCarHistoryByTime(String carId,Date startTime,Date endTime);
	/**
	 * 
	* 方法名: saveCarHistoryGps
	* 描述: 新增车辆历史轨迹
	* @param carGps
	* @return
	* @throws Exception
	 */
	public int saveCarHistoryGps(CarGps carGps) throws Exception;
	/**
	 * 根据经纬度区域边界查询区域内的车辆
	* @Title: queryCarCurrentGpsByBound
	* @param deptcode
	* @param minLon
	* @param minLat
	* @param maxLon
	* @param maxLat
	* @return
	* @throws UnknownHostException
	* @throws MongoException
	* List<CarCurrentGps>
	* @throws
	 */
	public List<CarGps> queryCarCurrentGpsByBound(String deptCode, double minLon, double minLat, double maxLon, double maxLat)throws UnknownHostException, MongoException;
}
