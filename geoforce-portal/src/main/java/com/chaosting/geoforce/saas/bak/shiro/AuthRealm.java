package com.chaosting.geoforce.saas.bak.shiro;

import com.alibaba.fastjson.JSON;
import com.chaosting.geoforce.saas.bak.controller.util.JedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.stereotype.Component;

//import com.dituhui.openapi.base.service.IApplicationService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthRealm extends AuthorizingRealm {

	private JedisUtil jedisUtil;

	//private IApplicationService applicationService;

	public void setJedisUtil(JedisUtil jedisUtil) {
		this.jedisUtil = jedisUtil;
	}

	/*public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}*/

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
/*System.out.println("AuthRealm doGetAuthorizationInfo");
		Application application = (Application)getAvailablePrincipal(principals);
		//TODO WebUtils.toHttp(request)
		ShiroHttpServletRequest request = (ShiroHttpServletRequest)((WebSubject)SecurityUtils.getSubject()).getServletRequest();*/
		SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
		/*for(Api api : application.getApis()) {
			// TODO 后续all包含CRUD多种权限      
			// 需要考虑/district、/district/getAdmincodesByName这种情况对于startsWith的影响
			if(request.getServletPath().startsWith(api.getRequestPath())) {
				sai.addStringPermission("api:all");
				break;
			}
		}*/
		return sai;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		/*UsernamePasswordToken upt = (UsernamePasswordToken) token;
		if(upt.getUsername() == null) {
			throw new AccountException("没有传key");
		}
		//  缓存
		Application application = null;
		String applicationValue = jedisUtil.get(upt.getUsername());

		if (null != applicationValue && !"".equals(applicationValue)) {
			application = com.alibaba.fastjson.JSON.parseObject(applicationValue, Application.class);
		}
		if (application == null) {
			application = applicationService.findByAppKey(upt.getUsername());
			if (null != application) {
				jedisUtil.set(application.getAppKey(), JSON.toJSONString(application));
			}
		}


		if (application == null) {
			throw new UnknownAccountException("数据库里没有传入的key");
		}
		return new SimpleAuthenticationInfo(application, upt.getPassword(), getName());*/
		return new SimpleAuthenticationInfo();
	}

//	private JsonConfig getJsonConfig() {
//		JsonConfig jsonConfig = new JsonConfig();
//		Map<String, Class> classMap = new HashMap<>();
//		classMap.put("apis", Api.class);
//		classMap.put("groupSet", GroupSet.class);
//		classMap.put("requestParameters", RequestParameter.class);
//		classMap.put("serviceParameters", ServiceParameter.class);
//		classMap.put("errorCodeSamples", ErrorCodeSample.class);
//		jsonConfig.setClassMap(classMap);
//		jsonConfig.setExcludes(new String[]{"resultSample"});
//		return jsonConfig;
//	}

}
