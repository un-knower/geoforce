package com.supermap.egispboss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BaseServiceModule;
import com.supermap.egispservice.base.pojo.BaseServiceModuleList;
import com.supermap.egispservice.base.pojo.ServiceModuleFieldNames;
import com.supermap.egispservice.base.service.IServiceModuleService;

public class ServiceModuleServlet extends BaseServlet{

	private static Logger LOGGER = Logger.getLogger(ServiceModuleServlet.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if(!CommonUtil.isStringEmpty(method)){
			if("addModule".equals(method)){
				addServiceModule(req,resp);
			}else if("updateModule".equals(method)){
				updateServiceModule(req,resp);
			}else if("queryModule".equals(method)){
				queryServiceModule(req,resp);
			}else if("deleteModule".equals(method)){
				deleteServiceModule(req,resp);
			}else if("queryServiceList".equals(method)){
				queryServiceModuleList(req,resp);
			}
		}else{
			write(req, resp, "参数[method]为空", null, false);
		}
	}

	/**
	 * 
	 * <p>Title ：queryServiceModuleList</p>
	 * Description：		查询服务模块列表
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-15 下午02:49:33
	 */
	private void queryServiceModuleList(HttpServletRequest req, HttpServletResponse resp) {
		String name = req.getParameter(ServiceModuleFieldNames.NAME);
		String status = req.getParameter(ServiceModuleFieldNames.STATUS);
		String id = req.getParameter(ServiceModuleFieldNames.ID);
		String pageNoStr = req.getParameter("pageNo");
		String pageSizeStr = req.getParameter("pageSize");
		try{
			if(!CommonUtil.isStringEmpty(name)){
				name = defaultCharacterConvert(name);
			}
			IServiceModuleService serviceModule = (IServiceModuleService) getBean("serviceModuleService");
			int pageNo = 0;
			if(!CommonUtil.isStringEmpty(pageNoStr)){
				pageNo = Integer.parseInt(pageNoStr);
			}
			int pageSize = 10;
			if(!CommonUtil.isStringEmpty(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
			}
			BaseServiceModuleList sml = serviceModule.query(id, name, status, pageNo, pageSize);
			write(req, resp, null, sml, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
		
	}

	/**
	 * 
	 * <p>Title ：deleteServiceModule</p>
	 * Description：		删除服务模块
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-12 上午10:13:48
	 */
	private void deleteServiceModule(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter("id");
		try{
			if(!CommonUtil.isStringEmpty(idStr)){
				IServiceModuleService serviceModule = (IServiceModuleService) getBean("serviceModuleService");
				serviceModule.deleteById(idStr);
				write(req, resp, "删除["+idStr+"]成功", null, true);
			}else{
				throw new ParameterException("参数[id]不允许为空");
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：queryServiceModule</p>
	 * Description：		根据ID查询模块详细信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-8 下午05:19:04
	 */
	private void queryServiceModule(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(ServiceModuleFieldNames.ID);
		try{
			if(!CommonUtil.isStringEmpty(idStr)){
				IServiceModuleService serviceModule = (IServiceModuleService) getBean("serviceModuleService");
				BaseServiceModule bsm = serviceModule.queryById(idStr);
				write(req, resp, null, bsm, true);
			}else{
				throw new ParameterException("参数["+ServiceModuleFieldNames.ID+"]不允许为空");
			}
			
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			write(req, resp, e.getMessage(), null, false);
		}
		
	}

	/**
	 * 
	 * <p>Title ：updateServiceModule</p>
	 * Description：		更新服务模块信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-8 下午04:18:50
	 */
	private void updateServiceModule(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(ServiceModuleFieldNames.ID);
		String status = req.getParameter(ServiceModuleFieldNames.STATUS);
		String useLimit = req.getParameter(ServiceModuleFieldNames.USE_LIMIT);
		String url = req.getParameter(ServiceModuleFieldNames.URL);
		String ref_url = req.getParameter(ServiceModuleFieldNames.REF_URL);
		String price = req.getParameter(ServiceModuleFieldNames.PRICE);
		String remarks = req.getParameter(ServiceModuleFieldNames.REMARKS);
		String code = req.getParameter(ServiceModuleFieldNames.CODE);
		String icon_url = req.getParameter(ServiceModuleFieldNames.ICON_URL);
		String type = req.getParameter(ServiceModuleFieldNames.TYPE);
		try{
			if(!CommonUtil.isStringEmpty(idStr)){
				IServiceModuleService serviceModule = (IServiceModuleService) getBean("serviceModuleService");
				serviceModule.update(idStr, status, useLimit, url, ref_url, remarks, price,code,icon_url,type);
				write(req, resp, "更新["+idStr+"]成功", null, true);
			}else{
				throw new ParameterException("参数["+ServiceModuleFieldNames.ID+"]不允许为空");
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			write(req, resp, e.getMessage(), null, false);
		}
		
	}

	/**
	 * 
	 * <p>Title ：addServiceModule</p>
	 * Description：添加服务模块
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-8 下午02:00:55
	 */
	private void addServiceModule(HttpServletRequest req, HttpServletResponse resp) {
		String name = req.getParameter(ServiceModuleFieldNames.NAME);
		String pid = req.getParameter(ServiceModuleFieldNames.PID);
		String status = req.getParameter(ServiceModuleFieldNames.STATUS);
		String useLimit = req.getParameter(ServiceModuleFieldNames.USE_LIMIT);
		String url = req.getParameter(ServiceModuleFieldNames.URL);
		String ref_url = req.getParameter(ServiceModuleFieldNames.REF_URL);
		String price = req.getParameter(ServiceModuleFieldNames.PRICE);
		String icon_url = req.getParameter(ServiceModuleFieldNames.ICON_URL);
		String type = req.getParameter(ServiceModuleFieldNames.TYPE);
		String remarks = req.getParameter(ServiceModuleFieldNames.REMARKS);
		try{
			name = defaultCharacterConvert(name);
			remarks = defaultCharacterConvert(remarks);
			// 检查必填字段
			String checkResult = CommonUtil.checkRequredParam(new String[] { ServiceModuleFieldNames.NAME,
					ServiceModuleFieldNames.STATUS, ServiceModuleFieldNames.URL, ServiceModuleFieldNames.PRICE,
					ServiceModuleFieldNames.ICON_URL }, new String[] { name, status, url,
					price, icon_url });
			if(!CommonUtil.isStringEmpty(checkResult)){
				throw new ParameterException(checkResult);
			}
			if(CommonUtil.isStringEmpty(type)){
				type = "1";
			}
			IServiceModuleService serviceModule = (IServiceModuleService) getBean("serviceModuleService");
			serviceModule.add(name, pid, status, useLimit, url, ref_url, price,remarks,icon_url,type);
			write(req, resp, "添加产品模块成功", null, true);
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			write(req, resp, e.getMessage(), null, false);
		}
	}



	
	
}
