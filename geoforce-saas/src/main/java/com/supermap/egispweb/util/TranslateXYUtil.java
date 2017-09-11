package com.supermap.egispweb.util;

/**
 * GPS坐标（WGS84）超图坐标(GCJ-02) 百度坐标（DB09）等坐标转换算法
* @ClassName: TranslateXYUtil
* @author WangShuang
* @date 2013-9-24 下午01:47:53
 */
public class TranslateXYUtil {
	private static double bd_x_pi = 3.14159265358979324 * 3000.0 / 180.0;//GCJ02转百度的偏移系数
	
	/**
	 * GCJ02坐标转百度坐标
	* @Title: bdEncrypt
	* @param gcjx
	* @param gcjy
	* @return
	* Double[]
	* @throws
	 */
	public static Double[] bdEncrypt(double gcjx,double gcjy){
	    double z = Math.sqrt(gcjx * gcjx + gcjy * gcjy) + 0.00002 * Math.sin(gcjy * bd_x_pi);  
	    double theta = Math.atan2(gcjy, gcjx) + 0.000003 * Math.cos(gcjx * bd_x_pi);  
	    
	    double bdx = z * Math.cos(theta) + 0.0065;  
	    double bdy = z * Math.sin(theta) + 0.006;
	    Double[] bdXYs = new Double[2];
	    bdXYs[0] = bdx;
	    bdXYs[1] = bdy;
	    return bdXYs;
	}
	/**
	 * 百度坐标转GCJ02
	* @Title: bdDecrypt
	* @param bdx
	* @param bdy
	* @return
	* Double[]
	* @throws
	 */
	public static Double[] bdDecrypt(double bdx,double bdy){
		double x = bdx - 0.0065, y = bdy - 0.006;
	    double z = Math.sqrt(bdx * bdx + bdy * bdy) - 0.00002 * Math.sin(bdy * bd_x_pi);
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * bd_x_pi);
	    double gcjx = z * Math.cos(theta);
	    double gcjy = z * Math.sin(theta);
	    Double[] gcjXYs = new Double[2];
	    gcjXYs[0] = gcjx;
	    gcjXYs[1] = gcjy;
	    return gcjXYs;
	}
	
	
}
