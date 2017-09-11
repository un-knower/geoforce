package com.supermap.egispweb.action.personPlan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.action.region.RegionSetAction;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.util.DateUtil;
import com.supermap.lbsp.provider.common.Page;

/**
 * 计划审批
 * @author wang
 *
 */
@Controller
public class PersonPlanAction extends BaseAction {
	static Logger logger = Logger.getLogger(RegionSetAction.class.getName());
	/**
	 * 工作计划初始化
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/toPersonPlan")
	public String toRegion(HttpServletRequest request,HttpServletResponse response){
		logger.info("to plan.jsp");
		return "personPlan/plan";
	}
	/**
	 * 计划列表
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="/com/supermap/planList")
	@ResponseBody
	public Page planList(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws ParseException{
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String deptCode=userEntity.getDeptId().getCode();
		String name = request.getParameter("name");
		String storeNanme = request.getParameter("storeNanme");
		String status=request.getParameter("status");
		String begindate = request.getParameter("begindate");//开始时间和结束时间处理
		String enddate = request.getParameter("enddate");
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(deptCode)){
			hm.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(storeNanme)){
			hm.put("storeNanme",storeNanme);
		}
		if(StringUtils.isNotBlank(name)){
			hm.put("psersonName", name);
		}
		if(StringUtils.isNotBlank(status)){
			hm.put("status", status);
		}
		if(StringUtils.isNotBlank(begindate)){
			Date startTime = DateUtil.formatStringByDate(begindate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			hm.put("begindate", startTime);
			
		}if (StringUtils.isNotBlank(enddate)){
			Date endTime = DateUtil.formatStringByDate(enddate+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			hm.put("enddate", endTime);	
		}
			Page page = getPage(request);
			page=personPlanConsumer.pagequeryPersonPlan(page, hm);
			return page;
	}
	
	/**
	 *  计划处理
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/updatePersonPlan")
	@ResponseBody
	public AjaxResult updatePersonPlan(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return new AjaxResult((short)1,"请用户登录");
		}
		String status = request.getParameter("status");
		
		String opinion=request.getParameter("opinion");
		if(opinion == null){
			return new AjaxResult((short)1,"请填写驳回内容");
		}
		String planId=request.getParameter("planId");
		if(planId == null){
			return new AjaxResult((short)1,"请填选择计划");
		}
		int i=personPlanConsumer.updatePersonPlan(userEntity.getId(), opinion, planId, status);
		if(i==0){
			return new AjaxResult((short)1,"操作成功");
		}
		return null;
	}
	

}
