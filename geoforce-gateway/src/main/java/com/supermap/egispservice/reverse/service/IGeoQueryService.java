package com.supermap.egispservice.reverse.service;

import java.util.List;

import com.supermap.egispservice.base.entity.AddressInfo;
import com.supermap.egispservice.base.entity.PointParam;



/**
 * 
  * @ClassName: IGeoQueryService
  * @Description: 
  *   基于GeoHash的查询服务  
  * @author huanghuasong
  * @date 2016-2-17 下午2:16:14
  *
 */
public interface IGeoQueryService {

	/**
	 * 
	  * @Title: queryNearestPOI
	  * @Description: 
	  *      根据经纬度查询最近的POI
	  * @param lon
	  * @param lat
	  * @return       
	  * @author huanghuasong
	  * @date 2016-2-17 下午2:31:45
	 */
	public AddressInfo queryNearestPOI(double lon, double lat);
	

	/**
	  * @Title: queryNearestPOI
	  * @Description: 
	  *      根据坐标查询最近的POI，查询范围由用户指定
	  * @param lon
	  * @param lat
	  * @param tolerance	查询范围，单位：米
	  * @return       
	  * @author huanghuasong
	  * @date 2016-2-17 下午2:32:43
	 */
	public AddressInfo queryNearestPOI(double lon,double lat,double tolerance);
	
	
	/**
	 * 
	  * @Title: batchQueryNearestPOI
	  * @Description: 批量查询最近的POI
	  *      
	  * @param points
	  * @return       
	  * @author huanghuasong
	  * @date 2016-4-13 下午2:30:59
	 */
	public List<AddressInfo> batchQueryNearestPOI(List<PointParam> points,String type);

	
}
