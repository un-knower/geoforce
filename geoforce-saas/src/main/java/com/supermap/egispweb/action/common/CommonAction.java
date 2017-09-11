package com.supermap.egispweb.action.common;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;
/**
 * 
* ClassName：CommonAction   
* 类描述：   公共action
* 操作人：wangshuang   
* 操作时间：2014-9-29 下午05:45:06     
* @version 1.0
 */
//@Controller
//@RequestMapping("common")
public class CommonAction extends BaseAction{

	/**
	 * 
	* 方法名: alarmRemind
	* 描述: 车辆监控页面 报警提醒
	* @return
	 */
	@RequestMapping("alarmRemind")
	@ResponseBody
	public AjaxResult alarmRemind(HttpSession session){
		UserEntity user = getUserSession(session);
		String deptCode = user.getDeptId().getCode();
		short status = 0;//未处理的报警信息
		return alarmConsumer.queryAlarmRemind(deptCode, status);
	}
}
