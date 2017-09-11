package com.supermap.lbsp.provider.service.carlocation;

import java.util.List;

import com.supermap.lbsp.provider.hibernate.gps.CarGps;

/**
 * 
* ClassName：CarRegSearchService   
* 类描述：   区域查询车辆
* 操作人：wangshuang   
* 操作时间：2014-9-11 下午02:03:00     
* @version 1.0
 */
public interface CarRegSearchService {

	/**
	 * 
	* 方法名: queryCarsByRegion
	* 描述: 按区域查询车辆
	* @param lngLats 经纬度串 格式 lng,lat;lng,lat
	* @param mapType
	* @return
	* @throws Exception
	 */
	public List<CarGps> queryCarsByRegion(String lngLats,String mapType,
			String deptCode) throws Exception;
}
