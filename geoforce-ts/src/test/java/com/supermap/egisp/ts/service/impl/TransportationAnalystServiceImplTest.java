package com.supermap.egisp.ts.service.impl;

import net.sf.json.JSONArray;

import org.junit.Before;
import org.junit.Test;

import com.supermap.egisp.ts.pojo.Point;
import com.supermap.egisp.ts.pojo.TSAnalystResult;
import com.supermap.egisp.ts.service.TransportationAnalystService;


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TransportationAnalystServiceImplTest {

	private TransportationAnalystService service ;
	
	@Before
	public void init(){
		service = new TransportationAnalystServiceImpl();
	}
	
	@Test
	public void testPathAnalyst() {
		Point startPoint = new Point(116.60771,39.83307);
		Point endPoint = new Point(116.61017,39.83505);
		Point passPoint = new Point(116.60985,39.83284);
		
		TSAnalystResult result = service.pathAnalyst(startPoint, endPoint, new Point[]{passPoint});
		JSONArray array = JSONArray.fromObject(result.getPath());
		System.out.println(array.toString());
	}
	
	@Test
	public void test(){
		System.out.println("hello world");
	}
	
	
	

}
