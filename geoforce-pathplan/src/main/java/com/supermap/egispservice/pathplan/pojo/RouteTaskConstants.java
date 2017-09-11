package com.supermap.egispservice.pathplan.pojo;

public interface RouteTaskConstants {
	
	// 定时设置
	public static final Byte TASK_STATUS_TIMING_SETTING = Byte.valueOf("6");
	// 正在运行
	public static final Byte TASK_STATUS_RUNNING = Byte.valueOf("7");
	// 定时设置
	public static final Byte TASK_STATUS_FINISHED= Byte.valueOf("8");
	
	// 删除标志
	public static final Byte DELETE_FLAG = Byte.valueOf("0");
	
	// 最优路径
	public static final Byte PLAN_TYPE_BEST_PATH = Byte.valueOf("0");
	
	 //任务状态 0禁用 1启用 2删除
	public static final String JOB_STATUS_FORBIDDEN = "0";
	public static final String JOB_STATUS_ENABLE = "1";
	public static final String JOB_STATUS_REMOVE = "2";
	
	
	public static final String PARAM_FIELD_NET_ID = "netid";
	public static final String PARAM_FIELD_NET_COORD = "netcoord";
	public static final String PARAM_FIELD_ORDER_IDS = "orderIds";
	public static final String PARAM_FIELD_ORDER_COORDS = "ordersCoords";
	public static final String PARAM_FIELD_CARS = "pathPlanCars";
	public static final String PARAM_FIELD_ORDER_TIME_RANGE = "timeTange";
	public static final String PARAM_FIELD_ORDER_CAR_LOAD = "carLoad";
	public static final String PARAM_FIELD_ORDER_FIXED_MIN = "fixedMin";
	public static final String PARAM_FIELD_ORDER_BATCH_TIME_START = "batchTimeStart";
	public static final String PARAM_FIELD_ORDER_BATCH_TIME_END = "batchTimeEnd";
	
	
	// 用于线路规划的参数文件名称
	public static final String FILE_PARAM_NAME = "nets_orders.data";
	// 用于线路规划的结果文件名称
	public static final String FILE_RESULT_NAME = "path.json";
	
	public static final String RESULT_NAME_PATH = "path";
	public static final String RESULT_NAME_PARTS = "parts";
	public static final String RESULT_NAME_INDEX = "index";
	public static final String RESULT_NAME_WEIGHT = "weight";
	public static final String RESULT_NAME_GUIDE = "guid";
	public static final String RESULT_NAME_DISTANCE = "distances";
	public static final String RESULT_NAME_TOTAL_DISTANCE = "t_distance";
	public static final String RESULT_NAME_STOP_WEIGHTS = "stopWeights";
	public static final String RESULT_NAME_STOP_TIMES = "stopTimes";
	
	
	
	
	
	
	

}
