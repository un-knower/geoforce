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

import com.supermap.egispservice.lbs.dao.AlarmTypeDao;
import com.supermap.egispservice.lbs.dao.RegionSetDao;
import com.supermap.egispservice.lbs.entity.Region;
import com.supermap.egispservice.lbs.entity.RegionSet;
import com.supermap.egispservice.lbs.pojo.RegionSetRegionBean;
import com.supermap.egispservice.lbs.util.BeanTool;

@Transactional
@Service("regionSetService")
public class RegionSetServiceImpl implements RegionSetService {

	@Autowired
	RegionSetDao regionSetDao;
	
	@Autowired
	AlarmTypeDao alarmTypeDao;
	
	@Override
	public int addRegionSet(RegionSet regionSet) {
		this.regionSetDao.save(regionSet);
		return 1;
	}

	@Override
	public int upadateRegionSet(RegionSet regionSet) {
		RegionSet regionSet2=this.regionSetDao.findByid(regionSet.getId());
		BeanUtils.copyProperties(regionSet,regionSet2,BeanTool.getNullPropertyNames(regionSet));
		this.regionSetDao.save(regionSet2);
		return 1;
	}

	@Override
	public RegionSet getRegionSet(String id) {
		return this.regionSetDao.findByid(id);
	}

	@Override
	public int delRegionSet(RegionSet regionSet) {
		this.regionSetDao.delete(regionSet);
		return 1;
	}

	@Override
	public List<RegionSetRegionBean> queryRegionSet(HashMap<String, Object> hm) {
		Map<String,Object> map=this.queryregionSetByPage(hm);
		if(map!=null){
			return (List<RegionSetRegionBean>)map.get("rows");
		}else return null;
	}

	@Override
	public Map<String, Object> pagequeryRegionSet(HashMap<String, Object> hm) {
		return this.queryregionSetByPage(hm);
	}
	
	
	public Map<String, Object> queryregionSetByPage(HashMap<String, Object> hm) {
		Map<String, Object> result=null;
		
		int pageNumber=((hm.get("pageNumber")==null||hm.get("pageNumber").equals(""))?-1:Integer.parseInt(hm.get("pageNumber").toString()));
		int pageSize=((hm.get("pageSize")==null||hm.get("pageSize").equals(""))?0:Integer.parseInt(hm.get("pageSize").toString()));
		String sortType=(String) (hm.get("sortType")==null?"auto":hm.get("sortType"));
		
		final String regionId=(String) (hm.get("regionId")==null?"":hm.get("regionId"));//围栏名称
		final String deptId=(String) (hm.get("deptId")==null?"":hm.get("deptId"));//部门id
		final Integer alarmType=((hm.get("alarmType")==null||hm.get("alarmType").equals(""))?-1:Integer.parseInt(hm.get("alarmType").toString()));//企业id
		final String status=(String) (hm.get("status")==null?"":hm.get("status"));//用户id
		final String name=(String) (hm.get("name")==null?"":hm.get("name"));//用户id
		final String eid=(String) (hm.get("eid")==null?"":hm.get("eid"));//用户id
		final String regionName=(String) (hm.get("regionName")==null?"":hm.get("regionName"));//用户id
		final String regionSetId=(String) (hm.get("regionSetId")==null?"":hm.get("regionSetId"));//用户id
		
		Specification<RegionSet> spec = new Specification<RegionSet>() {
    		@Override
    		public Predicate toPredicate(Root<RegionSet> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> idpath=root.get("id");
    			Path<String> regionidpath=root.get("regionId").get("id");
    			Path<String> deptidpath=root.get("deptId");
    			Path<String> eidpath=root.get("regionId").get("eid");
    			Path<Integer> alarmTypepath=root.get("alarmType");
    			Path<String> statuspath=root.get("regionId").get("status");
    			Path<String> namepath=root.get("regionId").get("name");
    			
    			
    			//regionId
    			if (StringUtils.isNoneEmpty(regionId)) {
    				Predicate p=builder.equal(regionidpath, regionId);
    				predicateList.add(p);
    			}
    			
    			//deptId
    			if (StringUtils.isNoneEmpty(deptId)) {
    				Predicate p=builder.equal(deptidpath, deptId);
    				predicateList.add(p);
    			}
    			
    			//alarmType
    			if (alarmType!=-1) {
    				Predicate p=builder.equal(alarmTypepath, alarmType);
    				predicateList.add(p);
    			}
    			
    			//status
    			if (StringUtils.isNoneEmpty(status)) {
    				Predicate p=builder.equal(statuspath, status);
    				predicateList.add(p);
    			}
    			
    			//name
    			if (StringUtils.isNoneEmpty(name)) {
    				Predicate p=builder.like(namepath, "%"+name+"%");
    				predicateList.add(p);
    			}
    			
    			//eid
    			if (StringUtils.isNoneEmpty(eid)) {
    				Predicate p=builder.equal(eidpath, eid);
    				predicateList.add(p);
    			}
    			
    			//regionName
    			if (StringUtils.isNoneEmpty(regionName)) {
    				Predicate p=builder.equal(namepath, regionName);
    				predicateList.add(p);
    			}
    			
    			//regionSetId
    			if (StringUtils.isNoneEmpty(regionSetId)) {
    				Predicate p=builder.notEqual(idpath, regionSetId);
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
    		Page<RegionSet> page=this.regionSetDao.findAll(spec, pageRequest);
    		List<RegionSetRegionBean> list=formatResult(page.getContent());
        	result=new HashMap<String, Object>();
        	result.put("total", page.getTotalPages());
        	result.put("page", pageNumber);
        	result.put("records", page.getTotalElements());
        	result.put("rows", list);
    	}else{
    		Sort sort = new Sort(Direction.DESC,new String[]{"id"});
    		List<RegionSet> regionlist=this.regionSetDao.findAll(spec,sort);
    		if(regionlist!=null&&regionlist.size()>0){
    			List<RegionSetRegionBean> list=formatResult(regionlist);
    			result=new HashMap<String, Object>();
    			result.put("rows", list);
    		}
    	}
    	return result;
	}
	
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)||sortType.equals("")) {
			sort = new Sort(Direction.DESC, "operDate");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	public List<RegionSetRegionBean> formatResult(List<RegionSet> list){
		List<RegionSetRegionBean> regionSetRegionBeans=null;
		if(list!=null&&list.size()>0){
			regionSetRegionBeans = new ArrayList<RegionSetRegionBean>();
			for(RegionSet set:list){
				RegionSetRegionBean regionSetRegionBean = new RegionSetRegionBean();
				Region region=set.getRegionId();
				regionSetRegionBean.setRegionSet(set);
				regionSetRegionBean.setRegion(region);
				Integer typeId=set.getAlarmType();
				regionSetRegionBean.setTypeName(this.alarmTypeDao.findOneByCode(typeId.toString()).getName());
				regionSetRegionBean.setId(set.getId());
				regionSetRegionBean.setLngLan(region.getRegion());
				regionSetRegionBean.setTypeCode(set.getAlarmType());
				regionSetRegionBean.setRegionName(region.getName());
				regionSetRegionBeans.add(regionSetRegionBean);
			}
		}
		return regionSetRegionBeans;
	}

}
