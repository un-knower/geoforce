package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.DimOrderStatusEntity;
import com.supermap.egispservice.base.entity.OrderBaseEntity;

public interface ILogisticsOrderService {

	/**
	 * 
	 * <p>Title ：saveOrderInfos</p>
	 * Description：		保存订单基础信息
	 * @param orderBaseInfos
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-23 下午04:11:06
	 */
	public List<String> saveOrderInfos(List<OrderBaseEntity> orderBaseInfos);
	
	/**
	 * 
	 * <p>Title ：update</p>
	 * Description：		更新订单信息
	 * @param obes
	 * Author：Huasong Huang
	 * CreateTime：2014-9-24 下午03:47:57
	 */
	public void update(List<OrderBaseEntity> obes);
	/**
	 * 
	 * <p>Title ：queryByBatch</p>
	 * Description：		
	 * @param userId
	 * @param enterpriseId
	 * @param departmentId
	 * @param batch
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-5 上午10:23:19
	 */
	public Map<String,Object> queryByBatch(final String userId,String enterpriseId,String dcode,final String batch,int pageNo,int pageSize);
	/**
	 * 
	 * <p>Title ：queryByBatch4API</p>
	 * Description：		为API提供的查询
	 * @param userId
	 * @param enterpriseId
	 * @param departmentId
	 * @param batch
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-6-11 上午10:28:32
	 */
	public Map<String,Object> queryByBatch4API(final String userId,String enterpriseId,String dcode,final String batch,int pageNo,int pageSize);
	/**
	 * 
	 * <p>Title ：updateOrderStatus</p>
	 * Description：		根据订单ID列表，更新订单状态为已，规划
	 * @param ids
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-28 下午02:47:22
	 */
	public boolean updateOrderStatus(List<String> ids);
	
	/**
	 * 
	 * <p>Title ：queryById</p>
	 * Description：		
	 * @param id
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-5 上午10:24:46
	 */
	public Map<String,Object> queryById(String id,String userId);
	
	public List<Map<String,Object>> queryByIds(String[] ids);
	
	
	/**
	 * 查询某企业下所有符合条件的批次
	 * @param eid
	 * @param userids
	 * @param batch
	 * @return
	 * @Author Juannyoh
	 * 2016-9-22下午2:24:07
	 */
	public List<String> queryBatchsByParams(String eid, List<String> userids,String batch);
	
	/**
	 * 按条件导出分单数据
	 * @param userId
	 * @param enterpriseId
	 * @param batch
	 * @param status
	 * @param admincode
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @Author Juannyoh
	 * 2016-9-22下午3:19:13
	 */
	public Map<String,Object> queryExportByBatch(final List<String> userId,String enterpriseId,final List<String> batch,String status,String admincode,int pageNo,int pageSize);

	/**
	 * 查询条件下的订单数量
	 * @param eid
	 * @param userids
	 * @param batch
	 * @param status
	 * @param admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-9-22下午3:20:48
	 */
	public int queryExportCountByParams(String eid,List<String> userids,List<String> batch,String status,String admincode);
	
	/**
	 * 删除条件下的订单及分单结果
	 * @param eid
	 * @param userids
	 * @param batch
	 * @param status
	 * @param admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-9-22下午3:21:01
	 */
	public int deleteOrdersByParams(String eid,List<String> userids,List<String> batch,String status,String admincode);

	public List<DimOrderStatusEntity> queryAllOrderStatus();
}
