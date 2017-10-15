package com.supermap.egispweb.action.lbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.lbs.pojo.CarDept;
import com.supermap.egispservice.lbs.pojo.JsonZTree;
import com.supermap.egispservice.lbs.service.CarService;


//@Controller
public class TreeAction extends BaseAction{
	static Logger logger = Logger.getLogger(TreeAction.class.getName());
	
	@Autowired
	InfoDeptService infoDeptService;
	@Autowired
	CarService carService;
	
	@RequestMapping(value="/com/supermap/carTree")
	@ResponseBody
	public List<JsonZTree>  getCarDeptTree(HttpServletRequest request,HttpSession session,HttpServletResponse response){

		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String deptId = userEntity.getDeptId().getId();
		String treeId = request.getParameter("treeId");
		List<JsonZTree> list = this.carService.getCarDeptTree(deptId, treeId);
		if((list == null || list.isEmpty())&&(StringUtils.isEmpty(treeId)||deptId.equals(treeId))){
			JsonZTree ztree = new JsonZTree();
			ztree.setId(userEntity.getDeptId().getName());
			ztree.setpId("");
			ztree.setName(userEntity.getDeptId().getName());
			ztree.setEname(userEntity.getDeptId().getName());
			if(ztree.getName().length() > 7){
				ztree.setEname(ztree.getName().substring(0,7)+"..");
			}
			ztree.setOpen(true);
			ztree.setChecked(false);
			ztree.setIsParent(true);
			list.add(ztree);
		}
		return list;
	}
	/**
	 * 
	* 方法名: carTreeSearch
	* 描述:车辆监控 车辆部门树搜索 按车牌号 or sim卡号 or 终端号搜索
	* @param carSearch
	* @return
	 */
	@RequestMapping(value = "/com/supermap/carTreeSearch")
	@ResponseBody
	public List<JsonZTree> carTreeSearch(String carSearch,HttpSession session){
		UserEntity user = getUserSession(session);
		if(user==null){
			return null;
		}
		String userid=user.getId();
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("userId", userid);
		hm.put("search", carSearch);
		List<JsonZTree> list = this.getcarTreeSearch(userid,carSearch);
		return list;
	}
	
	/**
	 * 部门树
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/deptTree")
	@ResponseBody
	public List<JsonZTree> deptTree(HttpServletRequest request,HttpSession session){
		String deptCode = request.getParameter("deptCode");
		List<JsonZTree> json = new ArrayList<JsonZTree>();
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(StringUtils.isBlank(deptCode)){
			if(userEntity == null){
				return json;
			}
			deptCode = userEntity.getDeptId().getCode();
		}
		 List<InfoDeptEntity> deptList= this.infoDeptService.getChildDepts(userEntity.getDeptId().getId());
			if (deptList != null && !deptList.isEmpty()){
				for (int i = 0; i < deptList.size(); i++){
					InfoDeptEntity dept = deptList.get(i);
					JsonZTree ztree = new JsonZTree();
					ztree.setId(dept.getId());
					ztree.setpId(dept.getParentId());
					ztree.setName(dept.getName());
					ztree.setEname(dept.getName());
					if(dept.getName().length() > 8){
						ztree.setEname(dept.getName().substring(0,8)+"..");
					}
					ztree.setCode(dept.getCode());
					if (dept.getCode().equals(deptCode) || deptCode.startsWith(dept.getCode())){
						ztree.setOpen(true);
					}
					ztree.setChecked(false);
					json.add(ztree);
				}
			}
		 
		return json;
	}
	
	/**
	 * 人员显示树
	 * @param request
	 * @param session
	 * @return
	 */
	/*@RequestMapping(value="/com/supermap/personTree")
	@ResponseBody
	public List<JsonZTree> personTree(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		
		String deptId = userEntity.getDeptId().getId();
		String treeId = request.getParameter("treeId");
		List<JsonZTree> list = personConsumer.getPersonTree(deptId, treeId);
		if(list == null || list.isEmpty()){
			JsonZTree ztree = new JsonZTree();
			ztree.setId(userEntity.getDeptId().getName());
			ztree.setpId("");
			ztree.setName(userEntity.getDeptId().getName());
			ztree.setEname(userEntity.getDeptId().getName());
			if(ztree.getName().length() > 7){
				ztree.setEname(ztree.getName().substring(0,7)+"..");
			}
			ztree.setOpen(true);
			ztree.setChecked(false);
			ztree.setIsParent(true);
			list.add(ztree);
		}
		return list;
	}*/
	
	/**
	 * 
	* 方法名: prsonSearch
	* 描述:人员部门树搜索 按姓名 or 手机号 or 终端号搜索
	* @param prsonSearch
	* @return
	 */
	/*@RequestMapping(value = "/com/supermap/personTreeSearch")
	@ResponseBody
	public List<JsonZTree> personTreeSearch(String personSearch,HttpSession session){
		UserEntity user = getUserSession(session);
		String deptCode = user.getDeptId().getCode();
		List<JsonZTree> list = personConsumer.personTreeSearch(deptCode, personSearch);
		if(list == null){
			return new ArrayList<JsonZTree>();
		}
		return list;
	}*/
	
	
	public List<JsonZTree> getcarTreeSearch(String userId,String carSearch){
		List<JsonZTree> list = new ArrayList<JsonZTree>();
		if (StringUtils.isBlank(carSearch)) {
			return list;
		}
		
		//根目录
		JsonZTree rootNode = new JsonZTree();
		rootNode.setId("111");
		rootNode.setpId("0");
		rootNode.setName("车辆信息");
		rootNode.setEname("车辆信息");
		rootNode.setOpen(true);
		rootNode.setIsParent(false);
		list.add(rootNode);
		try {
			
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("userId", userId);
			hm.put("carTreeSearch", carSearch);
			List<CarDept> cars = this.carService.queryCar(hm);
			if(cars == null || cars.isEmpty()){
				return list;
			}
			rootNode.setIsParent(true);
			for (CarDept carDept: cars){
				JsonZTree ztree = new JsonZTree();
				ztree.setId(carDept.getId());
				ztree.setpId("111");
				ztree.setName(carDept.getLicense());
				ztree.setEname(carDept.getLicense());
				if(carDept.getLicense().length() > 10){
					ztree.setEname(carDept.getLicense().substring(0,10)+"..");
				}
				ztree.setChecked(false);
				ztree.setIsParent(false);
				list.add(ztree);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}
	
}
