package com.supermap.egispweb.action.driver;

import java.util.ArrayList;
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
import com.supermap.egispweb.pojo.JsonZTree;
import com.supermap.egispweb.util.DateUtil;
import com.supermap.lbsp.provider.bean.CarDeptDriver;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.info.Dept;
import com.supermap.lbsp.provider.hibernate.lbsp.Driver;


//@Controller
public class DriverAction extends BaseAction{
	static Logger logger = Logger.getLogger(DriverAction.class.getName());
	@RequestMapping(value="/com/supermap/toDriver")
	public String toDriver(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		Dept dept = deptConsumer.getDept(userEntity.getDeptId().getId());
		if(dept == null){
			return null;
		}
		request.setAttribute("dept", dept);
		logger.info("to driverList.jsp");
		return "driver/driverList";
	}
	/**
	 * 查询司机
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/driverList")
	@ResponseBody
	public Page getDriverList(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String eid = userEntity.getEid().getId();
		HashMap<String,Object> hm = new HashMap<String, Object>();
		String name = request.getParameter("name");
		String deptId = request.getParameter("deptId");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String deptcode = userEntity.getDeptId().getCode();
		if(name!=null && !"".equals(name)){
			hm.put("name", name);
			logger.info("license is "+name);
		}
		if(deptId !=null && !"".equals(deptId)){
			Dept dept = deptConsumer.getDept(deptId);
			if(dept != null){
				deptcode = deptConsumer.getDept(deptId).getCode();
				logger.info("deptId is "+deptId);
			}
		}
		if(starttime != null && !"".equals(starttime)){
			Date startDate = DateUtil.formatStringByDate(starttime, "yyyy-MM-dd HH:mm:ss");
			hm.put("startDate", startDate);
			logger.info("startDate is "+starttime);
		}
		if(endtime != null && !"".equals(endtime)){
			Date endDate = DateUtil.formatStringByDate(endtime, "yyyy-MM-dd HH:mm:ss");
			hm.put("endDate", endDate);
			logger.info("endDate is "+endDate);
		}
		if(deptcode != null){
			hm.put("deptcode", deptcode);
		}
		if(eid != null){
			hm.put("eid", eid);
		}
		Page page = getPage(request);
		page = driverComsumer.queryDriverPage(page, hm);
		logger.info("list is "+page.getResult());
		
		return page;
	}
	@RequestMapping(value="/com/supermap/unbindDriverList")
	@ResponseBody
	public Page getNotDindDriverList(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String carId = request.getParameter("carId");
		String name = request.getParameter("name");
		HashMap<String,Object> hm = new HashMap<String, Object>();
		String deptcode = userEntity.getDeptId().getCode();
		if(StringUtils.isNotBlank(deptcode)){
			hm.put("deptcode", deptcode);
		}
		if(StringUtils.isBlank(carId)){
			return null;
		}
		if(StringUtils.isNotBlank(name)){
			hm.put("name", name);
		}
		hm.put("notCarId", carId);
		Page page = getPage(request);
		page = driverComsumer.queryDriverPage(page, hm);
		return page;
	}
	
	
	@RequestMapping(value="/com/supermap/bindDriverList")
	@ResponseBody
	public Page getDindDriverList(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		HashMap<String,Object> hm = new HashMap<String, Object>();
		String carId = request.getParameter("carId");
		String name = request.getParameter("name");
		if(StringUtils.isBlank(carId)){
			return null;
		}
		if(StringUtils.isNotBlank(name)){
			hm.put("name", name);
		}
		hm.put("carId", carId);
		Page page = getPage(request);
		page = driverComsumer.queryBindDriver(page, hm);
		return page;
	}
	
	/**
	 * 添加司机
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/addDriver")
	@ResponseBody
	public HashMap<String,Object> addDriver(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String eid = userEntity.getEid().getId();
		HashMap<String,Object> m = new HashMap<String, Object>();
		String drivername = request.getParameter("name");
		if(StringUtils.isBlank(drivername)){
			m.put("flag", "err_01");
			return m;
		}
		int rowName = driverComsumer.hasName(drivername,eid);
		
		if(rowName > 0){
			m.put("flag", "err_05");
			return m;
		}
		
		String deptId = request.getParameter("deptId");
		if(StringUtils.isBlank(deptId)){
			m.put("flag", "err_02");
			return m;
		}
		String license = request.getParameter("license");
		if(StringUtils.isBlank(license)){
			m.put("flag", "err_03");
			return m;
		}
		int row = driverComsumer.haslicense(license);
		if(row >0){
			m.put("flag", "err_04");
			return m;
		}
		
		String age = request.getParameter("age");
		String address= request.getParameter("address");
		String zipcode = request.getParameter("zipcode");
		String sex = request.getParameter("sex");
		String licenseSdate = request.getParameter("starttime");
		String licenseEdate = request.getParameter("endtime");
		String phone = request.getParameter("phone");
		
		Driver driver = new Driver();
		driver.setAddress(address);
		if(StringUtils.isNotBlank(age)){
			driver.setAge(Short.valueOf(age));
		}
		driver.setCreateUserid(userEntity.getId());
		driver.setDeptId(deptId);
		driver.setLicense(license);
		driver.setEid(eid);
		if(licenseSdate != null && licenseEdate!= null){
			Date sdate = DateUtil.formatStringByDate(licenseSdate, "yyyy-MM-dd");
			Date edate = DateUtil.formatStringByDate(licenseEdate, "yyyy-MM-dd");
			driver.setLicenseSdate(sdate);
		    driver.setLicenseEdate(edate);
		}
		driver.setModifyDate(new Date());
		driver.setName(drivername);
		driver.setPhone(phone);
		if(sex != null){
			driver.setSex(Short.valueOf(sex));
		}
		driver.setZipcode(zipcode);
		
		driverComsumer.addDriver(driver);
		m.put("flag", "ok");
		return m;
	}
	/**
	 * 得到司机信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/driverInfo")
	@ResponseBody
	public Driver getDriverInfo(HttpServletRequest request,HttpServletResponse response){
		//HashMap<String,Object> m = new HashMap<String, Object>();
		String driverId = request.getParameter("driverId");
		Driver driver = driverComsumer.getDriver(driverId);
		if(driver == null){
			logger.info("driver is "+null);
			return null;
		}
		return driver;
	}
	
	/**
	 * 修改司机信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/updateDriver")
	@ResponseBody
	public HashMap<String,Object> updateDriver(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String eid = userEntity.getEid().getId();
		HashMap<String,Object> m = new HashMap<String, Object>();
		String driverId = request.getParameter("driverId");
		Driver driver = driverComsumer.getDriver(driverId);
		if(driver == null){
			logger.info("driver is "+null);
			return null;
		}
		String drivername = request.getParameter("name");
		if(drivername == null || "".equals(drivername)){
			m.put("flag", "err_01");
			return m;
		}
		if(!driver.getName().equalsIgnoreCase(drivername)&&driverComsumer.hasName(drivername,eid)>0){
			m.put("flag", "err_05");
			return m;
		}
		String deptId = request.getParameter("deptId");
		if(deptId == null || "".equals(deptId)){
			m.put("flag", "err_02");
			return m;
		}
		String license = request.getParameter("license");
		if(license == null || "".equals(license)){
			m.put("flag", "err_03");
			return m;
		}
		int row = driverComsumer.haslicense(license);
		if(!driver.getLicense().equalsIgnoreCase(license) && row>0){
			m.put("flag", "err_04");
			return m;
		}
		String age = request.getParameter("age");
		String address= request.getParameter("address");
		String zipcode = request.getParameter("zipcode");
		String sex = request.getParameter("sex");
		String licenseSdate = request.getParameter("starttime");
		String licenseEdate = request.getParameter("endtime");
		String phone = request.getParameter("phone");
		driver.setAddress(address);
		if(StringUtils.isNotBlank(age)){
			driver.setAge(Short.valueOf(age));
		}
		driver.setCreateUserid(userEntity.getId());
		driver.setDeptId(deptId);
		driver.setLicense(license);
		if(licenseSdate != null && licenseEdate!= null){
			licenseSdate = licenseSdate.substring(0,10);
			licenseEdate = licenseEdate.substring(0,10);
			Date sdate = DateUtil.formatStringByDate(licenseSdate, "yyyy-MM-dd");
			Date edate = DateUtil.formatStringByDate(licenseEdate, "yyyy-MM-dd");
			driver.setLicenseSdate(sdate);
		    driver.setLicenseEdate(edate);
		}
		driver.setModifyDate(new Date());
		driver.setName(drivername);
		driver.setPhone(phone);
		if(StringUtils.isNotBlank(sex)){
			driver.setSex(Short.valueOf(sex));
		}
		driver.setZipcode(zipcode);
		driverComsumer.updateDriver(driver);
		m.put("flag", "ok");
		return m;
	}
	
	/**
	 * 删除司机
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/delDriver")
	@ResponseBody
	public HashMap<String, Object> delDriver(HttpServletRequest request,HttpServletResponse response){
		String driverId = request.getParameter("driverIds");
		String[] ids = driverId.split(",");
		
		for (int i = 0; i < ids.length; i++) {
			
			driverComsumer.delDriver(ids[i]);
			carConsumer.delCarDriverbyDriver(ids[i]);
		}
		HashMap<String,Object> msg = new HashMap<String, Object>();
		
		msg.put("flag", "ok");
		logger.info("delDriver is "+"ok");
		return msg;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/com/supermap/driverTree")
	@ResponseBody
	public List<JsonZTree> getDriverDeptTree(HttpServletRequest request,HttpServletResponse response){
		
		String deptId = "1";
		
		List<CarDeptDriver> driverList = null;//车辆列表
		List<Dept> deptList = null;//部门列表
		
		HashMap<String,Object> driverHm = new HashMap<String,Object>();
		HashMap<String,Object> deptHm = new HashMap<String,Object>();
		
		boolean isRoot = true;
		String treeId = request.getParameter("treeId");
		if(StringUtils.isNotBlank(treeId)){
			//treeId为0时，为根节点，按照登录用户所在部门获取车辆列表
			deptId = treeId;	
			isRoot = false;
		}
		
		List<JsonZTree> list = new ArrayList<JsonZTree>();
		try{
			if (isRoot){
				Dept rootDept = this.deptConsumer.getDept(deptId);
				if (rootDept != null){
					JsonZTree rootNode = new JsonZTree();
					rootNode.setId(deptId);
					
					if (StringUtils.isBlank(treeId)){
						rootNode.setpId("0");
					}else{
						rootNode.setpId(rootDept.getParentId());
					}
					rootNode.setName(rootDept.getName());
					rootNode.setEname(rootDept.getName());
					if(rootDept.getName().length() > 7){
						rootNode.setEname(rootDept.getName().substring(0,7)+"..");
					}
					rootNode.setOpen(true);
					rootNode.setParent(true);
					list.add(rootNode);
				}	
			}
			
			//carHm.put("status", (short)1);//1表示开通
			deptHm.put("parentId", deptId);
			deptList = this.deptConsumer.queryDept(null, deptHm);
            List<String> deptIds = new ArrayList<String>();
			
			
			if (deptList != null && !deptList.isEmpty()){
				for (Dept dept: deptList){
					JsonZTree ztree = new JsonZTree();
					ztree.setId(dept.getId());
					ztree.setpId(dept.getParentId());
					ztree.setName(dept.getName());
					ztree.setEname(dept.getName());
					if(dept.getName().length() > 7){
						ztree.setEname(dept.getName().substring(0,7)+"..");
					}
					ztree.setOpen(false);
					ztree.setChecked(false);
					ztree.setParent(true);
					list.add(ztree);
					deptIds.add(dept.getId());
				}
			}
			
			driverHm.put("deptIds", deptIds);
			driverList = this.driverComsumer.queryDriver(null, driverHm);
			if (driverList != null && !driverList.isEmpty()){
				for (CarDeptDriver carDeptDriver: driverList){
					JsonZTree ztree = new JsonZTree();
					ztree.setId(carDeptDriver.getId());
					ztree.setpId(carDeptDriver.getDeptId());
					ztree.setName(carDeptDriver.getName());
					if(carDeptDriver.getDeptName().length() > 7){
						ztree.setEname(carDeptDriver.getDeptName().substring(0,7)+"..");
					}
					ztree.setChecked(false);
					ztree.setParent(false);
					list.add(ztree);
				}
			}
			return list;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	@RequestMapping(value="/com/supermap/driverChangDept")
	@ResponseBody
	 public HashMap<String, Object> changDept(HttpServletRequest request,HttpServletResponse response){
		   HashMap<String,Object> m = new HashMap<String, Object>();
		   String driverId = request.getParameter("driverIds");
		   String deptId = request.getParameter("deptId");
		   String[] driverIds = driverId.split(",");
		   if(driverIds.length < 0){
				m .put("falg", "error");
				return m;
			}
		   if(StringUtils.isBlank(deptId)){
			   m .put("falg", "error");
				return m;
		   }
		   for (int i = 0; i < driverIds.length; i++) {
			   Driver driver = driverComsumer.getDriver(driverIds[i]);
			  
			   if(driver == null){
				   m .put("flag", "car_err");
				   return m;
			   }
			   
			   driver.setDeptId(deptId);
			   driver.setModifyDate(new Date());
			   driverComsumer.updateDriver(driver);
		   }
		   m .put("flag", "ok");
		   return m;
	   }
	

}
