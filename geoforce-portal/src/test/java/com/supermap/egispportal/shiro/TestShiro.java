package com.supermap.egispportal.shiro;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class TestShiro extends TestCase {
	@Test
	public void testShiro(){
		Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager=factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken("caobin","123456");
		try {
			subject.login(token);
		} catch (Exception e) {
			Logger.getLogger(TestShiro.class).error("登陆异常");
		}
		assertEquals(true, subject.isAuthenticated());
		subject.logout();
	}
	@Test
	public void testCustomShiro(){
		Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro-realm.ini");
		SecurityManager securityManager=factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken("caobin","123456");
		try {
			subject.login(token);
		} catch (Exception e) {
			Logger.getLogger(TestShiro.class).error("自定义realm登陆异常");
		}
		assertEquals(true, subject.isAuthenticated());
		subject.logout();
	}
}
