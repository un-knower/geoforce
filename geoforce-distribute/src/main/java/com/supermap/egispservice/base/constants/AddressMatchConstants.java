package com.supermap.egispservice.base.constants;

import java.util.Properties;

import com.supermap.utils.AppPropertiesUtil;

public class AddressMatchConstants {
	
	private static Properties prop = AppPropertiesUtil.readPropertiesFile("addressMatchConstants.properties",
			AddressMatchConstants.class);	
	
	// 关键词允许的长度
	public static int SM_LENGTH_THRESHOLD = Integer.parseInt(prop.getProperty("SM_LENGTH_THRESHOLD", "15"));
	// 当地址过滤之后，是否继续对该地址进行地址匹配
	public static boolean IS_CONTINUE_DISTRIBUTE = Boolean.parseBoolean(prop.getProperty("IS_CONTINUE_DISTRIBUTE", "false"));
	// 地址关键信息的最短长度
	public static int SM_LENGTH_NEED_THRESHOLD = Integer.parseInt(prop.getProperty("SM_LENGTH_NEED_THRESHOLD","4"));
	// 超图地址匹配的最大长度，超过即使用百度进行地址匹配
	public static int SM_LENGTH_MAX_THRESHOLD = Integer.parseInt(prop.getProperty("SM_LENGTH_MAX_THRESHOLD","25"));
	// 
	public static float REVERSE_MATCH_RANGE = Float.parseFloat(prop.getProperty("reverseMatchRange","500"));
	// 使用多线程进行地址匹配的数量阀值
	public static int MULTI_THREAD_COUNT_THRESHOD = Integer.parseInt(prop.getProperty("multi.thread.match.threshod","2"));
	// 处理批量地址的线程数量
	public static int MULTI_THREAD_COUNT = Integer.parseInt(prop.getProperty("multi.thread.match.count","10"));
	
	public static int LOGISTICS_BATCH_MAX_SIZE = Integer.parseInt(prop.getProperty("logistics.batch.max.size", "2000"));
	// 查询区域面多线程的数量
	public static int MULTI_THREAD_AREA_COUNT = Integer.parseInt(prop.getProperty("multi.thread.area.count", "4"));
	public static int MULTI_THREAD_AREA_THRESHOD = Integer.parseInt(prop.getProperty("multi.thread.area.threshod", "500"));
	
	public final static int RESULT_TYPE_SUCCESS = 1; // 有坐标，并且定位到地区
	public final static int RESULT_TYPE_NOT_AREA = 2; // 有坐标，没有定位到地区
	public final static int RESULT_TYPE_NOT_COOR = 3; // 没坐标
	public final static int RESULT_TYPE_COUNTY_DIFF = 4; // 分单点与地址所在的区不一致，应为：***区
	public final static int RESULT_TYPE_NOT_FULL = 5; // 地址不详细、地址不完整、缺少区县
	public final static int RESULT_TYPE_UNIT_DIFF = 6; // 派送单位不一致
	
	public final static String GPS = "GPS";
	public final static String SMC = "SMC";
	public final static String SMLL = "SMLL";
	
	public final static String SUCCESS_INFO = "";
	public final static String NOT_AREA_INFO = "有坐标但未定位到区";
	public final static String NOT_COOR_INFO = "未找到坐标";
	public final static String COUNTY_DIFF_INFO = "";
	
	public final static String NOT_FULL_ADMIN_INFO = "缺少行政区划信息";
	public final static String NOT_FULL_INFO = "地址信息不完整";
	public final static String NEED_MORE_ADDRESS_KEYWORD_INFO = "地址关键词过少";
	public final static String ADDRESS_FUZZY = "地址干扰信息过多或地址过长";
//	public final static String NOT_FULL_ADMIN_INFO = "";
//	public final static String NOT_FULL_INFO = "";
//	public final static String NEED_MORE_ADDRESS_KEYWORD_INFO = "";
//	public final static String ADDRESS_FUZZY = "";
	
	
}
