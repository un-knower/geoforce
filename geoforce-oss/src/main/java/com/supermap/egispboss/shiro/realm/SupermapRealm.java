package com.supermap.egispboss.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

public class SupermapRealm implements Realm {

	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username=(String) token.getPrincipal();
		String password=new String((char [])token.getCredentials());
		//TODO 获取数据库中的用户名 密码
		if (!username.equals("caobin")) {
			throw new UnknownAccountException();
		}
		if (!password.equals("123456")) {
			throw new IncorrectCredentialsException();
		}
		return new SimpleAuthenticationInfo(username, password, getName());
	}

	@Override
	public String getName() {
		return "Supermap";
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}

}
