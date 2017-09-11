package com.supermap.egispservice.base.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.InfoRoleEntity;
import com.supermap.egispservice.base.entity.ZtreeVO;


/**
 * 角色接口
 */
public interface InfoRoleService {
    
    /**
     * 保存
     */
    void save(InfoRoleEntity entity);
    /**
     * 全查
     */
    public List<InfoRoleEntity> findAll();
    
    /**
     * 根据id查找
     */
    InfoRoleEntity findById(String id);
    
    /**
     * 更新
     */
    void update(InfoRoleEntity entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(String id);
    
    /**
     * 分页查询
     * @param eid
     * @param roleName
     * @param startTime
     * @param endTime
     * @param pageNumber
     * @param pageSize
     * @param sortType
     * @return
     */
    public Map<String, Object> getRoles(final String userid,final String eid,final String deptId,final String roleName,final Date startTime,final Date endTime ,int pageNumber, int pageSize,String sortType);
   /**
    * 获取角色授权的树数据
    */
    List<ZtreeVO> getCheckedMenu(String userid,String eid ,String deptId ,String roleId,Byte sourceId);
    /**
     * 根据企业ID获取角色信息
     */
    List<InfoRoleEntity> findByEidAndDepts(final String userid,final String eid,final String deptId);
    /**
     * 菜单显示与部门管理员角色授权时查找当前用户拥有的角色
     */
	List<InfoRoleEntity> findRolesByEidAndUserid(String userid, String eid); 
    
}