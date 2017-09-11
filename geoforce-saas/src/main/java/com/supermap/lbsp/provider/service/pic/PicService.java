package com.supermap.lbsp.provider.service.pic;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPic;

public interface PicService {
	/**
	 * 
	 * @param 增加图片
	 * @return
	 */
	public PersonPic addPersonPic(PersonPic pic);
	/**
	 * 根据ID获取人员
	 * @param id
	 * @return
	 */

	public PersonPic getPersonPic(String id);
	/**
	 * 
	 * @param 删除人员
	 * @return
	 */
	public int delPersonPic(PersonPic pic);
	/**
	 * 根据条件得到图片列表
	 * @param map
	 * @return
	 */
	public List queryPersonPic(Page page,HashMap hm);
	public Page queryPersonPicPage(Page page,HashMap hm);
}
