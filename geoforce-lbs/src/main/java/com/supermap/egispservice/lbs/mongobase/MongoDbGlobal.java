package com.supermap.egispservice.lbs.mongobase;


public class MongoDbGlobal {
	/**车辆当前轨迹表名称*/
	public final static String CAR_CURRENT_GPS = "carCurrentGps";
	/**车辆历史轨迹表名称*/
	public final static String CAR_HISTORY_GPS = "carHistoryGps_";
	/**车辆报警表名称*/
	public final static String CAR_ALARM = "carAlarm_";
	
	private String mongoUrl;//mongodb的url
	private String dbName;//数据库名称
	private String dbUser;//用户名
	private String dbPwd;//密码
	

	public String getMongoUrl() {
		return mongoUrl;
	}

	public void setMongoUrl(String mongoUrl) {
		this.mongoUrl = mongoUrl;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPwd() {
		return dbPwd;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}
}
