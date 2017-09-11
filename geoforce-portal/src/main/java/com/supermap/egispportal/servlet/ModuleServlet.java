package com.supermap.egispportal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

//import com.supermap.egispportal.exception.ParameterException;
//import com.supermap.egispportal.exception.ParameterException.ExceptionType;
//import com.supermap.egispportal.pojo.BaseServiceModule;
//import com.supermap.egispportal.pojo.ServiceModuleByType;
//import com.supermap.egispportal.pojo.ServiceModuleFieldNames;
//import com.supermap.egispportal.service.IServiceModule;
import com.supermap.egispportal.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseServiceModule;
import com.supermap.egispservice.base.pojo.ServiceModuleByType;
import com.supermap.egispservice.base.pojo.ServiceModuleFieldNames;
import com.supermap.egispservice.base.service.IServiceModuleService;

/**
 * 
 * <p>Title: ModuleServlet</p>
 * Description:		服务模块
 *
 * @author Huasong Huang
 * CreateTime: 2014-9-4 下午02:34:32
 */
public class ModuleServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(ModuleServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		try{
			if(CommonUtil.isStringEmpty(method)){
				throw new ParameterException(ExceptionType.NOT_FOUND, "method");
			}
			if("queryServiceList".equals(method)){
				queryServiceList(req,resp);
			}else if("queryModule".equals(method)){
				queryModule(req,resp);
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：queryModule</p>
	 * Description：查询模块详情
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-9-5 上午10:54:55
	 */
	private void queryModule(HttpServletRequest req, HttpServletResponse resp) {
		String id = req.getParameter(ServiceModuleFieldNames.ID);
		try{
			if(CommonUtil.isStringEmpty(id)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,ServiceModuleFieldNames.ID);
			}
			IServiceModuleService serviceModule = (IServiceModuleService) getBean("serviceModuleService");
			BaseServiceModule serviceModuleInfo = serviceModule.queryById(id);
			write(req, resp, null, serviceModuleInfo, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：queryServiceList</p>
	 * Description：		查询服务模块列表
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-9-4 下午02:34:22
	 */
	private void queryServiceList(HttpServletRequest req, HttpServletResponse resp) {
		String name = req.getParameter(ServiceModuleFieldNames.NAME);
		String status = req.getParameter(ServiceModuleFieldNames.STATUS);
		String id = req.getParameter(ServiceModuleFieldNames.ID);
		String pageNoStr = req.getParameter("pageNo");
		String pageSizeStr = req.getParameter("pageSize");
		// 查询模块类型：0，父ID为空；2，父ID不为空；3，全部
		String moduleTypeStr = req.getParameter("moduleType");
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
			int moduleType = 2;
			if(!CommonUtil.isStringEmpty(moduleTypeStr)){
				moduleType = Integer.parseInt(moduleTypeStr);
			}
			
//			BaseServiceModuleList sml = serviceModule.query(id, name, status, pageNo, pageSize,moduleType);
			ServiceModuleByType[] smbs = serviceModule.queryAllByType(moduleType+"",status);
			write(req, resp, null, smbs, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	
}
