package com.supermap.egispweb.action.region;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.bean.RegionSetRegionBean;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.AlarmType;
import com.supermap.lbsp.provider.hibernate.lbsp.Region;
import com.supermap.lbsp.provider.hibernate.lbsp.RegionSet;


//@Controller
public class RegionSetAction extends BaseAction {
	static Logger logger = Logger.getLogger(RegionSetAction.class.getName());
	@RequestMapping(value="/com/supermap/toRegion")
	public String toRegion(HttpServletRequest request,HttpServletResponse response){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		List<AlarmType>  queryAlarmType=alarmTypeConsumer.queryAlarmType(hm);
		request.setAttribute("lists", queryAlarmType);
		logger.info("to regionList.jsp");
		return "region/regionList";
		
	}
	/**
	 * 围栏查询
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/regionList")
	@ResponseBody
	public Page getRegionist(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String eid=userEntity.getEid().getId();
		String name = request.getParameter("name");
		String type = request.getParameter("alarmType");
		String status=request.getParameter("status");
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(eid)){
			hm.put("eid", eid);
		}
		if(StringUtils.isNotBlank(type)){
			hm.put("alarmType", Integer.parseInt(type));
		}
		if(StringUtils.isNotBlank(name)){
			hm.put("name", name);
		}
		if(StringUtils.isNotBlank(status)){
			hm.put("status", status);
		}
			Page page = getPage(request);
			page=regionSetConsumer.pagequeryRegionSet(page, hm);
			return page;
	}
	/**
	 * 编辑围栏经纬度方法
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/updateRgionLanLen")
	@ResponseBody
	public HashMap<String,Object> updateRgionLanLen(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String eid=userEntity.getEid().getId();
		HashMap<String,Object> m = new HashMap<String, Object>();
		String lanLen = request.getParameter("lanLen");
		if(lanLen == null || "".equals(lanLen)){
			return m;
		}
		String name = request.getParameter("name");
		if(name == null || "".equals(name)){
			return m;
		}
		List<RegionSetRegionBean> list=null;
		Region region =null;
		HashMap<String,Object> h = new HashMap<String, Object>();
		h.put("eid",eid);
		h.put("regionName", name);
		list=regionSetConsumer.queryRegionSet(null, h);
		if(list!=null && !list.isEmpty()){
			region=list.get(0).getRegion();
			}
		else {
			m.put("flag", "no");
			return m;
			}
		region.setRegion(lanLen);
		int i=regionConsumer.updateRegion(region);
		m.put("flag", "ok");
		return m;	
		
	}
	
	/**
	 * 添加车辆围栏报警设置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/addRgionSet")
	@ResponseBody
	public AjaxResult addRgionSet(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return new AjaxResult((short)1,"请用户登录");
		}
		String eid=userEntity.getEid().getId();
		HashMap<String,Object> m = new HashMap<String, Object>();
		String region = request.getParameter("region");
		
		if(region == null || "".equals(region)){
			return new AjaxResult((short)0,"请绘制围栏");
		}
		String name = request.getParameter("name");
		if(name == null || "".equals(name)){
			return new AjaxResult((short)0,"请输入围栏名称");
		}
		String type = request.getParameter("type");
		if(type == null|| "".equals(type) ){
			return new AjaxResult((short)0,"请选择围栏类型");
		}
		String status = request.getParameter("status");
		if(status == null||"".equals(status)){
			return new AjaxResult((short)0,"请选择围栏状态");
		}
		String staTime = request.getParameter("staTime");
		if(staTime == null||"".equals(staTime)){
			return new AjaxResult((short)0,"请输入开始时间");
		}
		String endTime = request.getParameter("endTime");
		if(endTime == null||"".equals(endTime)){
			return new AjaxResult((short)0,"请输入结束时间");
		}
		String wookTIime=request.getParameter("wookTIime");
		if(wookTIime == null||"".equals(wookTIime)){
			return new AjaxResult((short)0,"请选择有效时间");
		}
		List<RegionSetRegionBean> list=null;
		HashMap<String,Object> h = new HashMap<String, Object>();
		h.put("eid",eid);
		h.put("regionName", name);
		list=regionSetConsumer.queryRegionSet(null, h);
		if(list!=null && !list.isEmpty()){
			return new AjaxResult((short)0,"围栏已经存在");
		}
		String remark=request.getParameter("remark");
		String userId=userEntity.getId();
		String deptId=userEntity.getDeptId().getCode();
		Region r=new Region();
		Short i=Short.parseShort(status);
		r.setStatus(i);
		r.setRegion(region);
		r.setIsShare(i);
		r.setName(name);
		r.setUserId(userId);
		r.setDeptId(deptId);
		r.setEid(eid);
		r.setRemark(remark);
		r.setEid(eid);
		r.setSource(i);
		//围栏报警设置
		RegionSet rs=new RegionSet();
		rs.setDeptId(deptId);
		rs.setUrserId(userId);
		rs.setWeek(wookTIime);
		rs.setStartTime(staTime);
		rs.setEndTime(endTime);
		rs.setModuleType(Short.parseShort("1"));
		//得到当前时间
		rs.setOperDate(new Date());
		rs.setAlarmType(Integer.parseInt(type));
		Region returnR;
		try {
			returnR = regionConsumer.addRegion(r);
		if(null!=returnR){
			rs.setRegionId(returnR.getId());
			regionSetConsumer.addRegionSet(rs);
			}
			return new AjaxResult((short)1,"操作成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
		
	} 
	/**
	 * 车辆围栏报警设置查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/regionInfo")
	@ResponseBody
	public RegionSetRegionBean getRegionInfo(HttpServletRequest request,HttpServletResponse response){
		RegionSetRegionBean rer=new RegionSetRegionBean();
		String regionId = request.getParameter("regionId");
		RegionSet rs= regionSetConsumer.getRegionSet(regionId);
		if(rs == null){
			return null;
		}
		String rid=rs.getRegionId();
		if(rid==null && "".equals(rid)){
			return null;
		}
		Region r;
		try {
			r = regionConsumer.getRegion(rid);
			if(r == null){
				return null;
			}
			rer.setRegion(r);
			rer.setRegionSet(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rer;
	}
	
	
	/**
	 * 修改围栏
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/supermap/editRegionMessage")
	@ResponseBody
	public AjaxResult updateRgionSet(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws Exception{
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return new AjaxResult((short)1,"请用户登录");
		}
		String eid=userEntity.getEid().getId();
		String name = request.getParameter("name");
		if(name == null || "".equals(name)){
			return new AjaxResult((short)0,"请输入围栏名称");
		}
		String type = request.getParameter("type");
		if(type == null|| "".equals(type) ){
			return new AjaxResult((short)0,"请选择围栏类型");
		}
		String status = request.getParameter("status");
		if(status == null||"".equals(status)){
			return new AjaxResult((short)0,"请选择围栏状态");
		}
		String staTime = request.getParameter("staTime");
		if(staTime == null||"".equals(staTime)){
			return new AjaxResult((short)0,"请输入开始时间");
		}
		String endTime = request.getParameter("endTime");
		if(endTime == null||"".equals(endTime)){
			return new AjaxResult((short)0,"请输入结束时间");
		}
		String wookTIime=request.getParameter("wookTIime");
		if(wookTIime == null||"".equals(wookTIime)){
			return new AjaxResult((short)0,"请选择有效时间");
		}
		String remark=request.getParameter("remark");
		String regionSetId=request.getParameter("regionSetId");
		List<RegionSetRegionBean> list=null;
		HashMap<String,Object> h = new HashMap<String, Object>();
		h.put("regionSetId", regionSetId);
		h.put("eid",eid);
		h.put("regionName", name);
		list=regionSetConsumer.queryRegionSet(null, h);
		if(list!=null && !list.isEmpty()){
			return new AjaxResult((short)0,"围栏已经存在");
		}
		RegionSet rs=regionSetConsumer.getRegionSet(regionSetId);
		Region r=regionConsumer.getRegion(rs.getRegionId());
		//得到创建用户id
		String userId=userEntity.getId();
		String depeCode=userEntity.getDeptId().getCode();
		r.setName(name);
		r.setDeptId(depeCode);
		r.setUserId(userId);
		r.setRemark(remark);
		Short i=Short.parseShort(status) ;
		r.setStatus(i);
		//围栏报警设置
		rs.setDeptId(depeCode);
		rs.setWeek(wookTIime);
		rs.setStartTime(staTime);
		rs.setEndTime(endTime);
		rs.setUrserId(userId);
		//得到当前时间
		rs.setOperDate(new Date());
		rs.setAlarmType(Integer.parseInt(type));
		regionSetConsumer.upadateRegionSet(rs);
		regionConsumer.updateRegion(r);
		return new AjaxResult((short)1,"操作成功");
	}
	/**
	 * 删除围栏
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/supermap/delRegion")
	@ResponseBody
	public HashMap<String, Object> delRegion(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//得到ids
		String regionSetid=request.getParameter("regionSetids");
		String[] ids = regionSetid.split(",");
		List<String> idList = Arrays.asList(ids);
		//得到每一个id
		int deleteRegion=0;
		int deleteRegionSet=0;
		for(String id:idList){
			//得到围栏设置对象
			RegionSet rs=regionSetConsumer.getRegionSet(id);
			//得到围栏id
			String regionId=rs.getRegionId();
			if(null==regionId && "".equals(regionId)){
				return null;
			}
			//得到围栏对象
			Region r=regionConsumer.getRegion(regionId);
			//删除
			deleteRegionSet=regionSetConsumer.delRegionSet(rs);
			deleteRegion=regionConsumer.delRegion(r);
		}
		//返回
		HashMap<String,Object> msg = new HashMap<String, Object>();
		if(deleteRegion==1||deleteRegion==1){
			msg.put("flag", "ok");
			return msg;
		}
		return null;
	}
}
