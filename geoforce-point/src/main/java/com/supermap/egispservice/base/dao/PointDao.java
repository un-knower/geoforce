package com.supermap.egispservice.base.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.entity.PointEntity;

public interface PointDao extends CrudRepository<PointEntity, String>,
		PagingAndSortingRepository<PointEntity, String>, JpaSpecificationExecutor<PointEntity> {

	/**
	 * 
	 * <p>Title ：queryByCreatetimeAndXYisNull</p>
	 * Description：	根据创建时间查询只有地址没有坐标的网点
	 * @param createTime
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-23 上午11:48:31
	 */
	@Query("SELECT t FROM PointEntity t where t.id in(:ids) and t.address is not null and t.status=2")
	public List<PointEntity> queryByCreatetimeAndXYisNull(@Param("ids") List<String> ids);
	/**
	 * 
	 * <p>Title ：queryByCreateTimeAndAddressIsNull</p>
	 * Description：		根据创建时间查询只有坐标没有地址的数据
	 * @param createTime
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-23 上午11:51:45
	 */
	@Query("SELECT t FROM PointEntity t where t.id in(:ids) and (t.address is null or t.address ='') and t.status=2")
	public List<PointEntity> queryByCreateTimeAndAddressIsNull(@Param("ids") List<String> ids);
	
	
	/**
	 * 
	 * <p>Title ：queryNormalPoints</p>
	 * Description：	查询状态为处理中的网点
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-1-7 下午03:49:58
	 */
	@Query("SELECT t FROM PointEntity t where t.id in(:ids) and t.status=2")
	public List<PointEntity> queryProcessPoints(@Param("ids") List<String> ids);
	
	@Query("SELECT t FROM PointEntity t where t.id=?1")
	public PointEntity findOneByPointid(String id);
	
	@Query(value = "SELECT p.id,p.name,p.smx,p.smy,p.duty_name,p.duty_phone,p.area_id,p.user_id,p.create_time,p.update_time,p.delete_flag,p.enterprise_id,p.department_id,p.address,p.status,p.net_pic_path,p.icon_style,p.groupid,p.styleid,pe.pointid,pe.userid,pe.col1,pe.col2,pe.col3,pe.col4,pe.col5,pe.col6,pe.col7,pe.col8,pe.col9,pe.col10,p.duty_pic_path,u.username  from biz_point p left join biz_point_extcolval pe on p.id=pe.pointid  left join  egisp_rss_user u on p.user_id=u.id where p.user_id=:userId order by p.create_time desc,p.update_time desc", nativeQuery = true)
	public List<?> queryAllPointAndExc(@Param("userId") String userId);
	
	/**
	 * 通过样式id查找网点列表
	 * @param styleid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-26下午4:20:24
	 */
	@Query(value="SELECT t.* FROM biz_point t where t.styleid=?1",nativeQuery=true)
	public List<PointEntity> findByStyleid(String styleid);
	
	/**
	 * 通过分组id查找网点列表
	 * @param groupid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-26下午4:28:14
	 */
	@Query(value="SELECT t.* FROM biz_point t where t.groupid=?1",nativeQuery=true)
	public List<PointEntity> findByGroupid(String groupid);
	
	/**
	 * 修改用户所有网点默认样式（无分组的）
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-26下午5:00:16
	 */
	@Transactional
	@Modifying
	@Query(value="update  biz_point p,biz_point_style s set p.styleid=null where p.user_id=?1 " +
			"AND p.styleid = s.id and p.groupid is null and p.styleid is not null " +
			"AND s.def1 = '1'",nativeQuery=true)
	public int updatePointDefaultStyle(String userid);
	
	/**
	 * 去掉网点与样式关联关系
	 * @param ids
	 * @return
	 * @Author Juannyoh
	 * 2015-11-26下午5:09:59
	 */
	@Transactional
	@Modifying
	@Query(value="update biz_point set styleid=null where id in(:ids)",nativeQuery=true)
	public int updatePointStylesToNUll(@Param("ids") List<String> ids);
	
	
	@Query(value="select * from biz_point where smx is not null and smy is not NULL and admincode is null order by  create_time desc",nativeQuery=true)
	public List<PointEntity> getNullAdmincodePoints();
	
	
	@Query(value="select id,smx,smy from biz_point where smx is not null and smy is not NULL and admincode is null order by  create_time desc",nativeQuery=true)
	public List<?> getNullAdmincodePoints2();
	
	
	@Transactional
	@Modifying
	@Query(value="update biz_point set admincode=?1,province=?2,city=?3,county=?4 where id =?5",nativeQuery=true)
	public int updateNullAdmincode(String admincode,String province,String city,String county,String id);


	@Query(value = "SELECT p.id,p.name,p.smx,p.smy,p.duty_name,p.duty_phone,p.area_id,p.user_id,p.create_time,p.update_time,p.delete_flag,p.enterprise_id,p.department_id,p.address,p.status,p.net_pic_path,p.icon_style,p.groupid,p.styleid,pe.pointid,pe.userid,pe.col1,pe.col2,pe.col3,pe.col4,pe.col5,pe.col6,pe.col7,pe.col8,pe.col9,pe.col10,p.duty_pic_path from biz_point p left join biz_point_extcolval pe on p.id=pe.pointid where p.user_id=:userId and (p.admincode like :admincode or p.admincode is null) order by p.create_time desc,p.update_time desc", nativeQuery = true)
	public List<?> queryAllPointAndExcByAdmincode(@Param("userId") String userId,@Param("admincode") String admincode);
	
	/**
	 * 同级部门及下级部门查找所有网点
	 * @param deptids
	 * @param admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-6-2下午3:50:32
	 */
	@Query(value = "SELECT p.id,p.name,p.smx,p.smy,p.duty_name,p.duty_phone,p.area_id,p.user_id,p.create_time,p.update_time,p.delete_flag,p.enterprise_id,p.department_id,p.address,p.status,p.net_pic_path,p.icon_style,p.groupid,p.styleid,pe.pointid,pe.userid,pe.col1,pe.col2,pe.col3,pe.col4,pe.col5,pe.col6,pe.col7,pe.col8,pe.col9,pe.col10,p.duty_pic_path,u.username from biz_point p left join biz_point_extcolval pe on p.id=pe.pointid  left join  egisp_rss_user u on p.user_id=u.id where p.department_id in(:deptids) and (p.admincode like :admincode or p.admincode is null) order by p.create_time desc,p.update_time desc", nativeQuery = true)
	public List<?> queryAllPointAndExcByDeptidsAdmincode(@Param("deptids") List<String> deptids,@Param("admincode") String admincode);
	
	
	@Query(value = "SELECT p.id,p.name,p.smx,p.smy,p.duty_name,p.duty_phone,p.area_id,p.user_id,p.create_time,p.update_time,p.delete_flag,p.enterprise_id,p.department_id,p.address,p.status,p.net_pic_path,p.icon_style,p.groupid,p.styleid,pe.pointid,pe.userid,pe.col1,pe.col2,pe.col3,pe.col4,pe.col5,pe.col6,pe.col7,pe.col8,pe.col9,pe.col10,p.duty_pic_path,u.username from biz_point p left join biz_point_extcolval pe on p.id=pe.pointid  left join  egisp_rss_user u on p.user_id=u.id where p.enterprise_id=:eid and (p.admincode like :admincode or p.admincode is null) order by p.create_time desc,p.update_time desc", nativeQuery = true)
	public List<?> queryAllPointAndExcByEidAdmincode(@Param("eid") String eid,@Param("admincode") String admincode);
	
	/**
	 * 全国时，按省份查询所有的网点数量
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-12-8下午2:13:20
	 */
	@Query(value="select substr(admincode, 1, 2) admincode_,count(*) sumcount FROM	biz_point where user_id =?1 GROUP BY  admincode_ ORDER BY  sumcount DESC",nativeQuery=true)
	public List<?> getPointCount2Porvice(String userid);
	
	/**
	 * 全国时，按省份查询所有的网点数量  -----上级部门查看下级部门网点
	 * @param deptids
	 * @return
	 * @Author Juannyoh
	 * 2016-6-2下午3:04:50
	 */
	@Query(value="select substr(admincode, :begin, :end) admincode_,count(*) sumcount FROM	biz_point where department_id in(:deptids) GROUP BY  admincode_ ORDER BY  sumcount DESC",nativeQuery=true)
	public List<?> getPointCount2PorviceByDeptIds(@Param("deptids")List<String> deptids,@Param("begin")int begin,@Param("end")int end);
	
	/**
	 * 全国时，按省份查询所有网点数量--企业下所有
	 * @param eid 企业id
	 * @param begin
	 * @param end
	 * @return
	 */
	@Query(value="select substr(admincode, :begin, :end) admincode_,count(*) sumcount FROM	biz_point where enterprise_id=:eid GROUP BY  admincode_ ORDER BY  sumcount DESC",nativeQuery=true)
	public List<?> getPointCount2PorviceByEid(@Param("eid")String eid,@Param("begin")int begin,@Param("end")int end);
	
	/**
	 * 按市、区聚集 -----上级部门查看下级部门网点
	 * @param deptids
	 * @param begin
	 * @param end
	 * @param admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3下午3:51:24
	 */
	@Query(value="select substr(admincode, :begin, :end) admincode_,count(*) sumcount FROM	biz_point where department_id in(:deptids) and (admincode like :admincode or admincode is null or admincode='') GROUP BY  admincode_ ORDER BY  sumcount DESC",nativeQuery=true)
	public List<?> getPointCount2CityByDeptIds(@Param("deptids")List<String> deptids,@Param("begin")int begin,@Param("end")int end,@Param("admincode")String admincode);
	
	/**
	 * 按市、区聚集 -----当前企业下所有的
	 * @param eid
	 * @param begin
	 * @param end
	 * @param admincode
	 * @return
	 */
	@Query(value="select substr(admincode, :begin, :end) admincode_,count(*) sumcount FROM	biz_point where enterprise_id=:eid  and (admincode like :admincode or admincode is null or admincode='') GROUP BY  admincode_ ORDER BY  sumcount DESC",nativeQuery=true)
	public List<?> getPointCount2CityByEid(@Param("eid")String eid,@Param("begin")int begin,@Param("end")int end,@Param("admincode")String admincode);
	
	
	/**
	 * 批量删除网点
	 * @param ids
	 * @return
	 * @Author Juannyoh
	 * 2015-12-8下午5:13:00
	 */
	@Transactional
	@Modifying
	@Query(value="delete from biz_point  where id in(:ids)",nativeQuery=true)
	public int deletePointsByIds(@Param("ids") List<String> ids);
	
	@Transactional
	@Modifying
	@Query(value="delete from biz_point_extcolval  where pointid in(:ids)",nativeQuery=true)
	public int deletePointsExtValByIds(@Param("ids") List<String> ids);
	
	/**
	 * 通过用户查找是否状态为处理中的网点
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-12-8下午5:31:57
	 */
	@Query(value="select id FROM biz_point where user_id =?1 and status=2 ",nativeQuery=true)
	public List<?> getAllProcessingPointByUserid(String userid);
	
	
	@Query(value = "SELECT p.id,p.name,p.smx,p.smy,p.duty_name,p.duty_phone,p.area_id,p.user_id,p.create_time,p.update_time,p.delete_flag,p.enterprise_id,p.department_id,p.address,p.status,p.net_pic_path,p.icon_style,p.groupid,p.styleid,pe.pointid,pe.userid,pe.col1,pe.col2,pe.col3,pe.col4,pe.col5,pe.col6,pe.col7,pe.col8,pe.col9,pe.col10,p.duty_pic_path,u.username from biz_point p left join biz_point_extcolval pe on p.id=pe.pointid left join  egisp_rss_user u on p.user_id=u.id where  p.department_id in(:deptids)  and ( p.admincode is null or p.admincode='' ) order by p.create_time desc,p.update_time desc", nativeQuery = true)
	public List<?> queryFailedPoints(@Param("deptids") List<String> deptids);
	
	/**
	 * 按企业查询所有的失败网点
	 * @param eid 企业ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Query(value = "SELECT p.id,p.name,p.smx,p.smy,p.duty_name,p.duty_phone,p.area_id,p.user_id,p.create_time,p.update_time,p.delete_flag,p.enterprise_id,p.department_id,p.address,p.status,p.net_pic_path,p.icon_style,p.groupid,p.styleid,pe.pointid,pe.userid,pe.col1,pe.col2,pe.col3,pe.col4,pe.col5,pe.col6,pe.col7,pe.col8,pe.col9,pe.col10,p.duty_pic_path,u.username from biz_point p left join biz_point_extcolval pe on p.id=pe.pointid left join  egisp_rss_user u on p.user_id=u.id where  p.enterprise_id=:eid   and ( p.admincode is null or p.admincode='' ) order by p.create_time desc,p.update_time desc", nativeQuery = true)
	public List queryFailedPointsByEid(@Param("eid")String eid);
	
	/**
	 * 根据用户id查询用户网点数量
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2016-2-26下午5:30:54
	 */
	@Query(value="select count(*) FROM biz_point where user_id =?1 ",nativeQuery=true)
	public BigInteger getPointCountsByUserid(String userid);
	
	/**
	 * 按条件查找网点数量
	 * @param deptids
	 * @param admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3上午11:10:41
	 */
	@Query(value="select count(p) from PointEntity p where p.departmentId in(:deptids) and p.admincode like :admincode")
	public long getCountsByAdminCodeAndDeptids(@Param("deptids")List<String> deptids,@Param("admincode")String admincode);
	
	
	/**
	 * APP端查询所有的网点数据，只要坐标信息和样式id
	 * @param deptids
	 * @param admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-6-6下午5:46:16
	 */
	@Query(value = "SELECT p.id,p.name,p.smx,p.smy,pg.styleid from biz_point p left join biz_point_group pg on p.groupid=pg.id  where p.department_id in(:deptids) and p.admincode like :admincode  order by p.create_time desc,p.update_time desc", nativeQuery = true)
	public List<?> queryAllPointXYForApp(@Param("deptids") List<String> deptids,@Param("admincode") String admincode);
	
	/**
	 * 根据大众版网点id查找网点，大众版网点id暂存于dutyname中
	 * @param cid
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2016-9-28下午2:26:03
	 */
	@Query(value="SELECT p.id from biz_point p where p.duty_name=:cid and p.user_id=:userid",nativeQuery=true)
	public List<?> findCpointByCidAndUserid(@Param("cid")String cid,@Param("userid")String userid);
}
