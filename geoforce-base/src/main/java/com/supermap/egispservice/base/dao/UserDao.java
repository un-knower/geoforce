package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.UserEntity;

public interface UserDao extends CrudRepository<UserEntity, String>, PagingAndSortingRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

	UserEntity findById(String id);
	UserEntity findByEmail(String email);
	UserEntity findByUsername(String username);
	
	@Query("SELECT u  from UserEntity u where u.email=:email and u.eid.id=:eid")
	UserEntity findByEmailandEid(@Param("email")String email,@Param("eid")String eid);
	
	@Query("SELECT u  from UserEntity u where u.username=:username and u.eid.id=:eid")
	UserEntity findByUsernameandEid(@Param("username")String username,@Param("eid")String eid);
	
	@Query("SELECT u  from UserEntity u where u.deptId.id=:deptid and u.eid.id=:eid")
	List<UserEntity> findByUserDeptIdAndEidOrderByCreateTimeAsc(@Param("deptid")String deptId,@Param("eid")String eid);

	/**
	 * 企业员工查询菜单
	 */
	@Query(value = "SELECT item.id id,item.`name` menuName,item.pid pid,item.icon_url iconUrl,item.url url,item.status_id statusId,orderItem.use_time useTime,MAX(orderItem.deadline) deadLine,orders.func_names funcName,datediff(MAX(orderItem.deadline),now() ) as days,orderItem.isLogined,orderItem.id as orderItemId FROM EGISP_RSS_USER users,EGISP_RSS_ORDER orders,EGISP_RSS_ORDER_ITEMS orderItem,EGISP_RSS_SERVICE_MODULE item WHERE users.eid=:eid AND users.id=orders.user_id AND orders.id=orderItem.order_id and orderItem.module_id=item.id AND orderItem.deadline>:now AND orders.status_id IN (2,3) AND item.id IN (SELECT f.FUN_ID FROM INFO_ROLE_FUNCTION f WHERE f.ROLE_ID in (:roleIds) GROUP BY f.FUN_ID) GROUP BY item.id ORDER BY item.code ", nativeQuery = true)
	public List<Object[]> getMenu(@Param("eid") String eid, @Param("now") String now, @Param("roleIds") List<String> roleIds);

	/**
	 * 企业管理员查询菜单
	 */
	@Query(value = "SELECT item.id id,item.`name` menuName,item.pid pid,item.icon_url iconUrl,item.url url,item.status_id statusId,orderItem.use_time useTime,MAX(orderItem.deadline) deadLine,orders.func_names funcName,datediff(MAX(orderItem.deadline),now() ) as days,orderItem.isLogined,orderItem.id as orderItemId FROM EGISP_RSS_USER users,EGISP_RSS_ORDER orders,EGISP_RSS_ORDER_ITEMS orderItem,EGISP_RSS_SERVICE_MODULE item WHERE users.eid=:eid AND users.id=orders.user_id AND orders.id=orderItem.order_id and orderItem.module_id=item.id AND orderItem.deadline>:now AND orders.status_id in (2,3) GROUP BY item.id ORDER BY item.code ", nativeQuery = true)
	public List<Object[]> getMenuForEnterpriseManager(@Param("eid") String eid, @Param("now") String now);

	// public UserEntity findByUsername(String username);
	
	@Query("SELECT u  from UserEntity u where (u.username=:nameOrEmail or u.email=:nameOrEmail)")
	public List<UserEntity> findByNameOrEmail(@Param("nameOrEmail")String nameOrEmail);
	
	
	@Query(value = "SELECT u.id id from EGISP_RSS_USER u where ( u.username=?1 or u.email =?1 ) and u.eid=?2", nativeQuery = true)
	public List<String> findByUsername(String username, String eid);

	@Modifying
	@Query("DELETE FROM UserEntity t WHERE t.id IN (:ids)")
	void deleteByIds(@Param("ids") List<String> ids);
	
	@Modifying
	@Query(value = "UPDATE EGISP_RSS_ORDER_ITEMS  set isLogined='1' where id=?1",nativeQuery=true)
	void updateOrderItemLoginStatus(String id);
	
	/**
	 * 根据用户id查找订单表中订单状态是“正式审核通过”的订单数量
	 * 查最近一条订单状态
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-7-29下午2:10:48
	 */
	//@Query(value = "select count(*) from egisp_rss_order where status_id=1 and user_id=?1", nativeQuery = true)
	@Query(value = "select status_id from egisp_rss_order where user_id=?1 ORDER BY submit_time desc limit 1", nativeQuery = true)
	public byte findCountByUserStat(String userid);
	
	/**
	 * 根据企业ID查找顶级用户
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2016-6-2下午2:20:12
	 */
	@Query(value="select u from UserEntity u where u.eid.id=:eid and u.sourceId in('1','2') order by createTime")
	public List<UserEntity> findTopUserByEid(@Param("eid")String eid);
	
	/**
	 * 根据企业ID查找所有的用户
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2016-6-13下午5:17:49
	 */
	@Query(value="select u.id,u.realname,u.username from EGISP_RSS_USER u where u.eid=:eid ",nativeQuery=true)
	public List<?> findAlluserByEid(@Param("eid")String eid);
}
