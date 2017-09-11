package com.supermap.egispservice.base.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.CorrectAddressEntity;

public interface ICorrectAddressDao extends CrudRepository<CorrectAddressEntity, String>, JpaSpecificationExecutor<CorrectAddressEntity> {
	
	@Modifying
	@Query("delete from CorrectAddressEntity t where t.id=:id and t.userId=:userId")
	public void deleteCorrectAddressByIdAndUserId(@Param("id")String id,@Param("userId")String userId);
	
	
	/**
	 * 通过地址查找纠错记录(同一个企业下)
	 * @param address
	 * @return
	 * @Author Juannyoh
	 * 2017-1-10下午2:38:12
	 */
	public List<CorrectAddressEntity> findByAddressAndEid(String address,String eid);
	
	
	
	/**
	 * 根据地址查找已纠错的地址列表数据（同一企业下）
	 * @param address
	 * @param eid
	 * @param status
	 * @return
	 * @Author Juannyoh
	 * 2017-1-10下午3:23:59
	 */
	public List<CorrectAddressEntity> findByAddressAndEidAndStatusOrderByAddTimeDesc(String address,String eid,int status);
	
	
	/**
	 * 根据企业ID查找有没有该企业的纠错库数据
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2017-1-11下午6:09:23
	 */
	@Query("select count(*) from CorrectAddressEntity where eid=:eid")
	public long queryCountByEid(@Param("eid")String eid);
}
