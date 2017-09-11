package com.supermap.egispweb.action.lbs;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.lbs.pojo.CarAlarm;
import com.supermap.egispservice.lbs.service.AlarmService;
import com.supermap.egispservice.lbs.util.Pagination;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.pojo.AlarmRemind;
/**
 * 
* ClassName：CommonAction   
* 类描述：   公共action
* 操作人：wangshuang   
* 操作时间：2014-9-29 下午05:45:06     
* @version 1.0
 */
@Controller
@RequestMapping("common")
public class CommonAction extends BaseAction{

	@Autowired
	AlarmService alarmService;
	
	static Logger logger = Logger.getLogger(CommonAction.class);
	
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
		return this.queryAlarmRemind(deptCode, status);
	}
	
	
	public AjaxResult queryAlarmRemind(String deptCode,short status){
		if(StringUtils.isBlank(deptCode)){
			return new AjaxResult((short)0,"无报警信息");
		}
		try {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("deptCode", deptCode);
			hm.put("status", String.valueOf(status));
			
			Pagination page=new Pagination();
			page.setPageNo(1);
			page.setPageSize(1);
			
			List<CarAlarm> list = alarmService.queryCarAlarmByRemind(hm,page);
			if(list == null || list.isEmpty())
				return new AjaxResult((short)0,"无报警信息");
			CarAlarm alarm = list.get(0);
			AlarmRemind info = new AlarmRemind();
			info.setCount((int)page.getTotalCount());
			info.setAlarmTime(alarm.getAlarmDateStr());
			info.setAddr(alarm.getAddr());
			info.setAlarmType(alarm.getTypeName());
			info.setLicense(alarm.getCarLicense());
			info.setOthers(alarm.getOthers());
			info.setTemCode(alarm.getTemCode());
			
			return new AjaxResult((short)1, info);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new AjaxResult((short)0, "无报警信息");
	}
}
