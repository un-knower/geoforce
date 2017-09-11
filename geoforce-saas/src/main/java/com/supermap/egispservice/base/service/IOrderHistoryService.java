package com.supermap.egispservice.base.service;

import java.util.Map;

public interface IOrderHistoryService {

	/**
	 * 获取历史订单
	 * 
	 * @param number
	 *            订单编号
	 * @param batch
	 *            订单批次
	 * @param admincode
	 *            行政区划编码
	 * @param address
	 *            地址
	 * @param deptId
	 *            用户所在部门ID
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页显示数量
	 * @param sortType
	 *            ID排序方式
	 * @return
	 */
	public Map<String, Object> getHistoryOrders(final String number, final String batch,
			final String admincode, String address, String deptId, int pageNumber,
			int pageSize, String sortType);
}
