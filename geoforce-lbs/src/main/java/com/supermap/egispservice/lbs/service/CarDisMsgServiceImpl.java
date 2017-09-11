package com.supermap.egispservice.lbs.service;

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

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.lbs.dao.CarDismsgDao;
import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.entity.CarAlarmForeign;
import com.supermap.egispservice.lbs.entity.CarDismsg;
import com.supermap.egispservice.lbs.entity.Terminal;
import com.supermap.egispservice.lbs.pojo.CarDept;
import com.supermap.egispservice.lbs.util.BeanTool;

@Transactional
@Service("carDisMsgService")
public class CarDisMsgServiceImpl implements CarDisMsgService {

	@Autowired
	CarDismsgDao carDismsgDao;
	
	@Autowired
	InfoDeptService infoDeptService;
	
	@Autowired
	UserService userService;
	
	@Override
	public List<CarDismsg> queryCarMessage(HashMap hm) {
		Map<String,Object> map=getCarDisMsgPage(hm);
		if(map!=null){
			return (List<CarDismsg>) map.get("rows");
		}
		else return null;
	}

	@Override
	public Map<String, Object> queryCarMessagePage(HashMap hm) {
		return getCarDisMsgPage(hm);
	}

	@Override
	public int addCarMessage(CarDismsg carMessage) {
		this.carDismsgDao.save(carMessage);
		return 1;
	}

	@Override
	public CarDismsg getCarMessage(String id) {
		return this.carDismsgDao.findByid(id);
	}

	@Override
	public int updateCarMessage(CarDismsg carDismsg) {
		CarDismsg carDismsg2=this.carDismsgDao.findByid(carDismsg.getId());
		BeanUtils.copyProperties(carDismsg,carDismsg2,BeanTool.getNullPropertyNames(carDismsg));
		this.carDismsgDao.save(carDismsg2);
		return 1;
	}

	@Override
	public int delCarMessage(CarDismsg carDismsg) {
		this.carDismsgDao.delete(carDismsg);
		return 1;
	}
	
	/**
	 * 根据部门id查找子部门id】
	 * @param deptId
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19上午10:39:36
	 */
	public List<String> findchilddeptIdByDeptid(String deptId){
		List<InfoDeptEntity> deptlist=this.infoDeptService.getChildDepts(deptId);
		List<String> deptidlist=new ArrayList<String>();
		if(deptlist != null && deptlist.size()>0){
			for (int i = 0; i < deptlist.size(); i++) {
				InfoDeptEntity dept = deptlist.get(i);
				if(dept!=null&&dept.getId()!=null&&!dept.getId().equals(deptId)){
					deptidlist.add(dept.getId());
				}else continue;
			}
		}
		return deptidlist;
	}
	
	
	/**
	 * 通过部门id查找自部门用户id
	 * 1、当有deptid参数时，说明按部门查询，此时是子部门
	 * 2、当没有deptid参数时，说明没有部门查询条件，此时按照子部门+当前用户
	 * @param hm
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19上午9:02:41
	 */
	private HashMap deptIdsConvertDeptId(HashMap hm){
		String deptId = (String)hm.get("deptId");
		String userId=(String)hm.get("userId");
		List<String> deptidlist=new ArrayList<String>();
		if(deptId != null){
			deptidlist=findchilddeptIdByDeptid(deptId);
			hm.remove("userId");
			hm.remove("deptId");
		}
		else{
			UserEntity user=this.userService.findUserById(userId);
			deptidlist=findchilddeptIdByDeptid(user.getDeptId().getId());
		}
		hm.put("deptIds", deptidlist);
		return hm;
	}
	
	 /**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)||sortType.equals("")) {
			sort = new Sort(Direction.DESC, "sendDate");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	
	/**
	 * 分页查询
	 * @param hm
	 * @return
	 * @Author Juannyoh
	 * 2016-2-1上午10:23:33
	 */
	public Map<String,Object> getCarDisMsgPage(HashMap hm){

		final String deptId=(String) (hm.get("deptId")==null?"":hm.get("deptId"));//当前用户部门id
		
		final String userId=(String) (hm.get("userId")==null?"":hm.get("userId"));//登录的用户id
		
		if(StringUtils.isNotEmpty(deptId)||StringUtils.isNotEmpty(userId)){
			hm=deptIdsConvertDeptId(hm);
		}
		
		final String deptId_2=(String) (hm.get("deptId")==null?"":hm.get("deptId"));//当前用户部门id
		
		Map<String, Object> m=null;
		
		int pageNumber=((hm.get("pageNumber")==null||hm.get("pageNumber").equals(""))?-1:Integer.parseInt(hm.get("pageNumber").toString()));
		int pageSize=((hm.get("pageSize")==null||hm.get("pageSize").equals(""))?0:Integer.parseInt(hm.get("pageSize").toString()));
		String sortType=(String) (hm.get("sortType")==null?"auto":hm.get("sortType"));
		
		final String carId=(String) (hm.get("carId")==null?"":hm.get("carId"));//车辆id
		final String type=(String) (hm.get("type")==null?"":hm.get("type"));//
		final String license=(String) (hm.get("license")==null?"":hm.get("license"));//车牌号
		final String status=(String) (hm.get("status")==null?"":hm.get("status"));//车辆状态 
		final Date startDate=(Date) (hm.get("startDate")==null?null:hm.get("startDate"));//时间范围 开始时间
		final Date endDate=(Date) (hm.get("endDate")==null?null:hm.get("endDate"));//时间范围  结束时间
		final List<String> childdeptids=(List<String>) hm.get("deptIds");//根据部门查找子部门

		
		Specification<CarDismsg> spec = new Specification<CarDismsg>() {
    		@Override
    		public Predicate toPredicate(Root<CarDismsg> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> caridpath=root.get("car").get("id");
    			Path<String> deptidpath=root.get("car").get("depId");
    			Path<String> UseridPath=root.get("userId");
    			Path<String> licensePath=root.get("car").get("license");
    			Path<String> typePath=root.get("type");
    			Path<String> carstatusPath=root.get("car").get("status");
    			Path<String> statusPath=root.get("status");
    			Path<Date> sendDatePath=root.get("sendDate");
    			
    			//状态不为3
    			Predicate ps=builder.notEqual(carstatusPath, "3");
				predicateList.add(ps);
    			
    			
				//车辆id
    			if (StringUtils.isNoneEmpty(carId)) {
    				Predicate p=builder.equal(caridpath, carId);
    				predicateList.add(p);
    			}
    			
    			//type
    			if (StringUtils.isNoneEmpty(type)) {
    				Predicate p=builder.equal(typePath, type);
    				predicateList.add(p);
    			}
    			
    			//license
    			if (StringUtils.isNoneEmpty(license)) {
    				Predicate p=builder.like(licensePath, "%"+license+"%");
    				predicateList.add(p);
    			}
    			
    			//status
    			if (StringUtils.isNoneEmpty(status)) {
    				Predicate p=builder.equal(statusPath, status);
    				predicateList.add(p);
    			}
    			
    			
    			//有部门筛选条件
    			if(StringUtils.isNotEmpty(deptId)&&StringUtils.isEmpty(deptId_2)){
    				childdeptids.add(deptId);
    				if(childdeptids!=null&&childdeptids.size()>0){
    					Predicate p=deptidpath.in(childdeptids);
    					predicateList.add(p);
    				}
    			}else{
    				if(childdeptids!=null&&childdeptids.size()>0){
    					List<Predicate> orpredicateList=new ArrayList<Predicate>();
    					Predicate p=deptidpath.in(childdeptids);
    					Predicate p2=builder.equal(UseridPath, userId);
    					orpredicateList.add(p);
    					orpredicateList.add(p2);
    					if(orpredicateList.size() > 0){
    						Predicate[] predicatess = new Predicate[orpredicateList.size()];
    						orpredicateList.toArray(predicatess);
    						predicateList.add(builder.or(predicatess));
    					}
    				}
    			}
    			
    			
    			if(startDate!=null&&endDate!=null){
    				Predicate p=builder.between(sendDatePath, startDate, endDate);
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
    		Page<CarDismsg> page=this.carDismsgDao.findAll(spec, pageRequest);
    		List<CarDismsg> result=FormatResult(page.getContent());//
        	m=new HashMap<String, Object>();
        	m.put("total", page.getTotalPages());
        	m.put("page", pageNumber);
        	m.put("records", page.getTotalElements());
        	m.put("rows", result);
    	}else{
    		Sort sort = new Sort(Direction.DESC,new String[]{"sendDate"});
    		List<CarDismsg> carlist=this.carDismsgDao.findAll(spec,sort);
    		if(carlist!=null&&carlist.size()>0){
    			m=new HashMap<String, Object>();
    			List<CarDismsg> result=FormatResult(carlist);
    			m.put("rows", result);
    		}
    	}
    	return m;
	}
	
	
	 public List<CarDismsg>  FormatResult(List<CarDismsg> result){
		 List<CarDismsg> list=null;
		 if(result!=null&&result.size()>0){
			 list=new ArrayList<CarDismsg>();
			 for(CarDismsg cd:result){
				 Car c=cd.getCar();
				 Terminal t=c.getTerminal();
				 if(t!=null){
					 t.setCar(null);
				 }
				 c.setTerminal(t);
				 cd.setLicense(c.getLicense());
				 cd.setCar(c);
				 list.add(cd);
			 }
		 }
		 return list;
	 }


}
