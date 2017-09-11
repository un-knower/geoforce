package com.supermap.egispservice.reverse.utils;

import com.supermap.entity.Point;
import com.utils.CoordinatesConvert;

public class ConvertCoordTool {

	public static com.utils.Point adjustedGPS2RealGPS(com.utils.Point adjustedGPSPoint){
		// 用二分法反算
		com.utils.Point tempPoint = CoordinatesConvert.gps2LL(adjustedGPSPoint);
		double realGPSLon = adjustedGPSPoint.getLon() + (adjustedGPSPoint.getLon() - tempPoint.getLon());
		double realGPSLat = adjustedGPSPoint.getLat() + (adjustedGPSPoint.getLat() - tempPoint.getLat());
		com.utils.Point realGPSPoint = new com.utils.Point(realGPSLon, realGPSLat);
		return realGPSPoint;
	}

	// 真实经纬度转百度经纬度
	public static Point realGPSPoint2BaiduGPS(Point realGPSPoint){
		String[] arr = com.supermap.convert.impl.BaiduCoordinateConvertImpl.bdWebAPI(realGPSPoint, 1, 5);
		double x = Double.parseDouble(arr[0]);
		double y = Double.parseDouble(arr[1]);
		Point baiduGPSPoint = new Point(x, y);
		return baiduGPSPoint;
	}

	public static Point baiduGPSPoint2RealGPS(Point baiduGPSPoint){
		// 用二分法反算
		String[] arr = com.supermap.convert.impl.BaiduCoordinateConvertImpl.bdWebAPI(baiduGPSPoint, 1, 5);
		double x = Double.parseDouble(arr[0]);
		double y = Double.parseDouble(arr[1]);
		Point tempGPSPoint = new Point(x, y);
		double realGPSLon = baiduGPSPoint.getLon() + (baiduGPSPoint.getLon() - tempGPSPoint.getLon());
		double realGPSLat = baiduGPSPoint.getLat() + (baiduGPSPoint.getLat() - tempGPSPoint.getLat());
		Point realGPSPoint = new Point(realGPSLon, realGPSLat);
		return realGPSPoint;
	}
}
