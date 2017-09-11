package com.supermap.egispservice.base.service.impl;

import org.springframework.stereotype.Service;

import com.supermap.egispservice.base.service.DemoService;
@Service
public class DemoServiceImpl implements DemoService {

	@Override
	public String sayHello(String name) {
		String string=" TODO Auto-generated method stub";
		System.out.println("egisp server say:"+string);
		return string;
	}

}
