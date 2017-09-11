package com.supermap.egispservice.reverse.service;

import com.supermap.egispservice.base.entity.AddressInfo;
import com.supermap.egispservice.base.entity.PointParam;
import com.supermap.egispservice.base.entity.PointXY;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestReverseServiceImpl extends TestCase {
	@Autowired
	private IReverseService reverseService;
	
	
	@Autowired
	private IGeoQueryService geoQueryService;

	 @Test
	 public void testPointToAddress() throws Exception {
		 List<PointParam> pointParams=new ArrayList<PointParam>();
         PointParam pointParam=new PointParam();
         pointParam.setCode("1");
         pointParam.setPoint(new PointXY(13513691.6677295, 3650858.65915608));
         pointParams.add(pointParam);

         PointParam pointParam1=new PointParam();
         pointParam1.setCode("2");
         pointParam1.setPoint(new PointXY(13542773.1388586, 3708968.70758491));
         pointParams.add(pointParam1);

         List<AddressInfo> addressInfos = reverseService.pointToAddress(pointParams,"SMC");
         for (int i = 0; i < addressInfos.size(); i++) {
             AddressInfo addressInfo = addressInfos.get(i);
             System.out.println(addressInfo);
//             System.out.println(addressInfo.getCode());
//             System.out.println(addressInfo.getPoint());
         }
     }
	 
	 
	 @Test
	 public void testBatchQueryNearestPOI(){
		 
		 List<PointParam> pointParams=new ArrayList<PointParam>();
         PointParam pointParam=new PointParam();
         pointParam.setCode("1");
         pointParam.setPoint(new PointXY(13513691.6677295, 3650858.65915608));
         pointParams.add(pointParam);

         PointParam pointParam1=new PointParam();
         pointParam1.setCode("2");
         pointParam1.setPoint(new PointXY(13542773.1388586, 3708968.70758491));
         pointParams.add(pointParam1);

         List<AddressInfo> addressInfos = geoQueryService.batchQueryNearestPOI(pointParams,"SMC");
         for (int i = 0; i < addressInfos.size(); i++) {
             AddressInfo addressInfo = addressInfos.get(i);
             System.out.println(addressInfo);
//             System.out.println(addressInfo.getCode());
//             System.out.println(addressInfo.getPoint());
         }
		 
		 
	 }
	 



}
