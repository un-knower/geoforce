package com.supermap.egispservice.base.service;

import java.util.List;

import com.supermap.egispservice.base.entity.InfoRoleFunctionEntity;


/**
 * 角色权限菜单关联表接口
 */
public interface InfoRoleFunctionService {
    
    /**
     * 保存
     */
    void save(InfoRoleFunctionEntity entity);
    /**
     * 全查
     */
    public List<InfoRoleFunctionEntity> findAll();
    
    /**
     * 根据id查找
     */
    InfoRoleFunctionEntity findById(String id);
    
    /**
     * 更新
     */
    void update(InfoRoleFunctionEntity entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(String id);
    /**
     * 根据角色查询关联的菜单
     */
    public List<InfoRoleFunctionEntity>  findByRoleId(String roleId);
    /**
     * 授权
     */
	void authorize(String checkedMenuIds, String roleId);
	/**
	 * 删除角色下的菜单关联信息
	 */
	void deleteByRoleId(String roleId);
    
}