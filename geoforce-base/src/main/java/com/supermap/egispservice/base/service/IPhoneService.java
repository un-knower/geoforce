package com.supermap.egispservice.base.service;

public interface IPhoneService {

	/**
	 * 
	 * <p>Title ：getValidationCode</p>
	 * Description：		获取电话号码
	 * @param telephone
	 * Author：Huasong Huang
	 * CreateTime：2015-7-23 下午05:48:24
	 */
	public void getValidationCode(String phone);
	
	
	/**
	 * 
	 * <p>Title ：bindTelephone</p>
	 * Description：		绑定手机号
	 * @param tel
	 * @param user_id
	 * @param captcha
	 * Author：Huasong Huang
	 * CreateTime：2015-7-23 下午05:50:57
	 */
	public void bindTelephone(String tel,String user_id,String captcha);
	
}
