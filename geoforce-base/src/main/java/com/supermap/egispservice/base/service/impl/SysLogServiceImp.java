package com.supermap.egispservice.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.supermap.egispservice.base.dao.ServiceModuleDao;
import com.supermap.egispservice.base.dao.SysLogDao;
import com.supermap.egispservice.base.entity.ServiceModuleEntity;
import com.supermap.egispservice.base.entity.SysLogEntity;
import com.supermap.egispservice.base.service.SysLogService;
@Transactional
@Service("sysLogService")
public class SysLogServiceImp implements SysLogService {
	
	@Autowired
	private SysLogDao sysLogDao;
	
	@Autowired
	private ServiceModuleDao serviceModuleDao;
	
	@Override
	public SysLogEntity saveSysLogEntity(SysLogEntity record) {
		return this.sysLogDao.save(record);
	}

	/**
	 * 通过条件查询用户日志数据
	 */
	@Override
	public Map<String, Object> findLogsByParam(final List<String> userid, final String eid,
			final List<String> deptid, final String loginname, final List<String> moduleid,
			final Date startTime, final Date endTime, int pageNumber, int pageSize,
			String sortType) {
		Map<String, Object>  result=null;
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
		Specification<SysLogEntity> spec=new Specification<SysLogEntity>(){
			@Override
			public Predicate toPredicate(Root<SysLogEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				List<Predicate> loginpredicate=new ArrayList<Predicate>();
				
				Path<String> useridpath=root.get("userId").get("id");
				Path<String> moduleidpath=root.get("moduleId");
				Path<String> eidpath=root.get("enterpriseId");
				Path<String> deptidpath=root.get("departmentId");
				Path<Date> operTimepath=root.<Date>get("operTime"); 
				Path<String> usernamepath=root.get("userId").get("username");
				Path<String> mobilephonepath=root.get("userId").get("mobilephone");
				Path<String> emailpath=root.get("userId").get("email");
				
				if(userid!=null&&userid.size()>0){
					Predicate p=useridpath.in(userid);
    				predicateList.add(p);
				}
				if(StringUtils.isNotEmpty(eid)){
					Predicate p=builder.equal(eidpath, eid);
    				predicateList.add(p);
				}
				if(StringUtils.isNotEmpty(loginname)){
					Predicate p=builder.like(usernamepath, "%"+loginname+"%");
					loginpredicate.add(p);
					Predicate p1=builder.like(mobilephonepath, "%"+loginname+"%");
					loginpredicate.add(p1);
					Predicate p2=builder.like(emailpath, "%"+loginname+"%");
					loginpredicate.add(p2);
				}
				
				if(loginpredicate.size()>0){
					Predicate[] orAdminpredicates = new Predicate[loginpredicate.size()];
					loginpredicate.toArray(orAdminpredicates);
					predicateList.add(builder.or(orAdminpredicates));
				}
				
				if(deptid!=null&&deptid.size()>0){
					Predicate p=deptidpath.in(deptid);
    				predicateList.add(p);
				}
				
				if(moduleid!=null&&moduleid.size()>0){
					Predicate p=moduleidpath.in(moduleid);
    				predicateList.add(p);
				}
				Predicate p=builder.between(operTimepath, startTime,endTime);
    			predicateList.add(p);
				
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
				return null;
			}
		};
		Page<SysLogEntity> page=this.sysLogDao.findAll(spec, pageRequest);
		
		//对结果进行处理
		List<SysLogEntity> loglist=page.getContent();
		List<Map<String,Object>> resultlist=null;
		if(loglist!=null&&loglist.size()>0){
			resultlist=new ArrayList<Map<String,Object>>();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int i=1;
			for(SysLogEntity log:loglist){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("id", log.getId());
				map.put("rowid", i);
				map.put("username", log.getUserId().getUsername());
				map.put("realname", log.getUserId().getRealname());
				map.put("mobilephone", log.getUserId().getMobilephone());
				map.put("email", log.getUserId().getEmail());
				map.put("deptname", log.getUserId().getDeptId().getName());
				map.put("moduleid", getModulename(log.getModuleId()));
				map.put("operdesc", log.getOperDesc());
				map.put("opertime", sf.format(log.getOperTime()));
				map.put("datadesc", log.getDataDesc());
				resultlist.add(map);
				i++;
			}
		}
		result=new HashMap<String,Object>();
		result.put("total", page.getTotalPages());
		result.put("page", pageNumber);
		result.put("records", page.getTotalElements());
		result.put("rows", resultlist);
    	return result;
	}

	
	/**
	 * 分页
	 * @param pageNumber
	 * @param pagzSize
	 * @param sortType
	 * @return
	 * @Author Juannyoh
	 * 2015-12-15下午4:03:21
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "operTime");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	@SuppressWarnings("unchecked")
	public String getModulename(String moduleid){
		if(StringUtils.isNotEmpty(moduleid)){
			if(moduleid.equals("5")){
				return "登录系统";
			}
			if(moduleid.equals("6")){
				return "登出系统";
			}
			Map<String,String> m=getModules();
			String modulename=null;
			if(m!=null){
				modulename=m.get(moduleid);
			}
			return modulename;
		}else return "";
		
	}
	
	@SuppressWarnings("rawtypes")
	public Map  getModules(){
		Map<String,String> map=null;
		List<ServiceModuleEntity> modulist=(List<ServiceModuleEntity>) this.serviceModuleDao.findAll();
		if(modulist!=null&&modulist.size()>0){
			map=new HashMap<String,String>();
			for(ServiceModuleEntity module:modulist){
				map.put(module.getId(), module.getName());
			}
		}
		return map;
	}
	

}
