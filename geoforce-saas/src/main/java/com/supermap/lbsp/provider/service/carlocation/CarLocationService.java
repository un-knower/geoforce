package com.supermap.lbsp.provider.service.carlocation;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.hibernate.gps.CarGps;

/**
 * 
* ClassName：CarLocationService   
* 类描述：   车辆定位跟踪service
* 操作人：wangshuang   
* 操作时间：2014-9-11 下午02:02:33     
* @version 1.0
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
	public String sendGetGpsMsg(String name,String msg)throws Exception;
}
