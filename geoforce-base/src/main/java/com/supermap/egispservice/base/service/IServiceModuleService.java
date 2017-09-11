package com.supermap.egispservice.base.service;

import com.supermap.egispservice.base.entity.ServiceModuleEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BaseServiceModule;
import com.supermap.egispservice.base.pojo.BaseServiceModuleList;
import com.supermap.egispservice.base.pojo.ServiceModuleByType;


public interface IServiceModuleService {
	
	public void add(ServiceModuleEntity entity);

	public void deleteById(String id);
	
	public ServiceModuleEntity queryById(String id,boolean isNeedItems);
	/**
	 * 
	 * <p>Title ：add</p>
	 * Description：		添加服务模块
	 * @param name		名称
	 * @param pid		父模块ID
	 * @param status	状态
	 * @param useLimit	使用限制
	 * @param url		URL
	 * @param refUrl	参考文档URL
	 * @param supportTel	技术支持电话
	 * @param commercialTel	商务洽谈电话
	 * @param price			价格
	 * @param remarks		备注
	 * @throws ParameterException
	 * Author：Huasong Huang
	 * CreateTime：2014-8-8 下午04:22:14
	 */
	public void add(String name, String pid, String status, String useLimit, String url, String refUrl
			, String price,String remarks,String icon_rul,String type) throws ParameterException;
	
	public void update(String idStr, String status, String useLimit, String url, String refUrl, String remarks,
			String price, String code,String icon_url,String type) throws ParameterException;

	public BaseServiceModule queryById(String id);
	
	public BaseServiceModuleList query(String id,String name,String status,int pageNo,int pageSize)throws ParameterException;
	
	public ServiceModuleByType[] queryAllByType(String queryType,String status);
}
