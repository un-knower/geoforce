package com.supermap.egispservice.area.constants;

import java.util.Properties;

import com.supermap.utils.AppPropertiesUtil;

public class EgispAreaConstants {

	
	private static Properties prop = AppPropertiesUtil.readPropertiesFile("config.properties",EgispAreaConstants.class);
	
	/**
	 * 区域面数据集的名称
	 */
	public static final String DATASET_AREA_NAME = prop.getProperty("dataset.area.name");
	
	public static final String IMPORT_DIR = prop.getProperty("import.dir","./data/udb");
	public static final String IMPORT_UNZIP_DIR = prop.getProperty("import.unzip.dir","./data/unzip");
	public static final String EXPORT_DIR = prop.getProperty("export.dir","");
	
	/**
	 * 要采用内存数据集擦除 最小面积  默认70000
	 */
	public static final String REGION_AREA_MIN = prop.getProperty("region.DVearse.areaMin","70000");
	
	/**
	 * 要采用内存数据集擦除  最小part 默认10
	 */
	public static final String REGION_PART_MIN = prop.getProperty("region.DVearse.partMin","10");
	
}
