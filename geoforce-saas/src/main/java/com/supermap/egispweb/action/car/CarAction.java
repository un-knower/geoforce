package com.supermap.egispweb.action.car;
import java.util.ArrayList;
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

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.lbs.entity.Terminal;
import com.supermap.egispweb.action.base.BaseAction;

import com.supermap.egispweb.pojo.AutocompleteBean;
import com.supermap.egispweb.pojo.car.CarTerminal;
import com.supermap.egispweb.util.DateUtil;
import com.supermap.lbsp.provider.bean.CarDept;

import com.supermap.lbsp.provider.common.Page;

import com.supermap.lbsp.provider.hibernate.info.Dept;
import com.supermap.lbsp.provider.hibernate.lbsp.Car;
import com.supermap.lbsp.provider.hibernate.lbsp.CarDriver;
import com.supermap.lbsp.provider.hibernate.lbsp.DataWordbook;
//import com.supermap.lbsp.provider.hibernate.lbsp.Terminal;





//@Controller
public class CarAction extends BaseAction{
	static Logger logger = Logger.getLogger(CarAction.class.getName());
	
	/**
	 * 跳转到车辆管理页面
	 * @return
	 */
	@RequestMapping(value="/com/supermap/toCar")
	public String toCar(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		InfoDeptEntity dept = userEntity.getDeptId();
		if(dept == null){
			return null;
		}
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("type", "19");
		List<com.supermap.egispservice.lbs.entity.DataWordbook> dataWorkList = dataWorkConsumer.getDataWordbookList(hm);
		
		List<DataWordbook> termList = terminalTypeConsumer.queryTerminalType("1");
		
		request.setAttribute("dataWorkList", dataWorkList);
		request.setAttribute("termList", termList);
	    request.setAttribute("dept", dept);
		logger.info("to carList.jsp");
		return "car/carList";
	}
	
	
	/**
	 * 查询车辆
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/carList")
	@ResponseBody
	public Page getCarList(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String eid = userEntity.getEid().getId();
		HashMap<String,Object> hm = new HashMap<String, Object>();
		String license = request.getParameter("license");
		String deptId = request.getParameter("deptId");
		
		String deptcode = userEntity.getDeptId().getCode();
		if(StringUtils.isNotBlank(license)){
			hm.put("license", license);
			logger.info("license is "+license);
		}
		if(deptId !=null && !"".equals(deptId)){
			Dept dept = deptConsumer.getDept(deptId);
			if(dept != null){
				deptcode = deptConsumer.getDept(deptId).getCode();
				logger.info("deptId is "+deptId);
			}
		}
		
		if(StringUtils.isNotBlank(deptcode)){
			hm.put("deptcode", deptcode);
		}
		if(StringUtils.isNotBlank(eid)){
			hm.put("eid", eid);
		}
		Page page = getPage(request);
		page = carConsumer.queryCarPage(page, hm);
		return page;
	}
	
	/**
	 * 添加车辆
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/addCar")
	@ResponseBody
	public HashMap<String,Object> addCar(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String eid = userEntity.getEid().getId();
		HashMap<String,Object> m = new HashMap<String, Object>();
		String license = request.getParameter("license");
		if(license == null || "".equals(license)){
			m.put("flag", "err_01");
			return m;
		}
		int rows = carConsumer.haslicense(license,eid);
		if(rows>0){
			m.put("flag", "err_06");
			return m;
		}
		
		String terminalCode = request.getParameter("terminalCode");
		if(terminalCode == null|| "".equals(terminalCode) ){
			m.put("flag", "err_02");
			return m;
		}
		Terminal isTerminal = terminalConsumer.getTerminalByCode(terminalCode);
		if(isTerminal != null){
			m.put("flag", "err_07");
			return m;
		}
		
		String deptId = request.getParameter("deptId");
		if(deptId == null||"".equals(deptId)){
			m.put("flag", "err_03");
			return m;
		}
		String terminalType = request.getParameter("terminalType");
		if(terminalType == null||"".equals(terminalType)){
			m.put("flag", "err_04");
			return m;
		}
		String mobile = request.getParameter("mobile");
		if(mobile == null||"".equals(mobile)){
			m.put("flag", "err_05");
			return m;
		}
		int row  = terminalConsumer.hasMobile(mobile);
		if(row > 0){
			m.put("flag", "err_08");
			return m;
		}
		String color = request.getParameter("color");
		String brand = request.getParameter("brand");
		String petrol = request.getParameter("petrol");
		String carType = request.getParameter("carType");
		String other = request.getParameter("other");
		
		CarTerminal carTerminal = new CarTerminal();
		Car car = new Car();
		car.setBrand(brand);
		car.setColor(color);
		car.setCreateUserid(userEntity.getId());
		car.setDepId(deptId);
		car.setEid(eid);
		car.setLicense(license);
		car.setOperDate(new Date());
		car.setOthers(other);
		car.setPetrol(petrol);
		car.setStatus((short)1);
		car.setStopDate(new Date());
		car.setType(carType);
		//carTerminal.setCar(car);
		Terminal terminal = new Terminal();
		terminal.setEid(eid);
		terminal.setCode(terminalCode);
		terminal.setDeptId(deptId);
		terminal.setMobile(mobile);
		terminal.setName(terminalCode);
		terminal.setOperDate(new Date());
		terminal.setTypeId(terminalType);
		//carTerminal.setTerminal(terminal);
		carConsumer.addCar(carTerminal);
		m.put("flag", "ok");
		
		return m;
	}
	
	
	
	/**
	 * 得到车辆信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/carInfo")
	@ResponseBody
	public CarTerminal getCarInfo(HttpServletRequest request,HttpServletResponse response){
		//HashMap<String,Object> m = new HashMap<String, Object>();
		String carId = request.getParameter("carId");
		Car car = carConsumer.getCar(carId);
		if(car == null){
			logger.info("car is "+null);
			return null;
		}
		Terminal terminal = terminalConsumer.getTerminalByCarId(carId);
		if(terminal == null){
			logger.info("terminal is "+null);
			return null;
		}
		CarTerminal carTerminal = new CarTerminal();
		//carTerminal.setCar(car);
		//carTerminal.setTerminal(terminal);
		return carTerminal;
	}
	
	/**
	 * 修改车辆信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/updateCar")
	@ResponseBody
	public HashMap<String,Object> updateCar(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String eid = userEntity.getEid().getId();
		HashMap<String,Object> m = new HashMap<String, Object>();
		String carId = request.getParameter("carId");
		Car car = carConsumer.getCar(carId);
		if(car == null){
			logger.info("car is "+null);
			return null;
		}
		Terminal terminal = terminalConsumer.getTerminalByCarId(carId);
		if(terminal == null){
			logger.info("terminal is "+null);
			return null;
		}
		String license = request.getParameter("license");
		if(license == null || "".equals(license)){
			m.put("flag", "01");
			return m;
		}
		if(!license.equals(car.getLicense()) && carConsumer.haslicense(license,eid)>0){
			m.put("flag", "err_06");
			return m;
		}
		
		String terminalCode = request.getParameter("terminalCode");
		if(terminalCode == null|| "".equals(terminalCode) ){
			m.put("flag", "02");
			return m;
		}
		Terminal isTerminal = terminalConsumer.getTerminalByCode(terminalCode);
		if(isTerminal != null && !isTerminal.getCarId().equals(car.getId()) ){
			m.put("flag", "err_07");
			return m;
		}
		String deptId = request.getParameter("deptId");
		if(deptId == null||"".equals(deptId)){
			m.put("flag", "03");
			return m;
		}
		String terminalType = request.getParameter("terminalType");
		if(terminalType == null||"".equals(terminalType)){
			m.put("flag", "04");
			return m;
		}
		String mobile = request.getParameter("mobile");
		if(mobile == null||"".equals(mobile)){
			m.put("flag", "05");
			return m;
		}
		int row  = terminalConsumer.hasMobile(mobile);
		if(isTerminal != null && !mobile.equals(isTerminal.getMobile()) && row > 0){
			m.put("flag", "err_08");
			return m;
		}
		String color = request.getParameter("color");
		String brand = request.getParameter("brand");
		String petrol = request.getParameter("petrol");
		String carType = request.getParameter("carType");
		String other = request.getParameter("other");
		
		car.setBrand(brand);
		car.setColor(color);
		car.setCreateUserid(userEntity.getId());
		car.setDepId(deptId);
		car.setLicense(license);
		car.setOperDate(new Date());
		car.setOthers(other);
		car.setPetrol(petrol);
		car.setStatus((short)1);
		car.setStopDate(new Date());
		car.setType(carType);
		carConsumer.updateCar(car);
		terminal.setCode(terminalCode);
		terminal.setDeptId(deptId);
		terminal.setMobile(mobile);
		terminal.setName(terminalCode);
		terminal.setOperDate(new Date());
		terminal.setTypeId(terminalType);
		this.terminalConsumer.updateTerminal(terminal);
		m.put("flag", "ok");
		
		return m;
	}
	
	/**
	 * 删除车辆
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/delCar")
	@ResponseBody
	public HashMap<String, Object> delCar(HttpServletRequest request,HttpServletResponse response){
		HashMap<String,Object> hm = new HashMap<String, Object>();
		HashMap<String,Object> msg = new HashMap<String, Object>();
		String carId = request.getParameter("carIds");
		String[] ids = carId.split(",");
		List idList = Arrays.asList(ids);
		hm.put("inCarIds", idList);
		List<CarDept> carList = carConsumer.queryCar(null, hm);
		for (int i = 0; i < carList.size(); i++) {
			CarDept obj = carList.get(i);
			Car car = carConsumer.getCar(obj.getId());
			Terminal terminal = terminalConsumer.getTerminalByCarId(car.getId());
			CarDriver carDriver = carConsumer .getCarDriver(car.getId(), car.getDepId());
			if(car == null){
				msg.put("flag", "err");
				logger.info("del Car is is null");
				return msg;
			}
			if(terminal == null){
				msg.put("flag", "err");
				logger.info("del terminal is is null");
				return msg;
			}
			Boolean falg = carConsumer.isPointHasCar(car.getId());
			if(falg){
				msg.put("flag", "poointErr");
			
				return msg;
				
			}
			car.setStatus((short)3);
			car.setOperDate(new Date());
			carConsumer.updateCar(car);
			if(carDriver != null){
				carConsumer.delCarDriverByCar(carDriver);
			}
			terminalConsumer.delTerminal(terminal);
		}
		
		
		msg.put("flag", "ok");
		logger.info("delCar is "+"ok");
		return msg;
	}
	/**
	 * 绑定车辆
	 * @return
	 */
	@RequestMapping(value="/com/supermap/BindDriver")
	@ResponseBody
	public HashMap<String, Object> BindDriver(HttpServletRequest request,HttpServletResponse response){
		
		HashMap<String,Object> m = new HashMap<String, Object>();
		String carId = request.getParameter("carIds");
		String driverId = request.getParameter("driverIds");
		String[] driverIds = driverId.split(",");
		String[] carIds = carId.split(",");
		if(driverIds.length < 0){
			m .put("falg", "error");
			return m;
		}
		for (int j = 0; j < carIds.length; j++) {
			
			for (int i = 0; i < driverIds.length; i++){
				CarDriver carDriver = carConsumer.getCarDriver(carIds[j], driverIds[i]);
				if(carDriver == null){
					CarDriver carDriverNew = new CarDriver();
					carDriverNew.setCarId(carIds[j]);
					carDriverNew.setDriverId(driverIds[i]);
					
					carConsumer.addCarDriver(carDriverNew);
				}
			}
		}
		 m .put("flag", "ok");
		return m;
		
	}
	
	/**
	 * 取消绑定
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/cancelBind")
	@ResponseBody
	public HashMap<String, Object> cancelBind(HttpServletRequest request,HttpServletResponse response){
		HashMap<String,Object> m = new HashMap<String, Object>();
		String carId = request.getParameter("carIds");
		String driverId = request.getParameter("driverIds");
		String[] driverIds = driverId.split(",");
		String[] carIds = carId.split(",");
		if(carIds.length < 0){
			m .put("falg", "error");
			return m;
		}
		for (int i = 0; i < carIds.length; i++) {
			for (int j = 0; j < driverIds.length; j++) {
				CarDriver cardriver = carConsumer.getCarDriver(carIds[i], driverIds[j]);
				if(cardriver != null){
					carConsumer.delCarDriverByCar(cardriver);
				}
			}
			
		}
		 m .put("flag", "ok");
		return m;
	}
	
	/**
	 * 调整部门
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/changDept")
	@ResponseBody
   public HashMap<String, Object> changDept(HttpServletRequest request,HttpServletResponse response){
	   HashMap<String,Object> m = new HashMap<String, Object>();
	   String carId = request.getParameter("carIds");
	   String deptId = request.getParameter("deptId");
	   String[] carIds = carId.split(",");
	   if(carIds.length < 0){
			m .put("falg", "error");
			return m;
		}
	   if(StringUtils.isBlank(deptId)){
		   m .put("falg", "error");
			return m;
	   }
	   for (int i = 0; i < carIds.length; i++) {
		   Car car = carConsumer.getCar(carIds[i]); 
		   Terminal terminal = terminalConsumer.getTerminalByCarId(car.getId());
		   if(car == null){
			   m .put("flag", "car_err");
			   return m;
		   }
		   if(terminal == null){
			   m .put("flag", "t_err");
			   return m;
		   }
		   car.setDepId(deptId);
		   car.setOperDate(new Date());
		   carConsumer.updateCar(car);
		   terminal.setDeptId(deptId);
		   terminal.setOperDate(new Date());
		   terminalConsumer.updateTerminal(terminal);
	   }
	   m .put("flag", "ok");
	   return m;
   }
	
	/**
	 * 得到车辆号码
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/getLicense")
	@ResponseBody
	public List<AutocompleteBean> getCarsByLicense(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String license = request.getParameter("text");
		String eid = userEntity.getEid().getId();
		String deptcode = userEntity.getDeptId().getCode();
		List<AutocompleteBean> beans = new ArrayList<AutocompleteBean>();
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if(StringUtils.isBlank(deptcode)){
			return beans;
		}
		if(StringUtils.isBlank(license)){
			return beans;
		}
		if(StringUtils.isBlank(eid)){
			return beans;
		}
		hm.put("deptcode", deptcode);
		hm.put("eid", eid);
		hm.put("license", license.trim().toUpperCase());
		try {
			Page page = getPage(request);
			List<CarDept> list = carConsumer.queryCar(page, hm);
			for(CarDept carDept:list){
				AutocompleteBean bean = new AutocompleteBean();
				bean.setId(carDept.getId());
				bean.setName(carDept.getLicense());
				beans.add(bean);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return beans;
	}
}
