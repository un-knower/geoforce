package com.supermap.egispboss.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.supermap.egispboss.excel.export.ExportExcel;
import com.supermap.egispboss.excel.export.UserBean;
import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispboss.util.CommonUtil.DateType;
import com.supermap.egispboss.util.EncryptionUtil;
import com.supermap.egispservice.base.entity.ComEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.entity.UserStatusEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseUserInfo;
import com.supermap.egispservice.base.pojo.BaseUserListInfo;
import com.supermap.egispservice.base.pojo.BaseUserListPkg;
import com.supermap.egispservice.base.pojo.UserInfoFieldNames;
import com.supermap.egispservice.base.service.UserService;


public class UserServlet extends BaseServlet{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(UserServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if(!CommonUtil.isStringEmpty(method)){
			LOGGER.info("## 收到请求[method,"+method+"]");
			if(method.equals("addUser")){
				// 添加用户
				addUserInfo(req,resp);
			}else if(method.equals("updateUser")){
				// 更新用户状态和备注，对于运营支撑系统只支持这两种操作
				updateUserStatus(req,resp);
			}else if(method.equals("queryUserInfo")){
				// 查询用户信息
				queryUserInfo(req,resp);
			}else if(method.equals("deleteUserInfo")){
				deleteUserInfo(req,resp);
			}else if("queryUserList".equals(method)){
				queryUserList(req,resp);
			}else if("queryChildUser".equals(method)){
				queryChildUserList(req,resp);//获取子账号
			}else if("exportUserExcel".equals(method)){
				exportUserExcel(req,resp);//导出用户信息
			}
		}else{
			write(req, resp, "参数[method]为空", null, false);
		}
	}

	




	/**
	 * 
	 * <p>Title ：queryUserList</p>
	 * Description：查询用户列表
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-13 下午05:47:28
	 */
	private void queryUserList(HttpServletRequest req, HttpServletResponse resp) {
		String pageSizeStr = req.getParameter("pageSize");
		String pageNoStr = req.getParameter("pageNo");
		String info = req.getParameter("info");
		String id = req.getParameter("id");
		String status = req.getParameter("status");
		
		String admincode=req.getParameter("admincode");//省份筛选
		String combusiness=req.getParameter("combusiness");//所属行业筛选
		String btime=req.getParameter("btime");//开始时间
		String etime=req.getParameter("etime");//结束时间
		
		//时间
		Date btimed=null;
		Date etimed=null;
		
		try{
			//时间转换
			if(!StringUtils.isEmpty(btime)){
				btimed=CommonUtil.dateConvert(btime.trim()+" 00:00:00", DateType.TIMESTAMP);
			}
			if(!StringUtils.isEmpty(etime)){
				etimed=CommonUtil.dateConvert(etime.trim()+" 23:59:59", DateType.TIMESTAMP);
			}
			
			int pageSize = 10;
			if(!CommonUtil.isStringEmpty(pageSizeStr)){
				int tempPageSize = Integer.parseInt(pageSizeStr);
				if(0 < tempPageSize && tempPageSize <= 10){
					pageSize = tempPageSize;
				}
			}
			int pageNo = 0;
			if(!CommonUtil.isStringEmpty(pageNoStr)){
				int tempPageNo = Integer.parseInt(pageNoStr);
				if(tempPageNo > 0 ){
					pageNo = tempPageNo;
				}
			}
			
			UserStatusEntity userStatus = null;
			if(!CommonUtil.isStringEmpty(status)){
				userStatus = new UserStatusEntity();
				userStatus.setValue(status);
			}
			
			if(!CommonUtil.isStringEmpty(info)){
				info=defaultCharacterConvert(info);
			}
			
			if(!CommonUtil.isStringEmpty(combusiness)){
				combusiness=defaultCharacterConvert(combusiness);
			}
			
			UserService userService = (UserService) getBean("userService");
			BaseUserListPkg pkg = userService.query(id,info,userStatus,pageNo, pageSize,admincode,combusiness,btimed,etimed);
			if(null != pkg && pkg.getCurrentCount() > 0){
				write(req, resp, null, pkg, true);
			}else{
				throw new ParameterException(ExceptionType.NOT_FOUND,"");
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			write(req, resp, e.getMessage(), null, false);
		}
		
	}

	/*private BaseUserListInfo[] userList2Array(List<BaseUserListInfo> lists){
		BaseUserListInfo bui[] = new BaseUserListInfo[lists.size()];
		for(int i=0;i<lists.size();i++){
			bui[i] = lists.get(i);
		}
		return bui;
	}*/

	/**
	 * 
	 * <p>Title ：addUserInfo</p>
	 * Description：	添加用户
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-7 下午03:30:42
	 */
	private void addUserInfo(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = (UserService) getBean("userService");
		try{
			UserEntity ue = parseUserParameter(req);
			String companyName = req.getParameter("companyName");
			String companyEmail = req.getParameter("companyEmail");
			String companyPhone = req.getParameter("companyPhone");
			String companyRemarks = req.getParameter("companyRemarks");
			String companyAddress = req.getParameter("companyAddress");
			ComEntity ce = new ComEntity();
			if(StringUtils.isEmpty(companyName)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED, "companyName");
			}
			companyName = defaultCharacterConvert(companyName);
			ce.setName(companyName);
			if(!StringUtils.isEmpty(companyAddress)){
				companyAddress = defaultCharacterConvert(companyAddress);
				ce.setAddress(companyAddress);
			}
			if(!StringUtils.isEmpty(companyRemarks)){
				companyRemarks = defaultCharacterConvert(companyRemarks);
				ce.setRemark(companyRemarks);
			}
			ce.setEmail(companyEmail);
			ce.setPhone(companyPhone);
			userService.saveUser(ue,ce);
			write(req, resp, "添加用户[id,"+ue.getId()+"]", null, true);
		}catch(ParameterException e){
			write(req,resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}catch(Exception e){
			write(req,resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	
	/**
	 * 
	 * <p>Title ：updateUserStatus</p>
	 * Description：		更新用户信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-7 下午04:19:37
	 */
	private void updateUserStatus(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = (UserService) getBean("userService");
		String idStr = req.getParameter("id");
		try{
			if(!CommonUtil.isStringEmpty(idStr)){
				boolean existUser = userService.existUser(idStr);
				if(existUser){
					boolean isNeedUpdate = false;
					String remarks = req.getParameter("remarks");
					if(!CommonUtil.isStringEmpty(remarks)){
						if(!isNeedUpdate){
							isNeedUpdate = true;
						}
					}
					String statusSTr = req.getParameter("status");
					UserStatusEntity us = null;
					// 判断待修改的状态是否为空
					if(!CommonUtil.isStringEmpty(statusSTr)){
						us = new UserStatusEntity();
						us.setValue(statusSTr);
						if(null == us){
							throw new ParameterException("参数[status,"+statusSTr+"]有误");
						}
						if(!isNeedUpdate){
							isNeedUpdate = true;
						}
					}
					if(isNeedUpdate){
						userService.updateUserStatus(idStr, us, remarks);
						write(req, resp, "更新用户信息成功", null, true);
					}else{
						throw new ParameterException("待更新的信息为空， 不执行更新");
					}
				}else{
					throw new ParameterException("用户ID["+idStr+"]未找到该用户信息，无法进行修改");
				}
			}else{
				throw new ParameterException("用户ID为空，无法进行修改");
			}
		}catch(Exception e){
			write(req,resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：queryUserInfo</p>
	 * Description：	查询用户详细信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-7 下午05:13:20
	 */
	private void queryUserInfo(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = (UserService) getBean("userService");
		try{
			String idStr = req.getParameter("id");
			if(!CommonUtil.isStringEmpty(idStr)){
				BaseUserInfo ue = userService.findEbossUserById(idStr);
				if(null != ue){
					write(req,resp, null, ue, true);
				}else{
					throw new ParameterException("用户ID["+idStr+"]未查询到相关信息");
				}
			}else{
				throw new ParameterException("参数[id]为空");
			}
		}catch(Exception e){
			write(req,resp, e.getMessage(), null, false);
		}
	}
	
	/**
	 * 
	 * <p>Title ：parseUserParameter</p>
	 * Description：		解析用户信息参数
	 * @param req
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-7 下午03:01:20
	 */
	private UserEntity parseUserParameter(HttpServletRequest req) throws ParameterException {
		UserEntity userEntity = new UserEntity();
		String username = req.getParameter(UserInfoFieldNames.USERNAME);
		userEntity.setUsername(username);
		// 需要对密码进行解密
		String password = req.getParameter(UserInfoFieldNames.PASSWORD);
		userEntity.setPassword(password);
		String realName = req.getParameter(UserInfoFieldNames.REALNAME);
		if(!StringUtils.isEmpty(realName)){
			realName = defaultCharacterConvert(realName);
			userEntity.setRealname(realName);
		}
		String qqInfo = req.getParameter(UserInfoFieldNames.QQ);
		userEntity.setQq(qqInfo);
		
		String sex = req.getParameter(UserInfoFieldNames.SEX);
		if(!CommonUtil.isStringEmpty(sex)){
			userEntity.setSex(sex.charAt(0));
		}
		String mobilephone = req.getParameter(UserInfoFieldNames.MOBILEPHONE);
		userEntity.setMobilephone(mobilephone);
		String telephone = req.getParameter(UserInfoFieldNames.TELEPHONE);
		userEntity.setTelephone(telephone);
		String email = req.getParameter(UserInfoFieldNames.EMAIL);
		userEntity.setEmail(email);
		String fax = req.getParameter(UserInfoFieldNames.FAX);
		userEntity.setFax(fax);
		String zipCode = req.getParameter(UserInfoFieldNames.ZIPCODE);
		userEntity.setZipCode(zipCode);
		String address = req.getParameter(UserInfoFieldNames.ADDRESS);
		userEntity.setAddress(address);
		String remarks = req.getParameter(UserInfoFieldNames.REMARKS);
		userEntity.setRemark(remarks);
		String checkResult = CommonUtil.checkRequredParam(new String[] { 
				UserInfoFieldNames.REALNAME, UserInfoFieldNames.MOBILEPHONE, UserInfoFieldNames.EMAIL },
				new String[] { userEntity.getRealname(), userEntity.getMobilephone(),
						userEntity.getEmail() });
		if(!CommonUtil.isStringEmpty(checkResult)){
			throw new ParameterException(checkResult);
		}
		// 对密码进行base64解码
		password = EncryptionUtil.base64Decode(password);
		userEntity.setPassword(password);
		return userEntity;
	}
	

	/**
	 * 
	 * <p>Title ：deleteUserInfo</p>
	 * Description：	删除用户信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-8 上午11:21:51
	 */
	private void deleteUserInfo(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = (UserService) getBean("userService");
		String idStr = req.getParameter("id");
		try{
			if(!CommonUtil.isStringEmpty(idStr)){
				boolean isExistUser = userService.existUser(idStr);
				if(isExistUser){
					userService.deleteUserById(idStr);
					write(req, resp, "删除用户["+idStr+"]成功", null, true);
				}else{
					throw new ParameterException("用户Id["+idStr+"]不存在");
				}
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	private void queryChildUserList(HttpServletRequest req, HttpServletResponse resp) {
		String topuserid = req.getParameter("parentuserid");
		if(StringUtils.isEmpty(topuserid)){
			write(req, resp, "总账号不能为空", null, false);
		}
		try{
			UserService userService = (UserService) getBean("userService");
			UserEntity topuser=userService.findUserById(topuserid);
			if(null!=topuser){
				List<UserEntity> childuserlist=userService.getUsersByDept(topuser.getDeptId().getId());
				if(null!=childuserlist&&childuserlist.size()>0){
					if(childuserlist.contains(topuser)){
						childuserlist.remove(topuser);
					}
					write(req, resp, null, childuserlist, true);
				}else{
					throw new ParameterException(ExceptionType.NOT_FOUND,"");
				}
			}
			else{
				throw new ParameterException(ExceptionType.NOT_FOUND,"");
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	/**
	 * 导出用户信息
	 * @param req
	 * @param resp
	 * @Author Juannyoh
	 * 2016-9-18下午3:25:02
	 */
	public void exportUserExcel(HttpServletRequest req, HttpServletResponse resp){
		try{
			String info = req.getParameter("info");
			String id = req.getParameter("id");
			String status = req.getParameter("status");
			
			String admincode=req.getParameter("admincode");//省份筛选
			String combusiness=req.getParameter("combusiness");//所属行业筛选
			String btime=req.getParameter("btime");//开始时间
			String etime=req.getParameter("etime");//结束时间
			
			//时间
			Date btimed=null;
			Date etimed=null;
		
			//时间转换
			if(!StringUtils.isEmpty(btime)){
				btimed=CommonUtil.dateConvert(btime.trim()+" 00:00:00", DateType.TIMESTAMP);
			}
			if(!StringUtils.isEmpty(etime)){
				etimed=CommonUtil.dateConvert(etime.trim()+" 23:59:59", DateType.TIMESTAMP);
			}
			
			UserStatusEntity userStatus = null;
			if(!CommonUtil.isStringEmpty(status)){
				userStatus = new UserStatusEntity();
				userStatus.setValue(status);
			}
			
			if(!CommonUtil.isStringEmpty(info)){
				info=defaultCharacterConvert(info);
			}
			
			if(!CommonUtil.isStringEmpty(combusiness)){
				combusiness=defaultCharacterConvert(combusiness);
			}
			UserService userService = (UserService) getBean("userService");
			BaseUserListPkg pkg = userService.query(id,info,userStatus,-1, 10,admincode,combusiness,btimed,etimed);
			if(pkg!=null){
				BaseUserListInfo[] user=pkg.getUserInfos();
				if(null!=user&&user.length>0){
					List<UserBean> datasets=new ArrayList<UserBean>();
					for(BaseUserListInfo userinfo:user){
						UserBean eu=new UserBean();
						eu.setAdminname(userinfo.getAdminname());
						eu.setCombusiness(userinfo.getCombusiness());
						eu.setComname(userinfo.getCompanyName());
						eu.setEmail(userinfo.getEmail());
						eu.setFirstlogin(userinfo.getFirstLogin());
						eu.setRealname(userinfo.getRealName());
						eu.setStatus(getValueOfStatus(userinfo.getStatus()));
						eu.setTelephone(userinfo.getTelephone());
						eu.setUserid(userinfo.getId());
						eu.setUsername(userinfo.getUsername());
						datasets.add(eu);
					}
					ExportExcel<UserBean> ex = new ExportExcel<UserBean>();
					resp.setHeader("Connection", "close");  
					resp.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");  
					 //防止文件名含有中文乱码
					String filedisplay = "用户信息.xls";
                    filedisplay = new String( filedisplay.getBytes("gb2312"), "ISO8859-1" );
			        resp.setHeader("Content-Disposition", "attachment;filename=" + filedisplay); 
					req.setCharacterEncoding("UTF-8");
					resp.setCharacterEncoding("UTF-8");
                    String[] titles={"用户ID","昵称","邮箱","手机号码","真实姓名","公司名称","公司所在地","所属行业","首次登录时间","状态"};
                    OutputStream out = resp.getOutputStream();
		            ex.exportUserExcel("用户信息",titles, datasets, out,"yyyy-MM-dd HH:mm:ss");
					out.close();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getValueOfStatus(String status){
		if(null!=status){
			if(status.equals("0")){
				return "普通";
			}else if(status.equals("1")){
				return "禁用";
			}else return "其他";
		}else return "其他";
	}

}
