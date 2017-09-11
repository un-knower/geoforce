package com.supermap.egispservice.lbs.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.Region;

public interface RegionDao  extends CrudRepository<Region, String>,
PagingAndSortingRepository<Region, String>, JpaSpecificationExecutor<Region>{

	/**
	 * 根据id查找围栏
	 * @param id
	 * @return
	 * @Author Juannyoh
	 * 2016-1-28上午10:54:08
	 */
     public Region findByid(String id);
     
     /**
      * 查看围栏名称是否重复
      * @param name
      * @param eid
      * @return
      * @Author Juannyoh
      * 2016-1-28上午10:54:26
      */
     public List<Region> findByNameAndEid(String name,String eid);
     
    
}
