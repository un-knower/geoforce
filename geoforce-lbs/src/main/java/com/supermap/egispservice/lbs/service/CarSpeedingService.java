package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.lbs.entity.SpeedingSet;


public interface CarSpeedingService {
	/**
	 * 增加车辆的速度报警信息
	 * 
	 * @param CarSpeeding
	 * @return
	 */
	public int addCarSpeeding(SpeedingSet carSpeeding);

	/**
	 * 修改速度报警信息
	 * 
	 * @param CarSpeeding
	 * @return
	 */
	public int updateCarSpeeding(SpeedingSet carSpeeding);

	/**
	 * 删除速度报警信息
	 * 
	 * @param CarSpeeding
	 * @return
	 */
	public int delCarSpeeding(SpeedingSet carSpeeding);

	/**
	 * 查询速度报警信息
	 * 分页 page
	 * @return
	 */
	public List<SpeedingSet> queryCarSpeeding(HashMap hm);
	
	/**
	 * 删除速度报警信息
	 * 
	 * @param CarSpeeding
	 * @return
	 */
	public SpeedingSet getCarSpeeding(String  id);

	public List<SpeedingSet> getCarSpeedingBySpeed(String speed,String eid);

	/**
	 * @param eid 
	 * 超速名称是否存在
	 * 存在返回1 否 返回0
	* @Title: hasName
	* @param name
	* @return
	* int
	* @throws
	 */
	public int hasName(String name, String eid);
	
	/**
	 * 分页查询
	 * @param page
	 * @param hm
	 * @return
	 */
	public Map<String,Object> pageCarSpeeding(HashMap<String,Object> hm);

}
