package com.supermap.egispservice.area.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.exceptions.AreaException;
import com.supermap.egispservice.area.service.IAreaService;

public class AreaServiceMainTest {

	ClassPathXmlApplicationContext context = null;
	private IAreaService areaService;
	
	@Before
	public void init(){
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		areaService = (IAreaService) context.getBean("areaService");
	}
	
//	
//	@Test
//	public void testMain() {
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
//		context.start();
//		System.out.println("按任意键退出");
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	

	@Test
	public void testQueryByEnOrDe() throws AreaException{
		
//		List<AreaEntity> aes = this.areaService.queryByEnOrDe(null, null, "8a04a77b4cbc865c014cc0b8df9c0019", "00090000", true);
//		System.out.println(aes.size());
		
		try {
			//List<Map<String,Object>> maplist=this.areaService.queryAreaCountByParm("8a04a77b4cbc865c014cc0b8df9c0019", "00", null, null);
			//System.out.println(maplist);
		
//		boolean x=this.areaService.updateAreaAttribution("30d404ae01244bdfb013cf0c98ed267a", null, null, null, null);
			boolean ff=this.areaService.updateAreaOwner("eb527df19f1c44bea1945083e8c54217", "40288f70574c1c130157653c18130005", "8a04a77b4e4949f0014e855f638d084b", "002820000001");
			System.out.println(ff);
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
