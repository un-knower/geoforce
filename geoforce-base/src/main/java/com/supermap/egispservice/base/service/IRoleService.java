package com.supermap.egispservice.base.service;

import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BaseRoleList;

public interface IRoleService {

	
	public String addRole(String name,String status,String privilegeIds[],String remarks)throws ParameterException;
	
	public BaseRoleList queryRoleList(String idStr,String name,int pageNo,int pageSize)throws ParameterException;
	
	public void updateRole(String id,String status,String remarks)throws ParameterException;
	
	public void addPrivileges(String id,String[] privilegeIds)throws ParameterException;
	public void removePrivileges(String id,String[] privilegeIds)throws ParameterException;
	
	public void deleteRole(String id)throws ParameterException;
	
	public void addOrRmPrivileges(String id,String[] privilegeIds)throws ParameterException;
}
