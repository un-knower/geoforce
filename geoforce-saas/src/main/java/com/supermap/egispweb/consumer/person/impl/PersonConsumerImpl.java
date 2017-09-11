package com.supermap.egispweb.consumer.person.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.consumer.person.PersonConsumer;
import com.supermap.egispweb.util.Md5Util;
import com.supermap.lbsp.provider.bean.JsonZTree;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.info.Dept;
import com.supermap.lbsp.provider.hibernate.lbsp.Person;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStoreForeign;
import com.supermap.lbsp.provider.service.dept.DeptService;
import com.supermap.lbsp.provider.service.person.PersonService;
import com.supermap.lbsp.provider.service.person.PersonStoreService;

@Component("personConsumer")
public class PersonConsumerImpl implements PersonConsumer{
	static Logger logger = Logger.getLogger(PersonConsumerImpl.class.getName());
	@Reference(version="2.5.3")
	private PersonService personService;
	@Reference(version="2.5.3")
	private DeptService deptService;
	@Reference(version="2.5.3")
	private PersonStoreService personStoreService;

	@Override
	public Page getPersonList(HttpServletRequest request, UserEntity userEntity, Page page) {
		
		HashMap<String,Object> hm = new HashMap<String, Object>();
		String name = request.getParameter("name");
		String deptcode = request.getParameter("deptCode");
		String eid = userEntity.getEid().getId();
		if(StringUtils.isBlank(deptcode)){
			deptcode = userEntity.getDeptId().getCode();
		}
		List<String> detptIds =  deptcodeConvertDeptId(deptcode);
		if(detptIds!= null && detptIds.size()>0){
			hm.put("deptIds", detptIds);
		}
		if(StringUtils.isNotBlank(eid)){
			hm.put("eid", eid);
		}
		if(StringUtils.isNotBlank(name)){
			hm.put("nickName", name);
			
		}
		page = personService.queryPerson(page, hm);
		return page;
	}
	
	

	@Override
	public AjaxResult addPerson(Person person, UserEntity userEntity) {
		AjaxResult res = new AjaxResult();
		if(StringUtils.isBlank(person.getPassword())){
			res.setStatus((short)0);
			res.setInfo("密码不能为空");
			return res;
		}
		String username = person.getName();
		if(StringUtils.isBlank(username)){
			res.setStatus((short)0);
			res.setInfo("账号不能为空");
			return res;
		}
		if(StringUtils.isBlank(person.getEmail())){
			res.setStatus((short)0);
			res.setInfo("邮箱不能为空");
			return res;
		}
		
		String eid = userEntity.getEid().getId();
		int row = personService.hasName(username, eid);
		if(row>0){
			res.setStatus((short)0);
			res.setInfo("账号重复");
			return res;
		}
		String temrcode = person.getTermcode();
		if( personService.hasTerminal(temrcode, eid)>0){
			res.setStatus((short)0);
			res.setInfo("终端号重复");
			return res;
		}
		person.setEid(userEntity.getEid().getId());	
		person.setUserId(userEntity.getId());	
		person.setOperDate(new Date());
		person.setPassword(Md5Util.md5(person.getPassword()));
		Person newPerson = new Person();
		BeanUtils.copyProperties(person, newPerson);
		Person obj = personService.addPerson(newPerson);
		if(obj != null){
			res.setStatus((short)1);
			res.setInfo("添加成功");
		}
		return res;
	}
	
	
	@Override
	public AjaxResult updatePerson(Person person,UserEntity userEntity) {
		AjaxResult res = new AjaxResult();
		if(StringUtils.isBlank(person.getPassword())){
			res.setStatus((short)0);
			res.setInfo("密码不能为空");
			return res;
		}
		String username = person.getName();
		if(StringUtils.isBlank(username)){
			res.setStatus((short)0);
			res.setInfo("账号不能为空");
			return res;
		}
		
		if(StringUtils.isBlank(person.getEmail())){
			res.setStatus((short)0);
			res.setInfo("邮箱不能为空");
			return res;
		}
		String eid = userEntity.getEid().getId();
		Person personDb = personService.getPerson(person.getId());
		if(!username.equals(personDb.getName())&& personService.hasName(username, eid)>0){
			res.setStatus((short)0);
			res.setInfo("账号重复");
			return res;
		}
		String temrcode = person.getTermcode();
		if(!temrcode.equals(personDb.getTermcode())&& personService.hasTerminal(temrcode, eid)>0){
			res.setStatus((short)0);
			res.setInfo("终端号重复");
			return res;
		}
		
		personDb.setAge(person.getAge());
		personDb.setDeptId(person.getDeptId());
		personDb.setEmail(person.getEmail());
		personDb.setName(person.getName());
		personDb.setNickname(person.getNickname());
		personDb.setPhone(person.getPhone());
		personDb.setPosition(person.getPosition());
		personDb.setSex(person.getSex());
		personDb.setTermcode(person.getTermcode());
		personDb.setTermtype(person.getTermtype());
		personDb.setUserId(userEntity.getId());
		personDb.setOperDate(new Date());
		int falg = personService.updatePerson(personDb);
		if(falg==0){
			res.setStatus((short)0);
			res.setInfo("修改失败");
			return res;
		}
		res.setStatus((short)1);
		res.setInfo("修改成功");
		return res;
	}
	
	@Override
	public Person getPersonInfo(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		return personService.getPerson(id);
		 
	}
	
	/**
	 * 部门code转化id
	 * @param deptcode
	 * @return
	 */
	private List<String> deptcodeConvertDeptId( String deptcode){
		
		if(deptcode != null){
			HashMap<String,Object> deptHm = new HashMap<String, Object>();
			deptHm.put("code", deptcode);
			List<String> listDeptId = new ArrayList<String>();
			List listDept = this.deptService.queryDept(null, deptHm);
			if(listDept != null && listDept.size()>0){
				for (int i = 0; i < listDept.size(); i++) {
					Dept dept = (Dept)listDept.get(i);
					listDeptId.add(dept.getId());
				}
				return listDeptId;
			}
			
			return null;
			
		}
		return null;
		
	}



	@Override
	public AjaxResult delPerson(String personIds) {
		if(StringUtils.isBlank(personIds)){
			return new AjaxResult((short)0,"请选择要删除的人员");
		}
		String[] idArr = personIds.split(",");
		List<String> errperson = new ArrayList<String>();
		for (int i = 0; i < idArr.length; i++) {
			Person person = personService.getPerson(idArr[i]);
			if(person !=null){
				int row = personService.delPerson(person);
				if(row!=0){
					personStoreService.delPersonStoreByPersonId(idArr[i]);
				}else{
					errperson.add(idArr[i]);
				}
			}else{
				errperson.add(idArr[i]);
			}
		}
		
		if(errperson.size()>0){
			return new AjaxResult((short)0,"有"+errperson.size()+"个人员删除失败");
		}
		return new AjaxResult((short)1,"操作成功");
	}



	@Override
	public AjaxResult setPersonStore(String personId, String storeIds) {
	    String[] storeIdArr = storeIds.split(",");
	    if(StringUtils.isBlank(storeIds)){
	    	return new AjaxResult((short)0,"请选择要分配的门店");
		}
	    for (int i = 0; i < storeIdArr.length; i++) {
	    	PersonStoreForeign personStoreForeign = new PersonStoreForeign();
	    	personStoreForeign.setPersonId(personId);
	    	personStoreForeign.setStoreId(storeIdArr[i]);
	    	personStoreService.addPersonStoreDao(personStoreForeign);
		}
		return new AjaxResult((short)1,"绑定成功");
	}



	@Override
	public AjaxResult changDept(String personIds, String deptid) {
		if(StringUtils.isBlank(personIds)){
			return new AjaxResult((short)0,"请选择要调整部门的人员");
		}
		String[] personIdArr = personIds.split(",");
		List<String> errperson = new ArrayList<String>();
		for (int i = 0; i < personIdArr.length; i++) {
			String id = personIdArr[i];
			Person person = personService.getPerson(id);
			if(person != null){
				person.setDeptId(deptid);
				personService.updatePerson(person);
			}else{
				errperson.add(id);
			}
		}
		if(errperson.size()>0){
			return new AjaxResult((short)0,"有"+errperson.size()+"个人员修改部门失败");
		}
		return new AjaxResult((short)1,"操作成功");
	}



	@Override
	public AjaxResult delPersonStore(String personId, String storeIds) {
		if(StringUtils.isBlank(personId)){
			new AjaxResult((short)0,"请选择员工");
		}
		if(StringUtils.isBlank(storeIds)){
			new AjaxResult((short)0,"请选择要删除的门店");
		}
		String[] storeIdArr = storeIds.split(",");
		for (int i = 0; i < storeIdArr.length; i++) {
			personStoreService.delPersonStoreDao(personId, storeIdArr[i]);
		}
		
		return new AjaxResult((short)1,"操作成功");
	}



	@Override
	public List<JsonZTree> personTreeSearch(String deptCode, String personSearch) {
		List<JsonZTree> list = new ArrayList<JsonZTree>();
		if (StringUtils.isBlank(personSearch)) {
			return list;
		}
		if(StringUtils.isBlank(deptCode)){
			return list;
		}
		HashMap<String,Object> deptHm = new HashMap<String, Object>();
		deptHm.put("code", deptCode);
		List<Dept> listDept = this.deptService.queryDept(null, deptHm);
		if(listDept == null || listDept.isEmpty()){
			return list;
		}
		List<String> deptIds = new ArrayList<String>();
		for (Dept dept:listDept) {
			deptIds.add(dept.getId());
		}
		//根目录
		JsonZTree rootNode = new JsonZTree();
		rootNode.setId("111");
		rootNode.setpId("0");
		rootNode.setName("人员信息");
		rootNode.setEname("人员信息");
		rootNode.setOpen(true);
		rootNode.setIsParent(false);
		list.add(rootNode);
		try {
			
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("deptIds", deptIds);
			hm.put("personTreeSearch", personSearch);
			List<Person> personList = personService.queryPersonList(null, hm);
			if(personList == null || personList.isEmpty()){
				return list;
			}
			rootNode.setIsParent(true);
			for (Person person: personList){
				JsonZTree ztree = new JsonZTree();
				ztree.setId(person.getId());
				ztree.setpId("111");
				ztree.setName(person.getNickname());
				ztree.setEname(person.getNickname());
				if(person.getNickname().length() > 10){
					ztree.setEname(person.getNickname().substring(0,10)+"..");
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



	@Override
	public List<JsonZTree> getPersonTree(String deptId, String treeId) {
		// TODO Auto-generated method stub
		return personService.personTree(deptId, treeId);
	}



	@Override
	public AjaxResult updatePwd(String personId, String oldPwd, String newPwd) {
		if(StringUtils.isBlank(personId)){
			return	new AjaxResult((short)0,"personId is null");
		}
		if(StringUtils.isBlank(oldPwd)){
			return new AjaxResult((short)0,"请输入原密码");
		}
		if(StringUtils.isBlank(newPwd)){
			return new AjaxResult((short)0,"请输入新密码");
		}
		Person person  = personService.getPerson(personId);
		if(person == null){
			return new AjaxResult((short)0,"无此人员");
		}
		
		if(!Md5Util.md5(oldPwd).equals(person.getPassword())){
			return new AjaxResult((short)0,"原密码有误");
		}
		
		
		person.setPassword(Md5Util.md5(newPwd));
		int falg = personService.updatePerson(person);
		if(falg == 0){
			return new AjaxResult((short)0,"修改失败");
		}
		return new AjaxResult((short)1,"修改成功");
	}



	



	
}
