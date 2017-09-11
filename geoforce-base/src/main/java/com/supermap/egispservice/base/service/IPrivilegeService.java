package com.supermap.egispservice.base.service;

import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BasePrivilegeList;
import com.supermap.egispservice.base.pojo.BasePrivilegeListItem;


public interface IPrivilegeService {
	
	public String add(String name, String code,  String status, String remark, String pid,String url)
			throws ParameterException;

	public BasePrivilegeList fuzzyQuery(String idStr,String levelStr,String name,String code,int pageNo,int pageSize)throws ParameterException;
	
	public BasePrivilegeListItem queryById(String id)throws ParameterException;
	
	public void updatePrivilege(String id,String statusStr,String url,String remarks)throws ParameterException;
	
	public void deletePrivilege(String id)throws ParameterException;
}
