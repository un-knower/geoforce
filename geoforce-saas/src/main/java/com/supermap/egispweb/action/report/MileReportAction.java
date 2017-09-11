package com.supermap.egispweb.action.report;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.info.Dept;


/**
 * 里程统计报表
* @ClassName: MileReportAction
* @author WangShuang
* @date 2013-8-1 下午03:36:02
 */
//@Controller
public class MileReportAction extends BaseAction{
	static Logger logger = Logger.getLogger(MileReportAction.class.getName());
	private static final long serialVersionUID = -2899755222398681146L;
	
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
		Dept dept = deptConsumer.getDept(userEntity.getDeptId().getId());
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
	public Page list(HttpServletRequest request,HttpSession session){
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
		Page page = getPage(request);
		if(StringUtils.isNotBlank(deptId)){
			
			Dept dept = deptConsumer.getDept(deptId);
			if(dept != null){
				deptCode = deptConsumer.getDept(deptId).getCode();
				logger.info("deptId is "+deptId);
			}
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
			
			return page;
		}

		hm.put("startDate", startDate);
		hm.put("endDate", endDate);
		page = carHistoryConsumer.getMileReportList(page, hm);
		return page;
	}
	
}
