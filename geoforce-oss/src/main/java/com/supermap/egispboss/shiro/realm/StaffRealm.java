package com.supermap.egispboss.shiro.realm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

//import com.supermap.egispboss.permission.pojo.BaseStaffAccessInfo;
//import com.supermap.egispboss.permission.service.IStaffService;
import com.supermap.egispboss.shiro.StaffPermission;
import com.supermap.egispservice.base.pojo.BaseStaffAccessInfo;
import com.supermap.egispservice.base.service.IStaffService;

public class StaffRealm extends AuthorizingRealm {

	private static Logger LOGGER = Logger.getLogger(StaffRealm.class);
	@Resource
	private IStaffService staffService;

	
	
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
		LOGGER.info("## 获取权限");
		BaseStaffAccessInfo info = (BaseStaffAccessInfo) token.getPrimaryPrincipal();
		SimpleAuthorizationInfo resultInfo = new SimpleAuthorizationInfo();
		resultInfo.addRoles(info.getRoleNames());
		List<Permission> permissionList = convert(info.getPermissions());
		resultInfo.addObjectPermissions(permissionList);
		return resultInfo;
	}

	private List<Permission> convert(Map<String,String> permissions){
		List<Permission> pList = new ArrayList<Permission>();
		if(null != permissions){
			Set<String> names = permissions.keySet();
			Iterator iterator = names.iterator();
			while(iterator.hasNext()){
				String code = (String) iterator.next();
				String url = permissions.get(code);
				pList.add(new StaffPermission(code,url));
			}
		}
		return pList;
	}
	
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		LOGGER.info("## 登录验证。。。");
		UsernamePasswordToken upToken = (UsernamePasswordToken)token;
		String username = upToken.getUsername();
		String password = new String(upToken.getPassword());
		BaseStaffAccessInfo info = null;
		try{
			info = staffService.login(username, password);
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			throw new AuthenticationException(e.getMessage());
		}
		return new SimpleAuthenticationInfo(info, password, getName());
	}

}
