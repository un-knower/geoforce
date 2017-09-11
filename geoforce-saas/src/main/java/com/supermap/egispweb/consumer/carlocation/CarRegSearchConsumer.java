package com.supermap.egispweb.consumer.carlocation;

import com.supermap.egispweb.common.AjaxResult;

/**
 * 
* ClassName：CarRegSearchConsumer   
* 类描述：   区域查车
* 操作人：wangshuang   
* 操作时间：2014-9-26 下午05:30:29     
* @version 1.0
 */
public interface CarRegSearchConsumer {

	/**
	 * 
	* 方法名: carRegSearch
	* 描述: 区域查车
	* @param lngLats
	* @return
	 */
	public AjaxResult carRegSearch(String lngLats,String mapType,String deptCode);
}
