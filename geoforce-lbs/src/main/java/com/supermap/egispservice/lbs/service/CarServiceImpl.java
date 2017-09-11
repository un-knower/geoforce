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

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.lbs.constants.Config;
import com.supermap.egispservice.lbs.dao.CarDao;
import com.supermap.egispservice.lbs.dao.CarDriverDao;
import com.supermap.egispservice.lbs.dao.DataWordbookDao;
import com.supermap.egispservice.lbs.dao.DriverDao;
import com.supermap.egispservice.lbs.dao.PointCarForeignDao;
import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.entity.CarAlarmForeign;
import com.supermap.egispservice.lbs.entity.CarDriver;
import com.supermap.egispservice.lbs.entity.DataWordbook;
import com.supermap.egispservice.lbs.entity.Driver;
import com.supermap.egispservice.lbs.pojo.CarDept;
import com.supermap.egispservice.lbs.pojo.CarDeptDriver;
import com.supermap.egispservice.lbs.pojo.JsonZTree;
import com.supermap.egispservice.lbs.util.BeanTool;

@Transactional
@Service("carService")
public class CarServiceImpl implements CarService{
	
	@Autowired
	CarDao carDao;
	
	@Autowired
	CarDriverDao carDriverDao;

	@Autowired
	UserService userService;
	
	@Autowired
	InfoDeptService infoDeptService;
	
	@Autowired
	DataWordbookDao dataWordbookDao;
	
	@Autowired
	DriverDao driverDao;
	
	@Autowired
	PointCarForeignDao pointCarForeignDao;
	
	@Autowired
	CarAlarmForeginService  carAlarmForeginService;
	
	@Override
	public Car addCar(Car car) {
		return this.carDao.save(car);
	}

	@Override
	public int addCarDriver(CarDriver carDriver) {
		carDriver =this.carDriverDao.save(carDriver);
		return 1;
	}

	@Override
	public CarDriver getCarDriver(String carId, String driverId) {
		return this.carDriverDao.findByDriverIdAndCarId(driverId, carId);
	}

	@Override
	public List<JsonZTree> getCarDeptTree(String deptId, String treeId) {
		List<CarDept> carList = null;//车辆列表
		List<InfoDeptEntity> deptList = null;//部门列表
		
		HashMap<String,Object> carHm = new HashMap<String,Object>();
		HashMap<String,Object> deptHm = new HashMap<String,Object>();
		
		boolean isRoot = true;
		if(StringUtils.isNotBlank(treeId)){
			//treeId为0时，为根节点，按照登录用户所在部门获取车辆列表
			deptId = treeId;	
			isRoot = false;
		}
		
		List<JsonZTree> list = new ArrayList<JsonZTree>();
		try{
			if (isRoot){
				InfoDeptEntity rootDept = this.infoDeptService.findDeptById(deptId);
				if (rootDept != null){
					JsonZTree rootNode = new JsonZTree();
					rootNode.setId(deptId);
					
					if (StringUtils.isBlank(treeId)){
						rootNode.setpId("0");
					}else{
						rootNode.setpId(rootDept.getParentId());
					}
					rootNode.setName(rootDept.getName());
					rootNode.setEname(rootDept.getName());
					if(rootDept.getName().length() > 7){
						rootNode.setEname(rootDept.getName().substring(0,7)+"..");
					}
					rootNode.setOpen(true);
					rootNode.setIsParent(true);
					list.add(rootNode);
				}	
			}
			
			deptHm.put("parentId", deptId);
			deptList = this.infoDeptService.getChildDepts(deptId);//查询所有后代集合，包括第三代第四代~~
			
			
			if (deptList != null && !deptList.isEmpty()){
				for (InfoDeptEntity dept: deptList){
					if(dept.getId().equals(deptId)
							//||!dept.getParentId().equals(deptId)
							){//按原逻辑过滤掉非直接后代，及本身
						continue;
					}
					JsonZTree ztree = new JsonZTree();
					ztree.setId(dept.getId());
					ztree.setpId(dept.getParentId());
					ztree.setName(dept.getName());
					ztree.setEname(dept.getName());
					if(dept.getName().length() > 7){
						ztree.setEname(dept.getName().substring(0,7)+"..");
					}
					ztree.setOpen(false);
					ztree.setChecked(false);
					ztree.setIsParent(true);
					list.add(ztree);
					
				}
			}
			
				carHm.put("deptId", deptId);
				Map<String,Object> m=queryCarPage(carHm);
				if(m!=null){
					carList=(List<CarDept>) m.get("rows");
				}
				if (carList != null && !carList.isEmpty()){
					for (CarDept car: carList){
						JsonZTree ztree = new JsonZTree();
						ztree.setId(car.getId());
						ztree.setpId(car.getDeptId());
						ztree.setName(car.getLicense());
						ztree.setEname(car.getLicense());
						if(car.getLicense().length() > 7){
							ztree.setEname(car.getLicense().substring(0,7)+"..");
						}
						ztree.setChecked(false);
						ztree.setIsParent(false);
						list.add(ztree);
					}
				}
			
			return list;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public int updateCar(Car car) {
		Car car2=this.carDao.findById(car.getId());
		BeanUtils.copyProperties(car,car2,BeanTool.getNullPropertyNames(car));
		this.carDao.save(car2);
		return 1;
	}

	@Override
	public Car getCar(String id) {
		return this.carDao.findById(id);
	}

	@Override
	public List<CarDept> queryCar(HashMap<String, Object> hm) {
		List<CarDept> resultlist=null;
		Map<String, Object> map=queryCarPage(hm);
		if(map!=null){
			resultlist=new ArrayList<CarDept>();
			resultlist=(List<CarDept>) map.get("rows");
		}
		return resultlist;
	}

	@Override
	public Map<String, Object> queryCarPage(HashMap<String, Object> hm) {
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
		final String notInCarIds=(String) (hm.get("notInCarIds")==null?"":hm.get("notInCarIds"));//
		final String eid=(String) (hm.get("eid")==null?"":hm.get("eid"));//企业id
		final List inCarIds=(List) ((hm.get("inCarIds")==null||hm.get("inCarIds").equals(""))?new ArrayList():hm.get("inCarIds"));//
		
		final String license=(String) (hm.get("license")==null?"":hm.get("license"));//车牌号
		final String SIM=(String) (hm.get("SIM")==null?"":hm.get("SIM"));//SIM卡
		final String type=(String) (hm.get("type")==null?"":hm.get("type"));//车辆类型
		//final String deptIds=(String) (hm.get("deptIds")==null?"":hm.get("deptIds"));//选择的部门Ids
		
		final String treeSearch=(String) (hm.get("treeSearch")==null?"":hm.get("treeSearch"));//。。按车牌号和 部门名称？暂不实现
		final String status=(String) (hm.get("status")==null?"":hm.get("status"));//车辆状态 
		final String startDate=(String) (hm.get("startDate")==null?"":hm.get("startDate"));//时间范围 开始时间
		
		final String endDate=(String) (hm.get("endDate")==null?"":hm.get("endDate"));//时间范围  结束时间
		final String carTreeSearch=(String) (hm.get("carTreeSearch")==null?"":hm.get("carTreeSearch"));//按照车牌号、手机号、终端code模糊匹配
		final String foreignIds=(String) (hm.get("foreignIds")==null?"":hm.get("foreignIds"));//选择的部门Id
		
		
		List<String> foreignlist=null;
		if(StringUtils.isNotEmpty(foreignIds)){
			List<CarAlarmForeign> cflist=this.carAlarmForeginService.getCarAlarmForeigeByForeignId(foreignIds);
			if(cflist!=null&&cflist.size()>0){
				foreignlist=new ArrayList<String>();
				for(CarAlarmForeign cf:cflist){
					foreignlist.add(cf.getCarId());
				}
			}else return null;
		}
		
		final List<String> foreignlist_=foreignlist;
		
		
		
		final List<String> childdeptids=(List<String>) hm.get("deptIds");//根据部门查找子部门

		
		Specification<Car> spec = new Specification<Car>() {
    		@Override
    		public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> idpath=root.get("id");
    			Path<String> deptidpath=root.get("depId");
    			Path<String> eidpath=root.get("eid");
    			Path<String> createUseridPath=root.get("createUserid");
    			Path<String> licensePath=root.get("license");
    			Path<String> SIMPath=root.get("terminal").get("mobile");
    			Path<String> typePath=root.get("type");
    			Path<String> statusPath=root.get("status");
    			Path<String> codePath=root.get("terminal").get("code");
    			
    			//状态不为3
    			Predicate ps=builder.notEqual(statusPath, "3");
				predicateList.add(ps);
    			
    			//车辆id
    			if (StringUtils.isNoneEmpty(carId)) {
    				Predicate p=builder.equal(idpath, carId);
    				predicateList.add(p);
    			}
    			
    			//in
    			if (inCarIds!=null&&inCarIds.size()>0) {
    				Predicate p=idpath.in(inCarIds);
    				predicateList.add(p);
    			}
    			
    			//license
    			if (StringUtils.isNoneEmpty(license)) {
    				Predicate p=builder.like(licensePath, "%"+license+"%");
    				predicateList.add(p);
    			}
    			
    			//SIM
    			if (StringUtils.isNoneEmpty(SIM)) {
    				Predicate p=builder.like(SIMPath, "%"+SIM+"%");
    				predicateList.add(p);
    			}
    			
    			//type
    			if (StringUtils.isNoneEmpty(type)) {
    				Predicate p=builder.equal(typePath, type);
    				predicateList.add(p);
    			}
    			
    			//status
    			if (StringUtils.isNoneEmpty(status)) {
    				Predicate p=builder.equal(statusPath, status);
    				predicateList.add(p);
    			}
    			
    			//carTreeSearch
    			if (StringUtils.isNoneEmpty(carTreeSearch)) {
    				List<Predicate> predicateList1=new ArrayList<Predicate>();
    				Predicate p1=builder.like(licensePath, "%"+carTreeSearch+"%");
    				Predicate p2=builder.like(codePath, "%"+carTreeSearch+"%");
    				Predicate p3=builder.like(SIMPath, "%"+carTreeSearch+"%");
    				predicateList1.add(p1);
    				predicateList1.add(p2);
    				predicateList1.add(p3);
    				if(predicateList1!=null&&predicateList1.size()>0){
    					Predicate[] predicatess = new Predicate[predicateList1.size()];
    					predicateList1.toArray(predicatess);
						predicateList.add(builder.or(predicatess));
    				}
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
    			
    			//企业id
    			if(StringUtils.isNotEmpty(eid)){
    				Predicate p=builder.equal(eidpath, eid);
    				predicateList.add(p);
    			}
    			
    			//foreignlist_
    			if (foreignlist_!=null&&foreignlist_.size()>0) {
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
    		Page<Car> page=this.carDao.findAll(spec, pageRequest);
    		List<CarDept> result=FormatResult(page.getContent());//关联信息（车辆、部门）
        	m=new HashMap<String, Object>();
        	m.put("total", page.getTotalPages());
        	m.put("page", pageNumber);
        	m.put("records", page.getTotalElements());
        	m.put("rows", result);
    	}else{
    		Sort sort = new Sort(Direction.DESC,new String[]{"operDate"});
    		List<Car> carlist=this.carDao.findAll(spec,sort);
    		if(carlist!=null&&carlist.size()>0){
    			m=new HashMap<String, Object>();
    			List<CarDept> result=FormatResult(carlist);
    			m.put("rows", result);
    		}
    	}
    	return m;
	}

	@Override
	public List queryCar(final List list) {
		Specification<Car> spec = new Specification<Car>() {
    		@Override
    		public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> idpath=root.get("id");
    			Path<String> statusPath=root.get("status");
    			if(list!=null&&list.size()>0){
    				Predicate p=idpath.in(list);
    				predicateList.add(p);
    			}
    			
    			//状态不为3
    			Predicate p=builder.notEqual(statusPath, "3");
				predicateList.add(p);
    			
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    			}
    		};
		Sort sort = new Sort(Direction.DESC,new String[]{"operDate"});
		List<Car> carlist=this.carDao.findAll(spec,sort);
		List<CarDept> result=null;
		if(carlist!=null&&carlist.size()>0){
			result=new ArrayList<CarDept>();
			result=FormatResult(carlist);
		}
		return result;
	}

	@Override
	public int haslicense(final String license, final String eid) {
		Specification<Car> spec = new Specification<Car>() {
    		@Override
    		public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> licensepath=root.get("license");
    			Path<String> eidpath=root.get("eid");
    			Path<String> statusPath=root.get("status");
    			
    			if(StringUtils.isNoneBlank(license)){
    				Predicate p=builder.equal(licensepath, license);
    				predicateList.add(p);
    			}
    			if(StringUtils.isNoneBlank(eid)){
    				Predicate p=builder.equal(eidpath, eid);
    				predicateList.add(p);
    			}
    			
    			//状态不为3
    			Predicate p=builder.notEqual(statusPath, "3");
				predicateList.add(p);
    			
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    			}
    		};
		List<Car> carlist=this.carDao.findAll(spec);
		if(carlist!=null&&carlist.size()>0){
		return carlist.size();	
		}
		else return 0;
	}

	@Override
	public Boolean isPointHasCar(String carId) {
		List list=this.pointCarForeignDao.findByCarId(carId);
		if(list!=null&&list.size()>0){
			return true;
		}
		else return false;
	}

	@Override
	public int delCar(Car car) {
		this.carDao.delete(car);
		return 1;
	}

	@Override
	public int delCarDriverByCar(CarDriver carDriver) {
		this.carDriverDao.delete(carDriver);
		return 1;
	}

	@Override
	public int delCarDriverbyCarId(String carId) {
		this.carDriverDao.deleteCarDriverByCarid(carId);
		return 1;
	}

	@Override
	public int delCarDriverbyDriver(String driver) {
		this.carDriverDao.deleteCarDriverByDriverid(driver);
		return 1;
	}

	@Override
	public int setDriver(CarDriver CarDriver) {
		this.carDriverDao.save(CarDriver);
		return 1;
	}

	@Override
	public int delDriver(CarDriver CarDriver) {
		this.carDriverDao.delete(CarDriver);
		return 1;
	}

	@Override
	public Car getCarByLicense(final String license,final String eid) {
		Car car=new Car();
		Specification<Car> spec = new Specification<Car>() {
    		@Override
    		public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> licensepath=root.get("license");
    			Path<String> eidpath=root.get("eid");
    			Path<String> statusPath=root.get("status");
    			
    			if(StringUtils.isNoneBlank(license)){
    				Predicate p=builder.equal(licensepath, license);
    				predicateList.add(p);
    			}
    			
    			if(StringUtils.isNoneBlank(eid)){
    				Predicate p=builder.equal(eidpath, eid);
    				predicateList.add(p);
    			}
    			
    			//状态不为3 未上线
    			Predicate p=builder.notEqual(statusPath, "3");
				predicateList.add(p);
    			
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    			}
    		};
		List<Car> carlist=this.carDao.findAll(spec);
		if(carlist!=null&&carlist.size()>0){
			car=carlist.get(0);
			car.setTerminal(null);
		}
		return car;
	}

	@Override
	public int getCarCount(HashMap hm) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List getCarDept(String carId) {
		// TODO Auto-generated method stub
		return null;
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
			sort = new Sort(Direction.DESC, "operDate");
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	
	public List<CarDept> FormatResult(List<Car> carlist){
		List<CarDept> resultlist=null;
		if(carlist!=null&&carlist.size()>0){
			resultlist=new ArrayList<CarDept>();
			for(int i=0;i<carlist.size();i++){
				
				Car car=carlist.get(i);
				String deptid=car.getDepId();
				InfoDeptEntity dept=this.infoDeptService.findDeptById(deptid);
				DataWordbook DataWordbook = this.dataWordbookDao.findByCode(car.getType());
				
				String typeStr=null;
				if(DataWordbook!=null){
					typeStr = DataWordbook.getName();
				}
				
				StringBuffer driverNames = new StringBuffer();//司机名称
				List<CarDriver> cardrivers=this.carDriverDao.findCarDriverByCarId(car.getId());
				if(cardrivers!=null&&cardrivers.size()>0){
					for(int j=0;j<cardrivers.size();j++){
						CarDriver cardriver=cardrivers.get(j);
						Driver driver=this.driverDao.findDriverById(cardriver.getDriverId());
						if(driver!=null&&driver.getName()!=null){
							driverNames.append(driver.getName().trim()).append(",");
						}
					}
					if(driverNames.length()>0){
						driverNames.delete(driverNames.length()-1, driverNames.length());
					}
				}
				
				CarDept carDept=new CarDept();
				carDept.setId(car.getId());
				carDept.setBrand(car.getBrand());
				carDept.setColor(car.getColor());
				carDept.setCreateUserid(car.getCreateUserid());
				carDept.setDeptName(dept.getName());
				carDept.setDeptId(car.getDepId());
				carDept.setLicense(car.getLicense());
				carDept.setOperDate(car.getOperDate());
				carDept.setOthers(car.getOthers());
				carDept.setStatus(car.getStatus());
				carDept.setPetrol(car.getPetrol());
				carDept.setStopDate(car.getStopDate());
				carDept.setTypeId(car.getType());
				carDept.setTypeName(typeStr);
				carDept.setDriverName(driverNames.toString());
				
				if(car.getTerminal()!=null){
					carDept.setTerminalCode(car.getTerminal().getCode());
					carDept.setSIM(car.getTerminal().getMobile());
				}
				
				resultlist.add(carDept);
			}
		}
		return resultlist;
	}
}
