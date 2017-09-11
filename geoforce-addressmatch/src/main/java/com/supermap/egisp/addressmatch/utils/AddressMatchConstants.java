package com.supermap.egisp.addressmatch.utils;

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
	
	// 高精度
	public static final int QUERY_LEVEL_HIGH_ACCURACY = 2;
	// 高匹配
	public static final int QUERY_LEVEL_HIGH_MATCH = 1;
	
	// 省市区前缀
	public static final int QUERY_STRATEGY_ADMIN_PREFIX = 5;
	// 省市区必须完整
	public static final int QUERY_STRATEGY_ADMIN_FULL = 1;
	// 省市区辅助，可以完整
	public static final int QUERY_STRATEGY_ADMIN_SHOULD = 2;
	// 城市以上的级别必须满足
	public static final int QUERY_STRATEGY_ADMIN_CITY_MUST = 3;
	// 直接进行关键词查询
	public static final int QUERY_STRATEGY_KEYWORD = 4;
	
	
	// 普通地址
	public static final int ADMIN_TYPE_COMMON = 0;
	// 直辖市
	public static final int ADMIN_TYPE_MUNIPALITY = 1;
	// 自治区直辖县
	public static final int ADMIN_TYPE_ZI_2_COUNTY = 2;
	// 省直辖县
	public static final int ADMIN_TYPE_P_2_COUNTY = 3;
	// 无区县级别
	public static final int ADMIN_TYPE_NO_COUNTY = 4;
	
	
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
