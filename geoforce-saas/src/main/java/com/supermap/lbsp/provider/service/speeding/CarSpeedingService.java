package com.supermap.lbsp.provider.service.speeding;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.SpeedingSet;

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
	 * 
	 * @return
	 */
	public List<SpeedingSet> queryCarSpeeding(Page page,
			HashMap hm);
	
	/**
	 * 删除速度报警信息
	 * 
	 * @param CarSpeeding
	 * @return
	 */
	public SpeedingSet getCarSpeeding(String  id);

	public SpeedingSet getCarSpeedingBySpeed(String speed);

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
	
	public Page pageCarSpeeding(Page page,HashMap<String,Object> hm);

}
