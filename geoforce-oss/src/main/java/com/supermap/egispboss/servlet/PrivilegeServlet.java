package com.supermap.egispboss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

//import com.supermap.egispboss.exception.ParameterException;
//import com.supermap.egispboss.exception.ParameterException.ExceptionType;
//import com.supermap.egispboss.permission.pojo.BasePrivilegeList;
//import com.supermap.egispboss.permission.pojo.BasePrivilegeListItem;
//import com.supermap.egispboss.permission.service.IPrivilegeService;
import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BasePrivilegeList;
import com.supermap.egispservice.base.pojo.BasePrivilegeListItem;
import com.supermap.egispservice.base.pojo.PrivilegeFieldNames;
import com.supermap.egispservice.base.service.IPrivilegeService;

public class PrivilegeServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(PrivilegeServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if("addPrivilege".equals(method)){
			addPrivilege(req,resp);
		}else if("queryPrivilegeList".equals(method)){
			queryPrivilegeList(req,resp);
		}else if("queryPrivilegeDetail".equals(method)){
			queryPrivilegeDetail(req,resp);
		}else if ("updatePrivilege".equals(method)){
			updatePrivilege(req,resp);
		}else if("deletePrivilege".equals(method)){
			deletePrivilege(req,resp);
		}else{
			write(req, resp, "未支持的方法参数["+method+"]", null, false);
		}
	}
	
	/**
	 * 
	 * <p>Title ：deletePrivilege</p>
	 * Description：		删除权限数据
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 上午10:41:39
	 */
	private void deletePrivilege(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(PrivilegeFieldNames.ID);
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,PrivilegeFieldNames.ID);
			}
			IPrivilegeService privilegeService = (IPrivilegeService) getBean("privilegeService");
			privilegeService.deletePrivilege(idStr);
			write(req, resp, "删除权限成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(),null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：updatePrivilege</p>
	 * Description：更新权限信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 上午09:47:03
	 */
	private void updatePrivilege(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(PrivilegeFieldNames.ID);
		String statusStr = req.getParameter(PrivilegeFieldNames.STATUS);
		String remarks = req.getParameter(PrivilegeFieldNames.REMARKS);
		String url = req.getParameter(PrivilegeFieldNames.URL);
		
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NOT_FOUND,PrivilegeFieldNames.ID);
			}
			if(!CommonUtil.isStringEmpty(remarks)){
				remarks = defaultCharacterConvert(remarks);
			}
			IPrivilegeService privilegeService = (IPrivilegeService) getBean("privilegeService");
			privilegeService.updatePrivilege(idStr, statusStr, url, remarks);
			write(req, resp, "更新权限信息成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(),null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：queryPrivilegeDetail</p>
	 * Description：		查询订单的详细信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 上午09:45:57
	 */
	private void queryPrivilegeDetail(HttpServletRequest req,
			HttpServletResponse resp) {
		String idStr = req.getParameter(PrivilegeFieldNames.ID);
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,PrivilegeFieldNames.ID);
			}
			IPrivilegeService privilegeService = (IPrivilegeService) getBean("privilegeService");
			BasePrivilegeListItem item = privilegeService.queryById(idStr);
			write(req, resp, null, item, true);
			
		}catch(Exception e){
			write(req, resp, e.getMessage(),null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：queryPrivilegeList</p>
	 * Description：		查询权限列表
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-19 下午04:15:27
	 */
	private void queryPrivilegeList(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(PrivilegeFieldNames.ID);
		String name = req.getParameter(PrivilegeFieldNames.NAME);
		String code = req.getParameter(PrivilegeFieldNames.CODE);
		String levelStr = req.getParameter(PrivilegeFieldNames.LEVEL);
		String pageNoStr = req.getParameter("pageNo");
		String pageSizeStr = req.getParameter("pageSize");
		try{
			if(!CommonUtil.isStringEmpty(name)){
				name = defaultCharacterConvert(name);
			}
			int pageNo = 0;
			if(!CommonUtil.isStringEmpty(pageNoStr)){
				pageNo = Integer.parseInt(pageNoStr);
			}
			int pageSize = 10;
			if(!CommonUtil.isStringEmpty(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
			}
			IPrivilegeService privilegeService = (IPrivilegeService) getBean("privilegeService");
			BasePrivilegeList privilegeList = privilegeService.fuzzyQuery(idStr, levelStr,name, code, pageNo, pageSize);
			write(req, resp, null, privilegeList, true);
			
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：addPrivilege</p>
	 * Description：		添加权限
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-19 下午03:37:19
	 */
	private void addPrivilege(HttpServletRequest req, HttpServletResponse resp) {
		String name = req.getParameter(PrivilegeFieldNames.NAME);
		String code = req.getParameter(PrivilegeFieldNames.CODE);
		String statusStr = req.getParameter(PrivilegeFieldNames.STATUS);
		String remarks = req.getParameter(PrivilegeFieldNames.REMARKS);
		String pidStr = req.getParameter(PrivilegeFieldNames.PID);
		String url = req.getParameter(PrivilegeFieldNames.URL);
		try{
			String checkResult = CommonUtil.checkRequredParam(new String[] { PrivilegeFieldNames.NAME,
					PrivilegeFieldNames.CODE,PrivilegeFieldNames.URL}, new String[] {
					name, code, url});
			if(!CommonUtil.isStringEmpty(checkResult)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED, checkResult);
			}
			name = defaultCharacterConvert(name);
			if(!CommonUtil.isStringEmpty(remarks)){
				remarks = defaultCharacterConvert(remarks);
			}
			
			IPrivilegeService privilegeService = (IPrivilegeService) getBean("privilegeService");
			String id = privilegeService.add(name, code,  statusStr, remarks, pidStr,url);
			write(req, resp, "添加权限成功[id:"+id+"]", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		                                    
		
	}
}
