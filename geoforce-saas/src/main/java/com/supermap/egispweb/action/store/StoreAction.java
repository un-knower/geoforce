package com.supermap.egispweb.action.store;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStore;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStoreType;
//@Controller
public class StoreAction extends BaseAction{
	
	/**
	 * 跳转到门店页面
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/toStore")
	public String toStore(HttpServletRequest request,HttpSession session){
		List<PersonStoreType> storeTypeList = storeTypeConsumer.getStoreTypeList();
		request.setAttribute("TypeList", storeTypeList);
		return "store/storeView";
	}
	 /**
	  * 得到门店列表
	  * @param request
	  * @param session
	  * @return
	  */
	@RequestMapping(value="/com/supermap/storeList")
	@ResponseBody
	public Page getStoreList(String name,String deptCode,HttpServletRequest request,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		Page page = getPage(request);
		if(userEntity == null){
			return page;
		}
		return storeConsumer.getStoreList(name, deptCode, userEntity, page);
	}
	
	/**
	 * 未分配门店
	 * @param name
	 * @param personId
	 * @param deptCode
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/storeList/Unallocated")
	@ResponseBody
	public Page getUnallocatedStoreList(String name,String deptCode,HttpServletRequest request,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		Page page = getPage(request);
		if(userEntity == null){
			return page;
		}
		return storeConsumer.getUnallocatedStoreList( name, deptCode, userEntity, page);
	}
	
	/**
	 * 添加门店
	 * @param Store
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/addStore")
	@ResponseBody
	public AjaxResult addStore(@ModelAttribute("storeForm")PersonStore Store,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		if(userEntity == null){
			return new AjaxResult((short)1,"请用户登录");
		}
		return storeConsumer.addStore(Store, userEntity);
	}
	
	
	/**
	 * 修改门店
	 * @param Store
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/updateStore")
	@ResponseBody
	public AjaxResult updateStore(@ModelAttribute("storeForm")PersonStore Store,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		if(userEntity == null){
			return new AjaxResult((short)1,"请用户登录");
		}
		return storeConsumer.updateStore(Store, userEntity);
	}
	
	
	/**
	 * 根据人员id查询门店
	 * @param name
	 * @param personId
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/storeByPerson")
	@ResponseBody
	public Page getStoreListPerson(String name,String personId,HttpServletRequest request,HttpSession session){
		UserEntity userEntity = getUserSession(session);
		Page page = getPage(request);
		if(userEntity == null){
			return page;
		}
		return storeConsumer.getStoreListPerson(name, personId, page);
	}
	
	/**
	 * 删除门店
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/delStore")
	@ResponseBody
	public AjaxResult delStore(String storeIds){
		return storeConsumer.delStore(storeIds);
	}
	
	/**
	 * 返回门店信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/storeInfo")
	@ResponseBody
	public PersonStore getStoreInfo(String id){
		return storeConsumer.getStoreInfo(id);
	}
}
