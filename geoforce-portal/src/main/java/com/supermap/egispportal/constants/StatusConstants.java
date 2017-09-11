package com.supermap.egispportal.constants;

/**
 * 
 * <p>Title: StatusConstants</p>
 * Description:		状态常量
 *
 * @author Huasong Huang
 * CreateTime: 2014-10-8 上午10:43:26
 */
public interface StatusConstants {

	// 用户状态常量
	public static final String USER_STATUS_COMMON = "0";
	public static final String USER_STATUS_FORBIDDEN = "1";
	public static final String USER_STATUS_WAIT_ACTIVE = "2";
	public static final String USER_STATUS_WAIT_AUDIT = "3";
	public static final String USER_STATUS_OTHER = "4";
	public static final String USER_STATUS_ADV = "5";
	// 权限状态常量
	public static final String PRIVILEGE_STATUS_USING = "0";
	public static final String PRIVILEGE_STATUS_FORBIDDEN = "1";
	public static final String PRIVILEGE_STATUS_ABANDON = "2";
	// 企业状态常量
	public static final String COMPANY_STATUS_COMMON = "0";
	public static final String COMPANY_STATUS_FORBIDDEN = "1";
	// 服务模块常量
	public static final String MODULE_STATUS_RELEASE = "0";
	public static final String MODULE_STATUS_BETA = "1";
	public static final String MODULE_STATUS_REVOCATION = "2";
	public static final String MODULE_STATUS_OTHER = "3";
	// 订单状态常量
	public static final String ORDER_STATUS_WAIT_AUDIT = "0";
	public static final String ORDER_STATUS_PASS = "1";
	public static final String ORDER_STATUS_TRY_PASS = "2";
	public static final String ORDER_STATUS_NO_PASS = "3";
	// 员工状态常量
	public static final String STAFF_STATUS_COMMON = "0";
	public static final String STAFF_STATUS_FORBIDDEN = "1";
	// 角色状态
	public static final String ROLE_STATUS_COMMON = "0";
	public static final String ROLE_STATUS_FORBIDDEN = "1";
	// 订单项类型
	public static final int	ITEM_TYPE_COMMON = 0;
	public static final int	ITEM_TYPE_AUTO = 1;
	public static final int	ITEM_TYPE_PARENT = 2;
	public static final int	ITEM_TYPE_TOP = 3;
	
	// 订单状态,0:试用，1：定制
	public static final int ORDER_TYPE_TRY = 1;
	public static final int ORDER_TYPE_CUSTOM = 0;
	
	
	
}
