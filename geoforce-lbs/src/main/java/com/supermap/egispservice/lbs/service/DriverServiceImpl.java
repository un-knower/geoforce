package com.supermap.egispservice.lbs.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
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
import com.supermap.egispservice.lbs.dao.CarDao;
import com.supermap.egispservice.lbs.dao.CarDriverDao;
import com.supermap.egispservice.lbs.dao.DriverDao;
import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.entity.CarDriver;
import com.supermap.egispservice.lbs.entity.Driver;
import com.supermap.egispservice.lbs.pojo.CarDeptDriver;
import com.supermap.egispservice.lbs.util.BeanTool;

@Transactional
@Service("driverService")
public class DriverServiceImpl implements DriverService{
	
	@Autowired
	DriverDao driverDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	InfoDeptService infoDeptService;
	
	@Autowired
	CarDriverDao carDriverDao;
	
	@Autowired
	CarDao carDao;

	
	@Override
	@Transactional
	public int addDriver(Driver driver) {
		driver=this.driverDao.save(driver);
		if(driver!=null&&driver.getId()!=null){
			return 1;
		}
		else return 0;
	}

	@Override
	@Transactional
	public int updateDriver(Driver driver) {
		Driver driver2=this.driverDao.findDriverById(driver.getId());
		BeanUtils.copyProperties(driver,driver2,BeanTool.getNullPropertyNames(driver));
		this.driverDao.save(driver2);
		return 1;
	}

	@Override
	public Driver getDriver(String id) {
		return this.driverDao.findDriverById(id);
	}

	@Override
	public int haslicense(String license,String eid) {
		List list=this.driverDao.findByLicenseAndEid(license,eid);
		if(list!=null&&list.size()>0){
			return list.size();
		}
		else return 0;
	}

	@Override
	public int hasName(String name, String eid) {
		List list=this.driverDao.findByNameAndEid(name, eid);
		if(list!=null&&list.size()>0){
			return list.size();
		}
		else return 0;
	}

	@Override
	@Transactional
	public int delDriver(Driver driver) {
		this.driverDao.delete(driver);
		return 1;
	}

	@Override
	@Transactional
	public int delDriver(String driverId) {
		this.driverDao.delete(driverId);
		return 1;
	}

	@Override
	public List<Driver> getDriversByCarId(String carId) {
		return this.driverDao.getDriversByCarId(carId);
	}

	@Override
	public Driver getDriverByLicense(String license,String eid) {
		return this.driverDao.findOneDriverByLicenseAndEid(license,eid);
	}

	@Override
	public int getDriverCount(HashMap hm) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CarDeptDriver> queryDriver(HashMap hm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String,Object> queryDriverPage(HashMap hm) {
		
		final String deptId=(String) (hm.get("deptId")==null?"":hm.get("deptId"));//选择的部门Id
		final String userId=(String) (hm.get("userId")==null?"":hm.get("userId"));//登录的用户id
		
		if(StringUtils.isNotEmpty(deptId)||StringUtils.isNotEmpty(userId)){
			hm=deptIdsConvertDeptId(hm);//查找子部门
		}
		
		final String deptId_2=(String) (hm.get("deptId")==null?"":hm.get("deptId"));//选择的部门Id
		
		int pageNumber=((hm.get("pageNumber")==null||hm.get("pageNumber").equals(""))?-1:Integer.parseInt(hm.get("pageNumber").toString()));
		int pageSize=((hm.get("pageSize")==null||hm.get("pageSize").equals(""))?0:Integer.parseInt(hm.get("pageSize").toString()));
		String sortType=(String) (hm.get("sortType")==null?"auto":hm.get("sortType"));
		
		final String driverid=(String) (hm.get("id")==null?"":hm.get("id"));//司机id
		final String drivername=(String) (hm.get("name")==null?"":hm.get("name"));//司机名称
		final List<String> childdeptids=(List<String>) hm.get("deptIds");//根据部门查找子部门
		final String eid=(String) (hm.get("eid")==null?"":hm.get("eid"));//企业id
		
		
		final String notCarId=(String) (hm.get("notCarId")==null?"":hm.get("notCarId"));//车辆未绑定的司机
		List<String> notInDriverList=null;
		if(StringUtils.isNotEmpty(notCarId)){
			List<CarDriver> cardriverlist=this.carDriverDao.findCarDriverByCarId(notCarId);
			if(cardriverlist!=null&&cardriverlist.size()>0){
				notInDriverList=new ArrayList<String>();
				for(CarDriver c:cardriverlist){
					notInDriverList.add(c.getDriverId());
				}
			}
		}
		final List<String> notInDriverList_=notInDriverList;
		
		
    	Specification<Driver> spec = new Specification<Driver>() {
    		@Override
    		public Predicate toPredicate(Root<Driver> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> idpath=root.get("id");
    			Path<String> namepath=root.get("name");
    			Path<String> deptidpath=root.get("deptId");
    			Path<String> eidpath=root.get("eid");
    			Path<String> createUseridPath=root.get("createUserid");
    			
    			//司机id
    			if (StringUtils.isNoneEmpty(driverid)) {
    				Predicate p=builder.equal(idpath, driverid);
    				predicateList.add(p);
    			}
    			//司机名称  模糊匹配
    			if (StringUtils.isNoneEmpty(drivername)) {
    				Predicate p=builder.like(namepath, "%"+drivername+"%");
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
    					Predicate p2=builder.equal(createUseridPath, userId);
    					orpredicateList.add(p);
    					orpredicateList.add(p2);
    					if(orpredicateList.size() > 0){
    						Predicate[] predicatess = new Predicate[orpredicateList.size()];
    						orpredicateList.toArray(predicatess);
    						predicateList.add(builder.or(predicatess));
    					}
    				}
    			}
    			
    			//not in
    			if(notInDriverList_!=null&&notInDriverList_.size()>0){
					Iterator iterator = notInDriverList_.iterator();
			        In in = builder.in(idpath);
			        while (iterator.hasNext()) {
			            in.value(iterator.next());
			        }
			        predicateList.add(builder.not(in));
    			}
    			
    			//企业id
    			if(StringUtils.isNotEmpty(eid)){
    				Predicate p=builder.equal(eidpath, eid);
    				predicateList.add(p);
    			}
    			
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    		}
    	};
    	Map<String, Object> m=null;
    	if(pageNumber!=-1){
    		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
    		Page<Driver> page=this.driverDao.findAll(spec, pageRequest);
        	List<CarDeptDriver> result=FormatResult(page.getContent());//关联信息（车辆、部门）
        	m=new HashMap<String, Object>();
        	m.put("total", page.getTotalPages());
        	m.put("page", pageNumber);
        	m.put("records", page.getTotalElements());
        	m.put("rows", result);
    	}else{
    		Sort sort = new Sort(Direction.DESC,new String[]{"modifyDate"});
    		List<Driver> list=this.driverDao.findAll(spec, sort);
    		if(list!=null&&list.size()>0){
    			List<CarDeptDriver> result=FormatResult(list);//关联信息（车辆、部门）
    			m=new HashMap<String, Object>();
    			m.put("rows", result);
    		}
    	}
    	return m;
	}

	@Override
	public Map<String, Object> queryBindDriver(HashMap hm) {
		final String deptId=(String) (hm.get("deptId")==null?"":hm.get("deptId"));//选择的部门Id
		final String userId=(String) (hm.get("userId")==null?"":hm.get("userId"));//登录的用户id
		
		if(StringUtils.isNotEmpty(deptId)||StringUtils.isNotEmpty(userId)){
			hm=deptIdsConvertDeptId(hm);//查找子部门
		}
		
		final String deptId_2=(String) (hm.get("deptId")==null?"":hm.get("deptId"));//选择的部门Id
		
		int pageNumber=((hm.get("pageNumber")==null||hm.get("pageNumber").equals(""))?-1:Integer.parseInt(hm.get("pageNumber").toString()));
		int pageSize=((hm.get("pageSize")==null||hm.get("pageSize").equals(""))?0:Integer.parseInt(hm.get("pageSize").toString()));
		String sortType=(String) (hm.get("sortType")==null?"auto":hm.get("sortType"));
		
		final String driverid=(String) (hm.get("id")==null?"":hm.get("id"));//司机id
		final String drivername=(String) (hm.get("name")==null?"":hm.get("name"));//司机名称
		final List<String> childdeptids=(List<String>) hm.get("deptIds");//根据部门查找子部门
		final String eid=(String) (hm.get("eid")==null?"":hm.get("eid"));//企业id
		
		final String carId=(String) (hm.get("carId")==null?"":hm.get("carId"));//车辆已绑定的司机
		
		
		List<String> InDriverList=null;
		if(StringUtils.isNotEmpty(carId)){
			List<CarDriver> cardriverlist=this.carDriverDao.findCarDriverByCarId(carId);
			if(cardriverlist!=null&&cardriverlist.size()>0){
				InDriverList=new ArrayList<String>();
				for(CarDriver c:cardriverlist){
					InDriverList.add(c.getDriverId());
				}
			}else return null;
		}
		final List<String> InDriverList_=InDriverList;
		
    	Specification<Driver> spec = new Specification<Driver>() {
    		@Override
    		public Predicate toPredicate(Root<Driver> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> idpath=root.get("id");
    			Path<String> namepath=root.get("name");
    			Path<String> deptidpath=root.get("deptId");
    			Path<String> eidpath=root.get("eid");
    			Path<String> createUseridPath=root.get("createUserid");
    			
    			//司机id
    			if (StringUtils.isNoneEmpty(driverid)) {
    				Predicate p=builder.equal(idpath, driverid);
    				predicateList.add(p);
    			}
    			//司机名称  模糊匹配
    			if (StringUtils.isNoneEmpty(drivername)) {
    				Predicate p=builder.like(namepath, "%"+drivername+"%");
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
    					Predicate p2=builder.equal(createUseridPath, userId);
    					orpredicateList.add(p);
    					orpredicateList.add(p2);
    					if(orpredicateList.size() > 0){
    						Predicate[] predicatess = new Predicate[orpredicateList.size()];
    						orpredicateList.toArray(predicatess);
    						predicateList.add(builder.or(predicatess));
    					}
    				}
    			}
    			
    			//in
    			if(InDriverList_!=null&&InDriverList_.size()>0){
					Predicate p=idpath.in(InDriverList_);
					predicateList.add(p);
				}
    			
    			//企业id
    			if(StringUtils.isNotEmpty(eid)){
    				Predicate p=builder.equal(eidpath, eid);
    				predicateList.add(p);
    			}
    			
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    		}
    	};
    	Map<String, Object> m=null;
    	if(pageNumber!=-1){
    		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
    		Page<Driver> page=this.driverDao.findAll(spec, pageRequest);
        	List<CarDeptDriver> result=FormatResult(page.getContent());//关联信息（车辆、部门）
        	m=new HashMap<String, Object>();
        	m.put("total", page.getTotalPages());
        	m.put("page", pageNumber);
        	m.put("records", page.getTotalElements());
        	m.put("rows", result);
    	}else{
    		Sort sort = new Sort(Direction.DESC,new String[]{"modifyDate"});
    		List<Driver> list=this.driverDao.findAll(spec, sort);
    		if(list!=null&&list.size()>0){
    			List<CarDeptDriver> result=FormatResult(list);//关联信息（车辆、部门）
    			m=new HashMap<String, Object>();
    			m.put("rows", result);
    		}
    	}
    	return m;
	}
	
	 /**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)||sortType.equals("")) {
			sort = new Sort(Direction.DESC, "modifyDate");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
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
	 * 根据部门id查找子部门下的用户
	 * @param deptId
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19上午10:25:58
	 */
	public List<String> findchilduserIdByDeptid(String deptId){
		List<UserEntity> userlist=this.userService.getUsersByDept(deptId);
		List<String> useridlist=new ArrayList<String>();
		if(userlist != null && userlist.size()>0){
			for (int i = 0; i < userlist.size(); i++) {
				UserEntity user = userlist.get(i);
				if(user!=null&&user.getDeptId()!=null&&user.getDeptId().getId()!=null&&!user.getDeptId().getId().equals("deptId")){
					useridlist.add(user.getId());
				}else continue;
			}
		}
		return useridlist;
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
	 * 司机信息关联车辆名称和部门名称
	 * @param driverlist
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午4:10:37
	 */
	public List<CarDeptDriver>  FormatResult(List<Driver> driverlist){
		List<CarDeptDriver> result=null;
		if(driverlist!=null&&driverlist.size()>0){
			result=new ArrayList<CarDeptDriver>();
			for(int i=0;i<driverlist.size();i++){
				CarDeptDriver carDeptDriver=new CarDeptDriver();
				Driver driver=driverlist.get(i);
				String deptid=driver.getDeptId();
				String dirverid=driver.getId();
				
				carDeptDriver.setId(driver.getId());
				carDeptDriver.setLicense(driver.getLicense());
				carDeptDriver.setName(driver.getName());
				carDeptDriver.setPhone(driver.getPhone());
				carDeptDriver.setModifyDate(driver.getModifyDate());
				Date licenseSdate = driver.getLicenseSdate();
				Date licenseEdate = driver.getLicenseEdate();
				if(licenseSdate != null && licenseEdate != null){
					String licenseSdateStr = new SimpleDateFormat("yyyy-MM-dd").format(licenseSdate);
					String licenseEdateStr = new SimpleDateFormat("yyyy-MM-dd").format(licenseEdate);
					carDeptDriver.setLicenseDate(licenseSdateStr+"至"+licenseEdateStr);
				}
				
				InfoDeptEntity dept=this.infoDeptService.findDeptById(deptid);
				
				List<CarDriver> cardrivers=this.carDriverDao.findCarDriverByDriverId(dirverid);
				if(dept!=null){
					carDeptDriver.setDeptId(dept.getId());
					carDeptDriver.setDeptName(dept.getName());
				}
				
				StringBuffer carNames = new StringBuffer();//车辆名称
				
				if(cardrivers!=null&&cardrivers.size()>0){
					for(int j=0;j<cardrivers.size();j++){
						CarDriver cardriver=cardrivers.get(j);
						Car car=this.carDao.findById(cardriver.getCarId());
						if(car!=null&&car.getLicense()!=null){
							carNames.append(car.getLicense().trim()).append(",");
						}
					}
					if(carNames.length()>0){
						carNames.delete(carNames.length()-1, carNames.length());
					}
				}
				carDeptDriver.setCarNames(carNames.toString());
				result.add(carDeptDriver);
			}
		}
		
		return result;
	}
}
