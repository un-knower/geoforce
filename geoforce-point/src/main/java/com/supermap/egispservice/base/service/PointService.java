package com.supermap.egispservice.base.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.entity.PointPicEntity;
import com.supermap.egispservice.base.pojo.ExportPointBean;

public interface PointService {

	/**
	 * 
	 * <p>Title ：add</p>
	 * Description：		添加网点
	 * @param name		网点名称
	 * @param smx
	 * @param smy
	 * @param dutyName	责任人
	 * @param dutyPhone	责任电话
	 * @param areaId	区域ID
	 * @param userId	用户ID
	 * @param enterpriseId	企业ID
	 * @param departmentId	部门ID
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-18 上午10:40:57
	 */
	public String add(String netPicPath,String dutyPicPath,String name,String address, BigDecimal smx, BigDecimal smy, String dutyName, String dutyPhone, String areaId,
			String userId, String enterpriseId, String departmentId,String iconStyle) throws Exception;
	/**
	 * 
	 * <p>Title ：updatePoint</p>
	 * Description：		更新网点信息
	 * @param pointEntity
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-18 下午02:28:05
	 */
	public boolean updatePoint(PointEntity pointEntity) throws Exception;
	
	/**
	 * 
	 * <p>Title ：deletePoint</p>
	 * Description：		删除网点
	 * @param id
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-18 上午11:28:10
	 */
	public boolean deletePoint(String id);
	
	/**
	 * 
	 * <p>Title ：queryById</p>
	 * Description：		根据ID查询网点
	 * @param id
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-18 上午11:29:22
	 */
	public PointEntity queryById(String id);
	
	
	public Map<String, Object> queryAllByPage(String userId, String name, String dutyName, String id,
			String enterpriseId, String departementId, int pageNo, int pageSize,String areaId);

	
	public List<PointEntity> queryByAreaId(String areaId,String enterpriseId,String departmentId);
	
	/**
	 * 
	 * <p>Title ：bindCar</p>
	 * Description：		绑定车辆
	 * @param netId
	 * @param carIds
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-19 上午10:46:42
	 */
	public boolean bindCar(String netId,String[] carIds);
	
	/**
	 * 
	 * <p>Title ：queryBindCar</p>
	 * Description：		查询绑定的车辆
	 * @param netId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-21 上午11:25:32
	 */
	public List<String> queryBindCar(String netId);
	
	
	/**
	 * 
	 * <p>Title ：importNetPoints</p>
	 * Description：		导入网点
	 * @param netPoint
	 * Author：Huasong Huang
	 * CreateTime：2014-10-23 上午10:38:36
	 */
	public Map<String,Object> importNetPoints(List<PointEntity> netPoint) throws Exception;
	
	public Map<String,Object> queryByAreaIdOrName(String areaId,String netName,String userId,String enterpriseId,String departmentId,int pageNo,int pageSize);
	/**
	 * 
	 * <p>Title ：startProcess</p>
	 * Description： 处理网点
	 * @param ids
	 * Author：Huasong Huang
	 * CreateTime：2015-1-8 下午04:04:21
	 */
	public void startProcess(List<String> ids);
	
	/**
	 * 通过样式id查找网点
	 * @param styleid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-26下午4:29:28
	 */
	public List<PointEntity> findByStyleid(String styleid);
	
	/**
	 * 通过分组id查找网点
	 * @param groupid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-26下午4:29:42
	 */
	public List<PointEntity> findByGroupid(String groupid);
	
	/**
	 * 修改用户网点默认样式（无分组的）
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-26下午5:01:38
	 */
	public int updatePointDefaultStyle(String userid);
	
	public int updatePointStylesToNUll(List<String> ids);
	
	/**
	 * 按省市区查询网点数据
	 * @param userId
	 * @param name
	 * @param dutyName
	 * @param id
	 * @param enterpriseId
	 * @param departementId
	 * @param pageNo
	 * @param pageSize
	 * @param areaId
	 * @param Admincode
	 * @return
	 * @Author Juannyoh
	 * 2015-12-8下午2:20:03
	 */
	public Map<String, Object> queryAllByAdmincode(String userId, String name, String dutyName, String id,
			String enterpriseId, String departementId, int pageNo, int pageSize,String areaId,String Admincode);

	public boolean startProcessOver(List<String> ids);
	
	/**
	 * 批量删除网点
	 * @param ids
	 * @return
	 * @Author Juannyoh
	 * 2015-12-8下午5:20:27
	 */
	public int deletePointsByIds(List<String> ids);
	
	/**
	 * 查找用户还有哪些状态为处理中的网点数据
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-12-8下午5:34:05
	 */
	public List<String> getAllProcessingPointByUserid(String userid);

	/**
	 * 查询用户所有定位失败的点
	 * @param userId
	 * @return
	 * @Author Juannyoh
	 * 2015-12-14下午4:10:40
	 */
	public Map<String, Object> queryFailedPoints(String userId);
	
	
	/**
	 * 保存网点图片
	 * @param pointPicEntity
	 * @return
	 * @Author Juannyoh
	 * 2016-2-22下午2:24:54
	 */
	public PointPicEntity savePointPicture(PointPicEntity pointPicEntity);
	
	/**
	 * 根据图片id删除图片
	 * @param picid
	 * @Author Juannyoh
	 * 2016-2-22下午2:26:23
	 */
	public void deletePointPictureByPicId(String picid);
	
	/**
	 * 根据网点id删除网点图片集
	 * @param pointid
	 * @Author Juannyoh
	 * 2016-2-22下午2:28:14
	 */
	public void deletePointPictureByPointid(String pointid);
	
	
	/**
	 * 根据网点id查到网点所有的图片
	 * @param pointid
	 * @return
	 * @Author Juannyoh
	 * 2016-2-22下午2:29:21
	 */
	public List<PointPicEntity> findPointPicturesByPointid(String pointid);
	
	/**
	 * 根据用户id，查找用户的网点数量
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2016-2-26下午5:32:20
	 */
	public int getPointCountsByUserid(String userid);
	
	
	/**
	 * 点聚集  是否APP
	 * @Author Juannyoh
	 * 2016-6-3上午11:40:14
	 */
	public Map<String, Object> queryAllByAdmincodeForConverge(String userId, String name,
			String groupid, String id, String enterpriseId,
			String dcode, int pageNo, int pageSize, String areaId,
			String Admincode ,boolean isApp);
	
	/**
	 * 点不聚集
	 * @return
	 * @Author Juannyoh
	 * 2016-6-3下午5:02:53
	 */
	public Map<String, Object> queryAllByAdmincodeForNoConverge(String userId, String name,
			String groupid, String id, String enterpriseId,
			String dcode, int pageNo, int pageSize, String areaId,
			String Admincode ,boolean isApp);

	/**
	 * 
	 * @param parammap
	 * @return
	 * @Author Juannyoh
	 * 2016-9-19下午2:01:29
	 */
	public List<ExportPointBean> queryAllForExport(Map<String,Object> parammap);
	
	/**
	 * 删除网点
	 * @param parammap
	 * @return
	 * @Author Juannyoh
	 * 2016-9-19下午5:13:46
	 */
	public int deletePoint(Map<String,Object> parammap);
	
	/**
	 * 查询将被删除的网点数量
	 * @param parammap
	 * @return
	 * @Author Juannyoh
	 * 2016-9-19下午5:13:54
	 */
	public int querydeletePointCount(Map<String,Object> parammap);
	
	/**
	 * 判断大众版网点id是否已存在产品中
	 * @param cid
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2016-9-28下午2:27:16
	 */
	public boolean isExsitCpoint(String cid,String userid);
	
	/**
	 * 路线规划中 起点、终点搜索网点时
	 * @param dcode
	 * @param eid
	 * @param groupname
	 * @param pointname
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @Author Juannyoh
	 * 2016-10-12下午3:31:45
	 */
	public List<ExportPointBean> queryPointForPathPlan(String dcode, String eid,
			String groupname,String pointname, int pageNo, int pageSize,String areaid );
	
	/**
	 * 根据网点编号查询网点
	 * @param dcode
	 * @param colkey
	 * @param colvalue
	 * @param eid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @Author Juannyoh
	 * 2016-10-12下午4:15:33
	 */
	public List<ExportPointBean> queryPointByColKeyForPathPlan(String dcode, String colkey, String colvalue,
			 String eid, int pageNo, int pageSize);
}
