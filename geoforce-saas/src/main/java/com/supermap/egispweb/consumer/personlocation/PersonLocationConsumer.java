package com.supermap.egispweb.consumer.personlocation;

import com.supermap.egispweb.common.AjaxResult;

/**
 * 
* ClassName：PersonLocationConsumer   
* 类描述：   人员位置监控
* 操作人：wangshuang   
* 操作时间：2014-11-27 下午05:36:44     
* @version 1.0
 */
public interface PersonLocationConsumer {

	/**
	 * 
	* 方法名: personLocation
	* 描述:根据id获取人员位置
	* @param personIds
	* @return
	 */
	public AjaxResult personLocation(String personIds);
}
