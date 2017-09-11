package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.supermap.egispservice.lbs.constants.Config;
import com.supermap.egispservice.lbs.dao.CarGpsDao;
import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.entity.DataWordbook;
import com.supermap.egispservice.lbs.entity.Driver;
import com.supermap.egispservice.lbs.mongobase.MongoDbGlobal;
import com.supermap.egispservice.lbs.mongobase.MongoDbHelper;
import com.supermap.egispservice.lbs.pojo.CarGps;
import com.supermap.egispservice.lbs.pojo.CarHistoryGps;
import com.supermap.egispservice.lbs.pojo.JsonZTree;
import com.supermap.egispservice.lbs.service.BaseService;
import com.supermap.egispservice.lbs.service.CarHistoryService;
import com.supermap.egispservice.lbs.service.CarLocationService;
import com.supermap.egispservice.lbs.service.CarRegSearchService;
import com.supermap.egispservice.lbs.service.CarService;
import com.supermap.egispservice.lbs.service.CarSpeedingService;
import com.supermap.egispservice.lbs.service.DataworkService;
import com.supermap.egispservice.lbs.service.DriverService;
import com.supermap.egispservice.lbs.service.RegionService;
import com.supermap.egispservice.lbs.service.RegionSetService;
import com.supermap.egispservice.lbs.service.TerminalService;
import com.supermap.egispservice.lbs.service.TerminalTypeService;
import com.supermap.egispservice.lbs.util.Pagination;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class testDriver {
	
	@Autowired
	DriverService driverService;
	
	@Autowired
	CarService carService;
	
	@Autowired
	TerminalService terminalService;
	
	@Autowired
	TerminalTypeService terminalTypeService;
	
	@Autowired
	DataworkService dataworkService;
	
	@Autowired
	BaseService baseService;
	
	@Autowired
	CarHistoryService carHistoryService;
	
	@Autowired
	CarLocationService carLocationService;
	
	@Autowired
	CarRegSearchService carRegSearchService;
	
	@Autowired
	CarGpsDao carGpsDao;
	
	@Autowired
	CarSpeedingService carSpeedingService;
	
	@Autowired
	RegionService regionService;
	
	@Autowired
	RegionSetService regionSetService;
	
	//@Test
	public void testadd(){
		
		for(int i=5;i<10;i++){
			Driver d=new Driver();
			d.setCreateUserid("40288fd94add378d014add3823f10063");
			d.setAge(Short.valueOf("20"));
			d.setDeptId("f9a8d6684a8b45f3014a8ef5e7e90003");
			d.setEid("40288e9f48625c010148625c07160000");
			d.setLicense("125125");
			d.setName("ouye_"+i);
			d.setPhone("13520156131");
			
			//int x=this.driverService.addDriver(d);//添加
			
		}
		
		
		Driver d2=new Driver();
		d2.setId("40288fcd5258b55b015258b587600000");
		d2.setName("ouye/ou");
		//int x=this.driverService.updateDriver(d2);//修改
		
		int x=0;
		
		//x=this.driverService.delDriver("40288fcd5258bf99015258bfa5d70004");//删除
		
		HashMap m=new HashMap();
		//m.put("userId", "40288fd94add378d014add3823f10063");
		m.put("userId", "40288e9f483f48e501483f48eb060000");
		m.put("pageNumber", 1);
		m.put("pageSize", 10);
		m.put("sortType", "auto");
		
		//Map<String,Object> result=this.driverService.queryDriverPage(m);//查询
		
		List list=null;
		//List result=this.carService.queryCar(list); //查询车辆
		
		//System.out.println(result.size());
		System.out.println(x);
		
	}
	
	@Test
	public void testCarService(){
		HashMap m=new HashMap();
		//m.put("userId", "40288fd94add378d014add3823f10063");
		//m.put("userId", "40288e9f483f48e501483f48eb060000");
		m.put("pageNumber", 1);
		m.put("pageSize", 10);
		m.put("sortType", "auto");
		m.put("deptId", "40288e9f48627238014862723e870000");
		m.put("type", "190002");
		//m.put("deptId", "40288e9f48627238014862723e870000");
		//m.put("deptcode", "00001000");
		//m.put("eid", "40288e9f48625c010148625c07160000");

		
	//	{deptcode=00001000, pageSize=10, pageNumber=1, eid=40288e9f48625c010148625c07160000, deptId=40288e9f48627238014862723e870000}
	//{deptcode=00001000, userId=40288e9f483f48e501483f48eb060000, pageSize=10, pageNumber=1, eid=40288e9f48625c010148625c07160000, deptId=f9a8d6684a8b45f3014a8ef4e2ae0001}
		
		Map<String,Object> result=this.carService.queryCarPage(m);
		System.out.println(result);
		
		//List<JsonZTree>  list=this.carService.getCarDeptTree("40288e9f48627238014862723e870000", null);
		//System.out.println(list.size());
		
		/*Car car=new Car();
		car.setBrand("1");
		car.setLicense("1");
		car.setType("11");
		car.setCreateUserid("40288e9f483f48e501483f48eb060000");
		car.setDepId("f9a8d6684a8b45f3014a8ef5e7e90003");
		car.setEid("40288e9f48625c010148625c07160000");
		car.setOperDate(new Date());*/
		//this.carService.addCar(car);
	
	}
	
	//@Test
	public void dataword(){
		HashMap m=new HashMap();
		m.put("code", "190005");
		List<DataWordbook>  list=this.dataworkService.getDataWordbookList(m);
		System.out.println(list.size());
	}
	
	//@Test
	public void testMongo(){
		DB db = MongoDbHelper.getMongodb();
		DBCollection currentGps = db.getCollection(MongoDbGlobal.CAR_CURRENT_GPS);
		System.out.println(db);
		System.out.println(currentGps);
	}
	
	//@Test
	public void testbase(){
		/*Map m=this.baseService.getALARM_MAP();
		System.out.println(m);*/
		CarGps carGps=new CarGps();
		Car car=this.carService.getCar("40288fcd52689fd4015268a09ad80000");
		carGps.setCarId(car.getId());
		carGps.setCarType(car.getType());
		carGps.setTemCode("001");
		
		try {
		//	this.carGpsDao.saveCarCurrentGps(carGps);
			List<String> ids=new ArrayList<String>();
			ids.add("40288fcd52689fd4015268a09ad80000");
			
			HashMap<String, Object> hm=new HashMap<String,Object>();
			hm.put("carIds", ids);
			
			List<CarGps> list=this.carLocationService.carLocation(hm);
			System.out.println(list.get(0).getCarId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testtree(){
		List<JsonZTree> tree=this.carService.getCarDeptTree("40288e9f48627238014862723e870000", "f9a8d6684a8b45f3014a8ef4e2ae0001");
		System.out.println(tree.size());
	}
	
	//@Test
	public  void testBindDriver(){
		HashMap hm=new HashMap();
		hm.put("userId", "40288e9f483f48e501483f48eb060000");
		hm.put("carId", "40288fcd5276841b015276ab73f90000");
		this.driverService.queryBindDriver(hm);
	}
	
	//@Test
	public void testRegionSet(){
		HashMap hm=new HashMap();
		hm.put("eid", "8a04a77b4ee30e56014f1f9a23fe02a4");
		Map<String,Object> result=this.regionSetService.pagequeryRegionSet(hm);
	}
	
	//@Test
	public void testCarHistory(){
		/*Pagination page=new Pagination();
		page.setPageNo(1);
		page.setPageSize(10);
		HashMap hm=new HashMap();
		hm.put("deptId", "40288e9f48627238014862723e870000");
		page=this.carHistoryService.getMileReportList(page, hm);*/
		
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			HashMap hm=new HashMap();
			hm.put("carId", "8a7109fe42a269240144afe654f40186");
			hm.put("startDate", sf.parse("2015-05-13 00:00:00"));
			hm.put("endDate", sf.parse("2015-05-13 23:55:49"));
			hm.put("isRuning", true);
			CarHistoryGps gps=this.carHistoryService.carHistory(hm, null);
			
			
			
			List l=this.carHistoryService.getCarHistoryGpsList("8a7109fe42a269240144afe654f40186", sf.parse("2015-05-13 00:00:00"), sf.parse("2015-05-13 23:55:49"));
			System.out.println(l.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//this.carHistoryService.carHistory(hm, page);
	}
	

}
