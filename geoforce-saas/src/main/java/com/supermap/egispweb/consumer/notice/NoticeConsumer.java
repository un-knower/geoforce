package com.supermap.egispweb.consumer.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.common.Page;

public interface NoticeConsumer {
	/**
	 * 发送消息
	 * @param title
	 * @param content
	 * @param personIds
	 * @param userEntity
	 * @return
	 */
	public AjaxResult addNotice(String title,String content,String personIds,UserEntity userEntity);
	
	/**
	 * 消息列表
	 * @param request
	 * @param userEntity
	 * @param page
	 * @return
	 */
	public Page getNoticeList(HttpServletRequest request,UserEntity userEntity,Page page);
}
