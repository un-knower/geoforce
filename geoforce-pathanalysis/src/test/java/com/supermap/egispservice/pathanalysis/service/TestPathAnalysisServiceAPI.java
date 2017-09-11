package com.supermap.egispservice.pathanalysis.service;

import com.supermap.egispservice.base.entity.PointXY;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.pathanalysis.constant.Config;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestPathAnalysisServiceAPI extends TestCase {
	@Autowired
	private IPathAnalysisService pathAnalysisService;
	@Autowired
	private Config config;

	@Test
	public void testAPI() throws Exception{
		PointXY start=new PointXY(116.4701866000004,39.743744600000056);
//		Point start=new Point(116.58644078423676,39.83206906700096);

		List<PointXY> passPoints = new ArrayList<PointXY>();
//		passPoints.add(new Point(116.4701866000004,39.743744600000056));
//		passPoints.add(new Point(116.21715790000026,39.76527889999998));
//		passPoints.add(new Point(116.31024999999956,39.85123000170635));
//		passPoints.add(new Point(116.45638000000028,39.87724000170665));
//		passPoints.add(new Point(116.37167000000026,40.01331000170795));

//		Point end=new Point(116.41253309999993,39.996346300000006);
		PointXY end=new PointXY(116.21715790000026,39.76527889999998);


		pathAnalysisService.bestPathAnalyst(start, end, passPoints, true, WeightNameType.MileAndTruck);

	}
}
