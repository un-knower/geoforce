package com.supermap.egispservice.pathplan.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.RouteTaskEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.entity.WeightNameType;

public interface IRouteTaskService {

	public abstract void save(RouteTaskEntity entity);

	public abstract List<RouteTaskEntity> findAll();

	public abstract RouteTaskEntity findById(String id);

	public abstract void update(RouteTaskEntity entity);

	public abstract boolean deleteById(String id) throws Exception;

	Map<String, Object> getTasks(String userid, String eid, String deptId, String taskId, Byte taskStatusId, String areaName, int pageNumber, int pageSize,
			String sortType);

	/**
	 * 
	 * <p>Title ：saveJobAndRun</p>
	 * Description：		保存线路规划工作，并运行
	 * @param netId		网点ID
	 * @param netCoord	网点坐标
	 * @param orderIds	订单Id列表，使用逗号分隔
	 * @param orderCoords	订单坐标，使用分号分隔，每对坐标第一个为经度，第二个为纬度，
	 * 						示例：121.43870568995496,31.21282983470203,121.47985517750072,31.296681193569263
	 * @param orderTimes	订单是配送时间列表，所有时间使用逗号相隔，第一个为开始时间，第二个为结束时间，
	 * 						示例：8:00,9:00,8:00,9:00
	 * @param areaId		区划ID
	 * @param areaName 		区划名称
	 * @param user			使用者信息
	 * @param taskName		任务名称
	 * @param carLoad		车辆负载量
	 * @param fixedConsumeMiniute	固定消耗时间
	 * @param pathType		路线类型
	 * @param anlystType	分析类型
	 * @param carType		车辆通行类型
	 * @Param batchTimeStart 批量开始时间
	 * @param batchTimeEnd 批量结束时间
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-16 下午03:22:42
	 */
	public Map<String, Object> saveJobAndRun(String netId, String netCoord, String orderIds, String orderCoords,
			String orderTimes, String areaId, String areaName, UserEntity user, String taskName, int carLoad,
			double fixedConsumeMiniute, int pathType, int anlystType, int carType,String batchTimeStart,String batchTimeEnd);

	Map<String, Object> getNetsAndOrders(String taskId) throws IOException;

	Map<String, Object> saveJobAndRun(String carsProperties, String netcoord, String ordersCoords, String areaName,
			String areaId, String ordersIds, int ordersCount, String netid, Date jobRunTime, Date jobstarttime,
			Date jobendtime, UserEntity user, String taskName, int typeAnalysis, int typeTrac, int typePath);

	public Map<String, Object> getResult(String taskId,String areaId,String deptId) throws Exception;

	public Map<String,Object> getNetsAndOrdersObj(String taskId);
	

}