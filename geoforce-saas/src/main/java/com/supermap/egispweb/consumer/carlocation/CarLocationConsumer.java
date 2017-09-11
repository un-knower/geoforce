package com.supermap.egispweb.consumer.carlocation;

import com.supermap.egispweb.common.AjaxResult;



public interface CarLocationConsumer {
	/**
	 * 
	* 方法名: carLocation
	* 描述:定位跟踪
	* @return
	 */
	public AjaxResult carLocation(String carIds);
}
