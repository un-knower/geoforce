package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.lbs.entity.CarDismsg;


public interface CarDisMsgService {
	/**
	 * 根据条件查询
	 * @param id
	 * @return
	 */

	public List<CarDismsg> queryCarMessage(HashMap hm);
	public Map<String,Object> queryCarMessagePage(HashMap hm);
	
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
