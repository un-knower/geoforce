package com.supermap.egispservice.base.service;

import java.util.Map;

import com.supermap.egispservice.base.pojo.CorrectAddress;

public interface ICorrectAddressService {

	/**
	 * 
	 * <p>Title ：addCorrectAddress</p>
	 * Description：		添加纠错地址
	 * @param address	待纠错的地址
	 * @param desc		地址描述
	 * @param userId	用户ID
	 * @param eid		企业ID
	 * @param dcode		部门code
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-8 上午09:55:35
	 */
	public boolean addCorrectAddress(String address,String desc,String userId,String eid,String dcode);
	
	/**
	 * 
	 * <p>Title ：query</p>
	 * Description：		查询纠错地址
	 * @param keyword
	 * @param status
	 * @param userId
	 * @param eid
	 * @param dcode
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-8 上午10:02:01
	 */
	public Map<String,Object> query(String keyword,int status,int pageNo,int pageSize,String userId,String eid,String dcode);
	
	/**
	 * 
	 * <p>Title ：moveCorrectAddress</p>
	 * Description： 移动纠错地址到相应位置，并重新计算分单结果
	 * @param addressId
	 * @param x
	 * @param y
	 * @param userId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-9 上午10:17:47
	 */
	public boolean moveCorrectAddress(String addressId,double x,double y,String userId,String eid,String dcode);
	
	/**
	 * 
	 * <p>Title ：delCorrectAddress</p>
	 * Description：  删除纠错地址
	 * @param addressId
	 * @param userId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-9 下午04:14:25
	 */
	public boolean delCorrectAddress(String addressId,String userId);
	
	
	/**
	 * 判断同一个企业下是否已存在相同的地址
	 * @param address
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2017-1-10下午3:07:58
	 */
	public boolean exsitAddress(String address,String eid);
	
	/**
	 * 根据地址查找已纠错的列表
	 * @param address
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2017-1-10下午3:22:17
	 */
	public CorrectAddress findCorrectAddress(String address,String eid);
	
	
	/**
	 * 判断是否需要用地址纠错库的分单逻辑
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2017-1-11下午6:11:23
	 */
//	public boolean isneedUseCorrectAddr(String eid);
	
}
