package com.supermap.egispportal.shiro.realm;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BaseAccessInfo;
import com.supermap.egispservice.base.service.UserService;

public class UserRealm extends AuthorizingRealm{

	@Resource
	private UserService userService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollections) {
		
		return null;
	}

	private static Logger LOGGER = Logger.getLogger(UserRealm.class);
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		String loginInfo = userToken.getUsername();
		LOGGER.info("## 用户["+loginInfo+"]登录");
//		String password = new String(userToken.getPassword());
//		String tempPassword =  EncryptionUtil.base64Decode(password);
		BaseAccessInfo info = null;
		try {
			info = userService.login(loginInfo, null);
		} catch (ParameterException e) {
			LOGGER.error("用户["+loginInfo+"]登录失败"+e.getMessage(), e);
			throw new AuthenticationException(e.getMessage());
		}
		return new SimpleAuthenticationInfo(info, "", getName());
	}

}
