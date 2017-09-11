package com.supermap.egispboss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseRoleList;
import com.supermap.egispservice.base.pojo.RoleFieldNames;
import com.supermap.egispservice.base.service.IRoleService;

public class RoleServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(RoleServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		try{
			if("addRole".equals(method)){
				addRole(req,resp);
			}else if("queryRoleList".equals(method)){
				queryRoleList(req,resp);
			}else if("updateRole".equals(method)){
				updateRole(req,resp);
			}else if("addPrivileges".equals(method)){
				addPrivileges(req,resp);
			}else if("addOrRmPrivileges".equals(method)){
				addOrRmPrivileges(req,resp);
			}else if("removePrivileges".equals(method)){
				removePrivileges(req,resp);
			}else if("deleteRole".equals(method)){
				deleteRole(req,resp);
			}else{
				throw new ParameterException("未支持的操作类型["+method+"]");
			}
			
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
		}
	}

	/**
	 * 
	 * <p>Title ：addOrRmPrivileges</p>
	 * Description：添加或移除权限
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-25 下午03:09:06
	 */
	private void addOrRmPrivileges(HttpServletRequest req, HttpServletResponse resp) {
		addOrRemovePrivilege(req, resp, 3);
	}

	/**
	 * 
	 * <p>Title ：deleteRole</p>
	 * Description：根据ID删除角色
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 下午05:22:10
	 */
	private void deleteRole(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(RoleFieldNames.ID);
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,RoleFieldNames.ID);
			}
			IRoleService roleService = (IRoleService) getBean("roleService");
			roleService.deleteRole(idStr);
			write(req, resp, "删除角色["+idStr+"]成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：removePrivileges</p>
	 * Description：		移除权限
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 下午05:08:32
	 */
	private void removePrivileges(HttpServletRequest req, HttpServletResponse resp) {
		addOrRemovePrivilege(req, resp, 2);
	}
	
	/**
	 * 
	 * <p>Title ：addOrRemovePrivilege</p>
	 * Description：		添加或移除权限
	 * @param req
	 * @param resp
	 * @param type：1.添加，2.移除
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 下午05:10:24
	 */
	private void addOrRemovePrivilege(HttpServletRequest req, HttpServletResponse resp,int type){
		String parameter = req.getParameter("parameter");
		try{
			if(CommonUtil.isStringEmpty(parameter)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,"parameter");
			}
			JSONObject parameterObj = JSONObject.fromObject(parameter);
			// 
			String id = null;
			if(parameterObj.containsKey(RoleFieldNames.ID)){
				id = parameterObj.getString(RoleFieldNames.ID);
				if(CommonUtil.isStringEmpty(id)){
					throw new ParameterException("角色ID["+id+"]有误");
				}
			}else{
				throw new ParameterException(ExceptionType.NULL_NO_NEED,RoleFieldNames.ID);
			}
			String[] privilegeIds = null;
			if(parameterObj.containsKey(RoleFieldNames.PRIVILEGES)){
				JSONArray array = parameterObj.getJSONArray(RoleFieldNames.PRIVILEGES);
				if(null == array || array.size() <= 0){
					throw new ParameterException(ExceptionType.NULL_NO_NEED,RoleFieldNames.PRIVILEGES);
				}
				privilegeIds = new String[array.size()];
				array.toArray(privilegeIds);
			}else{
				throw new ParameterException(ExceptionType.NULL_NO_NEED,RoleFieldNames.PRIVILEGES);
			}
			
			IRoleService roleService = (IRoleService) getBean("roleService");
			if(1 == type){
				roleService.addPrivileges(id, privilegeIds);
				write(req, resp, "添加权限成功", null, true);
			}else if(2 == type){
				// 移除权限
				roleService.removePrivileges(id, privilegeIds);
				write(req, resp, "移除权限成功", null, true);
			}else if(3 == type){
				roleService.addOrRmPrivileges(id, privilegeIds);
				write(req, resp, "更新权限成功", null, true);
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	

	/**
	 * 
	 * <p>Title ：addPrivileges</p>
	 * Description：		为角色添加权限
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 下午03:56:46
	 */
	private void addPrivileges(HttpServletRequest req, HttpServletResponse resp) {
		addOrRemovePrivilege(req, resp, 1);
		
	}

	/**
	 * 
	 * <p>Title ：updateRole</p>
	 * Description：更新角色
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 下午03:38:05
	 */
	private void updateRole(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(RoleFieldNames.ID);
		String statusStr = req.getParameter(RoleFieldNames.STATUS);
		String remarks = req.getParameter(RoleFieldNames.REMARKS);
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED, RoleFieldNames.ID);
			}
			if(!CommonUtil.isStringEmpty(remarks)){
				remarks = defaultCharacterConvert(remarks);
			}
			IRoleService roleService = (IRoleService) getBean("roleService");
			roleService.updateRole(idStr, statusStr, remarks);
			write(req, resp, "更新角色信息成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：queryRoleList</p>
	 * Description：		查询角色列表
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 下午02:49:13
	 */
	private void queryRoleList(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(RoleFieldNames.ID);
		String name = req.getParameter(RoleFieldNames.NAME);
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
			
			IRoleService roleService = (IRoleService) getBean("roleService");
			BaseRoleList roleList = roleService.queryRoleList(idStr, name, pageNo, pageSize);
			write(req, resp, null, roleList, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：addRole</p>
	 * Description：		添加角色
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 下午01:57:46
	 */
	private void addRole(HttpServletRequest req, HttpServletResponse resp) {
		String parameter = req.getParameter("parameter");
		try{
			if(CommonUtil.isStringEmpty(parameter)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED, "parameter");
			}
			parameter = defaultCharacterConvert(parameter);
			JSONObject jsonP = JSONObject.fromObject(parameter);
			// 解析名称
			String name = null;
			if(jsonP.containsKey(RoleFieldNames.NAME)){
				name = jsonP.getString(RoleFieldNames.NAME);
			}
			if(CommonUtil.isStringEmpty(name)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED, RoleFieldNames.NAME);
			}
			// 解析状态
			int statusInt = 0;
			if(jsonP.containsKey(RoleFieldNames.STATUS)){
				statusInt = jsonP.getInt(RoleFieldNames.STATUS);
			}
			// 解析备注
			String remarks = null;
			if(jsonP.containsKey(RoleFieldNames.REMARKS)){
				remarks = jsonP.getString(RoleFieldNames.REMARKS);
			}
			String[] privilegeIds = null;
			if(jsonP.containsKey(RoleFieldNames.PRIVILEGES)){
				JSONArray array = jsonP.getJSONArray(RoleFieldNames.PRIVILEGES);
				if(null == array || array.size() <= 0){
					throw new ParameterException(ExceptionType.NULL_NO_NEED,RoleFieldNames.PRIVILEGES);
				}
				privilegeIds = new String[array.size()];
				array.toArray(privilegeIds);
			}else{
				throw new ParameterException(ExceptionType.NULL_NO_NEED,RoleFieldNames.PRIVILEGES);
			}
			IRoleService roleService = (IRoleService) getBean("roleService");
			String id = roleService.addRole(name, statusInt+"", privilegeIds, remarks);
			write(req, resp, "创建角色["+id+"]成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}
