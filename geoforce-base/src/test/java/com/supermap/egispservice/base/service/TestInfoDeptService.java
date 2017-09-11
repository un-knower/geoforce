package com.supermap.egispservice.base.service;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.InfoDeptEntity;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestInfoDeptService extends TestCase {
	@Autowired
	private InfoDeptService infoDeptService;
	
	@SuppressWarnings("unchecked")
	@Test
	public void getChildDepts(){
		System.out.println(infoDeptService.getChildDepts("40288e9f488187790148818781ea0000").size()+"***********************"); 
	}
	
	@Test
	public void testGetDepts() {
		//Map<String, Object> map = infoDeptService.getDepts("", 1, 1, "");
		//System.out.println(map.size());
	}
	
	@Test
	public void testFindById() {
		String id = "40288ca7488b6c1f01488b94e9910002";
		System.out.println("======================");
		InfoDeptEntity entity = infoDeptService.findDeptById(id);
		System.out.println(entity);
		System.out.println(entity.getName());
		System.out.println("======================");
	}
}