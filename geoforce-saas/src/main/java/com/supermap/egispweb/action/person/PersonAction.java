package com.supermap.egispweb.action.person;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.DataWordbook;
import com.supermap.lbsp.provider.hibernate.lbsp.Person;
@Controller
public class PersonAction extends BaseAction{
	static Logger logger = Logger.getLogger(PersonAction.class.getName());
	
	/**
	 * 调整到人员管理页面
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/toPerson")
	public String toPerson(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		if(userEntity == null){
			return null;
		}
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("type", "12");
		List<com.supermap.egispservice.lbs.entity.DataWordbook> dataWorkList = this.dataworkService.getDataWordbookList(hm);
		InfoDeptEntity dept = userEntity.getDeptId();
		if(dept == null){
			return null;
		}
		request.setAttribute("dataWorkList", dataWorkList);
		request.setAttribute("dept", dept);
		return "person/personList";
	}
	
	
	/**
	 * 查询人员
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/personList")
	@ResponseBody
	public Page getPersonList(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		Page page = getPage(request);
		if(userEntity == null){
			return page;
		}
		return personConsumer.getPersonList(request, userEntity, page);
	}
	
	
	/**
	 * 添加人员
	 * @param request
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/addPerson")
	@ResponseBody
	public AjaxResult addPerson(@ModelAttribute("addPersonForm")Person person,HttpSession session){
		
		UserEntity userEntity = getUserSession(session);
		if(userEntity == null){
			return new AjaxResult((short)1,"请用户登录");
		}
		return personConsumer.addPerson(person, userEntity);
		 
	}
	
	
	/**
	 * 返回人员信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/personInfo")
	@ResponseBody
	public Person getPersonInfo(String id){
		return personConsumer.getPersonInfo(id);
	}
	
	
	/**
	 * 修改人员
	 * @param request
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/updatePerson")
	@ResponseBody
	public AjaxResult updatePerson(@ModelAttribute("addPersonForm")Person person,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		if(userEntity == null){
			return new AjaxResult((short)1,"请用户登录");
		}
		return personConsumer.updatePerson(person,  userEntity);
	}
	
	/**
	 * 删除人员
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/delPerson")
	@ResponseBody
	public AjaxResult delPerson(String personIds){
		return personConsumer.delPerson(personIds);
	}
	
	/**
	 * 分配门店
	 * @param personId
	 * @param storeIds
	 * @return
	 */
	@RequestMapping(value="/com/supermap/setStore")
	@ResponseBody
	public AjaxResult setPersonStore(String personId,String storeIds){
		return personConsumer.setPersonStore(personId,storeIds);
	}
	
	/**
	 * 删除员工所管门店
	 * @return
	 */
	@RequestMapping(value="/com/supermap/delPersonStore")
	@ResponseBody
	public AjaxResult delPersonStore(String personId,String storeIds){
		return personConsumer.delPersonStore(personId, storeIds);
		
	}
	
	/**
	 * 调整部门
	 * @param personIds
	 * @param deptid
	 * @return
	 */
	@RequestMapping(value="/com/supermap/person/changDept")
	@ResponseBody
	public AjaxResult changDept(String personIds,String deptid){
		return personConsumer.changDept(personIds, deptid);
	}
	
	/**
	 * 修改密码
	 * @param personId
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	@RequestMapping(value="/com/supermap/person/updatePwd")
	@ResponseBody
	public AjaxResult updatePwd(String personId,String oldPwd,String newPwd){
		return personConsumer.updatePwd(personId, oldPwd,newPwd);
	}
}
