package com.supermap.egispweb.action.statistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.egispweb.action.OrderAction;

//@Controller
//@RequestMapping(value = "/statistic/area")
public class AreaStatisticsAction {
	
	private final static Logger LOGGER = Logger.getLogger(OrderAction.class);
	
	@Autowired
	private IAreaService areaService;

	@Autowired
	private IGeocodingService geocodingService;
	
	
	@RequestMapping(value = "/show")
	public String show() {
		return "statistic/regionStatistic";
	}
	
	/**
	 * 区划量统计
	 * @param session
	 * @param admincode
	 * @param level
	 * @return
	 * @Author Juannyoh
	 * 2015-10-26下午4:21:54
	 */
	@RequestMapping("getAreaCountByParm")
	@ResponseBody
	public Map<String, Object> getAreaCountByParm(HttpSession session,
			String admincode, String level,@RequestParam(value="start")String bdate,@RequestParam(value="end")String edate) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> result=new HashMap<String,Object>();
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String eid=user.getEid().getId();
			String dcode=user.getDeptId().getCode();
			int adminlevel=1;
			
			if(admincode==null||admincode.equals("")){
				admincode=null;
			}
			if(level==null||level.equals("")){
				level=null;
			}
			
			//转换admincode
			if(admincode!=null&&level!=null&&!admincode.equals("")&&!level.equals("")){
				admincode=getAdmincode(admincode,Integer.parseInt(level));
			}
			
			List<Map<String,Object>> countlist=this.areaService.queryAreaCountByParm(eid, null, admincode, level,bdate,edate);
			
			
			String key=null;
			if(level==null||level.equals("")){
				key="PROVINCE";
			}else if(level.equals("1")){
				//直辖市
				if(admincode!=null&&!admincode.equals("")&&(
						admincode.indexOf("110")==0||admincode.indexOf("120")==0
						||admincode.indexOf("500")==0||admincode.indexOf("310")==0))
				{
					key="COUNTY";
					adminlevel=3;
				}else{
					key="CITY";
					adminlevel=2;
				}
			}else if(level.equals("2")){
				key="COUNTY";
				adminlevel=3;
			}
			
			if(countlist!=null&&countlist.size()>0){
				for(Map<String,Object> map:countlist){
					String code=(String) map.get("admincode");
					String tempcode=code;
					if(code.equals("500000")){//重庆市特殊处理
						tempcode="500100";
					}
					String name=(String) this.geocodingService.getCountyByAdmincode(tempcode).get(key);
					map.put("name", name);
				}
			}
			
			//所有城市列表查询
			List<Map<String,Object>> adminlist =new ArrayList<Map<String,Object>>();
			if(admincode==null||admincode.equals("")){
				adminlist = this.geocodingService.getAdminElement(null, adminlevel);
			}else{
				adminlist = this.geocodingService.getAdminElement(admincode, adminlevel);
			}
			
			//对比,没有数据的写0
			List<Map<String,Object>> resultlist =countlist;
			for(Map<String,Object> adminmap:adminlist){//所有城市列表
				String adminmapcode=(String) adminmap.get("ADMINCODE");
				adminmapcode = getAdmincode(adminmapcode,adminlevel);
				Map<String,Object> newmap=new HashMap<String,Object>();
				boolean flag=false;
				if(countlist!=null&&countlist.size()>0){//有数据的城市列表
					for(Map<String,Object> map:countlist){
						flag=false;
						String mapcode=(String) map.get("admincode");
						if(adminmapcode.equals(mapcode)){
							flag=true;
							break;
						}else{
							continue;
						}
					}
					if(!flag){
						newmap.put("count", 0);
						newmap.put("admincode", adminmapcode);
						newmap.put("name", adminmap.get(key));
						resultlist.add(newmap);
					}
				}else{
					newmap.put("count", 0);
					newmap.put("admincode", adminmapcode);
					newmap.put("name", adminmap.get(key));
					resultlist.add(newmap);
				}
			}
			
			if (null != resultlist) {
				result = buildResult(null, resultlist, true);
			}
		} catch (Exception e) {
			result = buildResult(e.getMessage(), null, false);
		}
		return result;
	}
	
	
	
	private Map<String, Object> buildResult(String info, Object result,
			boolean isSuccess) {
		Map<String, Object> resultObj = new HashMap<String, Object>();
		resultObj.put("info", info);
		resultObj.put("result", result);
		resultObj.put("isSuccess", isSuccess);
		return resultObj;
	}
	
	
	public String formatString(String s,int len){
		if(s!=null){
			while(s.length()<len){
				s=s+"0";
			}
		}
		return s;
	}
	
	
	/**
	 * admincode处理
	 * @param adminmapcode
	 * @param adminlevel
	 * @return
	 * @Author Juannyoh
	 * 2015-10-29下午5:36:33
	 */
	public String getAdmincode(String adminmapcode,int adminlevel){
		switch (adminlevel) {
		case 1:
			adminmapcode=formatString(adminmapcode.substring(0, 2),6);
			break;
		case 2:
			adminmapcode=formatString(adminmapcode.substring(0, 4),6);
			break;
		case 3:
			adminmapcode=adminmapcode;
			break;
		default:
			adminmapcode=adminmapcode;
			break;
		}
		return adminmapcode;
	}
	
}
