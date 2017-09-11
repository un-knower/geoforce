package com.supermap.lbsp.provider.service.notice;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonNotice;

public interface NoticeService {
	/**
	 * 
	 * @param 增加 
	 * @return
	 */
	public PersonNotice addNotice(PersonNotice notice);
	/**
	 * 
	 * @param 修改
	 * @return
	 */
	public int upadateNotice(PersonNotice noticeTask) ;
	
	/**
	 * 根据主键ID查询
	 * 
	 * @param id
	 * @return
	 */
	public PersonNotice getNotice(String id);
	/**
	 * 删除
	 * @param personInfo
	 * @return
	 */
	public int delNotice(PersonNotice noticeTask);
	/**
	 * 查询
	 * @return
	 */
	public Page queryNotice(Page page, HashMap<String, Object> hm) throws Exception;
	
	/**
	 *  
	 */
	public List<Object[]> getNoticeListByPerson(Page page, HashMap<String, Object> hm);
	public Page getNoticeListByPersonPage(Page page, HashMap<String, Object> hm);
	public void sendMsg(String noticeId,String personNoticeId);
}
