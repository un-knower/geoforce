package com.supermap.egispservice.base.dao;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.util.Md5Util;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestUserDao extends TestCase {
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testSaveUser(){
		for (int i = 0; i < 10; i++) {
			UserEntity u=new UserEntity();
			u.setAddress("四川省成都市天府软件园E6");
			Date d=new Date(System.currentTimeMillis());
			u.setCreateTime(d);
			//u.setCreateUser()
			//u.setDeptId()
			//u.setEid()
			u.setEmail("caobin@supermap.com");
			u.setFax("028-88888888");
			u.setMobilephone("13800138000");
			u.setPassword(Md5Util.md5("caobin"));
			u.setRealname("曹斌");
			u.setRemark("备注测试");
			u.setSex('m');//男人
			u.setSourceId((byte)1);//企业用户
			u.setStratusId((byte)2);//审核通过
			u.setTelephone("028-88888888");
			u.setUpdateTime(d);
			u.setUsername("caobin");
			u.setZipCode("610000");
			userDao.save(u);
		}
		
	}
	
}
