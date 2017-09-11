package com.supermap.egispservice.base.constants;

public class StatusConstants {

	/**
	 * 订单状态常量
	 */
	public static final Byte ORDER_BASE_IMPORTED = Byte.valueOf("1");
	// 分单中
	public static final Byte ORDER_BASE_LOGISTICSING = Byte.valueOf("2");
	// 自动分单成功
	public static final Byte ORDER_BASE_LOGISTICS_AUTO_SUCCESS = Byte.valueOf("3");
	// 自动分单失败
	public static final Byte ORDER_BASE_LOGISTICS_AUTO_FAIL = Byte.valueOf("4");
	// 手动分单成功
	public static final Byte ORDER_BASE_LOGISTICS_MANUAL_SUCCESS = Byte.valueOf("5");
	// 手动分单失败
	public static final Byte ORDER_BASE_LOGISTICS_MANUAL_FAIL = Byte.valueOf("10");
	// 线路规划中
	public static final Byte ORDER_BASE_LOGISTICS_PLANING = Byte.valueOf("6");
	// 线路规划完成
	public static final Byte ORDER_BASE_LOGISTICS_PLANED = Byte.valueOf("7");
	// 配送中
	public static final Byte ORDER_BASE_LOGISTICS_PEISONGING = Byte.valueOf("8");
	// 已签收
	public static final Byte ORDER_BASE_LOGISTICS_SIGN = Byte.valueOf("9");
	
	// 普通状态
	public static final String COM_STATUS_COMMON = "0";
	// 禁止
	public static final String COM_STATUS_FORBIDDEN = "1";
	
	/**
	 * 网点状态常量
	 * 
	 */
	// 可用状态
	public static final int POINT_COMMON  = 0;
	
	// 不可用
	public static final int POINT_DISABLE = 1;
	
	// 处理中
	public static final int POINT_PROCESSING = 2;
}
