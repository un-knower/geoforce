package com.supermap.egispservice.base.dao;

import java.util.List;

public interface IOrderBaseDao {
	
	/**
	 * 按条件查询所有批次串
	 * @param eid
	 * @param userids
	 * @param batch
	 * @return
	 * @Author Juannyoh
	 * 2016-9-22下午3:49:40
	 */
	public List<String> getDistinctBatchsByParams(String eid,List<String> userids,String batch);
	
	/**
	 * 按条件查询订单条数
	 * @param eid
	 * @param userids
	 * @param batch
	 * @param status
	 * @param admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-9-22下午3:49:57
	 */
	public int getOrderCountsByParams(String eid,List<String> userids,List<String> batch,String status,String admincode);

	/**
	 * 按条件删除订单
	 * @param eid
	 * @param userids
	 * @param batchs
	 * @param status
	 * @param admincode 
	 * @return
	 * @Author Juannyoh
	 * 2016-9-22下午3:51:28
	 */
	public int deleteOrdersByParam(String eid, List<String> userids,
			List<String> batchs, String status, String admincode);
	
	/**
	 * 导出订单
	 * @param eid
	 * @param userids
	 * @param batch
	 * @param status
	 * @param admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-10-10下午1:53:43
	 */
	public List queryExportByBatch(String eid,List<String> userids,List<String> batch,String status,String admincode);
}
