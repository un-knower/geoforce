package com.supermap.egispservice.geocoding.service.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoRegion;
import com.supermap.data.Point2Ds;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.SpatialQueryMode;
import com.supermap.egispservice.geocoding.config.Config;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.search.SearchByUDB;

public class GeocodingServiceImpl implements IGeocodingService {

	/**
	 * 使用UDB数据查询行政区划相关数据
	 */
	private static SearchByUDB searchByUDB = new SearchByUDB();
	
	@Autowired
	private Config config;
	
	private static String[] RETURN_FIELDS = new String[]{"PROVINCE","CITY2","COUNTY"};
	
	private static String[] RETURN_ADMINCODE_FIELDS = new String[]{"ADMINCODE","PROVINCE","CITY2","COUNTY"};
	
	@Override
	public Map<String, String> searchForCounty(double smx, double smy) {
		return searchByUDB.searchByPoint(smx, smy, null, RETURN_FIELDS);
	}

	@Override
	public List<Map<String, String>> searchCountyByAdmincode(String code) {
		if(code.length() < 3){
			throw new NullPointerException("查询Admincode不允许少于3位");
		}
		return searchByUDB.searchCountyByCodePrefix(code);
	}

	private static final Logger LOGGER = Logger.getLogger(GeocodingServiceImpl.class);
	
	@Override
	public List<Map<String, Object>> getAdminElement(String admincode, int level) {
		LOGGER.info("## query parameter ["+admincode+","+level+"]");
		return searchByUDB.getAdminElements(admincode, level);
	}

	@Override
	public Map<String, Object> getAdminGeoByCode(String admincode, int level) {
		LOGGER.info("## getAdminGeoByCode["+admincode+","+level+"]");
		Map<String,Object> adminGeo = null;
		switch (level) {
		case Config.ADMIN_LEVEL_PROVINCE:
			adminGeo = getAdminGeo(admincode,Config.ADMIN_LEVEL_PROVINCE);
			break;
		case Config.ADMIN_LEVEL_CITY:
			adminGeo = getAdminGeo(admincode,Config.ADMIN_LEVEL_CITY);
			break;
		case Config.ADMIN_LEVEL_COUNTY:
			adminGeo = getAdminGeo(admincode,Config.ADMIN_LEVEL_COUNTY);
			break;
		case Config.ADMIN_LEVEL_TOWN:
			adminGeo = getAdminGeo(admincode,Config.ADMIN_LEVEL_TOWN);
			break;
		}
		return adminGeo;
	}

	
	/**
	 * 
	 * <p>Title ：getAdminGeo</p>
	 * Description：		根据行政区划代码获取个级别行政区划对象
	 * @param admincode
	 * @param level
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-7-29 下午02:24:42
	 */
	private Map<String,Object> getAdminGeo(String admincode,int level){
		
		String subAdmin = null;
		DatasetVector dv = null;
		if(Config.ADMIN_LEVEL_PROVINCE == level){
			subAdmin = admincode.substring(0,2);
			dv = searchByUDB.getDatasectByName(config.getDatasetNameProvince());
		}else if(Config.ADMIN_LEVEL_CITY == level){
			subAdmin = admincode.substring(0,4);
			dv = searchByUDB.getDatasectByName(config.getDatasetNameCity());
		}else if(Config.ADMIN_LEVEL_COUNTY == level){
			subAdmin = admincode.substring(0, 6);
			dv = searchByUDB.getDatasectByName(config.getDatasetNameCounty());
		}else{
			subAdmin = admincode;
			dv = searchByUDB.getDatasectByName(config.getDatasetNameTown());
		}
		QueryParameter qp = new QueryParameter();
		if(Config.ADMIN_LEVEL_TOWN == level){//乡镇
			qp.setAttributeFilter("ADMINCODE_MZ like '"+subAdmin+"%'");
		}else{
			qp.setAttributeFilter("ADMINCODE like '"+subAdmin+"%'");
		}
		qp.setHasGeometry(true);
		Recordset recordset = null;
		GeoRegion region = null;
		Map<String,Object> resultMap = null;
		try {
			recordset = dv.query(qp);
			if(null != recordset && recordset.getRecordCount() > 0){
				resultMap = new HashMap<String,Object>();
				recordset.moveFirst();
				region = (GeoRegion) recordset.getGeometry();
//				double x = recordset.getDouble("x");
//				if(x > 0){
//					double y = recordset.getDouble("y");
//					resultMap.put("x", x);
//					resultMap.put("y", y);
//				}else{
					resultMap.put("x", region.getInnerPoint().getX());
					resultMap.put("y", region.getInnerPoint().getY());
//				}
				Map<String,Object> geoMap = getGeo(region);
				resultMap.put("geo", geoMap);
				if(Config.ADMIN_LEVEL_TOWN == level){//乡镇
					resultMap.put("admincode", recordset.getString("ADMINCODE_MZ"));
				}else{
					resultMap.put("admincode", recordset.getString("ADMINCODE"));
				}
				
				if(Config.ADMIN_LEVEL_TOWN == level){
					resultMap.put("town", recordset.getString("TOWN"));
				}
				else{
					String abbr = recordset.getString("ABBREVIATION");
					if(Config.ADMIN_LEVEL_PROVINCE == level){
						resultMap.put("province", abbr);
					}else if(Config.ADMIN_LEVEL_CITY == level){
						String[] abbrItems = abbr.split(",");
						resultMap.put("city", abbrItems[1]);
					}else if(Config.ADMIN_LEVEL_COUNTY == level){
						String[] abbrItems = abbr.split(",");
						if(abbrItems.length < 3){
							resultMap.put("county", abbrItems[1]);
						}else{
							resultMap.put("county", abbrItems[2]);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			if(null != region){
				region.dispose();
			}
			if(null != recordset){
				recordset.close();
			}
		}
		return resultMap;
	}
	

	
	
	/**
	 * 
	 * <p>Title ：getGeo</p>
	 * Description：		获取几何对象
	 * @param region
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-7-29 上午11:34:45
	 */
	private Map<String,Object> getGeo(GeoRegion region){
		int[] parts = new int[region.getPartCount()];
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> points = new LinkedList<Map<String,Object>>();
		DecimalFormat format = new DecimalFormat("##########.##");
		for(int i=0;i<region.getPartCount();i++){
			Point2Ds point2Ds = region.getPart(i);
			parts[i] = point2Ds.getCount();
			for(int j=0;j<point2Ds.getCount();j++){
				Map<String,Object> point = new HashMap<String,Object>();
				point.put("x", format.format(point2Ds.getItem(j).getX()));
				point.put("y", format.format(point2Ds.getItem(j).getY()));
				points.add(point);
			}
		}
		resultMap.put("points", points);
		resultMap.put("parts", parts);
		return resultMap;
	}

	@Override
	public Map<String, Object> getCountyByAdmincode(String admincode) {
		DatasetVector dv = null;
		String temAdmincode = admincode.replaceAll("0+$", "");
		//区县数据集
		if(temAdmincode.length()>0&&temAdmincode.length()<=6){
			dv=searchByUDB.getDatasectByName(config.getDatasetNameCounty());
		}
		//乡镇数据集
		else{
			dv=searchByUDB.getDatasectByName(config.getDatasetNameTown());
		}
		if(temAdmincode.length()<2&&temAdmincode.length()>0){
			admincode=temAdmincode+"0";
		}
		if(temAdmincode.length()>=2&&temAdmincode.length() < 5){
			if(temAdmincode.length()==3){
				admincode=temAdmincode+"0";
			}else admincode = temAdmincode;
		}
		if(temAdmincode.length()>6){//乡镇
			if(temAdmincode.length()==7){
				admincode=temAdmincode+"0";
			}else admincode = temAdmincode;
		}
		QueryParameter qp = new QueryParameter();
		String filter = null;
		if(admincode.length() < 6){
			filter = "ADMINCODE like '"+admincode+"%'";
			qp.setAttributeFilter(filter);
			if(admincode.length() < 3){
				qp.setGroupBy(new String[]{"PROVINCE"});
			}else{
				qp.setGroupBy(new String[]{"CITY"});
			}
			qp.setOrderBy(new String[]{"ADMINCODE"});
		}else if(admincode.length()>6){//乡镇
			filter = "ADMINCODE_MZ like '"+admincode+"%'";
			qp.setAttributeFilter(filter);
			qp.setOrderBy(new String[]{"ADMINCODE_MZ asc"});
		}else{
			filter = "ADMINCODE='"+admincode+"'";
			qp.setAttributeFilter(filter);
		}
		qp.setHasGeometry(false);
		qp.setSpatialQueryMode(SpatialQueryMode.NONE);
		qp.setCursorType(CursorType.STATIC);
		LOGGER.info("## 查询区县["+filter+"]");
		Recordset recordset = dv.query(qp);
		Map<String,Object> map = new HashMap<String,Object>();
		if(null != recordset && recordset.getRecordCount() > 0){
			recordset.moveFirst();
			String province = recordset.getString("PROVINCE");
			map.put("PROVINCE", province);
			if(admincode.length() > 2){
				String city = recordset.getString("CITY");
				map.put("CITY", city);
			}
			if(admincode.length() > 4){
				String county = recordset.getString("COUNTY");
				map.put("COUNTY", county);
			}
			if(admincode.length()>6){
				String town=recordset.getString("TOWN");
				map.put("TOWN", town);
			}
		}
		if(null!=recordset){
			recordset.close();
		}
		return map;
	}

	@Override
	public Map<String, String> searchAdmincodeForCounty(double smx, double smy) {
		return searchByUDB.searchByPoint(smx, smy, null, RETURN_ADMINCODE_FIELDS);
	}


}
