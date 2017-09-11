package com.supermap.egispservice.base.service;

import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BaseStaffAccessInfo;
import com.supermap.egispservice.base.pojo.BaseStaffDetails;
import com.supermap.egispservice.base.pojo.BaseStaffList;


public interface IStaffService {
	public String addStaff(String username, String password, String realName, String position, String sex,
			String mobilePhone, String phone, String email, String department) throws ParameterException;

	public BaseStaffList queryStaffList(String id, String username,String status,int pageNo,int pageSize)throws ParameterException;
	/**
	 * 
	 * <p>Title ：queryStaffDetails</p>
	 * Description：		查询员工的详细信息
	 * @param id
	 * @return
	 * @throws ParameterException
	 * Author：Huasong Huang
	 * CreateTime：2014-8-19 下午01:48:38
	 */
	public BaseStaffDetails queryStaffDetails(String id)throws ParameterException;
	/**
	 * 
	 * <p>Title ：updateStaffDetails</p>
	 * Description：		修改员工信息
	 * @param id
	 * @param mobilePhone
	 * @param phone
	 * @param email
	 * @param department
	 * @param position
	 * @param remarks
	 * @throws ParameterException
	 * Author：Huasong Huang
	 * CreateTime：2014-8-19 下午02:37:49
	 */
	public void updateStaffDetails(String id, String mobilePhone, String phone, String email,String department,String position ,String remarks)
			throws ParameterException;
	
	public void changePassword(String id,String oldPassword,String password) throws ParameterException;
	
	public void addRoles(String id,String[] roleIds)throws ParameterException;
	
	public void removeRoles(String id,String[] roleIds)throws ParameterException;
	public void deleteStaff(String id)throws ParameterException;
	
	public BaseStaffAccessInfo login(String username,String password)throws ParameterException;
	
	public void addOrRmRoles(String id,String[] roleIds)throws ParameterException;
}
