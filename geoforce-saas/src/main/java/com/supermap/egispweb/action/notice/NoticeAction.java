package com.supermap.egispweb.action.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.common.Page;


//@Controller
public class NoticeAction extends BaseAction{
	static Logger logger = Logger.getLogger(NoticeAction.class.getName());
	/**
	 * 调整到发送消息页面
	 * @return
	 */
	
	@RequestMapping(value="/com/supermap/toNotice")
	public String toNotice(){
		return "notice/notice";
	}
	/**
	 * 发送消息
	 * @param title
	 * @param content
	 * @param personIds
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/sendNotice")
	@ResponseBody
	public AjaxResult addNotice(String title,String content,String personIds,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		if(userEntity == null){
			return new AjaxResult((short)1,"请用户登录");
		}
		return noticeConsumer.addNotice(title, content, personIds, userEntity);
	}
	@RequestMapping(value="/com/supermap/toNoticeList")
	public String toList(){
		return "notice/noticeList";
	}
	/**
	 * 返回列表
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/noticeList")
	@ResponseBody
	public Page getNoticeList(HttpServletRequest request, HttpSession session){
		UserEntity userEntity = getUserSession(session);
		Page page = getPage(request);
		if(userEntity == null){
			return page;
		}
		return noticeConsumer.getNoticeList(request, userEntity, page);
	}
}
