package com.supermap.egispservice.lbs.service;

import java.util.ArrayList;
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

import com.supermap.egispservice.lbs.dao.CarAlarmForeignDao;
import com.supermap.egispservice.lbs.dao.SpeedingSetDao;
import com.supermap.egispservice.lbs.entity.CarAlarmForeign;
import com.supermap.egispservice.lbs.entity.Region;
import com.supermap.egispservice.lbs.entity.RegionSet;
import com.supermap.egispservice.lbs.entity.SpeedingSet;
import com.supermap.egispservice.lbs.util.BeanTool;

@Transactional
@Service("carSpeedingService")
public class CarSpeedingServiceImpl implements CarSpeedingService {

	@Autowired
	SpeedingSetDao speedingSetDao;
	
	@Autowired
	CarAlarmForeignDao  carAlarmForeignDao;
	
	@Override
	public int addCarSpeeding(SpeedingSet carSpeeding) {
		this.speedingSetDao.save(carSpeeding);
		return 1;
	}

	@Override
	public int updateCarSpeeding(SpeedingSet carSpeeding) {
		SpeedingSet carSpeeding2=this.speedingSetDao.findByid(carSpeeding.getId());
		BeanUtils.copyProperties(carSpeeding,carSpeeding2,BeanTool.getNullPropertyNames(carSpeeding));
		this.speedingSetDao.save(carSpeeding2);
		return 1;
	}

	@Override
	public int delCarSpeeding(SpeedingSet carSpeeding) {
		this.speedingSetDao.delete(carSpeeding);
		return 1;
	}

	@Override
	public List<SpeedingSet> queryCarSpeeding(HashMap hm) {
		Map<String,Object> map=queryCarSpeedingByPage(hm);
		if(map!=null){
			return (List<SpeedingSet>)map.get("rows");
		}
		else return null;
	}

	@Override
	public SpeedingSet getCarSpeeding(String id) {
		return this.speedingSetDao.findByid(id);
	}

	@Override
	public List<SpeedingSet> getCarSpeedingBySpeed(String speed,String eid) {
		return this.speedingSetDao.findBySpeedAndEid(speed, eid);
	}

	@Override
	public int hasName(String name, String eid) {
		List<SpeedingSet> list=this.speedingSetDao.findByNameAndEid(name, eid);
		if(list!=null&&list.size()>0){
			return 1;
		}else return 0;
	}

	@Override
	public Map<String, Object> pageCarSpeeding(HashMap<String, Object> hm) {
		return this.queryCarSpeedingByPage(hm);
	}
	
	
	public Map<String, Object> queryCarSpeedingByPage(HashMap<String, Object> hm) {
		Map<String, Object> result=null;
		
		int pageNumber=((hm.get("pageNumber")==null||hm.get("pageNumber").equals(""))?-1:Integer.parseInt(hm.get("pageNumber").toString()));
		int pageSize=((hm.get("pageSize")==null||hm.get("pageSize").equals(""))?0:Integer.parseInt(hm.get("pageSize").toString()));
		String sortType=(String) (hm.get("sortType")==null?"auto":hm.get("sortType"));
		
		final String name=(String) (hm.get("name")==null?"":hm.get("name"));//围栏名称
		final String deptId=(String) (hm.get("deptId")==null?"":hm.get("deptId"));//部门id
		final String eid=(String) (hm.get("eid")==null?"":hm.get("eid"));//企业id
		final String id=(String) (hm.get("id")==null?"":hm.get("id"));//用户id
		final String status=(String) (hm.get("status")==null?"":hm.get("status"));//企业id
		final String speedName=(String) (hm.get("speedName")==null?"":hm.get("speedName"));//用户id
		
		final List carIds=(List) ((hm.get("carIds")==null||hm.get("carIds").equals(""))?new ArrayList():hm.get("carIds"));//
		
		List<String> foreignlist=null;
		if(carIds!=null&&carIds.size()>0){
			foreignlist=new ArrayList<String>();
			List<CarAlarmForeign> foreignlsit=this.carAlarmForeignDao.findBycarIdIn(carIds);
			if(foreignlsit==null||foreignlsit.size()<=0){
				return null;
			}else{
				for(CarAlarmForeign cf : foreignlsit){
					foreignlist.add(cf.getForeignId());
				}
			}
		}
		
		final List<String> foreignlist_=foreignlist;
		
		
		Specification<SpeedingSet> spec = new Specification<SpeedingSet>() {
    		@Override
    		public Predicate toPredicate(Root<SpeedingSet> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> idpath=root.get("id");
    			Path<String> namepath=root.get("name");
    			Path<String> eidpath=root.get("eid");
    			Path<Short> statuspath=root.get("status");
    			
    			//名称
    			if (StringUtils.isNoneEmpty(name)) {
    				Predicate p=builder.like(namepath, "%"+name+"%");
    				predicateList.add(p);
    			}
    			
    			//status
    			if (StringUtils.isNoneEmpty(status)) {
    				Predicate p=builder.equal(statuspath, Short.valueOf(status));
    				predicateList.add(p);
    			}
    			
    			//eid
    			if (StringUtils.isNoneEmpty(eid)) {
    				Predicate p=builder.equal(eidpath, eid);
    				predicateList.add(p);
    			}
    			
    			//id
    			if (StringUtils.isNoneEmpty(id)) {
    				Predicate p=builder.notEqual(idpath, id);
    				predicateList.add(p);
    			}
    			
    			//speedName
    			if (StringUtils.isNoneEmpty(speedName)) {
    				Predicate p=builder.equal(namepath, speedName);
    				predicateList.add(p);
    			}
    			
    			//exsit
    			if(foreignlist_!=null&&foreignlist_.size()>0){
    				Predicate p=idpath.in(foreignlist_);
    				predicateList.add(p);
    			}
    			
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    		}
    	};
    	
    	if(pageNumber!=-1){
    		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
    		Page<SpeedingSet> page=this.speedingSetDao.findAll(spec, pageRequest);
        	result=new HashMap<String, Object>();
        	result.put("total", page.getTotalPages());
        	result.put("page", pageNumber);
        	result.put("records", page.getTotalElements());
        	result.put("rows", page.getContent());
    	}else{
    		Sort sort = new Sort(Direction.DESC,new String[]{"operTime"});
    		List<SpeedingSet> regionlist=this.speedingSetDao.findAll(spec,sort);
    		if(regionlist!=null&&regionlist.size()>0){
    			result=new HashMap<String, Object>();
    			result.put("rows", regionlist);
    		}
    	}
    	return result;
	}
	
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)||sortType.equals("")) {
			sort = new Sort(Direction.DESC, "operTime");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	

}
