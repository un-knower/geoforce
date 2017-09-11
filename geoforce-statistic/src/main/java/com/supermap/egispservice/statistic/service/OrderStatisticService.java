package com.supermap.egispservice.statistic.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.supermap.egispservice.statistic.dao.IAreaQueryDao;
import com.supermap.egispservice.statistic.dao.IOrderStatisticDao;
import com.supermap.egispservice.statistic.util.AreaFieldNames;

@Service
public class OrderStatisticService implements IOrderStatisticService {

	@Autowired
	private IOrderStatisticDao orderStatisticDao;
	
	@Autowired
	private IAreaQueryDao areaQueryDao;
	
	private Logger LOGGER=Logger.getLogger(OrderStatisticService.class);

	@Override
	@SuppressWarnings("rawtypes")
	public List getOrderCountStatistic(List<String> deptIdList, Date startDate,
			Date endDate) {
		return orderStatisticDao.getOrderCountGroupByAreaId(deptIdList,
				startDate, endDate);
	}

	@Override
	public List getOrderCountStatisticTop10(List<String> deptIdList,
			Date startDate, Date endDate) {
		return orderStatisticDao.getOrderCountGroupByAreaIdTop10(deptIdList,
				startDate, endDate);
	}

	@Override
	public List getOrderCountByAdminCode(List<String> deptIdList,
			String admincode, String level, Date startDate, Date endDate) {
		return orderStatisticDao.queryOrderCountByAdminCode(deptIdList, admincode, level, startDate, endDate);
	}

	@Override
	public List queryAllOrderGroupByReultType(
			List<String> deptIdList, Date startDate, Date endDate,String eid) {
		if(endDate==null&&startDate==null){//如果开始日期、结束日期均为空，则统计近一个月数据
			endDate=new Date();
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.MONTH, -1);
			startDate=calendar.getTime();
		}else if(endDate==null&&startDate!=null){//如果结束日期是空，则把截止日期设为当天
			endDate=new Date();
		}else if(endDate!=null&&startDate==null){//结束日期不为空，开始日期为空，默认结束日期往前推一个月
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.MONTH, -1);
			startDate=calendar.getTime();
		}
		return this.orderStatisticDao.queryAllOrderGroupByReultType(deptIdList, startDate, endDate,eid);
	}

	@Override
	public List getOrderCountByAdminCode(List<String> deptIdList,
			String admincode, String level, Date startDate, Date endDate,
			int resulttype,String eid) {
		if(endDate==null&&startDate==null){//如果开始日期、结束日期均为空，则统计近一个月数据
			endDate=new Date();
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.MONTH, -1);
			startDate=calendar.getTime();
		}else if(endDate==null&&startDate!=null){//如果结束日期是空，则把截止日期设为当天
			endDate=new Date();
		}else if(endDate!=null&&startDate==null){//结束日期不为空，开始日期为空，默认结束日期往前推一个月
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.MONTH, -1);
			startDate=calendar.getTime();
		}
		return this.orderStatisticDao.queryOrderCountByAdminCode(deptIdList, admincode, level, startDate, endDate, resulttype,eid);
	}

	@Override
	public Map<String,Object> queryOrderDetailByAdminCode(List<String> deptIdList,
			String admincode, String level, Date startDate, Date endDate,
			int resulttype, int pageNumber, int pageSize,
			String ordernum,String orderbatch,String address,String etpid) {
		Map<String,Object> resultmap=null;
		
		if(endDate==null&&startDate==null){//如果开始日期、结束日期均为空，则统计近一个月数据
			endDate=new Date();
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.MONTH, -1);
			startDate=calendar.getTime();
		}else if(endDate==null&&startDate!=null){//如果结束日期是空，则把截止日期设为当天
			endDate=new Date();
		}else if(endDate!=null&&startDate==null){//结束日期不为空，开始日期为空，默认结束日期往前推一个月
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.MONTH, -1);
			startDate=calendar.getTime();
		}
		List resultlist= this.orderStatisticDao.queryOrderCountByAdminCodeDetail(deptIdList, admincode, level, startDate, endDate, resulttype, pageNumber, pageSize,
				ordernum,orderbatch,address,etpid);
		//有区划的时候，需要区划名称
		if(resultlist!=null&&resultlist.size()>0){
			for(int i=0;i<resultlist.size();i++){
				Map<String,Object> map=(Map<String, Object>) resultlist.get(i);
				map.put("rowid", i+1);
				String eid=map.get("eid")==null?null:map.get("eid").toString();
				int resulttypes=map.get("resulttype")==null?0:Integer.parseInt(map.get("resulttype").toString());
				if(resulttypes==1){//分单成功，有区划
					int ordertype=Integer.parseInt(map.get("ordertype").toString());
					String areaid=map.get("areaid")==null?null:map.get("areaid").toString();
					Map<String,String> areamap=null;
					if(ordertype==2){//分单api
						if(StringUtils.isNotEmpty(areaid)){//此时areaid是区划名称
							map.put("areaname", areaid);
							areamap=this.areaQueryDao.findAreaByNameAndEid(areaid,eid);
						}
					}else{//导入的订单
						if(StringUtils.isNotEmpty(areaid)){
//							String name=this.areaQueryDao.queryNameByIdOrNumber(areaid, null, null);
//							map.put("areaname", name);
							areamap=findFirstRelationAreaAttrs(areaid);
							map.put("areaname", areamap==null?"":areamap.get(AreaFieldNames.NAME));
						}
					}
					if(areamap!=null){
						String areaStatus=areamap.get(AreaFieldNames.AREA_STATUS);
						String relationAreaid=areamap.get(AreaFieldNames.RELATION_AREAID);
						map.put("areastatus", areaStatus);
						if(StringUtils.isNotEmpty(areaStatus)&&areaStatus.equals("1")){//如果是停用状态，则去查询关联的区划
							if(StringUtils.isNotEmpty(relationAreaid)){
								map.put(AreaFieldNames.RELATION_AREAID, relationAreaid);
								List<Map<String,String>> relationlist=findRelationAreaAttrs(areaid,relationAreaid);
								if(relationlist!=null&&relationlist.size()>0){
									map.put("relation_areaname", relationlist.get(relationlist.size()-1).get("NAME"));
									map.put("relation_areanum", relationlist.get(relationlist.size()-1).get("AREA_NUM"));
								}else{
									map.put("relation_areaname","");
									map.put("relation_areanum","");
								}
							}else{
								map.put("relation_areaname","");
								map.put("relation_areanum","");
							}
						}else{
							map.put("relation_areaname","");
							map.put("relation_areanum","");
						}
					}else{
						map.put("areaname", "");
						map.put("areastatus", "-1");//无
						map.put("relation_areaname","");
						map.put("relation_areanum","");
					}
				}else{
					map.put("areaname", "");
					map.put("areastatus", "-1");//无
					map.put("relation_areaname","");
					map.put("relation_areanum","");
				}
			}
		}
				
		if(resultlist!=null&&resultlist.size()>0){
			int sumcount=this.orderStatisticDao.queryOrderCountByAdminCodeDetailCount(deptIdList, admincode, level, startDate, endDate, resulttype, ordernum, orderbatch, address,etpid);
			resultmap=new HashMap<String,Object>();
			if(pageNumber>0&&pageSize>0){
				resultmap.put("page", pageNumber);
				resultmap.put("records", sumcount);
				resultmap.put("rows", resultlist);
				resultmap.put("total", sumcount%pageSize==0?sumcount/pageSize:(sumcount/pageSize+1));
			}
			else{
				resultmap.put("rows", resultlist);
			}
		}
		return resultmap;
	}
	
	
	/**
	 * 查找关联区划信息
	 * @param areaid
	 * @param relationareaid
	 * @return
	 */
	public List<Map<String, String>> findRelationAreaAttrs(String areaid,String relationareaid){
		List<Map<String, String>> resultlist=null;
		if(StringUtils.isEmpty(relationareaid)){
			return null;
		}
		try {
			resultlist=findRelationAreaAttrsUConn(areaid,relationareaid);
		} catch (Exception e) {
			LOGGER.info("查找关联的区划信息失败："+e.getMessage());
			return null;
		}
		return resultlist;
	}
	
	public List<Map<String, String>> findRelationAreaAttrsUConn(String areaid,String relationareaid){
		List<Map<String, String>> resultlist=null;
		if(StringUtils.isEmpty(relationareaid)){
			return null;
		}
		try {
			Map<String, String>  first=this.findFirstRelationAreaAttrs(relationareaid);
			if(first!=null&&!StringUtils.isEmpty(first.get(AreaFieldNames.ID))){
				if(StringUtils.isNotEmpty(areaid)&&StringUtils.isNotEmpty(first.get(AreaFieldNames.RELATION_AREAID))&&first.get(AreaFieldNames.RELATION_AREAID).equals(areaid)){//如果查出来互相关联了，则不返回结果
					return null;
				}
				resultlist=new ArrayList<Map<String,String>>();
				resultlist.add(first);
				if(first.get(AreaFieldNames.AREA_STATUS).equals("1")&&!StringUtils.isEmpty(first.get(AreaFieldNames.RELATION_AREAID))){
					resultlist.addAll(findRelationAreaAttrsUConn(first.get(AreaFieldNames.ID),first.get(AreaFieldNames.RELATION_AREAID)));
				}
			}
			
		} catch (Exception e) {
			LOGGER.info("查找关联的区划信息失败："+e.getMessage());
			return null;
		}
		return resultlist;
	}
	
	
	/**
	 * 获取区划信息（区划ID,编号，名称，区划状态，关联区划ID）
	 * @param relationAreaid
	 * @return
	 */
	public Map<String,String> findFirstRelationAreaAttrs(String relationAreaid){
		return this.areaQueryDao.findFirstRelationAreaAttrs(relationAreaid);
	}

}
