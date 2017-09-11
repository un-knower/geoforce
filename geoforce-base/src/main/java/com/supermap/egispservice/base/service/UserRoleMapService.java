package com.supermap.egispservice.base.service;

import java.util.List;

import com.supermap.egispservice.base.entity.UserRoleMapEntity;


public interface UserRoleMapService {
    
    /**
     * 保存
     */
    void save(UserRoleMapEntity entity);
    /**
     * 全查
     */
    public List<UserRoleMapEntity> findAll();
    
    /**
     * 根据id查找
     */
    UserRoleMapEntity findById(String id);
    
    /**
     * 更新
     */
    void update(UserRoleMapEntity entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(String id);
    /**
     * 根据用户查询关联的角色
     */
    public List<UserRoleMapEntity>  findByUserId(String userId);
    /**
     * 授权
     */
	void authorize(String checkedRoleIds, String userId);
	/**
	 * 删除用户下的角色关联信息
	 */
	void deleteByUserId(String userId);
    
}