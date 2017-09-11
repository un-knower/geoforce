package com.supermap.egispweb.service;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
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
		/*Map<String, Object> map = infoDeptService.getDepts("", 1, 1, "");
		System.out.println("=========================");
		for(String key : map.keySet()) {
			System.out.println(key);
			if("rows".equals(key)) {
				List<InfoDeptEntity> list = (List<InfoDeptEntity>)(map.get(key));
				for(InfoDeptEntity ide : list) {
					System.out.println(ide.getId());
					System.out.println(ide.getName());
					//System.out.println(ide.getParentInfoDeptEntity());
				}
			} else {
				System.out.println(map.get(key));
			}
		}
		System.out.println("=========================");*/
	}
	
	@Test
	public void testFindById() {
		String parentId = "";
		System.out.println(infoDeptService.findDeptById(parentId));
	}
}