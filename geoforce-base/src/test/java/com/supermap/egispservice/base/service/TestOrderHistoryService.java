package com.supermap.egispservice.base.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.OrderBaseEntity;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestOrderHistoryService extends TestCase {
	@Autowired
	private IOrderHistoryService orderHistoryService;
	
	@Test
	public void testGetHistoryOrders() {
		//Map<String, Object> map = infoDeptService.getDepts("", 1, 1, "");
		//System.out.println(map.size());
		Map<String, Object> map = orderHistoryService.getHistoryOrders("", "", "", "", "40288e9f48627238014862723e870000", 1, 10, "asc");
		List<OrderBaseEntity> list = (List<OrderBaseEntity>) map.get("rows");
		for(OrderBaseEntity o : list) {
			System.out.println(o.getAddress());
		}
	}
}