package com.supermap.egispservice.geocoding.service.impl;


import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.geocoding.service.IGeocodingService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class GeocodingServiceImplTest {

	private IGeocodingService service = new GeocodingServiceImpl();
	
	@Test
	public void testGetAdminElement() {
		
		List<Map<String,Object>> list = service.getAdminElement("650000", 2);
		for(Map<String,Object> map : list ){
			System.out.println(map);
		}
		
	}

}
