package com.supermap.egispboss.shiro.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import com.supermap.egispboss.util.CommonUtil;

public class PasswordMatcher implements CredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		UsernamePasswordToken usernameToken = null;
		if(token instanceof UsernamePasswordToken){
			usernameToken = (UsernamePasswordToken) token;
			String submitPassword = new String(usernameToken.getPassword());
			if(CommonUtil.isStringEmpty(submitPassword)){
				return false;
			}
//			submitPassword = EncryptionUtil.md5Encry(src, salt)
			
			
		}
		return false;
	}

}
