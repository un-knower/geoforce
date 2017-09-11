package com.supermap.egispweb.consumer.pic.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.common.Constant;
import com.supermap.egispweb.consumer.pic.PicConsumer;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPic;
import com.supermap.lbsp.provider.service.pic.PicService;
@Component("picConsumer")
public class PicConsumerImpl implements PicConsumer {
	@Reference(version="2.5.3")
	private PicService picService;

	@Override
	public List<PersonPic> getPicList(String type, String id) {
		HashMap<String,Object> hm = new HashMap<String, Object>();
		hm.put("type", type);
		hm.put("foreignId", id);
		List<PersonPic> personPicList = picService.queryPersonPic(null, hm);
		for (int i = 0; i < personPicList.size(); i++) {
			PersonPic pic = personPicList.get(i);
			if(pic.getUrl() != null)
			pic.setUrl(Constant.PERSON_IMG_PATH+pic.getUrl());
		}
		return personPicList;
	}
}
