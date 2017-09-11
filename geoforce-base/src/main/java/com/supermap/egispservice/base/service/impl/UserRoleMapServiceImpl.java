package com.supermap.egispservice.base.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.supermap.egispservice.base.dao.UserRoleMapDao;
import com.supermap.egispservice.base.entity.UserRoleMapEntity;
import com.supermap.egispservice.base.service.UserRoleMapService;


/**
 * 
 * @description 用户角色关联表
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-9-16
 * @version 1.0
 */
@Transactional
@Service
public class UserRoleMapServiceImpl implements UserRoleMapService{
    
    
    @Autowired
    private UserRoleMapDao userRoleMapDao;

    @Override
    public void save(UserRoleMapEntity entity) {
    	userRoleMapDao.save(entity);
    }
    
    @Override
    public List<UserRoleMapEntity> findAll() {
    	return Lists.newArrayList(userRoleMapDao.findAll());
    }

    @Override
    public UserRoleMapEntity findById(String id) {
        return userRoleMapDao.findOne(id);
    }
    
	@Override
    public void update(UserRoleMapEntity entity) {
		userRoleMapDao.save(entity);
    }

	@Override
	public void deleteByUserId(String userId){
		userRoleMapDao.deleteByUserId(userId);
	}
	
    @Override
    public void deleteById(String id) {
    	userRoleMapDao.delete(id);
    }
    
    @Override
    public List<UserRoleMapEntity>  findByUserId(String userId) {
    	return userRoleMapDao.findByUserId(userId);
    }
    /**
     * 授权
     */
    @Override
    public void  authorize(String checkedRoleIds, String userId) {
    	userRoleMapDao.deleteByUserId(userId);
    	if(checkedRoleIds.length()>0){
    		if(checkedRoleIds.indexOf(",")>0){//多个id，用逗号分开
    			String [] tempArray=checkedRoleIds.split(",");
    			for (String id : tempArray) {
    				UserRoleMapEntity entity=new UserRoleMapEntity();
    				entity.setUserId(userId);
    				entity.setRoleId(id);
    				userRoleMapDao.save(entity);
				}
    		}else{//1个id
    			UserRoleMapEntity entity=new UserRoleMapEntity();
				entity.setUserId(userId);
				entity.setRoleId(checkedRoleIds);
				userRoleMapDao.save(entity);
    		}
    	}
    }
}
