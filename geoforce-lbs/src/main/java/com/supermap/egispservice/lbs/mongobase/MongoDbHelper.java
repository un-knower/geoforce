package com.supermap.egispservice.lbs.mongobase;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.supermap.egispservice.lbs.constants.Config;

public class MongoDbHelper {
	private static MongoDbHelper mdh;
	private static Mongo mongo;
	
	private MongoDbGlobal mongoDbGlobal;

	private MongoDbHelper() throws UnknownHostException, MongoException {
		mongoDbGlobal = getMongoDbGlobal();
		String url = "10.241.140.123";
		if (mongoDbGlobal != null && mongoDbGlobal.getMongoUrl() != null) {
			url = mongoDbGlobal.getMongoUrl();
		}
		mongo = new Mongo(url);
		
	}

	public static MongoDbHelper getInstance() throws UnknownHostException,
			MongoException {
		if (mdh == null) {
			mdh = new MongoDbHelper();
		}
		return mdh;
	}

	public Mongo getMongo() {
		return mongo;
	}
	public MongoDbGlobal getDbGlobal(){
		return this.mongoDbGlobal;
	}
	/**
	 * 读取mongodb配置文件
	* @Title: getMongoDbGlobal
	* @return
	* MongoDbGlobal
	* @throws
	 */
	public static MongoDbGlobal getMongoDbGlobal(){
		//MongoDbGlobal mongoDbGlobal = (MongoDbGlobal)SprintHelper.getInstance().getBean("mongoDbGlobal");
		MongoDbGlobal mongoDbGlobal =(MongoDbGlobal) MyApplicationContextUtil.getBean("mongoDbGlobal");
		return mongoDbGlobal;
	}
	/**
	 * 获取mongodb DB对象
	* @Title: getMongodb
	* @return
	* DB
	* @throws
	 */
	public static DB getMongodb(){
		DB db = null;
		try {
			MongoDbHelper helper = MongoDbHelper.getInstance();
			Mongo mongo = helper.getMongo();
			MongoDbGlobal global = helper.getDbGlobal();
			db = mongo.getDB(global.getDbName());
			if (db.isAuthenticated() == false) {
				db.authenticate(global.getDbUser(), global.getDbPwd()
								.toCharArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return db;
	}

}
