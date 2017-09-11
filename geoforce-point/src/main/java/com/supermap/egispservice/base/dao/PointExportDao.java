package com.supermap.egispservice.base.dao;

import java.util.Date;
import java.util.List;

public interface PointExportDao {
	
	/**
	 * 查询所有网点
	 * @param eid
	 * @param childuserid
	 * @param btime
	 * @param etime
	 * @param groupids
	 * @param status
	 * @param admincode
	 * @return
	 * @Author Juannyoh
	 * 2016-9-19上午9:42:52
	 */
	public List queryAllPoint(String eid,List<String> childuserid,Date btime,Date etime,List<String> groupids,String status,String admincode);

	public int deletePoint(String eid,List<String> childuserid,Date btime,Date etime,List<String> groupids,String status,String admincode);
	
	public int querydeletePointCount(String eid,List<String> childuserid,Date btime,Date etime,List<String> groupids,String status,String admincode);

	public List findPointByGroupname(List<String> deptids,String eid,String groupname,String pointname,String areaid, int pageNo, int pageSize);
	
	public List findPointByExtCols(String colkey,String colvalue,List<String> deptids,String eid, int pageNo, int pageSize);
}
