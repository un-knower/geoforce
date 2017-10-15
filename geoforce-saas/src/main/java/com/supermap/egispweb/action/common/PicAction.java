package com.supermap.egispweb.action.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPic;
//@Controller
public class PicAction extends BaseAction{
	/**
	 * 跟据存类型和外键id得到图的列表
	 * @param type
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/picList")
	@ResponseBody
	public List<PersonPic> getPicList(String type,String id,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		List<PersonPic> list = new ArrayList<PersonPic>();
		if(userEntity == null){
			return list;
		}
		return picConsumer.getPicList(type, id);
	}
	
}