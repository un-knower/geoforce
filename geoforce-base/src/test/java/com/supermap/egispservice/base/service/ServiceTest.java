/*package com.supermap.egispservice.base.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.supermap.egispservice.base.dao.PointDao;
import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.entity.PointExtcolEntity;
import com.supermap.egispservice.base.entity.PointExtcolValEntity;
import com.supermap.egispservice.base.entity.PointGroupEntity;
import com.supermap.egispservice.base.entity.PointPicEntity;
import com.supermap.egispservice.base.entity.PointStyleCustomEntity;
import com.supermap.egispservice.base.entity.PointStyleEntity;
import com.supermap.egispservice.base.entity.RoleVO;
import com.supermap.egispservice.base.entity.SysDefaultCityEntity;
import com.supermap.egispservice.base.entity.SysLogEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.pojo.NetPointInfoResult;
import com.supermap.egispservice.base.service.PointExtcolService;
import com.supermap.egispservice.base.service.PointExtcolValService;
import com.supermap.egispservice.base.service.PointService;
import com.supermap.egispservice.geocoding.service.IGeocodingService;

import junit.framework.TestCase;

public class ServiceTest{
	 ClassPathXmlApplicationContext ctx = null;
	 PointExtcolService pointextcolservice = null;
	 PointExtcolValService pointextcolvalservice = null;
	 PointService pointservice=null;
	 PointDao pointDao=null;
	 
	 PointGroupService pointgroupservice=null;
	 
	 PointStyleService  pointstyleservice=null;
	 
	 UserService userservice=null;
	 
	 IGeocodingService geocodingService=null;
	 
	 SysLogService sysLogService=null;
	 
	 private static final Logger LOGGER = Logger.getLogger(ServiceTest.class);
	
	@Before
	public  void init() throws Exception {
		System.out.println("++++++++++before");
		//ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		//System.out.println("++++++++++"+ctx);
		pointextcolservice = (PointExtcolService) ctx.getBean("pointExtcolService");
		//System.out.println("++++++++++"+this.service);
		pointextcolvalservice = (PointExtcolValService) ctx.getBean("pointExtcolValService");
		//System.out.println("++++++++++"+this.service);
		pointservice = (PointService) ctx.getBean("pointService");
		pointDao = (PointDao) ctx.getBean("pointDao");
		userservice=(UserService) ctx.getBean("userService");
		
		pointgroupservice=(PointGroupService) ctx.getBean("pointGroupService");
		
		pointstyleservice=(PointStyleService) ctx.getBean("pointStyleService");
		
		//geocodingService=(IGeocodingService) ctx.getBean("geocodingService");
		
		sysLogService=(SysLogService) ctx.getBean("sysLogService");
		
		//System.out.println("++++++++++this.pointDao:"+this.pointDao);
	}
	
	//@Test
	public void testProcess(){
		List<String> ids=new ArrayList<String>();
		ids.add("40288fcd51d362d80151d3656fc90000");
		this.pointservice.startProcess(ids);
		
		this.pointextcolvalservice.findByPointidOrUserid(null, "40288e9f483f48e501483f48eb060000");
	}
	
	
	//@Test
	public void testSyslog() throws Exception{
		SysLogEntity log=new SysLogEntity();
		log.setEnterpriseId("11");
		log.setDepartmentId("22");
		log.setIpaddr("31");
		log.setModuleId("44");
		log.setOperDesc("55");
		log.setOperTime(new Date());
		UserEntity u=this.userservice.findUserById("40288fa250dc31900150dc35041d0001");
		log.setUserId(u);
		//log=this.sysLogService.saveSysLogEntity(log);
		//System.out.println(log.getId());
		List<String> userid=new ArrayList<String>();
		userid.add("40288fa250dc31900150dc35041d0001");
		userid.add("22");
		List<String> deptid=new ArrayList<String>();
		deptid.add("22");
		List<String> moduleid=new ArrayList<String>();
		moduleid.add("44");
		Map m=this.sysLogService.findLogsByParam(userid, "11", deptid, "t", moduleid, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-12-15 10:00:00"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-12-15 20:00:00"), 1, 10, "auto");
		System.out.println(m);
	}
	
	
	//@Test
	public void saveDefaultCity(){
		SysDefaultCityEntity sys=new SysDefaultCityEntity();
		sys.setAdmincode("110000");
		sys.setCity("beij");
		sys.setClevel("1");
		sys.setCounty("beji");
		sys.setCreateTime(new Date());
		sys.setDefaultname("北京");
		sys.setDeptid("11");
		sys.setEid("22");
		sys.setProvince("bj");
		sys.setUserid("55555");
		//sys=this.userservice.saveUserDefaultCity(sys);
		sys=this.userservice.findUserDefaultCity("55555");
		System.out.println(sys.getId());
	}
	
	@Test
	public void mytest(){
		Map<String, Object> map=this.pointservice.queryAllByAdmincodeForConverge("40288e9f483f48e501483f48eb060000", 
				null, null, null, "40288e9f48625c010148625c07160000", 
				"00001000", -1, 10, null,"3201",false);
		List<NetPointInfoResult> npis=(List<NetPointInfoResult>) map.get("records");
		System.out.println("查询：" + npis.size());
		//List<String> ids=this.pointservice.getAllProcessingPointByUserid("40288fd94add378d014add3823f10063");
	
		List<PointExtcolValEntity> vallist=this.pointextcolvalservice.findByPointidOrUserid("", "40288fd94add378d014add3823f10063");
		System.out.println("查询：" + vallist.size());
		
		//PointEntity point=this.pointservice.queryById("ff8080815185d05d01518a9502210471");
		Map<String,Object> list= this.pointservice.queryFailedPoints("40288fd94add378d014add3823f10063");
		List<NetPointInfoResult> npis=(List<NetPointInfoResult>) list.get("records");
		System.out.println(npis.size());
		
		List<String> processing=this.pointservice.getAllProcessingPointByUserid("40288e9f483f48e501483f48eb060000");
		System.out.println(processing.size());
	}
	
	
	
	
	//@Test
	public void updateExtVal() throws Exception{
		//this.pointextcolvalservice.updatePointExtcolVal("col2", "11", "40288fb05119849701511984f7300000", null);
		//PointEntity p=new PointEntity();
		//p=this.pointservice.queryById("ff808081513dde4b015142c6c110002f");
		//p.setStyleid(null);
		//p.setName("2233335");
		//this.pointservice.updatePoint(p);
		List<PointEntity>  list=this.pointDao.findByStyleid("ff808081513dde4b01513e0e4c4c0004");
		System.out.println(list);
		
		List<PointEntity>  plist=this.pointDao.findByGroupid("40288fb050cbfe060150cbfe23870000");
		System.out.println(plist);
		
		//List<String> ids =new ArrayList<String>();
		//ids.add("ff808081513dde4b015142c4ebf5002d");
		//this.pointservice.updatePointStylesToNUll(ids);
		//int x=this.pointDao.updatePointDefaultStyle("40288fd94add378d014add3823f10063");
		
		//this.pointstyleservice.deleteCustomfileByid("ff8080815146b4a20151563190560069");
		//System.out.println(x);
		Map<String, Object> map=this.pointservice.queryAllByPage("8a04a77b4de717f2014e0b472f640108", 
				"888", null, null, "8a04a77b4de717f2014e0b472f3d0107", 
				"00129000", -1, 10, null);
		List<NetPointInfoResult> npis=(List<NetPointInfoResult>) map.get("records");
		JSONObject jsonObjectss = JSONObject.fromObject(map.toString());
		Object[] obj=getJsonToArray(jsonObjectss.get("records").toString());
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
		
		LOGGER.info("++++++++++++++++++++++开始查询时间："+sf.format(new Date()));
		List<PointEntity> pointlist=this.pointDao.getNullAdmincodePoints();
		LOGGER.info("++++++++++++++++++++++结束查询时间："+sf.format(new Date()));
		LOGGER.info("++++++++++++++++++++++开始匹配admincode时间："+sf.format(new Date()));
		if(pointlist!=null&&pointlist.size()>0){
			for(PointEntity p:pointlist){
				double smx=p.getSmx().doubleValue();
				double smy=p.getSmy().doubleValue();
				Map<String,String> m=geocodingService.searchAdmincodeForCounty(smx,smy);
				if(m!=null){
					p.setAdmincode(m.get("ADMINCODE"));
					p.setProvince(m.get("PROVINCE"));
					p.setCity(m.get("CITY2"));
					p.setCounty(m.get("COUNTY"));
				}else{
					LOGGER.info("失败坐标："+p.getId()+","+p.getStatus()+","+p.getSmx()+"，"+p.getSmy());
					continue;
				}
			}
		}
		LOGGER.info("++++++++++++++++++++++结束匹配admincode时间："+sf.format(new Date()));
		
		//this.pointDao.save(pointlist);
		
		
		LOGGER.info("++++++++++++++++++++++开始查询时间-2："+sf.format(new Date()));
		List pointlist2=this.pointDao.getNullAdmincodePoints2();
		List<PointEntity> pointlist3=null;
		if(pointlist2!=null&&pointlist2.size()>0){
			pointlist3=new ArrayList<PointEntity>();
			for(Object obj:pointlist2){
				Object x[]=(Object[])obj;
				String id=(String)x[0];
				PointEntity p=this.pointDao.findOneByPointid(id);
				pointlist3.add(p);
			}
		}
		LOGGER.info("++++++++++++++++++++++结束查询时间-2："+sf.format(new Date()));
		//System.out.println(pointlist);
	}
	
	
	
	//@Test
	public void addCustomFile(){
		PointStyleCustomEntity custom=new PointStyleCustomEntity();
		custom.setDcode("00001000");
		custom.setUserid("40288e9f483f48e501483f48eb060000");
		custom.setEid("40288e9f48625c010148625c07160000");
		custom.setFilepath("ss/ss555.jpg");
		custom.setUploadtime(new Date());
		//this.pointstyleservice.saveCustomFiles(custom);
		//PointStyleCustomEntity p=this.pointstyleservice.findCustomfileByid("40288fb05118988701511898a1280000");
		//this.pointstyleservice.deleteCustomfileByid("40288fb05118988701511898a1280000");
		//List<PointStyleCustomEntity> list=this.pointstyleservice.findCustomsByUserid("40288e9f483f48e501483f48eb060000");
		//this.pointstyleservice.deletePointStyle("40288fb050f02fd80150f02ff29b0001");
		PointGroupEntity group=new PointGroupEntity();
		group.setId("40288fb050f02fd80150f02ff29e0002");
		group.setStyleid(null);
		this.pointgroupservice.updateGroup(group);
		//List<PointGroupEntity>  list=this.pointgroupservice.queryAllGroups(null, null, "40288e9f483f48e501483f48eb060000", null, null);
		//System.out.println(list.size());
	}
	
	
	//@Test
	public void queryPoint(){
		Map<String, Object> map=this.pointservice.queryAllByPage("40288e9f483f48e501483f48eb060000", 
				null, "子部门", null, null, 
				null, -1, 10, null);
		List<NetPointInfoResult> npis=(List<NetPointInfoResult>) map.get("records");
		System.out.println("npis:"+npis.size());
		
		PointStyleEntity style=new PointStyleEntity();
		style.setId("40288fb050f035030150f0351b3f0001");
		PointGroupEntity group=this.pointgroupservice.findByStyleid("40288fb050f035030150f0351b3f0001");
	
		
		Map searmap=new HashMap();
		searmap.put("stylename", "默认样式");
		searmap.put("userid", "40288e9f483f48e501483f48eb060000");
		searmap.put("eid", "40288e9f48625c010148625c07160000");
		searmap.put("dcode", "00001000");
		searmap.put("def1", "0");
		 List<PointStyleEntity>  stylelist=this.pointstyleservice.findStyleByParam(searmap);
		 System.out.println(stylelist.size());
	}

	@Test
	public void testMain() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
		context.start();
		System.out.println("按任意键退出");
		try {
			System.in.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//@Test
	public void testPointExtcol() {
	// CRUD测试
	System.out.println("--- CRUD测试 ---");
	PointExtcolEntity record=new PointExtcolEntity();
	//record.setId("1111");
	//record.setCol1("22");
	record.setDefaultcol("002");
	record.setUserid("40288e9f483f48e501483f48eb060000");
	System.out.println(this.pointextcolservice);
	System.out.println("添加：" + this.pointextcolservice.updatePointExtcol("col1", "", "40288e9f483f48e501483f48eb060000")); // 修改
	//System.out.println("添加：" + this.service.addPointExtcol(record)); // 修改
	}
	
	
//	@Test
//	public void testPointExtcolVal() {
//	// CRUD测试
//	System.out.println("--- PointExtcolVal测试 ---");
//	PointExtcolValEntity record=new PointExtcolValEntity();
//	//record.setId("1111");
//	//record.setCol1("22");
////	record.setUserid("wrwer");
//	//System.out.println(this.service);
//	//System.out.println("添加：" + this.service.updatePointExtcol("col1", "", "wrwer")); // 修改
//	System.out.println("添加：" + this.valservice.addPointExtcolVal("col3", "666", "336", "wrwer")); // 修改
//	System.out.println("添加：" + this.valservice.addPointExtcolVal("col1", "44", "jhkyu", "wrwer")); // 修改
//	System.out.println("添加：" + this.valservice.addPointExtcolVal("col1", "55", "jhk", "wrwer")); // 修改
//	System.out.println("添加：" + this.valservice.addPointExtcolVal("col1", "66666", "e5t", "wrwer")); // 修改
//	System.out.println("添加：" + this.valservice.addPointExtcolVal("col7", "66666", "j767", "wrwer")); // 修改
//	//System.out.println("添加：" + this.valservice.updatePointExtcolVal("col1", "4个22222", "sfrr", "wrwer")); // 修改
//	//this.valservice.deletePointExtcolValByPointid("222222"); // 修改
//	
//	}
	
	
	
//	/@Test
	public void testFindPointExtcol() {
	// CRUD测试
	System.out.println("---pointservice测试 ---");
	PointExtcolEntity record=new PointExtcolEntity();
	//record.setId("1111");
	//record.setCol1("22");
	record.setDefaultcol("002");
	record.setUserid("wrwer");
	System.out.println(this.pointextcolservice);
	//System.out.println("添加：" + this.service.findByUserid("40288e9f483f48e501483f48eb060000")); // 修改
	
	//System.out.println("添加：" + this.valservice.findByPointidOrUserid("222", "").size()); // 修改
	
	System.out.println("添加：" + this.pointextcolvalservice.addPointExtcolVal("col1", "999", "ff8080814f8298a8014f8bec5ff60059", "ff8080814f262ffc014f730a19290088")); // 修改
	//System.out.println("添加：" + this.service.addPointExtcol(record)); // 修改"40288fb04e489914014e48a8e86f0000"
	Map<String, Object> map=this.pointservice.queryAllByPage("40288e9f483f48e501483f48eb060000", "北京", null, null, "40288e9f48625c010148625c07160000", "00001000", -1, 10, null);
	List list=(List) map.get("records");
	NetPointInfoResult point=(NetPointInfoResult) list.get(0);
	
	System.out.println("---pointservice测试 ---"+list.size());
	
	//PointEntity point=this.pointservice.queryById("40288fb04f695bfc014f695e614a0000");
	//List<PointEntity>  pointlist=this.pointservice.queryByAreaId("5c3490c1ee6644d7990b88c7dace9f4e", "40288e9f48625c010148625c07160000", "40288e9f48627238014862723e870000");
	//System.out.println("pointlist:"+pointlist.size());
	
	
//	PointEntity pointEntity=new PointEntity();
//	pointEntity.setAddress("中国江苏省常州市天宁区");
//	pointEntity.setName("test2");
//	pointEntity.setUserId("222");
//	pointEntity.setEnterpriseId("222");
//	pointEntity.setDepartmentId("222");
//	pointEntity.setId("40288fb04f67e541014f68a927e60007");
//	/boolean y=this.pointservice.updatePoint(pointEntity);
	
	System.out.println("111");
	//addPointTest();
	
	
	}
	
	*//**
	 * 添加网点
	 * 
	 * @Author Juannyoh
	 * 2015-8-25下午3:46:40
	 *//*
	//@Test
	public void addPointTest(){
		List<PointEntity> netPoints=new ArrayList<PointEntity>();
		for(int i=0;i<1;i++){
			PointEntity record=new PointEntity();
			record.setAddress("中国江苏省常州市天宁区");
			record.setName("test经纬度222"+i);
			record.setUserId("40288fd94add378d014add37b89d0010");
			record.setEnterpriseId("40288e9f48625c010148625c07160000");
			record.setDepartmentId("f9a8d6684a8b45f3014a8ef5e7e90003");
			record.setStatus(0);
			record.setSmx(BigDecimal.valueOf(116.271433958892));
			record.setSmy(BigDecimal.valueOf(39.8812143512846));
			record.setCreateTime(new Date());
			PointExtcolValEntity pointExtcolValEntity =new PointExtcolValEntity();
			pointExtcolValEntity.setCol1("222");
			pointExtcolValEntity.setCol2("222");
			pointExtcolValEntity.setUserid("222");
			
			//record.setPointExtcolValEntity(pointExtcolValEntity);
			//if(pointExtcolValEntity.getPointid()==null){
				//this.pointDao.save(record);
			//}
			this.pointDao.save(record);
			pointExtcolValEntity.setPointid(record.getId());
			this.pointextcolvalservice.save(pointExtcolValEntity);
			//String id =this.pointextcolvalservice.save(pointExtcolValEntity);
			String id=record.getId();
			pointExtcolValEntity.setPointid(record.getId());
			record.setPointExtcolValEntity(pointExtcolValEntity);
			
			PointGroupEntity groupentity=new PointGroupEntity();
			groupentity.setGroupname("xxx"+i);
			groupentity.setId("40288fb050fae9170150fae930160002");
			record.setGroupid(groupentity);
			netPoints.add(record);
		}
		
		for(int i=0;i<netPoints.size();i++){
			PointEntity record=netPoints.get(i);
			PointExtcolValEntity extval=record.getPointExtcolValEntity();
			record.setPointExtcolValEntity(null);
			PointEntity id=this.pointDao.save(record);
			System.out.println(id.getId());
			extval.setPointid(id.getId());
			this.valservice.save(extval);
		}
		
		try {
			//Map<String, Object> ids=this.pointservice.importNetPoints(netPoints);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	@Test
//	public void testUserRolelist(){
//		List<RoleVO> list=userservice.getCheckedRole("40288fd94add378d014add37bdd30014", "8a283f934b8195d4014c455ac39f01ec", "40288e9f48625c010148625c07160000", "f9a8d6684a8b45f3014a8ef5e7e90003");
//	}
	
	//@Test
	public  void updatePoint(){
		PointEntity point =new PointEntity();
		point.setName("北京市丰台区1分店");
		point.setId("ff808081507ee12001508814080a002c");
		try {
			this.pointservice.updatePoint(point);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public  void addPoint(){
		PointEntity point =new PointEntity();
		point.setName("华阳出口33");
		point.setAddress("地址");
		point.setUserId("40288e9f483f48e501483f48eb060000");
		point.setEnterpriseId("40288e9f48625c010148625c07160000");
		point.setDepartmentId("40288e9f48627238014862723e870000");
		
		PointEntity point1 =new PointEntity();
		point1.setName("华阳出口38");
		point1.setAddress("地址");
		point1.setUserId("40288e9f483f48e501483f48eb060000");
		point1.setEnterpriseId("40288e9f48625c010148625c07160000");
		point1.setDepartmentId("40288e9f48627238014862723e870000");
		List<PointEntity> list=new ArrayList<PointEntity>();
		list.add(point);
		list.add(point1);
		
		try {
			//this.pointservice.add(null, null, "北京市丰台区1分店", "地址", null, null, null, null, null, "40288e9f483f48e501483f48eb060000", "40288e9f48625c010148625c07160000", "40288e9f48627238014862723e870000", null);
			Map<String, Object> ids=this.pointservice.importNetPoints(list);
			System.out.println(ids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	//@Test
	public void selectPointlist(){
		Map<String, Object> map=this.pointservice.queryAllByPage("f9a8d6684a19c4b8014a2eba642f001a", "北京", null, null, "f9a8d6684a19c4b8014a2eba642a0019", "00010000", -1, 10, null);
		List<NetPointInfoResult> list=(List<NetPointInfoResult>) map.get("records");
		System.out.println("-------------------"+list.size());
		
		List<PointEntity> list=this.pointservice.queryByAreaId("162d87d3732f4611a07de792b5325ad1", "ff8080814f262ffc014f730a19260087", "ff8080814f262ffc014f730a192b0089");
		System.out.println("-------------------"+list.size());
	}
	
	//@Test
	public void addPointGroup(){
		PointGroupEntity record=new PointGroupEntity();
		record.setCreatTime(new Date());
		record.setDcode("00010001");
		record.setGroupname("group21");
		record.setEid("40288e9f48625c010148625c07160000");
		record.setUserid("40288e9f483f48e501483f48eb060000");
		//String id=this.pointgroupservice.addGroup("子部门商店", "40288e9f483f48e501483f48eb060000", "40288e9f48625c010148625c07160000", "000100010001");
		//System.out.println("id-----------"+id);
		
		PointStyleEntity style=new PointStyleEntity();
		style.setStylename("style1");
		style.setUserid("40288e9f483f48e501483f48eb060000");
		record.setEid("40288e9f48625c010148625c07160000");
		record.setDcode("00010001");
		String styleid=this.pointstyleservice.addPointStyle(style);
		System.out.println("styleid---------------"+styleid);
		
		List<PointGroupEntity> list=new ArrayList<PointGroupEntity>();
		list.add(record);
		//this.pointgroupservice.addPointGroups(list);
		
		//style.setGroupid(record);
		this.pointstyleservice.addPointStyle(style);
		
	}
	
	//@Test
	public void queryPointGroup(){
		String id=null;
		//id="40288fb050cbfe060150cbfe23870000";
		String groupname=null;
		//groupname="test";
		String userid=null;
		//userid="40288e9f483f48e501483f48eb060000";
		String eid=null;
		//eid="40288e9f48625c010148625c07160000";
		String dcode=null;
		dcode="000100010001";
		List<PointGroupEntity> list=this.pointgroupservice.queryAllGroups(id, groupname, userid, eid, dcode);
		System.out.println("-----"+list.size()+"--"+list.toString());
	
		PointGroupEntity record=this.pointgroupservice.findByid(id);
		System.out.println("+++++++++++"+record.getId());
		
		PointGroupEntity e=new PointGroupEntity();
		e.setId("40288fb050f037170150f03730f50002");
		
		//List<PointStyleEntity> stylelist=this.pointstyleservice.findByGroupid(e);
		System.out.println("stylelist:"+stylelist.size());
		for(PointStyleEntity es:stylelist){
			System.out.println(es.getStylename());
		}
	}
	
	//@Test
	public void updatePointGroup() throws Exception{
		
		
		PointStyleEntity style=new PointStyleEntity();
		style.setStylename("sanfjkf999");
		//style.setId("40288fb050f035030150f0351b3f0001");
		//record.setStyleid(null);
		record.setCreatTime(new Date());
		record.setDcode("00010001");
		record.setGroupname("test-1");
		record.setEid("40288e9f48625c010148625c07160000");
		record.setUserid("40288e9f483f48e501483f48eb060000");
		record.setId("40288fb050cbfe060150cbfe23870000");
		this.pointstyleservice.addPointStyle(style);
		
		PointGroupEntity record=new PointGroupEntity();
		record=this.pointgroupservice.findByid("40288fb050f037170150f03730f50002");
		record.setStyleid(style);
		this.pointgroupservice.updateGroup(record);
		//this.pointstyleservice.addPointStyle(style);
		this.pointstyleservice.addPointStyle(style);
		PointEntity p=this.pointservice.queryById("40288fb050faf05d0150faf77e200008");
		p.setStyleid(style);
		this.pointservice.updatePoint(p);
		
		
		PointStyleEntity style=new PointStyleEntity();
		style.setStylename("style1---1");
		style.setUserid("40288e9f483f48e501483f48eb060000");
		style.setId("40288fb050f037170150f03730f20001");
		PointGroupEntity e=new PointGroupEntity();
		e.setId("222");
		//style.setGroupid(e);
		this.pointstyleservice.updatePointStyle(style);
	}
	
	//@Test
	public void deletePointGroup(){
		//this.pointgroupservice.deleteGroup("40288fb050cc0cf10150cc0d17640000");
		
		this.pointstyleservice.deletePointStyle("40288fb050f02a570150f02a73090001");
	}
	
	public static Object[] getJsonToArray(String str) {
        JSONArray jsonArray = JSONArray.fromObject(str);
        return jsonArray.toArray();
    }
    
    public static JSONObject getJsonObject(String str) {
   	 JSONObject jsonArray = JSONObject.fromObject(str);
        return jsonArray;
    }
    
    //@Test
    public void testPic(){
    	for(int i=0;i<5;i++){
    		PointPicEntity pic=new PointPicEntity();
        	pic.setPointid("111");
        	pic.setFilepath("filepath_"+i);
        	pic.setHeight(22);
        	pic.setWidth(33);
        	pic.setUploadtime(new Date());
        	//this.pointservice.savePointPicture(pic);
    	}
    	//this.pointservice.deletePointPictureByPicId("40288fcd5307cc27015307cc4cbf0000");
    	//this.pointservice.deletePointPictureByPointid("111");
    	List<PointPicEntity>  piclist=this.pointservice.findPointPicturesByPointid("111");
    	System.out.println(piclist.size());
    	
    	System.out.println(this.pointservice.getPointCountsByUserid("40288fd94add378d014add37cdfd0023"));
    	
    }
    
   // @Test
    public void testConfigCols(){
    	PointExtcolEntity record=new PointExtcolEntity();
    	record.setUserid("11111");
		record.setConfigcols("2,2,1");
		this.pointextcolservice.updatePointExtcol("col5", "", "ff80808153a625be0153a6460d5a0000");
    }
    
   // @Test
    public void testColvalSave(){
    	PointExtcolValEntity record=new PointExtcolValEntity();
    	PointEntity point=this.pointservice.queryById("40288fcd5381f961015388bc1d0f0003");
    	record.setUserid("8a04a77b4ee30e56014f00e022800216");
		record.setCol2(null);
		record.setCol1("33");
		record.setPointid("11");
		String id=this.pointextcolvalservice.save(record);
		System.out.println(id);
    }
    
    //@Test
    public void testCountQuery(){
    	List<String> x=new ArrayList<String>();
    	x.add("40288fcd5373c97c015373cbf96f0002");
    	x.add("8a04a77b4ee30e56014f00e022950217");
    	long l=this.pointDao.getCountsByAdminCodeAndDeptids(x,"5101%");
    	System.out.println(l);
    }
	
}
*/