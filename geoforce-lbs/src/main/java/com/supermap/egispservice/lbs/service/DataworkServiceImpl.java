package com.supermap.egispservice.lbs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.lbs.dao.DataWordbookDao;
import com.supermap.egispservice.lbs.entity.DataWordbook;

@Transactional
@Service("dataworkService")
public class DataworkServiceImpl implements DataworkService {
	
	@Autowired
	DataWordbookDao dataWordbookDao;

	@Override
	public List<DataWordbook> getDataWordbookList(HashMap<String, Object> hm) {
		
		final String id=(String) (hm.get("id")==null?"":hm.get("id"));//词典id
		final String type=(String) (hm.get("type")==null?"":hm.get("type"));//类型
		final String code=(String) (hm.get("code")==null?"":hm.get("code"));//代码
		final String name=(String) (hm.get("name")==null?"":hm.get("name"));//名称
		final String starts=(String) (hm.get("status")==null?"":hm.get("status"));//是否启用？
		
		Specification<DataWordbook> spec = new Specification<DataWordbook>() {
    		@Override
    		public Predicate toPredicate(Root<DataWordbook> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> idpath=root.get("id");
    			Path<String> typepath=root.get("type");
    			Path<String> codepath=root.get("code");
    			Path<String> namepath=root.get("name");
    			Path<Short> startspath=root.get("starts");
    			
    			//id
    			if (StringUtils.isNoneEmpty(id)) {
    				Predicate p=builder.equal(idpath, id);
    				predicateList.add(p);
    			}
    			//类型
    			if (StringUtils.isNoneEmpty(type)) {
    				Predicate p=builder.equal(typepath, type);
    				predicateList.add(p);
    			}
    			
    			//代码
    			if(StringUtils.isNotEmpty(code)){
    				Predicate p=builder.equal(codepath, code);
    				predicateList.add(p);
    			}
    			
    			//名称
    			if(StringUtils.isNotEmpty(name)){
    				Predicate p=builder.equal(namepath, name);
    				predicateList.add(p);
    			}
    			
    			//状态
    			if(StringUtils.isNotEmpty(starts)){
    				Short s=Short.valueOf(starts);
    				Predicate p=builder.equal(startspath, s);
    				predicateList.add(p);
    			}
    			
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    		}
    	};
    	
    	Sort sort = new Sort(Direction.ASC,new String[]{"code"});
		List<DataWordbook> datawordbooklist=this.dataWordbookDao.findAll(spec,sort);
		return datawordbooklist;
	}

}
