package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

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
	
	public Map<String,Object> queryByBatch(final String userId,String enterpriseId,String departmentId,final String batch,int pageNo,int pageSize);
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
	
	public Map<String, Object> queryById(String id,String userId);
	
	/**
	 * 
	 * <p>Title ：queryByIds</p>
	 * Description：		根据id查询订单信息
	 * @param ids
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-28 上午10:09:51
	 */
	public List<Map<String,Object>> queryByIds(String[] ids);
}
