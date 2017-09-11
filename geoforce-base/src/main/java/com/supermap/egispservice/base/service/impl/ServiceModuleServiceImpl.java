package com.supermap.egispservice.base.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.supermap.egispservice.base.dao.ModuleStatusDao;
import com.supermap.egispservice.base.dao.OrderItemsDao;
import com.supermap.egispservice.base.dao.ServiceModuleDao;
import com.supermap.egispservice.base.entity.ModuleStatusEntity;
import com.supermap.egispservice.base.entity.OrderItemsEntity;
import com.supermap.egispservice.base.entity.ServiceModuleEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseServiceModule;
import com.supermap.egispservice.base.pojo.BaseServiceModuleList;
import com.supermap.egispservice.base.pojo.BaseServiceModuleListItem;
import com.supermap.egispservice.base.pojo.ServiceModuleByType;
import com.supermap.egispservice.base.pojo.ServiceModuleFieldNames;
import com.supermap.egispservice.base.service.IServiceModuleService;

@Component("serviceModule")
public class ServiceModuleServiceImpl implements IServiceModuleService {

	private static Logger LOGGER = Logger.getLogger(ServiceModuleServiceImpl.class);
	
	@Resource
	private ServiceModuleDao serviceModuleDao;
	
	@Resource
	private ModuleStatusDao moduleStatusDao;
	
	@Resource
	private OrderItemsDao orderItemsDao;
	
	
	@Override
	@Transactional
	public void add(ServiceModuleEntity entity) {
		serviceModuleDao.save(entity);
	}
	

	@Override
	@Transactional
	public void deleteById(String id) {
		ServiceModuleEntity module=this.serviceModuleDao.findById(id);
		serviceModuleDao.delete(id);
		if(module!=null){
			List<OrderItemsEntity> itemlist=this.orderItemsDao.findByModuleId(module);
			if(itemlist!=null&&itemlist.size()>0){
				for(OrderItemsEntity item:itemlist){
					this.orderItemsDao.deleteOrderItemsByid(item.getId());
				}
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public ServiceModuleEntity queryById(String id, boolean isNeedItems) {
		ServiceModuleEntity entity = this.serviceModuleDao.findById(id);
		if(isNeedItems && null != entity && entity.getServiceModules().size() > 0){
			List<ServiceModuleEntity> modules = entity.getServiceModules();
			for(ServiceModuleEntity sme : modules){
				sme.getId();
			}
		}
		return entity;
	}

	@Override
	@Transactional
	public void add(String name, String pid, String status, String useLimit, String url, String refUrl, String price,
			String remarks, String icon_url, String type) throws ParameterException {
		ServiceModuleEntity moduleEntity = buildEntity(name, pid, status, useLimit, url, refUrl, price, remarks, type);
		moduleEntity.setIconUrl(icon_url);
		add(moduleEntity);

	}
	/**
	 * 
	 * <p>Title ：buildEntity</p>
	 * Description：
	 * @param name
	 * @param pidStr
	 * @param status
	 * @param useLimit
	 * @param url
	 * @param refUrl
	 * @param price
	 * @param remarks
	 * @param code
	 * @param type
	 * @return
	 * @throws ParameterException
	 * Author：Huasong Huang
	 * CreateTime：2014-11-6 上午10:02:07
	 */
	private ServiceModuleEntity buildEntity(String name, String pidStr, String status, String useLimit, String url, String refUrl,
			 String price,String remarks,String type) throws ParameterException{
		ServiceModuleEntity moduleEntity = new ServiceModuleEntity();
		String code = buildCode(pidStr);
		moduleEntity.setCode(code);
		moduleEntity.setName(name);
		moduleEntity.setType(type);
		// 如果提供了父模块，则需要进行关联
		if(!StringUtils.isEmpty(pidStr)){
			ServiceModuleEntity parent = queryById(pidStr, false);
			if(null != parent){
				moduleEntity.setParent(parent);
			}
		}
		// 服务模块状态
		if(!StringUtils.isEmpty(status)){
			ModuleStatusEntity mse = this.moduleStatusDao.findByValue(status);
			if(null == mse){
				mse = new ModuleStatusEntity();
				mse.setValue(status);
				this.moduleStatusDao.save(mse);
			}
			moduleEntity.setStatusId(mse);
		}
		// 使用限制
		if(!StringUtils.isEmpty(useLimit)){
			int useLimitInt = Integer.parseInt(useLimit);
			moduleEntity.setUseLimit(useLimitInt);
		}
		moduleEntity.setUrl(url);
		moduleEntity.setRefUrl(refUrl);
		moduleEntity.setRemark(remarks);
		float priceFloat = Float.parseFloat(price);
		moduleEntity.setPrice(new BigDecimal(priceFloat));
		return moduleEntity;
	}
	

	/**
	 * 
	 * <p>Title ：buildCode</p>
	 * Description：		自动根据库中已有的产品进行code构建
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-6 上午10:27:46
	 */
	private String buildCode(String pid){
		String code = null;
		// 子产品code创建
		if (!StringUtils.isEmpty(pid)) {
			String maxCode = this.serviceModuleDao.getMaxCode(pid);
			int startIndex = 2;
			if (maxCode.length() > 4) {
				startIndex++;
			}
			String subCode = maxCode.substring(startIndex, maxCode.length());
			code = maxCode.substring(0, startIndex);
			int subCodeInt = Integer.parseInt(subCode);
			subCodeInt += 1;
			if ((0 + "").length() == 1) {
				code += "0" + subCodeInt;
			}
		} else {
			// 主产品code创建
			List<String> codes = this.serviceModuleDao.getCodes();
			if(null!=codes&&codes.size()>0){
				for(String c:codes){
					c=c.substring(0, 2);
				}
			}
			String codePre = "01";
			if(null != codes && codes.size() > 0){
				codePre = getIntervalCode(codes);
			}
			code = codePre +"00";
		}
		LOGGER.info("## 构建code ["+code+"]");
		return code;
	}
	/**
	 * 
	 * <p>Title ：getIntervalCode</p>
	 * Description：		获取未被使用的code前缀
	 * @param codes
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-6 上午11:06:26
	 */
	public String getIntervalCode(List<String> codes) {
		String first = codes.get(0);
		int firstInt = Integer.parseInt(first);
		for (int i = 1; i < codes.size(); i++) {
			String second = codes.get(i);
			int secondInt = Integer.parseInt(second);
			if ((secondInt - firstInt) > 1) {
				break;
			} else {
				firstInt = secondInt;
			}
		}
		firstInt += 1;
		return (firstInt + "").length() == 1 ? "0" + firstInt : firstInt + "";
	}
	
	
	

	@Override
	@Transactional
	public void update(String idStr, String status, String useLimit, String url, String refUrl, String remarks,
			String price, String code,String icon_url,String type) throws ParameterException {
		ServiceModuleEntity sme = this.serviceModuleDao.findById(idStr);
		if (null != sme) {
			boolean isNeedUpdate = false;
			// 修改模块状态
			ModuleStatusEntity moduleStatus = this.moduleStatusDao.findByValue(status);
			if (null != moduleStatus && !moduleStatus.equals(sme.getStatusId())) {
				isNeedUpdate = true;
				sme.setStatusId(moduleStatus);
			}
			// 使用限制
			if (!StringUtils.isEmpty(useLimit)) {
				int useLimitInt = Integer.parseInt(useLimit);
				if (useLimitInt != sme.getUseLimit()) {
					sme.setUseLimit(useLimitInt);
					isNeedUpdate = true;
				}
			}
			// 修改URL
			if (!StringUtils.isEmpty(url)) {
				if (!url.equals(sme.getUrl())) {
					sme.setUrl(url);
					isNeedUpdate = true;
				}
			}
			// 修改参考文档URL
			if (!StringUtils.isEmpty(refUrl)) {
				if (!refUrl.equals(sme.getRefUrl())) {
					sme.setRefUrl(refUrl);
					isNeedUpdate = true;
				}
			}

			// 修改价格
			if (!StringUtils.isEmpty(price)) {
				float priceFloat = Float.parseFloat(price);
				if (priceFloat != sme.getPrice().floatValue()) {
					sme.setPrice(new BigDecimal(priceFloat));
					isNeedUpdate = true;
				}
			}
			// 修改分类
			if (!StringUtils.isEmpty(type)) {
				if (!type.equals(sme.getType())) {
					sme.setType(type);
					isNeedUpdate = true;
				}
			}
			// 修改备注
			if (!StringUtils.isEmpty(remarks)) {
				if (!remarks.equals(sme.getRemark())) {
					sme.setRemark(remarks);
					isNeedUpdate = true;
				}
			}

			if (!StringUtils.isEmpty(code)) {
				if (!code.equals(sme.getCode())) {
					sme.setCode(code);
					isNeedUpdate = true;
				}
			}
			if (!StringUtils.isEmpty(icon_url)) {
				if (!icon_url.equals(sme.getIconUrl())) {
					sme.setIconUrl(icon_url);
					isNeedUpdate = true;
				}
			}
			

			if (isNeedUpdate) {
				sme.setUpdateTime(new Date());
				this.serviceModuleDao.save(sme);
			} else {
				throw new ParameterException("未发现需要更新的信息，不执行更新");
			}
		} else {
			throw new ParameterException("服务模块ID[" + idStr + "]不存在");
		}
	}


	@Override
	@Transactional(readOnly=true)
	public BaseServiceModule queryById(String idStr) {
		BaseServiceModule bsm = null;
		List<ServiceModuleEntity> moduleTree = this.serviceModuleDao.queryModuleTree(idStr);
		if(null != moduleTree && moduleTree.size() > 0){
			bsm = convert(moduleTree, idStr);
		}
		return bsm;
	}
	
	/**
	 * 
	 * <p>Title ：convert</p>
	 * Description：将服务模块树转换为POJO
	 * @param smes
	 * @param rootId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-12 上午09:41:36
	 */
	private BaseServiceModule convert(List<ServiceModuleEntity> smes,String rootId){
		BaseServiceModule bsm = null;
		ServiceModuleEntity rootModule = getServiceModuleById(smes, rootId);
		if(null != rootModule){
			bsm = convert(rootModule);
			// 只有在拥有子模块的情况下才进行此操作
			if(smes.size() > 1){
				List<BaseServiceModule> baseServiceModules = new ArrayList<BaseServiceModule>();
				for(ServiceModuleEntity entity : smes){
					if (!entity.getId().equals(bsm.getId())) {
						BaseServiceModule childModule = convert(entity);
						baseServiceModules.add(childModule);
					}
				}
				bsm.setServiceModule(baseServiceModules);
			}
		}
		return bsm;
	}
	
	/**
	 * 
	 * <p>Title ：convert</p>
	 * Description：		将实体Bean转换为客户端返回结果的POJO
	 * @param sme
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-12 上午09:33:22
	 */
	private BaseServiceModule convert(ServiceModuleEntity sme){
		BaseServiceModule bsm = new BaseServiceModule();
		bsm.setId(sme.getId());
		bsm.setName(sme.getName());
		bsm.setPrice(sme.getPrice().floatValue());
		bsm.setRef_url(sme.getRefUrl());
		bsm.setUrl(sme.getUrl());
		bsm.setRemarks(sme.getRemark());
		bsm.setStatus(sme.getStatusId().getValue());
		bsm.setCode(sme.getCode());
		bsm.setIcon_url(sme.getIconUrl());
		bsm.setType(sme.getType());
		return bsm;
	}

	/**
	 * 
	 * <p>Title ：getServiceModuleById</p>
	 * Description：		在一个ServiceModuleEntity集合中查找指定ID对象
	 * @param smes
	 * @param id
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-11 下午02:08:11
	 */
	private ServiceModuleEntity getServiceModuleById(List<ServiceModuleEntity> smes,String id){
		for(int i=0;i<smes.size();i++){
			ServiceModuleEntity sme = smes.get(i);
			if(id.equals(sme.getId())){
				return sme;
			}
		}
		return null;
	}


	@Override
	@Transactional
	public BaseServiceModuleList query(String id, final String name, final String status,int pageNo,int pageSize) throws ParameterException{
		BaseServiceModuleList moduleList = null;
		// 如果提供ID则，根据ID进行查询
		if(!StringUtils.isEmpty(id)){
			ServiceModuleEntity sme = this.serviceModuleDao.findById(id);
			if(null == sme){
				throw new ParameterException(ExceptionType.NOT_FOUND,ServiceModuleFieldNames.ID+":"+id);
			}
			moduleList = new BaseServiceModuleList();
			BaseServiceModuleListItem item[] = new BaseServiceModuleListItem[1];
			item[0] = new BaseServiceModuleListItem();
			item[0].setId(sme.getId());
			item[0].setLimit(sme.getUseLimit());
			item[0].setName(sme.getName());
			item[0].setPrice(sme.getPrice().floatValue());
			item[0].setStatus(sme.getStatusId().getValue());
			item[0].setType(sme.getType());
			moduleList.setCurrentCount(1);
			moduleList.setTotal(1);
			moduleList.setItems(item);
			return moduleList;
		}
		ModuleStatusEntity moduleStatus = null;
		if(!StringUtils.isEmpty(status)){
			moduleStatus = this.moduleStatusDao.findByValue(status);
		}
		final ModuleStatusEntity moduleStatusf=moduleStatus;
		
		Specification<ServiceModuleEntity> spec=new Specification<ServiceModuleEntity>() {

			@Override
			public Predicate toPredicate(Root<ServiceModuleEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> namepath=root.get("name");
				Path<String> statusispath=root.get("statusId").get("id");
				
				List<Predicate> prelist=new ArrayList<Predicate>();
				if(moduleStatusf!=null&&!StringUtils.isEmpty(status)){
					prelist.add(builder.equal(statusispath, moduleStatusf.getId()));
				}
				if(!StringUtils.isEmpty(name)){
					prelist.add(builder.like(namepath, "%"+name+"%"));
				}
				
				Predicate[] prearray=new Predicate[prelist.size()];
				prelist.toArray(prearray);
				query.where(prearray);
				return null;
			}
		};
		
		if(pageNo>=0){//分页
			PageRequest page=new PageRequest(pageNo, pageSize);
			Page<ServiceModuleEntity>  pageresult=this.serviceModuleDao.findAll(spec, page);
			if(pageresult!=null&&pageresult.getContent()!=null){
				moduleList = new BaseServiceModuleList();
				moduleList.setTotal((int)pageresult.getTotalElements());
				moduleList.setCurrentCount(pageresult.getContent().size());
				moduleList.setItems(entity2ListItem(pageresult.getContent()));
			}
		}else{//不分页
			List<ServiceModuleEntity> resultlist=this.serviceModuleDao.findAll(spec);
			if(resultlist!=null&&resultlist.size()>0){
				moduleList = new BaseServiceModuleList();
				moduleList.setTotal(resultlist.size());
				moduleList.setItems(entity2ListItem(resultlist));
			}
		}
		return moduleList;
	}
	/**
	 * 
	 * <p>Title ：entity2ListItem</p>
	 * Description：将服务模块的实体转换为查询列表的
	 * @param smes
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-18 上午09:43:44
	 */
	private List<BaseServiceModuleListItem> entity2ListItem(List<ServiceModuleEntity> smes){
		List<BaseServiceModuleListItem> items = null;
		if(smes != null){
			items = new ArrayList<BaseServiceModuleListItem>();
			for(ServiceModuleEntity sme : smes){
				BaseServiceModuleListItem item =  new BaseServiceModuleListItem();
				item.setId(sme.getId());
				item.setLimit(sme.getUseLimit()==null?10000:sme.getUseLimit());
				item.setName(sme.getName());
				item.setCode(sme.getCode());
				item.setType(sme.getType());
				ServiceModuleEntity parent = sme.getParent();
				if(null != parent){
					item.setPid(parent.getId());
				}
				item.setPrice(sme.getPrice().floatValue());
				item.setStatus(sme.getStatusId().getValue());
				items.add(item);
			}
		}
		return items;
	}
	
	
	public List<ServiceModuleEntity> queryAllByType(final String queryType, final String moduleType,final ModuleStatusEntity statusEntity){
		List<ServiceModuleEntity> resultlist=null;
		Specification<ServiceModuleEntity> spec=new Specification<ServiceModuleEntity>() {

			@Override
			public Predicate toPredicate(Root<ServiceModuleEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> prelist=new ArrayList<Predicate>();
				Path<ServiceModuleEntity> parentpath=root.get("parent");
				Path<String> typepath=root.get("type");
				Path<String> statusidpath=root.get("statusId").get("id");
				
				if(queryType.equals("0")){
					prelist.add(builder.isNull(parentpath));
				}
				
				if(queryType.equals("1")){
					prelist.add(builder.isNotNull(parentpath));
				}
				
				if(!StringUtils.isEmpty(moduleType)){
					prelist.add(builder.equal(typepath, moduleType));
				}
				
				if(statusEntity!=null){
					prelist.add(builder.equal(statusidpath, statusEntity.getId()));
				}
				
				Predicate[] prearray=new Predicate[prelist.size()];
				prelist.toArray(prearray);
				query.where(prearray);
				return null;
			}
		};
		
		Sort sort=new Sort(Direction.ASC,new String[]{"code"});
		resultlist=this.serviceModuleDao.findAll(spec, sort);
		return resultlist;
	}


	@Override
	public ServiceModuleByType[] queryAllByType(String queryType, String status) {
		ServiceModuleByType[] serviceModules = null;
		List<String> types = this.serviceModuleDao.getTypes();
		if (null != types && types.size() > 0) {
			serviceModules = new ServiceModuleByType[types.size()];
			ModuleStatusEntity statusEntity = null;
			// 如果名称不为空，则进行名称模糊搜索
			if(!StringUtils.isEmpty(status)){
				statusEntity = this.moduleStatusDao.findByValue(status);
			}
			for (int i = 0; i < types.size(); i++) {
				List<ServiceModuleEntity> list = queryAllByType(queryType, types.get(i),statusEntity);
				List<BaseServiceModuleListItem> items = entity2ListItem(list);
				BaseServiceModuleListItem[] itemsArr = null;
				if(null != items && items.size() > 0){
					itemsArr = new BaseServiceModuleListItem[items.size()];
					items.toArray(itemsArr);
				}
				serviceModules[i] = new ServiceModuleByType();
				serviceModules[i].setQueryType(queryType);
				serviceModules[i].setType(types.get(i));
				serviceModules[i].setModules(itemsArr);
			}
		}
		return serviceModules;
	}
	

}
