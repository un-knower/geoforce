package com.supermap.egispservice.reverse.utils;

import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;

/**
 * 
  * @ClassName: CoorUtils
  * @Description: 
  *   经纬度之间的距离计算
  * @author huanghuasong
  * @date 2016-2-22 上午9:40:58
  *
 */
public class CoorUtils {

	public static double getDistanceByGPS(double LonA, double LatA, double LonB, double LatB)  
	{  
	    // 东西经，南北纬处理，只在国内可以不处理(假设都是北半球，南半球只有澳洲具有应用意义)  
	    double MLonA = LonA;  
	    double MLonB = LonB;  
	    // 地球半径（千米）  
	    double R = 6371.004;  
//	    double R = 3395;
	    double C = Math.sin(rad(LatA)) * Math.sin(rad(LatB)) + Math.cos(rad(LatA)) * Math.cos(rad(LatB)) * Math.cos(rad(MLonA - MLonB));  
	    return (R * Math.acos(C)) * 1000;  
	}  
	  
	private static double rad(double d)  
	{  
	    return d * Math.PI / 180.0;  
	}  
	
	
	
	/**
	 * 
	  * @Title: getDistance
	  * @Description: 
	  *      获取距离
	  * @param LonA
	  * @param LatA
	  * @param LonB
	  * @param LatB
	  * @return       
	  * @author huanghuasong
	  * @date 2016-2-22 上午11:02:50
	 */
	public static double getDistance(double LonA, double LatA, double LonB, double LatB){
		com.supermap.entity.Point p1 = new com.supermap.entity.Point(LonA,LatA);
		com.supermap.entity.Point p2 = new com.supermap.entity.Point(LonB,LatB);
		p1 = SuperMapCoordinateConvertImpl.smLL2MC(p1);
		p2 = SuperMapCoordinateConvertImpl.smLL2MC(p2);
		return Math.sqrt(Math.pow(p1.getLon() - p2.getLon(), 2)+Math.pow(p1.getLat() - p2.getLat(), 2));
	}
	
}
