package com.supermap.egispservice.base.service;

import com.supermap.egispservice.base.entity.OrderEntity;
import com.supermap.egispservice.base.entity.OrderItemsEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BaseAccessInfo;
import com.supermap.egispservice.base.pojo.BaseOrderInfo;
import com.supermap.egispservice.base.pojo.BaseOrderInfoList;

import net.sf.json.JSONObject;


public interface IOrderService {
	
	/**
	 * 
	 * <p>Title ：add</p>
	 * Description：		添加订单
	 * @param orderEntity
	 * @param orderItems
	 * Author：Huasong Huang
	 * CreateTime：2014-8-4 下午03:25:38
	 */
	public void add(OrderEntity orderEntity,OrderItemsEntity[] orderItems);

	
	public void deleteById(String id);
	
	
	public OrderEntity queryById(String id,boolean isNeeedItems);
	
	public String add(JSONObject orderObj) throws ParameterException;
	
	/**
	 * 
	 * <p>Title ：queryOrderDetails</p>
	 * Description：查询订单详细信息
	 * @param id
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-12 下午04:48:43
	 */
	public BaseOrderInfo queryOrderDetails(String id)throws ParameterException;
	
	/**
	 * 
	 * <p>Title ：updateOrderDetails</p>
	 * Description：		修改订单详细信息
	 * @param id
	 * @param consultPrice
	 * @param remarks
	 * @param useTime
	 * @param deadline
	 * @throws ParameterException
	 * Author：Huasong Huang
	 * CreateTime：2014-8-13 上午10:26:44
	 */
	public void updateOrderDetails(String id, String consultPrice, String remarks)
			throws ParameterException;
	
	public void updateOrderItem(String id,String useLimitStr,String consultPriceStr,String startUseTime,String deadline) throws ParameterException;
	/**
	 * 
	 * <p>Title ：delelteOrderItem</p>
	 * Description：删除订单项
	 * @param id
	 * @throws ParameterException
	 * Author：Huasong Huang
	 * CreateTime：2014-8-13 下午02:17:56
	 */
	public void delelteOrderItem(String id) throws ParameterException;
	
	public void auditOrder(String id,String statusStr,String auditRemarks) throws ParameterException;
	
	public BaseOrderInfoList queryOrderList(String id,String info,String status,int pageNo,int pageSize) throws ParameterException;
	
	
	/**
	 * portal-----
	 */
	public String add(JSONObject orderObj,BaseAccessInfo userInfo)throws ParameterException;
	
	public BaseOrderInfoList queryOrderList(BaseAccessInfo userInfo,String statusStr,int orderType,int pageSize,int pageNo)throws ParameterException;

}
