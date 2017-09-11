package com.supermap.egispservice.lbs.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.QueryOperators;
import com.mongodb.util.JSON;
import com.supermap.egispservice.lbs.mongobase.MongoDbGlobal;
import com.supermap.egispservice.lbs.mongobase.MongoDbHelper;
import com.supermap.egispservice.lbs.pojo.CarGps;
import com.supermap.egispservice.lbs.util.DateUtil;
import com.supermap.egispservice.lbs.util.Pagination;
import com.supermap.egispservice.lbs.util.StringUtil;
import com.supermap.egispservice.lbs.util.TranslateXYUtil;

@Component("carGpsDao")
public class CarGpsDaoImpl implements CarGpsDao{

	@SuppressWarnings("unchecked")
	public List<CarGps> queryCarCurrentGps(HashMap<String, Object> hm)
			throws Exception {
		
		DB db = MongoDbHelper.getMongodb();
		if(db == null)
			return null;
		DBCollection currentGps = db.getCollection(MongoDbGlobal.CAR_CURRENT_GPS);
		BasicDBObject query = new BasicDBObject();
		if(hm != null){
			Date gpsDate = (Date)hm.get("gtGpsData");
			List<String> carIds = (List<String>)hm.get("carIds");
			String deptCode = (String)hm.get("deptCode");
			String temCode = (String)hm.get("temCode");
			if (gpsDate != null) {
				query.put("gpsDate", new BasicDBObject("$gt", gpsDate));
			}
			if (carIds != null && carIds.size() > 0) {
				query.put("carId", new BasicDBObject(QueryOperators.IN, carIds.toArray()));
			}
			if(deptCode != null && !deptCode.trim().equals("")){
				// 部门编号需以传入部门编号为开头检索
				Pattern pattern = Pattern.compile("^" + deptCode + ".*");
				query.put("deptCode", pattern);
			}
			if(StringUtils.isNotBlank(temCode)){
				query.put("temCode", temCode.trim());
			}
		}
		List<CarGps> clist = new ArrayList<CarGps>();
		DBCursor cur = currentGps.find(query);
		while (cur.hasNext()) {
			DBObject obj = cur.next();
			CarGps carGps = getCarGps(obj);
			clist.add(carGps);
		}
		return clist;
	}
	public static void main(String[] args) {
		DB db = MongoDbHelper.getMongodb();
		if(db == null)
			return;
		DBCollection currentGps = db.getCollection(MongoDbGlobal.CAR_CURRENT_GPS);
		DBCursor cur = currentGps.find();
		int i = 0;
		while (cur.hasNext()) {
			DBObject obj = cur.next();
			Set<String> set = obj.keySet();
			StringBuffer bf = new StringBuffer(100);
			bf.append("记录"+i+++":[");
			for(String key:set){
				bf.append(key+":"+obj.get(key)+"；");
			}
			bf.append("]");
			System.err.println(bf);
		}
	}
	
	public int saveCarCurrentGps(CarGps carGps) throws Exception {
		int ret = 0;
		try {
			DB db = MongoDbHelper.getMongodb();
			if(db == null)
				return ret;

			boolean flag = db.collectionExists(MongoDbGlobal.CAR_CURRENT_GPS);
			if (!flag) {
				String strObjName = "{'name':'" + MongoDbGlobal.CAR_CURRENT_GPS + "'}";
				DBObject obj = (BasicDBObject) JSON.parse(strObjName);
				DBCollection currentGps = db.createCollection(MongoDbGlobal.CAR_CURRENT_GPS,
						obj);
				DBObject keys = (BasicDBObject) JSON
						.parse("{'carId':1,'gpsDate':1}");
				currentGps.ensureIndex(keys, "carId_gpsDate_1");
			}

			DBCollection carCurrentGps = db.getCollection(MongoDbGlobal.CAR_CURRENT_GPS);
			DBObject obj = setCarGps(carGps);
			carCurrentGps.save(obj);
			ret = 1;
		} catch (Exception e) {
			ret = 0;
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return ret;
	}
	
	public List<CarGps> queryCarHistoryGps(HashMap<String, Object> hm,
			Pagination page) throws Exception {
		
		if(hm == null || hm.isEmpty()){
			return null;
		}
		String carId = (String)hm.get("carId");
		Date startDate = (Date)hm.get("startDate");
		Date endDate = (Date)hm.get("endDate");
		boolean isRuning = (Boolean)hm.get("isRuning");//只获取行驶中的轨迹，静止的不获取
		//必填项
		if(StringUtils.isBlank(carId) || startDate == null || endDate == null){
			return null;
		}
		DB db = MongoDbHelper.getMongodb();
		if(db == null)
			return null;
		
		List<String> tables = getHistoryTables(startDate, endDate);//历史轨迹表     每天一个表
		// 查询条件
		BasicDBObject query = new BasicDBObject();
		query.put("gpsDate", new BasicDBObject("$gte", startDate).append(
				"$lte", endDate));
		query.put("carId", carId);
		
		List<CarGps> list = new ArrayList<CarGps>();
		double tmpMile = 0;
		if(page == null){//一次查出所有数据不需要分页
			for(String tableName:tables){
				DBCollection historyGps = db.getCollection(tableName);
				DBCursor cur = historyGps.find(query).sort(
						new BasicDBObject("gpsDate", 1));
				while(cur.hasNext()){
					CarGps carGps = getCarGps(cur.next());
					if(isRuning){//过滤静止点
						if(carGps.getMile() > tmpMile){
							list.add(carGps);
						}
					}else {
						list.add(carGps);
					}
				}
			}
		}else {//分页查询 只查第一张表，联表分页性能太差 如返回数据不全 建议用户缩短查询时间
			DBCollection historyGps = db.getCollection(tables.get(0));
			long count = historyGps.getCount(query);
			int curPage = page.getPageNo();
			int size = page.getPageSize();
			DBCursor cur = historyGps.find(query).sort(
					new BasicDBObject("gpsDate", -1))
					.skip((curPage - 1) * size).limit(size);
			page.setTotalCount(((Long) count).intValue());
			while(cur.hasNext()){
				CarGps carGps = getCarGps(cur.next());
				if(isRuning){//过滤静止点
					if(carGps.getMile() > tmpMile){
						list.add(carGps);
					}
				}else {
					list.add(carGps);
				}
			}
		}
		
		return list;
	}
	
	public int saveCarHistoryGps(CarGps carGps) throws Exception{
		int ret = 0;
		try {
			DB db = MongoDbHelper.getMongodb();
			if(db == null)
				return ret;

			String strDate = DateUtil.format(new Date(),"yyyyMMdd");
			String dbName = MongoDbGlobal.CAR_HISTORY_GPS + strDate.trim();
			boolean flag = db.collectionExists(dbName);
			if (!flag) {
				String strObjName = "{'name':'" + dbName.trim() + "'}";
				DBObject obj = (BasicDBObject) JSON.parse(strObjName);
				DBCollection historyGps = db.createCollection(dbName, obj);
				DBObject keys = (BasicDBObject) JSON
						.parse("{'carId':1,'gpsDate':1}");
				historyGps.ensureIndex(keys, "carId_gpsDate_1");
			}

			DBCollection historyGps = db.getCollection(dbName.trim());
			DBObject obj = setCarGps(carGps);
			historyGps.save(obj);
			ret = 1;
		} catch (Exception e) {
			ret = 0;
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return ret;
	}
	public List<CarGps> queryCarCurrentGpsByBound(String deptCode, double minLon,
			double minLat, double maxLon, double maxLat)throws UnknownHostException, MongoException {
		
		if(deptCode == null){
			return null;
		}
		DB db = MongoDbHelper.getMongodb();
		if(db == null)
			return null;
		DBCollection currentGps = db.getCollection(MongoDbGlobal.CAR_CURRENT_GPS);
		BasicDBObject query = new BasicDBObject();
		Pattern codePt = Pattern.compile("^"+deptCode+".*");
		query.put("deptCode", codePt);
		query.put("lng", new BasicDBObject(QueryOperators.GT,minLon).append(
				QueryOperators.LT, maxLon));
		query.put("lat", new BasicDBObject(QueryOperators.GT,minLat).append(
				QueryOperators.LT, maxLat));
		
		DBCursor cur = currentGps.find(query);
		List<CarGps> list = new ArrayList<CarGps>();
		while (cur.hasNext()) {
			DBObject obj = cur.next();
			CarGps gps = getCarGps(obj);
			list.add(gps);
		}
		return list;
	}
	/**
	 * 
	* 方法名: getCarGps
	* 描述: 封装carGpsbean
	* @param obj
	* @return
	 */
	private CarGps getCarGps(DBObject obj){
		CarGps carGps = new CarGps();
		ObjectId id = (ObjectId)obj.get("_id");
		if(id != null)
			carGps.setId(id.toString());
		carGps.setTemCode((String) obj.get("temCode"));
		carGps.setCarId((String) obj.get("carId"));
		carGps.setDeptCode((String)obj.get("deptCode"));
		carGps.setLng((Double) obj.get("lng"));
		carGps.setLat((Double) obj.get("lat"));
		carGps.setOrgLng((Double) obj.get("orgLng"));
		carGps.setOrgLat((Double) obj.get("orgLat"));
		Double[] dbLngLat = TranslateXYUtil.bdEncrypt(carGps.getLng(), carGps.getLat());
		carGps.setBdLng(StringUtil.doubleFiex(dbLngLat[0].doubleValue(), 7));
		carGps.setBdLat(StringUtil.doubleFiex(dbLngLat[1].doubleValue(), 7));
		carGps.setSpeed((Double) obj.get("speed"));
		carGps.setDirection((Double) obj.get("direction"));
		carGps.setAddr((String) obj.get("addr"));
		carGps.setGpsDate((Date) obj.get("gpsDate"));
		carGps.setSysDate((Date) obj.get("sysDate"));
		carGps.setMile((Double) obj.get("mile"));
		carGps.setOil((Double)obj.get("oil"));
		carGps.setPicPath((String) obj.get("picPath"));
		carGps.setOthers((String) obj.get("others"));
		carGps.setZfTurn((String)obj.get("zfTurn"));
		carGps.setAlarm((String)obj.get("alarm"));
		return carGps;
	}
	/**
	 * 
	* 方法名: setCarGps
	* 描述:carGps转成mongodb对象
	* @param carGps
	* @return
	 */
	private DBObject setCarGps(CarGps carGps){
		if(carGps == null)
			return null;
		DBObject obj = new BasicDBObject();
		String id = carGps.getId();
		if(StringUtils.isNotBlank(id))
			obj.put("_id", new ObjectId(id));
		obj.put("temCode", carGps.getTemCode());
		obj.put("carId", carGps.getCarId());
		obj.put("deptCode", carGps.getDeptCode());
		obj.put("lng", StringUtil.doubleFiex(carGps.getLng(), 7));
		obj.put("lat", StringUtil.doubleFiex(carGps.getLat(), 7));
		obj.put("orgLng", StringUtil.doubleFiex(carGps.getOrgLng(), 7));
		obj.put("orgLat", StringUtil.doubleFiex(carGps.getOrgLat(), 7));
		obj.put("speed", StringUtil.doubleFiex(carGps.getSpeed(), 3));
		obj.put("direction", carGps.getDirection());
		obj.put("addr", carGps.getAddr());
		obj.put("sysDate", carGps.getSysDate());
		obj.put("gpsDate", carGps.getGpsDate());
		obj.put("mile", StringUtil.doubleFiex(carGps.getMile(), 3));
		obj.put("oil", StringUtil.doubleFiex(carGps.getOil(), 3));
		String others = carGps.getOthers() == null?"":carGps.getOthers();
		obj.put("picPath", carGps.getPicPath());
		obj.put("others", others);
		obj.put("zfTurn", carGps.getZfTurn());
		obj.put("alarm", carGps.getAlarm());
		return obj;
	}
	/**
	 * 获取历史数据要查询的表
	* @Title: getHistoryTables
	* @param startTime
	* @param endTime
	* @param breakDate
	* @return
	* List<String>
	* @throws
	 */
	private List<String> getHistoryTables(Date startTime,Date endTime){
		
		String sTime =  DateUtil.format(startTime, "yyyy-MM-dd");
		Date sDate = DateUtil.formatStringToDate(sTime+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Calendar sCl = Calendar.getInstance();
		sCl.setTime(sDate);
		String eTime = DateUtil.format(endTime,"yyyy-MM-dd");
		Date eDate = DateUtil.formatStringToDate(eTime+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
		Calendar eCl = Calendar.getInstance();
		eCl.setTime(eDate);
		
		List<String> tables = new ArrayList<String>();
		String tableName = "";
		while (sCl.before(eCl)) {
			tableName = MongoDbGlobal.CAR_HISTORY_GPS+DateUtil.format(sCl.getTime(), "yyyyMMdd");
			if(!tables.contains(tableName)){
				tables.add(tableName);
			}
			sCl.add(Calendar.DAY_OF_MONTH, 1);
		}
		return tables;
	}
	/**
	 * 获取一辆车在某一时间段内的行驶里程
	 * 包括 第一条GPS数据和最后一条GPS数据
	* @Title: getCarHistoryByTime
	* @param carId
	* @param startTime
	* @param endTime
	* @return
	* Double
	* @throws
	 */
	public List<CarGps> getCarHistoryByTime(String carId,Date startTime,Date endTime) {
		
		List<CarGps> list = new ArrayList<CarGps>();
		try {
			long diffTime = DateUtil.diffDates(endTime, startTime);
			if(diffTime < 0){//结束时间早于开始时间
				return list;
			}
			long diffSTime = DateUtil.diffDates(new Date(), startTime);
			if(diffSTime < 0){//开始时间大于当前系统时间
				startTime = new Date();
			}
			long diffETime = DateUtil.diffDates(new Date(), endTime);
			if(diffETime < 0){//结束时间大于当前系统时间
				endTime = new Date();
			}
			Mongo mongo = MongoDbHelper.getInstance().getMongo();
			MongoDbGlobal global = MongoDbHelper.getInstance().getDbGlobal();

			DB db = mongo.getDB(global.getDbName());
			if (db.isAuthenticated() == false) {
				db.authenticate(global.getDbUser(), global.getDbPwd()
								.toCharArray());
			}
			String startDate =  DateUtil.format(startTime, "yyyy-MM-dd");
			Date sDate = DateUtil.formatStringToDate(startDate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			Calendar sCl = Calendar.getInstance();
			sCl.setTime(sDate);
			String endDate = DateUtil.format(endTime,"yyyy-MM-dd");
			Date eDate = DateUtil.formatStringToDate(endDate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			Calendar eCl = Calendar.getInstance();
			eCl.setTime(eDate);
			
			// 查询条件
			BasicDBObject query = new BasicDBObject();
			query.put("gpsTime", new BasicDBObject("$gte", startTime).append(
					"$lte", endTime));
			query.put("carId", carId);
			
			// 查询指定字段
			BasicDBObject keys = new BasicDBObject();
			keys.put("mile", 1);
			keys.put("gpsTime", 1);
			keys.put("oil", 1);
			
			String startColletion = "historyGps_"+DateUtil.format(startTime, "yyyyMMdd");
			String endColletion = "historyGps_"+DateUtil.format(endTime, "yyyyMMdd");
			
			//查询时间段内的该车辆的第一条历史数据 得到开始里程
			DBCollection startHistory = db.getCollection(startColletion);
			DBCursor startCur = startHistory.find(query,keys).sort(
					new BasicDBObject("gpsTime", 1)).limit(1);
			CarGps sGps = null;
			DBObject startObj = null;
			if(startCur.hasNext())
				startObj = startCur.next();
			if(startObj != null){
				sGps = new CarGps();
				sGps.setCarId(carId);
				sGps.setMile((Double)startObj.get("mile"));
				sGps.setGpsDate((Date)startObj.get("gpsTime"));
				sGps.setOil((Double)startObj.get("oil"));
			}
			list.add(sGps);
			//查询时间段内的该车辆的最后一条历史数据 得到结束里程
			DBCollection endHistory = db.getCollection(endColletion);
			DBCursor endCur = endHistory.find(query,keys).sort(
					new BasicDBObject("gpsTime", -1)).limit(1);
			CarGps eGps = null;
			DBObject endObj = null;
			if(endCur.hasNext())
				endObj = endCur.next();
			if(endObj != null){
				eGps = new CarGps();
				eGps.setCarId(carId);
				eGps.setMile((Double)endObj.get("mile"));
				eGps.setGpsDate((Date)endObj.get("gpsTime"));
				eGps.setOil((Double)endObj.get("oil"));
			}
			list.add(eGps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
