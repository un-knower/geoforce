package com.supermap.egispservice.base.constants;

import java.util.Properties;

import com.supermap.utils.AppPropertiesUtil;

public class PortalConstants {
	
	public static final String USER_SOURCE_PORTAL = "门户";
	public static final String USER_SOURCE_RSS = "运营支撑";
	public static final String USER_SOURCE_ZIZU = "自助式";
	public static final String USER_SOURCE_OTHERS = "其它";
	public static final String USER_SOURCE_UNKONW = "-1";
	
	private static Properties prop = AppPropertiesUtil.readPropertiesFile("config.properties",
			PortalConstants.class);

	public static final String PHONE_CAPCHAR_SERVICE = prop.getProperty("phone.capchar.service");
	public static final String PHONE_CAPCHAR_SOURCE = prop.getProperty("phone.capchar.source");
	public static final String PHONE_CAPCHAR_TOKEN = prop.getProperty("phone.capchar.token");
	public static final String PHONE_BIND_SERVICE = prop.getProperty("phone.bind.service");
	
	
	
}
