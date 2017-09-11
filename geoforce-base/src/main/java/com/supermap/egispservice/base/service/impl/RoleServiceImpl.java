package com.supermap.egispservice.base.service.impl;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.supermap.egispservice.base.dao.IPrivilegeDao;
import com.supermap.egispservice.base.dao.IRoleDao;
import com.supermap.egispservice.base.dao.IRoleStatusDao;
import com.supermap.egispservice.base.entity.PrivlegeEntity;
import com.supermap.egispservice.base.entity.RoleEntity;
import com.supermap.egispservice.base.entity.RoleStatusEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BasePrivilegeListItem;
import com.supermap.egispservice.base.pojo.BaseRoleList;
import com.supermap.egispservice.base.pojo.BaseRoleListItem;
import com.supermap.egispservice.base.pojo.RoleFieldNames;
import com.supermap.egispservice.base.service.IRoleService;



@Component("roleService")
public class RoleServiceImpl implements IRoleService {

	@Resource
	private IRoleDao roleDao;
	@Resource
	private IPrivilegeDao privilegeDao;
	@Resource
	private IRoleStatusDao roleStatusDao;
	
	@Override
	@Transactional
	public String addRole(String name, String status, String privilegeIds[], String remarks) throws ParameterException {
		RoleEntity roleEntity = this.roleDao.findByName(name);
		if(null != roleEntity){
			throw new ParameterException(ExceptionType.EXISTED_DATA,RoleFieldNames.NAME+":"+name );
		}
		roleEntity = new RoleEntity();
		// 查询权限
		for(String id:privilegeIds){
			PrivlegeEntity pe = privilegeDao.findById(id);
			if(null == pe){
//				throw new ParameterException("权限ID["+id+"]未查询到权限");
				continue;
			}else{
				roleEntity.addPrivilege(pe);
			}
		}
		roleEntity.setName(name);
		if(!StringUtils.isEmpty(status)){
			RoleStatusEntity statusEntity = this.roleStatusDao.findByValue(status);
			roleEntity.setStatus(statusEntity);
		}
		if(!StringUtils.isEmpty(remarks)){
			roleEntity.setRemarks(remarks);
		}
		this.roleDao.save(roleEntity);
		return roleEntity.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public BaseRoleList queryRoleList(String idStr, String name, int pageNo, int pageSize) throws ParameterException {
		BaseRoleList roleList = null;
		// 根据ID进行查询
		if(!StringUtils.isEmpty(idStr)){
			RoleEntity oe = this.roleDao.findById(idStr);
			if(null == oe){
				throw new ParameterException(ExceptionType.NOT_FOUND,RoleFieldNames.ID+":"+idStr);
			}
			roleList = new BaseRoleList();
			roleList.setTotal(1);
			roleList.setCurrentCount(1);
			BaseRoleListItem item = entity2Item(oe);
			roleList.setItems(new BaseRoleListItem[]{item});
			return roleList;
		}
		
		if(pageNo>=0){//分页
			PageRequest page=new PageRequest(pageNo, pageSize);
			Page<RoleEntity> pageresult=null;
			if(!StringUtils.isEmpty(name)){
				pageresult=this.roleDao.findByNameLike("%"+name+"%", page);
			}else{
				pageresult=this.roleDao.findAll(page);
			}
			
			if(pageresult!=null&&pageresult.getContent()!=null){
				roleList = new BaseRoleList();
				roleList.setCurrentCount(pageresult.getContent().size());
				roleList.setTotal((int)pageresult.getTotalElements());
				roleList.setItems(entitys2Items(pageresult.getContent()));
			}
		}else{
			List<RoleEntity> role=new ArrayList<RoleEntity>();
			if(!StringUtils.isEmpty(name)){
				role=this.roleDao.findByNameLikeOrStatus("%"+name+"%", null);
			}else{
				role=(List<RoleEntity>) this.roleDao.findAll();
			}
			if(role!=null&&role.size()>0){
				roleList = new BaseRoleList();
				roleList.setCurrentCount(role.size());
				roleList.setTotal(role.size());
				roleList.setItems(entitys2Items(role));
			}
		}
		return roleList;
	}
	/**
	 * 
	 * <p>Title ：entitys2Items</p>
	 * Description：		将实体对象列表转换为前端角色列表
	 * @param res
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 下午03:18:38
	 */
	private BaseRoleListItem[] entitys2Items(List<RoleEntity> res){
		BaseRoleListItem[] items = new BaseRoleListItem[res.size()];
		for(int i=0;i<res.size();i++){
			items[i] = entity2Item(res.get(i));
		}
		return items;
	}
	
	
	/**
	 * 
	 * <p>Title ：entity2Item</p>
	 * Description：将角色实体转换为列表项
	 * @param re
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-20 下午03:08:57
	 */
	private BaseRoleListItem entity2Item(RoleEntity re){
		BaseRoleListItem item = new BaseRoleListItem();
		item.setId(re.getId());
		item.setName(re.getName());
		item.setStatus(re.getStatus().getValue());
		Set<PrivlegeEntity> privileges = re.getPrivileges();
		if(null != privileges && privileges.size() > 0){
			BasePrivilegeListItem pItems[] = new BasePrivilegeListItem[privileges.size()];
			Iterator iterator = privileges.iterator();
			int i = 0;
			while(iterator.hasNext()){
				PrivlegeEntity pe = (PrivlegeEntity) iterator.next();
				BasePrivilegeListItem pItem = entity2Item(pe);
				pItems[i++] = pItem;
			}
			item.setPrivileges(pItems);
		}
		return item;
	}
	/**
	 * 将实体对象转换为列表项
	 * @param pe
	 * @return
	 */
	private BasePrivilegeListItem entity2Item(PrivlegeEntity pe){
		BasePrivilegeListItem item = new BasePrivilegeListItem();
		item.setCode(pe.getCode());
		item.setId(pe.getId());
		item.setLevel(pe.getLevel());
		item.setName(pe.getName());
		item.setUrl(pe.getUrl());
		item.setStatus(pe.getStatus().getStatus());
		item.setRemarks(pe.getRemarks());
		if(null != pe.getParent()){
			item.setPid(pe.getParent().getId());
		}
		return item;
	}

	@Override
	@Transactional
	public void updateRole(String id, String status, String remarks) throws ParameterException {
		RoleEntity roleEntity = this.roleDao.findById(id);
		if(null == roleEntity){
			throw new ParameterException(ExceptionType.NOT_FOUND,RoleFieldNames.ID+":"+id);
		}
		boolean isUpdated = false;
		if(!StringUtils.isEmpty(status) && !status.equals(roleEntity.getStatus().getValue())){
			RoleStatusEntity statusEntity = this.roleStatusDao.findByValue(status);
			if(null == statusEntity){
				statusEntity = new RoleStatusEntity();
				statusEntity.setValue(status);
				this.roleStatusDao.save(statusEntity);
			}
			roleEntity.setStatus(statusEntity);
			isUpdated = true;
		}
		if(!StringUtils.isEmpty(remarks) && !remarks.equals(roleEntity.getRemarks())){
			roleEntity.setRemarks(remarks);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		if(!isUpdated){
			throw new ParameterException("未发现可更新的内容");
		}
		
	}

	@Override
	@Transactional
	public void addPrivileges(String id, String[] privilegeIds) throws ParameterException {
		RoleEntity re = this.roleDao.findById(id);
		if(null == re){
			throw new ParameterException(ExceptionType.NOT_FOUND,RoleFieldNames.ID+":"+id);
		}
		for(String privilegeId : privilegeIds){
			PrivlegeEntity pe = this.privilegeDao.findById(privilegeId);
			if(null == pe){
				throw new ParameterException(ExceptionType.NOT_FOUND,"权限ID["+privilegeId+"]");
			}
			re.addPrivilege(pe);
		}
		this.roleDao.save(re);
	}

	@Override
	@Transactional
	public void removePrivileges(String id, String[] privilegeIds) throws ParameterException {
		RoleEntity re = this.roleDao.findById(id);
		if(null == re){
			throw new ParameterException(ExceptionType.NOT_FOUND,RoleFieldNames.ID+":"+id);
		}
		for(String privilegeId : privilegeIds){
			PrivlegeEntity pe = this.privilegeDao.findById(privilegeId);
			if(null != pe){
				re.removePrivilege(pe);
			}
		}
		this.roleDao.save(re);
		
	}

	@Override
	@Transactional
	public void deleteRole(String id) throws ParameterException {
		this.roleDao.delete(id);
	}

	@Override
	@Transactional
	public void addOrRmPrivileges(String id, String[] privilegeIds) throws ParameterException {
		RoleEntity re = this.roleDao.findById(id);
		if(null == re){
			throw new ParameterException(ExceptionType.NOT_FOUND,RoleFieldNames.ID+":"+id);
		}
		re.clearPrivileges();
		for(String privilegeId : privilegeIds){
			PrivlegeEntity pe = this.privilegeDao.findById(privilegeId);
			re.addPrivilege(pe);
		}
		this.roleDao.save(re);
		
	}

}
