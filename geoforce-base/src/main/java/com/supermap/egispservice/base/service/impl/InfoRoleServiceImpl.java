package com.supermap.egispservice.base.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.supermap.egispservice.base.dao.InfoRoleDao;
import com.supermap.egispservice.base.entity.InfoRoleEntity;
import com.supermap.egispservice.base.entity.InfoRoleFunctionEntity;
import com.supermap.egispservice.base.entity.MenuVO;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.entity.UserRoleMapEntity;
import com.supermap.egispservice.base.entity.ZtreeVO;
import com.supermap.egispservice.base.service.InfoRoleFunctionService;
import com.supermap.egispservice.base.service.InfoRoleService;
import com.supermap.egispservice.base.service.UserRoleMapService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.base.util.BeanTool;


/**
 * 
 * @description 角色
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-9-15
 * @version 1.0
 */
@Transactional
@Service
public class InfoRoleServiceImpl implements InfoRoleService{
    
    
    @Autowired
    private InfoRoleDao infoRoleDao;
    @Autowired
    private UserService userService;
    @Autowired
    private InfoRoleFunctionService infoRoleFunctionService;
    @Autowired
    private UserRoleMapService userRoleMapService;
    /**
     * 菜单显示与部门管理员角色授权时查找当前用户拥有的角色
     */
    @Override
    public List<InfoRoleEntity> findRolesByEidAndUserid(final String userid,final String eid){
    	Specification<InfoRoleEntity> spec = new Specification<InfoRoleEntity>() {
    		@Override
    		public Predicate toPredicate(Root<InfoRoleEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> roleid2=root.get("id");
    			Path<String> eid2=root.get("eid"); 
    			List<String> roleids=new ArrayList<String>();
    			List<UserRoleMapEntity> userRoleMapEntities= userRoleMapService.findByUserId(userid);
    			for (UserRoleMapEntity userRoleMapEntity : userRoleMapEntities) {
    				roleids.add(userRoleMapEntity.getRoleId());
				}
    			
    			Predicate roleidPredicate=roleid2.in(roleids);
    			predicateList.add(roleidPredicate);
    			
    			Predicate eidPredicate=builder.equal(eid2, eid);
    			predicateList.add(eidPredicate);
    			
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    		}
    		
    	};
    	return infoRoleDao.findAll(spec);
    }
    /**
     * 使用企业ID与部门ID，查找部门下的所有角色
     */
    @Override
    public List<InfoRoleEntity> findByEidAndDepts(final String userid,final String eid,final String deptId){
    	Specification<InfoRoleEntity> spec = new Specification<InfoRoleEntity>() {
			@Override
			public Predicate toPredicate(Root<InfoRoleEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				Path<String> createUserid=root.get("createUserid");
				Path<String> eid2=root.get("eid"); 
				List<String> userids=new ArrayList<String>();
				userids.add(userid);
				List<UserEntity> userEntities= userService.getUsersByDept(deptId); 
				for (UserEntity userEntity : userEntities) {
					userids.add(userEntity.getId());
				}
				
				Predicate createUseridPredicate=createUserid.in(userids);
				predicateList.add(createUseridPredicate);
				
				Predicate eidPredicate=builder.equal(eid2, eid);
				predicateList.add(eidPredicate);
				
				Predicate [] predicates=new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				query.where(predicates);
				return null;
			}
			
		};
		return infoRoleDao.findAll(spec);
    }
    
    @Override
	public List<ZtreeVO> getCheckedMenu(String userid,String eid ,String deptId ,String roleId,Byte sourceId) {
		List<MenuVO> listMenuVOs=userService.getMenu(userid, eid, deptId,sourceId);
		List<InfoRoleFunctionEntity> lisInfoRoleFunctionEntities= infoRoleFunctionService.findByRoleId(roleId);
		List<ZtreeVO> ztreeVOs=new ArrayList<ZtreeVO>();
		for (MenuVO menuVO : listMenuVOs) {
			ZtreeVO ztreeVO=new ZtreeVO();
			ztreeVO.setId(menuVO.getId());
			ztreeVO.setPid(menuVO.getPid());
			ztreeVO.setName(menuVO.getMenuName());
			for (InfoRoleFunctionEntity infoRoleFunctionEntity : lisInfoRoleFunctionEntities) {
				if (menuVO.getId().equals(infoRoleFunctionEntity.getFunId())) {
					ztreeVO.setChecked(true);
					break;
				}
			}
			ztreeVOs.add(ztreeVO);
		}
    	return ztreeVOs;
    }
    
    @Override
    public void save(InfoRoleEntity entity) {
    	infoRoleDao.save(entity);
    }
    
    @Override
    public List<InfoRoleEntity> findAll() {
    	return Lists.newArrayList(infoRoleDao.findAll());
    }

    @Override
    public InfoRoleEntity findById(String id) {
        return infoRoleDao.findOne(id);
    }
    
	@Override
    public void update(InfoRoleEntity entity) {
		InfoRoleEntity entity2=findById(entity.getId());
		BeanUtils.copyProperties(entity, entity2,BeanTool.getNullPropertyNames(entity));
		infoRoleDao.save(entity2);
    }

    @Override
    public void deleteById(String id) {
    	if(id.indexOf(",")>0){
    		List<String> idList=new ArrayList<String>();
    		String [] ids=id.split(",");
    		for (String idString : ids) {
    			idList.add(idString);
			}
    		infoRoleFunctionService.deleteByRoleId(id);
    		infoRoleDao.deleteByIds(idList);
    	}else{
    		infoRoleFunctionService.deleteByRoleId(id);
        	infoRoleDao.delete(id);
    	}
    	
    }

	@Override
	public Map<String, Object> getRoles(final String userid,final String eid,final String deptId,final String roleName,final Date startTime,final Date endTime, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
		Specification<InfoRoleEntity> spec = new Specification<InfoRoleEntity>() {
			@Override
			public Predicate toPredicate(Root<InfoRoleEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				Path<String> roleName2=root.get("name");
				Path<String> eid2=root.get("eid");
				Path<Date> operDate=root.<Date>get("operDate");
				Path<String> createUserid=root.get("createUserid");
				if (StringUtils.isNoneEmpty(roleName)) {
					Predicate p=builder.like(roleName2, "%"+roleName+"%");
					predicateList.add(p);
				}
				Predicate eidPredicate=builder.equal(eid2, eid);
				predicateList.add(eidPredicate);
				Predicate p=builder.between(operDate, startTime,endTime);
				predicateList.add(p);
				final List<String> userids=new ArrayList<String>();
				userids.add(userid);
				List<UserEntity> userEntities= userService.getUsersByDept(deptId); 
				if(userEntities!=null){
					for (UserEntity userEntity : userEntities) {
						userids.add(userEntity.getId());
					}
				}
				
				Predicate createUseridPredicate=createUserid.in(userids);
				predicateList.add(createUseridPredicate);
				Predicate [] predicates=new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				query.where(predicates);
				return null;
			}
			
		};
		Page<InfoRoleEntity> page=infoRoleDao.findAll(spec, pageRequest);
		Map<String, Object> m=new HashMap<String, Object>();
		m.put("total", page.getTotalPages());
		m.put("page", pageNumber);
		m.put("records", page.getTotalElements());
		m.put("rows", page.getContent());
		return m;
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} 

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

}
