package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.AddressEntity;
import com.supermap.egispservice.base.pojo.DistributeAddress;
import com.supermap.egispservice.base.pojo.LogisticsAPIResult;
import com.supermap.egispservice.base.pojo.LogisticsResultInfo;

public interface ILogisticsService {

	/**
	 * 
	 * <p>Title ：logistic</p>
	 * Description：		单条分单
	 * @param orderBaseId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-23 下午03:12:50
	 */
	public LogisticsResultInfo logistic(String orderBaseId,String userId,String enterpriseId,String departmentId);
	
	/**
	 * 
	 * <p>Title ：logistics</p>
	 * Description：使用地址直接进行分单
	 * @param id
	 * @param address
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-23 下午03:14:38
	 */
	public LogisticsResultInfo logistics(String id,String address,String userId,String enterpriseId,String departmentId);
	
	/**
	 * 
	 * <p>Title ：logistic</p>
	 * Description：		批量分单
	 * @param orderBaseIds
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-23 下午03:13:03
	 */
	public void logistic(List<String> orderBaseIds,String userId,String enterpriseId,String departmentId);
	
	/**
	 * 
	 * <p>Title ：logistics</p>
	 * Description：		手动分单
	 * @param orderId
	 * @param smx
	 * @param smy
	 * Author：Huasong Huang
	 * CreateTime：2014-9-25 下午06:59:00
	 */
	public void logistics(String orderId,double smx ,double smy);
	
	
	/**
	 * 
	 * <p>Title ：logisticsAPI</p>
	 * Description：		为API平台提供的分单服务接口
	 * @param addresses
	 * @param userId
	 * @param enterpriseId
	 * @param departmentId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-4-9 下午04:42:12
	 */
	public List<LogisticsAPIResult> logisticsAPI(List<DistributeAddress> addresses,String coorType,String userId,String enterpriseId,String departmentId);
	/**
	 * 
	 * <p>Title ：addrssMatchAndSave</p>
	 * Description：		地址匹配并保存
	 * @param address
	 * @param userId
	 * @param eid
	 * @param dcode
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-13 下午03:23:24
	 */
	public AddressEntity addrssMatchAndSave(String address,String userId,String eid,String dcode);
	
	/**
	 * 
	 * <p>Title ：queryAddressList</p>
	 * Description：查询地址列表
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-13 下午03:23:55
	 */
	public Map<String,Object> queryAddressList(final String keyword,int pageNo,int pageSize,final String userId);
	
	
	/**
	 * 分单API--返回区划信息（区划状态&关联区划）
	 * @param addresses
	 * @param coorType
	 * @param userId
	 * @param enterpriseId
	 * @param departmentId
	 * @param needAreaInfo 是否需要返回区划信息
	 * @return
	 */
	public List<LogisticsAPIResult> logisticsAPI_Area(List<DistributeAddress> addresses,String coorType,String userId,String enterpriseId,String departmentId,boolean needAreaInfo);
	
}
