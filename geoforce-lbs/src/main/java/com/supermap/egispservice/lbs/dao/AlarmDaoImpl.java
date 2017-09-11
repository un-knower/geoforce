package com.supermap.egispservice.lbs.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;
import com.supermap.egispservice.lbs.mongobase.MongoDbHelper;
import com.supermap.egispservice.lbs.pojo.CarAlarm;
import com.supermap.egispservice.lbs.util.DateUtil;
import com.supermap.egispservice.lbs.util.Pagination;
@Component("alarmDao")
public class AlarmDaoImpl implements AlarmDao{

	@Override
	public List<CarAlarm> queryCarAlarmByReport(String month, Pagination page,
			HashMap<String, Object> hm) {
		// TODO Auto-generated method stub
		if (month == "") {
			return null;
		}
		DB db = MongoDbHelper.getMongodb();
		if (db == null)
			return null;

		String dbName = "carAlarm_" + month;
		DBCollection carAlarmDb = db.getCollection(dbName.trim());
		BasicDBObject query = getCarAlarmQuery(hm);
		System.out.println(query+"==================");
		DBCursor cur = null;
		if (page == null) {
			cur = carAlarmDb.find(query).sort(
					new BasicDBObject("alarmDate", -1));
		} else {
			long count = carAlarmDb.getCount(query);
			int curPage = page.getPageNo();
			int size = page.getPageSize();
			cur = carAlarmDb.find(query).sort(
					new BasicDBObject("alarmDate", -1))
					.skip((curPage - 1) * size).limit(size);
			page.setTotalCount(((Long) count).intValue());
		}
		List<CarAlarm> list = new ArrayList<CarAlarm>();
		while (cur.hasNext()) {
			DBObject obj = cur.next();
			CarAlarm carAlarm = loadCarAlarm(obj);

			list.add(carAlarm);
		}
		return list;
	}
	
	/**
	 * 根据查询条件获取mongodb 查询的query
	 * 
	 * @Title: getCarAlarmQuery
	 * @param hm
	 * @return BasicDBObject
	 * @throws
	 */
	private BasicDBObject getCarAlarmQuery(HashMap<String, Object> hm) {
		BasicDBObject query = new BasicDBObject();
		if (hm != null && hm.size() > 0) {
			Set<String> keys = hm.keySet();
			Iterator<String> it = keys.iterator();
			BasicDBObject alarmTimeQuery = null;
			while (it.hasNext()) {
				String key = (String) it.next();
				if (key.equals("startDate")) {
					Date startDate = (Date) hm.get(key);
					if(alarmTimeQuery == null){
						alarmTimeQuery = new BasicDBObject(QueryOperators.GTE, startDate);
					}else {
						alarmTimeQuery.append(QueryOperators.GTE, startDate);
					}
					query.put("alarmDate", alarmTimeQuery);
				} else if (key.equals("endDate")) {
					Date endDate = (Date) hm.get(key);
					if(alarmTimeQuery == null){
						alarmTimeQuery = new BasicDBObject(QueryOperators.LTE, endDate);
					}else {
						alarmTimeQuery.append(QueryOperators.LTE, endDate);
					}
					query.put("alarmDate", alarmTimeQuery);
				} else if (key.equals("status")) {
					Short status = Short.valueOf((String) hm.get(key));
					query.put("status", (int) status);
				} else if (key.equals("typeId")) {
					String typeId = (String) hm.get(key);
					query.put("typeId", typeId);
				} else if (key.equals("intypeId")) {
					List<String> typeCodes = (List<String>) hm.get(key);
					query.put("typeId", new BasicDBObject(QueryOperators.IN,typeCodes.toArray()));
				} else if (key.equals("license")) {
					String license = (String) hm.get(key);
					// 车牌号模糊搜索
					Pattern pattern = Pattern.compile("^.*" + license + ".*");
					query.put("license", pattern);
				} else if (key.equals("deptCode")) {
					String deptCode = (String) hm.get(key);
					// 部门编号需以传入部门编号为开头检索
					Pattern pattern = Pattern.compile("^" + deptCode + ".*");
					query.put("deptCode", pattern);
				} else if (key.equals("carId")) {
					String carId = (String) hm.get(key);
					query.put("carId", carId);
				}else if (key.equals("difTime")) {
					String difTime = (String) hm.get(key);
					query.put("difTime",  new BasicDBObject().append("$gte", Long.parseLong(difTime)));
				}
			}
		}
		return query;
	}
	public String getCarAlarmLastTable() {
		String lastTabl = null;
		try {
			DB db = MongoDbHelper.getMongodb();
			if (db == null)
				return lastTabl;

			Set<String> collNames = db.getCollectionNames();
			Iterator<String> it = collNames.iterator();
			List<String> carAlarmNames = new ArrayList<String>();
			while (it.hasNext()) {
				String collName = it.next();
				if (collName.contains("carAlarm")) {

					carAlarmNames.add(collName);
				}
			}
			Calendar cl = null,clTmp = Calendar.getInstance();

			for (String carAlarmTable : carAlarmNames) {
				clTmp.setTime(DateUtil.formatStringToDate(carAlarmTable
						.split("_")[1], "yyyyMM"));
				if (cl == null){
					cl = clTmp;
					lastTabl = carAlarmTable;
				}

				if (clTmp.after(cl)) {
					cl = clTmp;
					lastTabl = carAlarmTable;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			lastTabl = null;
		}
		return lastTabl;
	}


/**
 * 主键查询
 */
@Override
public CarAlarm getCarAlarm(String id, Date alarmDate) {
	//表时间和主键
	if (id == null || alarmDate == null) {
		return null;
	}
	DB db = MongoDbHelper.getMongodb();
	if (db == null)
		return null;
	String monthDate = DateUtil.format(alarmDate, "yyyyMM");
	String dbName = "carAlarm_" + monthDate;
	DBCollection alarmTable = db.getCollection(dbName.trim());
	BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
	DBObject obj = alarmTable.findOne(query);
	if (obj != null) {
		CarAlarm CarAlarm = loadCarAlarm(obj);
		return CarAlarm;
	}
	return null;
}
/**
 * 修改
 */
@Override
public int updateCarAlarm(CarAlarm carAlarm) {
	int ret = 0;
	try {
		DB db = MongoDbHelper.getMongodb();
		if (db == null)
			return ret;
		String monthDate = DateUtil.format(carAlarm.getAlarmDate(),
				"yyyyMM");
		String dbName = "carAlarm_" + monthDate;
		boolean flag = db.collectionExists(dbName);
		if (!flag) {// 表不存在返回错误
			return ret;
		}
		DBCollection alarmTable =  db.getCollection(dbName.trim());
		DBObject obj = getDBObjByCarAlarm(carAlarm);
		alarmTable.save(obj);
		ret = 1;
	} catch (Exception e) {
		e.printStackTrace();
		ret = 0;
	}
	return ret;
}


/**
 * 将mongodb对象封装成CarAlarm
 * 
 * @Title: loadCarAlarm
 * @param obj
 * @return CarAlarm
 * @throws
 */
private CarAlarm loadCarAlarm(DBObject obj) {
	CarAlarm carAlarm = new CarAlarm();
	carAlarm.setId(((ObjectId) obj.get("_id")).toString());
	Integer status = (Integer) obj.get("status");
	carAlarm.setStatus(Short.valueOf(status.toString()));
	carAlarm.setAlarmDate((Date) obj.get("alarmDate"));
	carAlarm.setDealDate((Date) obj.get("dealDate"));
	carAlarm.setOpinion((String) obj.get("opinion"));
	carAlarm.setCarId((String) obj.get("carId"));
	carAlarm.setTypeId((String) obj.get("typeId"));
	carAlarm.setUserId((String) obj.get("userId"));
	carAlarm.setTemCode((String) obj.get("temCode"));
	carAlarm.setLongitude((Double) obj.get("longitude"));
	carAlarm.setLatitude((Double) obj.get("latitude"));
	carAlarm.setSpeed((Double) obj.get("speed"));
	carAlarm.setDirection((Double) obj.get("direction"));
	carAlarm.setForeignId((String) obj.get("foreignId"));
	carAlarm.setAddr((String) obj.get("addr"));
	carAlarm.setOthers((String) obj.get("others"));
	carAlarm.setCarLicense((String) obj.get("license"));
	carAlarm.setDeptCode((String) obj.get("deptCode"));
	carAlarm.setUserId((String) obj.get("userId"));
	if (obj.get("lastDate") != null) {
		carAlarm.setLastDate((Date) obj.get("lastDate"));
	}else{
		carAlarm.setLastDate((Date) obj.get("alarmDate"));
	}
	if (obj.get("difTime") != null) {
		carAlarm.setDifTime((Long) obj.get("difTime"));
	}else{
		carAlarm.setDifTime(0l);
	}
	return carAlarm;
}
/**
 * 将carAlarm bean 转成 mongodb 对象
 */
private DBObject getDBObjByCarAlarm(CarAlarm carAlarm){
	DBObject obj = new BasicDBObject();
	String id = carAlarm.getId();
	if (id != null)
	obj.put("_id", new ObjectId(carAlarm.getId()));
	obj.put("status", carAlarm.getStatus());
	obj.put("alarmDate", carAlarm.getAlarmDate());
	obj.put("dealDate", carAlarm.getDealDate());
	obj.put("opinion", carAlarm.getOpinion());
	obj.put("typeId", carAlarm.getTypeId());
	obj.put("userId", carAlarm.getUserId());
	obj.put("carId", carAlarm.getCarId());
	obj.put("temCode", carAlarm.getTemCode());
	obj.put("longitude", carAlarm.getLongitude());
	obj.put("latitude", carAlarm.getLatitude());
	obj.put("speed", carAlarm.getSpeed());
	obj.put("direction", carAlarm.getDirection());
	obj.put("foreignId", carAlarm.getForeignId());
	obj.put("addr", carAlarm.getAddr());
	obj.put("lastDate", carAlarm.getLastDate());
	obj.put("difTime", carAlarm.getDifTime());
	obj.put("others", carAlarm.getOthers());
	obj.put("license", carAlarm.getCarLicense());
	obj.put("deptCode", carAlarm.getDeptCode());
	
	return obj;
}
}
