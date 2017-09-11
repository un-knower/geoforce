package com.supermap.egispboss.constants;

import java.util.Properties;


import com.supermap.utils.AppPropertiesUtil;

public class EgispBossConstants {
	private static Properties prop = AppPropertiesUtil.readPropertiesFile("egispboss.properties",
			EgispBossConstants.class);

	public static final boolean IS_DEBUG = Boolean.parseBoolean(prop.getProperty("IS_DEBUG", "false"));
	
	public static final String USER_SOURCE = "运营支撑";
	
	public static final String URI_ENCODING=prop.getProperty("URI_ENCODING", "iso-8859-1");
	
	public static final String AREA_EXPORT_DIR=prop.getProperty("export.dir");
	
}
