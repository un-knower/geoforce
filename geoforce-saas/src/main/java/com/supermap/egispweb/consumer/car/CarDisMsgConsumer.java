package com.supermap.egispweb.consumer.car;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.CarDismsg;

public interface CarDisMsgConsumer {
	/**
	 * 根据条件查询
	 * @param id
	 * @return
	 */

	public List<Object[]> queryCarMessage(Page page, HashMap hm);
	public Page queryCarMessagePage(Page page, HashMap hm);
	
	/**
	 * 
	 * @param 新增
	 * @return
	 */
	public int addCarMessage(CarDismsg  carMessage);

	/**
	 * 根据ID获取
	 * @param id
	 * @return
	 */

	public CarDismsg getCarMessage(String id);
	
	
	/**
	 * 
	 * @param 修改
	 * @return
	 */
	public int updateCarMessage(CarDismsg  carDismsg);
	/**
	 * 
	 * @param 删除
	 * @return
	 */
	public int delCarMessage(CarDismsg carDismsg);
}
