package com.supermap.egispweb.consumer.personSign.Impl;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.personSign.PersonSignConsumer;
import com.supermap.lbsp.provider.common.Page;

import com.supermap.lbsp.provider.service.personSign.PersonSignService;

@Component("personSignConsumer")
public class PersonSignConsumerImpl implements PersonSignConsumer {
	
	@Reference(version="2.5.3")
	private PersonSignService personSignService;
	
	@Override
	public Page pagePersonSign(Page page, HashMap<String, Object> hm) {
		try {
			return this.personSignService.pagePersonSign(page, hm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	

}
