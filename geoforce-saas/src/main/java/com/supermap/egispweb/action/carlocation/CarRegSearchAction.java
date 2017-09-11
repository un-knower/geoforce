package com.supermap.egispweb.action.carlocation;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;

/**
 * 
* ClassName：CarRegSearchAction   
* 类描述：   区域查车
* 操作人：wangshuang   
* 操作时间：2014-9-26 下午05:41:55     
* @version 1.0
 */
//@Controller
//@RequestMapping("carMonitor")
public class CarRegSearchAction extends BaseAction{

	@RequestMapping(value = "regSearch", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult carRegSearch(HttpSession session,String lngLats,String mapType){
		
		UserEntity user = getUserSession(session);
		String deptCode =  user.getDeptId().getCode();
		return carRegSearchConsumer.carRegSearch(lngLats, mapType,deptCode);
	}
}
