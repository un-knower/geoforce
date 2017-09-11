package com.supermap.egispservice.base.service;

import java.util.Map;

import com.supermap.egispservice.base.entity.OrderFendanEntity;

public interface ILogisticsFendanService {

	/**
	 * 
	 * <p>Title ：update</p>
	 * Description：		更新分单信息
	 * @param ofe
	 * Author：Huasong Huang
	 * CreateTime：2014-9-25 下午07:30:25
	 */
	public void update(OrderFendanEntity ofe);
	
	/**
	 * 
	 * <p>Title ：queryByBatchAndArea</p>
	 * Description：		按照批次和区域Id查询分单成功的订单
	 * @param userId
	 * @param areaId
	 * @param batch
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-27 下午03:52:39
	 */
	public Map<String, Object> queryByBatchAndArea(final String userId, final String areaId, final String batch,
			int pageNo, int pageSize);
}
