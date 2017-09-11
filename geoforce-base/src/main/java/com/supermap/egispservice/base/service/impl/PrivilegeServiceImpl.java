package com.supermap.egispservice.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.supermap.egispservice.base.constants.EbossStatusConstants;
import com.supermap.egispservice.base.dao.IPrivilegeDao;
import com.supermap.egispservice.base.dao.IPrivilegeStatusDao;
import com.supermap.egispservice.base.entity.PrivilegeStatusEntity;
import com.supermap.egispservice.base.entity.PrivlegeEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BasePrivilegeList;
import com.supermap.egispservice.base.pojo.BasePrivilegeListItem;
import com.supermap.egispservice.base.pojo.PrivilegeFieldNames;
import com.supermap.egispservice.base.service.IPrivilegeService;


@Component("privilegeService")
public class PrivilegeServiceImpl implements IPrivilegeService {

	@Resource
	private IPrivilegeDao privilegeDao;
	
	@Resource
	private IPrivilegeStatusDao privilegeStatusDao;
	
	@Override
	@Transactional
	public String add(String name, String code,  String status, String remark, String pid,String url)
			throws ParameterException {
		PrivlegeEntity pe = this.privilegeDao.findByName(name);
		// 判断是否名称已经存在
		if(null != pe){
			throw new ParameterException(ExceptionType.EXISTED_DATA,PrivilegeFieldNames.NAME+":"+name);
		}
		
		// 判断代码是否已经存在
		pe = this.privilegeDao.findByCode(code);
		if(null != pe){
			throw new ParameterException(ExceptionType.EXISTED_DATA, PrivilegeFieldNames.CODE+":"+code);
		}
		
		pe = new PrivlegeEntity();
		pe.setName(name);
		pe.setUrl(url);
		if(!StringUtils.isEmpty(status)){
			PrivilegeStatusEntity statusEntity = null;
			if(!StringUtils.isEmpty(status)){
				statusEntity = this.privilegeStatusDao.findByStatus(status);
				if(statusEntity == null){
					statusEntity = new PrivilegeStatusEntity();
					statusEntity.setStatus(EbossStatusConstants.PRIVILEGE_STATUS_USING);
					this.privilegeStatusDao.save(statusEntity);
				}
			}
			pe.setStatus(statusEntity);
		}
		pe.setCode(code);
		if(!StringUtils.isEmpty(remark)){
			pe.setRemarks(remark);
		}
		
		// 
		if(!StringUtils.isEmpty(pid)){
			// 如果存在父权限，则添加父权限
			PrivlegeEntity parent = this.privilegeDao.findById(pid);
			if(null == parent){
				throw new ParameterException(ExceptionType.NOT_FOUND,PrivilegeFieldNames.PID+":"+pid);
			}
			pe.setParent(parent);
			pe.setLevel(parent.getLevel() + 1);
		}
		this.privilegeDao.save(pe);
		return pe.getId();
	}
	

	@Override
	@Transactional(readOnly = true)
	public BasePrivilegeList fuzzyQuery(String idStr,final String levelStr, final String name, final String code,int pageNo,int pageSize) throws ParameterException {
		BasePrivilegeList privilegeList = null;
		PrivlegeEntity pe = null;
		if(!StringUtils.isEmpty(idStr)){
			pe = this.privilegeDao.findById(idStr);
			if(null != pe){
				privilegeList = new BasePrivilegeList();
				privilegeList.setTotal(1);
				privilegeList.setCurrentCount(1);
				BasePrivilegeListItem item = entity2Item(pe);
				privilegeList.setItems(new BasePrivilegeListItem[]{item});
				return privilegeList;
			}
		}
//		List<PrivlegeEntity> entitys = null;
		// 按级别查询
//		if(!StringUtils.isEmpty(levelStr)){
//			entitys = this.privilegeDao.findByLevel(Integer.parseInt(levelStr));
//		}
		
		
		Specification<PrivlegeEntity> spec=new Specification<PrivlegeEntity>() {
			@Override
			public Predicate toPredicate(Root<PrivlegeEntity> root,
					CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				Path<String> namepath=root.get("name");
				Path<String> codepath=root.get("code");
				Path<String> levelpath=root.get("level");
				
				List<Predicate> prelist=new ArrayList<Predicate>();
				 
				if(!StringUtils.isEmpty(levelStr)){
					prelist.add(builder.equal(levelpath, levelStr));
				}
				
				if(!StringUtils.isEmpty(name)){
					prelist.add(builder.like(namepath, "%"+name+"%"));
				}
				
				if(!StringUtils.isEmpty(code)){
					prelist.add(builder.like(codepath, "%"+code+"%"));
				}
				
				Predicate[] prearray=new Predicate[prelist.size()];
				prelist.toArray(prearray);
				query.where(prearray);
				return null;
			}
		};
		
		if(pageNo<0){//不分页
			List<PrivlegeEntity>  list=this.privilegeDao.findAll(spec);
			if(list!=null&&list.size()>0){
				privilegeList = new BasePrivilegeList();
				privilegeList.setTotal(list.size());
				privilegeList.setCurrentCount(list.size());
				privilegeList.setItems(entitys2Items(list));
			}
		}else{
			PageRequest page=new PageRequest(pageNo, pageSize);
			Page<PrivlegeEntity>  pageresult=this.privilegeDao.findAll(spec, page);
			if(pageresult!=null&&pageresult.getContent()!=null){
				privilegeList = new BasePrivilegeList();
				privilegeList.setTotal((int)pageresult.getTotalElements());
				privilegeList.setCurrentCount(pageresult.getContent().size());
				privilegeList.setItems(entitys2Items(pageresult.getContent()));
			}
		}
		return privilegeList;
	}
	
	
	/**
	 * 将实体列表转换为前端数组
	 * @param entitys
	 * @return
	 */
	private BasePrivilegeListItem[] entitys2Items(List<PrivlegeEntity> entitys){
		BasePrivilegeListItem[] items = new BasePrivilegeListItem[entitys.size()];
		for(int i=0;i<entitys.size();i++){
			PrivlegeEntity pe = entitys.get(i);
			items[i] = entity2Item(pe);
		}
		return items;
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
	@Transactional(readOnly = true)
	public BasePrivilegeListItem queryById(String id) throws ParameterException {
		PrivlegeEntity pe = this.privilegeDao.findById(id);
		if(null == pe){
			throw new ParameterException(ExceptionType.NOT_FOUND,PrivilegeFieldNames.ID+":"+id);
		}
		BasePrivilegeListItem item = entity2Item(pe);
		return item;
	}

	@Override
	@Transactional
	public void updatePrivilege(String id, String statusStr, String url, String remarks) throws ParameterException {
		PrivlegeEntity pe = this.privilegeDao.findById(id);
		if(null == pe){
			throw new ParameterException(ExceptionType.NOT_FOUND, PrivilegeFieldNames.ID+":"+id);
		}
		boolean isUpdated = false;
		// 修改状态
		if(!StringUtils.isEmpty(statusStr)){
//			PrivilegeStatus status = parseStatus(statusStr);
			PrivilegeStatusEntity statusEntity = this.privilegeStatusDao.findByStatus(statusStr);
			if(null == statusEntity){
				statusEntity = new PrivilegeStatusEntity();
				statusEntity.setStatus(statusStr);
				this.privilegeStatusDao.save(statusEntity);
			}
			
			if(!statusStr.equals(pe.getStatus().getStatus())){
				pe.setStatus(statusEntity);
				isUpdated = true;
			}
		}
		// 修改URL
		if(!StringUtils.isEmpty(url) && !url.equals(pe.getUrl())){
			pe.setUrl(url);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		
		// 修改备注
		if(!StringUtils.isEmpty(remarks) && !remarks.equals(pe.getRemarks())){
			pe.setRemarks(remarks);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		if(!isUpdated){
			throw new ParameterException("未发现需要更新的内容");
		}
		
	}

	@Override
	@Transactional
	public void deletePrivilege(String id) throws ParameterException {
		PrivlegeEntity pe = this.privilegeDao.findById(id);
		if(null == pe){
			throw new ParameterException(ExceptionType.NOT_FOUND,PrivilegeFieldNames.ID+":"+id);
		}
		this.privilegeDao.delete(id);
	}

}
