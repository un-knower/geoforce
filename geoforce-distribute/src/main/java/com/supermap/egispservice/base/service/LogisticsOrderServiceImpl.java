package com.supermap.egispservice.base.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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

import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.exceptions.AreaException;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.constants.StatusConstants;
import com.supermap.egispservice.base.dao.ILogisticsOrderBaseDao;
import com.supermap.egispservice.base.dao.ILogisticsOrderFendanDao;
import com.supermap.egispservice.base.dao.ILogisticsOrderFendanStatusDao;
import com.supermap.egispservice.base.dao.ILogisticsOrderStatusDao;
import com.supermap.egispservice.base.dao.IOrderBaseDao;
import com.supermap.egispservice.base.entity.DimOrderStatusEntity;
import com.supermap.egispservice.base.entity.FendanStatusEntity;
import com.supermap.egispservice.base.entity.OrderBaseEntity;
import com.supermap.egispservice.base.entity.OrderFendanEntity;
import com.supermap.egispservice.base.pojo.LogisticsResultInfo;
import com.supermap.egispservice.base.util.BeanTool;
import com.supermap.egispservice.base.util.StringUtil;

@Service("logisticsOrderService")
public class LogisticsOrderServiceImpl implements ILogisticsOrderService {

	private static Logger LOGGER = Logger.getLogger(LogisticsOrderServiceImpl.class);
	
	@Autowired
	private ILogisticsOrderBaseDao orderDao;
	
	@Autowired
	private ILogisticsOrderFendanDao fendanDao;
	
	@Autowired
	private IAreaService areaService;
	
	@Autowired
	private IOrderBaseDao orderBaseDao;
	
	
	@Autowired
	private ILogisticsOrderFendanStatusDao fendanStatusDao;
	@Autowired
	private ILogisticsOrderStatusDao orderStatusDao;
	
	@Override
	@Transactional
	public List<String> saveOrderInfos(List<OrderBaseEntity> orderBaseInfos) {
		long start = System.currentTimeMillis();
		orderDao.save(orderBaseInfos);
		LOGGER.info("## consumpt the time about times ["+(System.currentTimeMillis() - start)+"] ");
		return getIds(orderBaseInfos);
	}
	
	
	@Transactional
	public void update(List<OrderBaseEntity> obes){
		for (int i = 0; i < obes.size(); i++) {
			OrderBaseEntity obe = obes.get(i);
			OrderBaseEntity obe2 = orderDao.findOne(obe.getId());
			if(null != obe2){
				BeanUtils.copyProperties(obe, obe2, BeanTool.getNullPropertyNames(obe));
				this.orderDao.save(obe2);
			}
		}
	}
	
	
	private List<String> getIds(List<OrderBaseEntity> orderBaseInfos){
		List<String> ids = new ArrayList<String>();
		for(int i=0;i<orderBaseInfos.size();i++){
			ids.add(new String(orderBaseInfos.get(i).getId()));
		}
		return ids;
		
	}
	

	  /**
	 * 创建分页请求.
	 */
	public static PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}


	@Override
	public Map<String,Object> queryByBatch(final String userId,String enterpriseId,String decode,final String batch,int pageNo,int pageSize) {
		PageRequest pageRequest = buildPageRequest(pageNo, pageSize, null);
		Specification<OrderBaseEntity> spec = new Specification<OrderBaseEntity>(){

			@Override
			public Predicate toPredicate(Root<OrderBaseEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> uId = root.get("userId");
				List<Predicate> predicateList=new ArrayList<Predicate>();
				predicateList.add(builder.equal(uId, userId));
				Path<String> batch2 = root.get("batch");
				if(!StringUtils.isEmpty(batch)){
					predicateList.add(builder.like(batch2, "%" + batch.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0)));
				}else{
//					String maxBatch = orderDao.queryMaxBatch();
					String maxBatch=orderDao.queryMaxBatchByUserId(userId);//查询用户的最近一个批次
					predicateList.add(builder.like(batch2, maxBatch + "%"));
				}
				Predicate p[] = new Predicate[predicateList.size()];
				predicateList.toArray(p);
				query.where(p);
				return null;
			}
		};
		List<OrderBaseEntity> orderEntitys = null;
		Map<String, Object> m= new HashMap<String, Object>();;
		// 如果pageNo = -1 表示查询所有结果
		if(-1 != pageNo){
			Page<OrderBaseEntity> page=this.orderDao.findAll(spec, pageRequest);
			m.put("totalPages", page.getTotalPages());
			m.put("page", pageNo);
			m.put("totalCount", page.getTotalElements());
			orderEntitys = page.getContent();
		}else{
			orderEntitys  = this.orderDao.findAll(spec);
		}
		List<LogisticsResultInfo> results = new ArrayList<LogisticsResultInfo>();
		if(null != orderEntitys && orderEntitys.size() > 0){
			for (int i = 0; i < orderEntitys.size(); i++) {
				LogisticsResultInfo lri = new LogisticsResultInfo();
				OrderBaseEntity obe = orderEntitys.get(i);
				lri.setId(obe.getId());
				lri.setAddress(obe.getAddress());
				lri.setAdmincode(obe.getAdmincode());
				lri.setCity(obe.getCity());
				lri.setCounty(obe.getCounty());
				lri.setProvince(obe.getProvince());
				lri.setOrderNum(obe.getNumber());
				lri.setBatch(obe.getBatch());
				OrderFendanEntity fendanEntity = this.fendanDao.findOne(obe.getId());
				if(fendanEntity != null){
					lri.setAreaId(fendanEntity.getAreaId());
					if(!StringUtils.isEmpty(fendanEntity.getAreaId())){
						AreaEntity ae = null;
						try {
							ae = this.areaService.queryByIdOrNumber(fendanEntity.getAreaId(), null, null, false);
						} catch (AreaException e) {
							e.printStackTrace();
						}
						if(null != ae){
							lri.setAreaName(ae.getName());
							lri.setAreaNum(ae.getAreaNumber());
							
							//区划状态，关联的区划信息
							int areastatus=ae.getArea_status();
							lri.setAreaStatus(areastatus);
//							LOGGER.info("status:"+lri.getAreaStatus());
							if(areastatus==1){//如果是停用状态，则去查询关联的区划
								String relationid=ae.getRelation_areaid();
								if(StringUtils.isNotEmpty(relationid)){
									lri.setRelation_areaid(relationid);
									List<Map<String,String>> relationlist=this.areaService.findRelationAreaAttrs(ae.getId(),relationid);
									if(relationlist!=null&&relationlist.size()>0){
										lri.setRelation_areaname(relationlist.get(relationlist.size()-1).get("NAME"));
										lri.setRelation_areanumber(relationlist.get(relationlist.size()-1).get("AREA_NUM"));
//										LOGGER.info(lri.getRelation_areaname()+"/"+lri.getRelation_areanumber());
									}
								}
							}
						}
//						List<PointEntity> list = this.pointService.queryByAreaId(fendanEntity.getAreaId(), enterpriseId, departmentId);
//						if(null != list && list.size() > 0){
//							PointEntity pe = list.get(0);
//							lri.setNetId(pe.getId());
//							lri.setNetName(pe.getName());
//							lri.setDutyName(pe.getDutyName());
//							lri.setDutyPhone(pe.getDutyPhone());
//						}
					}
					Byte statusId = fendanEntity.getFendanStatusId();
					if(null != statusId){
						FendanStatusEntity fse = fendanStatusDao.findOne(statusId);
						if(null != fse){
							lri.setFendanStatus(fse.getValue());
							
						}
					}
					lri.setSmx(fendanEntity.getSmx().doubleValue());
					lri.setSmy(fendanEntity.getSmy().doubleValue());
				}
				Byte orderStatusId = obe.getOrderStatusId();
				if(null != orderStatusId){
					DimOrderStatusEntity dos = this.orderStatusDao.findOne(orderStatusId);
					if(dos != null){
						lri.setOrderStatus(dos.getValue());
						lri.setStatus(orderStatusId);
						if(StringUtils.isEmpty(lri.getFendanStatus())){
							lri.setFendanStatus(dos.getValue());
						}
					}
				}
				results.add(lri);
			}
		}
		m.put("records", results);
		return m;
	}
	
	@Override
	public Map<String, Object> queryByBatch4API(String userId, String enterpriseId, String dcode, String batch,
			int pageNo, int pageSize) {
		Map<String,Object> resultMap = queryByBatch(userId, enterpriseId, dcode, batch, pageNo, pageSize);
		
		return null;
	}


	@Override
	@Transactional
	public boolean updateOrderStatus(List<String> ids) {
		boolean isUpdateSuccess = false;
		try{
			isUpdateSuccess = updateOrderStatus(ids, StatusConstants.ORDER_BASE_LOGISTICS_PLANED);
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return isUpdateSuccess;
	}

	public boolean updateOrderStatus(List<String> ids,Byte statusId) {
		this.orderDao.updateOrderStatus(ids, statusId);
		return true;
	}


	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> queryById(String id,String userId) {
		OrderBaseEntity baseEntity = this.orderDao.findOne(id);
		Map<String,Object> map = null;
		if(null != baseEntity){
			if(!StringUtils.isEmpty(baseEntity.getUserId()) && baseEntity.getUserId().equals(userId)){
				map = new HashMap<String,Object>();
				LOGGER.info("## 查询到订单["+baseEntity.getId()+"]");
				map.put("address", baseEntity.getAddress());
				map.put("admincode", baseEntity.getAdmincode());
				map.put("batch", baseEntity.getBatch());
				map.put("city", baseEntity.getCity());
				map.put("county", baseEntity.getCounty());
				map.put("delete_flag", baseEntity.getDeleteFlag());
				map.put("departmentId", baseEntity.getDepartmentId());
				map.put("enterpriseId", baseEntity.getEnterpriseId());
				map.put("id", baseEntity.getId());
				map.put("userId", baseEntity.getUserId());
				map.put("import_time", baseEntity.getImportTime());
				map.put("number", baseEntity.getNumber());
				map.put("province", baseEntity.getProvince());
				DimOrderStatusEntity statusEntity = this.orderStatusDao.findOne(baseEntity.getOrderStatusId());
				if(null != statusEntity){
					map.put("status", statusEntity.getValue());
				}else{
					map.put("status", StatusConstants.ORDER_BASE_IMPORTED);
				}
				OrderFendanEntity fendanEntity = this.fendanDao.findOne(baseEntity.getId());
				double smx = 0d;
				double smy = 0d;
				if(null != fendanEntity){
					smx = fendanEntity.getSmx() == null?0d:fendanEntity.getSmx().doubleValue();
					smy = fendanEntity.getSmy() == null?0d:fendanEntity.getSmy().doubleValue();
				}
				map.put("smx", smx);
				map.put("smy", smy);
				
			}else{
				LOGGER.warn("## 查询到不属于该用户的数据[userId:"+userId+"],[orderId:"+baseEntity.getId()+"]");
			}
			
		}
		
		return map;
	}


	/**
	 * 根据id批量查询地址
	 */
	@Override
	public List<Map<String, Object>> queryByIds(String[] ids) {
		if(ids==null||ids.length==0){
			return null;
		}
		
		List<String> idlists=Arrays.asList(ids);
		List<Object[]> queryResults = this.orderDao.queryByIds(idlists);
		if(queryResults==null||queryResults.size()==0){
			return null;
		}
		
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		for(Object[] o:queryResults){
			String id = (String)o[0];
			String number = (String)o[1];
			String address = (String)o[2];
			String lot = (String)o[3];
			String start=(String)o[4];
			String end=(String)o[5];
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("id", id);
			item.put("number", number);
			item.put("address",address);
			item.put("batch", lot);
			item.put("startTime", start);
			item.put("endTime", end);
			results.add(item);
		}
		
		return results;
	}


	@Override
	public List<String> queryBatchsByParams(String eid, List<String> userids,
			String batch) {
		return this.orderBaseDao.getDistinctBatchsByParams(eid, userids, batch);
	}


	@Override
	public Map<String, Object> queryExportByBatch(final List<String> userId,
			final String enterpriseId, final List<String> batch, final String status,
			final String admincode, int pageNo, int pageSize) {
		if(StringUtils.isEmpty(enterpriseId)){
			return null;
		}
		Map<String, Object> m= new HashMap<String, Object>();
		List list=this.orderBaseDao.queryExportByBatch(enterpriseId, userId, batch, status, admincode);
		if(list!=null&&list.size()>0){
			List<LogisticsResultInfo> results = new ArrayList<LogisticsResultInfo>();
			for(int i=0,s=list.size();i<s;i++){
				LogisticsResultInfo lri = new LogisticsResultInfo();
				Map obe = (Map) list.get(i);
				lri.setId(StringUtil.convertObjectToString(obe.get("id")));
				lri.setAddress(StringUtil.convertObjectToString(obe.get("address")));
				lri.setAdmincode(StringUtil.convertObjectToString(obe.get("admincode")));
				lri.setCity(StringUtil.convertObjectToString(obe.get("city")));
				lri.setCounty(StringUtil.convertObjectToString(obe.get("county")));
				lri.setProvince(StringUtil.convertObjectToString(obe.get("province")));
				lri.setOrderNum(StringUtil.convertObjectToString(obe.get("number")));
				lri.setBatch(StringUtil.convertObjectToString(obe.get("batch")));
				
				String areaid=StringUtil.convertObjectToString(obe.get("areaid"));
				lri.setAreaId(areaid);
				if(!StringUtils.isEmpty(areaid)){
					AreaEntity ae = null;
					try {
						ae = this.areaService.queryByIdOrNumber(areaid, null, null, false);
					} catch (AreaException e) {
						e.printStackTrace();
					}
					if(null != ae){
						lri.setAreaName(ae.getName());
						lri.setAreaNum(ae.getAreaNumber());
					}
				}
				
				lri.setFendanStatus(StringUtil.convertObjectToString(obe.get("fendanstatus")));
				lri.setSmx(StringUtil.convertObjectToDouble(obe.get("smx")));
				lri.setSmy(StringUtil.convertObjectToDouble(obe.get("smy")));
				
				lri.setOrderStatus(StringUtil.convertObjectToString(obe.get("orderstatus")));
				lri.setStatus(StringUtil.convertObjectToInt(obe.get("orderstatusid")));
				results.add(lri);
			}
			m.put("records", results);
		}
		/*Specification<OrderBaseEntity> spec = new Specification<OrderBaseEntity>(){
			@Override
			public Predicate toPredicate(Root<OrderBaseEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> uIdpath = root.get("userId");
				Path<String> eidpath = root.get("enterpriseId");
				Path<String> batchpath=root.get("batch");
				Path<String> statuspath=root.get("orderStatusId");
				Path<String> admincodepath=root.get("admincode");
				
				List<Predicate> predicateList=new ArrayList<Predicate>();
				
				if(null!=userId&&userId.size()>0){
					predicateList.add(uIdpath.in(userId));
				}
				
				if(StringUtils.isNotEmpty(enterpriseId)){
					predicateList.add(builder.equal(eidpath, enterpriseId));
				}
				
				if(StringUtils.isNotEmpty(admincode)){
					predicateList.add(builder.like(admincodepath,admincode+"%"));
				}
				
				if(null!=batch&&batch.size()>0){
					predicateList.add(batchpath.in(batch));
				}
				
				if(StringUtils.isNotEmpty(status)){
					if(status.equals("0")){//成功
						List list=new ArrayList();
						list.add("3");list.add("5");
						predicateList.add(statuspath.in(list));
					}else if(status.equals("1")){//失败
						List list=new ArrayList();
						list.add("4");list.add("10");
						predicateList.add(statuspath.in(list));
					}
				}
				
				Predicate p[] = new Predicate[predicateList.size()];
				predicateList.toArray(p);
				query.where(p);
				return null;
			}
		};
		List<OrderBaseEntity> orderEntitys = null;
		// 如果pageNo = -1 表示查询所有结果
		if(-1 != pageNo){
			PageRequest pageRequest = new PageRequest(pageNo-1, pageSize, new Sort(Direction.ASC,new String[]{"importTime"}));
			Page<OrderBaseEntity> page=this.orderDao.findAll(spec, pageRequest);
			m.put("totalPages", page.getTotalPages());
			m.put("page", pageNo);
			m.put("totalCount", page.getTotalElements());
			orderEntitys = page.getContent();
		}else{
			orderEntitys  = this.orderDao.findAll(spec,new Sort(Direction.ASC,new String[]{"importTime"}));
		}
		List<LogisticsResultInfo> results = new ArrayList<LogisticsResultInfo>();
		if(null != orderEntitys && orderEntitys.size() > 0){
			List<DimOrderStatusEntity> orderstatuslist=(List<DimOrderStatusEntity>) this.orderStatusDao.findAll();
			List<FendanStatusEntity> fendanstatuslist=(List<FendanStatusEntity>) this.fendanStatusDao.findAll();
			Map<Byte,String> orderStatusMap=new HashMap<Byte,String>();
			Map<Byte,String> fendaStatusMap=new HashMap<Byte,String>();
			if(orderstatuslist!=null&&orderstatuslist.size()>0){
				for(DimOrderStatusEntity orderstatus:orderstatuslist){
					orderStatusMap.put(orderstatus.getId(), orderstatus.getValue());
				}
			}
			if(fendanstatuslist!=null&&fendanstatuslist.size()>0){
				for(FendanStatusEntity fendanstatus:fendanstatuslist){
					fendaStatusMap.put(fendanstatus.getId(), fendanstatus.getValue());
				}
			}
			
			for (int i = 0; i < orderEntitys.size(); i++) {
				LogisticsResultInfo lri = new LogisticsResultInfo();
				OrderBaseEntity obe = orderEntitys.get(i);
				lri.setId(obe.getId());
				lri.setAddress(obe.getAddress());
				lri.setAdmincode(obe.getAdmincode());
				lri.setCity(obe.getCity());
				lri.setCounty(obe.getCounty());
				lri.setProvince(obe.getProvince());
				lri.setOrderNum(obe.getNumber());
				lri.setBatch(obe.getBatch());
				OrderFendanEntity fendanEntity = this.fendanDao.findOne(obe.getId());
				if(fendanEntity != null){
					lri.setAreaId(fendanEntity.getAreaId());
					if(!StringUtils.isEmpty(fendanEntity.getAreaId())){
						AreaEntity ae = null;
						try {
							ae = this.areaService.queryByIdOrNumber(fendanEntity.getAreaId(), null, null, false);
						} catch (AreaException e) {
							e.printStackTrace();
						}
						if(null != ae){
							lri.setAreaName(ae.getName());
							lri.setAreaNum(ae.getAreaNumber());
						}
					}
					Byte statusId = fendanEntity.getFendanStatusId();
					if(null != statusId){
						lri.setFendanStatus(fendaStatusMap.get(statusId));
						FendanStatusEntity fse = fendanStatusDao.findOne(statusId);
						if(null != fse){
							lri.setFendanStatus(fse.getValue());
						}
					}
					lri.setSmx(fendanEntity.getSmx().doubleValue());
					lri.setSmy(fendanEntity.getSmy().doubleValue());
				}
				Byte orderStatusId = obe.getOrderStatusId();
				if(null != orderStatusId){
					lri.setOrderStatus(orderStatusMap.get(orderStatusId));
					lri.setStatus(orderStatusId);
					DimOrderStatusEntity dos = this.orderStatusDao.findOne(orderStatusId);
					if(dos != null){
						lri.setOrderStatus(dos.getValue());
						lri.setStatus(orderStatusId);
					}
				}
				results.add(lri);
			}
		}*/
		return m;
	}


	@Override
	public int queryExportCountByParams(String eid, List<String> userids,
			List<String> batch, String status, String admincode) {
		return this.orderBaseDao.getOrderCountsByParams(eid, userids, batch, status, admincode);
	}


	@Override
	public int deleteOrdersByParams(String eid, List<String> userids,
			List<String> batch, String status, String admincode) {
		return this.orderBaseDao.deleteOrdersByParam(eid, userids, batch, status, admincode);
	}


	@Override
	public List<DimOrderStatusEntity> queryAllOrderStatus() {
		return (List<DimOrderStatusEntity>) this.orderStatusDao.findAll();
	}
	
	
}
