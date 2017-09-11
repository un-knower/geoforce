package com.supermap.egispservice.base.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egisp.addressmatch.beans.AddressMatchResult;
import com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.Point2D;
import com.supermap.egispservice.area.exceptions.AreaException;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.callables.AddressSplitCallable;
import com.supermap.egispservice.base.callables.AreaQueryCallable;
import com.supermap.egispservice.base.constants.AddressMatchConstants;
import com.supermap.egispservice.base.constants.ConfigConstants;
import com.supermap.egispservice.base.constants.StatusConstants;
import com.supermap.egispservice.base.dao.IAddressDao;
import com.supermap.egispservice.base.dao.ILogisticsOrderBaseDao;
import com.supermap.egispservice.base.dao.ILogisticsOrderFendanDao;
import com.supermap.egispservice.base.dao.InfoDeptDao;
import com.supermap.egispservice.base.entity.AddressEntity;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.OrderBaseEntity;
import com.supermap.egispservice.base.entity.OrderFendanEntity;
import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.pojo.DistributeAddress;
import com.supermap.egispservice.base.pojo.LogisticsAPIResult;
import com.supermap.egispservice.base.pojo.LogisticsResultInfo;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.entity.Point;

@Service("logisticsService")
public class LogisticsServiceImpl implements ILogisticsService {

	private static Logger LOGGER = Logger.getLogger(LogisticsServiceImpl.class);
	
	@Autowired
	private ILogisticsOrderBaseDao baseDao;
	
	
	@Autowired
	private ILogisticsOrderFendanDao fendanDao;
	
	@Autowired
	private IAddressMatchService addressMatch;
	
	@Autowired
	private InfoDeptDao deptDao;
	
	@Autowired
	private ILogisticsOrderService orderService;
	
	@Autowired
	private PointService pointService;
	
	@Autowired
	private IAreaService areaService;
	
	@Autowired
	private IAddressDao addressDao;
	
	@Autowired
	private IGeocodingService geocodingService;
	
	@Autowired
	private ICorrectAddressService correctAddressService;
	
	@Autowired
	private ConfigConstants configConstants;
	
	
	@Override
	@Transactional
	public LogisticsResultInfo logistic(String orderBaseId,String userId,String enterpriseId,String departmentId) {
		LogisticsResultInfo result = null;
		OrderBaseEntity obe = baseDao.findOne(orderBaseId);
		if(null != obe){
			String id = obe.getId();
			String address = obe.getAddress();
			result = logistics(id, address, userId, enterpriseId, departmentId);
		}
		return result;
	}

	@Override
	public void logistic(List<String> orderBaseIds,String userId,String enterpriseId,String departmentId) {
		Iterable<OrderBaseEntity> iterator = baseDao.findAll(orderBaseIds);
		// 启动线程异步处理
		Thread thread = new Thread(new DistributeApp(iterator,enterpriseId,departmentId,userId));
		thread.start();
	}

	@Override
	@Transactional
	public LogisticsResultInfo logistics(String id, String address,String userId,String enterpriseId,String departmentId) {
		com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult amr = (DefaultAddressMatchResult) this.addressMatch.addressMatch(id, address,AddressMatchConstants.SMC);
		OrderBaseEntity obe = baseDao.findOne(id);
		if(null == obe){
			throw new NullPointerException("未找到[id:"+id+"]的数据");
		}
		obe.setProvince(obe.getProvince());
		obe.setCity(amr.getCity());
		obe.setCounty(obe.getCounty());
		obe.setOrderStatusId(Byte.valueOf("2"));
		obe.setAdmincode(obe.getAdmincode());
		obe.setStatusUpdateTime(new Date());
		List<OrderBaseEntity> list = new ArrayList<OrderBaseEntity>();
		list.add(obe);
		this.orderService.update(list);
		
		AreaEntity ae = null;
		// 点查面
		try {
			ae = this.areaService.queryAreaByPoint(new Point2D(amr.getX(),amr.getY()), enterpriseId, null);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		// 保存分单结果
		saveFendanResult(ae, amr,departmentId);
		return buildResult(ae, amr, enterpriseId, departmentId);
	}
	
	private LogisticsResultInfo buildResult(AreaEntity ae,com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult amr,String enterpriseId,String departmentId){
		LogisticsResultInfo lri = new LogisticsResultInfo();
		lri.setAddress(null);
		lri.setAdmincode(amr.getAdmincode());
		if(null != ae){
			lri.setAreaId(lri.getAreaId());
			lri.setAreaName(ae.getName());
			lri.setAreaNum(lri.getAreaNum());
			String areaId = lri.getAreaId();
			if(!StringUtils.isEmpty(areaId)){
				List<PointEntity> pe = this.pointService.queryByAreaId(areaId, enterpriseId, departmentId);
				if(null != pe && pe.size() > 0){
					PointEntity pEntity = pe.get(0);
					lri.setNetId(pEntity.getId());
					lri.setNetName(pEntity.getName());
					lri.setDutyName(pEntity.getDutyName());
					lri.setDutyPhone(pEntity.getDutyPhone());
				}
			}
			lri.setSmx(amr.getX());
			lri.setSmy(amr.getY());
		}
		OrderBaseEntity obe = this.baseDao.findOne(amr.getId());
		lri.setCity(amr.getCity());
		lri.setCounty(amr.getCounty());
		lri.setOrderNum(obe.getNumber());
		return lri;
	}
	
	
	@Transactional
	private void saveFendanResult(AreaEntity ae,com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult amr,String departmentId){
		OrderFendanEntity fendanEntity = new OrderFendanEntity();
		fendanEntity.setId(amr.getId());
		if(null != ae){
			if (amr.getResultType() != AddressMatchConstants.RESULT_TYPE_SUCCESS
					|| (StringUtils.isEmpty(ae.getAreaNumber()) && StringUtils.isEmpty(ae.getName()))) {
				fendanEntity.setFendanStatusId(Byte.valueOf("4"));
			}else{
				fendanEntity.setFendanStatusId(Byte.valueOf("3"));
			}
		}else{
			fendanEntity.setFendanStatusId(Byte.valueOf("4"));
		}
		fendanEntity.setAreaId(amr.getId());
		fendanEntity.setFendanTime(new Date());
		fendanEntity.setSmx(BigDecimal.valueOf(amr.getX()));
		fendanEntity.setSmy(BigDecimal.valueOf(amr.getY()));
		fendanEntity.setDepartment_id(departmentId);
		this.fendanDao.save(fendanEntity);
	}
	
	/**
	 * 
	 * <p>Title: DistributeApp</p>
	 * Description:		分单线程
	 *
	 * @author Huasong Huang
	 * CreateTime: 2014-10-11 上午09:43:40
	 */
	private class DistributeApp implements Runnable{

		private Iterable<OrderBaseEntity> iterable;
		private String enterpriseId;
		private String departmentId;
		private String userId;
		
		@SuppressWarnings("unused")
		public DistributeApp(){}
		
		public DistributeApp(Iterable<OrderBaseEntity> iterable,String enterpriseId,String departmentId,String userid){
			this.iterable = iterable;
			this.enterpriseId = enterpriseId;
			this.departmentId = departmentId;
			this.userId=userid;
		}
		
		@Override
		@Transactional
		public void run() {
			try{
				//构建地址解析参数
				List<DistributeAddress> apiaddressInfos = buildAPIAddressInfo(iterable);
				List<LogisticsAPIResult> resultlist=logisticsAddrByMultithread(apiaddressInfos,"SMC",userId,enterpriseId,departmentId);
				
				// 更新地址信息
				List<OrderBaseEntity> entitys = buildOrderEntityByAPIResult(resultlist);
				orderService.update(entitys);
				
				// 保存分单结果
				List<OrderFendanEntity> fendanEntitys = buildFendEntitysByAPIResult(resultlist,departmentId);
				fendanDao.save(fendanEntitys);
				
				LOGGER.debug("## 更新分单状态成功");
			}catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		/**
		 * 
		 * <p>Title ：buildOrderEntity</p>
		 * Description：		构建地址结果
		 * @param addressMatchResults
		 * @return
		 * Author：Huasong Huang
		 * CreateTime：2014-9-24 下午04:01:23
		 */
		@SuppressWarnings({ "unused" })
		private List<OrderBaseEntity> buildOrderEntity(List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> addressMatchResults){
			List<OrderBaseEntity> entitys = new ArrayList<OrderBaseEntity>();
			Iterator<AddressMatchResult> iterator = addressMatchResults.iterator();
			while(iterator.hasNext()){
				com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult results = (com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult) iterator.next();
				OrderBaseEntity obe = new OrderBaseEntity();
				obe.setId(results.getId());
				
				/**
				 * 根据坐标查询省市区
				 */
				double smx=results.getX();
				double smy=results.getY();
				Map<String,String> m=geocodingService.searchAdmincodeForCounty(smx,smy);
				if(m!=null){
					obe.setProvince(m.get("PROVINCE"));
					obe.setCity(m.get("CITY2"));
					obe.setCounty(m.get("COUNTY"));
					obe.setAdmincode(m.get("ADMINCODE"));
				}
				obe.setOrderStatusId(Byte.valueOf("2"));
				obe.setStatusUpdateTime(new Date());
				entitys.add(obe);
			}
			return entitys;
		}
		
		/**
		 * 
		 * <p>Title ：buildFendEntitys</p>
		 * Description：	 构建分单结果
		 * @param areaEntitys
		 * @param addressMatchResults
		 * @return
		 * Author：Huasong Huang
		 * CreateTime：2014-9-24 上午09:41:20
		 */
		@SuppressWarnings("unused")
		private List<OrderFendanEntity> buildFendEntitys(List<AreaEntity> areaEntitys,List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> addressMatchResults){
			List<OrderFendanEntity> fendanEntitys = new ArrayList<OrderFendanEntity>();
			for(int i=0;i<areaEntitys.size();i++){
				OrderFendanEntity fendanEntity = new OrderFendanEntity();
				AreaEntity ae = areaEntitys.get(i);
				com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult amr = (DefaultAddressMatchResult) addressMatchResults.get(i);
				fendanEntity.setId(amr.getId());
				if (amr.getResultType() != AddressMatchConstants.RESULT_TYPE_SUCCESS
						|| (StringUtils.isEmpty(ae.getAreaNumber()) && StringUtils.isEmpty(ae.getName()))) {
					fendanEntity.setFendanStatusId(Byte.valueOf("2"));
				}else{
					fendanEntity.setFendanStatusId(Byte.valueOf("1"));
				}
				fendanEntity.setFendanTime(new Date());
				fendanEntity.setAreaId(ae.getId());
				fendanEntity.setSmx(BigDecimal.valueOf(amr.getX()));
				fendanEntity.setSmy(BigDecimal.valueOf(amr.getY()));
				fendanEntity.setDepartment_id(this.departmentId);
				fendanEntitys.add(fendanEntity);
			}
			return fendanEntitys;
		}
		
		
		
		
		/**
		 * 
		 * <p>Title ：buildAddressInfo</p>
		 * Description：		构建地址匹配参数
		 * @return
		 * Author：Huasong Huang
		 * CreateTime：2014-9-23 下午05:04:55
		 */
		@SuppressWarnings("unused")
		private List<com.supermap.egisp.addressmatch.beans.AddressMatchParam> buildAddressInfo(){
			List<com.supermap.egisp.addressmatch.beans.AddressMatchParam> addressInfo = new ArrayList<com.supermap.egisp.addressmatch.beans.AddressMatchParam>();
			Iterator<OrderBaseEntity> iterator  = this.iterable.iterator();
			while(iterator.hasNext()){
				OrderBaseEntity obe = (OrderBaseEntity) iterator.next();
				com.supermap.egisp.addressmatch.beans.AddressMatchParam aid = new com.supermap.egisp.addressmatch.beans.AddressMatchParam();
				aid.setAddress(obe.getAddress());
				aid.setId(obe.getId());
				addressInfo.add(aid);
			}
			return addressInfo;
		}
		
	}

	@Override
	@Transactional
	public void logistics(String orderId, double smx, double smy) {
		OrderFendanEntity ofe = this.fendanDao.findOne(orderId);
		OrderBaseEntity obe = this.baseDao.findOne(orderId);
		String enterpriseId = obe.getEnterpriseId();
		if( null!= obe){
			if(null==ofe){
				ofe=new OrderFendanEntity();
				ofe.setId(obe.getId());
				ofe.setDepartment_id(obe.getDepartmentId());
				ofe.setFendanTime(new Date());
				ofe.setUserId(obe.getUserId());
			}
			AreaEntity ae = null;
			try {
				ae = this.areaService.queryAreaByPoint(new Point2D(smx,smy), enterpriseId, null);
			} catch (AreaException e) {
				LOGGER.error(e.getMessage(), e);
			}
			ofe.setSmx(BigDecimal.valueOf(smx));
			ofe.setSmy(BigDecimal.valueOf(smy));
			
			//10.28根据坐标查找省市区
			Map<String,String> map=this.geocodingService.searchAdmincodeForCounty(smx, smy);
			if(map!=null){
				obe.setProvince(map.get("PROVINCE"));
				obe.setCity(map.get("CITY2"));
				obe.setCounty(map.get("COUNTY"));
				obe.setAdmincode(map.get("ADMINCODE"));
			}
			//LOGGER.info("**********"+map.toString()); 
			//
			if(null != ae && !StringUtils.isEmpty(ae.getId())){
				ofe.setAreaId(ae.getId());
				ofe.setFendanStatusId(Byte.valueOf("3"));
				obe.setOrderStatusId(Byte.valueOf("5"));
			}else{
				ofe.setFendanStatusId(Byte.valueOf("4"));
				obe.setOrderStatusId(Byte.valueOf("10"));
			}
			obe.setStatusUpdateTime(new Date());
			this.fendanDao.save(ofe);
			this.baseDao.save(obe);
		}
	}

	/**
	 * needAreaInfo 是否需要返回区划信息
	 */
	@Override
	public List<LogisticsAPIResult> logisticsAPI(List<DistributeAddress> addresses, String coorType,String userId, String enterpriseId,
			String departmentId) {
		return logisticsAPI_Area(addresses,coorType,userId,enterpriseId,departmentId,false);
	}
	
	
	/**
	 * 
	 * <p>Title ：queryAreaByMultiThreads</p>
	 * Description：		通过多线程查询区域面
	 * @param point2Ds
	 * @param enterpriseId
	 * @param dcode
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-27 上午09:52:25
	 */
	private List<AreaEntity> queryAreaByMultiThreads(Point2D[] point2Ds, String enterpriseId, String dcode,String userId) {
		int length = point2Ds.length;
		int pageSize = length;
		List<AreaQueryCallable> lstCallable = new ArrayList<AreaQueryCallable>();
		if (length > AddressMatchConstants.MULTI_THREAD_AREA_THRESHOD) {
			pageSize = length / AddressMatchConstants.MULTI_THREAD_AREA_COUNT;
			for (int i = 0; i < AddressMatchConstants.MULTI_THREAD_AREA_COUNT; i++) {
				int start = i * pageSize;
				int end = pageSize * i + pageSize;;
				if(end >= length){
					end = length;
				}
				Point2D[] subPoints = Arrays.copyOfRange(point2Ds, start, end);
				AreaQueryCallable callable = new AreaQueryCallable(enterpriseId,dcode,subPoints,userId+"_"+i);
				callable.setAreaService(this.areaService);
				lstCallable.add(callable);
			}
		}else{
			AreaQueryCallable callable = new AreaQueryCallable(enterpriseId,dcode,point2Ds,userId+"_"+0);
			callable.setAreaService(this.areaService);
			lstCallable.add(callable);
		}
		
		List<java.util.concurrent.Future<List<AreaEntity>>> lstFutures = null;
		List<AreaEntity> results = new LinkedList<AreaEntity>();
		ExecutorService exec = Executors.newFixedThreadPool(lstCallable.size());
		try {
			lstFutures = exec.invokeAll(lstCallable);
			for (int i = 0; i < lstCallable.size(); i++) {
				Future<List<AreaEntity>> future = (Future<List<AreaEntity>>) lstFutures.get(i);
				List<AreaEntity> result = future.get();
				results.addAll(result);
			}
		} catch (Exception e1) {
			throw new NullPointerException(e1.getMessage());
		}finally{
			if(null != exec){
				exec.shutdown();
			}
		}
		return results;
	}
	
	private List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> buildTimeoutResult(
			List<DistributeAddress> list, String string) {

		List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> result = new ArrayList<com.supermap.egisp.addressmatch.beans.AddressMatchResult>();
		for (int i = 0; i < list.size(); i++) {
			DefaultAddressMatchResult dmar = new DefaultAddressMatchResult();
			dmar.setId(list.get(i).getId());
			result.add(dmar);
		}
		return result;
	}

	

	/**
	 * 
	 * <p>Title ：splitAddresses</p>
	 * Description：		拆分地址
	 * @param addresses
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-21 下午02:08:32
	 */
	private List<List<DistributeAddress>> splitAddresses(List<DistributeAddress> addresses){
		int paramSize = addresses.size();
		if(paramSize > AddressMatchConstants.LOGISTICS_BATCH_MAX_SIZE){
			throw new NullPointerException("地址数量["+paramSize+"]超过["+AddressMatchConstants.LOGISTICS_BATCH_MAX_SIZE+"]");
		}
		int DEFAULT_PROGRESS_COUNT = AddressMatchConstants.MULTI_THREAD_COUNT;
		int progressCount = paramSize <= DEFAULT_PROGRESS_COUNT ? 1 : DEFAULT_PROGRESS_COUNT;
		int num = paramSize / DEFAULT_PROGRESS_COUNT;
		List<List<DistributeAddress>> params = new ArrayList<List<DistributeAddress>>();
		for (int i = 0; i < progressCount; i++) {
			List<DistributeAddress> paramList = null;
			if (i < progressCount - 1) {
				paramList = addresses.subList(i * num, (i + 1) * num);
			} else {
				paramList = addresses.subList(i * num, addresses.size());
			}
			params.add(paramList);
		}
		return params;
	}
	
	
	/**
	 * 
	 * <p>Title ：buildAPIAddressMatchInfo</p>
	 * Description：		构建API地址解析
	 * @param addresses
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-21 上午09:48:13
	 */
	private List<com.supermap.egisp.addressmatch.beans.AddressMatchParam> buildAPIAddressMatchInfo(List<DistributeAddress> addresses){
		List<com.supermap.egisp.addressmatch.beans.AddressMatchParam> details = new ArrayList<com.supermap.egisp.addressmatch.beans.AddressMatchParam>();
		for(DistributeAddress address : addresses){
			com.supermap.egisp.addressmatch.beans.AddressMatchParam addressInfoItem = new com.supermap.egisp.addressmatch.beans.AddressMatchParam();
			addressInfoItem.setAddress(address.getAddress());
			addressInfoItem.setId(address.getId());
			details.add(addressInfoItem);
		}
		return details;
	}
	
	/**
	 * 
	 * <p>Title ：buildDistributePoint</p>
	 * Description：		构建点查面参数
	 * @param addressMatchResults
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-23 下午05:26:21
	 */
	private Point2D[] buildDistributePoint(List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> addressMatchResults){
		Point2D[] point2ds = new Point2D[addressMatchResults.size()];
		for(int i = 0;i<addressMatchResults.size();i++){
			point2ds[i] = new Point2D(addressMatchResults.get(i).getX(),addressMatchResults.get(i).getY());
		}
		return point2ds;
	}

	
	/**
	 * 
	 * @param amrs
	 * @param areaEntitys
	 * @param coorType
	 * @param addresses
	 * @param eid
	 * @param dcode
	 * @param userId
	 * @param isUseCorrectFlag
	 * @param needAreaInfo 是否需要返回区划状态与区划属性信息
	 * @return
	 */
	private List<LogisticsAPIResult> buildAPIResult(List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> amrs,List<AreaEntity> areaEntitys,String coorType,List<DistributeAddress> addresses
			,String eid,String dcode,String userId,boolean isUseCorrectFlag,boolean needAreaInfo){
		List<LogisticsAPIResult> lars = new ArrayList<LogisticsAPIResult>();
		boolean isAreaSuccess = true;
		if (null == areaEntitys || areaEntitys.size() <= 0) {
			isAreaSuccess = false;
		}
		for (int i = 0; i < amrs.size(); i++) {
			LogisticsAPIResult lar = new LogisticsAPIResult();
			com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult amr = (DefaultAddressMatchResult) amrs.get(i);
			lar.setId(amr.getId());
			int resultType = amr.getResultType();
			if (resultType == AddressMatchConstants.RESULT_TYPE_SUCCESS && isAreaSuccess) {
				AreaEntity areaEntity = areaEntitys.get(i);
				String areaNumber = areaEntity.getAreaNumber();
				String areaName = areaEntity.getName();
				if (StringUtils.isEmpty(areaName) && StringUtils.isEmpty(areaNumber)) {
					lar.setResultType(AddressMatchConstants.RESULT_TYPE_NOT_AREA + "");
				} else {
					lar.setAreaName(areaName);
					lar.setAreaNumber(areaNumber);
					lar.setResultType(AddressMatchConstants.RESULT_TYPE_SUCCESS+"");
					lar.setStatus(areaEntity.getArea_status());
					//判断是否需要返回区划状态及关联区划
					if(needAreaInfo){
						lar.setRelationAreaname(areaEntity.getRelation_areaname());
						lar.setRelationAreanum(areaEntity.getRelation_areanumber());
//						if(areaEntity.getArea_status()==1){//如果是停用状态，则去查询关联的区划
//							String relationid=areaEntity.getRelation_areaid();
//							if(StringUtils.isNotEmpty(relationid)){
//								List<Map<String,String>> relationlist=this.areaService.findRelationAreaAttrs(areaEntity.getId(),relationid);
//								if(relationlist!=null&&relationlist.size()>0){
//									lar.setRelationAreaname(relationlist.get(relationlist.size()-1).get("NAME"));
//									lar.setRelationAreanum(relationlist.get(relationlist.size()-1).get("AREA_NUM"));
//								}
//							}
//						}
					}
				}
				Point p = new Point(amr.getX(),amr.getY());
				// 如果需要返回的坐标类型为经纬度，则进行坐标转换。
				if(coorType != null && coorType.equalsIgnoreCase(AddressMatchConstants.SMLL)){
					p = SuperMapCoordinateConvertImpl.smMCToLatLon(p);
				}
				lar.setX(p.getLon());
				lar.setY(p.getLat());
			} else {
				lar.setResultType(resultType + "");
				//如果需要写到纠错库，将解析失败的地址写入到纠错库(没坐标)
				if(isUseCorrectFlag&&resultType==AddressMatchConstants.RESULT_TYPE_NOT_COOR){
					this.correctAddressService.addCorrectAddress(addresses.get(i).getAddress(), "1", userId, eid, dcode);
				}
			}
			lars.add(lar);
		}
		return lars;
	}

	@Override
	@Transactional
	public AddressEntity addrssMatchAndSave(String address, String userId, String eid, String dcode) {
		DefaultAddressMatchResult amr = (DefaultAddressMatchResult) addressMatch.addressMatch("", address, AddressMatchConstants.SMC);
		AddressEntity ae = new AddressEntity();
		ae.setAddress(address);
		ae.setProvince(amr.getProvince());
		ae.setCity(amr.getCity());
		ae.setCounty(amr.getCounty());
		ae.setAddTime(new Date());
		try {
			AreaEntity areaEntity = this.areaService.queryAreaByPoint(new com.supermap.egispservice.area.Point2D(amr.getX(),amr.getY()),eid, null);
			if(null != areaEntity && !StringUtils.isEmpty(areaEntity.getId())){
				ae.setAreaId(areaEntity.getId());
				ae.setAreaName(areaEntity.getName());
				ae.setAreaNum(areaEntity.getAreaNumber());
			}
		} catch (AreaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ae.setSmx(amr.getX());
		ae.setSmy(amr.getY());
		ae.setDcode(dcode);
		ae.setEid(eid);
		ae.setUserId(userId);
		if(amr.getX() > 0 && amr.getY() > 0){
			ae.setStatus(StatusConstants.ADDRESS_SUCCESS);
		}
		ae = this.addressDao.save(ae);
		return ae;
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> queryAddressList(final String keyword, int pageNo, int pageSize, final String userId) {
		PageRequest pageRequest = new PageRequest(pageNo - 1,pageSize,new Sort(Direction.DESC,"addTime"));
		Specification<AddressEntity> spec = new Specification<AddressEntity>(){

			@Override
			public Predicate toPredicate(Root<AddressEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				
				List<Predicate> predicateList=new ArrayList<Predicate>();
				Path<String> userPath = root.get("userId");
				predicateList.add(builder.equal(userPath, userId));
				
				if(!StringUtils.isEmpty(keyword)){
					Path<String> addrPath = root.get("address");
					predicateList.add(builder.like(addrPath, "%" + keyword.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0)));
				}
				Predicate p[] = new Predicate[predicateList.size()];
				predicateList.toArray(p);
				query.where(p);
				return null;
			}
			
		};
		Page<AddressEntity> pages = this.addressDao.findAll(spec, pageRequest);
		Map<String,Object> rmap = new HashMap<String,Object>();
		rmap.put("totalPages", pages.getTotalPages());
		rmap.put("page", pageNo);
		rmap.put("total", pages.getTotalElements());
		rmap.put("records", pages.getContent());
		return rmap;
	}
	
	
	/**
	 * 多线程分单---订单导入情况
	 * @param addresses
	 * @param coorType
	 * @param userId
	 * @param enterpriseId
	 * @param departmentId
	 * @return
	 * @Author Juannyoh
	 * 2016-11-25下午4:24:40
	 */
	public List<LogisticsAPIResult> logisticsAddrByMultithread(List<DistributeAddress> addresses, String coorType,String userId, String enterpriseId,
			String departmentId) {
		
		// 判断是否需要进行地址拆分
		List<List<DistributeAddress>> params = splitAddressesImport(addresses);

		// 创建线程
		ExecutorService exec = Executors.newFixedThreadPool(params.size());
		List<AddressSplitCallable> lstCallables = new ArrayList<AddressSplitCallable>();
		for(int i=0;i<params.size();i++){
			List<com.supermap.egisp.addressmatch.beans.AddressMatchParam> addressInfos = buildAPIAddressMatchInfo(params.get(i));
			AddressSplitCallable callable = null;
			callable = new AddressSplitCallable(addressInfos,userId+"_"+i,this.addressMatch,null,null);
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
					result = buildTimeoutResult(params.get(i),"分单发生超时");
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
		// 更新地址信息
		Point2D[] point2Ds = buildDistributePoint(results);
		// 点查面
		LOGGER.debug("## 开始点查面。。");
		InfoDeptEntity deptEntity = deptDao.findById(departmentId);
		if(null == deptEntity){
			throw new NullPointerException("未找到部门信息");
		}
		String dcode = deptEntity.getCode();
		// 通过多线程方式查询区域面
		List<AreaEntity> areaEntitys = queryAreaByMultiThreads4Import(point2Ds, enterpriseId, dcode, userId);
		
		return buildAddressResult(results, areaEntitys, coorType);
	}
	
	private List<LogisticsAPIResult> buildAddressResult(List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> amrs,List<AreaEntity> areaEntitys,String coorType){
		List<LogisticsAPIResult> lars = new ArrayList<LogisticsAPIResult>();
		boolean isAreaSuccess = true;
		if (null == areaEntitys || areaEntitys.size() <= 0) {
			isAreaSuccess = false;
		}
		for (int i = 0; i < amrs.size(); i++) {
			LogisticsAPIResult lar = new LogisticsAPIResult();
			com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult amr = (DefaultAddressMatchResult) amrs.get(i);
			lar.setId(amr.getId());
			int resultType = amr.getResultType();
			if (resultType == AddressMatchConstants.RESULT_TYPE_SUCCESS && isAreaSuccess) {
				AreaEntity areaEntity = areaEntitys.get(i);
				String areaNumber = areaEntity.getAreaNumber();
				String areaName = areaEntity.getName();
				if (StringUtils.isEmpty(areaName) && StringUtils.isEmpty(areaNumber)) {
					lar.setResultType(AddressMatchConstants.RESULT_TYPE_NOT_AREA + "");
				} else {
					lar.setAreaName(areaName);
					lar.setAreaNumber(areaEntity.getId());//临时传areaid字段
					lar.setResultType(AddressMatchConstants.RESULT_TYPE_SUCCESS+"");
				}
				Point p = new Point(amr.getX(),amr.getY());
				// 如果需要返回的坐标类型为经纬度，则进行坐标转换。
				if(coorType != null && coorType.equalsIgnoreCase(AddressMatchConstants.SMLL)){
					p = SuperMapCoordinateConvertImpl.smMCToLatLon(p);
				}
				lar.setX(p.getLon());
				lar.setY(p.getLat());
			} else {
				lar.setResultType(resultType + "");
			}
			lars.add(lar);
		}
		return lars;
	}
	
	private List<OrderBaseEntity> buildOrderEntityByAPIResult(List<LogisticsAPIResult> addressMatchResults){
		List<OrderBaseEntity> entitys = new ArrayList<OrderBaseEntity>();
		Iterator<LogisticsAPIResult> iterator = addressMatchResults.iterator();
		while(iterator.hasNext()){
			LogisticsAPIResult results = (LogisticsAPIResult) iterator.next();
			int resultType=Integer.parseInt(results.getResultType());
			OrderBaseEntity obe = new OrderBaseEntity();
			obe.setId(results.getId());
			/**
			 * 根据坐标查询省市区
			 */
			double smx=results.getX();
			double smy=results.getY();
			Map<String,String> m=this.geocodingService.searchAdmincodeForCounty(smx,smy);
			if(m!=null){
				obe.setProvince(m.get("PROVINCE"));
				obe.setCity(m.get("CITY2"));
				obe.setCounty(m.get("COUNTY"));
				obe.setAdmincode(m.get("ADMINCODE"));
			}
			if(resultType==AddressMatchConstants.RESULT_TYPE_SUCCESS){
				obe.setOrderStatusId(Byte.valueOf("3"));
			}else{
				obe.setOrderStatusId(Byte.valueOf("4"));
			}
			obe.setStatusUpdateTime(new Date());
			entitys.add(obe);
		}
		return entitys;
	}
	
	
	private List<OrderFendanEntity> buildFendEntitysByAPIResult(List<LogisticsAPIResult> addressMatchResults,String departmentId){
		List<OrderFendanEntity> fendanEntitys = new ArrayList<OrderFendanEntity>();
		for(LogisticsAPIResult result:addressMatchResults){
			int resulttype=Integer.parseInt(result.getResultType());
			if(resulttype==AddressMatchConstants.RESULT_TYPE_SUCCESS
					||resulttype==AddressMatchConstants.RESULT_TYPE_NOT_AREA){
				OrderFendanEntity fendanEntity = new OrderFendanEntity();
				fendanEntity.setId(result.getId());
				if (resulttype== AddressMatchConstants.RESULT_TYPE_SUCCESS) {
					fendanEntity.setFendanStatusId(Byte.valueOf("1"));
				}else {
					fendanEntity.setFendanStatusId(Byte.valueOf("2"));
				}
				fendanEntity.setFendanTime(new Date());
				fendanEntity.setAreaId(result.getAreaNumber());
				fendanEntity.setSmx(BigDecimal.valueOf(result.getX()));
				fendanEntity.setSmy(BigDecimal.valueOf(result.getY()));
				fendanEntity.setDepartment_id(departmentId);
				fendanEntitys.add(fendanEntity);
			}
		}
		return fendanEntitys;
	}
	
	private List<DistributeAddress> buildAPIAddressInfo(Iterable<OrderBaseEntity> iterable){
		List<DistributeAddress> addressInfo = new ArrayList<DistributeAddress>();
		Iterator<OrderBaseEntity> iterator  = iterable.iterator();
		while(iterator.hasNext()){
			OrderBaseEntity obe = (OrderBaseEntity) iterator.next();
			DistributeAddress aid = new DistributeAddress();
			aid.setAddress(obe.getAddress());
			aid.setId(obe.getId());
			addressInfo.add(aid);
		}
		return addressInfo;
	}
	
	/**
	 * 最多1000地址
	 * 最多15个线程
	 * @param addresses
	 * @return
	 * @Author Juannyoh
	 * 2016-11-25下午5:36:43
	 */
	private List<List<DistributeAddress>> splitAddressesImport(List<DistributeAddress> addresses){
		int paramSize = addresses.size();
		int DEFAULT_PROGRESS_COUNT = AddressMatchConstants.MULTI_THREAD_COUNT;
		int progressCount = paramSize <= DEFAULT_PROGRESS_COUNT ? 1 : DEFAULT_PROGRESS_COUNT;
		int num = paramSize / DEFAULT_PROGRESS_COUNT;
		List<List<DistributeAddress>> params = new ArrayList<List<DistributeAddress>>();
		for (int i = 0; i < progressCount; i++) {
			List<DistributeAddress> paramList = null;
			if (i < progressCount - 1) {
				paramList = addresses.subList(i * num, (i + 1) * num);
			} else {
				paramList = addresses.subList(i * num, addresses.size());
			}
			params.add(paramList);
		}
		return params;
	}
	
	private List<AreaEntity> queryAreaByMultiThreads4Import(Point2D[] point2Ds, String enterpriseId, String dcode,String userId) {
		int length = point2Ds.length;
		int pageSize = length;
		List<AreaQueryCallable> lstCallable = new ArrayList<AreaQueryCallable>();
		if (length > AddressMatchConstants.MULTI_THREAD_AREA_THRESHOD) {
			pageSize = length / AddressMatchConstants.MULTI_THREAD_AREA_COUNT;
			for (int i = 0; i < AddressMatchConstants.MULTI_THREAD_AREA_COUNT; i++) {
				Point2D[] subPoints =null;
				if(i<AddressMatchConstants.MULTI_THREAD_AREA_COUNT-1){
					subPoints = Arrays.copyOfRange(point2Ds, i * pageSize, pageSize * i + pageSize);
				}else{
					subPoints = Arrays.copyOfRange(point2Ds, i * pageSize, length);
				}
				AreaQueryCallable callable = new AreaQueryCallable(enterpriseId,dcode,subPoints,userId+"_"+i);
				callable.setAreaService(this.areaService);
				lstCallable.add(callable);
			}
		}else{
			AreaQueryCallable callable = new AreaQueryCallable(enterpriseId,dcode,point2Ds,userId+"_"+0);
			callable.setAreaService(this.areaService);
			lstCallable.add(callable);
		}
		
		List<java.util.concurrent.Future<List<AreaEntity>>> lstFutures = null;
		List<AreaEntity> results = new LinkedList<AreaEntity>();
		ExecutorService exec = Executors.newFixedThreadPool(lstCallable.size());
		try {
			lstFutures = exec.invokeAll(lstCallable);
			for (int i = 0; i < lstCallable.size(); i++) {
				Future<List<AreaEntity>> future = (Future<List<AreaEntity>>) lstFutures.get(i);
				List<AreaEntity> result = future.get();
				results.addAll(result);
			}
		} catch (Exception e1) {
			throw new NullPointerException(e1.getMessage());
		}finally{
			if(null != exec){
				exec.shutdown();
			}
		}
		return results;
	}

	@Override
	public List<LogisticsAPIResult> logisticsAPI_Area(
			List<DistributeAddress> addresses, String coorType, String userId,
			String enterpriseId, String departmentId, boolean needAreaInfo) {
		// 判断是否需要进行地址拆分
	    long time1 = System.currentTimeMillis();
		List<List<DistributeAddress>> params = splitAddresses(addresses);
		
		//判断企业是否需要去查地址纠错库
		long time2 = System.currentTimeMillis();
//		boolean isUseCorrectFlag=this.correctAddressService.isneedUseCorrectAddr(enterpriseId);
		boolean isUseCorrectFlag=this.configConstants.isUseCorrectAddr(enterpriseId);
		
		// 创建线程
		long time3 = System.currentTimeMillis();
		ExecutorService exec = Executors.newFixedThreadPool(params.size());
		List<AddressSplitCallable> lstCallables = new ArrayList<AddressSplitCallable>();
		for(int i=0;i<params.size();i++){
			List<com.supermap.egisp.addressmatch.beans.AddressMatchParam> addressInfos = buildAPIAddressMatchInfo(params.get(i));
			AddressSplitCallable callable = null;
			if(isUseCorrectFlag){//需要去查地址纠错库
				callable = new AddressSplitCallable(addressInfos,userId+"_"+i,this.addressMatch,this.correctAddressService,enterpriseId);
			}else{
				callable = new AddressSplitCallable(addressInfos,userId+"_"+i,this.addressMatch,null,null);
			}
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
					result = buildTimeoutResult(params.get(i),"分单发生超时");
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
		// todo: 需要对批量地址进行拆分并创建多线程进行并发处理
		// 地址匹配
//		List<AddressMatchResult> addressMatchResults = addressMatch.search(addressInfos);
		// 更新地址信息
		long time4 = System.currentTimeMillis();
		Point2D[] point2Ds = buildDistributePoint(results);
		// 点查面
		LOGGER.debug("## 开始点查面。。");
		long time5 = System.currentTimeMillis();
		InfoDeptEntity deptEntity = deptDao.findById(departmentId);
		if(null == deptEntity){
			throw new NullPointerException("未找到部门信息");
		}
		String dcode = deptEntity.getCode();
		// 通过多线程方式查询区域面
		long time6 = System.currentTimeMillis();
		List<AreaEntity> areaEntitys = queryAreaByMultiThreads(point2Ds, enterpriseId, dcode, userId);
		
		//构建返回结果，同时将解析失败的地址写入到纠错库，给个标识，知否需要写到纠错库
		long time7 = System.currentTimeMillis();
		List<LogisticsAPIResult> result = buildAPIResult(results, areaEntitys, coorType,addresses, enterpriseId, dcode, userId,isUseCorrectFlag,needAreaInfo);
		long time8 = System.currentTimeMillis();
		LOGGER.info("logisticsAPI:["+(time2-time1)+"ms]["+(time3-time2)+"ms]["+(time4-time3)+"ms]["+(time5-time4)+"ms]["+(time6-time5)+"ms]["+(time7-time6)+"ms]["+(time8-time7)+"ms]");
		return result;
	}
}


