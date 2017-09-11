package com.supermap.lbsp.provider.service.notice;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonNoticeForeign;

public interface PersonNoticeService {
	/**
	 * 
	 * @param 增加 
	 * @return
	 */
	public PersonNoticeForeign addPersonNotice(PersonNoticeForeign personNoticeTask) ;
    /**
     * 修改
     * @param personNoticeTask
     * @return
     */
	public int updatePersonNotice(PersonNoticeForeign personNoticeTask);
	/**
	 * 根据主键ID查询
	 * 
	 * @param id
	 * @return
	 */
	public PersonNoticeForeign getPersonNotice(String id);
	/**
	 * 删除
	 * @return
	 */
	public int delPersonNotice(PersonNoticeForeign personNoticeTask);
	public int delPersonNotice(String noticeId);
	/**
	 * 查询
	 * @return
	 */
	public List<PersonNoticeForeign> queryPersonNotice(String personId,
			String noticeId, Short status);
	/**
	 * 查询明细
	 * @param page
	 * @param hm
	 * @return
	 */
	public List<Object[]> queryPersonNoticeDetails(Page page,HashMap<String, Object> hm);
}
