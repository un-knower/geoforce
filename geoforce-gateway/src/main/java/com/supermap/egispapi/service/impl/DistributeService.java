package com.supermap.egispapi.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.egispapi.pojo.DistributeParam;
import com.supermap.egispapi.service.IDistributeService;
import com.supermap.egispservice.base.pojo.DistributeAddress;
import com.supermap.egispservice.base.pojo.LogisticsAPIResult;
import com.supermap.egispservice.base.service.ILogisticsOrderService;
import com.supermap.egispservice.base.service.ILogisticsService;

@Component
public class DistributeService implements IDistributeService {

	@SuppressWarnings("unused")
	private static Logger LOGGER = Logger.getLogger(DistributeService.class);
	
	@Autowired
	private ILogisticsService logisticsService;
	
	@Autowired
	private ILogisticsOrderService orderService;
	
	
	@Override
	public List<LogisticsAPIResult> batchDistribute(DistributeParam param, String userId,String eid,String departId,boolean needAreaInfo) {
		List<DistributeAddress> addresses = param.getAddresses();
		List<LogisticsAPIResult> results = logisticsService.logisticsAPI_Area(addresses, param.getType(), userId, eid, departId,needAreaInfo);
		return results;
	}


	@Override
	public Map<String, Object> queryByBatch(String userId, String enterpriseId, String dcode, String batch,
			int pageNo, int pageSize) {
		return this.orderService.queryByBatch(userId, enterpriseId, dcode, batch, pageNo, pageSize);
	}

}
