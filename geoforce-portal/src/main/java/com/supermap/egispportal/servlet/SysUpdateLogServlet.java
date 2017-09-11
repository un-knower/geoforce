package com.supermap.egispportal.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

//import com.supermap.egispportal.service.ISysUpdateLogService;
import com.supermap.egispportal.util.CommonUtil;
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
			if("queryLogList".equals(method)){//查询日志列表
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
				if(pageSize>50){
					pageSize=50;
				}
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
	
}
