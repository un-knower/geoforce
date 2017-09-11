package com.supermap.egispservice.base.dao;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.InfoDeptEntity;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestDeptDao extends TestCase {
	@Autowired
    private InfoDeptDao infoDeptDao;
	
	@Test
	public void testFindById(){
		String id = "40288e35488896bc01488897c9030000";
		System.out.println("======================");
		InfoDeptEntity entity = infoDeptDao.findOne(id);
		System.out.println(entity);
		System.out.println(entity.getName());
		System.out.println("======================");
	}
	
}
