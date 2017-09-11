/*package com.supermap.egispboss.service;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispboss.entity.AppKeyEntity;
import com.supermap.egispboss.entity.CompanyEntity;
import com.supermap.egispboss.entity.UserEntity;
import com.supermap.egispboss.exception.ParameterException;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestUserService extends TestCase {
	@Autowired
	private IUserService userService;
	@Resource
	private IOrderService orderService;
	
	
	@Test
	public void testSaveUser() throws ParameterException{
		UserEntity user=new UserEntity();
//		user.setId(1);
		user.setAddress("四川成都天府软件园");
//		user.setCompanyName("超图软件22");
		user.setEmail("webmaster@supermap5.com");
		user.setMobilephone("13800138000");
		user.setPassword("supermap124");
		user.setRealName("超人");
		user.setTelephone("028-888888");
		user.setUsername("wangmazi05");
		user.setZipCode("610000");
		CompanyEntity ce = new CompanyEntity();
		ce.setName("超图软件");
		userService.saveUser(user,ce);
	}
	
	@Test
	public void testDeleteUser(){
		userService.deleteUserById(1+"");
	}
	
	@Test
	public void testAddAppKey(){
		AppKeyEntity keyEntity = new AppKeyEntity();
		keyEntity.setRemarks("hello world");
		userService.addAppKey(1+"", keyEntity);
	}
	
	@Test
	public void testQueryById(){
		userService.findUserById(1+"");
	}
	
	
}
*/