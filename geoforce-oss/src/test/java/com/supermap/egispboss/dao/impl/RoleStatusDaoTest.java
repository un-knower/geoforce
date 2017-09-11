/*package com.supermap.egispboss.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispboss.dao.IRoleStatusDao;
import com.supermap.egispboss.permission.entity.RoleStatusEntity;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class RoleStatusDaoTest {

	@Autowired
	private IRoleStatusDao statusDao;
	
	@Test
	@Transactional
	public void testAdd() {
		RoleStatusEntity rse = new RoleStatusEntity();
		rse.setStatus("0");
		statusDao.add(rse);
	}

}
*/