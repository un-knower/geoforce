package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.OrderBaseEntity;

public interface ILogisticsOrderBaseDao extends CrudRepository<OrderBaseEntity, String>, PagingAndSortingRepository<OrderBaseEntity, String>, JpaSpecificationExecutor<OrderBaseEntity>{
	

	@Query("SELECT oe.id,oe.address,oe.batch,ofe.smx,ofe.smy FROM OrderBaseEntity oe,OrderFendanEntity ofe where oe.userId=:userId and oe.id=ofe.id and oe.orderStatusId in(:statusId) and ofe.areaId=:areaId and oe.batch like :batch escape :escchar")
	public List<Object[]> queryByAreaIdAndBatch(@Param("userId") String userId, @Param("statusId") List<Byte> statusId,
			@Param("batch") String batch, @Param("areaId") String areaId,@Param("escchar")String escchar);
	
	@Modifying
	@Query("update OrderBaseEntity t set t.orderStatusId=:status_id where t.id in(:ids)")
	public void updateOrderStatus(@Param("ids") List<String> ids,@Param("status_id") Byte status_id);
	
	@Query("SELECT max(oe.batch) FROM  OrderBaseEntity oe")
	public String queryMaxBatch();
	
	@Query("SELECT max(oe.batch) FROM  OrderBaseEntity oe where oe.userId=:userId")
	public String queryMaxBatchByUserId(@Param("userId")String userId);
	
	@Query("SELECT oe.id,oe.address,oe.batch,ofe.smx,ofe.smy,oe.startTime,oe.endTime FROM OrderBaseEntity oe,OrderFendanEntity ofe where oe.userId=:userId and oe.id=ofe.id and oe.orderStatusId in(:statusId) and ofe.smx>0 and ofe.smy>0 and oe.batch =:batch")
	public List<Object[]> queryByXYAndBatch(@Param("userId") String userId, @Param("statusId") List<Byte> statusId,@Param("batch") String batch);
	
	
	@Query("SELECT max(oe.batch) FROM  OrderBaseEntity oe where oe.userId=:userid")
	public String queryMaxBatchByUserid(@Param("userid") String userid);
	
	@Query("SELECT oe.id,oe.number,oe.address,oe.batch,oe.startTime,oe.endTime FROM OrderBaseEntity oe  where  oe.id in(:ids)  ORDER BY FIELD(id,:ids)")
	public List<Object[]> queryByIds(@Param("ids") List<String> ids);
	
}
