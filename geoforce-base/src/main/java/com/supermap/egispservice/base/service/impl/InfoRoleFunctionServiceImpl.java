package com.supermap.egispservice.base.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.supermap.egispservice.base.dao.InfoRoleFunctionDao;
import com.supermap.egispservice.base.entity.InfoRoleFunctionEntity;
import com.supermap.egispservice.base.service.InfoRoleFunctionService;


/**
 * 
 * @description 角色菜单关联表
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-9-15
 * @version 1.0
 */
@Transactional
@Service
public class InfoRoleFunctionServiceImpl implements InfoRoleFunctionService{
    
    
    @Autowired
    private InfoRoleFunctionDao infoRoleFunctionDao;

    @Override
    public void save(InfoRoleFunctionEntity entity) {
    	infoRoleFunctionDao.save(entity);
    }
    
    @Override
    public List<InfoRoleFunctionEntity> findAll() {
    	return Lists.newArrayList(infoRoleFunctionDao.findAll());
    }

    @Override
    public InfoRoleFunctionEntity findById(String id) {
        return infoRoleFunctionDao.findOne(id);
    }
    
	@Override
    public void update(InfoRoleFunctionEntity entity) {
		infoRoleFunctionDao.save(entity);
    }

	@Override
	public void deleteByRoleId(String roleId){
		if(roleId.indexOf(",")>0){
    		List<String> idList=new ArrayList<String>();
    		String [] ids=roleId.split(",");
    		for (String idString : ids) {
    			idList.add(idString);
			}
    		infoRoleFunctionDao.deleteByIds(idList);
    	}else{
    		infoRoleFunctionDao.deleteByRoleId(roleId);
    	}
	}
	
    @Override
    public void deleteById(String id) {
    	infoRoleFunctionDao.delete(id);
    }
    
    @Override
    public List<InfoRoleFunctionEntity>  findByRoleId(String roleId) {
    	return infoRoleFunctionDao.findByRoleId(roleId);
    }
    /**
     * 授权
     */
    @Override
    public void  authorize(String checkedMenuIds,String roleId) {
    	infoRoleFunctionDao.deleteByRoleId(roleId);
    	if(checkedMenuIds.length()>0){
    		if(checkedMenuIds.indexOf(",")>0){//多个菜单，用逗号分开
    			String [] tempArray=checkedMenuIds.split(",");
    			for (String menuid : tempArray) {
    				InfoRoleFunctionEntity entity=new InfoRoleFunctionEntity();
        			entity.setFunId(menuid);
        			entity.setRoleId(roleId);
        			infoRoleFunctionDao.save(entity);
				}
    		}else{//1个菜单
    			InfoRoleFunctionEntity entity=new InfoRoleFunctionEntity();
    			entity.setFunId(checkedMenuIds);
    			entity.setRoleId(roleId);
    			infoRoleFunctionDao.save(entity);
    		}
    	}
    }
}
