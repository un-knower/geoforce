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

import com.supermap.egispservice.lbs.dao.AlarmTypeDao;
import com.supermap.egispservice.lbs.entity.AlarmType;

@Transactional
@Service("alarmTypeService")
public class AlarmTypeServiceImpl implements AlarmTypeService {

	@Autowired
	AlarmTypeDao alarmTypeDao;
	
	@Override
	public List<AlarmType> queryAlarmType(HashMap<String, Object> hm) {
		
		final String name=(String) (hm.get("name")==null?"":hm.get("name"));//名称
		final String type=(String) (hm.get("type")==null?"":hm.get("type"));//类型
		final String code=(String) (hm.get("code")==null?"":hm.get("code"));//code
		
		Specification<AlarmType> spec=new Specification<AlarmType>(){
			@Override
			public Predicate toPredicate(Root<AlarmType> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				Path<String> namepath=root.get("name");
    			Path<String> typepath=root.get("type");
    			Path<String> codepath=root.get("code");
    			
    			//name
    			if (StringUtils.isNoneEmpty(name)) {
    				Predicate p=builder.like(namepath, "%"+name+"%");
    				predicateList.add(p);
    			}
    			//车辆id
    			if (StringUtils.isNoneEmpty(type)) {
    				Predicate p=builder.equal(typepath, type);
    				predicateList.add(p);
    			}
    			//车辆id
    			if (StringUtils.isNoneEmpty(code)) {
    				Predicate p=builder.equal(codepath, code);
    				predicateList.add(p);
    			}
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
				return null;
			}
		};
		Sort sort = new Sort(Direction.DESC,new String[]{"code"});
		List<AlarmType> result=this.alarmTypeDao.findAll(spec,sort);
		return result;
	}

	@Override
	public AlarmType getAlarmType(String id) {
		return this.alarmTypeDao.findByid(id);
	}

	@Override
	public AlarmType getAlarmTypeByCode(Integer code) {
		return this.alarmTypeDao.findOneByCode(code.toString());
	}

}
