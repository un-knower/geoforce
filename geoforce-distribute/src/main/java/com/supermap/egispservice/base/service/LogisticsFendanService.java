package com.supermap.egispservice.base.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.StringUtils;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egispservice.base.constants.StatusConstants;
import com.supermap.egispservice.base.dao.ILogisticsOrderBaseDao;
import com.supermap.egispservice.base.dao.ILogisticsOrderFendanDao;
import com.supermap.egispservice.base.entity.OrderFendanEntity;
import com.supermap.egispservice.base.util.BeanTool;

@Service("fendanService")
public class LogisticsFendanService implements ILogisticsFendanService {

	@Autowired
	private ILogisticsOrderFendanDao fendanDao;
	
	@Autowired
	private ILogisticsOrderBaseDao orderBaseDao;
	
	@Override
	@Transactional
	public void update(OrderFendanEntity ofe) {
		OrderFendanEntity fendanEntity = this.fendanDao.findOne(ofe.getId());
		if(null != fendanEntity){
			BeanUtils.copyProperties(ofe, fendanEntity, BeanTool.getNullPropertyNames(ofe));
			this.fendanDao.save(fendanEntity);
		}
	}

	private static Logger LOGGER = Logger.getLogger(LogisticsFendanService.class);
	
	/**
	 * 根据区域ID和批次查询分单成功的订单
	 */
	public Map<String,Object> queryByBatchAndArea(final String userId,final String areaId,final String batch,int pageNo,int pageSize){
		List<Byte> statusList = new ArrayList<Byte>();
		statusList.add(StatusConstants.ORDER_BASE_LOGISTICS_AUTO_SUCCESS);
		statusList.add(StatusConstants.ORDER_BASE_LOGISTICS_MANUAL_SUCCESS);
		statusList.add(StatusConstants.ORDER_BASE_LOGISTICS_AUTO_FAIL);
		statusList.add(StatusConstants.ORDER_BASE_LOGISTICS_MANUAL_FAIL);
		//String batchValue = "%" + batch.replaceAll("_", "/_").replaceAll("%", "/%") + "%";
		String batchValue = null;
		
		if(StringUtils.isNullOrEmpty(batch)){//传的批次为空时，查最近一个批次
			String maxBatch = orderBaseDao.queryMaxBatchByUserid(userId);//查找用户最大的批次
			batchValue=maxBatch;
		}else {
			batchValue=batch;
		}
		
		LOGGER.info("## filter : " + batchValue);
		
		//List<Object[]> queryResults = this.orderBaseDao.queryByAreaIdAndBatch(userId, statusList,batchValue, areaId,"/");
		List<Object[]> queryResults = this.orderBaseDao.queryByXYAndBatch(userId, statusList,batchValue);
		
		//oe.id,oe.address,oe.batch,ofe.smx,ofe.smy
		Map<String,Object> result = new HashMap<String,Object>();
		if(queryResults != null && queryResults.size() > 0){
			int totalCount = queryResults.size();
			LOGGER.info("## 查询到结果："+totalCount);
			int pageCount = totalCount / pageSize;
			if(totalCount % pageSize > 0){
				pageCount ++;
			}
			if(pageNo > pageCount){
				pageNo = pageCount;
			}
			int startIndex = (pageNo -1)*pageSize;
			int endIndex = startIndex + pageSize;
			if(endIndex > queryResults.size()){
				endIndex = queryResults.size();
			}
			queryResults = queryResults.subList(startIndex, endIndex);
			List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
			for(Object[] o:queryResults){
				String id = (String)o[0];
				String address = (String)o[1];
				String lot = (String)o[2];
				BigDecimal x = (BigDecimal)o[3];
				BigDecimal y = (BigDecimal)o[4];
				
				String start=(String)o[5];
				String end=(String)o[6];
				
				com.supermap.entity.Point p = new com.supermap.entity.Point(x.doubleValue(),y.doubleValue());
				p = SuperMapCoordinateConvertImpl.smMCToLatLon(p);
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("id", id);
				item.put("lot", lot);
				item.put("addr",address);
				item.put("lon", p.getLon());
				item.put("lat", p.getLat());
				item.put("start", start);
				item.put("end", end);
				results.add(item);
			}
			result.put("total", pageCount);
			result.put("page", pageNo);
			result.put("records", totalCount);
			result.put("rows", results);
		}else{
			result.put("total", 0);
			result.put("page", 0);
			result.put("records", 0);
		}
		return result;
	}
	
	
	
}
