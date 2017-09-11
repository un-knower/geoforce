package com.supermap.egispservice.base.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.supermap.egispservice.base.constants.EbossStatusConstants;
import com.supermap.egispservice.base.dao.OrderDao;
import com.supermap.egispservice.base.dao.OrderItemsDao;
import com.supermap.egispservice.base.dao.OrderStatusDao;
import com.supermap.egispservice.base.entity.OrderEntity;
import com.supermap.egispservice.base.entity.OrderItemsEntity;
import com.supermap.egispservice.base.entity.OrderStatusEntity;
import com.supermap.egispservice.base.entity.ServiceModuleEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseAccessInfo;
import com.supermap.egispservice.base.pojo.BaseOrderInfo;
import com.supermap.egispservice.base.pojo.BaseOrderInfoList;
import com.supermap.egispservice.base.pojo.BaseOrderInfoListItem;
import com.supermap.egispservice.base.pojo.BaseOrderItem;
import com.supermap.egispservice.base.pojo.OrderFieldNames;
import com.supermap.egispservice.base.pojo.UserInfoFieldNames;
import com.supermap.egispservice.base.service.IOrderService;
import com.supermap.egispservice.base.service.IServiceModuleService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.base.util.CommonUtil;
import com.supermap.egispservice.base.util.CommonUtil.DateType;


@Component("orderService")
public class OrderServiceImpl implements IOrderService {

	@Resource
	private OrderDao orderDao;
	
	@Resource
	private OrderItemsDao orderItemsDao;

	@Resource
	private UserService userService;

	@Resource
	private IServiceModuleService serviceModule;
	
	@Resource
	private OrderStatusDao orderStatusDao;

	private static Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);

	@Override
	@Transactional
	public void add(OrderEntity orderEntity, OrderItemsEntity[] orderItems) {
		for (OrderItemsEntity item : orderItems) {
			orderEntity.addOrderItem(item);
		}
		orderDao.save(orderEntity);
	}

	public IServiceModuleService getServiceModule() {
		return serviceModule;
	}

	public void setServiceModule(IServiceModuleService serviceModule) {
		this.serviceModule = serviceModule;
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		this.orderDao.delete(id);

	}

	@Transactional(readOnly = true)
	public OrderEntity queryById(String id, boolean isNeeedItems) {
		OrderEntity entity = this.orderDao.findById(id);
		if (isNeeedItems) {
			List<OrderItemsEntity> items = entity.getOrderItems();
			for (OrderItemsEntity item : items) {
				item.getId();
			}
		}
		return entity;
	}

	@Override
	@Transactional
	public String add(JSONObject orderObj) throws ParameterException {
		OrderEntity orderEntity = parseParameter(orderObj);
		List<OrderItemsEntity> items = orderEntity.getOrderItems();
		if(items.size() <= 0){
			throw new ParameterException(ExceptionType.NULL_NO_NEED,"orderItems");
		}
		this.orderDao.save(orderEntity);
		return orderEntity.getId();
	}

	/**
	 * 
	 * <p>
	 * Title ：parseParameter
	 * </p>
	 * Description： 将JSON参数转换为订单实体对象
	 * 
	 * @param orderObj
	 * @return Author：Huasong Huang CreateTime：2014-8-12 上午11:11:21
	 */
	private OrderEntity parseParameter(JSONObject orderObj) throws ParameterException {
		OrderEntity orderEntity = new OrderEntity();
		// 解析用户ID
		if (orderObj.containsKey(OrderFieldNames.USER_ID)) {
			String userId = orderObj.getString(OrderFieldNames.USER_ID);
			UserEntity ue = this.userService.findUserById(userId);
			if (null == ue) {
				throw new ParameterException(ParameterException.ExceptionType.NOT_FOUND, "userId:" + userId);
			}
			orderEntity.setUser(ue);
			orderEntity.setEid(ue.getEid()==null?"-1":ue.getEid().getId());
		} else {
			throw new ParameterException(ParameterException.ExceptionType.NULL_NO_NEED, OrderFieldNames.USER_ID);
		}
		// 设置状态
		OrderStatusEntity orderStatusEntity = this.orderStatusDao.findByValue(EbossStatusConstants.ORDER_STATUS_WAIT_AUDIT);
		if(null == orderStatusEntity){
			orderStatusEntity = new OrderStatusEntity();
			orderStatusEntity.setValue(EbossStatusConstants.ORDER_STATUS_WAIT_AUDIT);
			this.orderStatusDao.save(orderStatusEntity);
		}
		orderEntity.setStatus(orderStatusEntity);
		
		// remarks
		if (orderObj.containsKey("remarks")) {
			String remarks = orderObj.getString("remarks");
			if (!StringUtils.isEmpty(remarks)) {
				orderEntity.setRemarks(remarks);
			}
		}

		// 商讨价格
		if (orderObj.containsKey(OrderFieldNames.CONSULT_PRICE)) {
			String totalConsultPriceStr = orderObj.getString(OrderFieldNames.CONSULT_PRICE);
			if (!StringUtils.isEmpty(totalConsultPriceStr)) {
				float totalConsultPrice = Float.parseFloat(totalConsultPriceStr);
				orderEntity.setConsultSum(totalConsultPrice);
			}
		}
		// 订单类型
		if(orderObj.containsKey(OrderFieldNames.ORDER_TYPE)){
			String orderTypeStr = orderObj.getString(OrderFieldNames.ORDER_TYPE);
			if(!StringUtils.isEmpty(orderTypeStr)){
				int orderType = Integer.parseInt(orderTypeStr);
				if(orderType != EbossStatusConstants.ORDER_TYPE_CUSTOM){
					orderType = EbossStatusConstants.ORDER_TYPE_TRY;
				}
				orderEntity.setOrderType(orderType);
			}
		}

		if (orderObj.containsKey(OrderFieldNames.ORDER_ITEMS)) {
			JSONArray jsonArray = orderObj.getJSONArray(OrderFieldNames.ORDER_ITEMS);
			if (null != jsonArray) {
				for (int i = 0; i < jsonArray.size(); i++) {
					OrderItemsEntity itemEntity = new OrderItemsEntity();
					JSONObject jsonItem = jsonArray.getJSONObject(i);
					// 解析服务模块ID
					String moduleId = jsonItem.getString(OrderFieldNames.MODULE_ID);
					ServiceModuleEntity sme = serviceModule.queryById(moduleId, false);
					if (null == sme) {
						throw new ParameterException(ParameterException.ExceptionType.NOT_FOUND,
								OrderFieldNames.MODULE_ID + ":" + moduleId);
					}
//					// 如果是根模块，则只添加占位
//					ServiceModuleEntity parent = sme.getParent();
//					if(null == parent){
//						itemEntity.setConsultPrice(0);
//						itemEntity.setModuleEntity(sme);
//						itemEntity.setLimit(0);
//						orderEntity.addOrderItem(itemEntity);
//						continue;
//					}
					
					itemEntity.setModuleId(sme);
					itemEntity.setConsultPrice(sme.getPrice());
					itemEntity.setUseLimit(sme.getUseLimit());
					// 解析商讨价格
					if (jsonItem.containsKey(OrderFieldNames.CONSULT_PRICE)) {
						String consultPriceStr = jsonItem.getString(OrderFieldNames.CONSULT_PRICE);
						if (!StringUtils.isEmpty(consultPriceStr)) {
							float consultPrice = Float.parseFloat(consultPriceStr);
							itemEntity.setConsultPrice(new BigDecimal(consultPrice));
						}
					}
					// 使用限制
					if (jsonItem.containsKey(OrderFieldNames.USE_LIMIT)) {
						String useLimit = jsonItem.getString(OrderFieldNames.USE_LIMIT);
						if (!StringUtils.isEmpty(useLimit)) {
							int limit = Integer.parseInt(useLimit);
							itemEntity.setUseLimit(limit);
						}
					}
					// 开始使用时间
					if (jsonItem.containsKey(OrderFieldNames.USETIME)) {
						String useTimeStr = jsonItem.getString(OrderFieldNames.USETIME);
						if (!StringUtils.isEmpty(useTimeStr)) {
							Date useTime = CommonUtil.dateConvert(useTimeStr, CommonUtil.DateType.DATE_TYPE);
							itemEntity.setUseTime(useTime);
						} else {
							throw new ParameterException(ParameterException.ExceptionType.NULL_NO_NEED, OrderFieldNames.USETIME);
						}
					} else {
						throw new ParameterException(ParameterException.ExceptionType.NULL_NO_NEED, OrderFieldNames.USETIME);
					}
			
					// 解析使用截止时间
					if (jsonItem.containsKey(OrderFieldNames.DEADLINE)) {
						String deadline = jsonItem.getString(OrderFieldNames.DEADLINE);
						if (!StringUtils.isEmpty(deadline)) {
							Date deadlineDate = CommonUtil.dateConvert(deadline, CommonUtil.DateType.DATE_TYPE);
							itemEntity.setDeadline(deadlineDate);
						} else {
							throw new ParameterException(ParameterException.ExceptionType.NULL_NO_NEED, OrderFieldNames.DEADLINE);
						}
					} else {
						throw new ParameterException(ParameterException.ExceptionType.NULL_NO_NEED, OrderFieldNames.DEADLINE);
					}
					orderEntity.addOrderItem(itemEntity);
				}
			}
		}
		
		return orderEntity;
	}

	@Override
	@Transactional(readOnly = true)
	public BaseOrderInfo queryOrderDetails(String id) throws ParameterException {
		OrderEntity orderEntity = this.orderDao.findById(id);
		BaseOrderInfo boi = parseOrder(orderEntity);
		return boi;
	}

	private BaseOrderInfo parseOrder(OrderEntity orderEntity) {
		BaseOrderInfo baseOrderInfo = null;
		if (null != orderEntity) {
			baseOrderInfo = new BaseOrderInfo();
			// 解析用户ID和名称
			UserEntity userEntity = orderEntity.getUser();
			if (null != userEntity) {
				baseOrderInfo.setUserid(userEntity.getId());
				baseOrderInfo.setUserName(userEntity.getUsername());
				baseOrderInfo.setTelephone(userEntity.getTelephone());
				baseOrderInfo.setEmail(userEntity.getEmail());
				
			}
			Date commitTime = orderEntity.getSubmitTime();
			if (null != commitTime) {
				baseOrderInfo.setSubmitTime(CommonUtil.dataConvert(commitTime, DateType.TIMESTAMP));
			}
			
			baseOrderInfo.setOrderId(orderEntity.getId());
			baseOrderInfo.setConsultPrice(orderEntity.getConsultSum());
			baseOrderInfo.setTotalPrice(orderEntity.getTotalPrice());
			baseOrderInfo.setStatus(orderEntity.getStatus().getValue());
			baseOrderInfo.setRemarks(orderEntity.getRemarks());
			baseOrderInfo.setFuncNames(orderEntity.getFuncNames());
			baseOrderInfo.setOrderType(orderEntity.getOrderType());
			List<OrderItemsEntity> orderItems = orderEntity.getOrderItems();
			List<BaseOrderItem> baseOrderItems = new ArrayList<BaseOrderItem>();
			for (int i = 0; i < orderItems.size(); i++) {
				OrderItemsEntity orderItem = orderItems.get(i);
				if (null != orderItem) {
					BaseOrderItem baseOrderItem = new BaseOrderItem();
					baseOrderItem.setConsultPrice(orderItem.getConsultPrice().floatValue());
					baseOrderItem.setLimit(orderItem.getUseLimit());
					baseOrderItem.setId(orderItem.getId());
					Date useTime = orderItem.getUseTime();
					Date endTime = orderItem.getDeadline();
					baseOrderItem.setStartUseTime(CommonUtil.dataConvert(useTime, DateType.TIMESTAMP));
					baseOrderItem.setDeadline(CommonUtil.dataConvert(endTime, DateType.TIMESTAMP));
					baseOrderItem.setType(orderItem.getItem_type());
					ServiceModuleEntity sme = orderItem.getModuleId();
					if (sme != null) {
						baseOrderItem.setModuleId(sme.getId());
						baseOrderItem.setModuleName(sme.getName());
						ServiceModuleEntity parent = sme.getParent();
						if(null != parent){
							baseOrderItem.setpModuleId(parent.getId());
						}
					}
					baseOrderItems.add(baseOrderItem);
				}
			}
			baseOrderInfo.setOrderItems(baseOrderItems);
		}
		return baseOrderInfo;
	}

	@Override
	@Transactional
	public void updateOrderDetails(String id, String consultPriceStr, String remarks)
			throws ParameterException {
		OrderEntity orderEntity = this.orderDao.findById(id);
		if (null == orderEntity) {
			throw new ParameterException(ExceptionType.NOT_FOUND, OrderFieldNames.ORDER_ID + ":" + id);
		}
		float consultPrice = -1;
		boolean isNeedUpdate = false;
		try {
			// 修改商讨价格
			if (!StringUtils.isEmpty(consultPriceStr)) {
				consultPrice = Float.parseFloat(consultPriceStr);
				if (consultPrice != orderEntity.getConsultSum()) {
					orderEntity.setConsultSum(consultPrice);
					isNeedUpdate = true;
				}
			}
			// 修改备注
			if (!StringUtils.isEmpty(remarks) && !remarks.equals(orderEntity.getRemarks())) {
				orderEntity.setRemarks(remarks);
				isNeedUpdate = true;
			}
			if (!isNeedUpdate) {
				throw new ParameterException("未发现可更新的内容");
			}
			orderEntity.setUpdateTime(new Date());

		} catch (NumberFormatException e) {
			throw new ParameterException(ExceptionType.CONVERT_EXCEPTION, e.getMessage());
		}
	}

	@Override
	@Transactional
	public void updateOrderItem(String id, String useLimitStr, String consultPriceStr,String startUseTime,String deadline) throws ParameterException {
		OrderItemsEntity orderItem = this.orderItemsDao.findById(id);
		if (null == orderItem) {
			throw new ParameterException(ExceptionType.NOT_FOUND, OrderFieldNames.ORDER_ID + ":" + id);
		}

		try {
			boolean isUpdated = false;
			if (!StringUtils.isEmpty(useLimitStr)) {
				// 更新使用限制
				int useLimit = Integer.parseInt(useLimitStr);
				if (useLimit != orderItem.getUseLimit()) {
					orderItem.setUseLimit(useLimit);
					isUpdated = true;
				}
			}
			// 更新商谈价格同时要更新订单的总价格
			if (!StringUtils.isEmpty(consultPriceStr)) {
				float consultPrice = Float.parseFloat(consultPriceStr);
				if (consultPrice != orderItem.getConsultPrice().floatValue()) {
					orderItem.setConsultPrice(new BigDecimal(consultPrice));
					OrderEntity order = orderItem.getOrderId();
					if (null == order) {
						throw new ParameterException(ExceptionType.NOT_FOUND, "订单");
					}
					order.updateConsultPrice();
					isUpdated = true;
				}
			}
			// 更新开始使用时间
			if(!StringUtils.isEmpty(startUseTime)){
				Date useTime = CommonUtil.dateConvert(startUseTime, DateType.DATE_TYPE);
				orderItem.setUseTime(useTime);
				isUpdated = true;
			}
			// 更新截止使用时间
			if(!StringUtils.isEmpty(deadline)){
				Date endTime = CommonUtil.dateConvert(deadline, DateType.DATE_TYPE);
				orderItem.setDeadline(endTime);
				isUpdated = true;
			}
			if (!isUpdated) {
				throw new ParameterException(ParameterException.NO_DATA_UPDATED);
			}
		} catch (NumberFormatException e) {
			throw new ParameterException(ExceptionType.CONVERT_EXCEPTION, e.getMessage());
		}
	}
	
	@Transactional
	public void delelteOrderItem(String id) throws ParameterException{
		
		OrderItemsEntity itemEntity = this.orderItemsDao.findById(id);
		if(null == itemEntity){
			throw new ParameterException(ExceptionType.NOT_FOUND,OrderFieldNames.ORDER_ID+":"+id);
		}
		// 删除订单项
		this.orderItemsDao.delete(id);
		
		OrderEntity orderEntity = orderDao.findById(itemEntity.getOrderId().getId());
		if(orderEntity.getOrderItems().size() > 1){
			if(orderEntity.getTotalPrice() > itemEntity.getConsultPrice().floatValue()){
//				orderEntity.setConsultSum(orderEntity.getConsultSum() - itemEntity.getConsultPrice());
				orderEntity.setTotalPrice(orderEntity.getTotalPrice() - itemEntity.getConsultPrice().floatValue());
			}else{
				orderEntity.setTotalPrice(0);
			}
			
			orderEntity.removeOrderItemByID(id);
			
			// 清理订单项，删除只有父节点而没有子节点的订单项
			List<OrderItemsEntity> orderItemEntitys = orderEntity.getOrderItems();
			List<String> deleteIds = new ArrayList<String>();
			for(int i=0;i<orderItemEntitys.size();i++){
				OrderItemsEntity orderItemEntity = orderItemEntitys.get(i);
				// 只针对订单项类型为父节的订单进行扫描
				if(EbossStatusConstants.ITEM_TYPE_PARENT == orderItemEntity.getItem_type()){
					ServiceModuleEntity sme = orderItemEntity.getModuleId();
					ServiceModuleEntity psme = sme.getParent();
					if(psme == null){
						String pid = sme.getId();
						boolean hasChild = false;
						for (int j = 0; j < orderItemEntitys.size(); j++) {
							ServiceModuleEntity oie = orderItemEntitys.get(j).getModuleId();
							if (oie.getParent() != null && oie.getParent().getId().equals(pid)) {
								hasChild = true;
							}
						}
						if(!hasChild){
							deleteIds.add(orderItemEntity.getId());
						}
					}
				}
			}
			for(String deleteId : deleteIds){
				this.orderItemsDao.delete(deleteId);
				orderEntity.removeOrderItemByID(deleteId);
			}
			// 扫描剩下的节点是否只有ITEM_TYPE为AUTO的，如果是，则删除整个订单,否则更新订单
			List<OrderItemsEntity> leastItems = orderEntity.getOrderItems();
			if(null != leastItems && leastItems.size() > 1){
				boolean hasCommon = false;
				for(OrderItemsEntity ie : leastItems){
					if(ie.getItem_type() == EbossStatusConstants.ITEM_TYPE_COMMON){
						hasCommon = true;
						break;
					}
				}
				if(!hasCommon){
					this.orderDao.delete(orderEntity.getId());
				}else{
					this.orderDao.save(orderEntity);
				}
			}else{
				this.orderDao.delete(orderEntity.getId());
			}
		}else{
			this.orderDao.delete(orderEntity.getId());
		}
		
	}

	@Override
	@Transactional
	public void auditOrder(String id, String statusStr,String auditRemarks) throws ParameterException {
		OrderEntity orderEntity = this.orderDao.findById(id);
		if(null == orderEntity){
			throw new ParameterException(ExceptionType.NOT_FOUND,OrderFieldNames.ORDER_ID+":"+id);
		}
		OrderStatusEntity oldStatus = orderEntity.getStatus();
		OrderStatusEntity statusTemp = this.orderStatusDao.findByValue(statusStr);
		if(null == statusTemp){
			statusTemp = new OrderStatusEntity();
			statusTemp.setValue(statusStr);
			this.orderStatusDao.save(statusTemp);
		}
		
		if(!oldStatus.getValue().equals(statusStr)){
			orderEntity.setStatus(statusTemp);
			// 审核通过之后，需要修改订单项使用和截止时间
			if(statusTemp.getValue().equals(EbossStatusConstants.ORDER_STATUS_CUSTOM_PASS) || statusTemp.getValue().equals(EbossStatusConstants.ORDER_STATUS_TRY_PASS)){
				List<OrderItemsEntity> items = orderEntity.getOrderItems();
				try {
					updateAuditTime(items);
					orderEntity.setOrderItems(items);
				} catch (ParseException e) {
					throw new ParameterException("修改订单项使用时间失败");
				}
			}
		}
		
		if (!StringUtils.isEmpty(auditRemarks)) {
			orderEntity.setAuditRemarks(auditRemarks);
		}
		this.orderDao.save(orderEntity);
	}
	

	/**
	 * 
	 * <p>Title ：updateAuditTime</p>
	 * Description：		根据订单的审核时间修改订单项的使用时间
	 * @param orderItems
	 * @throws ParseException
	 * Author：Huasong Huang
	 * CreateTime：2014-11-13 下午01:48:43
	 */
	private void updateAuditTime(List<OrderItemsEntity> orderItems) throws ParseException {
		Calendar nowCal = Calendar.getInstance();
		Date nowDate = nowCal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long nowLong = sdf.parse(sdf.format(nowDate)).getTime();
		for (OrderItemsEntity orderItem : orderItems) {
			Date date = orderItem.getUseTime();
			long dateLong = sdf.parse(sdf.format(date)).getTime();
			long tempLong = (nowLong - dateLong) / (1000 * 3600 * 24);
			LOGGER.info("## 延迟审核天数 : " + tempLong);
			if (tempLong > 0) {
				// 修改开始使用时间
				Calendar startCal = Calendar.getInstance();
				startCal.setTime(date);
				startCal.add(Calendar.DAY_OF_MONTH, (int) tempLong);
				orderItem.setUseTime(startCal.getTime());
				// 修改结束使用时间
				Date endUserTime = orderItem.getDeadline();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(endUserTime);
				calendar.add(Calendar.DAY_OF_MONTH, (int) tempLong);
				orderItem.setDeadline(calendar.getTime());
			}
		}
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public BaseOrderInfoList queryOrderList(String id, final String info, final String statusStr, int pageNo, int pageSize)
			throws ParameterException {
		BaseOrderInfoList orderList = null;
		// 如果ID不为空，则进行ID查询
		if(!StringUtils.isEmpty(id)){
			OrderEntity oe = this.orderDao.findById(id);
			orderList = new BaseOrderInfoList();
			if(null != oe){
				BaseOrderInfoListItem item = new BaseOrderInfoListItem();
				item.setConsultPrice(oe.getConsultSum());
				item.setId(oe.getId());
				item.setStatus(oe.getStatus().getValue());
				item.setTotalAmount(oe.getTotalPrice());
				item.setUsername(oe.getUser().getUsername());
				item.setEmail(oe.getUser().getEmail());
				item.setOrderType(oe.getOrderType());
				item.setTelephone(oe.getUser().getTelephone());
				if(null != oe.getSubmitTime()){
					item.setSubmit_time(CommonUtil.dataConvert(oe.getSubmitTime(), DateType.TIMESTAMP));
				}
				orderList.setItems(new BaseOrderInfoListItem[]{item});
			}
			return orderList;
		}
		final OrderStatusEntity status = this.orderStatusDao.findByValue(statusStr);
		if(!StringUtils.isEmpty(statusStr) && status == null){
			return new BaseOrderInfoList(); 
		}
		
		//查询
		Specification<OrderEntity> spec=new Specification<OrderEntity>() {

			@Override
			public Predicate toPredicate(Root<OrderEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> statusidpath=root.get("status").get("id");
				Path<String> usermailpath=root.get("user").get("email");
				Path<String> usernamepath=root.get("user").get("username");
				Path<String> telephonepath=root.get("user").get("telephone");
				Path<String> mobilepath=root.get("user").get("mobilephone");
				
				List<Predicate> prelist=new ArrayList<Predicate>();
				
				if(!StringUtils.isEmpty(statusStr) && status != null){
					prelist.add(builder.equal(statusidpath,status.getId()));
				}
				
				if(!StringUtils.isEmpty(info)){
					List<Predicate> orprelist=new ArrayList<Predicate>();
					orprelist.add(builder.like(telephonepath, "%"+info+"%"));
					orprelist.add(builder.like(mobilepath, "%"+info+"%"));
					orprelist.add(builder.like(usernamepath, "%"+info+"%"));
					orprelist.add(builder.like(usermailpath, "%"+info+"%"));
					Predicate[] prearray=new Predicate[orprelist.size()];
					orprelist.toArray(prearray);
					prelist.add(builder.or(prearray));
				}

				Predicate[] prearray=new Predicate[prelist.size()];
				prelist.toArray(prearray);
				query.where(prearray);
				return null;
			}
		};
		
		if(pageNo>=0){//分页
			PageRequest page=new PageRequest(pageNo, pageSize, Direction.DESC, new String[]{"submitTime"});
			Page<OrderEntity> pageresult=this.orderDao.findAll(spec, page);
			if(pageresult!=null&&pageresult.getContent()!=null){
				orderList = new BaseOrderInfoList();
				orderList.setCurrentCount(pageresult.getContent().size());
				orderList.setItems(entity2Item(pageresult.getContent()));
				orderList.setTotal((int)pageresult.getTotalElements());
			}
		}
		else{//不分页
			List<OrderEntity> resultlist=this.orderDao.findAll(spec, new Sort(Direction.DESC, new String[]{"submitTime"}));
			if(resultlist!=null&&resultlist.size()>0){
				orderList = new BaseOrderInfoList();
				orderList.setItems(entity2Item(resultlist));
				orderList.setTotal(resultlist.size());
			}
		}
		return orderList;
	}
	
	private List<BaseOrderInfoListItem> entity2Item(List<OrderEntity> entitys){
		
		List<BaseOrderInfoListItem> items = null;
		if(null != entitys){
			items = new ArrayList<BaseOrderInfoListItem>();
			for(OrderEntity oe : entitys){
				BaseOrderInfoListItem item  = new BaseOrderInfoListItem();
				item.setConsultPrice(oe.getConsultSum());
				item.setId(oe.getId());
				item.setStatus(oe.getStatus().getValue());
				item.setTotalAmount(oe.getTotalPrice());
				item.setUsername(oe.getUser().getUsername());
				item.setEmail(oe.getUser().getEmail());
				item.setOrderType(oe.getOrderType());
				item.setTelephone(oe.getUser().getTelephone());
				if(null != oe.getSubmitTime()){
					item.setSubmit_time(CommonUtil.dataConvert(oe.getSubmitTime(), DateType.TIMESTAMP));
				}
				items.add(item);
			}
		}
		return items;
	}

	/**
	 * 门户添加订单
	 */
	@Override
	public String add(JSONObject orderObj, BaseAccessInfo userInfo)
			throws ParameterException {
		OrderEntity orderEntity = new OrderEntity();
		UserEntity ue = this.userService.findUserById(userInfo.getUserId());
		if(null == ue){
			throw new ParameterException(ExceptionType.NULL_NO_NEED,UserInfoFieldNames.ID);
		}
		orderEntity.setUser(ue);
		// 解析备注
		if(orderObj.containsKey(OrderFieldNames.REMARKS)){
			orderEntity.setRemarks(orderObj.getString(OrderFieldNames.REMARKS));
		}
		
		// 解析功能列表
		if(orderObj.containsKey(OrderFieldNames.FUNC_NAMES)){
			orderEntity.setFuncNames(orderObj.getString(OrderFieldNames.FUNC_NAMES));
		}
		// 解析商讨价格
		if(orderObj.containsKey(OrderFieldNames.CONSULT_PRICE)){
			orderEntity.setConsultSum(Float.parseFloat(orderObj.getString(OrderFieldNames.CONSULT_PRICE)));
		}
		// 解析状态
		OrderStatusEntity statusEntity = null;;
		statusEntity = this.orderStatusDao.findByValue(EbossStatusConstants.ORDER_STATUS_WAIT_AUDIT);
		orderEntity.setStatus(statusEntity);
		
		// 解析订单类型
		if(orderObj.containsKey(OrderFieldNames.ORDER_TYPE)){
			try {
				int orderType = orderObj.getInt(OrderFieldNames.ORDER_TYPE);
				if(orderType != EbossStatusConstants.ORDER_TYPE_TRY){
					orderType = EbossStatusConstants.ORDER_TYPE_CUSTOM;
				}
				orderEntity.setOrderType(orderType);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		
		orderEntity.setSubmitTime(new Date());
		
		if(!orderObj.containsKey("orderItems")){
			throw new ParameterException("缺少订单项");
		}
		JSONArray items = orderObj.getJSONArray("orderItems");
		for(int i=0;i<items.size();i++){
			JSONObject item = items.getJSONObject(i);
			OrderItemsEntity itemEntity = new OrderItemsEntity();
			String moduleId = item.getString(OrderFieldNames.MODULE_ID);
			ServiceModuleEntity moduleEntity = this.serviceModule.queryById(moduleId, false);
			if(null == moduleEntity){
				throw new ParameterException(ExceptionType.NOT_FOUND,OrderFieldNames.MODULE_ID+":"+moduleId);
			}
			itemEntity.setModuleId(moduleEntity);
			
			//	解析类型参数
			if(!item.containsKey(OrderFieldNames.ITEM_TYPE)){
				throw new ParameterException(OrderFieldNames.ITEM_TYPE+" 不允许为空");
			}
			int type = item.getInt(OrderFieldNames.ITEM_TYPE);
			itemEntity.setItem_type(type);
			
			// 解析使用限制
			int limit = 0;
			if(item.containsKey(OrderFieldNames.USE_LIMIT)){
				limit = item.getInt(OrderFieldNames.USE_LIMIT);
			}else{
				limit = moduleEntity.getUseLimit();
			}
			itemEntity.setUseLimit(limit);
			// 解析商讨价格
			float consultPrice = 0;
			if(item.containsKey(OrderFieldNames.CONSULT_PRICE)){
				consultPrice = Float.parseFloat(item.getString(OrderFieldNames.CONSULT_PRICE));
			}else{
				consultPrice = moduleEntity.getPrice().floatValue();
			}
			
			itemEntity.setConsultPrice(new BigDecimal(consultPrice));
			// 解析使用时间
			Date useTime = null;
			if(!item.containsKey(OrderFieldNames.USETIME)){
				useTime = new Date();
			}else{
				String useTimeStr = item.getString(OrderFieldNames.USETIME);
				useTime = CommonUtil.dateConvert(useTimeStr, DateType.DATE_TYPE);
			}
			itemEntity.setUseTime(useTime);
			// 解析截止使用时间
			Date deadline = null;
			if(!item.containsKey(OrderFieldNames.DEADLINE)){
				throw new ParameterException("截止使用时间不允许为空");
			}
			String deadlineStr = item.getString(OrderFieldNames.DEADLINE);
			deadline = CommonUtil.dateConvert(deadlineStr, DateType.DATE_TYPE);
			itemEntity.setDeadline(deadline);
			orderEntity.addOrderItem(itemEntity);
		}
		this.orderDao.save(orderEntity);
		return orderEntity.getId();
	}

	@Override
	public BaseOrderInfoList queryOrderList(BaseAccessInfo userInfo,
			String statusStr, int orderType, int pageSize, int pageNo)
			throws ParameterException {
		BaseOrderInfoList infoList = new BaseOrderInfoList();
		UserEntity ue = this.userService.findUserById(userInfo.getUserId());
		OrderStatusEntity statusEntity = null;
		if(!StringUtils.isEmpty(statusStr)){
			statusEntity = this.orderStatusDao.findByValue(statusStr);
		}
		
		if(null != ue){
			Map<String,Object> result=null;
			if(pageNo>=0){
				PageRequest page=new PageRequest(pageNo, pageSize);
				result=findByUserAndStatusAndOrderType(ue.getId(), statusEntity, orderType, page);
			}
			else{
				result=findByUserAndStatusAndOrderType(ue.getId(), statusEntity, orderType, null);
			}
			if(null!=result){
				infoList.setCurrentCount(Integer.parseInt(result.get("CurrentCount").toString()));
				infoList.setTotal(Integer.parseInt(result.get("Total").toString()));
				infoList.setInfos(result.get("Infos")==null?null:parse((List<OrderEntity>)result.get("Infos")));
			}
		}
		return infoList;
	}
	
	private List<BaseOrderInfo> parse(List<OrderEntity> orders){
		List<BaseOrderInfo> infos = new ArrayList<BaseOrderInfo>();
		for(int i=0;i<orders.size();i++){
			BaseOrderInfo orderInfo = parseOrder(orders.get(i));
			infos.add(orderInfo);
		}
		return infos;
	}
	
	@Transactional(readOnly=true)
	public Map<String,Object> findByUserAndStatusAndOrderType(final String userid,
			final OrderStatusEntity status,final int orderType,Pageable page){
		Map<String,Object>  result=null;
		Specification<OrderEntity> spec=new Specification<OrderEntity>() {

			@Override
			public Predicate toPredicate(Root<OrderEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> useridpath=root.get("user").get("id");
				Path<String> statusidpath=root.get("status").get("id");
				Path<String> orderTypepath=root.get("orderType");
				List<Predicate> prelist=new ArrayList<Predicate>();
				if(!StringUtils.isEmpty(userid)){
					prelist.add(builder.equal(useridpath, userid));
				}
				if(null!=status){
					prelist.add(builder.equal(statusidpath, status.getId()));
				}
				
				if(-1 != orderType){
					prelist.add(builder.equal(orderTypepath,orderType));
				}
				
				Predicate[] prea=new Predicate[prelist.size()];
				prelist.toArray(prea);
				query.where(prea);
				return null;
			}
		};
		
		if(null!=page){
			Page<OrderEntity> pageresult=this.orderDao.findAll(spec, page);
			if(pageresult!=null&&pageresult.getContent()!=null){
				result=new HashMap<String,Object>();
				result.put("CurrentCount", pageresult.getContent().size());
				result.put("Total", pageresult.getTotalElements());
				result.put("Infos",pageresult.getContent());
			}
		}else{
			List<OrderEntity> list=this.orderDao.findAll(spec);
			if(list!=null&&list.size()>0){
				result=new HashMap<String,Object>();
				result.put("CurrentCount", list.size());
				result.put("Total", list.size());
				result.put("Infos", list);
			}
		}
		return result;
	}
	
}
