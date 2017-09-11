package com.supermap.egispservice.base.service;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.InfoRoleEntity;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestInfoRoleService extends TestCase {
	@Autowired
	private InfoRoleService infoRoleService;
	
	@Test
	public void testSave(){
		InfoRoleEntity entity=new InfoRoleEntity();
		entity.setCreateUserid("40288e9f48625c010148625c07160000");
		entity.setEid("40288e9f48625c010148625c07160000");
		entity.setName("销售A级");
		entity.setOperDate(new Date());
		entity.setRemark("备注");
		infoRoleService.save(entity);
	}
	@Test
	public void testGetRoles(){
		Map<String, Object> list=infoRoleService.getRoles("40288e354868e649014868e674790000","40288e9f48625c010148625c07160000","40288e9f48627238014862723e870000", "", new DateTime(2010,9,4,14,10,10).toDate(), new DateTime(2114,9,4,14,41,42).toDate(), 1, 10, "auto");
		System.out.println(list);
	}
}