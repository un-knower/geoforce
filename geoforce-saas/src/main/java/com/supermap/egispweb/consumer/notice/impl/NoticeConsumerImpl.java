package com.supermap.egispweb.consumer.notice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.consumer.notice.NoticeConsumer;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.info.Dept;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonNotice;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonNoticeForeign;
import com.supermap.lbsp.provider.service.dept.DeptService;
import com.supermap.lbsp.provider.service.notice.NoticeService;
import com.supermap.lbsp.provider.service.notice.PersonNoticeService;


@Component("noticeConsumer")
public class NoticeConsumerImpl implements NoticeConsumer{
	static Logger logger = Logger.getLogger(NoticeConsumerImpl.class.getName());
	@Reference(version="2.5.3")
	private NoticeService noticeService;
	@Reference(version="2.5.3")
	private DeptService deptService;
	@Reference(version="2.5.3")
	private PersonNoticeService personNoticeService;
	@Override
	public AjaxResult addNotice(String title, String content,String personIds,UserEntity userEntity) {
		if(StringUtils.isBlank(title)){
			return new AjaxResult((short)0,"请填写标题");
		}
		if(StringUtils.isBlank(content)){
			return new AjaxResult((short)0,"请填写内容");
		}
		if(StringUtils.isBlank(personIds)){
			return new AjaxResult((short)0,"请选择发送人");
		}
		PersonNotice notice = new PersonNotice();
		notice.setContent(content);
		notice.setDeptId(userEntity.getDeptId().getId());
		notice.setPubDate(new Date());
		notice.setPubUser(userEntity.getId());
		notice.setPubWay((short)1);
		notice.setType((short)1);
		notice.setTitle(title);
		PersonNotice noticedb = noticeService.addNotice(notice);
		if(noticedb == null){
			return new AjaxResult((short)0,"发送失败");
		}
		String[] personIdArr = personIds.split(",");
		StringBuffer personNoticeIds = new StringBuffer();
		List<String> errList = new ArrayList<String>();
		for (int i = 0; i < personIdArr.length; i++) {
			PersonNoticeForeign  personNotice = new PersonNoticeForeign();
			personNotice.setNoticeId(noticedb.getId());
			personNotice.setPersonId(personIdArr[i]);
			personNotice.setStatus((short)0);//默认未发送网关发送后改变状态
			PersonNoticeForeign obj = personNoticeService.addPersonNotice(personNotice);
			if(obj == null){
				errList.add(personIdArr[i]);
			}else{
				personNoticeIds = personNoticeIds.append(obj.getId()).append(",");
			}
		}
		personNoticeIds.delete(personNoticeIds.length()-1, personNoticeIds.length());
		//调用发送接口
		noticeService.sendMsg(noticedb.getId(), personNoticeIds.toString());
		if(errList.size()>0){
			return new AjaxResult((short)0,"有"+errList.size()+"条发送失败"); 
		}
		return new AjaxResult((short)1,"发送成功");
	}
	@Override
	public Page getNoticeList(HttpServletRequest request,UserEntity userEntity, Page page) {
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		String name = request.getParameter("name");
		String deptcode = request.getParameter("deptCode");
		if(StringUtils.isBlank(deptcode)){
			deptcode = userEntity.getDeptId().getCode();
		}
		List<String> detptIds =  deptcodeConvertDeptId(deptcode);
		if(detptIds!= null && detptIds.size()>0){
			hm.put("deptIds", detptIds);
		}
		if(StringUtils.isNotBlank(name)){
			hm.put("name", name);
			logger.info("license is "+name);
		}
		try {
			page = noticeService.queryNotice(page, hm);
		} catch (Exception e) {
			return page;
		}
		
		return page;
	}
	/**
	 * 部门code转化id
	 * @param deptcode
	 * @return
	 */
	private List<String> deptcodeConvertDeptId( String deptcode){
		
		if(deptcode != null){
			HashMap<String,Object> deptHm = new HashMap<String, Object>();
			deptHm.put("code", deptcode);
			List<String> listDeptId = new ArrayList<String>();
			List listDept = this.deptService.queryDept(null, deptHm);
			if(listDept != null && listDept.size()>0){
				for (int i = 0; i < listDept.size(); i++) {
					Dept dept = (Dept)listDept.get(i);
					listDeptId.add(dept.getId());
				}
				return listDeptId;
			}
			
			return null;
			
		}
		return null;
		
	}

}
