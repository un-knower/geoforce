package com.supermap.egispservice.base.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.pojo.NetPointInfoResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class PointServiceImplTest {

	@Autowired
	private PointService pointService;
	
//	@Test
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
	
	@Test
	public void mytest(){
		Map<String, Object> map=this.pointService.queryAllByAdmincodeForConverge("40288e9f483f48e501483f48eb060000", 
				null, null, null, "40288e9f48625c010148625c07160000", 
				"00001000", -1, 10, null,"3201",false);
		List<NetPointInfoResult> npis=(List<NetPointInfoResult>) map.get("records");
		System.out.println("查询：" + npis.size());
		//List<String> ids=this.pointservice.getAllProcessingPointByUserid("40288fd94add378d014add3823f10063");
	
		/*List<PointExtcolValEntity> vallist=this.pointextcolvalservice.findByPointidOrUserid("", "40288fd94add378d014add3823f10063");
		System.out.println("查询：" + vallist.size());*/
		
		//PointEntity point=this.pointservice.queryById("ff8080815185d05d01518a9502210471");
		/*Map<String,Object> list= this.pointservice.queryFailedPoints("40288fd94add378d014add3823f10063");
		List<NetPointInfoResult> npis=(List<NetPointInfoResult>) list.get("records");
		System.out.println(npis.size());*/
		
		/*List<String> processing=this.pointservice.getAllProcessingPointByUserid("40288e9f483f48e501483f48eb060000");
		System.out.println(processing.size());*/
	}

}
