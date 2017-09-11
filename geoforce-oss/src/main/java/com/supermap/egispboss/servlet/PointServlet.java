package com.supermap.egispboss.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.supermap.egispboss.excel.export.ExportExcel;
import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispboss.util.CommonUtil.DateType;
import com.supermap.egispservice.base.entity.PointExtcolEntity;
import com.supermap.egispservice.base.entity.PointGroupEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.ExportPointBean;
import com.supermap.egispservice.base.service.PointExtcolService;
import com.supermap.egispservice.base.service.PointGroupService;
import com.supermap.egispservice.base.service.PointService;
import com.supermap.egispservice.geocoding.service.IGeocodingService;

/**
 * 网点服务
 * @author Administrator
 */
public class PointServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(PointServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if(!CommonUtil.isStringEmpty(method)){
			LOGGER.info("## 收到请求[method,"+method+"]");
			if(method.equals("queryPointGroup")){
				queryPointGroup(req,resp);//网点分组
			}else if(method.equals("exportPoints")){
				exportPoints(req,resp);//导出
			}else if(method.equals("queryPointsCount")){
				queryPointsCount(req,resp);//查询条件下的网点数量
			}else if(method.equals("getAdminElements")){
				getAdminElements(req,resp);//城市列表
			}else if(method.equals("deletePoints")){
				deletePoints(req,resp);//删除条件下的网点
			}
		}else{
			write(req, resp, "参数[method]为空", null, false);
		}
	}
	
	/**
	 * 查询所有分组
	 * @Author Juannyoh
	 * 2016-9-18下午4:05:09
	 */
	public void queryPointGroup(HttpServletRequest req, HttpServletResponse resp){
		try {
			String userid=req.getParameter("parentuserid");//总账号id
			if(StringUtils.isEmpty(userid)){
				write(req, resp,"总账号信息为空", null, false);
			}
			PointGroupService groupservice=(PointGroupService) getBean("pointGroupService");
			List<PointGroupEntity> grouplist = groupservice.queryAllGroups(null, null, userid, null, null);
			if(null!=grouplist&&grouplist.size()>0){
				write(req, resp, null, grouplist, true);
			}else{
				throw new ParameterException(ExceptionType.NOT_FOUND,"");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	/**
	 * 导出所有网点
	 * @Author Juannyoh
	 * 2016-9-18下午4:08:44
	 */
	public void exportPoints(HttpServletRequest req, HttpServletResponse resp){
		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String bdate=req.getParameter("bdate");//开始时间
		String edate=req.getParameter("edate");//结束时间
		String groupids=req.getParameter("groupids");//分组id，多个以逗号分割
		String pointstatus=req.getParameter("pointstatus");//网点状态
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		
		String type=req.getParameter("type");//需要导出的坐标类型
		if(StringUtils.isEmpty(type)){
			type="SMLL";//默认为超图经纬度
		}
		
		try {
			if(StringUtils.isEmpty(eid)||StringUtils.isEmpty(parentuserid)){
				throw new Exception("总账号信息错误");
			}
			
			PointService pointService=(PointService) getBean("pointService");
			PointExtcolService pointExtcolService=(PointExtcolService) getBean("pointExtcolService");
			//先查询自定义字段
			PointExtcolEntity record=pointExtcolService.findByUserid(parentuserid);//根据用户id查找到
			//查询网点信息
			if(!StringUtils.isEmpty(levels)&&!StringUtils.isEmpty(admincode)){
				if(levels.equals("1")){
					admincode=admincode.substring(0, 2);
				}else if(levels.equals("2")){
					admincode=admincode.substring(0, 4);
				}
			}
			//时间
			Date btimed=null;
			Date etimed=null;
			//时间转换
			if(!StringUtils.isEmpty(bdate)){
				btimed=CommonUtil.dateConvert(bdate.trim()+" 00:00:00", DateType.TIMESTAMP);
			}
			if(!StringUtils.isEmpty(edate)){
				etimed=CommonUtil.dateConvert(edate.trim()+" 23:59:59", DateType.TIMESTAMP);
			}
			
			List<String> childlist=null;
			if(!StringUtils.isEmpty(childuserids)){
				childlist=new ArrayList<String>();
				String[] childs=childuserids.split(",");
				if(childs!=null&&childs.length>0){
					for(String child:childs){
						childlist.add(child);
					}
				}
			}
			
			List<String> grouplist=null;
			if(!StringUtils.isEmpty(groupids)){
				grouplist=new ArrayList<String>();
				String[] groups=groupids.split(",");
				if(groups!=null&&groups.length>0){
					for(String group:groups){
						grouplist.add(group);
					}
				}
			}
			Map<String,Object> parammap=new HashMap<String,Object>();
			parammap.put("eid", eid);
			parammap.put("childuserid", childlist);
			parammap.put("btime", btimed);
			parammap.put("etimed", etimed);
			parammap.put("groupids", grouplist);
			parammap.put("status", pointstatus);
			parammap.put("admincode", admincode);
			parammap.put("type", type);
			long start=System.currentTimeMillis();
			List<ExportPointBean> exportlist=pointService.queryAllForExport(parammap);
			long end=System.currentTimeMillis();
			LOGGER.info("查询网点用时："+(end-start)/1000);
			
			String[] titles={"ID","网点名称","网点地址",record.getCol1(),record.getCol2(),record.getCol3(),record.getCol4(),record.getCol5()
					,record.getCol6(),record.getCol7(),record.getCol8(),record.getCol9(),record.getCol10()
					,"经度","纬度","绑定区划","所属子账号","创建时间","修改时间","所属分组","网点状态"};
			ExportExcel<ExportPointBean> ex = new ExportExcel<ExportPointBean>();
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/x-download");
            String filedisplay = "网点信息.xls";
            filedisplay = new String( filedisplay.getBytes("gb2312"), "ISO8859-1" );//防止文件名含有中文乱码
            resp.setHeader("Content-Disposition", "attachment;filename="+ filedisplay);
            OutputStream out = resp.getOutputStream();
            ex.exportPointExcel("网点信息",titles, exportlist,out,"yyyy-MM-dd HH:mm:ss");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询城市列表
	 * @Author Juannyoh
	 * 2016-9-20上午10:30:07
	 */
	public void getAdminElements(HttpServletRequest req, HttpServletResponse resp){
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		try {
			IGeocodingService geocodingService=(IGeocodingService) getBean("geocodingService");
			int level =  2;
			try {
				level = Integer.parseInt(levels);
			} catch (Exception e) {
				level = 2;
			}
			List<Map<String,Object>> list = geocodingService.getAdminElement(admincode, level);
			if(null != list && list.size() > 0){
				write(req, resp, null, list, true);
			}else{
				write(req, resp, "查询失败", null, false);
			}
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	/**
	 * 查询网点数量
	 * @Author Juannyoh
	 * 2016-9-20上午10:34:50
	 */
	public void queryPointsCount(HttpServletRequest req, HttpServletResponse resp){
//		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String bdate=req.getParameter("bdate");//开始时间
		String edate=req.getParameter("edate");//结束时间
		String groupids=req.getParameter("groupids");//分组id，多个以逗号分割
		String pointstatus=req.getParameter("pointstatus");//网点状态
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		try {
			if(StringUtils.isEmpty(eid)){
				throw new Exception("总账号信息错误");
			}
			
			PointService pointService=(PointService) getBean("pointService");
			//查询网点信息
			if(!StringUtils.isEmpty(levels)&&!StringUtils.isEmpty(admincode)){
				if(levels.equals("1")){
					admincode=admincode.substring(0, 2);
				}else if(levels.equals("2")){
					admincode=admincode.substring(0, 4);
				}
			}
			//时间
			Date btimed=null;
			Date etimed=null;
			//时间转换
			if(!StringUtils.isEmpty(bdate)){
				btimed=CommonUtil.dateConvert(bdate.trim()+" 00:00:00", DateType.TIMESTAMP);
			}
			if(!StringUtils.isEmpty(edate)){
				etimed=CommonUtil.dateConvert(edate.trim()+" 23:59:59", DateType.TIMESTAMP);
			}
			
			List<String> childlist=null;
			if(!StringUtils.isEmpty(childuserids)){
				childlist=new ArrayList<String>();
				String[] childs=childuserids.split(",");
				if(childs!=null&&childs.length>0){
					for(String child:childs){
						childlist.add(child);
					}
				}
			}
			
			List<String> grouplist=null;
			if(!StringUtils.isEmpty(groupids)){
				grouplist=new ArrayList<String>();
				String[] groups=groupids.split(",");
				if(groups!=null&&groups.length>0){
					for(String group:groups){
						grouplist.add(group);
					}
				}
			}
			Map<String,Object> parammap=new HashMap<String,Object>();
			parammap.put("eid", eid);
			parammap.put("childuserid", childlist);
			parammap.put("btime", btimed);
			parammap.put("etimed", etimed);
			parammap.put("groupids", grouplist);
			parammap.put("status", pointstatus);
			parammap.put("admincode", admincode);
			int count=pointService.querydeletePointCount(parammap);
			if(count>=0){
				write(req, resp, null, count, true);
			}else{
				write(req, resp, "查询失败", 0, false);
			}
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	
	/**
	 * 删除网点
	 * @Author Juannyoh
	 * 2016-9-20上午10:36:29
	 */
	public void deletePoints(HttpServletRequest req, HttpServletResponse resp){
//		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String bdate=req.getParameter("bdate");//开始时间
		String edate=req.getParameter("edate");//结束时间
		String groupids=req.getParameter("groupids");//分组id，多个以逗号分割
		String pointstatus=req.getParameter("pointstatus");//网点状态
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		try {
			if(StringUtils.isEmpty(eid)){
				throw new Exception("总账号信息错误");
			}
			
			PointService pointService=(PointService) getBean("pointService");
			//查询网点信息
			if(!StringUtils.isEmpty(levels)&&!StringUtils.isEmpty(admincode)){
				if(levels.equals("1")){
					admincode=admincode.substring(0, 2);
				}else if(levels.equals("2")){
					admincode=admincode.substring(0, 4);
				}
			}
			//时间
			Date btimed=null;
			Date etimed=null;
			//时间转换
			if(!StringUtils.isEmpty(bdate)){
				btimed=CommonUtil.dateConvert(bdate.trim()+" 00:00:00", DateType.TIMESTAMP);
			}
			if(!StringUtils.isEmpty(edate)){
				etimed=CommonUtil.dateConvert(edate.trim()+" 23:59:59", DateType.TIMESTAMP);
			}
			
			List<String> childlist=null;
			if(!StringUtils.isEmpty(childuserids)){
				childlist=new ArrayList<String>();
				String[] childs=childuserids.split(",");
				if(childs!=null&&childs.length>0){
					for(String child:childs){
						childlist.add(child);
					}
				}
			}
			
			List<String> grouplist=null;
			if(!StringUtils.isEmpty(groupids)){
				grouplist=new ArrayList<String>();
				String[] groups=groupids.split(",");
				if(groups!=null&&groups.length>0){
					for(String group:groups){
						grouplist.add(group);
					}
				}
			}
			Map<String,Object> parammap=new HashMap<String,Object>();
			parammap.put("eid", eid);
			parammap.put("childuserid", childlist);
			parammap.put("btime", btimed);
			parammap.put("etimed", etimed);
			parammap.put("groupids", grouplist);
			parammap.put("status", pointstatus);
			parammap.put("admincode", admincode);
			int count=pointService.deletePoint(parammap);
			if(count>=0){
				write(req, resp, null, count, true);
			}else{
				write(req, resp, "删除失败", null, false);
			}
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
			
}
