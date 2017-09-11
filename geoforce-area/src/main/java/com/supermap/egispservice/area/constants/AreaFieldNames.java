package com.supermap.egispservice.area.constants;


public interface AreaFieldNames {

	public static final String ID = "ID";
	public static final String NAME = "NAME";
	public static final String AREANUMBER = "AREA_NUM";
	public static final String NET_ID = "POINT";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String UPDATE_TIME = "UPDATE_TIME";
	public static final String DELETE_FLAG = "DELETE_FLAG";
	public static final String USER_ID = "USER_ID";
	public static final String ENTERPRISE_ID = "ENTERPRISE_ID";
	public static final String DEPARTENT_CODE = "DCODE";
	public static final String ADMINCODE = "ADMINCODE";
	
	/**
	 * 区划状态
	 */
	public static final String AREA_STATUS = "AREA_STATUS";
	/**
	 * 关联的区划id
	 */
	public static final String RELATION_AREAID = "RELATION_AREAID";
	
	/**
	 * haier网格组编码
	 */
	public static final String WGZ_CODE = "WGZ_CODE";
	
	/**
	 * haier网格组名称
	 */
	public static final String WGZ_NAME = "WGZ_NAME";
	/**
	 * haier线路编码
	 */
	public static final String LINE_CODE = "LINE_CODE";
	/**
	 * haier线路名称
	 */
	public static final String LINE_NAME = "LINE_NAME";
	
	
	public static final String KEEP_FIELD_NAMES[] = new String[]{
			ID,NAME,AREANUMBER,NET_ID,DELETE_FLAG,USER_ID,ENTERPRISE_ID,DEPARTENT_CODE,CREATE_TIME,ADMINCODE,AREA_STATUS,RELATION_AREAID
			,WGZ_CODE,WGZ_NAME,LINE_CODE,LINE_NAME
		};
	
	
}
