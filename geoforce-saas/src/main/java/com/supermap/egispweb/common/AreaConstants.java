package com.supermap.egispweb.common;

import java.util.Properties;

import com.supermap.utils.AppPropertiesUtil;

public class AreaConstants {

	private static Properties prop = AppPropertiesUtil.readPropertiesFile("config.properties", AreaConstants.class);
	
	public static final String IMPORT_DIR = prop.getProperty("import.dir","./data/udb");
	public static final String IMPORT_UNZIP_DIR = prop.getProperty("import.unzip.dir","./data/unzip");
	
}
