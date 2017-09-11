package com.supermap.egispservice.base.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.ComEntity;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.util.Md5Util;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestUserService extends TestCase {
	@Autowired
	private UserService userService;
	
	@Autowired
	IOrderService orderService;
	
	@SuppressWarnings("unchecked")
//	@Test
	public void testGetUsers(){
		Map<String, Object> list=userService.getUsers("40288e9f483f48e501483f48eb060000","40288e9f48625c010148625c07160000","40288e9f48627238014862723e870000",(byte)3,"", null, new DateTime(2010,9,4,14,10,10).toDate(), new DateTime(2114,9,4,14,41,42).toDate(), 1, 10, "auto");
		System.out.println(((List<UserEntity>)(list.get("rows"))).get(0).getCreateTime());
	}
	
//	@Test
	public void testFindByUsername(){
		System.out.println(userService.findByUsername("caobin","40288e9f48625c010148625c07160000").getDeptId().getName());
	}
	
//	@Test
	public void testSave100Users(){
		Date d = new Date(System.currentTimeMillis());
		for (int i = 0; i < 100; i++) {
			String userString="wcxtest"+i;
			UserEntity u=new UserEntity();
			u.setUsername(userString);
			u.setRealname("张三"+i);
			u.setPassword(Md5Util.md5(userString));
			InfoDeptEntity deptEntity = new InfoDeptEntity();
			deptEntity.setId("f9a8d6684a8b45f3014a8ef5e7e90003");
			u.setDeptId(deptEntity);
			u.setAddress("四川成都华阳南湖公园");
			u.setMobilephone("13800138000");
			u.setZipCode("610000");
			u.setEmail(userString+"@supermap.com");
			ComEntity comEntity = new ComEntity();
			comEntity.setId("40288e9f48625c010148625c07160000");
			u.setEid(comEntity);
			
			u.setCreateTime(d);
			u.setCreateUser("40288e9f483f48e501483f48eb060000");
			u.setSex((char)1);
			u.setSourceId((byte) 3);// 自助式
			u.setStratusId((byte) 1);// 正常
			u.setUpdateTime(d);
			userService.saveUser(u, userString);
			System.out.println(i);
		}
		
	}
	
//	@Test
	public void TestfindIfUserReadLastLogs(){
		System.out.println(this.userService.findIfUserReadLastLogs("40288e9f483f48e501483f48eb060000"));
	}
	
//	@Test
	public void TestsaveSysUpdateLogUser(){
		this.userService.saveSysUpdateLogUser("40288e9f483f48e501483f48eb060000");
	}
	
	
//	@Test
	public void testPortalService(){
		System.out.println(this.userService.queryByName("juannyoh22"));
	}
	
	@Test
	public void deleteOrders(){
		this.orderService.deleteById("f9a8d668490da5fa01493cc5f85d0126");
		this.orderService.deleteById("f9a8d668490da5fa01493cc3f13b0120");
	}
}