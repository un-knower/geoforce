package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;

import com.supermap.egispservice.lbs.pojo.CarGps;



/**
 * 车辆坐标跟踪  实时
 * @author Administrator
 *
 */
public interface CarLocationService {

	/**
	 * 
	* 方法名: carLocation
	* 描述:车辆定位跟踪查询接口
	* @param hm
	* @return
	* @throws Exception
	 */
	public List<CarGps> carLocation(HashMap<String, Object> hm) throws Exception;
	
	/**
	 * 发送获得位置上传接口
	 * @param name
	 * @param msg
	 * @return
	 */
	public String sendGetGpsMsg(String name,String msg);
}
