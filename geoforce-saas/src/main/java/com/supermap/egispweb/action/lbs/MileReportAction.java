package com.supermap.egispweb.action.lbs;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.lbs.service.CarHistoryService;
import com.supermap.egispservice.lbs.util.Pagination;


/**
 * 里程统计报表
* @ClassName: MileReportAction
* @author WangShuang
* @date 2013-8-1 下午03:36:02
 */
//@Controller
public class MileReportAction extends BaseAction{
	static Logger logger = Logger.getLogger(MileReportAction.class.getName());

	
	@Autowired
	InfoDeptService infoDeptService;
	
	@Autowired
	CarHistoryService carHistoryService;
	
	/**
	 * 里程统计报表初始化
	* @Title: init
	* @return
	* String
	* @throws
	 */
	@RequestMapping(value="/com/supermap/toMileList")
	public String init(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		InfoDeptEntity dept = this.infoDeptService.findDeptById(userEntity.getDeptId().getId());
		if(dept == null){
			return null;
		}
		request.setAttribute("dept", dept);
		return "report/mileReport";
	}
	/**
	 * 行驶里程统计
	* @Title: list
	* @return
	* String
	* @throws
	 */
	@RequestMapping(value="/com/supermap/mileList")
	@ResponseBody
	public Map<String,Object>  list(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String eid = userEntity.getEid().getId();
		String deptId = request.getParameter("deptId");
		String license = request.getParameter("license");
		String startDate = request.getParameter("starttime");
		String endDate = request.getParameter("endtime");
		String deptCode = userEntity.getDeptId().getCode();
		HashMap<String, Object> hm = new HashMap<String, Object>();
		Pagination page = getPage(request);
		if(StringUtils.isNotBlank(deptId)){
			logger.info("deptId is "+deptId);
			hm.put("deptId", deptId);
		}
		if(StringUtils.isNotBlank(deptCode)){
			hm.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(license)){
			hm.put("license", license.trim().toUpperCase());
			
		}
		if(StringUtils.isNotBlank(eid)){
			hm.put("eid", eid);
		}
		
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			return builderResult(null,"时间不能不能为空");
		}

		hm.put("startDate", startDate);
		hm.put("endDate", endDate);
		page = this.carHistoryService.getMileReportList(page, hm);
		Map<String,Object> map=builderResult(page,null);
		return map;
	}
	
}
