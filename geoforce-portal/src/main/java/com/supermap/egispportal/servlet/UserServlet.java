package com.supermap.egispportal.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;

import com.supermap.egispportal.constants.PortalConstants;
//import com.supermap.egispportal.entity.CompanyEntity;
//import com.supermap.egispportal.entity.UserEntity;
//import com.supermap.egispportal.entity.UserInfoFieldNames;
//import com.supermap.egispportal.exception.ParameterException;
//import com.supermap.egispportal.exception.ParameterException.ExceptionType;
//import com.supermap.egispportal.pojo.BaseAccessInfo;
//import com.supermap.egispportal.pojo.BaseUserInfo;
//import com.supermap.egispportal.service.IPhoneService;
//import com.supermap.egispportal.service.IUserService;
import com.supermap.egispportal.util.CommonUtil;
import com.supermap.egispportal.util.EncryptionUtil;
import com.supermap.egispservice.base.entity.ComEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseAccessInfo;
import com.supermap.egispservice.base.pojo.BaseUserInfo;
import com.supermap.egispservice.base.pojo.UserInfoFieldNames;
import com.supermap.egispservice.base.service.IPhoneService;
import com.supermap.egispservice.base.service.UserService;

public class UserServlet extends BaseServlet {
	
	private static Logger LOGGER = Logger.getLogger(UserServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		try{
			if(CommonUtil.isStringEmpty(method)){
				throw new ParameterException(ExceptionType.NOT_FOUND, "method");
			}
			// 补充用户信息
			if("sup".equals(method)){
				sup(req,resp);
			}else if("updateUser".equals(method)){
				updateUser(req,resp);
			}else if("queryUserInfo".equals(method)){
				queryUserInfo(req,resp);
			}else if("changePassword".equals(method)){
				changePassword(req,resp);
			}else if("requestLicense".equals(method)){
				requestLicense(req, resp);
			}else if("phoneService".equals(method)){
				sendCaptChar(req, resp);
			}else if("bindPhone".equals(method)){
				bindPhone(req, resp);
			}
			/*else if("regist".equals(method)){
				regist(req, resp);
			}*/
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * <p>Title ：bindPhone</p>
	 * Description：		绑定电话
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2015-7-23 下午07:56:33
	 */
	private void bindPhone(HttpServletRequest req, HttpServletResponse resp) {
		String telephone = req.getParameter("phone");
		String code = req.getParameter("captcha");
		try {
			if(StringUtils.isEmpty(telephone)){
				throw new Exception("phone不允许为空");
			}
			if(StringUtils.isEmpty(code)){
				throw new Exception("captchar不允许为空");
			}
			int iid = getUserInfo().getIid();
			IPhoneService phoneService = (IPhoneService) getBean("phoneService");
			phoneService.bindTelephone(telephone, iid+"", code);
			BaseAccessInfo bai = getUserInfo();
			if(null != bai){
				bai.setTel(telephone);
				setUserInfo(bai);
			}
			// 更新用户手机
			write(req, resp, "绑定手机成功", null, true);
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
		
	}
	
	
	/**
	 * 
	 * <p>Title ：sendCaptChar</p>
	 * Description：		发送验证码服务
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2015-7-23 下午06:25:13
	 */
	private void sendCaptChar(HttpServletRequest req, HttpServletResponse resp) {
		String telephone = req.getParameter("phone");
		try {
			if(StringUtils.isEmpty(telephone)){
				throw new Exception("phone不允许为空");
			}
			IPhoneService phoneService = (IPhoneService) getBean("phoneService");
			phoneService.getValidationCode(telephone);
			write(req, resp, "发送验证码成功", null, true);
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	/**
	 * 
	 * <p>Title ：regist</p>
	 * Description：注册
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-11-24 下午04:36:45
	 */
	private void regist(HttpServletRequest req, HttpServletResponse resp) {
		try{
			UserEntity userEntity = parseUserParameter(req);
			if(StringUtils.isEmpty(userEntity.getPassword())){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,UserInfoFieldNames.PASSWORD);
			}
			
			String companyName = req.getParameter(UserInfoFieldNames.COMPANY_NAME);
			String companyPhone = req.getParameter(UserInfoFieldNames.COMPANY_PHONE);
			String companyEmail = req.getParameter(UserInfoFieldNames.COMPANY_EMAIL);
			String companyAddress = req.getParameter(UserInfoFieldNames.COMPANY_ADDRESS);
			
			if(CommonUtil.isStringEmpty(companyName)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,UserInfoFieldNames.COMPANY_NAME);
			}
			
			companyName = defaultCharacterConvert(companyName);
			if(!CommonUtil.isStringEmpty(companyAddress)){
				companyAddress = defaultCharacterConvert(companyAddress);
			}
			ComEntity ce = null;
			ce = new ComEntity();
			ce.setAddress(companyAddress);
			ce.setEmail(companyEmail);
			ce.setName(companyName);
			ce.setPhone(companyPhone);
			
			UserService userService = (UserService) getBean("userService");
			userService.regist(userEntity, ce);
			write(req, resp, "注册成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	
	
	/**
	 * 
	 * <p>Title ：changePassword</p>
	 * Description：修改密码
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-9-4 下午02:09:32
	 */
	private void changePassword(HttpServletRequest req, HttpServletResponse resp) {
		String password = req.getParameter(UserInfoFieldNames.PASSWORD);
		String oldPassword = req.getParameter(UserInfoFieldNames.OLD_PASSWORD);
		try{
			Subject subject = SecurityUtils.getSubject();
			if(!subject.isAuthenticated()){
				throw new ParameterException("用户未登录");
			}
			BaseAccessInfo accessInfo = (BaseAccessInfo) subject.getPrincipal();
			if(null == accessInfo){
				throw new ParameterException("用户信息为空");
			}
			String id = accessInfo.getUserId();
			
			String checkResult = CommonUtil.checkRequredParam(new String[] { 
					UserInfoFieldNames.PASSWORD, UserInfoFieldNames.OLD_PASSWORD }, new String[] {  password,
					oldPassword });
			if(!CommonUtil.isStringEmpty(checkResult)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,checkResult);
			}
			password = EncryptionUtil.base64Decode(password);
			oldPassword = EncryptionUtil.base64Decode(oldPassword);
			UserService userService = (UserService) getBean("userService");
			userService.changePassword(id,password,oldPassword);
			write(req, resp, "修改密码成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：queryUserInfo</p>
	 * Description：		查询用户详情
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-9-4 上午11:35:28
	 */
	private void queryUserInfo(HttpServletRequest req, HttpServletResponse resp) {
		try{
			
			BaseAccessInfo accessInfo = getUserInfo();
			if(null == accessInfo){
				throw new ParameterException("用户信息为空,请登录");
			}
			BaseUserInfo bui = null;
			if(!accessInfo.getSource().equals("-1")){
				String id = accessInfo.getUserId();
				UserService userService = (UserService) getBean("userService");
				 bui = userService.findEbossUserById(id);
			}else{
				bui = new BaseUserInfo();
				bui.setEmail(accessInfo.getEmail());
			}
			write(req, resp, null, bui, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * 
	 * <p>Title ：updateUser</p>
	 * Description：		更新用户信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-26 下午03:58:01
	 */
	private void updateUser(HttpServletRequest req, HttpServletResponse resp) {
		String mobilePhone = req.getParameter(UserInfoFieldNames.MOBILEPHONE);
		String phone = req.getParameter(UserInfoFieldNames.TELEPHONE);
		String fax = req.getParameter(UserInfoFieldNames.FAX);
		String zipCode = req.getParameter(UserInfoFieldNames.ZIPCODE);
		String address = req.getParameter(UserInfoFieldNames.ADDRESS);
		String companyPhone = req.getParameter(UserInfoFieldNames.COMPANY_PHONE);
		String companyEmail = req.getParameter(UserInfoFieldNames.COMPANY_EMAIL);
		String companyAddress = req.getParameter(UserInfoFieldNames.COMPANY_ADDRESS);
		String qq = req.getParameter(UserInfoFieldNames.QQ);
		String companyName = req.getParameter(UserInfoFieldNames.COMPANY_NAME);
		String realName = req.getParameter(UserInfoFieldNames.REALNAME);
		
		String remarks = req.getParameter(UserInfoFieldNames.REMARKS);
		
		String admincode= req.getParameter(UserInfoFieldNames.COMPANY_ADMINCODE);
		String adminname= req.getParameter(UserInfoFieldNames.COMPANY_ADMINNAME);
		String combusiness= req.getParameter(UserInfoFieldNames.COMPANY_COMBUSINESS);
		String businessremark= req.getParameter(UserInfoFieldNames.COMPANY_BUSINESSREMARK);
		
		try{
			
			BaseAccessInfo accessInfo = getUserInfo();
			if(null == accessInfo){
				throw new ParameterException("用户信息为空");
			}
			String id = accessInfo.getUserId();
			
			if(!CommonUtil.isStringEmpty(address)){
				address = defaultCharacterConvert(address);
			}
			if(!CommonUtil.isStringEmpty(companyAddress)){
				companyAddress =defaultCharacterConvert(companyAddress);
			}
			if(!CommonUtil.isStringEmpty(remarks)){
				remarks = defaultCharacterConvert(remarks);
			}
			
			if(!StringUtils.isEmpty(realName)){
				realName = defaultCharacterConvert(realName);
			}
			if(!StringUtils.isEmpty(companyName)){
				companyName = defaultCharacterConvert(companyName);
			}
			if(!StringUtils.isEmpty(admincode)){
				admincode = defaultCharacterConvert(admincode);
			}
			if(!StringUtils.isEmpty(adminname)){
				adminname = defaultCharacterConvert(adminname);
			}
			if(!StringUtils.isEmpty(combusiness)){
				combusiness = defaultCharacterConvert(combusiness);
			}
			if(!StringUtils.isEmpty(businessremark)){
				businessremark = defaultCharacterConvert(businessremark);
			}
			
			UserService userService = (UserService) getBean("userService");
			userService.updateUserInfo(id, mobilePhone, phone, fax, zipCode, address, companyAddress, companyEmail,
					companyPhone, remarks,qq,realName,companyName
					,admincode,adminname,combusiness,businessremark);
			write(req, resp, "更新信息成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：regist</p>
	 * Description：		注册
	 * @param req
	 * @param resp 
	 * Author：Huasong Huang
	 * CreateTime：2014-8-26 上午11:49:05
	 */
	private void sup(HttpServletRequest req, HttpServletResponse resp) {
		try{
			req.setCharacterEncoding("utf-8");
			HttpSession session = req.getSession();
			Subject subject = SecurityUtils.getSubject();
			BaseAccessInfo bci = getUserInfo();
			UserEntity userEntity = parseUserParameter(req);
			userEntity.setUsername(bci.getUsername());
			userEntity.setEmail(bci.getEmail());
			//userEntity.setTelephone(bci.getTel());
			if(StringUtils.isEmpty(userEntity.getPassword())){
				userEntity.setPassword("placeholder");
			}
			String companyName = req.getParameter(UserInfoFieldNames.COMPANY_NAME);
			String companyPhone = req.getParameter(UserInfoFieldNames.COMPANY_PHONE);
			String companyEmail = req.getParameter(UserInfoFieldNames.COMPANY_EMAIL);
			String companyAddress = req.getParameter(UserInfoFieldNames.COMPANY_ADDRESS);
			
			String companyadmincode = req.getParameter(UserInfoFieldNames.COMPANY_ADMINCODE);
			String companyadminname = req.getParameter(UserInfoFieldNames.COMPANY_ADMINNAME);
			String companybusiness = req.getParameter(UserInfoFieldNames.COMPANY_COMBUSINESS);
			String companybusinessremark = req.getParameter(UserInfoFieldNames.COMPANY_BUSINESSREMARK);
			
			if(CommonUtil.isStringEmpty(companyName)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,UserInfoFieldNames.COMPANY_NAME);
			}
			companyName = defaultCharacterConvert(companyName);
			if(!CommonUtil.isStringEmpty(companyAddress)){
				companyAddress = defaultCharacterConvert(companyAddress);
			}
			
			if(!CommonUtil.isStringEmpty(companyadminname)){
				companyadminname = defaultCharacterConvert(companyadminname);
			}
			if(!CommonUtil.isStringEmpty(companybusiness)){
				companybusiness = defaultCharacterConvert(companybusiness);
			}
			if(!CommonUtil.isStringEmpty(companyadminname)){
				companybusinessremark = defaultCharacterConvert(companybusinessremark);
			}
			
			ComEntity ce = null;
			ce = new ComEntity();
			ce.setAddress(companyAddress);
			ce.setEmail(companyEmail);
			ce.setName(companyName);
			ce.setPhone(companyPhone);
			ce.setAdmincode(companyadmincode);
			ce.setAdminname(companyadminname);
			ce.setCombusiness(companybusiness);
			ce.setBusinessremark(companybusinessremark);
			
			
			UserService userService = (UserService) getBean("userService");
			// 更新用户信息
			userService.saveUser(userEntity, ce);
			bci = userService.login(userEntity.getEmail(), null);
			subject.getSession().setAttribute(PortalConstants.USER_INFO_CONSTANTS, bci);
			session.setAttribute(PortalConstants.USER_INFO_CONSTANTS, bci);
			LOGGER.info("## SESSION ID02 : ["+session.getId()+"]");
			write(req, resp, "补充信息成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
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
		if (!CommonUtil.isStringEmpty(password)) {
			password = EncryptionUtil.base64Decode(password);
		}
		
		String qq = req.getParameter(UserInfoFieldNames.QQ);
		userEntity.setQq(qq);
		
		userEntity.setPassword(password);
		String realName = req.getParameter(UserInfoFieldNames.REALNAME);
		// 解析真实姓名
		if (!CommonUtil.isStringEmpty(realName)) {
			realName = defaultCharacterConvert(realName);
		}

		userEntity.setRealname(realName);
		String sex = req.getParameter(UserInfoFieldNames.SEX);
		if (!CommonUtil.isStringEmpty(sex)) {
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
		// 地址解码
		if (!CommonUtil.isStringEmpty(address)) {
			address = defaultCharacterConvert(address);
		}
		userEntity.setAddress(address);
		String remarks = req.getParameter(UserInfoFieldNames.REMARKS);
		// 备注解码
		if (!CommonUtil.isStringEmpty(remarks)) {
			remarks = defaultCharacterConvert(remarks);
		}
		userEntity.setRemark(remarks);
		String checkResult = CommonUtil.checkRequredParam(new String[] { UserInfoFieldNames.REALNAME },
				new String[] { userEntity.getRealname() });
		if (!CommonUtil.isStringEmpty(checkResult)) {
			throw new ParameterException(ExceptionType.NULL_NO_NEED, checkResult);
		}
		return userEntity;
	}
	
	
	/**
	 * 
	 * <p>Title ：requestLicense</p>
	 * Description：申请License
	 * @param req
	 * @param resp
	 * @throws ParameterException
	 * Author：Huasong Huang
	 * CreateTime：2015-4-23 下午02:39:31
	 */
	public void requestLicense(HttpServletRequest req,HttpServletResponse resp) throws ParameterException{
		
		BaseAccessInfo accessInfo = getUserInfo();
		if(null == accessInfo){
			throw new ParameterException("用户信息为空");
		}
		UserService userService = (UserService) getBean("userService");
		// 更新用户信息
		String licenseInfo = null;
		try {
			licenseInfo = userService.licenseService(accessInfo.getUserId());
			String email = accessInfo.getEmail();
			String filename = email.substring(0, email.indexOf("@"));
			filename += "_"+dateFormat.format(new Date());
			write(resp, filename,licenseInfo,true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			write(req, resp, e.getMessage(), null, false);
		}
	} 
	
	static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
}
