package com.supermap.egispweb.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.pathplan.service.IPathPlanService;
import com.supermap.egispservice.pathplan.service.IRouteTaskService;

//@Controller
//@RequestMapping("path")
public class PathPlanAction {

	/**
	 * 传统模式
	 */
	private static final int COMMON_TYPE = 0;
	/**
	 * 推荐模式（每条路线最大承载订单量控制）
	 */
	private static final int SUGGEST_TYPE = 1;
	
	
	/**
	 * 每辆车设置最大承载量控制模式
	 */
	private static final int REQUIREMENT_TYPE=2;
	
	@Autowired
	private IPathPlanService pathPlanService;
	@Autowired
	private IRouteTaskService routeTaskService;

	@RequestMapping("show")
	public String show() {
		return "pathplan";
	}

	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@RequestParam(required = true) String taskName,
			@RequestParam(required = true) int weightNameType, @RequestParam(required = true) int directionType,
			@RequestParam(required = true) int groupType, String carsProperties,
			@RequestParam(required = true) String netcoord, @RequestParam(required = true) String ordersCoords,
			@RequestParam(required = true) String areaName, @RequestParam(required = true) String areaId,
			@RequestParam(required = true) String ordersIds, @RequestParam(required = true) int ordersCount,
			@RequestParam(required = true) String netid, 
			@RequestParam(required = true) int type, String orderTimes,
			int carLoad, @RequestParam(value = "fixConsumeMin") double fixedConsumeMiniute,
			@RequestParam(required = true) String batchTimeStart,
			@RequestParam(required = true) String batchTimeEnd,
			HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");

		// 强制使用推荐模式
		type = SUGGEST_TYPE;
		Map<String, Object> m = null;
		try{
			switch (type) {
			case COMMON_TYPE:
				if(StringUtils.isEmpty(carsProperties)){
					throw new NullPointerException("参数carsProperties不允许为空");
				}
				m = routeTaskService.saveJobAndRun(carsProperties, netcoord, ordersCoords, areaName, areaId, ordersIds,
						ordersCount, netid, null, null, null, user, taskName, weightNameType,
						directionType, groupType);
				break;
			case SUGGEST_TYPE:
				if(StringUtils.isEmpty(orderTimes)){
					throw new NullPointerException("参数orderTimes不允许为空");
				}
				if(carLoad <= 0){
					throw new NullPointerException("参数carLoad不允许为空,或小于1");
				}
				if(fixedConsumeMiniute < 0d){
					throw new NullPointerException("参数fixConsumeMin不允许为空,或小于0");
				}
				m = routeTaskService.saveJobAndRun(netid, netcoord, ordersIds, ordersCoords, orderTimes, areaId,
						areaName, user, taskName, carLoad, fixedConsumeMiniute, weightNameType, groupType,
						directionType, batchTimeStart, batchTimeEnd);
				break;
			default:
				m = new HashMap<String,Object>();
				throw new NullPointerException("未能识别的分析类型");
			}
		}catch(Exception e){
			m = new HashMap<String,Object>();
			m.put("flag", "error");
			m.put("errorInfo", e.getMessage());
		}
		return m;
	}
	

	@RequestMapping("get")
	@ResponseBody
	public Map<String, Object> query(String taskId, Byte taskStatusId, String areaName,@RequestParam(defaultValue="1") int page,@RequestParam(defaultValue="10") int rows, HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		if(page < 1 ){
			page = 1;
		}
		if(rows < 1 || rows > 100){
			rows = 10;
		}
		Map<String, Object> map = routeTaskService.getTasks(user.getId(), user.getEid().getId(), user.getDeptId().getId(), taskId, taskStatusId, areaName,
				page, rows, "auto");
		return map;
	}

	/**
	 * 
	 * @param points
	 * @return Map<String, Object> length：int:线路长度，detail：String：中文道路信息，flag：ok,error;info:描述信息
	 * @throws Exception
	 */
	@RequestMapping("length")
	@ResponseBody
	public Map<String, Object> getLength(String points) throws Exception {

		if (!"".equals(points) && null != points && points.length() > 0) {
			Map<String, Object> m = new HashMap<String, Object>();
			if (points.indexOf(",") > 0 && !points.endsWith(",")) {
				String[] pointsStringArray = points.split(",");
				List<Point> pointsList = new ArrayList<Point>();
				for (int i = 0; i < pointsStringArray.length; i = i + 2) {
					pointsList.add(new Point(Double.valueOf(pointsStringArray[i]), Double.valueOf(pointsStringArray[i + 1])));
				}
				boolean withDetail = false;
				m = pathPlanService.getLength(pointsList, withDetail);
				return m;
			} else {
				m.put("length", 0);
				m.put("flag", "error");
				m.put("info", "传入参数数据错误");
				return m;
			}
		}
		return null;
	}


	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(String id) throws Exception {
		boolean flag=routeTaskService.deleteById(id);
		Map<String, Object> m = new HashMap<String, Object>();
		if(flag){
			m.put("flag", "ok");
		}else{
			m.put("flag", "error");
		}
		return m;
	}

	@RequestMapping("result")
	@ResponseBody
	public Map<String, Object> showResult(String id,String areaId,HttpSession session) throws Exception {
		UserEntity user = (UserEntity) session.getAttribute("user");
		return routeTaskService.getResult(id,areaId,user.getDeptId().getCode());
	}
	
	

	
	@RequestMapping(value="saveV2",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveV2(@RequestParam(required = true) String taskName,
			@RequestParam(required = true) int weightNameType, @RequestParam(required = true) int directionType,
			@RequestParam(required = true) int groupType, String carsProperties,
			@RequestParam(required = true) String netcoord, @RequestParam(required = true) String ordersCoords,
			@RequestParam(required = true) String areaName, @RequestParam(required = true) String areaId,
			@RequestParam(required = true) String ordersIds, @RequestParam(required = true) int ordersCount,
			@RequestParam(required = true) String netid, 
			@RequestParam(required = true) int type, String orderTimes,
			int carLoad, @RequestParam(value = "fixConsumeMin") double fixedConsumeMiniute,
			@RequestParam(required = true) String batchTimeStart,
			@RequestParam(required = true) String batchTimeEnd,
			String startAddress,//起点地址（常规网点或者是poi地址）
			long MaxCarCarry, //每辆车最大承载量
			String poiAddresses,String poiCoords,//POI地址及坐标
			String excelAddresses,String excelCoords,String excelTimes, long requirements,//excel导入的数据
			HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");

		// 默认强制使用推荐模式
		type = SUGGEST_TYPE;
		
		if(MaxCarCarry>0){//每辆车最大承载量模式
			type=REQUIREMENT_TYPE;
		}
		
		Map<String, Object> m = null;
		try{
			switch (type) {
			case COMMON_TYPE:
				if(StringUtils.isEmpty(carsProperties)){
					throw new NullPointerException("参数carsProperties不允许为空");
				}
				m = routeTaskService.saveJobAndRun(carsProperties, netcoord, ordersCoords, areaName, areaId, ordersIds,
						ordersCount, netid, null, null, null, user, taskName, weightNameType,
						directionType, groupType);
				break;
			case SUGGEST_TYPE:
				if(StringUtils.isEmpty(orderTimes)){
					throw new NullPointerException("参数orderTimes不允许为空");
				}
				if(carLoad <= 0){
					throw new NullPointerException("参数carLoad不允许为空,或小于1");
				}
				if(fixedConsumeMiniute < 0d){
					throw new NullPointerException("参数fixConsumeMin不允许为空,或小于0");
				}
				m = routeTaskService.saveJobAndRun(netid, netcoord, ordersIds, ordersCoords, orderTimes, areaId,
						areaName, user, taskName, carLoad, fixedConsumeMiniute, weightNameType, groupType,
						directionType, batchTimeStart, batchTimeEnd);
				break;
			case REQUIREMENT_TYPE:
				if(requirements<=0){
					throw new NullPointerException("参数requirements不允许为空,或小于1");
				}
				m = routeTaskService.saveJobAndRun(netid, netcoord, ordersIds, ordersCoords, orderTimes, areaId,
						areaName, user, taskName, carLoad, fixedConsumeMiniute, weightNameType, groupType,
						directionType, batchTimeStart, batchTimeEnd);
				break;
			default:
				m = new HashMap<String,Object>();
				throw new NullPointerException("未能识别的分析类型");
			}
		}catch(Exception e){
			m = new HashMap<String,Object>();
			m.put("flag", "error");
			m.put("errorInfo", e.getMessage());
		}
		return m;
	}
	
	
	

	@Deprecated
	@RequestMapping("orders")
	@ResponseBody
	public Pager getOrders() {
		return getTestOrders();
	}

	@Deprecated
	@RequestMapping("nets")
	@ResponseBody
	public Pager getNets() {
		return getTestNet();
	}

	/**
	 * 获取测试订单数据
	 * 
	 * @return
	 */
	@Deprecated
	private Pager getTestOrders() {
		OrderPoint p1 = new OrderPoint("40288e9f492c6e3b01492c6e95c20011", "20141029", "测试地址1", 121.228479, 31.108227);
		OrderPoint p2 = new OrderPoint("40288e9f492c6e3b01492c6e95c20012", "20141029", "测试地址2", 121.230899, 31.108528);
		OrderPoint p3 = new OrderPoint("40288e9f492c6e3b01492c6e95c20013", "20141029", "测试地址3", 121.235373, 31.111953);
		OrderPoint p4 = new OrderPoint("40288e9f492c6e3b01492c6e95c20014", "20141029", "测试地址4", 121.235898, 31.109476);
		OrderPoint p5 = new OrderPoint("40288e9f492c6e3b01492c6e95c20015", "20141029", "测试地址5", 121.236148, 31.108343);

		List<Object> l = new ArrayList<Object>();
		l.add(p1);
		l.add(p2);
		l.add(p3);
		l.add(p4);
		l.add(p5);

		Pager p = new Pager(1, 1, 5, l);
		return p;
	}

	@Deprecated
	class OrderPoint {
		private final String id;
		private final String lot;
		private final String addr;
		private final double lon;
		private final double lat;

		public OrderPoint(String id, String lot, String addr, double lon, double lat) {
			super();
			this.id = id;
			this.lot = lot;
			this.addr = addr;
			this.lon = lon;
			this.lat = lat;
		}

		public String getId() {
			return id;
		}

		public String getLot() {
			return lot;
		}

		public String getAddr() {
			return addr;
		}

		public double getLon() {
			return lon;
		}

		public double getLat() {
			return lat;
		}

	}

	@Deprecated
	class Pager {
		private final int total;
		private final int page;
		private final int records;
		private final List<Object> rows;

		public Pager(int total, int page, int records, List<Object> rows) {
			super();
			this.total = total;
			this.page = page;
			this.records = records;
			this.rows = rows;
		}

		public int getTotal() {
			return total;
		}

		public int getPage() {
			return page;
		}

		public int getRecords() {
			return records;
		}

		public List<Object> getRows() {
			return rows;
		}

	}

	/**
	 * 获取测试网点数据
	 * 121.227782, 31.112766
	 * @return
	 */
	@Deprecated
	private Pager getTestNet() {
		NetPoint p = new NetPoint();
		p.setId("40288e9f4997974b014997a055d60000");
		p.setAreaid("areaid1");
		p.setNetAddress("网点地址");
		p.setNetImg("");
		p.setNetName("测试网点名称");
		p.setPersonImg("");
		p.setPersonName("张三");
		p.setPhone("13800138000");
		p.setLon(116.58644078423676);
		p.setLat(39.83206906700096);
		List<Object> l = new ArrayList<Object>();
		l.add(p);

		Pager page = new Pager(1, 1, 1, l);
		return page;
	}

	@Deprecated
	class NetPoint {
		private String id;
		private String netName;
		private String personName;
		private String phone;
		private String netAddress;
		private String netImg;
		private String personImg;
		private String areaid;
		private  double lon;
		private  double lat;

		public NetPoint() {
			super();
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNetName() {
			return netName;
		}

		public void setNetName(String netName) {
			this.netName = netName;
		}

		public String getPersonName() {
			return personName;
		}

		public void setPersonName(String personName) {
			this.personName = personName;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public double getLon() {
			return lon;
		}

		public double getLat() {
			return lat;
		}

		public String getNetAddress() {
			return netAddress;
		}

		public void setNetAddress(String netAddress) {
			this.netAddress = netAddress;
		}

		public String getNetImg() {
			return netImg;
		}

		public void setNetImg(String netImg) {
			this.netImg = netImg;
		}

		public String getPersonImg() {
			return personImg;
		}

		public void setPersonImg(String personImg) {
			this.personImg = personImg;
		}

		public String getAreaid() {
			return areaid;
		}

		public void setAreaid(String areaid) {
			this.areaid = areaid;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

	}
}
