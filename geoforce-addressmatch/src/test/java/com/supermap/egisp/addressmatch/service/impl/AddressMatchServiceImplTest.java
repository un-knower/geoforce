package com.supermap.egisp.addressmatch.service.impl;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egisp.addressmatch.beans.AddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class AddressMatchServiceImplTest {

	@Autowired
	@Qualifier("addressMatchService")
	private IAddressMatchService service;
	
	@Test
	public void testAddressMatchStringStringString() {
		AddressMatchResult amr = service.addressMatch("1", "北京市东城区安定门街道   宝钞胡同56号", "SMC");
		System.out.println(amr.getX()+","+amr.getY());
		
	}


}
