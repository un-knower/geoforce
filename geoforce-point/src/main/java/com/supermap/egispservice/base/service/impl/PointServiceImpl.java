package com.supermap.egispservice.base.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.StringUtils;
import com.supermap.convert.impl.BaiduCoordinateConvertImpl;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egisp.addressmatch.beans.AddressMatchParam;
import com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult;
import com.supermap.egisp.addressmatch.beans.ReverseAddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.exceptions.AreaException;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.callables.AddressSplitCallable;
import com.supermap.egispservice.base.constants.AddressMatchConstants;
import com.supermap.egispservice.base.constants.Config;
import com.supermap.egispservice.base.constants.StatusConstants;
import com.supermap.egispservice.base.dao.MapPointCarDao;
import com.supermap.egispservice.base.dao.PointDao;
import com.supermap.egispservice.base.dao.PointExportDao;
import com.supermap.egispservice.base.dao.PointExtcolValDao;
import com.supermap.egispservice.base.dao.PointGroupDao;
import com.supermap.egispservice.base.dao.PointPicDao;
import com.supermap.egispservice.base.dao.PointStyleDao;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.MapPointCarEntity;
import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.entity.PointExtcolEntity;
import com.supermap.egispservice.base.entity.PointExtcolValEntity;
import com.supermap.egispservice.base.entity.PointGroupEntity;
import com.supermap.egispservice.base.entity.PointPicEntity;
import com.supermap.egispservice.base.entity.PointStyleCustomEntity;
import com.supermap.egispservice.base.entity.PointStyleEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.pojo.AddresInfoDetails;
import com.supermap.egispservice.base.pojo.AppPointPojo;
import com.supermap.egispservice.base.pojo.ExportPointBean;
import com.supermap.egispservice.base.pojo.NetPointInfoResult;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.base.service.PointExtcolService;
import com.supermap.egispservice.base.service.PointExtcolValService;
import com.supermap.egispservice.base.service.PointGroupService;
import com.supermap.egispservice.base.service.PointService;
import com.supermap.egispservice.base.service.PointStyleService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.base.util.BeanTool;
import com.supermap.egispservice.base.util.FieldMap;
import com.supermap.egispservice.base.util.StringUtil;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.entity.Point;

@Service("pointService")
public class PointServiceImpl implements PointService {

	@Autowired
	private PointDao pointDao;
	
	@Autowired
	private PointExtcolValService pointExtcolValService;
	
	@Autowired
	private MapPointCarDao mapPointCarDao;
	
	@Autowired
	private IAreaService areaService;
	
	@Autowired
	private IAddressMatchService addressMatch;
	
	@Autowired
	private PointGroupDao pointGroupDao;
	
	@Autowired
	private PointStyleDao pointStyleDao;
	
	@Autowired
	private PointExtcolValDao pointExtcolValDao;
	
	@Autowired
	PointExtcolService pointExtcolService;
	
	@Autowired
	private PointExportDao pointExportDao;
	
	private static Logger LOGGER = Logger.getLogger(PointServiceImpl.class);
	
	@Autowired
	PointGroupService pointGroupService;
	@Autowired
	PointStyleService pointStyleService;
	
	@Autowired
	IGeocodingService geocodingService;
	
	@Autowired
	PointPicDao  pointPicDao;
	
	@Autowired
	InfoDeptService infoDeptService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	Config config;
	
	BaiduCoordinateConvertImpl bdconver=new BaiduCoordinateConvertImpl();
	
	@Transactional
	@Override
	public String add(String netPicPath,String dutyPicPath,String name,String address, BigDecimal smx, BigDecimal smy, String dutyName, String dutyPhone, String areaId,
			String userId, String enterpriseId, String departmentId,String iconStyle) throws Exception{
		
		isExsitPointname(name,userId,enterpriseId,departmentId);	
		
			String id=null;
			PointEntity pointEntity = new PointEntity();
			pointEntity.setNetPicPath(netPicPath);
			pointEntity.setDutyPicPath(dutyPicPath);
			pointEntity.setName(name);
			pointEntity.setAddress(address);
			pointEntity.setSmx(smx);
			pointEntity.setSmy(smy);
			pointEntity.setDutyName(dutyName);
			pointEntity.setDutyPhone(dutyPhone);
			pointEntity.setAreaId(areaId);
			pointEntity.setUserId(userId);
			pointEntity.setEnterpriseId(enterpriseId);
			pointEntity.setDepartmentId(departmentId);
			pointEntity.setCreateTime(new Date());
			pointEntity.setStatus(StatusConstants.POINT_COMMON);
			pointEntity.setIconStyle(iconStyle);
			pointEntity.setPointExtcolValEntity(null);
			
			//admincode、省市区
			if(smx!=null&&smy!=null){
				Map<String,String> m=geocodingService.searchAdmincodeForCounty(smx.doubleValue(),smy.doubleValue());
				if(m!=null){
					pointEntity.setAdmincode(m.get("ADMINCODE"));
					pointEntity.setProvince(m.get("PROVINCE"));
					pointEntity.setCity(m.get("CITY2"));
					pointEntity.setCounty(m.get("COUNTY"));
				}else{
					pointEntity.setStatus(StatusConstants.POINT_DISABLE);
				}
			}
			
			pointEntity = this.pointDao.save(pointEntity);
			id=pointEntity.getId();
		    return id;
	}

	@Override
	@Transactional
	public boolean deletePoint(String id) {
		PointEntity pointEntity=this.pointDao.findOne(id);
		pointEntity.setPointExtcolValEntity(null);
		this.pointDao.delete(id);
		this.pointExtcolValService.deletePointExtcolValByPointid(id);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryAllByPage(final String userId, final String name, final String dutyName,
			final String id, final String enterpriseId, final String dcode, int pageNo, int pageSize,
			final String areaId) {

		
		Specification<PointEntity> spec = new Specification<PointEntity>() {

			
			@Override
			public Predicate toPredicate(Root<PointEntity> root, CriteriaQuery<?> criterialQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				Path<String> userIdPath = root.get("userId");
				Path<String> enterpriseIdPath = root.get("enterpriseId");
				Path<String> idPath = root.get("id");
				Path<String> namePath = root.get("name");
				//将dutyname暂用为分组名称来查询
				Path<String> groupnamePath=root.get("groupid").get("groupname");
				//
				Path<String> areaIdPath = root.get("areaId");
				if(!StringUtils.isNullOrEmpty(id)){
					predicateList.add(criteriaBuilder.equal(idPath, id));
				}
				Predicate namePredicate = null;
				Predicate dutyPredicate = null;
				List<Predicate> orPredicates = new ArrayList<Predicate>();
				// 查询名称
				if(!StringUtils.isNullOrEmpty(name)){
					namePredicate = criteriaBuilder.like(namePath, "%" +name.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0));
					orPredicates.add(namePredicate);
				}
				if(!StringUtils.isNullOrEmpty(dutyName)){
					dutyPredicate = criteriaBuilder.like(groupnamePath, "" +dutyName.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0));
					orPredicates.add(dutyPredicate);
				}
				if(orPredicates.size() > 0){
					Predicate[] predicatess = new Predicate[orPredicates.size()];
					orPredicates.toArray(predicatess);
					predicateList.add(criteriaBuilder.or(predicatess));
				}
				if(!StringUtils.isNullOrEmpty(userId)){
					predicateList.add(criteriaBuilder.equal(userIdPath, userId));
				}
				if(!StringUtils.isNullOrEmpty(enterpriseId)){
					predicateList.add(criteriaBuilder.equal(enterpriseIdPath, enterpriseId));
				}
				if(!StringUtils.isNullOrEmpty(areaId)){
					predicateList.add(criteriaBuilder.equal(areaIdPath, areaId));
				}
			
				Predicate[] predicates = new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				criterialQuery.where(predicates);
				return null;
			}};
			
			Map<String, Object> m= null;
			// 如果pageNo = -1 表示查询所有结果
			if(-1 != pageNo){
				m = new HashMap<String, Object>();
				PageRequest request = buildPageRequest(pageNo, pageSize,null, Direction.DESC,new String[]{"createTime","updateTime"});
				Page<PointEntity> page=pointDao.findAll(spec, request);
				List<NetPointInfoResult> npis = assembleResult(page.getContent(),dcode,enterpriseId);
				m.put("totalPages", page.getTotalPages());
				m.put("page", pageNo);
				m.put("totalCount", page.getTotalElements());
				m.put("records", npis);
			}else{
				// 如果不是输入查询，则进行全部查询
				if(org.springframework.util.StringUtils.isEmpty(name) && org.springframework.util.StringUtils.isEmpty(dutyName)){
					List listMaps = this.pointDao.queryAllPointAndExc(userId);
					List<NetPointInfoResult> npis = assembleNativeQueryResult(listMaps);
					m = new HashMap<String, Object>();
					m.put("records", npis);
				}else{
					Sort sort = new Sort(Direction.DESC,new String[]{"createTime","updateTime"});
					List<PointEntity> pes = pointDao.findAll(spec,sort);
					if(null != pes && pes.size() > 0){
						m = new HashMap<String, Object>();
						List<NetPointInfoResult> npis = assembleResult(pes,dcode,enterpriseId);
						m.put("records", npis);
					}
				}
			}
			
		return m;
	}
	
	private List<NetPointInfoResult> assembleResult(List<PointEntity> list,String dcode,String eid){
		return assembleResult(list, dcode,eid,false);
	}
	
	@SuppressWarnings("unchecked")
	private List<NetPointInfoResult> assembleNativeQueryResult(List queryResult){
		List<NetPointInfoResult> npis = null;
		if(null != queryResult && queryResult.size() > 0){
			npis = new ArrayList<NetPointInfoResult>();
			//先查询出所有分组，再匹配
			Map<String,PointGroupEntity> groupmap=null;
			Object obs[]=(Object[])queryResult.get(0);
			String userid=(String) obs[7];
			String eid=(String) obs[11];
			
			/**
			 * 2016.6.2查询总账号的分组，子账号不允许增加修改或删除分组，分组、样式、字段都以总账号为准
			 */
			UserEntity topuser=this.userService.findTopUserByEid(eid);
			
			List<PointGroupEntity> grouplist=pointGroupService.queryAllGroups(null, null, topuser.getId(), null, null);
			if(grouplist!=null&&grouplist.size()>0){
				groupmap=new HashMap<String,PointGroupEntity>();
				for(PointGroupEntity p:grouplist){
					groupmap.put(p.getId(), p);
				}
			}
			
			//获取所有部门ID，部门CODE
			Map<String,String> deptcodeMap=new HashMap<String,String>();
			if(null!=eid&&!eid.equals("")){
				deptcodeMap=getDeptCodesByEid(eid);
			}
			
			NetPointInfoResult npi=null;
			for(int i = 0,resultsize=queryResult.size();i<resultsize ;i++){
				Object[] objs = (Object[]) queryResult.get(i);
				npi = new NetPointInfoResult();
				npi.setDcode(deptcodeMap.get((String) objs[12]));
				npi.setAddress((String) objs[13]);
				npi.setAreaId((String) objs[6]);
				npi.setCol1(StringUtils.isNullOrEmpty((String)objs[21])?"":(String)objs[21]);
				npi.setCol2(StringUtils.isNullOrEmpty((String)objs[22])?"":(String)objs[22]);
				npi.setCol3(StringUtils.isNullOrEmpty((String)objs[23])?"":(String)objs[23]);
				npi.setCol4(StringUtils.isNullOrEmpty((String)objs[24])?"":(String)objs[24]);
				npi.setCol5(StringUtils.isNullOrEmpty((String)objs[25])?"":(String)objs[25]);
				npi.setCol6(StringUtils.isNullOrEmpty((String)objs[26])?"":(String)objs[26]);
				npi.setCol7(StringUtils.isNullOrEmpty((String)objs[27])?"":(String)objs[27]);
				npi.setCol8(StringUtils.isNullOrEmpty((String)objs[28])?"":(String)objs[28]);
				npi.setCol9(StringUtils.isNullOrEmpty((String)objs[29])?"":(String)objs[29]);
				npi.setCol10(StringUtils.isNullOrEmpty((String)objs[30])?"":(String)objs[30]);
				npi.setCreateTime((Date) objs[8]);
				npi.setDeleteFlag((Byte) objs[10]);
				npi.setDepartmentId((String) objs[12]);
				npi.setDutyName((String) objs[4]);
				npi.setDutyPhone((String) objs[5]);
				npi.setDutyPicPath((String) objs[31]);
				npi.setEnterpriseId((String) objs[11]);
				npi.setIconStyle((String) objs[16]);
				npi.setId((String) objs[0]);
				npi.setName((String) objs[1]);
				npi.setNetPicPath((String) objs[15]);
				npi.setSmx((BigDecimal) objs[2]);
				npi.setSmy((BigDecimal) objs[3]);
				npi.setStatus((Integer) objs[14]);
				npi.setUpdateTime((Date) objs[9]);
				npi.setUserId((String) objs[7]);
				npi.setUsername((String) objs[32]);
				// 查询区域面
				String areaId = (String) objs[6];
				LOGGER.info("## AREA_ID : " + areaId + ", status : " + npi.getStatus());
				if (!StringUtils.isNullOrEmpty(areaId)) {
					AreaEntity ae = null;
					try {
						//ae = areaService.queryByIdOrNumber(areaId, null, dcode, false);
						ae = areaService.queryByIdOrNumber(areaId, null, null, false);//9.30号 取消dcode，可以对应到子账号的区划
					} catch (AreaException e) {
						e.printStackTrace();
					}
					if (null != ae) {
						npi.setAreaName(ae.getName());
					}
				}
				
				String groupid= (String) objs[17];
				String styleid= (String) objs[18];
				LOGGER.info("## 开始查询分组样式groupid : " + groupid + ", styleid : " + styleid);
				if(!StringUtils.isNullOrEmpty(groupid)){
					PointGroupEntity group=null;
					if(groupmap!=null){
						group=groupmap.get(groupid);
					}
					//group=this.pointGroupDao.findById(groupid);
					npi.setGroupid(group);
				}
				if(!StringUtils.isNullOrEmpty(styleid)){
					PointStyleEntity style=null;
					style=this.pointStyleDao.findById(styleid);
					npi.setStyleid(style);
				}
				npis.add(npi);
			}
		}
		return npis;
	}
	
	private List<NetPointInfoResult> assembleResult(List<PointEntity> list, String dcode,String eid,boolean isReturnSMLL) {
		List<NetPointInfoResult> netInfo = new ArrayList<NetPointInfoResult>();
		NetPointInfoResult npi=null;
		
		//获取所有部门ID，部门CODE
		Map<String,String> deptcodeMap=new HashMap<String,String>();
		if(null!=eid&&!eid.equals("")){
			deptcodeMap=getDeptCodesByEid(eid);
		}
		
		for (int i = 0,listsize=list.size(); i < listsize; i++) {
			npi = new NetPointInfoResult();
			PointEntity pe = list.get(i);
			PointExtcolValEntity valentity=pe.getPointExtcolValEntity();
			BeanUtils.copyProperties(pe, npi);
			
			npi.setDcode(deptcodeMap.get(pe.getDepartmentId()));
			
			//将自定义字段属性直接加到NetPointInfoResult中
			npi.setCol1(valentity==null?null:valentity.getCol1());
			npi.setCol2(valentity==null?null:valentity.getCol2());
			npi.setCol3(valentity==null?null:valentity.getCol3());
			npi.setCol4(valentity==null?null:valentity.getCol4());
			npi.setCol5(valentity==null?null:valentity.getCol5());
			npi.setCol6(valentity==null?null:valentity.getCol6());
			npi.setCol7(valentity==null?null:valentity.getCol7());
			npi.setCol8(valentity==null?null:valentity.getCol8());
			npi.setCol9(valentity==null?null:valentity.getCol9());
			npi.setCol10(valentity==null?null:valentity.getCol10());
			
			npi.setStyleid(pe.getStyleid());
			npi.setGroupid(pe.getGroupid());
			npi.setPointExtcolValEntity(null);
			
			if(isReturnSMLL && npi.getSmx() != null && npi.getSmy() != null){
				com.supermap.entity.Point p = new com.supermap.entity.Point();
				p.setLon(pe.getSmx().doubleValue());
				p.setLat(pe.getSmy().doubleValue());
				p = SuperMapCoordinateConvertImpl.smMCToLatLon(p);
				npi.setSmx(BigDecimal.valueOf(p.getLon()));
				npi.setSmy(BigDecimal.valueOf(p.getLat()));
			}
			
			String areaId = pe.getAreaId();
			LOGGER.info("## AREA_ID : " + areaId + ", status : " + npi.getStatus());
			if (!StringUtils.isNullOrEmpty(areaId)) {
				AreaEntity ae = null;
				try {
					//ae = areaService.queryByIdOrNumber(areaId, null, dcode, false);
					ae = areaService.queryByIdOrNumber(areaId, null, null, false);//9.30号 取消dcode，可以对应到子账号的区划
				} catch (AreaException e) {
					e.printStackTrace();
				}
				if (null != ae) {
					npi.setAreaName(ae.getName());
				}
			}
			netInfo.add(npi);
		}

		return netInfo;
	}

	  /**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,String defaltProperty, Direction direction,String[] propteries) {
		Sort sort = null;
		if (!StringUtils.isNullOrEmpty(defaltProperty) && "auto".equals(defaltProperty)) {
			sort = new Sort(direction, "id");
		}else{
			sort = new Sort(direction,propteries);
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	
	
	
	@Override
	@Transactional(readOnly = true)
	public PointEntity queryById(String id) {
		//return this.pointDao.findOneByPointid(id);
		PointEntity point=null;
		point=this.pointDao.findOne(id);
		if(point!=null&&point.getPointExtcolValEntity()!=null){
			point.getPointExtcolValEntity().setPointEntity(null);
		}
		return point;
	}

	@Override
	@Transactional
	public boolean updatePoint(PointEntity pe) throws Exception {
		boolean flag=false;
		String id = pe.getId();
		PointEntity pe2 = this.pointDao.findOne(id);
		if(null == pe2){
			return false;
		}
		
		//判断名称重复
		if(pe.getName()!=null&&pe2.getName()!=null&&!pe.getName().equals(pe2.getName())){
			isExsitPointname(pe.getName(),pe2.getUserId(),pe2.getEnterpriseId(),pe2.getDepartmentId());
		}
		
		BeanUtils.copyProperties(pe, pe2, BeanTool.getNullPropertyNames(pe));
		//设置 groupid，styleid为null
		if(pe.getGroupid()==null){
			pe2.setGroupid(null);
		}
		if(pe.getStyleid()==null){
			pe2.setStyleid(null);
		}
		
		pe2.setUpdateTime(new Date());
		pe2.setPointExtcolValEntity(null);

		//admincode、省市区
		if(pe2.getSmx()!=null&&pe2.getSmy()!=null){
			Map<String,String> m=geocodingService.searchAdmincodeForCounty(pe2.getSmx().doubleValue(),pe2.getSmy().doubleValue());
			if(m!=null){
				pe2.setAdmincode(m.get("ADMINCODE"));
				pe2.setProvince(m.get("PROVINCE"));
				pe2.setCity(m.get("CITY2"));
				pe2.setCounty(m.get("COUNTY"));
			}else{
				pe2.setAdmincode("");
				pe2.setProvince("");
				pe2.setCity("");
				pe2.setCounty("");
				pe2.setStatus(1);
			}
		}
		
		this.pointDao.save(pe2);
		flag=true;
		return flag;
	}

	@Override
	@Transactional
	public boolean bindCar(final String netId, String[] carIds) {
		long count = this.mapPointCarDao.getBindCarsCount(netId);
		if(count > 0){
			this.mapPointCarDao.deleteByNetIds(netId);
		}
		for(final String carId: carIds){
			MapPointCarEntity mce = new MapPointCarEntity();
			mce.setCarId(carId);
			mce.setPointId(netId);
			this.mapPointCarDao.save(mce);
		}
		return true;
	}

	@Override
	public List<PointEntity> queryByAreaId(final String aid,final  String enterpriseId,final  String departmentId) {
		Specification<PointEntity> spec = new Specification<PointEntity>() {
			@Override
			public Predicate toPredicate(Root<PointEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> areaId = root.get("areaId");
				Path<String> eid = root.get("enterpriseId");
				Path<String> pid = root.get("departmentId");
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(areaId, aid));
				if(!StringUtils.isNullOrEmpty(enterpriseId)){
					predicates.add(builder.equal(eid, enterpriseId));
				}
				if(!StringUtils.isNullOrEmpty(departmentId)){
					predicates.add(builder.equal(pid, departmentId));
				}
				Predicate[] pres = new Predicate[predicates.size()];
				predicates.toArray(pres);
				query.where(pres);
				return null;
			}
		};
		return this.pointDao.findAll(spec);
	}

	@Override
	public List<String> queryBindCar(final String netId) {
		Specification<MapPointCarEntity> spec = new Specification<MapPointCarEntity>() {
			@Override
			public Predicate toPredicate(Root<MapPointCarEntity> root, CriteriaQuery<?> criterialQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
				Path<String> pointId = root.get("pointId");
				predicateList.add(criteriaBuilder.equal(pointId, netId));
				Predicate[] predicates = new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				criterialQuery.where(predicates);
				return null;
			}
		};
		List<String> result = null;
		try {
			List<MapPointCarEntity> mapCars = this.mapPointCarDao.findAll(spec);
			if (null != mapCars) {
				result = new ArrayList<String>();
				for (int i = 0; i < mapCars.size(); i++) {
					MapPointCarEntity mpc = mapCars.get(i);
					result.add(mpc.getCarId());
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}

	@Override
	@Transactional
	public Map<String,Object> importNetPoints(List<PointEntity> netPoints) throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();
		if(null != netPoints && netPoints.size() > 0){
			LOGGER.info("## 开始坐标转换。。。");
			// 坐标转换
			for(int i=0;i<netPoints.size();i++){
				PointEntity pe = netPoints.get(i);
				BigDecimal smx = pe.getSmx();
				BigDecimal smy = pe.getSmy();
				if(null != smx && null != smy){
					com.supermap.entity.Point p = new com.supermap.entity.Point();
					p.setLon(smx.doubleValue());
					p.setLat(smy.doubleValue());
					if(BaiduCoordinateConvertImpl.isLLPoint(p)){
						p = SuperMapCoordinateConvertImpl.smLL2MC(p);
						pe.setSmx(BigDecimal.valueOf(p.getLon()));
						pe.setSmy(BigDecimal.valueOf(p.getLat()));
						
						//admincode、省市区
						if(pe.getSmx()!=null&&pe.getSmy()!=null){
							Map<String,String> m=geocodingService.searchAdmincodeForCounty(pe.getSmx().doubleValue(),pe.getSmy().doubleValue());
							if(m!=null){
								pe.setAdmincode(m.get("ADMINCODE"));
								pe.setProvince(m.get("PROVINCE"));
								pe.setCity(m.get("CITY2"));
								pe.setCounty(m.get("COUNTY"));
							}else{
								pe.setStatus(StatusConstants.POINT_PROCESSING);
							}
						}
						
					}
				}
			}
			LOGGER.info("## 结束坐标转换，开始写入数据库。。。");
			// 入库
			//Iterable<PointEntity> iterable = this.pointDao.save(netPoints);
			//ids = getIds(iterable);
			map=this.savePoint(netPoints);
			LOGGER.info("## 结束写入数据库。。。");
		}
		return map;
	}
	

	/**
	 * 
	 * <p>Title: AddressMatch</p>
	 * Description:		地址解析线程
	 *
	 * @author Huasong Huang
	 * CreateTime: 2014-10-23 上午11:47:17
	 */
	class AddressMatch implements Runnable{
		
		private List<String> ids = null;
		
		public AddressMatch(){}
		public AddressMatch(List<String> ids){
			this.ids = ids;
		}
		
		@Override
		@Transactional
		public void run() {
			LOGGER.info("## 开始地址解析线程。。");
			try {
				// 查询只有地址没有坐标
				List<PointEntity> pointEntitys = pointDao.queryByCreatetimeAndXYisNull(this.ids);
				if(null != pointEntitys && pointEntitys.size() > 0){
					LOGGER.info("## 查询到需要地址解析的网点数量："+pointEntitys.size());
					String userid=pointEntitys.get(0).getUserId();
					// 构建地址匹配参数
					//List<AddresInfoDetails> params = buildAddressMatchParam(pointEntitys);
					List<AddressMatchParam> addressInfo=buildAddressInfo(pointEntitys);
					
					//List<AddressMatchResult> results = addressMatch.search(params);
					//List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> results= addressMatch.addressMatch(addressInfos,"SMC");
					// 判断是否需要进行地址拆分
					List<List<AddressMatchParam>> params = splitAddresses(addressInfo);

					// 创建线程
					ExecutorService exec = Executors.newFixedThreadPool(params.size());
					List<AddressSplitCallable> lstCallables = new ArrayList<AddressSplitCallable>();
					for(int i=0;i<params.size();i++){
						List<com.supermap.egisp.addressmatch.beans.AddressMatchParam> addressInfos = params.get(i);
						AddressSplitCallable callable = null;
						callable = new AddressSplitCallable(addressInfos,userid+"_"+i,addressMatch);
						lstCallables.add(callable);
					}
					// 启动线程执行
					List<java.util.concurrent.Future<List<com.supermap.egisp.addressmatch.beans.AddressMatchResult>>> lstFutures = null;
					List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> results = new LinkedList<com.supermap.egisp.addressmatch.beans.AddressMatchResult>();
					
					try {
						lstFutures = exec.invokeAll(lstCallables);
						int timeoutCount = 0;
						// 等待获取结果
						for (int i = 0; i < params.size(); i++) {
							Future<List<com.supermap.egisp.addressmatch.beans.AddressMatchResult>> future = (Future<List<com.supermap.egisp.addressmatch.beans.AddressMatchResult>>) lstFutures.get(i);
							List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> result = future.get();
							if(null == result || result.size() <= 0){
								timeoutCount += params.get(i).size();
								// 发生超时，需要构造超时结果；
								result = buildTimeoutResult(params.get(i),"地址解析超时");
							}
							results.addAll(result);
						}
					} catch (Exception e1) {
						throw new NullPointerException(e1.getMessage());
					}finally{
						if(null != exec){
							exec.shutdown();
						}
					}
					
					
					List<PointEntity> needUpdate = new ArrayList<PointEntity>();
					Date updateTime = new Date();
					for(int i=0;i<results.size();i++){
						com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult result = (DefaultAddressMatchResult) results.get(i);
						PointEntity pointEntity = pointEntitys.get(i);
						if(result.getX() > 0 && result.getY() > 0){
							pointEntity.setSmx(BigDecimal.valueOf(result.getX()));
							pointEntity.setSmy(BigDecimal.valueOf(result.getY()));
							pointEntity.setStatus(StatusConstants.POINT_COMMON);
							pointEntity.setAdmincode(result.getAdmincode());
							pointEntity.setProvince(result.getProvince());
							pointEntity.setCity(result.getCity());
							pointEntity.setCounty(result.getCounty());
						}else{
							pointEntity.setStatus(StatusConstants.POINT_DISABLE);
						}
						pointEntity.setUpdateTime(updateTime);
						needUpdate.add(pointEntity);
					}
					
					PointEntity temp=null;
					if(this.ids!=null&&this.ids.size()>0){
						//查询此批网点是否被删除
						temp=pointDao.findOne(this.ids.get(0));
					}
					
					if(needUpdate.size() > 0&&temp!=null&&temp.getId()!=null){
						pointDao.save(needUpdate);
					}
				}
				LOGGER.info("## 结束地址解析线程，开始反向地址解析线程。。。");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			// 处理只有坐标无地址的网点
			new Thread(new ReverseAddressMatch(this.ids)).start();
		}
		
		/**
		 * 
		 * <p>Title ：buildAddressMatchParam</p>
		 * Description：		构建地址匹配参数
		 * @param pointEntitys
		 * @return
		 * Author：Huasong Huang
		 * CreateTime：2014-10-23 上午11:25:06
		 */
		private List<AddresInfoDetails> buildAddressMatchParam(List<PointEntity> pointEntitys){
			List<AddresInfoDetails> params = new ArrayList<AddresInfoDetails>();
			for(PointEntity pe : pointEntitys){
				AddresInfoDetails details = new AddresInfoDetails();
				details.setAddress(pe.getAddress());
				details.setId(pe.getId());
				params.add(details);
			}
			return params;
		}
		
		
		private List<AddressMatchParam> buildAddressInfo(List<PointEntity> pointEntitys){
			List<AddressMatchParam> params = new ArrayList<AddressMatchParam>();
			for(PointEntity pe : pointEntitys){
				AddressMatchParam details = new AddressMatchParam();
				details.setAddress(pe.getAddress());
				details.setId(pe.getId());
				params.add(details);
			}
			return params;
		}
		
		//拆分地址
		private List<List<AddressMatchParam>> splitAddresses(List<AddressMatchParam> addresses){
			int paramSize = addresses.size();
			if(paramSize > AddressMatchConstants.LOGISTICS_BATCH_MAX_SIZE){
				throw new NullPointerException("地址数量["+paramSize+"]超过["+AddressMatchConstants.LOGISTICS_BATCH_MAX_SIZE+"]");
			}
			int DEFAULT_PROGRESS_COUNT = AddressMatchConstants.MULTI_THREAD_COUNT;
			int progressCount = paramSize <= DEFAULT_PROGRESS_COUNT ? 1 : DEFAULT_PROGRESS_COUNT;
			int num = paramSize / DEFAULT_PROGRESS_COUNT;
			List<List<AddressMatchParam>> params = new ArrayList<List<AddressMatchParam>>();
			for (int i = 0; i < progressCount; i++) {
				List<AddressMatchParam> paramList = null;
				if (i < progressCount - 1) {
					paramList = addresses.subList(i * num, (i + 1) * num);
				} else {
					paramList = addresses.subList(i * num, addresses.size());
				}
				params.add(paramList);
			}
			return params;
		}
		
		//地址匹配失败
		private List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> buildTimeoutResult(
				List<AddressMatchParam> list, String string) {

			List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> result = new ArrayList<com.supermap.egisp.addressmatch.beans.AddressMatchResult>();
			for (int i = 0; i < list.size(); i++) {
				DefaultAddressMatchResult dmar = new DefaultAddressMatchResult();
				dmar.setId(list.get(i).getId());
				result.add(dmar);
			}
			return result;
		}
		
	}


	/**
	 * 
	 * <p>Title: reverseAddressMatch</p>
	 * Description:		反向地址解析线程
	 *
	 * @author Huasong Huang
	 * CreateTime: 2014-10-23 上午11:47:54
	 */
	class ReverseAddressMatch implements Runnable{

		
		private List<String> ids;
		
		public ReverseAddressMatch(List<String> ids){
			this.ids = ids;
		}
		
		
		@Override
		@Transactional
		public void run() {
			
			try {
				// 根据时间查询只有地址没有坐标的网点
				List<PointEntity> pointEntitys = pointDao.queryByCreateTimeAndAddressIsNull(this.ids);
				Date updateTime = new Date();
				if(null != pointEntitys && pointEntitys.size() > 0){
					for (int i = 0; i < pointEntitys.size(); i++) {
						PointEntity pointEntity = pointEntitys.get(i);
						/*ReverseMatchResult rmr = iaddressMatch.reverseMatch(pointEntity.getSmx().doubleValue(), pointEntity
								.getSmy().doubleValue(), -1, AddressMatchConstants.REVERSE_MATCH_RANGE);*/
						ReverseAddressMatchResult rmr = (ReverseAddressMatchResult) addressMatch.addressMatchByCoor(pointEntity.getSmx().doubleValue(), pointEntity
								.getSmy().doubleValue(), -1, AddressMatchConstants.REVERSE_MATCH_RANGE);
						StringBuilder addressBuilder = new StringBuilder();
						if(rmr != null){
							addressBuilder.append(rmr.getProvince());
							if(!StringUtils.isNullOrEmpty(rmr.getCity())){
								addressBuilder.append(rmr.getCity());
							}
							if(!StringUtils.isNullOrEmpty(rmr.getCounty())){
								addressBuilder.append(rmr.getCounty());
							}
							if(!StringUtils.isNullOrEmpty(rmr.getAddress())){
								addressBuilder.append(rmr.getAddress());
							}
							if(!StringUtils.isNullOrEmpty(rmr.getName())){
								addressBuilder.append(rmr.getName());
							}
							
							pointEntity.setAddress(addressBuilder.toString());
							pointEntity.setStatus(StatusConstants.POINT_COMMON);
						}else{
							pointEntity.setStatus(StatusConstants.POINT_DISABLE);
						}
						pointEntity.setUpdateTime(updateTime);
					}
				}
				
				PointEntity temp=null;
				if(this.ids!=null&&this.ids.size()>0){
					//查询此批网点是否被删除
					temp=pointDao.findOne(this.ids.get(0));
				}
				
				if(temp!=null&&temp.getId()!=null){
					// 如果需要更新的列表不为空，则进行更新
					pointDao.save(pointEntitys);
				}
				
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			LOGGER.info("## 结束反向地址解析线程。。。");
			// 对处理结束状态任然为处理中的网点进行状态变更
			List<PointEntity> pointEntitys = pointDao.queryProcessPoints(this.ids);
			if(null != pointEntitys && pointEntitys.size() > 0){
				for(PointEntity pe : pointEntitys){
					pe.setStatus(StatusConstants.POINT_DISABLE);
					pe.setUpdateTime(new Date());
				}
				
				PointEntity temp=null;
				if(this.ids!=null&&this.ids.size()>0){
					//查询此批网点是否被删除
					temp=pointDao.findOne(this.ids.get(0));
				}
				
				if(temp!=null&&temp.getId()!=null){
					pointDao.save(pointEntitys);
				}
			}
			
		}
	}


	@Override
	public Map<String, Object> queryByAreaIdOrName(final String areaId, final String netName, final String userId,
			final String enterpriseId, final String departmentId, int pageNo, int pageSize) {
	
		PageRequest request = buildPageRequest(pageNo, pageSize, null,Direction.DESC,new String[]{"createTime","updateTime"});
		Specification<PointEntity> spec = new Specification<PointEntity>() {

			
			@Override
			public Predicate toPredicate(Root<PointEntity> root, CriteriaQuery<?> criterialQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				Path<String> userIdPath = root.get("userId");
				Path<String> enterpriseIdPath = root.get("enterpriseId");
				Path<String> departmentIdPath = root.get("departmentId");
				Path<String> namePath = root.get("name");
				Path<String> areaIdPath = root.get("areaId");
				if(!StringUtils.isNullOrEmpty(netName)){
					predicateList.add(criteriaBuilder.like(namePath, "%" +netName+"%"));
				}
				if(!StringUtils.isNullOrEmpty(userId)){
					predicateList.add(criteriaBuilder.equal(userIdPath, userId));
				}
				if(!StringUtils.isNullOrEmpty(enterpriseId)){
					predicateList.add(criteriaBuilder.equal(enterpriseIdPath, enterpriseId));
				}
				if(!StringUtils.isNullOrEmpty(departmentId)){
					predicateList.add(criteriaBuilder.equal(departmentIdPath, departmentId));
				}
				if(!StringUtils.isNullOrEmpty(areaId)){
					predicateList.add(criteriaBuilder.equal(areaIdPath, areaId));
				}
				Predicate[] predicates = new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				criterialQuery.where(predicates);
				return null;
			}};
			
		Map<String, Object> m = null;
		// 如果pageNo = -1 表示查询所有结果
		m = new HashMap<String, Object>();
		Page<PointEntity> page = pointDao.findAll(spec, request);
		List<NetPointInfoResult> npis = assembleResult(page.getContent(),null,enterpriseId,true);
		m.put("total", page.getTotalPages());
		m.put("page", pageNo);
		m.put("records", page.getTotalElements());
		m.put("rows", npis);
		return m;
	}

	@Override
	@Transactional
	public void startProcess(List<String> ids) {
		// 启动地址解析处理线程,该线程中包含了处理有坐标无地址的线程
		new Thread(new AddressMatch(ids)).start();
	}
	
	
	/**
	 * 保存网点，以及网点自定义字段
	 * @param netPoints
	 * @return
	 * @throws Exception 
	 * @Author Juannyoh
	 * 2015-8-25下午7:13:55
	 */
	public Map<String,Object> savePoint(List<PointEntity> netPoints) throws Exception {
		List<String> strlist=new ArrayList<String>();
		List<String> namelist=new ArrayList<String>();
		
		//批量判断重复,减少耗时
		if(netPoints!=null&&netPoints.size()>0){
			String userid=netPoints.get(0).getUserId();
			namelist=compareRenames(netPoints,userid);
			
			for(PointEntity point:netPoints){
				PointExtcolValEntity extval=point.getPointExtcolValEntity();
				point.setPointExtcolValEntity(null);
				
				if(namelist!=null&&namelist.size()>0){
					if(namelist.contains(point.getName())){
//						continue;
						point.setName(point.getName()+"_"+RandomStringUtils.randomAlphanumeric(6));//对于名称有重复的生成随机串加在名称后面
					}
				}
				
				//导入分组
				if(point.getGroupid()!=null&&(point.getGroupid().getId()==null||point.getGroupid().getId().equals(""))){
					this.pointGroupDao.save(point.getGroupid());
				}
				//
				
				PointEntity idEntity=this.pointDao.save(point);
				String id=idEntity.getId();
				if(extval!=null){
					extval.setPointid(id);
					this.pointExtcolValDao.save(extval);
				}
				strlist.add(id);
			}
		}
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ids", strlist);
//		map.put("namelist", namelist);
		map.put("namelist", null);
		return map;
	}
	
	public List<String> getIds(Iterable<PointEntity> iterable){
		List<String> ids=new ArrayList<String>();
		for(PointEntity o:iterable){
			ids.add(o.getId());
		}
		return ids;
	}
	
	/**
	 * 判断网点名称是否重复
	 * @param name
	 * @param userId
	 * @param enterpriseId
	 * @param departmentId
	 * @return
	 * @throws Exception 
	 * @Author Juannyoh
	 * 2015-10-21上午10:37:37
	 */
	public boolean isExsitPointname(final String name,final String userId,final String enterpriseId, final String departmentId) throws Exception{
		boolean flag=false;
		Specification<PointEntity> spec = new Specification<PointEntity>() {
			@Override
			public Predicate toPredicate(Root<PointEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> namePath = root.get("name");
				Path<String> uid = root.get("userId");
				Path<String> eid = root.get("enterpriseId");
				Path<String> pid = root.get("departmentId");
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(namePath, name));
				predicates.add(builder.equal(uid, userId));
				predicates.add(builder.equal(eid, enterpriseId));
				predicates.add(builder.equal(pid, departmentId));
				Predicate[] pres = new Predicate[predicates.size()];
				predicates.toArray(pres);
				query.where(pres);
				return null;
			}
		};
		int pointcount= (int) this.pointDao.count(spec);
		if(pointcount>0){
			flag=true;
			throw new Exception("网点名称重复："+name);
		}
		return flag;
	}
	
	
	/**
	 * 批量判断重复名称
	 * @param excellist
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-23上午11:22:32
	 */
	public List<String> compareRenames(List<PointEntity> excellist,
			String userid) {
		List<String> renames = new ArrayList<String>();
		List listMaps = this.pointDao.queryAllPointAndExc(userid);// 该用户的所有网点
		List<NetPointInfoResult> npis = assembleNativeQueryResult(listMaps);

		List<String> exsitnames = new ArrayList<String>();
		List<String> excelnames = new ArrayList<String>();
		if (excellist != null && excellist.size() > 0) {
			for (PointEntity p : excellist) {
				excelnames.add(p.getName());
			}
		}

		if (npis != null && npis.size() > 0) {
			for (NetPointInfoResult r : npis) {
				exsitnames.add(r.getName());
			}
		}

		//excelnames.addAll(exsitnames);// 将两个list合并
		
		if(excelnames!=null){
			String temp = "";
			for(int i=0;i<excelnames.size();i++){
				temp=excelnames.get(i);
				if(exsitnames.contains(temp)){
					renames.add(temp);
				}
			}
		}
		
		String temp = "";
		for (int i = 0; i < excelnames.size() - 1; i++) {
			temp = excelnames.get(i);
			for (int j = i + 1; j < excelnames.size(); j++) {
				if (temp.equals(excelnames.get(j))) {
					renames.add(temp);
				}
			}
		}
		
		List<String> listWithoutDup = new ArrayList<String>(new HashSet<String>(renames));
		
		return listWithoutDup;
	}

	@Override
	public List<PointEntity> findByStyleid(String styleid) {
		return this.pointDao.findByStyleid(styleid);
	}

	@Override
	public List<PointEntity> findByGroupid(String groupid) {
		return this.pointDao.findByGroupid(groupid);
	}

	@Transactional
	@Override
	public int updatePointDefaultStyle(String userid) {
		return this.pointDao.updatePointDefaultStyle(userid);
	}

	@Transactional
	@Override
	public int updatePointStylesToNUll(List<String> ids) {
		return this.pointDao.updatePointStylesToNUll(ids);
	}

	
	/**
	 * 按省市区查询网点
	 */
	@Override
	public Map<String, Object> queryAllByAdmincode(final String userId, final String name,
			final String groupid, final String id, final String enterpriseId,
			final String dcode, int pageNo, int pageSize, final String areaId,
			final String Admincode) {
		Map<String, Object> m= null;
		
		/**
		 * 根据部门code查找部门本级及下级所有用户
		 * 查找所有子部门
		 */
		List<String> deptids=getChildDeptIdsByDeptcode(dcode);
		final List<String> finaldeptids=deptids;
		
		if(StringUtils.isNullOrEmpty(Admincode)){//admincode为空，表示全国查询
			m = new HashMap<String, Object>();
			List list=this.pointDao.getPointCount2PorviceByDeptIds(deptids,1,2);//包括子账号
			m.put("records", formatConvergeResult(list,1));
		}
		else{
//			Specification<PointEntity> spec =  getCommonSpec(userId, name,groupid, id,enterpriseId,
//					finaldeptids ,pageNo,pageSize,areaId,Admincode,true);
			Specification<PointEntity> spec =  getCommonSpec(userId, name,groupid, id,enterpriseId,
					null ,pageNo,pageSize,areaId,Admincode,false);
				
				// 如果pageNo = -1 表示查询所有结果
				if(-1 != pageNo){
					m = new HashMap<String, Object>();
					PageRequest request = buildPageRequest(pageNo, pageSize,null, Direction.DESC,new String[]{"createTime","updateTime"});
					Page<PointEntity> page=pointDao.findAll(spec, request);
					List<NetPointInfoResult> npis = assembleResult(page.getContent(),dcode,enterpriseId);
					m.put("totalPages", page.getTotalPages());
					m.put("page", pageNo);
					m.put("totalCount", page.getTotalElements());
					m.put("records", npis);
				}else{
					// 如果不是输入查询，则进行全部查询
					if(org.springframework.util.StringUtils.isEmpty(name)&&org.springframework.util.StringUtils.isEmpty(groupid)){
//						List listMaps = this.pointDao.queryAllPointAndExcByDeptidsAdmincode(deptids,Admincode+"%");
						List listMaps = this.pointDao.queryAllPointAndExcByEidAdmincode(enterpriseId, Admincode+"%");
						List<NetPointInfoResult> npis = assembleNativeQueryResult(listMaps);
						m = new HashMap<String, Object>();
						m.put("records", npis);
					}else{
						Sort sort = new Sort(Direction.DESC,new String[]{"createTime","updateTime"});
						List<PointEntity> pes = pointDao.findAll(spec,sort);
						if(null != pes && pes.size() > 0){
							m = new HashMap<String, Object>();
							List<NetPointInfoResult> npis = assembleResult(pes,dcode,enterpriseId);
							m.put("records", npis);
						}
					}
				}
		}
		return m;
	}

	/**
	 * 带返回值的进程执行
	 */
	@Override
	public boolean startProcessOver(List<String> ids) {
		boolean flag=false;
		// 启动地址解析处理线程,该线程中包含了处理有坐标无地址的线程
		Thread thread=new Thread(new AddressMatch(ids));
		thread.start();
		while(true){
			if(!thread.isAlive()){
				flag=true;
				return flag;
			}
		}
	}

	@Override
	public int deletePointsByIds(List<String> ids) {
		int result=this.pointDao.deletePointsByIds(ids);
		this.pointDao.deletePointsExtValByIds(ids);
		return result;
	}

	/**
	 * 查找用户还有哪些状态为处理中的网点数据
	 */
	@Override
	public List<String> getAllProcessingPointByUserid(String userid) {
		List<String> ids=null;
		List list=this.pointDao.getAllProcessingPointByUserid(userid);
		if(list!=null&&list.size()>0){
			ids=new ArrayList<String>();
			for(int i=0;i<list.size();i++){
				String id=(String)list.get(i);
				ids.add(id);
			}
		}
		return ids;
	}

	@Override
	public Map<String, Object> queryFailedPoints(String userId) {
		Map<String, Object> m=null;
		/**
		 * 根据部门code查找部门本级及下级所有用户
		 * 查找所有子部门
		 */
		UserEntity user=this.userService.findUserById(userId);
		List<String> deptids=getChildDeptIdsByDeptcode(user.getDeptId().getCode());
		List list=this.pointDao.queryFailedPoints(deptids);
		
		List<NetPointInfoResult> npis = assembleNativeQueryResult(list);
		if(npis!=null&&npis.size()>0){
			m = new HashMap<String, Object>();
			m.put("records", npis);
		}
		return m;
	}

	@Override
	public PointPicEntity savePointPicture(PointPicEntity pointPicEntity) {
		pointPicEntity = this.pointPicDao.save(pointPicEntity);
		return pointPicEntity;
	}

	@Override
	public void deletePointPictureByPicId(String picid) {
		this.pointPicDao.delete(picid);
	}

	@Override
	public void deletePointPictureByPointid(String pointid) {
		this.pointPicDao.deletePicByPointId(pointid);
	}

	@Override
	public List<PointPicEntity> findPointPicturesByPointid(String pointid) {
		return this.pointPicDao.findByPointid(pointid);
	}

	@Override
	public int getPointCountsByUserid(String userid) {
		return this.pointDao.getPointCountsByUserid(userid).intValue();
	}

	/**
	 * 按省市区聚集 
	 * isApp 代表是否APP，如果是APP则返回的数据减少，只需要坐标、名称、样式id
	 */
	@Override
	public Map<String, Object> queryAllByAdmincodeForConverge(final String userId, final String name,
			final String groupid, final String id, final String enterpriseId,
			final String dcode, int pageNo, int pageSize, final String areaId,
			final String Admincode, boolean isApp) {
		Map<String, Object> m= null;
		
		/**
		 * 根据部门code查找部门本级及下级所有用户
		 * 查找所有子部门
		 */
		List<String> deptids=getChildDeptIdsByDeptcode(dcode);
		final List<String> finaldeptids=deptids;
		
		if(StringUtils.isNullOrEmpty(Admincode)){//admincode为空，表示全国查询
			m = new HashMap<String, Object>();
//			List list=this.pointDao.getPointCount2PorviceByDeptIds(deptids,1,2);//包括子账号
			List list=this.pointDao.getPointCount2PorviceByEid(enterpriseId, 1, 2);//企业下所有的
			m.put("records", formatConvergeResult(list,1));
			m.put("isConverge", true);
			return m;
		}
		else{
			//区县聚集超过500个点时，聚集显示，否则散开
			if(Admincode.length()==6&&!isApp){
//				List list=this.pointDao.getPointCount2CityByDeptIds(deptids,1,6,Admincode+"%");//包括子账号
				List list=this.pointDao.getPointCount2CityByEid(enterpriseId, 1,6,Admincode+"%");//企业下所有的
				List<Map<String, Object>> formatlist=formatConvergeResult(list,3);
				if(formatlist!=null&&formatlist.size()>0){
					for(int i=0,s=formatlist.size();i<s;i++){
						Map<String, Object> map=formatlist.get(i);
						if(map.get("admincode")!=null){
							int count=Integer.parseInt(map.get("count").toString());
							if(count>=config.getWebconvergeNumber()){//判断个数是否超过500个，是则返回聚集结果
								m = new HashMap<String, Object>();
								m.put("records", formatlist);
								m.put("isConverge", true);
								return m;
							}
						}
					}
				}
			}
			//省/市聚集
			if(Admincode.length()<=4){
				if(Admincode.length()==3){
					m = new HashMap<String, Object>();
//					List list=this.pointDao.getPointCount2CityByDeptIds(deptids,1,6,Admincode+"%");//包括子账号
					List list=this.pointDao.getPointCount2CityByEid(enterpriseId, 1,6,Admincode+"%");//企业下所有的
					m.put("records", formatConvergeResult(list,3));
					m.put("isConverge", true);
					return m;
				}else{
					m = new HashMap<String, Object>();
//					List list=this.pointDao.getPointCount2CityByDeptIds(deptids,1,Admincode.length()+2,Admincode+"%");//包括子账号
					List list=this.pointDao.getPointCount2CityByEid(enterpriseId,1,Admincode.length()+2,Admincode+"%");//企业下所有的
					m.put("records", formatConvergeResult(list,(Admincode.length()/2)+1));
					m.put("isConverge", true);
					return m;
				}
			}
			//区县展开所有
			else{
//				Specification<PointEntity> spec = getCommonSpec(userId, name,groupid, id,enterpriseId,
//						finaldeptids ,pageNo,pageSize,areaId,Admincode,true);
				Specification<PointEntity> spec = getCommonSpec(userId, name,groupid, id,enterpriseId,
						null ,pageNo,pageSize,areaId,Admincode,true);
					// 如果pageNo = -1 表示查询所有结果
					if(-1 != pageNo){
						m = new HashMap<String, Object>();
						PageRequest request = buildPageRequest(pageNo, pageSize,null, Direction.DESC,new String[]{"createTime","updateTime"});
						Page<PointEntity> page=pointDao.findAll(spec, request);
						if(isApp){
							List<AppPointPojo> npis = formatAppResult(page.getContent());
							m.put("totalPages", page.getTotalPages());
							m.put("page", pageNo);
							m.put("totalCount", page.getTotalElements());
							m.put("records", npis);
							m.put("isConverge", false);
						}
						else{
							List<NetPointInfoResult> npis = assembleResult(page.getContent(),dcode,enterpriseId);
							m.put("totalPages", page.getTotalPages());
							m.put("page", pageNo);
							m.put("totalCount", page.getTotalElements());
							m.put("records", npis);
							m=formatCommonPoint(m,userId);
							m.put("isConverge", false);
						}
					}else{
						//不分页
						if(isApp){
							List listMaps = this.pointDao.queryAllPointXYForApp(deptids, Admincode+"%");
							List<AppPointPojo>  records=assembleNativeQueryResultAPP(listMaps);
							m = new HashMap<String, Object>();
							m.put("records", records);
							m.put("isConverge", false);
						}else{
							// 如果不是输入查询，则进行全部查询
							if(org.springframework.util.StringUtils.isEmpty(name)&&org.springframework.util.StringUtils.isEmpty(groupid)){
//								List listMaps = this.pointDao.queryAllPointAndExcByDeptidsAdmincode(deptids,Admincode+"%");
								List listMaps = this.pointDao.queryAllPointAndExcByEidAdmincode(enterpriseId, Admincode+"%");
								List<NetPointInfoResult> npis = assembleNativeQueryResult(listMaps);
								m = new HashMap<String, Object>();
								m.put("records", npis);
								m=formatCommonPoint(m,userId);
								m.put("isConverge", false);
							}else{
								Sort sort = new Sort(Direction.DESC,new String[]{"createTime","updateTime"});
								List<PointEntity> pes = pointDao.findAll(spec,sort);
								if(null != pes && pes.size() > 0){
									m = new HashMap<String, Object>();
									List<NetPointInfoResult> npis = assembleResult(pes,dcode,enterpriseId);
									m.put("records", npis);
									m=formatCommonPoint(m,userId);
									m.put("isConverge", false);
								}
							}
						}
					}
			}
		}
		return m;
	}
	
	
	/**
	 * 点聚集结果返回  
	 * 根据省市区查询的网点数量结果进行 处理
	 * 根据admincode查询出对应的省市区名字 和中心点
	 * @param list
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3下午2:12:42
	 */
	public  List<Map<String,Object>> formatConvergeResult(List list,int level){
		List<Map<String,Object>> maplist=null;
		if(list!=null&&list.size()>0){
			maplist=new ArrayList<Map<String,Object>>();
			Map<String,Object> map=null;
			int nullcount=0;
			for(int i=0,listsize=list.size();i<listsize;i++){
				Object obj[]=(Object[])list.get(i);
				map=new HashMap<String,Object>();
				String admincode=(obj[0]==null||obj[0].equals(""))?"":(String)StringUtil.fromatString((String)obj[0], 6, "0");
				String count=obj[1]==null?"0":obj[1]+"";
				//String tempcode=admincode;
				if(admincode!=null&&!admincode.equals("")){
					/*if(admincode.equals("500000")){//重庆市特殊处理
						tempcode="500100";
					}*/
					//provincename=(String) this.geocodingService.getCountyByAdmincode(tempcode).get("PROVINCE");
					Map<String,Object> geomap=this.geocodingService.getAdminGeoByCode(admincode, level);
					if(geomap!=null){
						double x=(Double) geomap.get("x");
						double y=(Double) geomap.get("y");
						BigDecimal bx=new BigDecimal(x);
						BigDecimal by=new BigDecimal(y);
						x=bx.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						y=by.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						map.put("province", geomap.get("province"));
						map.put("city", geomap.get("city"));
						map.put("county", geomap.get("county"));
						map.put("x", x);
						map.put("y", y);
						map.put("admincode",admincode);
						map.put("count", count);
					}else{
						map.put("province", null);
						map.put("city", null);
						map.put("county", null);
						map.put("x", null);
						map.put("y", null);
						map.put("admincode",admincode);
						map.put("count", count);
					}
					maplist.add(map);
				}else{
					if(count!=null&&!count.equals("")){
						nullcount+=Integer.parseInt(count);
					}
				}
			}
			
			Map<String,Object> nullcountmap=new HashMap<String,Object>();
			nullcountmap.put("province", null);
			nullcountmap.put("city", null);
			nullcountmap.put("county", null);
			nullcountmap.put("x", null);
			nullcountmap.put("y", null);
			nullcountmap.put("admincode",null);
			nullcountmap.put("count", nullcount);
			maplist.add(nullcountmap);
		}
		return maplist;
	}
	
	/**
	 * 通用查询
	 * @param userId
	 * @param name
	 * @param groupid
	 * @param id
	 * @param enterpriseId
	 * @param finaldeptids
	 * @param pageNo
	 * @param pageSize
	 * @param areaId
	 * @param Admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3下午2:41:49
	 */
	public Specification<PointEntity> getCommonSpec(final String userId, final String name,
			final String groupid, final String id, final String enterpriseId,
			final List<String> finaldeptids ,int pageNo, int pageSize, final String areaId,
			final String Admincode ,final boolean queryFail){
		Specification<PointEntity> spec = new Specification<PointEntity>() {
			@Override
			public Predicate toPredicate(Root<PointEntity> root, CriteriaQuery<?> criterialQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				//Path<String> userIdPath = root.get("userId");
				Path<String> enterpriseIdPath = root.get("enterpriseId");
				Path<String> idPath = root.get("id");
				Path<String> namePath = root.get("name");
				Path<String> groupidPath=root.get("groupid").get("id");
				Path<String> areaIdPath = root.get("areaId");
				Path<String> admincodePath = root.get("admincode");
				
				Path<String> deptidPath = root.get("departmentId");//部门id
				
				if(!StringUtils.isNullOrEmpty(id)){
					predicateList.add(criteriaBuilder.equal(idPath, id));
				}
				Predicate namePredicate = null;
				List<Predicate> orPredicates = new ArrayList<Predicate>();
				// 查询名称
				if(!StringUtils.isNullOrEmpty(name)){
					namePredicate = criteriaBuilder.like(namePath, "%" +name.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0));
					orPredicates.add(namePredicate);
				}
				//分组id
				if(!StringUtils.isNullOrEmpty(groupid)){
					if("0".equals(groupid)){
						predicateList.add(criteriaBuilder.isNull(groupidPath));
					}else{
						predicateList.add(criteriaBuilder.equal(groupidPath, groupid));
					}
				}
				
				if(orPredicates.size() > 0){
					Predicate[] predicatess = new Predicate[orPredicates.size()];
					orPredicates.toArray(predicatess);
					predicateList.add(criteriaBuilder.or(predicatess));
				}
				/*if(!StringUtils.isNullOrEmpty(userId)){
					predicateList.add(criteriaBuilder.equal(userIdPath, userId));
				}*/
				
				/**
				 * 所有下级部门ids
				 */
				if(finaldeptids!=null&&finaldeptids.size()>0){
					predicateList.add(deptidPath.in(finaldeptids));
				}
				
				if(!StringUtils.isNullOrEmpty(enterpriseId)){
					predicateList.add(criteriaBuilder.equal(enterpriseIdPath, enterpriseId));
				}
				if(!StringUtils.isNullOrEmpty(areaId)){
					predicateList.add(criteriaBuilder.equal(areaIdPath, areaId));
				}
				
				if(queryFail){
					if(!StringUtils.isNullOrEmpty(Admincode)){
						List<Predicate> orAdmin = new ArrayList<Predicate>(); 
						orAdmin.add(criteriaBuilder.like(admincodePath, Admincode+"%"));
						orAdmin.add(criteriaBuilder.isNull(admincodePath));
						if(orAdmin.size()>0){
							Predicate[] orAdminpredicates = new Predicate[orAdmin.size()];
							orAdmin.toArray(orAdminpredicates);
							predicateList.add(criteriaBuilder.or(orAdminpredicates));
						}
					}
				}else{
					if(!StringUtils.isNullOrEmpty(Admincode)){
						predicateList.add(criteriaBuilder.like(admincodePath, Admincode+"%"));
					}
				}
			
				Predicate[] predicates = new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				criterialQuery.where(predicates);
				return null;
			}
		};
		return spec;
	}
	
	/**
	 * 标准化移动端的网点返回结果
	 * @param pointlist
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3下午3:25:30
	 */
	public List<AppPointPojo> formatAppResult(List<PointEntity> pointlist){
		List<AppPointPojo> apppointlist=null;
		if(pointlist!=null&&pointlist.size()>0){
			apppointlist=new ArrayList<AppPointPojo>();
			for(PointEntity point:pointlist){
				AppPointPojo apppoint=new AppPointPojo();
				apppoint.setId(point.getId());
				apppoint.setName(point.getName());
				apppoint.setSmx(point.getSmx());
				apppoint.setSmy(point.getSmy());
				if(point.getStyleid()!=null){
					apppoint.setStyleid(point.getStyleid().getId());
				}
				if(point.getGroupid()!=null&&point.getGroupid().getStyleid()!=null){
					apppoint.setStyleid(point.getGroupid().getStyleid().getId());
				}
				apppointlist.add(apppoint);
			}
		}
		return apppointlist;
	}
	
	/**
	 * 点不聚集，但有强制聚集规则
	 * 当点数量超过point.WebconvergeNumber时，自动返回聚集结果
	 */
	@Override
	public Map<String, Object> queryAllByAdmincodeForNoConverge(final String userId, final String name,
			final String groupid, final String id, final String enterpriseId,
			final String dcode, int pageNo, int pageSize, final String areaId,
			final String Admincode, boolean isApp) {
		Map<String, Object> m= null;
		
		//如果是APP端的查询单个网点
		if(isApp&&!StringUtils.isNullOrEmpty(id)&&!StringUtils.isNullOrEmpty(enterpriseId)){
			return getOnePointByIDForAPP(id,enterpriseId);
		}
		
		/**
		 * 根据部门code查找部门本级及下级所有用户
		 * 查找所有子部门
		 */
		List<String> deptids=getChildDeptIdsByDeptcode(dcode);
		final List<String> finaldeptids=deptids;
		
		/**
		 * 如果是app的查询，按名称搜索
		 */
		if(isApp&&!StringUtils.isNullOrEmpty(name)){
//			Specification<PointEntity> spec = getCommonSpec(userId, name,groupid, id,enterpriseId,
//					finaldeptids ,pageNo,pageSize,areaId,Admincode,true);
			
			Specification<PointEntity> spec = getCommonSpec(userId, name,groupid, id,enterpriseId,
					null ,pageNo,pageSize,areaId,Admincode,true);
			
			// 如果pageNo = -1 表示查询所有结果
			if(-1 != pageNo){
				m = new HashMap<String, Object>();
				PageRequest request = buildPageRequest(pageNo, pageSize,null, Direction.DESC,new String[]{"createTime","updateTime"});
				Page<PointEntity> page=pointDao.findAll(spec, request);
				List<AppPointPojo> npis = formatAppResult(page.getContent());
				m.put("totalPages", page.getTotalPages());
				m.put("page", pageNo);
				m.put("totalCount", page.getTotalElements());
				m.put("records", npis);
				m.put("isConverge", false);
			}
			return m;
		}
		
		
		if(StringUtils.isNullOrEmpty(Admincode)){//admincode为空，表示全国查询
			m = new HashMap<String, Object>();
//			List list=this.pointDao.getPointCount2PorviceByDeptIds(deptids,1,2);//包括子账号
			List list=this.pointDao.getPointCount2PorviceByEid(enterpriseId,1,2);//企业下所有
			m.put("records", formatConvergeResult(list,1));
			m.put("isConverge", true);
			return m;
		}
		else{
//			Specification<PointEntity> countspec = getCommonSpec(userId, name,groupid, id,enterpriseId,
//					finaldeptids ,pageNo,pageSize,areaId,Admincode,false);
//			
//			Specification<PointEntity> spec = getCommonSpec(userId, name,groupid, id,enterpriseId,
//						finaldeptids ,pageNo,pageSize,areaId,Admincode,true);
			
			Specification<PointEntity> countspec = getCommonSpec(userId, name,groupid, id,enterpriseId,
					null ,pageNo,pageSize,areaId,Admincode,false);
			
			Specification<PointEntity> spec = getCommonSpec(userId, name,groupid, id,enterpriseId,
						null ,pageNo,pageSize,areaId,Admincode,true);
			
			if(Admincode.length()<=4){
				long count=this.pointDao.count(countspec);
				//当查询的数量超过界值时，自动聚集
				if((isApp&&count>=config.getAppconvergeNumber())||(!isApp&&count>=config.getWebconvergeNumber())){
					//省/市聚集
					if(Admincode.length()==3){
						m = new HashMap<String, Object>();
//						List list=this.pointDao.getPointCount2CityByDeptIds(deptids,1,6,Admincode+"%");//包括子账号
						List list=this.pointDao.getPointCount2CityByEid(enterpriseId,1,6,Admincode+"%");//企业下所有
						m.put("records", formatConvergeResult(list,3));
						m.put("isConverge", true);
					}else{
						m = new HashMap<String, Object>();
//						List list=this.pointDao.getPointCount2CityByDeptIds(deptids,1,Admincode.length()+2,Admincode+"%");//包括子账号
						List list=this.pointDao.getPointCount2CityByEid(enterpriseId,1,Admincode.length()+2,Admincode+"%");//企业下所有
						m.put("records", formatConvergeResult(list,(Admincode.length()/2)+1));
						m.put("isConverge", true);
					}
					return m;
				}
			}
			// 如果pageNo = -1 表示查询所有结果
			if(-1 != pageNo){
				m = new HashMap<String, Object>();
				PageRequest request = buildPageRequest(pageNo, pageSize,null, Direction.DESC,new String[]{"createTime","updateTime"});
				Page<PointEntity> page=pointDao.findAll(spec, request);
				if(isApp){
					List<AppPointPojo> npis = formatAppResult(page.getContent());
					m.put("totalPages", page.getTotalPages());
					m.put("page", pageNo);
					m.put("totalCount", page.getTotalElements());
					m.put("records", npis);
					m.put("isConverge", false);
				}
				else{
					List<NetPointInfoResult> npis = assembleResult(page.getContent(),dcode,enterpriseId);
					m.put("totalPages", page.getTotalPages());
					m.put("page", pageNo);
					m.put("totalCount", page.getTotalElements());
					m.put("records", npis);
					m=formatCommonPoint(m,userId);
					m.put("isConverge", false);
				}
			}else{//不分页
				if(isApp){
					List listMaps = this.pointDao.queryAllPointXYForApp(deptids, Admincode+"%");
					List<AppPointPojo>  records=assembleNativeQueryResultAPP(listMaps);
					m = new HashMap<String, Object>();
					m.put("records", records);
					m.put("isConverge", false);
				}
				else{
					// 如果不是输入查询，则进行全部查询
					if(org.springframework.util.StringUtils.isEmpty(name)&&org.springframework.util.StringUtils.isEmpty(groupid)){
//						List listMaps = this.pointDao.queryAllPointAndExcByDeptidsAdmincode(deptids,Admincode+"%");
						List listMaps = this.pointDao.queryAllPointAndExcByEidAdmincode(enterpriseId, Admincode+"%");
						List<NetPointInfoResult> npis = assembleNativeQueryResult(listMaps);
						m = new HashMap<String, Object>();
						m.put("records", npis);
						m=formatCommonPoint(m,userId);
						m.put("isConverge", false);
					}else{
						Sort sort = new Sort(Direction.DESC,new String[]{"createTime","updateTime"});
						List<PointEntity> pes = pointDao.findAll(spec,sort);
						if(null != pes && pes.size() > 0){
							m = new HashMap<String, Object>();
							List<NetPointInfoResult> npis = assembleResult(pes,dcode,enterpriseId);
							m.put("records", npis);
							m=formatCommonPoint(m,userId);
							m.put("isConverge", false);
						}
					}
				}
			}
		}
		return m;
	}
	
	
	/**
	 * 根据部门编码查找下级部门ids
	 * @param dcode
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3下午6:43:16
	 */
	public List<String> getChildDeptIdsByDeptcode(String dcode){
		List<String> deptids=null;
		if(!StringUtils.isNullOrEmpty(dcode)){
			InfoDeptEntity dept=this.infoDeptService.findDeptByCode(dcode);
			if(dept!=null){
				//所有子部门，包含当前部门
				List<InfoDeptEntity> childs=this.infoDeptService.getChildDepts(dept.getId());
				if(childs!=null&&childs.size()>0){
					deptids=new ArrayList<String>();
					for(InfoDeptEntity d:childs){
						deptids.add(d.getId());
					}
				}
			}
		}
		return deptids;
	}
	
	/**
	 * web端返回的网点数据进行一个包装
	 * @param result
	 * @return
	 * @Author Juannyoh
	 * 2016-6-4下午2:40:20
	 */
	public Map<String,Object> formatCommonPoint(Map<String,Object> result,String currentuserid){
		FieldMap fieldmap=new FieldMap();
		//将自定义文件写入
		if(result!=null&&result.get("records")!=null){
			List<NetPointInfoResult> pointlist=(List<NetPointInfoResult>) result.get("records");
			List<Map<String,Object>> maplist=new ArrayList<Map<String,Object>>();
			
			UserEntity user=this.userService.findUserById(currentuserid);
			Map<String,String> userlist=new HashMap<String,String>();
			Map<String,String> deptcodeMap=new HashMap<String,String>();
			if(user!=null){
				userlist=this.userService.findAllUserByEid(user.getEid().getId());
				//获取所有部门ID，部门CODE
				deptcodeMap=getDeptCodesByEid(user.getEid().getId());
			}
			
			if(pointlist!=null&&pointlist.size()>0){
				for(NetPointInfoResult point:pointlist){
					Map<String,Object> pointmap=fieldmap.convertBean(point);
					/**
					 * 20160613 返回子账号用户名
					 */
					pointmap.put("username", getUsernameByPointUserID(point.getUserId(),currentuserid,userlist));
					pointmap.put("dcode", deptcodeMap.get(point.getDepartmentId()));//部门编码
					
					
					if(point.getStyleid()!=null){
						PointStyleEntity style=point.getStyleid();
						Map<String,Object> stylemap=fieldmap.convertBean(style);
						//查询自定义文件
						if(style!=null&&style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
							PointStyleCustomEntity custom=this.pointStyleService.findCustomfileByid(style.getAppcustom());
							if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
								stylemap.put("appcustom", custom.getFilepath());
								stylemap.put("def1", custom.getWidth()+","+custom.getHeight());
								stylemap.put("appcustomid", style.getAppcustom());
							}
						}else{
							stylemap.put("appcustom", "");
							stylemap.put("def1", "");
							stylemap.put("appcustomid", "");
						}
						pointmap.put("styleid", stylemap);
						maplist.add(pointmap);
					}else if(point.getGroupid()!=null&&point.getGroupid().getStyleid()!=null){
						Map<String,Object> groupmap=fieldmap.convertBean(point.getGroupid());
						PointStyleEntity style=point.getGroupid().getStyleid();
						Map<String,Object> stylemap=fieldmap.convertBean(style);
						//查询自定义文件
						if(style!=null&&style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
							PointStyleCustomEntity custom=this.pointStyleService.findCustomfileByid(style.getAppcustom());
							if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
								stylemap.put("appcustom", custom.getFilepath());
								stylemap.put("def1", custom.getWidth()+","+custom.getHeight());
								stylemap.put("appcustomid", style.getAppcustom());
							}
						}else{
							stylemap.put("appcustom", "");
							stylemap.put("def1", "");
							stylemap.put("appcustomid", "");
						}
						groupmap.put("styleid", stylemap);
						pointmap.put("groupid", groupmap);
						maplist.add(pointmap);
					}else {
						maplist.add(pointmap);
						continue;
					}
				}
			}
			result.put("records", maplist);
		}
		return result;
	}
	
	/**
	 * APP端查询网点结果返回 包装
	 * @param queryResult
	 * @return
	 * @Author Juannyoh
	 * 2016-6-6下午5:55:11
	 */
	private List<AppPointPojo> assembleNativeQueryResultAPP(List queryResult){
		List<AppPointPojo> apppointlist=null;
		if(null != queryResult && queryResult.size() > 0){
			apppointlist=new ArrayList<AppPointPojo>();
			for(int i=0,s=queryResult.size();i<s;i++){
				Object[] objs = (Object[]) queryResult.get(i);
				AppPointPojo apppoint=new AppPointPojo();
				apppoint.setId((String) objs[0]);
				apppoint.setName((String) objs[1]);
				apppoint.setSmx(((BigDecimal) objs[2]).setScale(2, BigDecimal.ROUND_HALF_UP));
				apppoint.setSmy(((BigDecimal) objs[3]).setScale(2, BigDecimal.ROUND_HALF_UP));
				apppoint.setStyleid((String) objs[4]);
				apppointlist.add(apppoint);
			}
		}
		return apppointlist;
	}
	
	
	/**
	 * 移动端查询单个网点
	 * @param id
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2016-6-7下午1:25:06
	 */
	public Map<String,Object> getOnePointByIDForAPP(String id,String eid){
		Map<String,Object> result=null;
		if(!StringUtils.isNullOrEmpty(id)){
			result=new HashMap<String,Object>();
			PointEntity pe=this.pointDao.findOneByPointid(id);
			UserEntity topuser=this.userService.findTopUserByEid(eid);
			if(pe!=null&&topuser!=null){
				result.put("id", pe.getId());
				result.put("name", pe.getName());
				result.put("smx", pe.getSmx().setScale(2, BigDecimal.ROUND_HALF_UP));
				result.put("smy", pe.getSmy().setScale(2, BigDecimal.ROUND_HALF_UP));
				result.put("address", pe.getAddress());
				result.put("areaId", pe.getAreaId());
				result.put("createTime", pe.getCreateTime());
				result.put("updateTime", pe.getUpdateTime());
				result.put("status", pe.getStatus());
				result.put("admincode", pe.getAdmincode());
				result.put("province", pe.getProvince());
				result.put("city", pe.getCity());
				result.put("county", pe.getCounty());
				result.put("groupname", pe.getGroupid()==null?null:pe.getGroupid().getGroupname());
				// 查询区域面
				String areaId = pe.getAreaId();
				LOGGER.info("##APP查询网点对应区域 AREA_ID : " + areaId );
				if (!StringUtils.isNullOrEmpty(areaId)) {
					AreaEntity ae = null;
					try {
						ae = areaService.queryByIdOrNumber(areaId, null, null, false);
					} catch (AreaException e) {
						e.printStackTrace();
					}
					if (null != ae) {
						result.put("areaName", ae.getName());
					}else{
						result.put("areaName", null);
					}
				}else{
					result.put("areaName", null);
				}
				
				//自定义字段
				PointExtcolEntity record=this.pointExtcolService.findByUserid(topuser.getId());//根据用户id查找到
				List<PointExtcolValEntity>  vallist =this.pointExtcolValService.findByPointidOrUserid(pe.getId(),null);
				if(record!=null){
					List<Map<String,Object>> colvaluelist=new ArrayList<Map<String,Object>>();
					PointExtcolValEntity npi=new PointExtcolValEntity();
					if(vallist!=null&&vallist.size()>0){
						npi=vallist.get(0);
					}
					// 组装扩展信息
					if(null != record){
						if(!StringUtils.isNullOrEmpty(record.getCol1())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol1());
							map.put("colvalue",npi.getCol1());
							colvaluelist.add(map);
						}
						if(!StringUtils.isNullOrEmpty(record.getCol2())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol2());
							map.put("colvalue",npi.getCol2());
							colvaluelist.add(map);
						}
						if(!StringUtils.isNullOrEmpty(record.getCol3())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol3());
							map.put("colvalue",npi.getCol3());
							colvaluelist.add(map);
						}
						if(!StringUtils.isNullOrEmpty(record.getCol4())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol4());
							map.put("colvalue",npi.getCol4());
							colvaluelist.add(map);
						}
						if(!StringUtils.isNullOrEmpty(record.getCol5())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol5());
							map.put("colvalue",npi.getCol5());
							colvaluelist.add(map);
						}
						if(!StringUtils.isNullOrEmpty(record.getCol6())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol6());
							map.put("colvalue",npi.getCol6());
							colvaluelist.add(map);
						}
						if(!StringUtils.isNullOrEmpty(record.getCol7())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol7());
							map.put("colvalue",npi.getCol7());
							colvaluelist.add(map);
						}
						if(!StringUtils.isNullOrEmpty(record.getCol8())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol8());
							map.put("colvalue",npi.getCol8());
							colvaluelist.add(map);
						}
						if(!StringUtils.isNullOrEmpty(record.getCol9())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol9());
							map.put("colvalue",npi.getCol9());
							colvaluelist.add(map);
						}
						if(!StringUtils.isNullOrEmpty(record.getCol10())){
							Map<String,Object> map=new HashMap<String,Object>();
							map.put("colkey", record.getCol10());
							map.put("colvalue",npi.getCol10());
							colvaluelist.add(map);
						}
						result.put("extcols", colvaluelist);
					}
				}
			}
		}
		return result;
	}
		
	
	/**
	 * 根据网点中用户id查找用户名称
	 * @param pointuserid
	 * @param sessionuser
	 * @return
	 * @Author Juannyoh
	 * 2016-6-13下午1:41:19
	 */
	public String getUsernameByPointUserID(String pointuserid,String currentuserid,Map<String,String> usernamemap){
		String username=null;
		if(usernamemap!=null){
			//查询网点对应的用户名
			if(!StringUtils.isNullOrEmpty(pointuserid)&&!pointuserid.equals(currentuserid)){
				if(usernamemap.containsKey(pointuserid)){
					username=usernamemap.get(pointuserid);
				}
			}
		}
		return username;
	}

	//导出网点
	@Override
	public List<ExportPointBean> queryAllForExport(Map<String, Object> parammap) {
		String eid=StringUtil.convertObjectToString(parammap.get("eid"));
		List<String> childuserid=parammap.get("childuserid")==null?null:(List<String>)parammap.get("childuserid");
		Date btime=parammap.get("btime")==null?null:(Date)parammap.get("btime");
		Date etime=parammap.get("etime")==null?null:(Date)parammap.get("etime");
		List<String> groupids=parammap.get("groupids")==null?null:(List<String>)parammap.get("groupids");
		String status=StringUtil.convertObjectToString(parammap.get("status"));
		String admincode=StringUtil.convertObjectToString(parammap.get("admincode"));
		//目标坐标类型
		String type=StringUtil.convertObjectToString(parammap.get("type"));
		
		List resultlist=this.pointExportDao.queryAllPoint(eid, childuserid, btime, etime, groupids, status, admincode);
		return buildExportResult(resultlist,type);
	}
	
	
	public List<ExportPointBean> buildExportResult(List resultlist,String type){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ExportPointBean> beanlist=null;
		try {
			if(resultlist!=null&&resultlist.size()>0){
				beanlist=new ArrayList<ExportPointBean>();
				for(int i=0,s=resultlist.size();i<s;i++){
					Map map=(Map) resultlist.get(i);
					ExportPointBean bean=new ExportPointBean();
					bean.setAddress(StringUtil.convertObjectToString(map.get("address")));
					String areaid=StringUtil.convertObjectToString(map.get("areaid"));
					if(!StringUtils.isNullOrEmpty(areaid)){
						AreaEntity ae = null;
						try {
							ae = areaService.queryByIdOrNumber(areaid, null, null, false);
						} catch (AreaException e) {
							bean.setAreaName(null);
						}
						if (null != ae) {
							bean.setAreaName(ae.getName());
						}
					}
					bean.setCol1(StringUtil.convertObjectToString(map.get("col1")));
					bean.setCol10(StringUtil.convertObjectToString(map.get("col10")));
					bean.setCol2(StringUtil.convertObjectToString(map.get("col2")));
					bean.setCol3(StringUtil.convertObjectToString(map.get("col3")));
					bean.setCol4(StringUtil.convertObjectToString(map.get("col4")));
					bean.setCol5(StringUtil.convertObjectToString(map.get("col5")));
					bean.setCol6(StringUtil.convertObjectToString(map.get("col6")));
					bean.setCol7(StringUtil.convertObjectToString(map.get("col7")));
					bean.setCol8(StringUtil.convertObjectToString(map.get("col8")));
					bean.setCol9(StringUtil.convertObjectToString(map.get("col9")));
					bean.setCreateTime(map.get("createTime")==null?null:sdf.parse(StringUtil.convertObjectToString(map.get("createTime"))));
					bean.setGroupname(StringUtil.convertObjectToString(map.get("groupname")));
					bean.setId(StringUtil.convertObjectToString(map.get("id")));
					bean.setName(StringUtil.convertObjectToString(map.get("name")));
					if(org.apache.commons.lang3.StringUtils.isNotEmpty(type)){
						if("SMMC".equals(type)){//墨卡托
							bean.setSmx(map.get("smx")==null?null:new BigDecimal(StringUtil.convertObjectToString(map.get("smx"))));
							bean.setSmy(map.get("smy")==null?null:new BigDecimal(StringUtil.convertObjectToString(map.get("smy"))));
						}else if("SMLL".equals(type)){//超图经纬度
							if(map.get("smx")!=null&&map.get("smy")!=null){
								Point p=new Point(StringUtil.convertObjectToDouble(map.get("smx")),StringUtil.convertObjectToDouble(map.get("smy")));
								p=convertPoint(p,"SMMC","SMLL");
								bean.setSmx(new BigDecimal(p.getLon()));
								bean.setSmy(new BigDecimal(p.getLat()));
							}
						}else if("BDLL".equals(type)){//百度经纬度
							if(map.get("smx")!=null&&map.get("smy")!=null){
								Point p=new Point(StringUtil.convertObjectToDouble(map.get("smx")),StringUtil.convertObjectToDouble(map.get("smy")));
								p=convertPoint(p,"SMMC","BDLL");
								bean.setSmx(new BigDecimal(p.getLon()));
								bean.setSmy(new BigDecimal(p.getLat()));
							}
						}else{
							bean.setSmx(map.get("smx")==null?null:new BigDecimal(StringUtil.convertObjectToString(map.get("smx"))));
							bean.setSmy(map.get("smy")==null?null:new BigDecimal(StringUtil.convertObjectToString(map.get("smy"))));
						}
					}else{
						bean.setSmx(map.get("smx")==null?null:new BigDecimal(StringUtil.convertObjectToString(map.get("smx"))));
						bean.setSmy(map.get("smy")==null?null:new BigDecimal(StringUtil.convertObjectToString(map.get("smy"))));
					}
					bean.setStatus(getValueOfPointStatus(StringUtil.convertObjectToString(map.get("status"))));
					bean.setUpdateTime(map.get("updateTime")==null?null:sdf.parse(StringUtil.convertObjectToString(map.get("updateTime"))));
					bean.setUsername(StringUtil.convertObjectToString(map.get("username")));
					beanlist.add(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return beanlist;
	}
	
	/**
	 * 坐标转换
	 * @param p
	 * @param from
	 * @param to
	 * @return
	 * @Author Juannyoh
	 * 2016-9-30下午3:22:13
	 */
	public Point convertPoint(Point p,String from,String to){
		if(null!=p){
			if("SMMC".equals(from)){
				if("SMLL".equals(to)){//超图墨卡托转经纬度
					p=SuperMapCoordinateConvertImpl.smMCToLatLon(p);
				}else if("BDLL".equals(to)){//超图墨卡托转百度经纬度
					p=bdconver.reverseConvert(p);
				}
			}else if("SMLL".equals(from)){
				if("SMMC".equals(to)){//超图经纬度转墨卡托
					p=SuperMapCoordinateConvertImpl.smLL2MC(p);
				}else if("BDLL".equals("to")){
					p=bdconver.reverseConvert(p);
				}
			}
		}
		return p;
	}
	
	public String getValueOfPointStatus(String status){
		if(!StringUtils.isNullOrEmpty(status)){
			if(status.equals("0")){
				return "定位成功";
			}else if(status.equals("1")){
				return "定位失败";
			}else if(status.equals("2")){
				return "地址解析中";
			}else return "其他";
		}else return "其他";
	}

	@Override
	public int deletePoint(Map<String, Object> parammap) {
		String eid=StringUtil.convertObjectToString(parammap.get("eid"));
		List<String> childuserid=parammap.get("childuserid")==null?null:(List<String>)parammap.get("childuserid");
		Date btime=parammap.get("btime")==null?null:(Date)parammap.get("btime");
		Date etime=parammap.get("etime")==null?null:(Date)parammap.get("etime");
		List<String> groupids=parammap.get("groupids")==null?null:(List<String>)parammap.get("groupids");
		String status=StringUtil.convertObjectToString(parammap.get("status"));
		String admincode=StringUtil.convertObjectToString(parammap.get("admincode"));
		return this.pointExportDao.deletePoint(eid, childuserid, btime, etime, groupids, status, admincode);
	}

	@Override
	public int querydeletePointCount(Map<String, Object> parammap) {
		String eid=StringUtil.convertObjectToString(parammap.get("eid"));
		List<String> childuserid=parammap.get("childuserid")==null?null:(List<String>)parammap.get("childuserid");
		Date btime=parammap.get("btime")==null?null:(Date)parammap.get("btime");
		Date etime=parammap.get("etime")==null?null:(Date)parammap.get("etime");
		List<String> groupids=parammap.get("groupids")==null?null:(List<String>)parammap.get("groupids");
		String status=StringUtil.convertObjectToString(parammap.get("status"));
		String admincode=StringUtil.convertObjectToString(parammap.get("admincode"));
		return this.pointExportDao.querydeletePointCount(eid, childuserid, btime, etime, groupids, status, admincode);
	}

	@Override
	public boolean isExsitCpoint(String cid, String userid) {
		boolean flag=false;
		List list=this.pointDao.findCpointByCidAndUserid(cid, userid);
		if(null!=list&&list.size()>0){
			flag=true;
		}
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ExportPointBean> queryPointForPathPlan(String dcode, String eid,
			String groupname,String pointname, int pageNo, int pageSize ,String areaid){
		List<String> deptids=getChildDeptIdsByDeptcode(dcode);
		List<ExportPointBean> list=this.pointExportDao.findPointByGroupname(deptids, eid, groupname, pointname, areaid, pageNo, pageSize);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExportPointBean> queryPointByColKeyForPathPlan(String dcode,
			String colkey, String colvalue, String eid, int pageNo, int pageSize) {
		List<String> deptids=getChildDeptIdsByDeptcode(dcode);
		List<ExportPointBean> list=this.pointExportDao.findPointByExtCols(colkey, colvalue, deptids, eid, pageNo, pageSize);
		return list;
	}
	
	
	/**
	 * 查找企业下的所有部门编码（部门ID，部门CODE）
	 * @param eid企业ID
	 * @return
	 */
	public Map<String,String> getDeptCodesByEid(String eid){
		Map<String,String> deptcodemap=null;
		UserEntity topuser=this.userService.findTopUserByEid(eid);
		if(null!=topuser){
			 List<InfoDeptEntity> deptlist=this.infoDeptService.getChildDepts(topuser.getDeptId().getId());
			 if(null!=deptlist&&deptlist.size()>0){
				 deptcodemap=new HashMap<String,String>();
				 for(InfoDeptEntity dept:deptlist){
					 deptcodemap.put(dept.getId(), dept.getCode());
				 }
			 }
		}
		return deptcodemap;
	}
	
}


