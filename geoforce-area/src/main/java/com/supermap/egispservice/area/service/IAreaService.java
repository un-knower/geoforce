package com.supermap.egispservice.area.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.PageQueryResult;
import com.supermap.egispservice.area.Point2D;
import com.supermap.egispservice.area.exceptions.AreaException;

public interface IAreaService {

	/**
	 * 
	 * <p>Title ：addArea</p>
	 * Description：		添加区域面
	 * @param areaName
	 * @param areaNumber
	 * @param net_id
	 * @param userId
	 * @param enterpriseId
	 * @param departmentId
	 * @param point2Ds
	 * @param wgzCode 网格组编码
	 * @param wgzName 网格组名称
	 * @param lineCode 线路编码
	 * @param lineName 线路名称
	 * @return  添加成功，返回该区域ID，否则为空或跑出异常
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-9-9 下午03:33:19
	 */
	public String addArea(String areaName, String areaNumber, String net_id,String admincode, String userId, String enterpriseId,
			String dcode, com.supermap.egispservice.area.Point2D[] point2Ds,String wgzCode,String wgzName,String lineCode,String lineName) throws AreaException;
	
	
	/**
	 * 
	 * <p>Title ：queryByIdOrNumber</p>
	 * Description：		查询某个企业划定的某个区域面，
	 * @param areaId
	 * @param areaNumber
	 * @param userId
	 * @param enterpriseId
	 * @param isNeedPoints
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-9-10 上午10:30:38
	 */
	public AreaEntity queryByIdOrNumber(String areaId,String areaNumber,String dcode,boolean isNeedPoints)throws AreaException;
	
	/**
	 * 
	 * <p>Title ：importUDB</p>
	 * Description：			导入UDB数据
	 * @param fileName
	 * @param userId
	 * @param enterpriseId
	 * @param dcode
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-12-10 下午04:36:43
	 */
	public  boolean importUDB(String fileName,String userId,String enterpriseId,String dcode)throws AreaException;
	
	
	/**
	 * 
	 * <p>Title ：queryByEnOrDe</p>
	 * Description：		通过企业ID或部门ID查询所有区域面
	 * @param areaName 
	 * @param areaNumber
	 * @Param enterpriseId
	 * @param departmentId
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-9-10 下午01:40:31
	 */
	public List<AreaEntity> queryByEnOrDe(String areaName,String number,String admincode,String enterpriseId,String dcode,boolean isNeedPoint) throws AreaException;
	
	/**
	 * 
	 * <p>Title ：updateAreaAttribution</p>
	 * Description：		更新区域属性
	 * @param id
	 * @param areaName
	 * @param areaNumber
	 * @param net_id
	 * @param userId
	 * @param enterpriseId
	 * @param departmentId
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-9-10 下午02:16:14
	 */
	public boolean updateAreaAttribution(String id,String areaName, String areaNumber, String net_id,String admincode,int areaStatus,
			String relationAreaid,String wgzCode,String wgzName,String lineCode,String lineName)throws AreaException;
	
	/**
	 * 
	 * <p>Title ：updateAreaRegion</p>
	 * Description：		更新区域面区域
	 * @param id
	 * @param point2Ds
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-9-10 下午04:36:46
	 */
	public boolean updateAreaRegion(String id,com.supermap.egispservice.area.Point2D[] point2Ds,List<Integer> parts)throws AreaException;
	
	/**
	 * 
	 * <p>Title ：deleteRegion</p>
	 * Description：		根据ID删除区域
	 * @param areaId
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-9-12 下午02:06:49
	 */
	public boolean deleteRegion(String areaId)throws AreaException;
	
	/**
	 * 
	 * <p>Title ：existAreaNameOrNumber</p>
	 * Description：		是否存在指定的区域或名称
	 * @param areaName
	 * @param areaNum
	 * @param enterpriseId
	 * @param dcode
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-9-12 下午02:10:11
	 */
	public void existAreaNameOrNumber(String areaName,String areaNum,String enterpriseId,String  dcode,String id)throws AreaException;
	
	
	/**
	 * 
	 * <p>Title ：bindNet</p>
	 * Description：		绑定网点
	 * @param areaId
	 * @param netId
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-9-16 上午11:20:16
	 */
	public boolean bindNet(String areaId,String netId)throws AreaException;
	
	/**
	 * 
	 * <p>Title ：queryAreaByPoint</p>
	 * Description：		通过点查找相交的面
	 * @param point
	 * @param enterpriseId
	 * @param dcode
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-23 下午01:51:54
	 */
	public AreaEntity queryAreaByPoint(Point2D point,String enterpriseId,String dcode)throws AreaException;
	
	/**
	 * 
	 * <p>Title ：queryAreaByPoint</p>
	 * Description：		批量点查找相关的面2
	 * @param point
	 * @param enterpriseId
	 * @param dcode
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-23 下午01:52:44
	 */
	public List<AreaEntity> queryAreaByPoint(Point2D[] point,String enterpriseId,String departmentId)throws AreaException;

	/**
	 * 
	 * <p>Title ：queryAreaByNamePage</p>
	 * Description：		根据区域名称进行模糊查询，
	 * @param userId
	 * @param areaName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-10-27 上午10:59:51
	 */
	public PageQueryResult queryAreaByNamePage(String userId,String areaName,int pageNo,int pageSize,boolean isNeedPoints) throws AreaException;
	
	/**
	 * 
	 * <p>Title ：mergeRegion</p>
	 * Description：			合并区域面
	 * @param areaIds		待合并区域面的名称
	 * @param decode		部门code
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-8 下午05:07:30
	 */
	public boolean mergeRegion(String[] areaIds,String dcode) throws AreaException;
	
	/**
	 * 
	 * <p>Title ：lineSplit</p>
	 * Description：		线拆分面
	 * @param line
	 * @param areaId
	 * @param dcode
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-8 下午05:11:02
	 */
	public boolean lineSplit(Point2D[] line,String areaId,String dcode) throws AreaException;
	
	
	/**
	 * 
	 * <p>Title ：regionSplit</p>
	 * Description：		面拆分
	 * @param regionPoints
	 * @param areaId
	 * @param dcode
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-12-10 上午09:49:58
	 */
	public boolean regionSplit(Point2D[] regionPoints,String areaId,String dcode)throws AreaException;
	/**
	 * 
	 * <p>Title ：exportUDB</p>
	 * Description：		到处UDB数据
	 * @param areaIds
	 * @param dcode
	 * @return
	 * @throws AreaException
	 * Author：Huasong Huang
	 * CreateTime：2014-12-11 上午11:27:07
	 */
	public String exportUDB(String[] areaIds,String dcode) throws AreaException;
	
	/**
	 * 导出udb数据，并转成字节数组
	 * @param areaIds
	 * @param dcode
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2016-9-30上午10:53:04
	 */
	public byte[] exportUDB2Byte(String[] areaIds,String dcode) throws AreaException;
	
	/**
	 * 按条件查询结果并保存成udb，转成字节数组
	 * @param admincode
	 * @param enterpriseId
	 * @param userids
	 * @param btime
	 * @param etime
	 * @param groupids
	 * @param isNeedPoint
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2016-12-21下午7:08:07
	 */
	public byte[] exportUDB2Byte(String admincode,String enterpriseId,String[] userids,Date btime,Date etime,String[] groupids,boolean isNeedPoint) throws AreaException;
	
	/**
	 * 首页，查询前10条区域面信息
	 * @param areaName
	 * @param areaNumber
	 * @param admincode
	 * @param enterpriseId
	 * @param dcode
	 * @param isNeedPoint
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2015-6-29上午10:16:22
	 */
	public List<AreaEntity> queryByEnOrDeTop10(String areaName,String areaNumber,String admincode,String enterpriseId, String dcode,boolean isNeedPoint) throws AreaException;

	/**
	 * 统计 区划量
	 * @param enterpriseId
	 * @param dcode
	 * @param admincode
	 * @param level
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2015-10-26下午1:44:09
	 */
	public List<Map<String,Object>> queryAreaCountByParm(String enterpriseId, String dcode,String admincode,String level,String bdate,String edate) throws Exception;

	/**
	 * 运营支撑查询区划信息 
	 * @Author Juannyoh
	 * 2016-9-21上午10:06:11
	 */
	public List<AreaEntity> queryByUseridsEboss(String admincode,String enterpriseId,String[] userids,Date btime,Date etime,String[] groupids,boolean isNeedPoint) throws AreaException;

	/**
	 * 批量删除区划
	 * @param areaId
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2016-9-21上午10:32:04
	 */
	public boolean deleteRegions(List<String> areaId)throws AreaException;
	
	
	/**
	 * 修改区划所属用户
	 * @param id
	 * @param userid
	 * @param eid
	 * @param dcode
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2016-10-20下午2:30:54
	 */
	public boolean updateAreaOwner(String id,String userid, String eid, String dcode)throws AreaException;

	/**
	 * 保存用户反选区 即按照城市边界裁剪出空白区域保存为一个区
	 * @param admincode
	 * @param levelStr
	 * @param userId
	 * @param enterpriseId
	 * @param dcode
	 * @return
	 * @Author Juannyoh
	 * 2016-10-25下午3:48:58
	 */
	public String saveReverseSelectionArea(String admincode,int levelStr,String userId, String enterpriseId,
			String dcode) throws AreaException;
	
	/**
	 * 按时间等条件查询区划明细
	 * @param areaName
	 * @param number
	 * @param admincode
	 * @param enterpriseId
	 * @param dcode
	 * @param isNeedPoint
	 * @param bdate
	 * @param edate
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2016-12-1下午2:49:40
	 */
	public List<AreaEntity> queryByEnOrDeAndDate(String areaName,String number,String admincode,String enterpriseId,String dcode,boolean isNeedPoint,String bdate,String edate) throws AreaException;

	/**
	 * 修改区划状态
	 * @param areaId
	 * @param status
	 * @return
	 * @throws AreaException
	 * @Author Juannyoh
	 * 2016-12-19上午9:38:13
	 */
	public boolean changeStatus(String areaId,int status)throws AreaException;
	
	
	/**
	 * 根据当前区划所关联的区划id查找其对应的区划属性（区划编号、区划名称）
	 * 如果关联的区划再有关联，则查到最后一个为止，依此类推。。。
	 * @param areaid
	 * @return
	 * @Author Juannyoh
	 * 2016-12-19上午9:53:51
	 */
	public List<Map<String,String>> findRelationAreaAttrs(String areaid,String relationareaid) ;
	
	
	/**
	 * 按名称精确查找区划信息
	 * @Author Juannyoh
	 * 2017-1-3下午4:22:14
	 */
	public List<AreaEntity> queryOneByName(String areaName,String number,String admincode,String enterpriseId,String dcode,boolean isNeedPoint) throws AreaException;
}
