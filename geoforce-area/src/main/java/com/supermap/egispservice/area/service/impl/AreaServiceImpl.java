package com.supermap.egispservice.area.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.FieldInfos;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.Point2Ds;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.SpatialQueryMode;
import com.supermap.data.Workspace;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.PageQueryResult;
import com.supermap.egispservice.area.Point2D;
import com.supermap.egispservice.area.constants.AreaFieldNames;
import com.supermap.egispservice.area.constants.EgispAreaConstants;
import com.supermap.egispservice.area.exceptions.AreaException;
import com.supermap.egispservice.area.exceptions.AreaException.ExceptionType;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.area.utils.AreaHelper;
import com.supermap.egispservice.area.utils.CommonUtil;
import com.supermap.egispservice.area.utils.DatasetUtils;
import com.supermap.egispservice.area.utils.GeometristUtil;
import com.supermap.egispservice.area.utils.ZipExtend;
import com.supermap.egispservice.area.utils.CommonUtil.DateType;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.utils.AppPropertiesUtil;
import com.supermap.utils.DBUtil;

@Component("areaService")
public class AreaServiceImpl implements IAreaService {

	@Resource
	private AreaHelper areaHelper = null;
	
	@Resource
	private IGeocodingService geocodingService;
	
	
	private static Logger LOGGER = Logger.getLogger(AreaServiceImpl.class);
	
	/**
	 * 操作状态为新增
	 */
	private static final int OPT_TYPE_SAVE=1;
	/**
	 * 操作状态为修改
	 */
	private static final int OPT_TYPE_UPDATE=2;
	/**
	 * 操作状态为删除
	 */
	private static final int OPT_TYPE_DELETE=3;
	
	/**
	 *  操作状态的字段名称
	 */
	private static final String OPT_TYPE_FIELDNAME="OPT_TYPE";
	
	/**
	 *  保存删除的区划的数据集名称
	 */
	private static final String DATASET_AREA_NAME_DELETE="BIZ_AREA_DELETE";
	
	/**
	 * 保存删除的区划到另一张表的企业ID列表
	 * eids.add("8a04a77b5962181a015b234dbbd75633");//海尔8a04a77b5962181a015b234dbbd85634 张京涛
	   eids.add("8a04a77b5962181a015b232c2c255622");//海尔8a04a77b5962181a015b232c2c255623 吕德逞
	 */
	private static final List<String> eids=Arrays.asList("8a04a77b5962181a015b234dbbd75633","8a04a77b5962181a015b232c2c255622");
	
	
	@Override
	public String addArea(String areaName, String areaNumber, String netId,String admincode, String userId, String enterpriseId,
			String dcode, com.supermap.egispservice.area.Point2D[] point2Ds,String wgzCode,String wgzName,String lineCode,String lineName) throws AreaException {
		if(null == point2Ds || point2Ds.length <= 0){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW, "point2Ds");
		}
		// 判断是否存在重复的区划编号或名称，存在即会抛出异常
		existAreaNameOrNumber(areaName, areaNumber, enterpriseId, dcode,null);
		
		GeoRegion newRegion = new GeoRegion(new Point2Ds(convert(point2Ds)));
//		String filter = AreaFieldNames.ADMINCODE+"='"+admincode+"'";
		// 查询是否有相交的区域面
		Recordset intersectRecord = queryIntersectGeoRegion(newRegion, EgispAreaConstants.DATASET_AREA_NAME, enterpriseId, dcode,null);
		if(null != intersectRecord && intersectRecord.getRecordCount() > 0){
			LOGGER.info("## 存在重叠区域，需要对区域面进行裁剪");
			for(intersectRecord.moveFirst();!intersectRecord.isEOF();intersectRecord.moveNext()){
				Geometry geometry = intersectRecord.getGeometry();
				if(null != geometry){
					if(newRegion == null){
						break;
					}
					newRegion = (GeoRegion) Geometrist.erase(newRegion, geometry);
				}
			}
		}
		AreaHelper.closeRecordset(intersectRecord);
		if(null == newRegion || newRegion.getArea() < 500){
			throw new AreaException(ExceptionType.OTHER,"经过裁剪之后，面为空或过小，添加失败");
		}
		boolean isAddSuccess = false;
		String id = null;
		Recordset toSaveRecordset = null;
		try{
			toSaveRecordset = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME).getRecordset(false, CursorType.DYNAMIC);
			toSaveRecordset.addNew(newRegion);
			toSaveRecordset.setString(AreaFieldNames.AREANUMBER, areaNumber);
			toSaveRecordset.setString(AreaFieldNames.CREATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
			toSaveRecordset.setString(AreaFieldNames.DEPARTENT_CODE, dcode);
			toSaveRecordset.setString(AreaFieldNames.ENTERPRISE_ID, enterpriseId);
			toSaveRecordset.setString(AreaFieldNames.NAME, areaName);
			toSaveRecordset.setString(AreaFieldNames.NET_ID, netId);
			toSaveRecordset.setString(AreaFieldNames.USER_ID, userId);
			toSaveRecordset.setString(AreaFieldNames.ADMINCODE, admincode);
			toSaveRecordset.setString(AreaFieldNames.ID, CommonUtil.getUUID());
			toSaveRecordset.setString(AreaFieldNames.WGZ_CODE, wgzCode);//网格组编码
			toSaveRecordset.setString(AreaFieldNames.WGZ_NAME, wgzName);//网格组名称
			toSaveRecordset.setString(AreaFieldNames.LINE_CODE, lineCode);//线路编码
			toSaveRecordset.setString(AreaFieldNames.LINE_NAME, lineName);//线路名称
			toSaveRecordset.setInt32(OPT_TYPE_FIELDNAME, OPT_TYPE_SAVE);//操作状态为新增
			isAddSuccess = toSaveRecordset.update();
			if(isAddSuccess){
				id = toSaveRecordset.getString(AreaFieldNames.ID);
			}else{
				throw new Exception("保存区域面失败");
			}
		}catch(Exception e){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,e.getMessage());
		}finally{
			AreaHelper.closeRecordset(toSaveRecordset);
		}
			return id;
	}
	
	/**
	 * 
	 * <p>Title ：convert</p>
	 * Description：		将点转换为ObJectJava点对象
	 * @param point2ds
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-12 上午10:16:27
	 */
	private com.supermap.data.Point2D[] convert(com.supermap.egispservice.area.Point2D[] point2ds){
		com.supermap.data.Point2D[] point2Ds = null;
		if(null != point2ds){
			point2Ds = new com.supermap.data.Point2D[point2ds.length];
			for(int i=0;i<point2ds.length;i++){
				point2Ds[i] = new com.supermap.data.Point2D(point2ds[i].getX(),point2ds[i].getY());
			}
		}
		return point2Ds;
	}
	
	
	
	
	/**
	 * 
	 * <p>Title ：queryIntersectGeoRegion</p>
	 * Description： 通过给定的面查询相交的区域面
	 * @param geo
	 * @param datasetName
	 * @param enterpriseId
	 * @param departmentId
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-9-10 上午10:19:57
	 */
	public Recordset queryIntersectGeoRegion(Geometry geo,String datasetName,String enterpriseId,String dcode,String filter) throws AreaException {
		Recordset intersectRecordset = null;
		try{
			
			DatasetVector datasetVector = areaHelper.getDatasetVector(datasetName);
			LOGGER.info("datasetVector:" + datasetVector.getRecordCount());
			QueryParameter parameter = new QueryParameter();
			parameter.setSpatialQueryObject(geo);
			parameter.setSpatialQueryMode(SpatialQueryMode.INTERSECT);
			StringBuilder filterBuilder = new StringBuilder();
			if(!CommonUtil.isStringEmpty(filter)){
				filterBuilder.append(filter).append(" and ");
			}
			filterBuilder.append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append("' and "+AreaFieldNames.DELETE_FLAG+"!=1");
			filterBuilder.append(" and "+AreaFieldNames.DEPARTENT_CODE+" like '").append(dcode).append("%'");
			String sqlFilter = filterBuilder.toString();
			LOGGER.info("## queryIntersectGeoRegion : "+sqlFilter);
			parameter.setAttributeFilter(sqlFilter);
			parameter.setCursorType(CursorType.STATIC);
			parameter.setOrderBy(new String[]{AreaFieldNames.ADMINCODE+" asc"});
			intersectRecordset = datasetVector.query(parameter);
			LOGGER.info("intersectRecordset:" + intersectRecordset.getRecordCount());
		}catch(Exception e){
			LOGGER.error("## 查询相交面发生异常"+e.getMessage(),e);
			throw new AreaException(ExceptionType.INNER_EXCEPTION,e.getMessage());
		}
		return intersectRecordset;
	}
	



	@Override
	public AreaEntity queryByIdOrNumber(String areaId, String areaNumber, String decode,boolean isNeedPoints) throws AreaException {
		StringBuilder filterBuilder = new StringBuilder();
		boolean isNeedQuery =false;
		if(CommonUtil.isStringEmpty(decode) && StringUtils.isEmpty(areaId)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,decode);
		}
		// 组装区域ID
		if(!CommonUtil.isStringEmpty(areaId)){
			filterBuilder.append(AreaFieldNames.ID).append("='").append(areaId).append("'");
			isNeedQuery = true;
		}
		// 组装区域编号
		if(!CommonUtil.isStringEmpty(areaNumber)){
			if(isNeedQuery){
				filterBuilder.append(" and ");
			}
			filterBuilder.append(AreaFieldNames.AREANUMBER).append("='").append(areaNumber).append("'");
			isNeedQuery = true;
		}
		AreaEntity areaEntity  = null;
		if(isNeedQuery){
			if(!StringUtils.isEmpty(decode)){
				/*filterBuilder.append(" and ").append(AreaFieldNames.DEPARTENT_CODE).append("='").append(decode)
				.append("'");*/
				filterBuilder.append(" and ").append(AreaFieldNames.DEPARTENT_CODE).append(" like '").append(decode).append("%'");
			}
			String filter = filterBuilder.toString();
			LOGGER.info("## 查询过滤条件["+filter+"]");
			DatasetVector datasetVector = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
			if(null == datasetVector ){
				throw new AreaException(ExceptionType.OTHER,"服务内部异常，获取数据集["+EgispAreaConstants.DATASET_AREA_NAME+"]为空");
			}
			Recordset recordset = datasetVector.query(filter, CursorType.STATIC);
			areaEntity = parseResult(recordset,isNeedPoints);
			AreaHelper.closeRecordset(recordset);
		}
		return areaEntity;
	}
	
	
	/**
	 * 
	 * <p>Title ：parseResult</p>
	 * Description：		解析结果
	 * @param recordset
	 * @param isNeedPoint
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-10 下午01:34:12
	 */
	private AreaEntity parseResult(Recordset recordset,boolean isNeedPoint){
		List<AreaEntity> results = parseResults(recordset, isNeedPoint);
		return (results == null || results.size() <= 0)?null:results.get(0);
	}
	
	/**
	 * 
	 * <p>Title ：getPoints</p>
	 * Description： 解析面对象的点串
	 * @param region
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-10 上午11:11:52
	 */
	private com.supermap.egispservice.area.Point2D[] getPoints(GeoRegion region,AreaEntity ae){
		List<com.supermap.egispservice.area.Point2D> points = new ArrayList<com.supermap.egispservice.area.Point2D>();
		int[] parts = new int[region.getPartCount()];
		for(int i=0;i<region.getPartCount();i++){
			Point2Ds point2Ds = region.getPart(i);
			parts[i] = point2Ds.getCount();
			for(int j=0;j<point2Ds.getCount();j++){
				points.add(new com.supermap.egispservice.area.Point2D(point2Ds.getItem(j).getX(),point2Ds.getItem(j).getY()));
			}
		}
		com.supermap.egispservice.area.Point2D[] point2ds = null;
		if(points.size() > 0){
			point2ds = new com.supermap.egispservice.area.Point2D[points.size()];
			points.toArray(point2ds);
		}
		ae.setParts(parts);
		return point2ds;
	}
	
	/**
	 * 
	 * @Title: formatPoint 格式化点
	 * @param Point2D
	 * @return
	 * @throws
	 */
	private com.supermap.egispservice.area.Point2D[] formatPoint(com.supermap.egispservice.area.Point2D[] point2Ds) {
		try {
			DecimalFormat format = new DecimalFormat("##########.##");
			format.setMaximumFractionDigits(2);
			for (com.supermap.egispservice.area.Point2D point : point2Ds) {
				point.setX(Double.valueOf(format.format(point.getX())).doubleValue());
				point.setY(Double.valueOf(format.format(point.getY())).doubleValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return point2Ds;
	}

	
	/**
	 * 
	 * <p>Title ：parseResults</p>
	 * Description：解析结果集，返回结果集中所有结果
	 * @param recordset
	 * @param isNeedPoints
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-10 下午02:06:08
	 */
	public List<AreaEntity> parseResults(Recordset recordset,boolean isNeedPoint){
		List<AreaEntity> results = null;
		if(null != recordset && recordset.getRecordCount() > 0){
			results = new ArrayList<AreaEntity>();
			for(recordset.moveFirst();!recordset.isEOF();recordset.moveNext()){
				AreaEntity entity = new AreaEntity();
				entity.setAreaNumber(recordset.getString(AreaFieldNames.AREANUMBER));
				entity.setCreate_time(recordset.getString(AreaFieldNames.CREATE_TIME));
				entity.setDelete_flag(recordset.getInt32(AreaFieldNames.DELETE_FLAG));
				entity.setDcode(recordset.getString(AreaFieldNames.DEPARTENT_CODE));
				entity.setEnterprise_id(recordset.getString(AreaFieldNames.ENTERPRISE_ID));
				entity.setId(recordset.getString(AreaFieldNames.ID));
				entity.setName(recordset.getString(AreaFieldNames.NAME));
				entity.setNet_Id(recordset.getString(AreaFieldNames.NET_ID));
				entity.setUpdate_time(recordset.getString(AreaFieldNames.UPDATE_TIME));
				entity.setUser_id(recordset.getString(AreaFieldNames.USER_ID));
				entity.setAdmincode(recordset.getString(AreaFieldNames.ADMINCODE));
				
				//区划状态，关联的区划信息
				int areastatus=recordset.getInt32(AreaFieldNames.AREA_STATUS);
				entity.setArea_status(areastatus);
				if(areastatus==1){//如果是停用状态，则去查询关联的区划
					String relationid=recordset.getString(AreaFieldNames.RELATION_AREAID);
					if(!CommonUtil.isStringEmpty(relationid)){
						entity.setRelation_areaid(relationid);
						List<Map<String,String>> relationlist=findRelationAreaAttrs(recordset.getString(AreaFieldNames.ID),relationid);
						if(relationlist!=null&&relationlist.size()>0){
							entity.setRelation_areaname(relationlist.get(relationlist.size()-1).get(AreaFieldNames.NAME));
							entity.setRelation_areanumber(relationlist.get(relationlist.size()-1).get(AreaFieldNames.AREANUMBER));
						}
					}
				}
				
				//网格组线路信息
				entity.setWgzCode(recordset.getString(AreaFieldNames.WGZ_CODE));
				entity.setWgzName(recordset.getString(AreaFieldNames.WGZ_NAME));
				entity.setLineCode(recordset.getString(AreaFieldNames.LINE_CODE));
				entity.setLineName(recordset.getString(AreaFieldNames.LINE_NAME));
				
				
				if(isNeedPoint){
					GeoRegion region = (GeoRegion) recordset.getGeometry();
					if(null != region){
						com.supermap.data.Point2D p = region.getInnerPoint();
						entity.setCenter(new com.supermap.egispservice.area.Point2D(p.getX(),p.getY()));
						com.supermap.egispservice.area.Point2D[] point2Ds = getPoints(region,entity);
						// 抽稀点串
						point2Ds = formatPoint(point2Ds);
						entity.setPoints(point2Ds);
						AreaHelper.closeGeometry(region);
					}
				}
				results.add(entity);
			}
		}
		return results;
	}





	@Override
	public List<AreaEntity> queryByEnOrDe(String areaName,String areaNumber,String admincode,String enterpriseId, String dcode,boolean isNeedPoint) throws AreaException {
		boolean isNeedQuery = false;
		StringBuilder filterBuilder = new StringBuilder();
		
		if(StringUtils.isEmpty(enterpriseId) && StringUtils.isEmpty(dcode)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,AreaFieldNames.DEPARTENT_CODE+"或"+AreaFieldNames.ENTERPRISE_ID);
		}
		
		if(!CommonUtil.isStringEmpty(enterpriseId)){
			filterBuilder.append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append("' ");
			isNeedQuery = true;
		}
		
		if(!CommonUtil.isStringEmpty(admincode) && admincode.length() >= 2){
			if(isNeedQuery){
				filterBuilder.append(" and (");
			}else{
				filterBuilder.append(" ( ");
			}
			filterBuilder.append(AreaFieldNames.ADMINCODE).append(" like '").append(admincode).append("%' or ").append(
					AreaFieldNames.ADMINCODE).append(" is null or ").append(AreaFieldNames.ADMINCODE).append("='')");
			isNeedQuery = true;
		}
		
		if(!StringUtils.isEmpty(areaName) || !StringUtils.isEmpty(areaNumber)){
			if(isNeedQuery){
				filterBuilder.append(" and ");
			}
			filterBuilder.append(" ( ");
			boolean isNameQuery = false;
			if(!StringUtils.isEmpty(areaName)){
				filterBuilder.append(AreaFieldNames.NAME).append(" like '%").append(areaName.replaceAll("_", "/_").replaceAll("%", "/_")).append("%' escape '/'");
				isNameQuery = true;
			}
			if(!StringUtils.isEmpty(areaNumber)){
				if(isNameQuery){
					filterBuilder.append(" or ");
				}
				filterBuilder.append(AreaFieldNames.AREANUMBER).append(" like '%").append(areaNumber.replaceAll("_", "/_").replaceAll("%", "/%'")).append("%' escape '/'");
			}
			filterBuilder.append(" ) ") ;
			isNeedQuery = true;
		}
		
		if(!CommonUtil.isStringEmpty(dcode)){
			if(isNeedQuery){
				filterBuilder.append(" and ");
			}
			filterBuilder.append(AreaFieldNames.DEPARTENT_CODE).append(" like '").append(dcode).append("%'");
			isNeedQuery = true;
		}
//		filterBuilder.append(" order by ").append(AreaFieldNames.CREATE_TIME).append(" DESC,").append(
//				AreaFieldNames.UPDATE_TIME).append(" DESC");
		
		DatasetVector dv = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if(dv == null){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,"DatasetVector为空");
		}
		String filter = filterBuilder.toString();
		LOGGER.info("## 查询过滤条件["+filter+"]");
		QueryParameter qp = new QueryParameter();
		qp.setAttributeFilter(filter);
		qp.setOrderBy(new String[]{AreaFieldNames.CREATE_TIME+" desc",AreaFieldNames.UPDATE_TIME+" desc"});
		qp.setCursorType(CursorType.STATIC);
		qp.setHasGeometry(isNeedPoint);
		Recordset recordset = dv.query(qp);
//		Recordset recordset = dv.query(filter, CursorType.STATIC);
		
		List<AreaEntity> results = parseResults(recordset, isNeedPoint);
		AreaHelper.closeRecordset(recordset);
		return results;
	}



	@Override
	public boolean updateAreaAttribution(String id, String areaName, String areaNumber, String netId,String admincode, int areaStatus,
			String relationAreaid,String wgzCode,String wgzName,String lineCode,String lineName) throws AreaException {
		if(CommonUtil.isStringEmpty(id)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW, AreaFieldNames.ID);
		}
		AreaEntity ae = this.queryByIdOrNumber(id, null, null, false);
		if(null == ae){
			throw new AreaException(ExceptionType.NOT_FOUND,AreaFieldNames.ID);
		}
		
		String eid = ae.getEnterprise_id();
		String dcode = ae.getDcode();
		
		boolean isNeedUpdate = false;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update ").append(EgispAreaConstants.DATASET_AREA_NAME).append(" set ");
		// 不允许重复的区域名称和编号
		if(!StringUtils.isEmpty(areaName) || !StringUtils.isEmpty(areaNumber)){
			isNeedUpdate = true;
			existAreaNameOrNumber(areaName, areaNumber, eid, dcode,ae.getId());
		}else{
			isNeedUpdate = true;
		}
		
		// 修改区域名称
		if(!CommonUtil.isStringEmpty(areaName)){
			sqlBuilder.append(AreaFieldNames.NAME).append("='").append(areaName).append("',");
		}
		// 修改区域编号
		if(!CommonUtil.isStringEmpty(areaNumber)){
			sqlBuilder.append(AreaFieldNames.AREANUMBER).append("='").append(areaNumber).append("',");
		}
		// 修改网点
		if(!CommonUtil.isStringEmpty(netId)){
			sqlBuilder.append(AreaFieldNames.NET_ID).append("='").append(netId).append("',");
			isNeedUpdate = true;
		}
		//修改所属行政区
		if(!CommonUtil.isStringEmpty(admincode)){
			sqlBuilder.append(AreaFieldNames.ADMINCODE).append("='").append(admincode).append("',");
			isNeedUpdate = true;
		}
		
		//网格组编码
		if(!CommonUtil.isStringEmpty(wgzCode)){
			sqlBuilder.append(AreaFieldNames.WGZ_CODE).append("='").append(wgzCode).append("',");
			isNeedUpdate = true;
		}
		
		//网格组名称
		if(!CommonUtil.isStringEmpty(wgzName)){
			sqlBuilder.append(AreaFieldNames.WGZ_NAME).append("='").append(wgzName).append("',");
			isNeedUpdate = true;
		}
		//线路编码
		if(!CommonUtil.isStringEmpty(lineCode)){
			sqlBuilder.append(AreaFieldNames.LINE_CODE).append("='").append(lineCode).append("',");
			isNeedUpdate = true;
		}
		//线路名称
		if(!CommonUtil.isStringEmpty(lineName)){
			sqlBuilder.append(AreaFieldNames.LINE_NAME).append("='").append(lineName).append("',");
			isNeedUpdate = true;
		}
		
		
		//修改区划状态
		if(areaStatus>=0){
			sqlBuilder.append(AreaFieldNames.AREA_STATUS).append("='").append(areaStatus).append("',");
			if(areaStatus==0){//区划状态更改为正常状态，则该区划关联的区划自动解除关联
				sqlBuilder.append(AreaFieldNames.RELATION_AREAID).append("='' ").append(",");
			}
			isNeedUpdate = true;
		}
		
		//修改关联的区划id，只有区划状态是停用的时候才修改
		if(!CommonUtil.isStringEmpty(relationAreaid)&&areaStatus==1){
			sqlBuilder.append(AreaFieldNames.RELATION_AREAID).append("='").append(relationAreaid).append("',");
			isNeedUpdate = true;
		}
		
		Statement statement = null;
		Connection conn = null;
		try{
			if(isNeedUpdate){
				sqlBuilder.append(" OPT_TYPE='2', ");//操作状态为修改状态
				sqlBuilder.append(AreaFieldNames.UPDATE_TIME).append("='").append(CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP)).append("'");
				String sql = sqlBuilder.toString();
				sql = sql + " where "+AreaFieldNames.ID+"='"+id+"'";
				LOGGER.info("## 更新区域属性SQL : ["+sql+"]");
				conn = DBUtil.getConnection();
				statement = conn.createStatement();
				int count = statement.executeUpdate(sql);
				if(count > 0){
					return true;
				}
			}
		}catch(Exception e){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,e.getMessage());
		}finally{
			if(statement != null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	
	
	@Override
	public boolean updateAreaRegion(String id, com.supermap.egispservice.area.Point2D[] point2Ds,List<Integer> parts) throws AreaException {
		if (null == point2Ds || point2Ds.length <= 0) {
			throw new AreaException(ExceptionType.NULL_NO_ALLOW, "point2Ds");
		}
		if (CommonUtil.isStringEmpty(id)) {
			throw new AreaException(ExceptionType.NULL_NO_ALLOW, AreaFieldNames.ID);
		}
		DatasetVector dv = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if (null == dv) {
			throw new AreaException(ExceptionType.INNER_EXCEPTION, EgispAreaConstants.DATASET_AREA_NAME + "为空");
		}
		boolean isUpdateSucces = false;
		GeoRegion region = null;
		Recordset recordset = null;
		try {
			// 查询指定区域面
			recordset = dv.query(AreaFieldNames.ID + "='" + id + "'", CursorType.DYNAMIC);
			if (null == recordset || recordset.getRecordCount() <= 0) {
				throw new AreaException(ExceptionType.NOT_FOUND, AreaFieldNames.ID + ":" + id);
			}
			recordset.moveFirst();
			region = getCompoundRegion(point2Ds, parts);
			
			if(null == region || region.getArea() <= 500){
				throw new AreaException(ExceptionType.OTHER,"区域面为空或区域面过小，不允许修改");
			}
			
			String enterpriseId = recordset.getString(AreaFieldNames.ENTERPRISE_ID);
			String dcode = recordset.getString(AreaFieldNames.DEPARTENT_CODE);
			String filter = AreaFieldNames.ID +"!='"+id+"'";
			Recordset intersectRecord = queryIntersectGeoRegion(region, EgispAreaConstants.DATASET_AREA_NAME, enterpriseId, dcode,filter);
			
			GeoRegion  earseregion=null;
			//擦除
			if(region.getPartCount()>=Double.parseDouble(EgispAreaConstants.REGION_PART_MIN)
					&&region.getArea()>=Double.parseDouble(EgispAreaConstants.REGION_AREA_MIN)){//用数据集方式擦除
				LOGGER.info("采用内存数据集擦除");
				earseregion=GeometristUtil.earseAdminGeo(region, intersectRecord);
			}else{//普通擦除
				LOGGER.info("采用面对象擦除");
				earseregion=GeometristUtil.earseAdminGeo(region, intersectRecord);
			}
			
			if(earseregion!=null){
				region.dispose();
				region=earseregion;
			}
			
			AreaHelper.closeRecordset(intersectRecord);
			//清理碎面
			region=GeometristUtil.cleanPiecedArea(region);
			// 更新区域面
			recordset.edit();
			recordset.setGeometry(region);
			recordset.setInt32(OPT_TYPE_FIELDNAME, OPT_TYPE_UPDATE);//操作状态为修改
			recordset.setInt32("WULIU_SYNC",0);
			recordset.setString(AreaFieldNames.UPDATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
			isUpdateSucces = recordset.update();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new AreaException(ExceptionType.INNER_EXCEPTION, e.getMessage());
		}finally{
			if(null != region){
				AreaHelper.closeGeometry(region);
			}
			if(null != recordset){
				AreaHelper.closeRecordset(recordset);
			}
		}
		return isUpdateSucces;
	}

	/**
	 * 
	 * <p>Title ：getCompoundPoints</p>
	 * Description：获取复合面
	 * @param point2Ds
	 * @param parts
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-1-16 上午11:09:58
	 */
	private GeoRegion getCompoundRegion(com.supermap.egispservice.area.Point2D[] point2Ds,List<Integer> parts){
		int j = 0;
		GeoRegion region = new GeoRegion();
		for (int i = 0; i < parts.size(); i++) {
			Point2Ds point2Dss = new Point2Ds();
			int count = parts.get(i);
			com.supermap.egispservice.area.Point2D[] partItems = new com.supermap.egispservice.area.Point2D[count];
			int index = 0;
			for (; j < point2Ds.length; ) {
				partItems[index] = point2Ds[j];
				index++;
				j++;
				if (index >= count) {
					break;
				}
			}
			point2Dss.addRange(convert(partItems));
			region.addPart(point2Dss);
		}
		return region;
	}
	
	
	
	@Override
	public boolean deleteRegion(String areaId) throws AreaException {
		if(CommonUtil.isStringEmpty(areaId)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,AreaFieldNames.ID);
		}
		DatasetVector dv = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if(null == dv){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,EgispAreaConstants.DATASET_AREA_NAME+"为空");
		}
		Recordset recordset = dv.query(AreaFieldNames.ID+"='"+areaId+"'", CursorType.DYNAMIC);
		//??
		Recordset toDelset = dv.query(AreaFieldNames.ID+"='"+areaId+"'", CursorType.STATIC);
		if(null == recordset || recordset.getRecordCount() <= 0){
			throw new AreaException(ExceptionType.NOT_FOUND,AreaFieldNames.ID+":"+areaId);
		}
		//记录待删除的面数据
		String eid=toDelset.getString(AreaFieldNames.ENTERPRISE_ID);
		GeoRegion region=null;
		AreaEntity area=new AreaEntity();
		if(isSaveDelEid(eid)){//判断是否指定需要保存删除的区划的企业
			region=(GeoRegion) toDelset.getGeometry();
			List<AreaEntity> areaList=parseResults(toDelset,true);
			if(null!=areaList&&areaList.size()>0){
				area=areaList.get(0);
			}
		}		
		//
		boolean isDeleteSuccess = false;
		try{
			// 删除
			recordset.edit();
			isDeleteSuccess = recordset.delete();
			//如果删除成功，则写入到删除的表
			if(isDeleteSuccess){
				saveDeleteAreaAttr(area,region);
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			throw new AreaException(ExceptionType.INNER_EXCEPTION,e.getMessage());
		}finally{
			LOGGER.info("## delete the area ["+areaId+"] success ? "+isDeleteSuccess);
			AreaHelper.closeRecordset(recordset);
			AreaHelper.closeRecordset(toDelset);
		}
		return isDeleteSuccess;
	}

	@Override
	public void existAreaNameOrNumber(String areaName, String areaNum, String enterpriseId, String dcode,String id)
			throws AreaException {
		String checkResult = CommonUtil.checkRequredParam(new String[] { AreaFieldNames.ENTERPRISE_ID,
				AreaFieldNames.DEPARTENT_CODE }, new String[] { enterpriseId, dcode });
		if(!CommonUtil.isStringEmpty(checkResult)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,checkResult);
		}
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select count(id) from ").append(EgispAreaConstants.DATASET_AREA_NAME);
		sqlBuilder.append(" where ").append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append(
				"' and ").append(AreaFieldNames.DEPARTENT_CODE).append(" like '").append(dcode).append("%'");
		boolean isNeedQuery = false;
		if(!CommonUtil.isStringEmpty(areaName)){
			sqlBuilder.append(" and (").append(AreaFieldNames.NAME).append("='").append(areaName).append("'");
			isNeedQuery = true;
		}
		if(!CommonUtil.isStringEmpty(areaNum)){
			if(isNeedQuery){
				sqlBuilder.append(" or ");
			}else{
				sqlBuilder.append(" and ");
			}
			sqlBuilder.append(AreaFieldNames.AREANUMBER).append("='").append(areaNum).append("'");
			if(isNeedQuery){
				sqlBuilder.append(")");
			}
			isNeedQuery = true;
		}else{
			sqlBuilder.append(")");
		}
		
		if(!isNeedQuery){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,"区划名称或区划编号不允许都为空");
		}
		Connection conn = DBUtil.getConnection();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			if(!StringUtils.isEmpty(id)){
				sqlBuilder.append(" and id<>'").append(id).append("'");
			}
			String sql = sqlBuilder.toString();
			LOGGER.info("## 查询SQL["+sql+"]");
			ResultSet resultset = statement.executeQuery(sql);
			if(resultset.next()){
				int count = resultset.getInt(1);
				if(count > 0){
					throw new AreaException("区划名称或区划编号已存在");
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new AreaException(e.getMessage());
		}finally{
			if(null != statement ){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean bindNet(String areaId, String netId) throws AreaException {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update ").append(EgispAreaConstants.DATASET_AREA_NAME).append(" set net_id='").append(netId)
				.append("' where id='").append(areaId).append("'");
		boolean isSuccess = false;
		Statement statement = null;
		try {
			Connection conn = DBUtil.getConnection();
			 statement = conn.createStatement();
			LOGGER.info("## 绑定网点["+sqlBuilder.toString()+"]");
			int count = statement.executeUpdate(sqlBuilder.toString());
			if(count > 0){
				isSuccess = true;
			}
		} catch (Exception e) {
			throw new AreaException(e.getMessage());
		}finally{
			if(null != statement ){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return isSuccess;
	}

	@Override
	public AreaEntity queryAreaByPoint(Point2D point, String enterpriseId, String dcode)throws AreaException {
		DatasetVector dv = this.areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		AreaEntity areaEntity = null;
		try{
			if(null == dv){
				throw new AreaException(ExceptionType.INNER_EXCEPTION, EgispAreaConstants.DATASET_AREA_NAME+"为空");
			}
			areaEntity = queryByPoint(point, enterpriseId, dcode, dv);
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			throw new AreaException(e.getMessage());
		}
		return areaEntity == null?new AreaEntity():areaEntity;
	}

	private AreaEntity queryByPoint(Point2D point,String enterpriseId,String dcode,DatasetVector datasetVector){
		AreaEntity result = null;
		Recordset recordset = null;
		long start=System.currentTimeMillis();
		try{
			if(point.getX() > 0 && point.getY() > 0){
				QueryParameter parameter = new QueryParameter();
				
				StringBuilder filtersb=new StringBuilder();
				filtersb.append(AreaFieldNames.ENTERPRISE_ID)
						.append("='")
						.append(enterpriseId)
						.append("' ");
				if(StringUtils.isNotEmpty(dcode)){
					filtersb.append(" and ")
							.append(AreaFieldNames.DEPARTENT_CODE)
							.append(" like '")
							.append(dcode)
							.append("%'");
				}
//				String filter = AreaFieldNames.ENTERPRISE_ID + "='" + enterpriseId + "' and " + AreaFieldNames.DEPARTENT_CODE
//				+ " like '" + dcode + "%'";
				LOGGER.debug("## queryByPoint filter ["+filtersb.toString()+"]");
				parameter.setAttributeFilter(filtersb.toString());
				parameter.setCursorType(CursorType.STATIC);
				parameter.setSpatialQueryMode(SpatialQueryMode.INTERSECT);
				parameter.setSpatialQueryObject(new com.supermap.data.Point2D(point.getX(),point.getY()));
				recordset = datasetVector.query(parameter);
				result = parseResult(recordset, false);
			}else{
				result = new AreaEntity();
			}
		}finally{
			AreaHelper.closeRecordset(recordset);
		}
		long end=System.currentTimeMillis();
		LOGGER.info("#####queryByPoint:every ---"+(end-start));
		return result;
	}
	
	@Override
	public List<AreaEntity> queryAreaByPoint(Point2D[] point, String enterpriseId, String dcode)throws AreaException {
		long startdv=System.currentTimeMillis();
		DatasetVector dv = this.areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if(null == dv){
			throw new AreaException(ExceptionType.INNER_EXCEPTION, EgispAreaConstants.DATASET_AREA_NAME+"为空");
		}
		List<AreaEntity> list = new ArrayList<AreaEntity>();
		for(int i=0;i<point.length;i++){
			AreaEntity areaEntity = queryByPoint(point[i], enterpriseId, dcode, dv);
			if(null == areaEntity){
				areaEntity = new AreaEntity();
			}
			list.add(areaEntity);
		}
		long end=System.currentTimeMillis();
		LOGGER.info("#####queryAreaByPoint:ALL---"+(end-startdv));
		return list;
	}

	@Override
	public PageQueryResult queryAreaByNamePage(String userId, String areaName, int pageNo, int pageSize,boolean isNeedPoints)
			throws AreaException {
		DatasetVector dv = this.areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if(null == dv){
			throw new AreaException(ExceptionType.INNER_EXCEPTION, EgispAreaConstants.DATASET_AREA_NAME+"为空");
		}
		String filter = AreaFieldNames.USER_ID + "='" + userId +"'";
//		areaName = areaName.replaceAll("_", "").replaceAll("%", "");
		if (!StringUtils.isEmpty(areaName)) {
			filter += " and " + AreaFieldNames.NAME + " like '%" + areaName.replaceAll("_", "/_").replaceAll("%", "/%") + "% escape '/''";
		}
		LOGGER.info("## 查询过滤条件["+filter+"]");
		Recordset recordset = dv.query(filter, CursorType.STATIC);
		int records = 0;
		int total = 0;
		PageQueryResult result = new PageQueryResult();
		
		if(recordset != null){
			records = recordset.getRecordCount();
			LOGGER.info("## 查询到结果:"+records);
			result.setRecords(records);
			if(records > 0){
				total = records / pageSize;
				if(records % pageSize > 0){
					total ++;
				}
				result.setTotal(total);
				if(pageNo > total){
					pageNo = total;
				}
				result.setPage(pageNo);
				// 解析查询结果
				List<AreaEntity> areaEntitys = parseResults(recordset, isNeedPoints);
				int startIndex = (pageNo -1) * pageSize;
				int endIndex = startIndex + pageSize;
				if(endIndex > areaEntitys.size()){
					endIndex = areaEntitys.size();
				}
				areaEntitys = areaEntitys.subList(startIndex, endIndex);
				result.setRows(areaEntitys);
			}
			AreaHelper.closeRecordset(recordset);
		}
		return result;
	}

	@Override
	public boolean lineSplit(Point2D[] line, String areaId, String dcode) throws AreaException {
		
		DatasetVector datasetVector = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		StringBuilder filterBuilder = new StringBuilder();
		
		// 查询待拆分的面
		filterBuilder.append(AreaFieldNames.ID).append("='").append(areaId).append("' and ").append(
				AreaFieldNames.DEPARTENT_CODE).append("='").append(dcode).append("'");
		LOGGER.info("## 查询待拆分的面[" + filterBuilder.toString() + "]");
		
		Recordset recordset = datasetVector.query(filterBuilder.toString(), CursorType.DYNAMIC);
		if (null == recordset || recordset.getRecordCount() <= 0) {
			throw new AreaException("未找到待拆分的数据[id:" + areaId + ",dcode:" + dcode);
		}
		
		String enterpriseId = recordset.getString(AreaFieldNames.ENTERPRISE_ID);
		
		boolean isSplitSuccess = false;
		GeoRegion region = (GeoRegion) recordset.getGeometry();
		// 判断起点和终点距离是否在十米以内，是则进行相交判断，如果相交，则不处理，不相交则进行偏移处理
		Point2D start = line[0];
		Point2D end = line[line.length-1];
		double len = Math.sqrt(Math.pow(start.getX()-end.getX(),2)+Math.pow(start.getY()-end.getY(), 2));
		if(len < 10){
			boolean isNeedOffset = false;
			GeoPoint geo = new GeoPoint(start.getX(),start.getY());
			isNeedOffset = Geometrist.hasIntersection(region, geo);
			if(!isNeedOffset){
				Point2D start01 = calculateOffsetPoint(line[0], line[1]);
				line[0] = start01;
			}
		}
		
		GeoLine splitLine = new GeoLine(new Point2Ds(convert(line)));
		// 保存拆分之后的面
		GeoRegion target1 = new GeoRegion(), target2 = new GeoRegion();
		boolean success = Geometrist.splitRegion(region, splitLine, target1, target2);
		if (!success) {
			return isSplitSuccess;
		} else {
			Map<String, Object> map = DatasetUtils.getAttibuteMap(recordset, AreaFieldNames.KEEP_FIELD_NAMES);
			// 删除旧面
			recordset.delete();
			boolean idDeleteOldSuccess = recordset.update();
			LOGGER.info("## delete the old region success ? ["+idDeleteOldSuccess+"]");
			AreaHelper.closeRecordset(recordset);

			// 添加新面
			Recordset toSaveRecordset = datasetVector.getRecordset(false, CursorType.DYNAMIC);
			
			GeoRegion region01 = GeometristUtil.clean(target1);
			map.put(AreaFieldNames.UPDATE_TIME, CommonUtil.dateConvert(new Date(),DateType.TIMESTAMP));
			map.put(OPT_TYPE_FIELDNAME, OPT_TYPE_UPDATE);//第一个面的状态为修改
			toSaveRecordset.setInt32("WULIU_SYNC",0);
			toSaveRecordset.addNew(region01, map);
			boolean addFirstRegionSuccess = toSaveRecordset.update();
			LOGGER.info("## add the first region success ? ["+addFirstRegionSuccess+"]");
			

			long time1 = System.currentTimeMillis();
			// 拓扑处理
			int smid_1 = toSaveRecordset.getID();
			
			StringBuilder topologyfilterB=new StringBuilder();
			topologyfilterB.append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append("' ")
							.append(" and "+AreaFieldNames.DEPARTENT_CODE+" like '").append(dcode).append("%' ");
			
			GeometristUtil.adjoinRegionTopology(datasetVector, smid_1,topologyfilterB.toString());
//			try {
//				GeometristUtil.adjoinRegionTopologyFor7c(datasetVector, smid_1);
//			} catch (Exception e) {
//				LOGGER.error(e.getMessage(), e);
//				throw new AreaException(e.getMessage());
//			}
			long time2 = System.currentTimeMillis();
			LOGGER.debug("拓扑处理[" + (time2 - time1) + "]ms");

			toSaveRecordset.moveNext();// 添加多个的时候要移动指针，否则只会加到一个上
			GeoRegion region2 = GeometristUtil.clean(target2);
			Map<String, Object> map02 = new HashMap<String, Object>();
			map02.put(AreaFieldNames.ID, CommonUtil.getUUID());
			map02.put(AreaFieldNames.AREANUMBER, makeNewNumberTail(map.get(AreaFieldNames.AREANUMBER) == null ? ""
					: map.get(AreaFieldNames.AREANUMBER) + ""));
			map02.put(AreaFieldNames.DEPARTENT_CODE, dcode);
			map02.put(AreaFieldNames.ENTERPRISE_ID, map.get(AreaFieldNames.ENTERPRISE_ID));
			map02.put(AreaFieldNames.NAME, makeNewNumberTail(map.get(AreaFieldNames.NAME) == null ? "" : map
					.get(AreaFieldNames.NAME)
					+ ""));
			map02.put(AreaFieldNames.USER_ID, map.get(AreaFieldNames.USER_ID));
			map02.put(AreaFieldNames.ADMINCODE, map.get(AreaFieldNames.ADMINCODE));
			map02.put(AreaFieldNames.CREATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
			map02.put(OPT_TYPE_FIELDNAME, OPT_TYPE_SAVE);//第二个面的状态为新增
			toSaveRecordset.addNew(region2, map02);
			boolean addSecondRegionSuccess = toSaveRecordset.update();
			LOGGER.info("## add the second region success? ["+addSecondRegionSuccess+"]");

			// 拓扑处理
			time1 = System.currentTimeMillis();
			int smid_2 = toSaveRecordset.getID();
			
//			try {
//				GeometristUtil.adjoinRegionTopologyFor7c(datasetVector, smid_2);
//			} catch (Exception e) {
//				LOGGER.error(e.getMessage(), e);
//				throw new AreaException(e.getMessage());
//			}
			GeometristUtil.adjoinRegionTopology(datasetVector, smid_2,topologyfilterB.toString());
			// GeometristUtil.adjoinRegionTopology(datasetVector, region2);
			time2 = System.currentTimeMillis();
			LOGGER.debug("拓扑处理[" + (time2 - time1) + "]ms");
			AreaHelper.closeRecordset(toSaveRecordset);
			isSplitSuccess = true;
		}
		return isSplitSuccess;
	}
	
	
	/**
	 * 
	 * <p>Title ：makeNewNumberTail</p>
	 * Description：		根据旧的编号构建新的编号
	 * 				规则：oldNumber +"_"+ 数字
	 * 			
	 * @param oldNum	
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-1-15 上午11:13:09
	 */
	private static String makeNewNumberTail(String oldNum){
		StringBuilder sb = new StringBuilder();
		if(!StringUtils.isEmpty(oldNum)){
			
			int index = oldNum.lastIndexOf("_");
			int number = 1;
			String prefix = "";
			if(index > 0 && index < oldNum.length() - 1){
				prefix = oldNum.substring(0,index);
				String sub = oldNum.substring(index + 1, oldNum.length());
				try{
					number = Integer.parseInt(sub);
					number ++;
				}catch(Exception e){
					prefix = oldNum;
				}
			}else{
				prefix = oldNum;
			}
			sb.append(prefix).append("_").append(number);
		}else{
			sb.append("01");
		}
		
		return sb.toString();
	}
	
	
	private Point2D calculateOffsetPoint(Point2D start,Point2D second){
		
		double len = Math.sqrt(Math.pow(start.getX()-second.getX(), 2)+Math.pow(start.getY()- second.getY(),2));
		double sinValue = (start.getY()-second.getY()) / len;
		double cosValue = (start.getX()-second.getX()) / len;
		double offsetX = cosValue * 10;
		double offsetY = sinValue * 10;
		Point2D result = new Point2D();
		result.setX(start.getX() - offsetX);
		result.setY(start.getY()- offsetY);
		return result;
	}
	
	
	
	@Override
	public boolean mergeRegion(String[] areaIds, String dcode) throws AreaException {
		
		DatasetVector datasetVector = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		Recordset recordset = null;
		Recordset toDelset = null;
		GeoRegion newRegion = null;// 合并后的区域
		boolean isUnionSuccess = false;
		
		String firstAreaId = null;
		String eid=null;
		for (int i = 0; i < areaIds.length; i++) {
			String filter = AreaFieldNames.ID + " = '" + areaIds[i] + "' and " + AreaFieldNames.DEPARTENT_CODE + " = '"
					+ dcode + "'";
			LOGGER.info("## 合并区域面查询条件[" + filter + "]");
			recordset = datasetVector.query(filter, CursorType.DYNAMIC);
			int cnt = recordset.getRecordCount();
			if (cnt < 1) {
				recordset.close();
				throw new AreaException("["+filter+"]不存在");
			}
			if (firstAreaId == null) {// 记录第一个面ID
				firstAreaId = areaIds[i];
			}
			GeoRegion region = (GeoRegion) recordset.getGeometry();
			if (newRegion == null) {
				newRegion = region;
			} else {
				// 合并
				newRegion = (GeoRegion) Geometrist.union(newRegion, region);
			}
		}
		if (newRegion != null) {
			// 合并到第一面，同时删除其他面
//			newRegion = GeometristUtil.clean(newRegion);
			String filter = AreaFieldNames.ID + " = '" + firstAreaId + "' and " + AreaFieldNames.DEPARTENT_CODE + " = '"
			+ dcode + "'";
			recordset = datasetVector.query(filter, CursorType.DYNAMIC);
			
			eid=recordset.getString(AreaFieldNames.ENTERPRISE_ID);//获取企业ID
			
			recordset.edit();
			boolean isSetGeoSuccess = recordset.setGeometry(newRegion);
			recordset.setString(AreaFieldNames.UPDATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
			boolean isMergedSuccess = recordset.update();
			LOGGER.info("## save the merged area success ? [isMergedSuccess,"+isMergedSuccess+"],[isSetGeoSuccess:"+isSetGeoSuccess+"]");
			if(isMergedSuccess){
				for (int i = 0; i < areaIds.length; i++) {
					if (!areaIds[i].equals(firstAreaId)) {
						
						String deleteFilter = AreaFieldNames.ID + " = '" + areaIds[i] + "' and " + AreaFieldNames.DEPARTENT_CODE + " = '"
						+ dcode + "'";
						recordset = datasetVector.query(deleteFilter, CursorType.DYNAMIC);
						toDelset = datasetVector.query(deleteFilter, CursorType.STATIC);
						int cnt = recordset.getRecordCount();
						if (cnt < 1) {
							continue;
						}
						
						//保存到删除库--
						//记录待删除的面数据
						GeoRegion region=null;
						AreaEntity area=new AreaEntity();
						if(isSaveDelEid(eid)){//判断是否指定需要保存删除的区划的企业
							region=(GeoRegion) toDelset.getGeometry();
							List<AreaEntity> areaList=parseResults(toDelset,true);
							if(null!=areaList&&areaList.size()>0){
								area=areaList.get(0);
							}
							saveDeleteAreaAttr(area,region);
						}
						
						// 删除数据
						recordset.delete();
						recordset.update();
					}
				}
				isUnionSuccess = true;
			}
			AreaHelper.closeRecordset(recordset);
			AreaHelper.closeRecordset(toDelset);
		}
		return isUnionSuccess;
	}

	@Override
	public boolean regionSplit(Point2D[] regionPoints, String areaId, String dcode) throws AreaException {
		DatasetVector datasetVector = this.areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		String filter = AreaFieldNames.ID+" = '"+areaId+"' and "+AreaFieldNames.DEPARTENT_CODE+"='"+dcode+"'";
		LOGGER.info("## 查询待拆分的面["+filter+"]");
		Recordset recordset = datasetVector.query(filter, CursorType.DYNAMIC);
		if(null == recordset || recordset.getRecordCount() < 1){
			throw new AreaException("未找到待拆分的区域面");
		}
		
		String enterpriseId=recordset.getString(AreaFieldNames.ENTERPRISE_ID);
		
		boolean isSplitSuccess = false;
		
		GeoRegion region = (GeoRegion) recordset.getGeometry();
		GeoRegion splitRegion = new GeoRegion(new Point2Ds(convert(regionPoints)));
		GeoRegion target01 = new GeoRegion(),target02 = new GeoRegion();
		boolean isSuccess = Geometrist.splitRegion(region, splitRegion, target01, target02);
		if(isSuccess){
			Map<String, Object> map = DatasetUtils.getAttibuteMap(recordset, AreaFieldNames.KEEP_FIELD_NAMES);
			// 删除旧面
			recordset.delete();
			recordset.update();

			// 添加新面
			GeoRegion region01 = GeometristUtil.clean(target01);
			recordset.addNew(region01, map);
			map.put(AreaFieldNames.UPDATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
			recordset.update();

			long time1 = System.currentTimeMillis();
			// 拓扑处理
			int smid_1 = recordset.getID();
//			try {
//				GeometristUtil.adjoinRegionTopologyFor7c(datasetVector, smid_1);
//			} catch (Exception e) {
//				LOGGER.error(e.getMessage(), e);
//				throw new AreaException(e.getMessage());
//			}
			
			StringBuilder topologyfilterB=new StringBuilder();
			topologyfilterB.append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append("' ")
							.append(" and "+AreaFieldNames.DEPARTENT_CODE+" like '").append(dcode).append("%' ");
			GeometristUtil.adjoinRegionTopology(datasetVector, smid_1,topologyfilterB.toString());
			long time2 = System.currentTimeMillis();
			LOGGER.debug("拓扑处理[" + (time2 - time1) + "]ms");

			recordset.moveNext();// 添加多个的时候要移动指针，否则只会加到一个上
			GeoRegion region2 = GeometristUtil.clean(target02);
			Map<String, Object> map02 = new HashMap<String, Object>();
			map02.put(AreaFieldNames.ID, CommonUtil.getUUID());
			map02.put(AreaFieldNames.AREANUMBER, map.get(AreaFieldNames.AREANUMBER) + "001");
			map02.put(AreaFieldNames.DEPARTENT_CODE, dcode);
			map02.put(AreaFieldNames.ENTERPRISE_ID, map.get(AreaFieldNames.ENTERPRISE_ID));
			map02.put(AreaFieldNames.NAME, map.get(AreaFieldNames.NAME)+ "001");
			map02.put(AreaFieldNames.USER_ID, map.get(AreaFieldNames.USER_ID));
			map02.put(AreaFieldNames.ADMINCODE, map.get(AreaFieldNames.ADMINCODE));
			map02.put(AreaFieldNames.CREATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
			recordset.addNew(region2, map02);
			recordset.update();

			// 拓扑处理
			time1 = System.currentTimeMillis();
			int smid_2 = recordset.getID();
			AreaHelper.closeRecordset(recordset);
//			try {
//				GeometristUtil.adjoinRegionTopologyFor7c(datasetVector, smid_2);
//			} catch (Exception e) {
//				LOGGER.error(e.getMessage(), e);
//				throw new AreaException(e.getMessage());
//			}
			GeometristUtil.adjoinRegionTopology(datasetVector, smid_2,topologyfilterB.toString());
			// GeometristUtil.adjoinRegionTopology(datasetVector, region2);
			time2 = System.currentTimeMillis();
			LOGGER.debug("拓扑处理[" + (time2 - time1) + "]ms");
			isSplitSuccess = true;
		}
		return isSplitSuccess;
	}

	@Override
	public boolean importUDB(String fileName, String userId, String enterpriseId, String dcode) throws AreaException {
		String path = EgispAreaConstants.IMPORT_DIR+File.separator+fileName;
		LOGGER.info("## UDB压缩文件路径["+path+"]");
		String unzipFilePath = path.substring(0, path.lastIndexOf("."));
		boolean isImportSuccess = false;
		try {
			// 解压文件
			ZipExtend.unZipToFolder(path, unzipFilePath);
			LOGGER.info("## 解压到路径["+unzipFilePath+"]");
			// 获取所有udb,shp数据文件
			File folder = new File(unzipFilePath);
			FileFilter filter = new FileFilter() {
				public boolean accept(File file) {
					int suffixIndex = file.getName().lastIndexOf(".");
					String suffix = file.getName().substring(suffixIndex + 1);
					if ("udb".equalsIgnoreCase(suffix)) {
						return true;
					}
					return false;
				}
			};
			
			File udbFiles[] = folder.listFiles(filter);
			if(null != udbFiles){
				LOGGER.info("## 发现UDB数据数量["+udbFiles.length+"]");
				for(File udbFile :  udbFiles){
					importRegionData(udbFile, enterpriseId, dcode, userId);
				}
			}else{
				throw new Exception("未发现UDB数据源");
			}
			isImportSuccess = true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new AreaException(e.getMessage());
		}
		return isImportSuccess;
	}
	
	/**
	 * 
	 * <p>Title ：importRegionData</p>
	 * Description：		导入区域数据
	 * @param dataFile
	 * @param enterpriseId
	 * @param dcode
	 * @param userId
	 * @throws Exception
	 * Author：Huasong Huang
	 * CreateTime：2014-12-11 上午10:28:04
	 */
	private void importRegionData(File dataFile,String enterpriseId,String dcode,String userId) throws Exception {

		String filePath = dataFile.getAbsolutePath();
		LOGGER.info("## 处理UDB数据["+filePath+"]");
		if(filePath.endsWith(".udb")){
			Workspace workspace = new Workspace();
			DatasourceConnectionInfo datasourceconnection = new DatasourceConnectionInfo();
			datasourceconnection.setEngineType(EngineType.UDB);
			datasourceconnection.setServer(filePath);
			Datasource datasource = workspace.getDatasources().open(datasourceconnection);
			if (datasource == null) {
				String warn = "打开数据源失败:" + filePath;
				LOGGER.warn(warn);
				datasourceconnection.dispose();
				workspace.dispose();
				return;
			}
			Datasets datasets = datasource.getDatasets();
			int datasetCount = datasets.getCount();
			for (int i = 0; i < datasetCount; i++) {
				Dataset dataset = datasets.get(i);
				String datasetName = dataset.getName();
				LOGGER.info("## 发现数据集名称["+datasetName+"]");
				if (!(dataset instanceof DatasetVector)) {
					// 非法数据集
					LOGGER.error("illegal dataset type");
					continue;
				}
				if (!datasetName.equalsIgnoreCase(EgispAreaConstants.DATASET_AREA_NAME)) {
					// 忽略不同名数据集
					LOGGER.debug("ignore dataset :" + datasetName);
					continue;
				}
				DatasetVector datasetVector = (DatasetVector) dataset;
				Recordset recordset = datasetVector.getRecordset(false, CursorType.STATIC);
				
				
				for(recordset.moveFirst();!recordset.isEOF();recordset.moveNext()){
					GeoRegion region = (GeoRegion) recordset.getGeometry();
					Recordset intersectRecord = queryIntersectGeoRegion(region, EgispAreaConstants.DATASET_AREA_NAME, enterpriseId, dcode, null);
					GeoRegion newRegion = region;
					if(null != intersectRecord && intersectRecord.getRecordCount() > 0){
						LOGGER.info("## 存在重叠区域，需要对区域面进行裁剪");
						for(intersectRecord.moveFirst();!intersectRecord.isEOF();intersectRecord.moveNext()){
							Geometry geometry = intersectRecord.getGeometry();
							if(null != geometry){
								if(null == newRegion){
									break;
								}
								newRegion = (GeoRegion) Geometrist.erase(newRegion, geometry);
							}
						}
					}
					newRegion = GeometristUtil.clean(newRegion);
					if(null == newRegion || newRegion.getArea() < 500){
						continue;
					}
					LOGGER.info("## newRegion [ "+newRegion.getArea()+"]");
					Recordset toSaveRecordset = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME).getRecordset(false, CursorType.DYNAMIC);
					FieldInfos fieldInfos = recordset.getFieldInfos();
					String areaNumber = null;
					if(fieldInfos.indexOf(AreaFieldNames.AREANUMBER) >= 0){
						areaNumber = recordset.getString(AreaFieldNames.AREANUMBER);
					}
					String areaName = null;
					if(fieldInfos.indexOf(AreaFieldNames.NAME) >= 0){
						areaName = recordset.getString(AreaFieldNames.NAME);
					}
					String netId = null;
					if(fieldInfos.indexOf(AreaFieldNames.NET_ID) >= 0){
						netId = recordset.getString(AreaFieldNames.NET_ID);
					}
					
					toSaveRecordset.addNew(newRegion);
					toSaveRecordset.setString(AreaFieldNames.AREANUMBER,areaNumber);
					toSaveRecordset.setString(AreaFieldNames.CREATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
					toSaveRecordset.setString(AreaFieldNames.DEPARTENT_CODE, dcode);
					toSaveRecordset.setString(AreaFieldNames.ENTERPRISE_ID, enterpriseId);
					toSaveRecordset.setString(AreaFieldNames.NAME, areaName);
					toSaveRecordset.setString(AreaFieldNames.NET_ID, netId);
					toSaveRecordset.setString(AreaFieldNames.USER_ID, userId);
					toSaveRecordset.setString(AreaFieldNames.ID, CommonUtil.getUUID());
					boolean isSuccess = toSaveRecordset.update();
					LOGGER.info("## 导入UDB" + (isSuccess ? "成功" : "失败"));
					AreaHelper.closeRecordset(toSaveRecordset);
					AreaHelper.closeRecordset(intersectRecord);
				}
				AreaHelper.closeRecordset(recordset);
			}
			datasourceconnection.dispose();
			workspace.close();
			workspace.dispose();
		}
	}

	@Override
	public String exportUDB(String[] areaIds, String dcode) throws AreaException {
		DatasetVector datasetVector = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		StringBuilder filter = new StringBuilder();
		filter.append(AreaFieldNames.ID).append(" in (");
		for(int i=0;i<areaIds.length;i++){
			filter.append("'").append(areaIds[i]).append("'");
			if(i < areaIds.length - 1){
				filter.append(",");
			}
		}
		filter.append(")");
		LOGGER.info("## 导出["+filter.toString()+"]");
		Recordset recordset = datasetVector.query(filter.toString(), CursorType.STATIC);
		String exportFilePath = null;
		try {
			exportFilePath = DatasetUtils.recordsetToUDB(recordset, EgispAreaConstants.EXPORT_DIR);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new AreaException(e.getMessage());
		}
		return exportFilePath;
	}
	
	

	/**
	 * 点击首页，查询前10条数据，按照区域面创建时间倒序排序
	 * @param areaName
	 * @param areaNumber
	 * @param admincode
	 * @param enterpriseId
	 * @param dcode
	 * @param isNeedPoint
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2015-6-29
	 */
	@Override
	public List<AreaEntity> queryByEnOrDeTop10(String areaName,String areaNumber,String admincode,String enterpriseId, String dcode,boolean isNeedPoint) throws AreaException {
		boolean isNeedQuery = false;
		StringBuilder filterBuilder = new StringBuilder();
		
		if(StringUtils.isEmpty(enterpriseId) && StringUtils.isEmpty(dcode)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,AreaFieldNames.DEPARTENT_CODE+"或"+AreaFieldNames.ENTERPRISE_ID);
		}
		
		if(!CommonUtil.isStringEmpty(enterpriseId)){
			filterBuilder.append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append("' ");
			isNeedQuery = true;
		}
		
		if(!CommonUtil.isStringEmpty(admincode) && admincode.length() >= 3){
			if(isNeedQuery){
				filterBuilder.append(" and (");
			}else{
				filterBuilder.append(" ( ");
			}
			filterBuilder.append(AreaFieldNames.ADMINCODE).append(" like '").append(admincode).append("%' or ").append(
					AreaFieldNames.ADMINCODE).append(" is null or ").append(AreaFieldNames.ADMINCODE).append("='')");
			isNeedQuery = true;
		}
		
		if(!StringUtils.isEmpty(areaName) || !StringUtils.isEmpty(areaNumber)){
			if(isNeedQuery){
				filterBuilder.append(" and ");
			}
			filterBuilder.append(" ( ");
			boolean isNameQuery = false;
			if(!StringUtils.isEmpty(areaName)){
				filterBuilder.append(AreaFieldNames.NAME).append(" like '%").append(areaName.replaceAll("_", "/_").replaceAll("%", "/_")).append("%' escape '/'");
				isNameQuery = true;
			}
			if(!StringUtils.isEmpty(areaNumber)){
				if(isNameQuery){
					filterBuilder.append(" or ");
				}
				filterBuilder.append(AreaFieldNames.AREANUMBER).append(" like '%").append(areaNumber.replaceAll("_", "/_").replaceAll("%", "/%'")).append("%' escape '/'");
			}
			filterBuilder.append(" ) ") ;
			isNeedQuery = true;
		}
		
		if(!CommonUtil.isStringEmpty(dcode)){
			if(isNeedQuery){
				filterBuilder.append(" and ");
			}
			filterBuilder.append(AreaFieldNames.DEPARTENT_CODE).append(" like '").append(dcode).append("%'");
			isNeedQuery = true;
		}
//		filterBuilder.append(" order by ").append(AreaFieldNames.CREATE_TIME).append(" DESC,").append(
//				AreaFieldNames.UPDATE_TIME).append(" DESC");
		
		DatasetVector dv = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if(dv == null){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,"DatasetVector为空");
		}
		
		filterBuilder.append(" and  rownum<=10 ");
		
		
		String filter = filterBuilder.toString();
		LOGGER.info("## 查询过滤条件["+filter+"]");
		QueryParameter qp = new QueryParameter();
		qp.setAttributeFilter(filter);
		//qp.setOrderBy(new String[]{AreaFieldNames.CREATE_TIME+" desc",AreaFieldNames.UPDATE_TIME+" desc"});
		qp.setOrderBy(new String[]{AreaFieldNames.CREATE_TIME+" desc "});
		qp.setCursorType(CursorType.STATIC);
		qp.setHasGeometry(isNeedPoint);
		Recordset recordset = dv.query(qp);
//		Recordset recordset = dv.query(filter, CursorType.STATIC);
		
		
		List<AreaEntity> results = parseResults(recordset, isNeedPoint);
		AreaHelper.closeRecordset(recordset);
		return results;
	}

	@Override
	public List<Map<String, Object>> queryAreaCountByParm(String enterpriseId,
			String dcode, String admincode, String level,String bdate,String edate) throws Exception {
		List<Map<String, Object>> maplist=new ArrayList<Map<String,Object>>();
		Properties dbprop = AppPropertiesUtil.readPropertiesFile("config.properties",AreaServiceImpl.class);
		Properties prop = AppPropertiesUtil.readPropertiesFile("config.properties",AreaServiceImpl.class);
		Connection conn = null;
		PreparedStatement ps = null; 
		ResultSet rs = null;
		
		/*SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		
		if(StringUtils.isEmpty(bdate)&&StringUtils.isEmpty(edate)){//如果开始日期、结束日期均为空，则统计近一个月数据
			Date endDate=new Date();
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(endDate);
			edate=dateFormat.format(calendar.getTime());
			calendar.add(Calendar.MONTH, -1);
			bdate=dateFormat.format(calendar.getTime());
		}*/
		
		try {
			Class.forName(dbprop.getProperty("DB_DRIVER"));
			conn = DriverManager.getConnection(dbprop.getProperty("DB_URL"), dbprop.getProperty("DB_USERNAME"), dbprop.getProperty("DB_PASSWORD"));
			String filter="";
			int m=1,n=2;
			
			if(enterpriseId!=null&&!enterpriseId.equals("")){
				filter+=" and "+AreaFieldNames.ENTERPRISE_ID+"='"+enterpriseId+"' ";
			}
			if(dcode!=null&&!dcode.equals("")){
				filter+=" and "+AreaFieldNames.DEPARTENT_CODE+" like '"+dcode+"%' ";
			}
			
			if(StringUtils.isNotEmpty(bdate)){
				bdate=bdate+" 00:00:00";
				filter+=" and "+AreaFieldNames.CREATE_TIME+" >='"+bdate+"' ";
			}
			
			if(StringUtils.isNotEmpty(edate)){
				edate=edate+" 23:59:59";
				filter+=" and "+AreaFieldNames.CREATE_TIME+" <='"+edate+"' ";
			}
			
			String admincodetemp="";
			if(level==null||level.equals("")){
				level="1";
				m=0;n=2;
			}
			else{
				int levels=Integer.parseInt(level);
				//LOGGER.info("***********"+levels);
				if(admincode!=null&&!admincode.equals("")){
					//判断直辖市 北京110，天津120，重庆500，上海310
					if(admincode.indexOf("110")==0||admincode.indexOf("120")==0||admincode.indexOf("500")==0||admincode.indexOf("310")==0){
						switch (levels) {
						case 1://市
							admincodetemp=admincode.substring(0, 3);
							m=0;n=6;
							break;
						case 3://区
							admincodetemp=admincode;
							m=0;n=6;
							break;
						default:
							admincodetemp=admincode;
							m=0;n=6;
							break;
						}
					}
					//其他省市区
					else {
						switch (levels) {
						case 1://省
							admincodetemp=admincode.substring(0, 2);
							m=0;n=4;
							break;
						case 2://市
							admincodetemp=admincode.substring(0, 4);
							m=0;n=6;
							break;
						case 3://区
							admincodetemp=admincode;
							m=0;n=6;
							break;
						default:
							admincodetemp=admincode;
							m=0;n=6;
							break;
						}
					}
				}
			}
			
			//LOGGER.info("***********"+admincodetemp);
			if(admincode!=null&&!admincode.equals("")){
				filter+=" and "+AreaFieldNames.ADMINCODE+" like '"+admincodetemp+"%' "
						+" and "+AreaFieldNames.ADMINCODE+" <>'"+admincode+"' ";
			}
			
			
			
			String sql="select count(*) count,substr(admincode,"+m+","+n+") admincode from " +
					prop.getProperty("dataset.area.name")+
					" where 1=1 "+filter+
					" group by substr(admincode,"+m+","+n+") " +
				    " order by count desc ";
			LOGGER.info("***********"+sql);
			
			ps=conn.prepareStatement(sql);
			
			rs=ps.executeQuery();
			while(rs.next()){
				Map<String, Object> map=new HashMap<String,Object>();
				if(rs.getString("admincode")!=null&&!rs.getString("admincode").equals("")){
					map.put("admincode", formatString(rs.getString("admincode"),6));
					map.put("count", rs.getInt("count"));
					maplist.add(map);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new AreaException(e.getMessage());
		}
		finally{
			if(rs!=null){
				rs.close();
				rs=null;
			}
			if(ps!=null){
				ps.close();
				ps=null;
			}
			if(conn!=null){
				conn.close();
				conn=null;
			}
		}
		return maplist;
	}


	public String formatString(String s,int len){
		if(s!=null){
			while(s.length()<len){
				s=s+"0";
			}
		}
		return s;
	}

	@Override
	public List<AreaEntity> queryByUseridsEboss(String admincode,
			String enterpriseId, String[] userids, Date btime, Date etime,
			String[] groupids, boolean isNeedPoint) throws AreaException {
		StringBuilder filterBuilder = new StringBuilder();
		if(StringUtils.isEmpty(enterpriseId)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,AreaFieldNames.ENTERPRISE_ID);
		}
		
		filterBuilder.append(" 1=1 ");
		
		String datePattern="yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf=new SimpleDateFormat(datePattern);
		
		if(!CommonUtil.isStringEmpty(enterpriseId)){
			filterBuilder.append(" and ").append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append("' ");
		}
		
		if(!CommonUtil.isStringEmpty(admincode) && admincode.length() >= 2){
			filterBuilder.append(" and ").append(AreaFieldNames.ADMINCODE).append(" like '").append(admincode).append("%' ");
		}
		
		if(btime!=null){
			filterBuilder.append(" and ").append(AreaFieldNames.CREATE_TIME).append(" >=").append("'").append(sdf.format(btime)).append("'");
		}
		if(etime!=null){
			filterBuilder.append(" and ").append(AreaFieldNames.CREATE_TIME).append(" <=").append("'").append(sdf.format(etime)).append("'");
		}
		
		if(userids!=null&&userids.length>0){
			filterBuilder.append(" and ").append(AreaFieldNames.USER_ID).append(" in (");
			for(int i=0;i<userids.length;i++){
				filterBuilder.append("'").append(userids[i]).append("'");
				if(i < userids.length - 1){
					filterBuilder.append(",");
				}
			}
			filterBuilder.append(")");
		}
		
		DatasetVector dv = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if(dv == null){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,"DatasetVector为空");
		}
		String filter = filterBuilder.toString();
		LOGGER.info("## 查询过滤条件["+filter+"]");
		QueryParameter qp = new QueryParameter();
		qp.setAttributeFilter(filter);
		qp.setOrderBy(new String[]{AreaFieldNames.CREATE_TIME+" desc",AreaFieldNames.UPDATE_TIME+" desc"});
		qp.setCursorType(CursorType.STATIC);
		qp.setHasGeometry(isNeedPoint);
		Recordset recordset = dv.query(qp);
		List<AreaEntity> results = parseResults(recordset, isNeedPoint);
		AreaHelper.closeRecordset(recordset);
		return results;
	}

	@Override
	public boolean deleteRegions(List<String> areaId) throws AreaException {
		if(null==areaId||areaId.size()<=0){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,AreaFieldNames.ID);
		}
		DatasetVector dv = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if(null == dv){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,EgispAreaConstants.DATASET_AREA_NAME+"为空");
		}
		StringBuilder filterBuilder=new StringBuilder();
		filterBuilder.append(AreaFieldNames.ID).append(" in (");
		for(int i=0;i<areaId.size();i++){
			filterBuilder.append("'").append(areaId.get(i)).append("'");
			if(i < areaId.size() - 1){
				filterBuilder.append(",");
			}
		}
		filterBuilder.append(")");
		LOGGER.info(filterBuilder.toString());
		Recordset recordset = dv.query(filterBuilder.toString(), CursorType.DYNAMIC);
		if(null == recordset || recordset.getRecordCount() <= 0){
			throw new AreaException(ExceptionType.NOT_FOUND,AreaFieldNames.ID+":"+areaId);
		}
		boolean isDeleteSuccess = false;
		try{
			// 删除
			isDeleteSuccess=recordset.deleteAll();
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			throw new AreaException(ExceptionType.INNER_EXCEPTION,e.getMessage());
		}finally{
			LOGGER.info("## delete the area ["+areaId+"] success ? "+isDeleteSuccess);
			AreaHelper.closeRecordset(recordset);
		}
		return isDeleteSuccess;
	}
	
	
	@Override
	public byte[] exportUDB2Byte(String[] areaIds, String dcode)
			throws AreaException {
		DatasetVector datasetVector = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		StringBuilder filter = new StringBuilder();
		filter.append(AreaFieldNames.ID).append(" in (");
		for(int i=0;i<areaIds.length;i++){
			filter.append("'").append(areaIds[i]).append("'");
			if(i < areaIds.length - 1){
				filter.append(",");
			}
		}
		filter.append(")");
		LOGGER.info("## 导出["+filter.toString()+"]");
		Recordset recordset = datasetVector.query(filter.toString(), CursorType.STATIC);
		String exportFilePath = null;
		byte[] filebytes=null;
		try {
			exportFilePath = DatasetUtils.recordsetToUDB(recordset, EgispAreaConstants.EXPORT_DIR);
			filebytes=File2Byte(exportFilePath);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new AreaException(e.getMessage());
		}
		return filebytes;
	}
	
	/** 
     * 获得指定文件的byte数组 
     */  
    private static byte[] File2Byte(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);
            LOGGER.info("待删除的临时文件dirPath:"+filePath.substring(0, filePath.lastIndexOf(".")));
            File dir=new File(filePath.substring(0, filePath.lastIndexOf(".")));
            if(file.exists() && file.isFile()){
            	 FileInputStream fis = new FileInputStream(file);  
                 ByteArrayOutputStream bos = new ByteArrayOutputStream(1024*1024);  
                 byte[] b = new byte[1024*1024];  
                 int n;  
                 while ((n = fis.read(b)) != -1) {  
                     bos.write(b, 0, n);  
                 }  
                 fis.close();  
                 bos.close();  
                 buffer = bos.toByteArray();
                 file.delete();//删除压缩文件
            }
           if(dir.exists()&&dir.isDirectory()){//删除压缩文件原文件包
        	   	 System.gc();//垃圾回收，解除文件占用
            	 boolean result=deleteDir(dir);
            	 LOGGER.info("###删除文件包:"+result);
          }
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
            	LOGGER.info("###删除文件："+children[i]);
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 修改区划所属用户
     * 1、判断裁剪
     * 2、判断名称重复
     */
	@Override
	public boolean updateAreaOwner(String id, String userid, String eid,
			String dcode) throws AreaException {
		boolean flag=false;
		if(CommonUtil.isStringEmpty(id)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW, AreaFieldNames.ID);
		}
		AreaEntity ae = this.queryByIdOrNumber(id, null, null, true);
		if(null == ae){
			throw new AreaException(ExceptionType.NOT_FOUND,AreaFieldNames.ID);
		}
		String oldeid=ae.getEnterprise_id();
		String olduserid=ae.getUser_id();
		String olddcode=ae.getDcode();
		
		if(StringUtils.isEmpty(eid)||org.apache.commons.lang.StringUtils.isEmpty(dcode)||StringUtils.isEmpty(userid)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW, AreaFieldNames.USER_ID);
		}
		
		boolean isNeedUpdate = false;
		
		//判断名称、编号重复
		if(!StringUtils.isEmpty(ae.getName()) || !StringUtils.isEmpty(ae.getAreaNumber())){
			isNeedUpdate = true;
			existAreaNameOrNumber(ae.getName(), ae.getAreaNumber(), eid, dcode,ae.getId());
		}else{
			isNeedUpdate = true;
		}
		
		if(isNeedUpdate){
			boolean  ownerfirst=this.updateAreaOwnerAttri(id, userid, eid, dcode);
			if(ownerfirst){
				//判断裁剪
				List<Integer> parts=new ArrayList<Integer>();
				int partsi[]=ae.getParts();
				if(partsi!=null&&ae.getParts().length>0){
					for(int part:partsi){
						parts.add(part);
					}
				}
				try {
					boolean updateregion=this.updateAreaRegion(id, ae.getPoints(), parts);
					LOGGER.info("##更新区域所属用户后裁剪"+updateregion);
					if(!updateregion){
						this.updateAreaOwnerAttri(id, olduserid, oldeid, olddcode);
					}else{
						flag=true;
					}
				} catch (AreaException e) {
					this.updateAreaOwnerAttri(id, olduserid, oldeid, olddcode);
					throw new AreaException("更新用户后区域面过小或为空，不允许修改");
				}
			}
		}
		return flag;
	}
	
	/**
	 * 修改区划所属用户
	 * @param id
	 * @param userid
	 * @param eid
	 * @param dcode
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2016-10-24下午2:58:06
	 */
	public boolean updateAreaOwnerAttri(String id, String userid, String eid,
			String dcode) throws AreaException {
		if(CommonUtil.isStringEmpty(id)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW, AreaFieldNames.ID);
		}
		AreaEntity ae = this.queryByIdOrNumber(id, null, null, true);
		if(null == ae){
			throw new AreaException(ExceptionType.NOT_FOUND,AreaFieldNames.ID);
		}
		
		if(StringUtils.isEmpty(eid)||org.apache.commons.lang.StringUtils.isEmpty(dcode)||StringUtils.isEmpty(userid)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW, AreaFieldNames.USER_ID);
		}
		
		boolean isNeedUpdate = false;
		
		//判断名称、编号重复
		if(!StringUtils.isEmpty(ae.getName()) || !StringUtils.isEmpty(ae.getAreaNumber())){
			isNeedUpdate = true;
			existAreaNameOrNumber(ae.getName(), ae.getAreaNumber(), eid, dcode,ae.getId());
		}else{
			isNeedUpdate = true;
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update ").append(EgispAreaConstants.DATASET_AREA_NAME).append(" set ");
		
		// 修改用户id
		if(!CommonUtil.isStringEmpty(userid)){
			sqlBuilder.append(AreaFieldNames.USER_ID).append("='").append(userid).append("',");
			isNeedUpdate = true;
		}
		// 修改企业id
		if(!CommonUtil.isStringEmpty(eid)){
			sqlBuilder.append(AreaFieldNames.ENTERPRISE_ID).append("='").append(eid).append("',");
			isNeedUpdate = true;
		}
		// 修改部门编码
		if(!CommonUtil.isStringEmpty(dcode)){
			sqlBuilder.append(AreaFieldNames.DEPARTENT_CODE).append("='").append(dcode).append("',");
			isNeedUpdate = true;
		}
		
		Statement statement = null;
		try{
			if(isNeedUpdate){
				sqlBuilder.append(AreaFieldNames.UPDATE_TIME).append("='").append(CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP)).append("'");
				String sql = sqlBuilder.toString();
				sql = sql + " where "+AreaFieldNames.ID+"='"+id+"'";
				LOGGER.info("## 更新区域属性SQL : ["+sql+"]");
				Connection conn = DBUtil.getConnection();
				statement = conn.createStatement();
				int count = statement.executeUpdate(sql);
				if(count > 0){
					return true;
				}
			}
		}catch(Exception e){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,e.getMessage());
		}finally{
			if(statement != null){
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
    
	/*@SuppressWarnings("unchecked")
	@Override
	public String saveReverseSelectionArea(String admincode, int levelStr,
			String userId, String enterpriseId, String dcode) throws AreaException {
		Map<String,Object> georesultmap=this.geocodingService.getAdminGeoByCode(admincode, levelStr);//查行政区边界
		String code=getAdmincode(admincode,levelStr);//admincode做下处理
		Map<String, Object> resultcityname=this.geocodingService.getCountyByAdmincode(code);//根据admincode查省市区名称
		String provice=com.supermap.egispservice.area.utils.StringUtil.convertObjectToString(resultcityname.get("PROVINCE"));
		String city=com.supermap.egispservice.area.utils.StringUtil.convertObjectToString(resultcityname.get("CITY"));
		String county=com.supermap.egispservice.area.utils.StringUtil.convertObjectToString(resultcityname.get("COUNTY"));
		String town=com.supermap.egispservice.area.utils.StringUtil.convertObjectToString(resultcityname.get("TOWN"));
		StringBuilder countynameSb=new StringBuilder();
		if(StringUtils.isNotEmpty(provice)){
			countynameSb.append(provice);
		}if(StringUtils.isNotEmpty(provice)&&StringUtils.isNotEmpty(city)&&!city.equals(provice)){
			countynameSb.append(city);
		}
		if(StringUtils.isNotEmpty(county)){
			countynameSb.append(county);
		}
		if(admincode.length()>6){
			countynameSb.append(town);
		}
		if(georesultmap==null){
			throw new AreaException(ExceptionType.NOT_FOUND,admincode);
		}
		Map<String,Object> georegionmap=(Map<String, Object>) georesultmap.get("geo");
		int[] parts=(int[]) georegionmap.get("parts");
		List<Map<String,Object>> points=(List<Map<String, Object>>) georegionmap.get("points");
		if(null!=points&&points.size()>0){
			Point2D[] point2Ds=new Point2D[points.size()]; 
			List<Integer> partlists=new ArrayList<Integer>();
			for(int part:parts){
				partlists.add(part);
			}
			for(int i=0,s=points.size();i<s;i++){
				Map<String,Object> pointmap=points.get(i);
				Point2D point2D=new Point2D();
				point2D.setX(com.supermap.egispservice.area.utils.StringUtil.convertObjectToDouble(pointmap.get("x")));
				point2D.setY(com.supermap.egispservice.area.utils.StringUtil.convertObjectToDouble(pointmap.get("y")));
				point2Ds[i]=point2D;
			}
			GeoRegion geoRegion=getCompoundRegion(point2Ds,partlists);
			
			//先将行政边界面处理下,清理碎面
			geoRegion=GeometristUtil.cleanPiecedArea(geoRegion);
			
			//裁剪
			Recordset intersectRecord = queryIntersectGeoRegion(geoRegion, EgispAreaConstants.DATASET_AREA_NAME, enterpriseId, dcode,null);
			//第一次裁剪失败的面
			List<GeoRegion> failList=null;
			if(null != intersectRecord && intersectRecord.getRecordCount() > 0){
				LOGGER.info("## 存在重叠区域，需要对区域面进行裁剪:"+intersectRecord.getRecordCount());
				for(intersectRecord.moveFirst();!intersectRecord.isEOF();intersectRecord.moveNext()){
					Geometry geometry = intersectRecord.getGeometry();
					if(null != geometry){
						String name = intersectRecord.getString("name");
//						if(false){}
						if(Geometrist.hasIntersection(geometry, geoRegion)||Geometrist.hasTouch(geometry, geoRegion)){//部分重叠
							GeoRegion tempGeo=(GeoRegion) Geometrist.intersect(geoRegion, geometry);
							if(tempGeo!=null&&tempGeo.getPartCount()>0){
								tempGeo=GeometristUtil.cleanPiecedArea(tempGeo);
								if(tempGeo!=null&&tempGeo.getArea()>=500){
									int partCount=tempGeo.getPartCount();
									GeoRegion temp_ = null;
//									if(partCount==1){
//										temp_ = (GeoRegion) Geometrist.erase(geoRegion,geometry);
//									}else{
										temp_ = (GeoRegion) Geometrist.erase(geoRegion,tempGeo);
//									}
									if(temp_==null||temp_.getArea()<500){
										geoRegion=null;
										break;
									}
									temp_=GeometristUtil.cleanPiecedArea(temp_);
									double beforeEraseSize = geoRegion.getArea();
									double temRegionSize = temp_.getArea();
									double eraseSize = beforeEraseSize - temRegionSize;
									LOGGER.info(name+"##部分重叠:partCount："+partCount+"eraseSize:"+NumberFormat.getInstance().format(eraseSize));
									if(eraseSize > 0){
										geoRegion.dispose();
										geoRegion=temp_;
										geometry.dispose();
										geometry = null;
									}else{
										if(null==failList){
											failList=new ArrayList<GeoRegion>();
										}
										failList.add(cloneRegion((GeoRegion)geometry));
									}
									tempGeo.dispose();
									tempGeo=null;
									if(geoRegion==null || geoRegion.getArea() < 500){
										break;
									}
								}
							}
						}
						else{//其他
							double beforeEraseSize = geoRegion.getArea();
							GeoRegion temp =(GeoRegion) Geometrist.erase(geoRegion,geometry);
							if(temp==null||temp.getArea()<500){
								geoRegion=null;
								break;
							}
							temp=GeometristUtil.cleanPiecedArea(temp);
							double temRegionSize = temp.getArea();
							double eraseSize = beforeEraseSize - temRegionSize;
							LOGGER.info(name+"##其他:"+":erase:"+NumberFormat.getInstance().format(eraseSize));
							if(eraseSize > 0){
								geoRegion.dispose();
								geoRegion=temp;
								geometry.dispose();
								geometry = null;
							}else{
								if(null==failList){
									failList=new ArrayList<GeoRegion>();
								}
								failList.add(cloneRegion((GeoRegion)geometry));
							}
							if(geoRegion==null || geoRegion.getArea() < 500){
								break;
							}
						}
					}
				}
			}
			AreaHelper.closeRecordset(intersectRecord);
			
			//碎面清理
			geoRegion=GeometristUtil.cleanPiecedArea(geoRegion);
			
			//验证面
//			repairAreaRegion(geoRegion);
			
			if(null == geoRegion || geoRegion.getArea() < 500){
				throw new AreaException(ExceptionType.OTHER,"经过裁剪之后，面为空或过小，添加失败");
			}
			
			boolean isAddSuccess = false;
			String id = null;
			Recordset toSaveRecordset = null;
			try{
				String areaNumber=code;
				try {
					// 判断是否存在重复的区划编号或名称，存在即会抛出异常
					existAreaNameOrNumber(countynameSb.toString(), areaNumber, enterpriseId, dcode,null);
				} catch (Exception e) {
					countynameSb.append("_"+RandomStringUtils.randomAlphanumeric(6));
					areaNumber=areaNumber+"_"+RandomStringUtils.randomAlphanumeric(6);	
				}
				DatasetVector dv=areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
				toSaveRecordset = dv.getRecordset(false, CursorType.DYNAMIC);
				toSaveRecordset.addNew(geoRegion);
				toSaveRecordset.setString(AreaFieldNames.AREANUMBER, areaNumber);
				toSaveRecordset.setString(AreaFieldNames.CREATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
				toSaveRecordset.setString(AreaFieldNames.DEPARTENT_CODE, dcode);
				toSaveRecordset.setString(AreaFieldNames.ENTERPRISE_ID, enterpriseId);
				if(countynameSb.length()>0){
					toSaveRecordset.setString(AreaFieldNames.NAME, countynameSb.toString());
				}
				else{
					toSaveRecordset.setString(AreaFieldNames.NAME, "自派区域-"+RandomStringUtils.randomAlphanumeric(6));
				}
				toSaveRecordset.setString(AreaFieldNames.NET_ID, null);
				toSaveRecordset.setString(AreaFieldNames.USER_ID, userId);
				toSaveRecordset.setString(AreaFieldNames.ADMINCODE, code);
				toSaveRecordset.setString(AreaFieldNames.ID, CommonUtil.getUUID());
				isAddSuccess = toSaveRecordset.update();
				if(isAddSuccess){
					id = toSaveRecordset.getString(AreaFieldNames.ID);
					boolean flag=earseRegion(dv,id,failList);
					LOGGER.info("##二次擦除flag:"+flag);
				}else{
					throw new Exception("保存区域面失败");
				}
			}catch(Exception e){
				throw new AreaException(ExceptionType.INNER_EXCEPTION,e.getMessage());
			}finally{
				AreaHelper.closeRecordset(toSaveRecordset);
			}
				return id;
		}
		return null;
	}*/
	
	public String getAdmincode(String adminmapcode,int adminlevel){
		switch (adminlevel) {
		case 1:
			adminmapcode=formatString(adminmapcode.substring(0, 2),6);
			break;
		case 2:
			adminmapcode=formatString(adminmapcode.substring(0, 4),6);
			break;
		case 3:
			adminmapcode=formatString(adminmapcode.substring(0, 6),6);
			break;
		default:
			break;
		}
		return adminmapcode;
	}
	
	
	/**
	 * 拓扑处理区划面
	 * @param areaId
	 * @Author Juannyoh
	 * 2016-11-18上午9:52:33
	 */
	/*public void adjoinRegionTopology(DatasetVector datasetVector,String areaId){
		StringBuilder filterBuilder = new StringBuilder();
		// 查询待拆分的面
		filterBuilder.append(AreaFieldNames.ID).append("='").append(areaId).append("'");
		LOGGER.info("## 拓扑处理的面[" + filterBuilder.toString() + "]");
		
		Recordset recordset = datasetVector.query(filterBuilder.toString(), CursorType.DYNAMIC);
		if (null == recordset || recordset.getRecordCount() <= 0) {
			return ;
		}
		// 拓扑处理
		long time1 = System.currentTimeMillis();
		int smid_1 = recordset.getID();
		GeometristUtil.adjoinRegionTopology(datasetVector, smid_1);
		long time2 = System.currentTimeMillis();
		LOGGER.info("拓扑处理[" + (time2 - time1) + "]ms");
		AreaHelper.closeRecordset(recordset);
	}*/
	
	/**
	 * 复制一个新面对象，释放原对象
	 * @param region
	 * @return
	 * @Author Juannyoh
	 * 2016-11-19下午2:05:15
	 */
	public GeoRegion cloneRegion(GeoRegion region){
		if(region==null){
			return null;
		}
		GeoRegion geo=new GeoRegion();
		for(int i=0;i<region.getPartCount();i++){
			geo.addPart(region.getPart(i));
		}
		region.dispose();
		region=null;
		return geo;	
	}
	
	/**
	 * 对面进行修改擦除
	 * @param areaid
	 * @param region
	 * @return
	 * @Author Juannyoh
	 * 2016-11-18下午8:30:02
	 */
	public boolean earseRegion(DatasetVector dv,String areaid,List<GeoRegion> regionlist){
		Recordset recordset = null;
		boolean isUpdateSucces=false;
		try {
			if (null == dv) {
				return isUpdateSucces;
			}
			if(null==regionlist||regionlist.size()==0){
				return isUpdateSucces;
			}
			LOGGER.info("###second earse regions："+regionlist.size());
			
			//拓扑处理
//			adjoinRegionTopology(dv,areaid);
			
			// 查询指定区域面
			recordset = dv.query(AreaFieldNames.ID + "='" + areaid + "'", CursorType.DYNAMIC);
			if (null == recordset || recordset.getRecordCount() <= 0) {
				return false;
			}
			recordset.moveFirst();
			Geometry geo=recordset.getGeometry();
			for(int i=0;i<regionlist.size();i++){
				GeoRegion temp=(GeoRegion) Geometrist.erase(geo, regionlist.get(i));
				if(temp==null||temp.getArea()<500){
					return false;
				}
				geo.dispose();
				geo=temp;
				regionlist.get(i).dispose();
			}
			regionlist=null;
			// 更新区域面
			recordset.edit();
			recordset.setGeometry(geo);
			isUpdateSucces = recordset.update();
			if(null != geo){
				AreaHelper.closeGeometry(geo);
			}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}finally{
				if(null != recordset){
					AreaHelper.closeRecordset(recordset);
				}
			}
		return isUpdateSucces;
	}
	
	
	/**
	 * 验证区域每个部分 第一个点是否与最后一个点相同
	 * @param region
	 * @Author Juannyoh
	 * 2016-11-24上午9:18:04
	 */
	public void repairAreaRegion(GeoRegion region){
		if(null==region||region.getPartCount()==0){
			return;
		}
		for(int i=0;i<region.getPartCount();i++){
			Point2Ds point2ds=region.getPart(i);
			if(point2ds.getCount()>3){
				if(!comparePoints(point2ds.getItem(0),point2ds.getItem(point2ds.getCount()-1))){
					LOGGER.info("第"+i+"部分起点终点没重合");
				}else{
//					LOGGER.info("第"+i+"部分起点终点重合--"+point2ds.getCount()+"--"+point2ds.getItem(0)+","+point2ds.getItem(point2ds.getCount()-1));
//					region.getPart(i).add(region.getPart(i).getItem(0));
				}
			}
		}
	}
	
	public boolean comparePoints(com.supermap.data.Point2D p1,com.supermap.data.Point2D p2){
		if((p1.getX()==p2.getX())&&(p1.getY()==p2.getY())){
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String saveReverseSelectionArea(String admincode, int levelStr,
			String userId, String enterpriseId, String dcode) throws AreaException {
		Map<String,Object> georesultmap=this.geocodingService.getAdminGeoByCode(admincode, levelStr);//查行政区边界
		String code=getAdmincode(admincode,levelStr);//admincode做下处理
		Map<String, Object> resultcityname=this.geocodingService.getCountyByAdmincode(code);//根据admincode查省市区名称
		String provice=com.supermap.egispservice.area.utils.StringUtil.convertObjectToString(resultcityname.get("PROVINCE"));
		String city=com.supermap.egispservice.area.utils.StringUtil.convertObjectToString(resultcityname.get("CITY"));
		String county=com.supermap.egispservice.area.utils.StringUtil.convertObjectToString(resultcityname.get("COUNTY"));
		String town=com.supermap.egispservice.area.utils.StringUtil.convertObjectToString(resultcityname.get("TOWN"));
		StringBuilder countynameSb=new StringBuilder();
		if(StringUtils.isNotEmpty(provice)){
			countynameSb.append(provice);
		}if(StringUtils.isNotEmpty(provice)&&StringUtils.isNotEmpty(city)&&!city.equals(provice)){
			countynameSb.append(city);
		}
		if(StringUtils.isNotEmpty(county)){
			countynameSb.append(county);
		}
		if(admincode.length()>6){
			countynameSb.append(town);
		}
		if(georesultmap==null){
			throw new AreaException(ExceptionType.NOT_FOUND,admincode);
		}
		Map<String,Object> georegionmap=(Map<String, Object>) georesultmap.get("geo");
		int[] parts=(int[]) georegionmap.get("parts");
		List<Map<String,Object>> points=(List<Map<String, Object>>) georegionmap.get("points");
		if(null!=points&&points.size()>0){
			Point2D[] point2Ds=new Point2D[points.size()]; 
			List<Integer> partlists=new ArrayList<Integer>();
			for(int part:parts){
				partlists.add(part);
			}
			for(int i=0,s=points.size();i<s;i++){
				Map<String,Object> pointmap=points.get(i);
				Point2D point2D=new Point2D();
				point2D.setX(com.supermap.egispservice.area.utils.StringUtil.convertObjectToDouble(pointmap.get("x")));
				point2D.setY(com.supermap.egispservice.area.utils.StringUtil.convertObjectToDouble(pointmap.get("y")));
				point2Ds[i]=point2D;
			}
			GeoRegion geoRegion=getCompoundRegion(point2Ds,partlists);
			
			//先将行政边界面处理下,清理碎面
			geoRegion=GeometristUtil.cleanPiecedArea(geoRegion);
			
			//查询相交的面
			Recordset intersectRecord = queryIntersectGeoRegion(geoRegion, EgispAreaConstants.DATASET_AREA_NAME, enterpriseId, dcode,null);
			int counts=intersectRecord==null?0:intersectRecord.getRecordCount();
//			for(int i=0;i<counts;i++){
//				double size= intersectRecord.getDouble("smarea");
//				String name=intersectRecord.getString(AreaFieldNames.NAME);
//				LOGGER.info("经过裁剪----"+name+","+size);
//				intersectRecord.moveNext();
//			}
			//裁剪
			GeoRegion earseregion=null;
			try {
				earseregion = GeometristUtil.earseAdminGeo(geoRegion, intersectRecord);
				LOGGER.info("经过裁剪后的剩余面积大小："+(earseregion==null?0:earseregion.getArea()));
			} catch (Exception e1) {
				throw new AreaException("保存区域面失败");
			}
			
			AreaHelper.closeRecordset(intersectRecord);
			
			if(counts>0){//如果有裁剪
				geoRegion.dispose();
				geoRegion=earseregion;
			}
			
			if(null == geoRegion || geoRegion.getArea() < 500){
				throw new AreaException(ExceptionType.OTHER,"经过裁剪之后，面为空或过小，添加失败");
			}
			
			boolean isAddSuccess = false;
			String id = null;
			Recordset toSaveRecordset = null;
			try{
				String areaNumber=code;
				try {
					// 判断是否存在重复的区划编号或名称，存在即会抛出异常
					existAreaNameOrNumber(countynameSb.toString(), areaNumber, enterpriseId, dcode,null);
				} catch (Exception e) {
					countynameSb.append("_"+RandomStringUtils.randomAlphanumeric(6));
					areaNumber=areaNumber+"_"+RandomStringUtils.randomAlphanumeric(6);	
				}
				DatasetVector dv=areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
				toSaveRecordset = dv.getRecordset(false, CursorType.DYNAMIC);
				toSaveRecordset.addNew(geoRegion);
				toSaveRecordset.setString(AreaFieldNames.AREANUMBER, areaNumber);
				toSaveRecordset.setString(AreaFieldNames.CREATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
				toSaveRecordset.setString(AreaFieldNames.DEPARTENT_CODE, dcode);
				toSaveRecordset.setString(AreaFieldNames.ENTERPRISE_ID, enterpriseId);
				if(countynameSb.length()>0){
					toSaveRecordset.setString(AreaFieldNames.NAME, countynameSb.toString());
				}
				else{
					toSaveRecordset.setString(AreaFieldNames.NAME, "自派区域-"+RandomStringUtils.randomAlphanumeric(6));
				}
				toSaveRecordset.setString(AreaFieldNames.NET_ID, null);
				toSaveRecordset.setString(AreaFieldNames.USER_ID, userId);
				toSaveRecordset.setString(AreaFieldNames.ADMINCODE, code);
				toSaveRecordset.setString(AreaFieldNames.ID, CommonUtil.getUUID());
				toSaveRecordset.setInt32(OPT_TYPE_FIELDNAME, OPT_TYPE_SAVE);
				isAddSuccess = toSaveRecordset.update();
				if(isAddSuccess){
					id = toSaveRecordset.getString(AreaFieldNames.ID);
				}else{
					throw new Exception("保存区域面失败");
				}
			}catch(Exception e){
				throw new AreaException(ExceptionType.INNER_EXCEPTION,e.getMessage());
			}finally{
				AreaHelper.closeRecordset(toSaveRecordset);
			}
				return id;
		}
		return null;
	}

	@Override
	public List<AreaEntity> queryByEnOrDeAndDate(String areaName,
			String areaNumber, String admincode, String enterpriseId, String dcode,
			boolean isNeedPoint, String bdate, String edate)
			throws AreaException {
		StringBuilder sqlBuilder = new StringBuilder();
		/*SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		
		if(StringUtils.isEmpty(bdate)&&StringUtils.isEmpty(edate)){//如果开始日期、结束日期均为空，则统计近一个月数据
			Date endDate=new Date();
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(endDate);
			edate=dateFormat.format(calendar.getTime());
			calendar.add(Calendar.MONTH, -1);
			bdate=dateFormat.format(calendar.getTime());
		}*/
		Connection conn = null;
		List<AreaEntity> results=null;
		Statement statement = null;
		ResultSet resultset = null;
		try {
			sqlBuilder.append("select  ")
						.append(AreaFieldNames.ID).append(", ")
						.append(AreaFieldNames.NAME).append(", ")
						.append(AreaFieldNames.AREANUMBER).append(", ")
						.append(AreaFieldNames.CREATE_TIME).append(", ")
						.append(AreaFieldNames.UPDATE_TIME).append(", ")
						.append(AreaFieldNames.USER_ID).append(", ")
						.append(AreaFieldNames.ENTERPRISE_ID).append(", ")
						.append(AreaFieldNames.DEPARTENT_CODE).append(", ")
						.append(AreaFieldNames.ADMINCODE).append(", ")
						.append(AreaFieldNames.AREA_STATUS).append(", ")
						.append(AreaFieldNames.RELATION_AREAID).append(", ")
						.append(AreaFieldNames.WGZ_CODE).append(", ")
						.append(AreaFieldNames.WGZ_NAME).append(", ")
						.append(AreaFieldNames.LINE_CODE).append(",")
						.append(AreaFieldNames.LINE_NAME).append(" ")
						.append(" from ")
						.append(EgispAreaConstants.DATASET_AREA_NAME)
						.append(" where 1=1 ");
			
			if(StringUtils.isEmpty(enterpriseId) && StringUtils.isEmpty(dcode)){
				throw new AreaException(ExceptionType.NULL_NO_ALLOW,AreaFieldNames.DEPARTENT_CODE+"或"+AreaFieldNames.ENTERPRISE_ID);
			}
			
			if(!CommonUtil.isStringEmpty(enterpriseId)){
				sqlBuilder.append(" and ").append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append("' ");
			}
			
			if(!CommonUtil.isStringEmpty(admincode) && admincode.length() >= 2){
				sqlBuilder.append(" and (")
						.append(AreaFieldNames.ADMINCODE).append(" like '").append(admincode).append("%' or ").append(
						AreaFieldNames.ADMINCODE).append(" is null or ").append(AreaFieldNames.ADMINCODE).append("='')");
			}
			
			if(StringUtils.isNotEmpty(bdate)){
				bdate=bdate+" 00:00:00";
				sqlBuilder.append(" and ").append(AreaFieldNames.CREATE_TIME).append(" >='").append(bdate).append("' ");
			}
			
			if(StringUtils.isNotEmpty(edate)){
				edate=edate+" 23:59:59";
				sqlBuilder.append(" and ").append(AreaFieldNames.CREATE_TIME).append(" <='").append(edate).append("' ");
			}
			
			if(!StringUtils.isEmpty(areaName) || !StringUtils.isEmpty(areaNumber)){
				sqlBuilder.append(" and (");
				boolean isNameQuery = false;
				if(!StringUtils.isEmpty(areaName)){
					sqlBuilder.append(AreaFieldNames.NAME).append(" like '%").append(areaName.replaceAll("_", "/_").replaceAll("%", "/_")).append("%' escape '/'");
					isNameQuery = true;
				}
				if(!StringUtils.isEmpty(areaNumber)){
					if(isNameQuery){
						sqlBuilder.append(" or ");
					}
					sqlBuilder.append(AreaFieldNames.AREANUMBER).append(" like '%").append(areaNumber.replaceAll("_", "/_").replaceAll("%", "/%'")).append("%' escape '/'");
				}
				sqlBuilder.append(" ) ") ;
			}
			
			if(!CommonUtil.isStringEmpty(dcode)){
				sqlBuilder.append(" and ")
						.append(AreaFieldNames.DEPARTENT_CODE).append(" like '").append(dcode).append("%'");
			}
			
			conn = DBUtil.getConnection();
			statement = conn.createStatement();
			resultset = statement.executeQuery(sqlBuilder.toString());
			if(null!=resultset){
				results=new ArrayList<AreaEntity>();
				while(resultset.next()){
					AreaEntity area=new AreaEntity();
					area.setId(resultset.getString(AreaFieldNames.ID));
					area.setAdmincode(resultset.getString(AreaFieldNames.ADMINCODE));
					area.setArea_status(resultset.getInt(AreaFieldNames.AREA_STATUS));
					area.setAreaNumber(resultset.getString(AreaFieldNames.AREANUMBER));
					area.setCreate_time(resultset.getString(AreaFieldNames.CREATE_TIME));
					area.setDcode(resultset.getString(AreaFieldNames.DEPARTENT_CODE));
					area.setEnterprise_id(resultset.getString(AreaFieldNames.ENTERPRISE_ID));
					area.setName(resultset.getString(AreaFieldNames.NAME));
					area.setRelation_areaid(resultset.getString(AreaFieldNames.RELATION_AREAID));
					area.setUpdate_time(resultset.getString(AreaFieldNames.UPDATE_TIME));
					area.setUser_id(resultset.getString(AreaFieldNames.USER_ID));
					area.setWgzCode(resultset.getString(AreaFieldNames.WGZ_CODE));
					area.setWgzName(resultset.getString(AreaFieldNames.WGZ_NAME));
					area.setLineCode(resultset.getString(AreaFieldNames.LINE_CODE));
					area.setLineName(resultset.getString(AreaFieldNames.LINE_NAME));
					results.add(area);
				}
				//查找关联的区划名称、编号
				results=parseAttrResults(results);
			}
		} catch (Exception e) {
			LOGGER.info("导出区划属性信息失败："+e.getMessage());
			return null;
		}finally{
			try {
				if(null!=resultset){
					resultset.close();
				}
				if(null != statement ){
					statement.close();
				}
			} catch (Exception e) {
				LOGGER.info("关闭连接异常"+e.getMessage());
			}
		}
		
		return results;
	}

	@Override
	public boolean changeStatus(String areaId, int status) throws AreaException {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update ").append(EgispAreaConstants.DATASET_AREA_NAME).append(" set ");
		//如果是正常状态，则自动清除已关联的区划id
		if(status==0){
			sqlBuilder.append(AreaFieldNames.RELATION_AREAID+"='', ");
		}
		sqlBuilder.append(AreaFieldNames.AREA_STATUS+"='")
				.append(status)
				.append("',")
				.append(AreaFieldNames.UPDATE_TIME)
				.append("='").append(CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP))
				.append("' where id='").append(areaId).append("'");
		boolean isSuccess = false;
		Statement statement = null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			statement = conn.createStatement();
			LOGGER.info("## 修改区划状态["+sqlBuilder.toString()+"]");
			int count = statement.executeUpdate(sqlBuilder.toString());
			if(count > 0){
				isSuccess = true;
			}
		} catch (Exception e) {
			throw new AreaException(e.getMessage());
		}finally{
			try {
				if(null != statement ){
					statement.close();
				}
			} catch (SQLException e) {
				LOGGER.info("## 修改区划状态异常"+e.getMessage());
			}
		}
		return isSuccess;
	}

	@Override
	public List<Map<String, String>> findRelationAreaAttrs(String areaid,String relationareaid){
		List<Map<String, String>> resultlist=null;
		if(CommonUtil.isStringEmpty(relationareaid)){
			return null;
		}
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			resultlist=findRelationAreaAttrsUConn(areaid,relationareaid,conn);
		} catch (Exception e) {
			LOGGER.info("查找关联的区划信息失败："+e.getMessage());
			return null;
		}
		return resultlist;
	}
	
	public List<Map<String, String>> findRelationAreaAttrsUConn(String areaid,String relationareaid,Connection conn){
		List<Map<String, String>> resultlist=null;
		if(CommonUtil.isStringEmpty(relationareaid)){
			return null;
		}
		try {
			Map<String, String>  first=this.findFirstRelationAreaAttrs(relationareaid, conn);
			if(first!=null&&!CommonUtil.isStringEmpty(first.get(AreaFieldNames.ID))){
				if(StringUtils.isNotEmpty(areaid)&&StringUtils.isNotEmpty(first.get(AreaFieldNames.RELATION_AREAID))&&first.get(AreaFieldNames.RELATION_AREAID).equals(areaid)){//如果查出来互相关联了，则不返回结果
					return null;
				}
				resultlist=new ArrayList<Map<String,String>>();
				resultlist.add(first);
				if(first.get(AreaFieldNames.AREA_STATUS).equals("1")&&!CommonUtil.isStringEmpty(first.get(AreaFieldNames.RELATION_AREAID))){
					resultlist.addAll(findRelationAreaAttrsUConn(first.get(AreaFieldNames.ID),first.get(AreaFieldNames.RELATION_AREAID),conn));
				}
			}
			
		} catch (Exception e) {
			LOGGER.info("查找关联的区划信息失败："+e.getMessage());
			return null;
		}
		return resultlist;
	}
	
	
	
	/**
	 * 根据关联的区划id查找区划名称和区划编号
	 * @param relationareaid
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2016-12-19下午2:52:59
	 */
	public Map<String,String> findFirstRelationAreaAttrs(String relationareaid,Connection conn) throws AreaException {
		Map<String,String> map=null;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select  ")
				.append(AreaFieldNames.ID+", ")
				.append(AreaFieldNames.NAME+", ")
				.append(AreaFieldNames.AREANUMBER+", ")
				.append(AreaFieldNames.AREA_STATUS+", ")
				.append(AreaFieldNames.RELATION_AREAID+" ")
				.append(" from ")
				.append(EgispAreaConstants.DATASET_AREA_NAME)
				.append(" where id='").append(relationareaid).append("' ");
		Statement statement = null;
		ResultSet resultset = null;
		try {
			statement = conn.createStatement();
			LOGGER.info("## 查询关联区划信息["+sqlBuilder.toString()+"]");
			resultset = statement.executeQuery(sqlBuilder.toString());
			if(null!=resultset){
				map=new HashMap<String,String>();
				while(resultset.next()){
					map.put(AreaFieldNames.ID, resultset.getString(AreaFieldNames.ID));
					map.put(AreaFieldNames.NAME, resultset.getString(AreaFieldNames.NAME));
					map.put(AreaFieldNames.AREANUMBER, resultset.getString(AreaFieldNames.AREANUMBER));
					map.put(AreaFieldNames.AREA_STATUS, resultset.getString(AreaFieldNames.AREA_STATUS));
					map.put(AreaFieldNames.RELATION_AREAID, resultset.getString(AreaFieldNames.RELATION_AREAID));
				}
			}
		} catch (Exception e) {
			throw new AreaException(e.getMessage());
		}finally{
			try {
				if(null!=resultset){
					resultset.close();
				}
				if(null != statement ){
					statement.close();
				}
			} catch (Exception e) {
				LOGGER.info("关闭连接异常"+e.getMessage());
			}
		}
		return map;
	}
	
	public List<AreaEntity> parseAttrResults(List<AreaEntity> arealist){
		if(null != arealist && arealist.size() > 0){
			for(AreaEntity area:arealist){
				//区划状态，关联的区划信息
				int areastatus=area.getArea_status();
				if(areastatus==1){//如果是停用状态，则去查询关联的区划
					String relationid=area.getRelation_areaid();
					if(!CommonUtil.isStringEmpty(relationid)){
						List<Map<String,String>> relationlist=findRelationAreaAttrs(area.getId(),relationid);
						if(relationlist!=null&&relationlist.size()>0){
							area.setRelation_areaname(relationlist.get(relationlist.size()-1).get(AreaFieldNames.NAME));
							area.setRelation_areanumber(relationlist.get(relationlist.size()-1).get(AreaFieldNames.AREANUMBER));
						}
					}
				}
			}
		}
		return arealist;
	}

	
	/**
	 * 根据条件获得结果集
	 * @param admincode
	 * @param enterpriseId
	 * @param userids
	 * @param btime
	 * @param etime
	 * @param groupids
	 * @param isNeedPoint
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2016-12-21下午7:03:41
	 */
	public Recordset queryByUseridsEbossRecordSet(String admincode,
			String enterpriseId, String[] userids, Date btime, Date etime,
			String[] groupids, boolean isNeedPoint) throws AreaException {
		StringBuilder filterBuilder = new StringBuilder();
		if(StringUtils.isEmpty(enterpriseId)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,AreaFieldNames.ENTERPRISE_ID);
		}
		
		filterBuilder.append(" 1=1 ");
		
		String datePattern="yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf=new SimpleDateFormat(datePattern);
		
		if(!CommonUtil.isStringEmpty(enterpriseId)){
			filterBuilder.append(" and ").append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append("' ");
		}
		
		if(!CommonUtil.isStringEmpty(admincode) && admincode.length() >= 2){
			filterBuilder.append(" and ").append(AreaFieldNames.ADMINCODE).append(" like '").append(admincode).append("%' ");
		}
		
		if(btime!=null){
			filterBuilder.append(" and ").append(AreaFieldNames.CREATE_TIME).append(" >=").append("'").append(sdf.format(btime)).append("'");
		}
		if(etime!=null){
			filterBuilder.append(" and ").append(AreaFieldNames.CREATE_TIME).append(" <=").append("'").append(sdf.format(etime)).append("'");
		}
		
		if(userids!=null&&userids.length>0){
			filterBuilder.append(" and ").append(AreaFieldNames.USER_ID).append(" in (");
			for(int i=0;i<userids.length;i++){
				filterBuilder.append("'").append(userids[i]).append("'");
				if(i < userids.length - 1){
					filterBuilder.append(",");
				}
			}
			filterBuilder.append(")");
		}
		
		DatasetVector dv = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if(dv == null){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,"DatasetVector为空");
		}
		String filter = filterBuilder.toString();
		LOGGER.info("## 查询过滤条件["+filter+"]");
		QueryParameter qp = new QueryParameter();
		qp.setAttributeFilter(filter);
		qp.setOrderBy(new String[]{AreaFieldNames.CREATE_TIME+" desc",AreaFieldNames.UPDATE_TIME+" desc"});
		qp.setCursorType(CursorType.STATIC);
		qp.setHasGeometry(isNeedPoint);
		Recordset recordset = dv.query(qp);
		return recordset;
	}

	@Override
	public byte[] exportUDB2Byte(String admincode, String enterpriseId,
			String[] userids, Date btime, Date etime, String[] groupids,
			boolean isNeedPoint) throws AreaException {
		Recordset recordset = queryByUseridsEbossRecordSet(admincode,enterpriseId,userids,btime,etime,groupids,isNeedPoint);
		String exportFilePath = null;
		byte[] filebytes=null;
		try {
			exportFilePath = DatasetUtils.recordsetToUDB(recordset, EgispAreaConstants.EXPORT_DIR);
			filebytes=File2Byte(exportFilePath);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new AreaException(e.getMessage());
		}finally{
			AreaHelper.closeRecordset(recordset);
		}
		return filebytes;
	}

	@Override
	public List<AreaEntity> queryOneByName(String areaName, String areaNumber,
			String admincode, String enterpriseId, String dcode,
			boolean isNeedPoint) throws AreaException {
		boolean isNeedQuery = false;
		StringBuilder filterBuilder = new StringBuilder();
		
		if(StringUtils.isEmpty(enterpriseId) && StringUtils.isEmpty(dcode)){
			throw new AreaException(ExceptionType.NULL_NO_ALLOW,AreaFieldNames.DEPARTENT_CODE+"或"+AreaFieldNames.ENTERPRISE_ID);
		}
		
		if(!CommonUtil.isStringEmpty(enterpriseId)){
			filterBuilder.append(AreaFieldNames.ENTERPRISE_ID).append("='").append(enterpriseId).append("' ");
			isNeedQuery = true;
		}
		
		if(!CommonUtil.isStringEmpty(admincode) && admincode.length() >= 2){
			if(isNeedQuery){
				filterBuilder.append(" and (");
			}else{
				filterBuilder.append(" ( ");
			}
			filterBuilder.append(AreaFieldNames.ADMINCODE).append(" like '").append(admincode).append("%' or ").append(
					AreaFieldNames.ADMINCODE).append(" is null or ").append(AreaFieldNames.ADMINCODE).append("='')");
			isNeedQuery = true;
		}
		
		if(!StringUtils.isEmpty(areaName) || !StringUtils.isEmpty(areaNumber)){
			if(isNeedQuery){
				filterBuilder.append(" and ");
			}
			filterBuilder.append(" ( ");
			boolean isNameQuery = false;
			if(!StringUtils.isEmpty(areaName)){
				filterBuilder.append(AreaFieldNames.NAME).append("='").append(areaName).append("' ");
				isNameQuery = true;
			}
			if(!StringUtils.isEmpty(areaNumber)){
				if(isNameQuery){
					filterBuilder.append(" or ");
				}
				filterBuilder.append(AreaFieldNames.AREANUMBER).append(" ='").append(areaNumber).append("' ");
			}
			filterBuilder.append(" ) ") ;
			isNeedQuery = true;
		}
		
		if(!CommonUtil.isStringEmpty(dcode)){
			if(isNeedQuery){
				filterBuilder.append(" and ");
			}
			filterBuilder.append(AreaFieldNames.DEPARTENT_CODE).append(" like '").append(dcode).append("%'");
			isNeedQuery = true;
		}	
		
		DatasetVector dv = areaHelper.getDatasetVector(EgispAreaConstants.DATASET_AREA_NAME);
		if(dv == null){
			throw new AreaException(ExceptionType.INNER_EXCEPTION,"DatasetVector为空");
		}
		String filter = filterBuilder.toString();
		LOGGER.info("## 查询过滤条件["+filter+"]");
		QueryParameter qp = new QueryParameter();
		qp.setAttributeFilter(filter);
		qp.setOrderBy(new String[]{AreaFieldNames.CREATE_TIME+" desc",AreaFieldNames.UPDATE_TIME+" desc"});
		qp.setCursorType(CursorType.STATIC);
		qp.setHasGeometry(isNeedPoint);
		Recordset recordset = dv.query(qp);
		
		List<AreaEntity> results = parseResults(recordset, isNeedPoint);
		AreaHelper.closeRecordset(recordset);
		return results;
	}
	
	/**
	 * 保存已删除的区划数据
	 * @param areaName 
	 * @param areaNumber
	 * @param admincode
	 * @param userId
	 * @param enterpriseId
	 * @param dcode
	 * @param newRegion
	 * @param wgzCode
	 * @param wgzName
	 * @param lineCode
	 * @param lineName
	 */
	private void saveDeleteAreaAttr(AreaEntity area,GeoRegion newRegion) throws AreaException {
		if(area==null||StringUtils.isEmpty(area.getId())||!isSaveDelEid(area.getEnterprise_id())){//判断是否指定需要保存删除的区划的企业
			return ;
		}
		Recordset toSaveRecordset = null;
		boolean isAddSuccess =false;
		try{
			toSaveRecordset = areaHelper.getDatasetVector(DATASET_AREA_NAME_DELETE).getRecordset(false, CursorType.DYNAMIC);
			toSaveRecordset.addNew(newRegion);
			toSaveRecordset.setString(AreaFieldNames.AREANUMBER, area.getAreaNumber());
			toSaveRecordset.setString(AreaFieldNames.CREATE_TIME, CommonUtil.dateConvert(new Date(), DateType.TIMESTAMP));
			toSaveRecordset.setString(AreaFieldNames.DEPARTENT_CODE, area.getDcode());
			toSaveRecordset.setString(AreaFieldNames.ENTERPRISE_ID, area.getEnterprise_id());
			toSaveRecordset.setString(AreaFieldNames.NAME, area.getName());
			toSaveRecordset.setString(AreaFieldNames.USER_ID, area.getUser_id());
			toSaveRecordset.setString(AreaFieldNames.ADMINCODE, area.getAdmincode());
			toSaveRecordset.setString(AreaFieldNames.ID, area.getId());
			toSaveRecordset.setString(AreaFieldNames.WGZ_CODE, area.getWgzCode());//网格组编码
			toSaveRecordset.setString(AreaFieldNames.WGZ_NAME, area.getWgzName());//网格组名称
			toSaveRecordset.setString(AreaFieldNames.LINE_CODE, area.getLineCode());//线路编码
			toSaveRecordset.setString(AreaFieldNames.LINE_NAME, area.getLineName());//线路名称
			toSaveRecordset.setInt32(OPT_TYPE_FIELDNAME, OPT_TYPE_DELETE);//操作状态为删除
			isAddSuccess = toSaveRecordset.update();
			LOGGER.info("保存删除的面:"+isAddSuccess);
		}catch(Exception e){
			LOGGER.info("保存删除的面失败");
		}finally{
			AreaHelper.closeRecordset(toSaveRecordset);
		}
	}
	
	/**
	 * 判断是否是需要保存删除的企业ID
	 * @param eid
	 * @return
	 */
	private boolean isSaveDelEid(String eid){
		boolean flag=false;
//		List<String> eids=new ArrayList<String>();
//		eids.add("8a04a77b5962181a015b234dbbd75633");//海尔8a04a77b5962181a015b234dbbd85634 张京涛
//		eids.add("8a04a77b5962181a015b232c2c255622");//海尔8a04a77b5962181a015b232c2c255623 吕德逞
		if(eids.contains(eid)){
			flag=true;
		}
		return flag;
	}
	
}
