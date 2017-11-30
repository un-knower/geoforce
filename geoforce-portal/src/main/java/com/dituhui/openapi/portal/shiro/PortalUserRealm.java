package com.dituhui.openapi.portal.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.dituhui.openapi.base.service.IPortalUserService;
import com.dituhui.openapi.entity.PortalUser;

@Component
public class PortalUserRealm extends AuthorizingRealm {

	private IPortalUserService portalUserService;

	public void setPortalUserService(IPortalUserService portalUserService) {
		this.portalUserService = portalUserService;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upt = (UsernamePasswordToken) token;
		PortalUser pUser = portalUserService.findByPhoneAndPassword(
				upt.getUsername(), new String(upt.getPassword()));
		if (pUser != null) {
			return new SimpleAuthenticationInfo(pUser, upt.getCredentials(),
					getName());
		}
		return null;
	}

}
