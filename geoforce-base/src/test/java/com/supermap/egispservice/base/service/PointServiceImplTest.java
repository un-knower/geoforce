/*package com.supermap.egispservice.base.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.PointEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class PointServiceImplTest {

	@Autowired
	private PointService pointService;
	
	@Test
	public void testImportNetPoints() {
		List<PointEntity> list = new ArrayList<PointEntity>();
		PointEntity pe01 = new PointEntity();
		pe01.setName("北京市朝阳区立水桥分店");
		pe01.setAddress("北京北京市朝阳区立水桥汤立路");
		pe01.setDutyName("刘可");
		pe01.setDutyPhone("13878963134");
		list.add(pe01);
		
		PointEntity pe02 = new PointEntity();
		pe02.setName("北京市昌平区天通苑分店");
		pe02.setSmx(BigDecimal.valueOf(121.487899));
		pe02.setSmy(BigDecimal.valueOf(31.249162));
		pe02.setDutyName("胡年");
		pe02.setDutyPhone("13878963134");
		list.add(pe02);
		
		try {
			pointService.importNetPoints(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
*/