package com.supermap.egispweb.consumer.pic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPic;

public interface PicConsumer {
	
	public List<PersonPic> getPicList(String type,String id);

}
