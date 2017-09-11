package com.supermap.egispservice.lbs.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.PointCarForeign;

public interface PointCarForeignDao  extends CrudRepository<PointCarForeign, String>,
PagingAndSortingRepository<PointCarForeign, String>, JpaSpecificationExecutor<PointCarForeign>{

	/**
	 * 根据网点id查找网点与车辆的关系
	 * @param pointId
	 * @return
	 * @Author Juannyoh
	 * 2016-1-21上午9:52:00
	 */
	public List<PointCarForeign> findByPointId(String pointId);
	
	/**
	 * 根据车辆id查找网点与车辆关系
	 * @param carid
	 * @return
	 * @Author Juannyoh
	 * 2016-1-21上午9:52:23
	 */
	public List<PointCarForeign> findByCarId(String carid);
	
	/**
	 * 根据车辆id和网点id查询车辆网点关系
	 * @param pointid
	 * @param carid
	 * @return
	 * @Author Juannyoh
	 * 2016-1-21上午9:52:37
	 */
	public PointCarForeign findByPointIdAndCarId(String pointid,String carid);
   
}
