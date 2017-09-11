package com.supermap.egispweb.consumer.person;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.bean.JsonZTree;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Person;

public interface PersonConsumer {
	
	/**
	 * 返回人员列表
	 * @param request
	 * @param userEntity
	 * @param page
	 * @return
	 */
	public Page getPersonList(HttpServletRequest request, UserEntity userEntity, Page page);
	
	/**
	 * 添加人员
	 * @param request
	 * @param userEntity
	 * @return
	 */
	public AjaxResult addPerson(Person person, UserEntity userEntity);
	
	/**
	 * 修改人员
	 * @param person
	 * @param userEntity
	 * @return
	 */
	public AjaxResult updatePerson(Person person, UserEntity userEntity);
	
	/**
	 * 根据用户信息返回用户信息
	 * @param id
	 * @return
	 */
	public Person getPersonInfo(String id);
	
	/**
	 * 删除员工
	 * @param personId
	 * @return
	 */
	public AjaxResult delPerson(String personIds);
	
	/**
	 * 给员工分配门店
	 * @param personId
	 * @param storeIds
	 * @return
	 */
	public AjaxResult setPersonStore(String personId,String storeIds);
	
	/**
	 * 删除员工所管门店
	 * @param personId
	 * @param storeIds
	 * @return
	 */
	public AjaxResult delPersonStore(String personId,String storeIds);
	
	/**
	 * 批量改变部门
	 * @param personIds
	 * @param deptid
	 * @return
	 */
	public AjaxResult changDept(String personIds,String deptid);
	
	/**
	 * 搜索人员树
	 * @param deptCode
	 * @param personSearch
	 * @return
	 */
	public List<JsonZTree> personTreeSearch(String deptCode,String personSearch);
	
	public List<JsonZTree> getPersonTree(String deptId, String treeId);
	
	public AjaxResult updatePwd(String personId,String oldPwd,String newPwd);
	
}
