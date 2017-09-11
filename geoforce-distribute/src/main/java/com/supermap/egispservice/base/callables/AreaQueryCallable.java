package com.supermap.egispservice.base.callables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.Point2D;
import com.supermap.egispservice.area.service.IAreaService;

public class AreaQueryCallable implements Callable<List<AreaEntity>> {
	
	private String enterpriseId;
	private String dcode;
	private Point2D[] point2Ds;
	private String threadNo;
	private static Logger LOGGER = Logger.getLogger(AreaQueryCallable.class);
	private IAreaService areaService;
	
	

	public AreaQueryCallable(String enterpriseId, String dcode, Point2D[] point2Ds,String threadNo) {
		this.enterpriseId = enterpriseId;
		this.dcode = dcode;
		this.point2Ds = point2Ds;
		this.threadNo = threadNo;
	}




	@Override
	public List<AreaEntity> call() throws Exception {
		LOGGER.info("## thread no [" + threadNo + "] start to addressMatch...[count," + this.point2Ds.length + "]");
		long start = System.currentTimeMillis();
		List<AreaEntity> areas = null;
		try {
//			areas = areaService.queryAreaByPoint(point2Ds, enterpriseId, dcode);
			areas = areaService.queryAreaByPoint(point2Ds, enterpriseId, null);
		} catch (Exception e) {
			LOGGER.error("## threadNo ["+threadNo+"] exception : "+e.getMessage(),e);
			areas = buildAreaQueryTimeoutResult(this.point2Ds.length);
		}finally{
			LOGGER.info("## threadNo ["+threadNo+"] consume time["+(System.currentTimeMillis() - start)+"]");
		}
		return areas;
	}

	private List<AreaEntity> buildAreaQueryTimeoutResult(int size) {

		List<AreaEntity> result = new ArrayList<AreaEntity>();
		for (int i = 0; i < size; i++) {
			AreaEntity dmar = new AreaEntity();
			result.add(dmar);
		}
		return result;
	}


	public void setAreaService(IAreaService areaService) {
		this.areaService = areaService;
	}

	
	
}
