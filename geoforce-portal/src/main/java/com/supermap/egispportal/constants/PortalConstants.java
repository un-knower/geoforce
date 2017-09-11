package com.supermap.egispportal.constants;

import java.util.Properties;

import com.supermap.utils.AppPropertiesUtil;

public class PortalConstants {
	
	public static final String USER_SOURCE_PORTAL = "门户";
	public static final String USER_SOURCE_RSS = "运营支撑";
	public static final String USER_SOURCE_ZIZU = "自助式";
	public static final String USER_SOURCE_OTHERS = "其它";
	public static final String USER_SOURCE_UNKONW = "-1";
	
	private static Properties prop = AppPropertiesUtil.readPropertiesFile("egispportal.properties",
			PortalConstants.class);

	public static final boolean IS_DEBUG = Boolean.parseBoolean(prop.getProperty("IS_DEBUG", "false"));
	
	public static final String ACCESS_PATH = prop.getProperty("accessListPath");
	/**
	 * 登录成功跳转URL
	 */
	public static final String SEND_REDIRECT = prop.getProperty("sendRedirect_url");
	
	public static final String REMOTE_REGIST_URL = prop.getProperty("regist_url");
	// 远程注册的超时时间
	public static final int REGIST_TIMEOUT = Integer.parseInt(prop.getProperty("remote_regist_timeout","3000"));
	
	public static final String USER_INFO_CONSTANTS = "_userinfo_";
	
	public static final String URIENCODING = prop.getProperty("uriencoding", "ISO8859-1");
	public static final String TARGET_MAIL = prop.getProperty("target.mail","cloud@supermap.com");
	
}
