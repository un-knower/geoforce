package com.supermap.egispweb.action.lbs;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.lbs.service.CarDisMsgService;
import com.supermap.egispweb.util.DateUtil;

//@Controller
public class CarDisMsgReportAction extends BaseAction{
	
	@Autowired
	CarDisMsgService carDisMsgService;
	
	static Logger logger = Logger.getLogger(CarDisMsgReportAction.class.getName());
	/**
	 * 跳转到列表
	 * @return
	 */
	@RequestMapping(value="/com/supermap/toMsgList")
	public String toCarDisMsgList(){
		
		return "report/carDisMsgReport";
	}
	
	/**
	 * 返回列表
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/com/supermap/msgList")
	@ResponseBody
	public Map<String,Object> getCarDisMsgList(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		
		Map<String,Object> result=new HashMap<String,Object>();
		
		if(userEntity == null){
			return null;
		}
		String type = request.getParameter("type");
		String license = request.getParameter("license");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		HashMap<String,Object> hm = new HashMap<String, Object>();
		if(type != null&& !"".equals(type)){
			hm.put("type", type);
		}
		if(license != null && !"".equals(license)){
			hm.put("license", license);
		}
		if(starttime != null && !"".equals(starttime)){
			Date startTime = DateUtil.formatStringByDate(starttime+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			hm.put("startDate", startTime);
			logger.info("startDate is "+starttime);
		}
		if(endtime != null && !"".equals(endtime)){
			Date endTime = DateUtil.formatStringByDate(endtime+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			hm.put("endDate", endTime);
			logger.info("endDate is "+endTime);
		}
		hm.put("userId", userEntity.getId());
		hm = this.getPageMap(request,hm);
		result = this.carDisMsgService.queryCarMessagePage(hm);
		return result;
	}
}
