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


	Map<String, Object> getNetsAndOrders(String taskId) throws IOException;

	Map<String, Object>  saveJobAndRun(String carsProperties,String netcoord, String ordersCoords, String areaName, String areaId, String ordersIds, int ordersCount, String netid, Date jobRunTime,
			Date jobstarttime, Date jobendtime, UserEntity user,String taskName,int typeAnalysis,int typeTrac,int typePath );

	public Map<String, Object> saveJobAndRun(String netId, String netCoord, String orderIds, String orderCoords,
			String orderTimes, String areaId, String areaName, UserEntity user, String taskName, int carLoad,
			double fixedConsumeMiniute, int pathType, int anlystType, int carType,String batchTimeStart,String batchTimeEnd);
	
	public Map<String, Object> getResult(String taskId,String areaId,String deptId) throws Exception;

	

}