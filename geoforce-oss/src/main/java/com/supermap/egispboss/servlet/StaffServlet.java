package com.supermap.egispboss.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseStaffAccessInfo;
import com.supermap.egispservice.base.pojo.BaseStaffDetails;
import com.supermap.egispservice.base.pojo.BaseStaffList;
import com.supermap.egispservice.base.pojo.StaffFieldNames;
import com.supermap.egispservice.base.service.IStaffService;

public class StaffServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(StaffServlet.class);
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		try{
			if(!CommonUtil.isStringEmpty(method)){
				if("addStaff".equals(method)){
					addStaff(req,resp);
				}else if("queryStaffList".equals(method)){
					queryStaffList(req,resp);
				}else if("queryStaffDetail".equals(method)){
					queryStaffDetail(req,resp);
				}else if("updateStaff".equals(method)){
					updateStaff(req,resp);
				}else if("changePassword".equals(method)){
					changePassword(req,resp);
				}else if("addRoles".equals(method)){
					addRoles(req,resp);
				}else if("removeRoles".equals(method)){
					removeRoles(req,resp);
				}else if("addOrRmRoles".equals(method)){
					addOrRmRoles(req,resp);
				}else if("deleteStaff".equals(method)){
					deleteStaff(req,resp);
				}else if("login".equals(method)){
					login(req,resp);
				}else{
					throw new ParameterException("未支持的服务方法["+method+"]");
				}
			}else{
				throw new ParameterException("参数[method]不允许为空");
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * <p>Title ：addOrRmRoles</p>
	 * Description：		添加或删除角色
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-25 下午03:49:02
	 */
	private void addOrRmRoles(HttpServletRequest req, HttpServletResponse resp) {
		addOrRemoveRoles(req, resp, 3);
		
	}

	private void login(HttpServletRequest req, HttpServletResponse resp) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		LOGGER.info("USERNAME : "+username +" , PASSWORD : "+password);
	}
	/**
	 * 
	 * <p>Title ：deleteStaff</p>
	 * Description：		根据ID删除员工数据
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-21 上午11:24:14
	 */
	private void deleteStaff(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(StaffFieldNames.ID);
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,StaffFieldNames.ID);
			}
			IStaffService staffService =(IStaffService) getBean("staffService"); 
			staffService.deleteStaff(idStr);
			write(req, resp, "删除员工成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}
	/**
	 * 
	 * <p>Title ：removeRoles</p>
	 * Description：		为员工移除角色
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-21 上午09:05:09
	 */
	private void removeRoles(HttpServletRequest req, HttpServletResponse resp) {
		addOrRemoveRoles(req, resp, 2);
	}
	/**
	 * 
	 * <p>Title ：addRoles</p>
	 * Description：		为员工添加角色
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-21 上午09:05:36
	 */
	private void addRoles(HttpServletRequest req, HttpServletResponse resp) {
		addOrRemoveRoles(req, resp, 1);
	}
	/**
	 * 
	 * <p>Title ：addOrRemoveRoles</p>
	 * Description：		添加或移除角色
	 * @param req
	 * @param resp
	 * @param type
	 * Author：Huasong Huang
	 * CreateTime：2014-8-21 上午09:06:01
	 */
	private void addOrRemoveRoles(HttpServletRequest req, HttpServletResponse resp,int type){
		String parameter = req.getParameter("parameter");
		try{
			if(CommonUtil.isStringEmpty(parameter)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,"parameter");
			}
			JSONObject obj = JSONObject.fromObject(parameter);
			String id = null;
			if(obj.containsKey(StaffFieldNames.ID)){
				id = obj.getString(StaffFieldNames.ID);
				if(CommonUtil.isStringEmpty(id)){
					throw new ParameterException(ExceptionType.NULL_NO_NEED,StaffFieldNames.ID);
				}
			}else{
				throw new ParameterException(ExceptionType.NULL_NO_NEED,StaffFieldNames.ID);
			}
			String[] roleIds = null;
			if(obj.containsKey("roles")){
				JSONArray array = obj.getJSONArray("roles");
				if(null == array || array.size() <= 0){
					throw new ParameterException(ExceptionType.NULL_NO_NEED, "roles");
				}
				roleIds = new String[array.size()];
				array.toArray(roleIds);
			}else{
				throw new ParameterException(ExceptionType.NULL_NO_NEED, "roles");
			}
			IStaffService staffService =(IStaffService) getBean("staffService"); 
			if(1 == type){
				staffService.addRoles(id, roleIds);
				write(req, resp, "为员工添加角色成功", null, true);
			}else if(2 == type){
				staffService.removeRoles(id, roleIds);
				write(req, resp, "为员工移除角色成功", null, true);
			}else if(3 == type){
				staffService.addOrRmRoles(id, roleIds);
				write(req, resp, "修改员工角色成功", null, true);
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 
	 * <p>Title ：changePassword</p>
	 * Description：		修改密码
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-19 下午02:34:06
	 */
	private void changePassword(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(StaffFieldNames.ID);
		String password = req.getParameter(StaffFieldNames.PASSWORD);
		String oldPassword = req.getParameter("oldPassword");
		try{
			String checkResult = CommonUtil.checkRequredParam(new String[] { StaffFieldNames.ID,
					StaffFieldNames.PASSWORD }, new String[] { idStr, password });
			if(!CommonUtil.isStringEmpty(checkResult)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,checkResult);
			}
			IStaffService staffService =(IStaffService) getBean("staffService"); 
			staffService.changePassword(idStr, oldPassword,password);
			write(req, resp, "修改密码成功",null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}
	/**
	 * 
	 * <p>Title ：updateStaff</p>
	 * Description：		更新员工信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-19 上午10:24:54
	 */
	private void updateStaff(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(StaffFieldNames.ID);
		String mobilePhone = req.getParameter(StaffFieldNames.MOBILE_PHONE);
		String phone = req.getParameter(StaffFieldNames.PHONE);
		String remarks = req.getParameter(StaffFieldNames.REMARKS);
		String department = req.getParameter(StaffFieldNames.DEPARTMENT);
		String position = req.getParameter(StaffFieldNames.POSITION);
		String email = req.getParameter(StaffFieldNames.EMAIL);
		
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,StaffFieldNames.ID);
			}
			if(!CommonUtil.isStringEmpty(department)){
				department = defaultCharacterConvert(department);
			}
			if(!CommonUtil.isStringEmpty(position)){
				position = defaultCharacterConvert(position);
			}
			if(!CommonUtil.isStringEmpty(remarks)){
				remarks = defaultCharacterConvert(remarks);
			}
			IStaffService staffService =(IStaffService) getBean("staffService"); 
			staffService.updateStaffDetails(idStr, mobilePhone, phone, email, department, position, remarks);
			write(req, resp, "更新信息成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}
	/**
	 * 
	 * <p>Title ：queryStaffDetail</p>
	 * Description：查询用户详情
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-18 下午05:15:04
	 */
	private void queryStaffDetail(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(StaffFieldNames.ID);
		try{
			Subject subject = SecurityUtils.getSubject();
			if(CommonUtil.isStringEmpty(idStr)){
				BaseStaffAccessInfo info = (BaseStaffAccessInfo) subject.getPrincipal();
				idStr = info.getId() +"";
				if(CommonUtil.isStringEmpty(idStr)){
					throw new ParameterException(ExceptionType.NULL_NO_NEED,StaffFieldNames.ID);
				}
			}
			IStaffService staffService =(IStaffService) getBean("staffService"); 
			BaseStaffDetails details = staffService.queryStaffDetails(idStr);
			if(null == details ){
				throw new ParameterException(ExceptionType.NOT_FOUND,StaffFieldNames.ID+":"+idStr);
			}
			write(req, resp, null,details, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
		}
	}
	/**
	 * 
	 * <p>Title ：queryStaffList</p>
	 * Description：查询员工列表
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-18 下午02:35:22
	 */
	private void queryStaffList(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(StaffFieldNames.ID);
		String username = req.getParameter(StaffFieldNames.USERNAME);
		String status = req.getParameter(StaffFieldNames.STATUS);
		String pageNoStr = req.getParameter("pageNo");
		String pageSizeStr = req.getParameter("pageSize");
		try{
			int pageNo = 0;
			if(!CommonUtil.isStringEmpty(pageSizeStr)){
				pageNo = Integer.parseInt(pageNoStr);
			}
			int pageSize = 10;
			if(!CommonUtil.isStringEmpty(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
			}
			IStaffService staffService =(IStaffService) getBean("staffService"); 
			BaseStaffList staffList = staffService.queryStaffList(idStr, username,status, pageNo, pageSize);
			write(req, resp, null, staffList, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	/**
	 * 
	 * <p>Title ：addStaff</p>
	 * Description：		添加员工信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-14 下午05:31:09
	 */
	private void addStaff(HttpServletRequest req, HttpServletResponse resp) {
		String username = req.getParameter(StaffFieldNames.USERNAME);
		String password = req.getParameter(StaffFieldNames.PASSWORD);
		String realName = req.getParameter(StaffFieldNames.REAL_NAME);
		String sex = req.getParameter(StaffFieldNames.SEX);
		String position = req.getParameter(StaffFieldNames.POSITION);
		String mobilePhone = req.getParameter(StaffFieldNames.MOBILE_PHONE);
		String phone = req.getParameter(StaffFieldNames.PHONE);
		String email = req.getParameter(StaffFieldNames.EMAIL);
		String department = req.getParameter(StaffFieldNames.DEPARTMENT);
		try{
			String checkResult = CommonUtil.checkRequredParam(new String[] { StaffFieldNames.USERNAME,
					StaffFieldNames.PASSWORD, StaffFieldNames.REAL_NAME, StaffFieldNames.SEX, StaffFieldNames.POSITION,
					StaffFieldNames.EMAIL, StaffFieldNames.MOBILE_PHONE }, new String[] { username, password, realName,
					sex, position, email, mobilePhone });
			if(!CommonUtil.isStringEmpty(checkResult)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,checkResult);
			}
			realName = defaultCharacterConvert(realName);
			department = defaultCharacterConvert(department);
			position = defaultCharacterConvert(position);
			
			IStaffService staffService =(IStaffService) getBean("staffService"); 
			String id = staffService.addStaff(username, password, realName, position, sex, mobilePhone, phone, email, department);
			if(!CommonUtil.isStringEmpty(id)){
				write(req, resp, null, id, true);
			}else{
				throw new ParameterException("添加员工失败");
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
		
	}
	
}
