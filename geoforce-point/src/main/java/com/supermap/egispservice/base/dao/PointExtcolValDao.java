package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.entity.PointExtcolValEntity;

public interface PointExtcolValDao extends CrudRepository<PointExtcolValEntity,String>,JpaSpecificationExecutor<PointExtcolValEntity>{
	
	/**
	 * 根据网点编号查找网点的所有扩展字段
	 * @param pointid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-18上午10:13:49
	 */
	public List<PointExtcolValEntity> findByPointid(String pointid);
	
	/**
	 * 根据用户id查找网点扩展
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-26下午3:31:50
	 */
	public List<PointExtcolValEntity> findByUserid(String userid);
	
	
	@Modifying
	@Query("delete from  PointExtcolValEntity t where t.pointid =?1")
	void deleteByPointId(String pointid);
	
	/**
	 * 通过用户id查找扩展字段数据
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-12-10下午5:00:29
	 */
	@Query(value="select  t.pointid,t.userid,t.col1,t.col2,t.col3,t.col4,t.col5,t.col6,t.col7,t.col8,t.col9,t.col10  from  biz_point_extcolval t where t.userid =?1",nativeQuery=true)
	public List findExtvolByUserid(String userid);
	
	
	@Query(value="select t.pointid,t.userid,t.col1,t.col2,t.col3,t.col4,t.col5,t.col6,t.col7,t.col8,t.col9,t.col10 from  biz_point_extcolval t where t.pointid =?1",nativeQuery=true)
	public List findExtvolByPointid(String pointid);
}
