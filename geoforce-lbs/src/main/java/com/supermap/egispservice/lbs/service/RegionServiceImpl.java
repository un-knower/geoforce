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

import com.supermap.egispservice.lbs.dao.RegionDao;
import com.supermap.egispservice.lbs.dao.RegionSetDao;
import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.entity.Region;
import com.supermap.egispservice.lbs.pojo.CarDept;
import com.supermap.egispservice.lbs.util.BeanTool;

@Transactional
@Service("regionService")
public class RegionServiceImpl implements RegionService {

	@Autowired
	RegionDao regionDao;
	
	@Autowired
	RegionSetDao regionSetDao;
	
	
	@Override
	public Region addRegion(Region region) {
		region=this.regionDao.save(region);
		return region;
	}

	@Override
	public int updateRegion(Region region) {
		Region region2=this.regionDao.findByid(region.getId());
		BeanUtils.copyProperties(region,region2,BeanTool.getNullPropertyNames(region));
		this.regionDao.save(region2);
		return 1;
	}

	@Override
	public int delRegion(Region region) {
		this.regionDao.delete(region);
		return 1;
	}

	@Override
	public Map<String, Object> queryFence(HashMap<String, Object> hm)
			throws Exception {
		return this.queryregionByPage(hm);
	}

	@Override
	public Region getRegion(String id) throws Exception {
		return this.regionDao.findByid(id);
	}

	@Override
	public int hasName(String name, String eid) {
		List<Region> list=this.regionDao.findByNameAndEid(name, eid);
		if(list!=null&&list.size()>0){
			return 1;
		}
		else return 0;
	}

	@Override
	public Map<String, Object> queryAllregion(HashMap<String, Object> hm) {
		List<String> setregionids=this.regionSetDao.findAllregionIds();
		if(setregionids!=null&&setregionids.size()>0){
			hm.put("notinIds", setregionids);
		}
		return this.queryregionByPage(hm);
	}
	
	
	public Map<String, Object> queryregionByPage(HashMap<String, Object> hm) {
		Map<String, Object> result=null;
		
		int pageNumber=((hm.get("pageNumber")==null||hm.get("pageNumber").equals(""))?-1:Integer.parseInt(hm.get("pageNumber").toString()));
		int pageSize=((hm.get("pageSize")==null||hm.get("pageSize").equals(""))?0:Integer.parseInt(hm.get("pageSize").toString()));
		String sortType=(String) (hm.get("sortType")==null?"auto":hm.get("sortType"));
		
		final String name=(String) (hm.get("name")==null?"":hm.get("name"));//围栏名称
		final String deptId=(String) (hm.get("deptId")==null?"":hm.get("deptId"));//部门id
		final String eid=(String) (hm.get("eid")==null?"":hm.get("eid"));//企业id
		final String userId=(String) (hm.get("userId")==null?"":hm.get("userId"));//用户id
		
		//未被处理的警报
		final List notinIds=(List) ((hm.get("notinIds")==null||hm.get("notinIds").equals(""))?new ArrayList():hm.get("notinIds"));//
		
		
		Specification<Region> spec = new Specification<Region>() {
    		@Override
    		public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> idpath=root.get("id");
    			Path<String> namepath=root.get("name");
    			Path<String> deptidpath=root.get("deptId");
    			Path<String> eidpath=root.get("eid");
    			Path<String> userIdpath=root.get("userId");
    			
    			//围栏名称
    			if (StringUtils.isNoneEmpty(name)) {
    				Predicate p=builder.like(namepath, "%"+name+"%");
    				predicateList.add(p);
    			}
    			
    			//部门id
    			if (StringUtils.isNoneEmpty(deptId)) {
    				Predicate p=builder.equal(deptidpath, deptId);
    				predicateList.add(p);
    			}
    			
    			//eid
    			if (StringUtils.isNoneEmpty(eid)) {
    				Predicate p=builder.equal(eidpath, eid);
    				predicateList.add(p);
    			}
    			
    			//userId
    			if (StringUtils.isNoneEmpty(userId)) {
    				Predicate p=builder.equal(userIdpath, userId);
    				predicateList.add(p);
    			}
    			
    			/**
    			 * 未被处理的
    			 */
    			if(notinIds!=null&&notinIds.size()>0){
    				Predicate p=idpath.in(notinIds);
    				predicateList.add(builder.not(p));
    			}
    			
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    		}
    	};
    	
    	if(pageNumber!=-1){
    		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
    		Page<Region> page=this.regionDao.findAll(spec, pageRequest);
        	result=new HashMap<String, Object>();
        	result.put("total", page.getTotalPages());
        	result.put("page", pageNumber);
        	result.put("records", page.getTotalElements());
        	result.put("rows", page.getContent());
    	}else{
    		Sort sort = new Sort(Direction.DESC,new String[]{"id"});
    		List<Region> regionlist=this.regionDao.findAll(spec,sort);
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
			sort = new Sort(Direction.DESC, "id");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
