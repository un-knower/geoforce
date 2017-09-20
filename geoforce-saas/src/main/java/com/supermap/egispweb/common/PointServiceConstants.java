package com.supermap.egispweb.common;

import java.util.Properties;

import com.supermap.utils.AppPropertiesUtil;

public class PointServiceConstants {

	private static Properties prop = AppPropertiesUtil.readPropertiesFile("config.properties",
			PointServiceConstants.class);
	
	public static final String IMG_ROOT_PATH = prop.getProperty("imgRootPath","/");
	
}
