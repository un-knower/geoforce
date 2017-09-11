package com.supermap.egispservice.pathplan.dao;

import java.io.File;
import java.util.Date;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.ComEntity;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.RouteTaskEntity;
import com.supermap.egispservice.base.entity.UserEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestRouteTaskDao extends TestCase {
	@Autowired
	private RouteTaskDao routeTaskDao;

	@Test
	public void testSave() {
		RouteTaskEntity o = new RouteTaskEntity();
		o.setAreaId("11111111111");
		o.setConsumeTime(10);
		o.setCreateTime(new Date());
		o.setDeleteFlag(Byte.valueOf("0"));
		o.setDeliveryEndTime(new Date());// 自定义配送结束时间
		o.setDeliveryStartTime(new Date());// 自定义配送开始时间
		InfoDeptEntity deptEntity = new InfoDeptEntity();
		deptEntity.setId("40288e9f48627238014862723e870000");
		o.setDepartmentId(deptEntity);
		ComEntity com = new ComEntity();
		com.setId("40288e9f48625c010148625c07160000");
		o.setEnterpriseId(com);
		o.setOrderCount(30);
		o.setPlanTime(new DateTime(2014, 10, 20, 15, 30, 42).toDate());
		o.setPlanTypeId(Byte.valueOf("1"));
		o.setTaskStatusId(Byte.valueOf("1"));
		UserEntity u = new UserEntity();
		u.setId("40288e9f483f48e501483f48eb060000");
		o.setUserId(u);

		routeTaskDao.save(o);
		o.setResultPath(o.getId() + File.separator + "path.json");
		routeTaskDao.save(o);// 更新结果路径
	}
}
