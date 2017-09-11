package com.supermap.egispboss.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispservice.base.entity.SysUpdateLogEntity;
import com.supermap.egispservice.base.service.ISysUpdateLogService;

public class SysUpdateLogServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger LOGGER = Logger.getLogger(SysUpdateLogServlet.class);

	private static final String DATE_FORMAT="yyyy-MM-dd";
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if(!CommonUtil.isStringEmpty(method)){
			LOGGER.info("## 收到请求[method,"+method+"]");
			if(method.equals("addLog")){
				// 添加日志
				saveSysUpdateLog(req,resp);
			}else if(method.equals("updateLog")){
				// 更新日志
				updateSysUpdateLog(req,resp);
			}else if(method.equals("queryLogInfo")){
				// 查询日志--单条
				querySysUpdateLog(req,resp);
			}else if(method.equals("deleteLog")){
				//删除日志  逻辑删
				deleteSysUpdateLog(req,resp);
			}else if("queryLogList".equals(method)){//查询日志列表
				querySysUpdateLogList(req,resp);
			}
		}else{
			write(req, resp, "参数[method]为空", null, false);
		}
	}
	
	/**
	 * 查询所有的更新日志
	 * @param req
	 * @param resp
	 * @Author Juannyoh
	 * 2016-8-9下午3:27:13
	 */
	private void querySysUpdateLogList(HttpServletRequest req, HttpServletResponse resp) {
		String pageSizeStr = req.getParameter("pageSize");
		String pageNoStr = req.getParameter("pageNo");
		String btime = req.getParameter("btime");
		String etime = req.getParameter("etime");
		String versionname = req.getParameter("versionname");
		try {
			if(StringUtils.isNotEmpty(versionname)){
				versionname=defaultCharacterConvert(versionname);
			}
			Map<String,Object> parammap=new HashMap<String,Object>();
			parammap.put("btime", btime);
			parammap.put("etime", etime);
			parammap.put("versionname", versionname);
			
			int pageNo = -1;
			if(!CommonUtil.isStringEmpty(pageNoStr)){
				pageNo = Integer.parseInt(pageNoStr);
				parammap.put("pageNo", pageNo);
			}
			
			int pageSize = 10;
			if(!CommonUtil.isStringEmpty(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
				parammap.put("pageSizeStr", pageSize);
			}
			ISysUpdateLogService sysUpdateLogService = (ISysUpdateLogService) getBean("sysUpdateLogService");
			Map<String, Object>  result=sysUpdateLogService.getLogsByParams(parammap);
			write(req, resp, null, result, true,DATE_FORMAT);
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 保存更新日志
	 * @param req
	 * @param resp
	 * @Author Juannyoh
	 * 2016-8-9下午4:08:33
	 */
	private void saveSysUpdateLog(HttpServletRequest req, HttpServletResponse resp) {
		String onlineTime = req.getParameter("onlineTime");
		String versionName = req.getParameter("versionName");
		String newFunctions = req.getParameter("newFunctions");
		String improveFunctions = req.getParameter("improveFunctions");
		String repairBugs = req.getParameter("repairBugs");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			if(StringUtils.isNotEmpty(onlineTime)){
				onlineTime=defaultCharacterConvert(onlineTime);
			}
			if(StringUtils.isNotEmpty(versionName)){
				versionName=defaultCharacterConvert(versionName);
				if(versionName.length()>500){
					write(req, resp, "版本名称不能超过500字符", null, false);
				}
			}
			if(StringUtils.isNotEmpty(newFunctions)){
				newFunctions=defaultCharacterConvert(newFunctions);
				if(newFunctions.length()>500){
					write(req, resp, "新增功能不能超过500字符", null, false);
				}
			}
			if(StringUtils.isNotEmpty(improveFunctions)){
				improveFunctions=defaultCharacterConvert(improveFunctions);
				if(improveFunctions.length()>500){
					write(req, resp, "改进功能不能超过500字符", null, false);
				}
			}
			if(StringUtils.isNotEmpty(repairBugs)){
				repairBugs=defaultCharacterConvert(repairBugs);
				if(repairBugs.length()>500){
					write(req, resp, "缺陷修复不能超过500字符", null, false);
				}
			}
			
			ISysUpdateLogService sysUpdateLogService = (ISysUpdateLogService) getBean("sysUpdateLogService");
			
			SysUpdateLogEntity log=new SysUpdateLogEntity();
			log.setCreateTime(new Date());
			log.setDeleteflag(0);
			log.setImproveFunctions(improveFunctions);
			log.setNewFunctions(newFunctions);
			log.setOnlineTime(sdf.parse(onlineTime));
			log.setRepairBugs(repairBugs);
			log.setVersionName(versionName);
			log=sysUpdateLogService.saveLog(log);
			write(req, resp, null, log, true,DATE_FORMAT);
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 修改日志信息
	 * @param req
	 * @param resp
	 * @Author Juannyoh
	 * 2016-8-9下午4:30:44
	 */
	private void updateSysUpdateLog(HttpServletRequest req, HttpServletResponse resp) {
		String logid=req.getParameter("logid");
		String onlineTime = req.getParameter("onlineTime");
		String versionName = req.getParameter("versionName");
		String newFunctions = req.getParameter("newFunctions");
		String improveFunctions = req.getParameter("improveFunctions");
		String repairBugs = req.getParameter("repairBugs");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			if(StringUtils.isNotEmpty(onlineTime)){
				onlineTime=defaultCharacterConvert(onlineTime);
			}
			if(StringUtils.isNotEmpty(versionName)){
				versionName=defaultCharacterConvert(versionName);
				if(versionName.length()>500){
					write(req, resp, "版本名称不能超过500字符", null, false);
				}
			}
			if(StringUtils.isNotEmpty(newFunctions)){
				newFunctions=defaultCharacterConvert(newFunctions);
				if(newFunctions.length()>500){
					write(req, resp, "新增功能不能超过500字符", null, false);
				}
			}
			if(StringUtils.isNotEmpty(improveFunctions)){
				improveFunctions=defaultCharacterConvert(improveFunctions);
				if(improveFunctions.length()>500){
					write(req, resp, "改进功能不能超过500字符", null, false);
				}
			}
			if(StringUtils.isNotEmpty(repairBugs)){
				repairBugs=defaultCharacterConvert(repairBugs);
				if(repairBugs.length()>500){
					write(req, resp, "缺陷修复不能超过500字符", null, false);
				}
			}
			
			ISysUpdateLogService sysUpdateLogService = (ISysUpdateLogService) getBean("sysUpdateLogService");
			
			SysUpdateLogEntity log=new SysUpdateLogEntity();
			log.setCreateTime(new Date());
			log.setDeleteflag(0);
			log.setImproveFunctions(improveFunctions);
			log.setNewFunctions(newFunctions);
			log.setOnlineTime(sdf.parse(onlineTime));
			log.setRepairBugs(repairBugs);
			log.setVersionName(versionName);
			log.setId(logid);
			log=sysUpdateLogService.updateLog(log);
			write(req, resp, null, log, true,DATE_FORMAT);
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 删除日志
	 * @param req
	 * @param resp
	 * @Author Juannyoh
	 * 2016-8-9下午4:32:16
	 */
	private void deleteSysUpdateLog(HttpServletRequest req, HttpServletResponse resp) {
		String logid=req.getParameter("logid");
		try {
			if(StringUtils.isNotEmpty(logid)){
				ISysUpdateLogService sysUpdateLogService = (ISysUpdateLogService) getBean("sysUpdateLogService");
				sysUpdateLogService.deleteLog(logid);
				write(req, resp, "删除成功", null, true);
			}
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获得单个日志
	 * @param req
	 * @param resp
	 * @Author Juannyoh
	 * 2016-8-9下午4:35:07
	 */
	private void querySysUpdateLog(HttpServletRequest req, HttpServletResponse resp) {
		String logid=req.getParameter("logid");
		try {
			if(StringUtils.isNotEmpty(logid)){
				ISysUpdateLogService sysUpdateLogService = (ISysUpdateLogService) getBean("sysUpdateLogService");
				SysUpdateLogEntity log = sysUpdateLogService.findById(logid);
				write(req, resp, null, log, true,DATE_FORMAT);
			}
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}
