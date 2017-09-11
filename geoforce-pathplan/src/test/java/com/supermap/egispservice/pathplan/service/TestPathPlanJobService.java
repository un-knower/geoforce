package com.supermap.egispservice.pathplan.service;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import com.supermap.egispservice.base.entity.GroupType;
//import com.supermap.egispservice.base.entity.RouteTaskEntity;
//import com.supermap.egispservice.pathplan.dao.RouteTaskDao;
//import com.supermap.egispservice.pathplan.service.IPathPlanJobService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestPathPlanJobService extends TestCase {
//	@Autowired
//	private RouteTaskDao routeTaskDao;
//
//	@Autowired
//	private IPathPlanJobService pathPlanJobService;

	@Test
	public void testNewJob() throws Exception {
//		RouteTaskEntity routeTaskEntity = routeTaskDao.findOne("40288e9f492c6e3b01492c6e95c20000");
//		pathPlanJobService.addJob(routeTaskEntity,GroupType.NoneGroup);
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
