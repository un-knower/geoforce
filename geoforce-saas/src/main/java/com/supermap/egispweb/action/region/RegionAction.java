package com.supermap.egispweb.action.region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.bean.CarDept;
import com.supermap.lbsp.provider.bean.HasLinkedCarBean;
import com.supermap.lbsp.provider.hibernate.lbsp.CarAlarmForeign;
import com.supermap.lbsp.provider.hibernate.lbsp.RegionSet;


//@Controller
public class RegionAction extends BaseAction {
	static Logger logger = Logger.getLogger(RegionAction.class.getName());
	/**
	 * 车辆关联围栏列表
	 */
	@RequestMapping(value="/com/supermap/getHasLinkedRegiovCars")
	@ResponseBody
	public List<HasLinkedCarBean>  getHasLinkedCars(HttpServletRequest request,HttpServletResponse response){
		String regionId =request.getParameter("region");
		List<HasLinkedCarBean> cars = new ArrayList<HasLinkedCarBean>();
		if(regionId==null){
			return null;
		}
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("foreignIds", regionId);
		try {
			List<CarDept> carDepts = carConsumer.queryCar(null, hm);
			if(carDepts == null || carDepts.isEmpty()){
				HasLinkedCarBean bean = new HasLinkedCarBean();
				bean.setId("123");
				bean.setDeptName("暂无数据");
				bean.setLicense("暂无数据");
				cars.add(bean);
				return cars;
			}
			for(CarDept carDept:carDepts){
				HasLinkedCarBean bean = new HasLinkedCarBean();
				bean.setId(carDept.getId());//车辆id
				bean.setLicense(carDept.getLicense());//车牌号
				bean.setDeptName(carDept.getDeptName());//部门名称
				cars.add(bean);
			}
			return cars;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 车辆围栏关联
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/saveSelectedCars")
	@ResponseBody
	public AjaxResult saveSelectedCars(HttpServletRequest request,HttpServletResponse response){
		String regionSetId =request.getParameter("regionId"); //围栏id
		String carIds = request.getParameter("carIds"); //车辆id
		String alarmCode=request.getParameter("alarmTypeCode");
		int addRet=0;
		if(StringUtils.isBlank(regionSetId)){
			return new AjaxResult((short)0,"围栏信息不存在");
		}
		try {
			RegionSet getRegionSet =regionSetConsumer.getRegionSet(regionSetId);
			if(getRegionSet == null){
				return null;
			}
			String[] carIdArray = {};
			if(StringUtils.isNotBlank(carIds)){
				carIdArray = carIds.split(",");
			}
			//得到已经关联的超速车辆
			List<CarAlarmForeign> list = carAlarmForeginConsumer.getCarAlarmForeigeByForeignId(regionSetId);
			List<String> addCarIds = new ArrayList<String>(Arrays.asList(carIdArray));
			if(list != null && list.size() > 0){
				for(CarAlarmForeign foreign:list){
					if(addCarIds.contains(foreign.getCarId())){//选择的carIds 在数据库存在 则不再添加
						addCarIds.remove(foreign.getCarId());
					}else{//选择的carIds 不包含数据库中已存在的，则删除数据库中的记录
						int delRet = carAlarmForeginConsumer.delCarAlarmForeign(foreign);
							//carAlarmForeginConsumer().delCarAlarmForeign(foreign);
						if(delRet == 0){
							return new AjaxResult((short)0,"没有选择车辆");
						}
					}
				}
			}
			for(String carId:addCarIds){
				CarAlarmForeign carAlarmForeign = new CarAlarmForeign();
				carAlarmForeign.setCarId(carId);
				carAlarmForeign.setForeignId(regionSetId);
				carAlarmForeign.setType(alarmCode);
				addRet= carAlarmForeginConsumer.addCarAlarmForeign(carAlarmForeign);
			}
			return new AjaxResult((short)1,"操作成功");
			/*if(addRet == 1){
				return new AjaxResult((short)1,"操作成功");
			}else{
				return new AjaxResult((short)0,"成功");
			}*/
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
