package com.supermap.egispservice.base.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.SysDefaultCityEntity;


public interface SysDefaultCityDao extends CrudRepository<SysDefaultCityEntity, String> ,
PagingAndSortingRepository<SysDefaultCityEntity, String>, JpaSpecificationExecutor<SysDefaultCityEntity>{

	/**
	 * 通过用户id查找默认城市
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-23下午3:01:18
	 */
	List<SysDefaultCityEntity> findByUserid(String userid);
	
	/**
	 * 修改用户默认城市
	 * @param eid
	 * @param deptid
	 * @param clevel
	 * @param admincode
	 * @param defaultname
	 * @param province
	 * @param city
	 * @param county
	 * @param updateTime
	 * @param userid
	 * @Author Juannyoh
	 * 2015-11-23下午3:01:40
	 */
	@Modifying
	@Query("update SysDefaultCityEntity set eid=?1,deptid=?2,clevel=?3,admincode=?4,defaultname=?5," +
			"province=?6,city=?7,county=?8,updateTime=?9 where userid =?10")
	int updateDefaultCity(String eid,String deptid,String clevel,String admincode,String defaultname,
			String province,String city,String county,Date updateTime,String userid);
	
	
	
}
