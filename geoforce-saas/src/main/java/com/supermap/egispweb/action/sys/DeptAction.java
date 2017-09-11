package com.supermap.egispweb.action.sys;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.base.service.UserService;

@Controller
@RequestMapping(value = "/dept")
@SessionAttributes("user")
public class DeptAction {

	@Autowired
	private InfoDeptService infoDeptService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/show")
	public String show() {
		return "sys/dept";
	}

	@RequestMapping(value = "/getChildDepts")
	@ResponseBody
	public List<InfoDeptEntity> getChildDepts(HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		return infoDeptService.getChildDepts(user.getDeptId().getId());
	}

	@RequestMapping(value = "/childDept")
	@ResponseBody
	public List<InfoDeptEntity> getChildDept(HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		return infoDeptService.getChildDepts(user.getDeptId().getId());
	}

	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getDepts(String name, int page, int rows,
			String sord, HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		return infoDeptService.getDepts(name, page, rows, sord, user
				.getDeptId().getId());
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveDept(String parentId, String name,
			String phone, String headName, String headPhone, String address,
			String zipcode, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);

		try {
			if (infoDeptService
					.isReplicationNameInTheSameParent(name, parentId)) {
				map.put("errorInfo", "该部门已存在！");
				return map;
			}

			UserEntity user = (UserEntity) session.getAttribute("user");

			InfoDeptEntity infoDeptEntity = new InfoDeptEntity(name, headName,
					headPhone, address, phone, zipcode, parentId, (byte) 1,
					"0", user.getId(), new Date());
			infoDeptService.saveDept(infoDeptEntity);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorInfo", "操作失败！");
		}
		return map;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateDept(String id, String name, String phone,
			String headName, String headPhone, String address, String zipcode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		try {
			InfoDeptEntity infoDeptEntity = infoDeptService.findDeptById(id);
			if (!infoDeptEntity.getName().equals(name)
					&& infoDeptService.isReplicationNameInTheSameParent(name,
							infoDeptEntity.getParentId())) {
				map.put("errorInfo", "该部门已存在！");
				return map;
			}

			infoDeptEntity.setName(name);
			infoDeptEntity.setPhone(phone);
			infoDeptEntity.setHeadName(headName);
			infoDeptEntity.setHeadPhone(headPhone);
			infoDeptEntity.setAddress(address);
			infoDeptEntity.setZipcode(zipcode);
			infoDeptEntity.setOperDate(new Date());

			infoDeptService.updateDept(infoDeptEntity);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorInfo", "操作失败！");
		}
		return map;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteDept(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		try {
			String[] ids = id.split(",");
			for (String deptId : ids) {
				List<InfoDeptEntity> depts = infoDeptService
						.getChildDepts(deptId);
				List<UserEntity> uses=this.userService.getUsersByCurrentDept(deptId);
				if ((depts != null && depts.size() > 1)||(null!=uses&&uses.size()>0)) {
					map.put("errorInfo", "操作失败，请先删除部门下用户或子部门！");
					return map;
				}
			}
			infoDeptService.deleteDeptByIds(id);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorInfo", "操作失败！");
		}
		return map;
	}

}
