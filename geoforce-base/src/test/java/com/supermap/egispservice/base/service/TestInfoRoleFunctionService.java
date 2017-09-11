package com.supermap.egispservice.base.service;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.InfoRoleFunctionEntity;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestInfoRoleFunctionService extends TestCase {
	@Autowired
	private InfoRoleFunctionService infoRoleFunctionService;
	
	@Test
	public void testSave(){
		InfoRoleFunctionEntity entity=new InfoRoleFunctionEntity();
		entity.setRoleId("40288e9f4877e2ad014877e2b6ed0000");
		entity.setFunId("40288e9f48448d8a0148448d90770004");
		infoRoleFunctionService.save(entity);
	}
	
	@Test
	public void testFindByRoleId(){
		
		List<InfoRoleFunctionEntity>  list=infoRoleFunctionService.findByRoleId("40288e9f4877e2ad014877e2b6ed0000");
		System.out.println(list.size());
	}
}