package com.supermap.egispservice.geocoding.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Title: IGeocodingService</p>
 * Description:	地理编码服务接口，包括地址解析及逆地址解析等
 *
 * @author Huasong Huang
 * CreateTime: 2015-1-15 下午02:39:41
 */
public interface IGeocodingService {

	
	/**
	 * 
	 * <p>Title ：searchForCounty</p>
	 * Description：通过坐标查询省市区
	 * @param smx
	 * @param smy
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-1-15 下午02:40:46
	 */
	public Map<String,String> searchForCounty(double smx,double smy);
	
	
	public List<Map<String,String>> searchCountyByAdmincode(String code);
	
	
	/**
	 * 
	 * <p>Title ：getAdminElement</p>
	 * Description：		获取行政区划元素
	 * @param provinceCode 省级别的代码：直辖市提供前两位
	 * @param level
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-6-26 下午03:04:34
	 */
	public List<Map<String,Object>> getAdminElement(String provinceCode,int level);
	
	/**
	 * 
	 * <p>Title ：getAdminGeoByCode</p>
	 * Description：		根据行政区划代码和级别获取行政空间数据
	 * @param admincode
	 * @param level
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-7-29 上午10:22:42
	 */
	public Map<String,Object> getAdminGeoByCode(String admincode,int level);
	
	/**
	 * 
	 * <p>Title ：getCountyByAdmincode</p>
	 * Description：		根据Admincode获取省市区
	 * @param admincode
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-7 下午05:09:12
	 */
	public Map<String,Object> getCountyByAdmincode(String admincode);
	
	/**
	 * 根据坐标查找省市区以及admincode
	 * @param smx
	 * @param smy
	 * @return
	 * @Author Juannyoh
	 * 2015-11-23上午11:23:49
	 */
	public Map<String,String> searchAdmincodeForCounty(double smx,double smy);
}
