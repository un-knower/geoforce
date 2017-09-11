package com.supermap.egispportal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

//import com.supermap.egispportal.entity.CompanyEntity;
//import com.supermap.egispportal.entity.UserEntity;
//import com.supermap.egispportal.entity.UserInfoFieldNames;
//import com.supermap.egispportal.exception.ParameterException;
//import com.supermap.egispportal.exception.ParameterException.ExceptionType;
//import com.supermap.egispportal.service.IUserService;
import com.supermap.egispportal.util.CommonUtil;
import com.supermap.egispportal.util.EncryptionUtil;
import com.supermap.egispservice.base.entity.ComEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.UserInfoFieldNames;
import com.supermap.egispservice.base.service.UserService;

public class RegistServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(UserSupServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		regist(req, resp);
	}
	
	/**
	 * 
	 * <p>Title ：regist</p>
	 * Description：注册
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-12-4 上午10:32:33
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
	
	
	private UserEntity parseUserParameter(HttpServletRequest req) throws ParameterException {
		UserEntity userEntity = new UserEntity();
		String username = req.getParameter(UserInfoFieldNames.USERNAME);
		userEntity.setUsername(username);
		// 需要对密码进行解密
		String password = req.getParameter(UserInfoFieldNames.PASSWORD);
		if (!CommonUtil.isStringEmpty(password)) {
			password = EncryptionUtil.base64Decode(password);
		}
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
		String checkResult = CommonUtil.checkRequredParam(new String[] { UserInfoFieldNames.USERNAME,
				UserInfoFieldNames.REALNAME, UserInfoFieldNames.MOBILEPHONE, UserInfoFieldNames.EMAIL },
				new String[] { userEntity.getUsername(), userEntity.getRealname(), userEntity.getMobilephone(),
						userEntity.getEmail() });
		if (!CommonUtil.isStringEmpty(checkResult)) {
			throw new ParameterException(ExceptionType.NULL_NO_NEED, checkResult);
		}
		return userEntity;
	}
	
}
