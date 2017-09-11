package com.supermap.egispweb.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.mapper.JsonMapper;

import com.supermap.egispservice.base.entity.ComEntity;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.MenuVO;
import com.supermap.egispservice.base.entity.OrderEntity;
import com.supermap.egispservice.base.entity.OrderItemsEntity;
import com.supermap.egispservice.base.entity.RoleVO;
import com.supermap.egispservice.base.entity.SysDefaultCityEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.pojo.DeptUserZtree;
import com.supermap.egispservice.base.service.UserRoleMapService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.egispweb.httpclient.HttpClientDituhui;
import com.supermap.egispweb.util.Config;
import com.supermap.egispweb.util.FieldMap;
import com.supermap.egispweb.util.Md5Util;

import net.sf.json.JSONObject;

/**
 * 
 * @description 用户管理
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-9-12
 * @version 1.0
 */
@Controller
@RequestMapping("user")
public class UserAction {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleMapService userRoleMapService;
	
	@Autowired
	private com.supermap.egispweb.util.Config config;

	@Autowired
	private IGeocodingService geocodingService;
	
	private static final String CHARSET = ";charset=UTF-8";
	
	
	@RequestMapping("show")
	public String showIndex() {
		return "user";
	}

	@RequestMapping("info")
	public String showInfo() {
		return "info";
	}

	@RequestMapping("pass")
	public String showPass() {
		return "pass";
	}

	@RequestMapping(value="update/pass",produces=MediaType.APPLICATION_JSON_VALUE + CHARSET)
	@ResponseBody
	public Object updatePass(HttpSession session, String oldPass, String newPass,String callbacks) {
//		UserEntity user = (UserEntity) session.getAttribute("user");
//		boolean isOK = userService.updatePass(user, oldPass, newPass);
//		Map<String, Object> m = new HashMap<String, Object>();
//		if (isOK) {
//			user.setPassword(Md5Util.md5(newPass));
//			// user=userService.findUserById(user.getId());
//			m.put("flag", "ok");
//		} else {
//			m.put("flag", "fail");
//			m.put("info", "原密码错误!");
//		}
//		return m;
		
		Map<String, Object> m = new HashMap<String, Object>();
		// 对于非离线的保持原有逻辑
		if(!config.isDeployOffline()){
			UserEntity user = (UserEntity) session.getAttribute("user");
			// 如果原密码不为空，则要求提供旧密码
			if(!StringUtils.isEmpty(user.getPassword()) && StringUtils.isEmpty(oldPass)){
				m.put("flag", "fail");
				m.put("info", "旧密码不允许为空");
				return buildResult(m, callbacks);
			}
			// 新密码不允许为空
			if(StringUtils.isEmpty(newPass)){
				m.put("flag", "fail");
				m.put("info", "新密码不允许为空");
			}else{
				boolean isOK = userService.updatePass(user, oldPass, newPass);
				if (isOK) {
					user.setPassword(Md5Util.md5(newPass));
					m.put("flag", "ok");
				} else {
					m.put("flag", "fail");
					m.put("info", "原密码错误!");
				}
			}
			return buildResult(m, callbacks);
		}else{
			
			// 新密码不允许为空
			if(StringUtils.isEmpty(newPass)){
				m.put("flag", "fail");
				m.put("info", "新密码不允许为空");
			}else{
				UserEntity ue = null;
				Object userObj = session.getAttribute("user");
				// 正常修改密码,旧密码不允许为空
				if(null != userObj){
					if(StringUtils.isEmpty(oldPass)){
						m.put("flag", "fail");
						m.put("info", "旧密码不允许为空");
						return buildResult(m, callbacks);
					}
					ue = (UserEntity) userObj;
				}else{
					Object firstFlag =  session.getAttribute("username");
					if(null == firstFlag){
						m.put("flag", "fail");
						m.put("info", "未知用户信息");
						return buildResult(m, callbacks);
					}
					ue = this.userService.findByUsername((String)firstFlag, null);
				}
				LOGGER.info("## start update password ["+ue.getUsername()+","+ue.getEmail()+"]");
				boolean isOK = userService.updatePass(ue, oldPass, newPass);
				if (isOK) {
					ue.setPassword(Md5Util.md5(newPass));
					m.put("flag", "ok");
				} else {
					m.put("flag", "fail");
					m.put("info", "原密码错误!");
				}
			}
			return buildResult(m, callbacks);
		}
	}

	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> saveUser(@RequestParam(value = "username3") String username, String realname, String password, String dept, String address,
			@RequestParam(value = "mobilephone2") String mobilephone, String zipcode, String email, char sex, HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> m = new HashMap<String, Object>();
		username=email.trim();//将用户名设置为email使用email登陆
		if (userService.findByUsername(username, user.getEid().getId()) == null) {
			UserEntity u = new UserEntity();
			u.setUsername(username);
			u.setRealname(realname);
			u.setPassword(Md5Util.md5(password));
			InfoDeptEntity deptEntity = new InfoDeptEntity();
			deptEntity.setId(dept);
			u.setDeptId(deptEntity);
			u.setAddress(address);
			u.setMobilephone(mobilephone);
			u.setZipCode(zipcode);
			u.setEmail(email);
			ComEntity comEntity = new ComEntity();
			comEntity.setId(user.getEid().getId());
			u.setEid(comEntity);

			Date d = new Date(System.currentTimeMillis());
			u.setCreateTime(d);
			u.setCreateUser(user.getId());
			u.setSex(sex);
			u.setSourceId((byte) 3);// 自助式
			u.setStratusId((byte) 1);// 正常
			u.setUpdateTime(d);
			Map<String, Object> temp=userService.saveUser(u,password.trim());
			m=temp;
		} else {
			m.put("flag", "fail");
			m.put("info", "已存在相同的用户名!");
		}

		return m;
	}

	@RequestMapping("update")
	@ResponseBody
	public Map<String, Object> updateUser(String id, @RequestParam(value = "username3") String username, String realname, String dept, char sex,
			String address, @RequestParam(value = "mobilephone2") String mobilephone, String zipcode, String email) {
		UserEntity u = new UserEntity();
		u.setId(id);
		u.setUsername(username);
		u.setRealname(realname);
		InfoDeptEntity deptEntity = new InfoDeptEntity();
		deptEntity.setId(dept);
		u.setDeptId(deptEntity);
		u.setSex(sex);
		u.setAddress(address);
		u.setMobilephone(mobilephone);
		u.setZipCode(zipcode);
		u.setEmail(email);
		Date d = new Date(System.currentTimeMillis());
		u.setUpdateTime(d);
		userService.updateUser(u);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("flag", "ok");
		return m;
	}

	/**
	 * 修改个人资料
	 */
	@RequestMapping("update/info")
	@ResponseBody
	public Map<String, Object> updateUserInfo(String id,String qq, @RequestParam(value = "username3") String username, String realname, String dept, char sex,
			String address, @RequestParam(value = "mobilephone2") String mobilephone, String zipcode, String email, HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		UserEntity u = new UserEntity();
		u.setId(user.getId());
		u.setUsername(username);
		u.setRealname(realname);
		InfoDeptEntity deptEntity = new InfoDeptEntity();
		deptEntity.setId(dept);
		u.setDeptId(deptEntity);
		u.setSex(sex);
		u.setAddress(address);
		u.setMobilephone(mobilephone);
		u.setZipCode(zipcode);
		u.setEmail(email);
		Date d = new Date(System.currentTimeMillis());
		u.setUpdateTime(d);
		u.setQq(qq.trim());
		userService.updateUser(u);
		session.removeAttribute("user");
		UserEntity u2=userService.findUserById(u.getId());
		session.setAttribute("user", u2) ;
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("flag", "ok");
		return m;
	}

	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> deleteUser(String id) {
		userService.deleteUserById(id);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("flag", "ok");
		return m;
	}

	@RequestMapping("get")
	@ResponseBody
	public Map<String, Object> getUsers(@RequestParam(value = "search_username") String username,
			@RequestParam(value = "search_mobilephone") String mobilephone,
			@RequestParam(defaultValue = "2014-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date starttime,
			@RequestParam(defaultValue = "2114-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endtime, int page, int rows,
			HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> map = userService.getUsers(user.getId(), user.getEid().getId(), user.getDeptId().getId(), (byte) 3, username, mobilephone,
				starttime, endtime, page, rows, "auto");
		return map;
	}

	@RequestMapping("menu")
	@ResponseBody
	public List<MenuVO> getUserMenu(HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		List<MenuVO> menu = userService.getMenu(user.getId(), user.getEid().getId(), user.getDeptId().getId(), user.getSourceId());
		return menu;
	}

	@RequestMapping("role")
	@ResponseBody
	public List<RoleVO> getRoles(@RequestParam(value = "lineUserId") String lineUserId, HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		List<RoleVO> roleVOs = userService.getCheckedRole(user.getId(), lineUserId, user.getEid().getId(), user.getDeptId().getId());
		return roleVOs;
	}

	/**
	 * 授权
	 */
	@RequestMapping("authorize")
	@ResponseBody
	public Map<String, Object> authorize(String checkedRolesId, String userId) {
		userRoleMapService.authorize(checkedRolesId, userId);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("flag", "ok");
		return m;
	}

	private static final Logger LOGGER = Logger.getLogger(UserAction.class);
	@RequestMapping("validateComStatus")
	@ResponseBody
	public Map<String,Boolean> isEnterpriseAvaliable(@RequestParam(required=true)String eid){
		boolean isAvaliable = false;
		try {
			isAvaliable = this.userService.isComanyAvaliable(eid);
		} catch (Exception e) {
			LOGGER.error("## 校验公司状态信息发生异常:"+e.getMessage(), e);
		}
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		map.put("flag", isAvaliable);
		return map;
	}
	
	

	@RequestMapping("updateLoginStatus")
	@ResponseBody
	public Map<String,Object> updateOrderItemUpdateStatus(@RequestParam(required=true)String id){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean isUpdateSuccess = false;
		try {
			 isUpdateSuccess = this.userService.updateOrderItemLoginStatus(id);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		map.put("success", isUpdateSuccess);
		return map;
	}
	
	/**
	 * 登录接口，不需要走门户的验证
	 * @param nameOrEmail
	 * @param password
	 * @param session
	 * @param req
	 * @param callbacks
	 * @return
	 * @Author Juannyoh
	 * 2016-4-12下午2:17:57
	 */
	@RequestMapping(value="login",produces=MediaType.APPLICATION_JSON_VALUE + CHARSET)
	@ResponseBody
	public Object login(@RequestParam(required = true) String nameOrEmail,
			@RequestParam(required = true) String password,HttpSession session,HttpServletRequest req,String callbacks) {
		UserEntity userEntity = (UserEntity) session.getAttribute("user");
		Map<String,Object> map = new HashMap<String,Object>();
		
		// 如果用户未登录，则进行登录
		if(null == userEntity){
			map = new HashMap<String,Object>();
			
			// 根据用户名或邮箱登录
			userEntity = this.userService.findByUsername(nameOrEmail, null);
			// 判断用户是否存在
			if(null == userEntity){
				map.put("status", Config.LOGIN_USERINFO_ERROR);
				return buildResult(map, callbacks);
			}
			
			// 判断密码是否为空，如果为空提醒用户重置密码
			if(!StringUtils.isEmpty(userEntity.getPassword())){
				String tempPassword = Md5Util.md5(password);
				if(!tempPassword.equals(userEntity.getPassword())){
					map.put("status", Config.LOGIN_USERINFO_ERROR);
					return buildResult(map, callbacks);
				}
				session.removeAttribute("user");
				session.removeAttribute("uri");
				session.setAttribute("user",userEntity );
				session.setAttribute("isLogined",userService.isLogined(userEntity.getId()));
				session.setAttribute("uri",req.getRequestURI() );
				List<MenuVO> menu = userService.getMenu(userEntity.getId(), userEntity.getEid().getId(), userEntity.getDeptId().getId(), userEntity.getSourceId());
				boolean istry=true;
				if(menu.size()>0){
					MenuVO vo=menu.get(0);
					session.setAttribute("days",vo.getDays() );
					if(StringUtils.isEmpty(vo.getFuncName())){
						istry=false;
					}
				}
				session.setAttribute("istry",istry );
				map.put("status", Config.LOGIN_SUCCESS);
				map.put("key", userEntity.getId());
			}else{
				req.getSession().setAttribute("username", nameOrEmail);
				map.put("status", Config.LOGIN_RESET_PASS);
			}
			return buildResult(map, callbacks);
		}else{
			map.put("status", Config.LOGIN_LOGINED);
			map.put("key", userEntity.getId());
			return buildResult(map, callbacks);
		}
	}
	
	/**
	 * 离线
	 * @param nameOrEmail
	 * @param password
	 * @param session
	 * @param req
	 * @param callbacks
	 * @return
	 * @Author Juannyoh
	 * 2016-4-12下午2:15:37
	 */
	public Object login_bak(@RequestParam(required = true) String nameOrEmail,
			@RequestParam(required = true) String password,HttpSession session,HttpServletRequest req,String callbacks) {
		UserEntity userEntity = (UserEntity) session.getAttribute("user");
		Map<String,Object> map = new HashMap<String,Object>();
		
		// 如果用户未登录，则进行登录
		if(null == userEntity){
			map = new HashMap<String,Object>();
			boolean isFirst = this.userService.isOfflineFirstRun();
			// 如果为首次登录，则提示用户需要导入许可
			if(isFirst){
				map.put("status", Config.LOGIN_NEED_LIC);
				return buildResult(map, callbacks);
			}
			
			// 根据用户名或邮箱登录
			userEntity = this.userService.findByUsername(nameOrEmail, null);
			// 判断用户是否存在
			if(null == userEntity){
				map.put("status", Config.LOGIN_USERINFO_ERROR);
				return buildResult(map, callbacks);
			}
			
			// 判断密码是否为空，如果为空提醒用户重置密码
			if(!StringUtils.isEmpty(userEntity.getPassword())){
				
				String tempPassword = Md5Util.md5(password);
				if(!tempPassword.equals(userEntity.getPassword())){
					map.put("status", Config.LOGIN_USERINFO_ERROR);
					return buildResult(map, callbacks);
				}
				session.removeAttribute("user");
				session.removeAttribute("uri");
				session.setAttribute("user",userEntity );
				session.setAttribute("isLogined",userService.isLogined(userEntity.getId()));
				session.setAttribute("uri",req.getRequestURI() );
				List<MenuVO> menu = userService.getMenu(userEntity.getId(), userEntity.getEid().getId(), userEntity.getDeptId().getId(), userEntity.getSourceId());
				boolean istry=true;
				if(menu.size()>0){
					MenuVO vo=menu.get(0);
					session.setAttribute("days",vo.getDays() );
					if(StringUtils.isEmpty(vo.getFuncName())){
						istry=false;
					}
				}
				session.setAttribute("istry",istry );
				map.put("status", Config.LOGIN_SUCCESS);
			}else{
				req.getSession().setAttribute("username", nameOrEmail);
				map.put("status", Config.LOGIN_RESET_PASS);
			}
			return buildResult(map, callbacks);
		}else{
			map.put("status", Config.LOGIN_LOGINED);
			return buildResult(map, callbacks);
		}
	}
	
	
	@RequestMapping("initUserInfoAndOrder")
	public String initUserInfo(@RequestParam(required = true) String email,
			@RequestParam(required = true) String username, @RequestParam(required = true) String moduleId) {
		// 读取模板
		String tempatePath = config.getOrderTemplatePath() + File.separator + moduleId + ".template";
		LOGGER.info("## load order template[" + tempatePath + "]");
		File f = new File(tempatePath);
		if (!f.exists()) {
			throw new NullPointerException("模块ID有误，加载模板出错");
		}
		InputStream is;
		try {
			is = new FileInputStream(f);
			SAXReader reader = new SAXReader();
			Document document = reader.read(is);
			UserEntity ue = new UserEntity();
			List<OrderEntity> orders = new ArrayList<OrderEntity>();
			List<OrderItemsEntity> orderItems = new ArrayList<OrderItemsEntity>();
			// 解析模板信息
			parseTemplate(document, ue, orders, orderItems);
			for(OrderEntity oe : orders){
				oe.setMainModuleId(moduleId);
			}
			ue.setUsername(username);
			ue.setEmail(email);
			ue.getEid().setName(username);
			ue.getDeptId().setName(username);
			String  key = this.userService.initUserInfoOrOrders(ue, orders, orderItems,moduleId);
			if (!StringUtils.isEmpty(key)) {
				String redirectUrl = "redirect:/welcome/show?key=" + key;
				LOGGER.info("## init userinfoSuccess ["+redirectUrl+"]");
				return redirectUrl;
			} else {
				throw new NullPointerException("用户信息初始化失败");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new NullPointerException(e.getMessage());
		}
	}
	
	
	private void parseTemplate(Document document,UserEntity ue,List<OrderEntity> orders,List<OrderItemsEntity> orderItems) throws ParseException{
		Element rootElement = document.getRootElement();
		Element userinfoElement = rootElement.element("userinfo");
		// 解析用户信息
		parseUserInfo(userinfoElement,ue);
		// 解析订单信息
		Element ordersElement = rootElement.element("orders");
		parOrderInfoForLic(ue,ordersElement,orderItems,orders);
	}
	
	/**
	 * 
	 * <p>Title ：parOrderInfo</p>
	 * Description：		解析订单信息
	 * @param ue
	 * @param ordersElement
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-4-27 上午10:02:31
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	private void parOrderInfoForPub(UserEntity ue, Element ordersElement,List<OrderItemsEntity> orderItems,List<OrderEntity> orders) throws ParseException {
		/*List<Element> orderElements = ordersElement.elements("order");
		for (int i = 0; i < orderElements.size(); i++) {
			Element orderElement = orderElements.get(i);
			OrderEntity orderEntity = new OrderEntity();
			String orderId = orderElement.elementText("id");
			String order_type = orderElement.elementText("order_type");
			String status = orderElement.elementText("status");
			orderEntity.setId(orderId);
			orderEntity.setOrderType(Integer.parseInt(order_type));
			orderEntity.setSubmitTime(new Date());
			orderEntity.setStatusId(Byte.parseByte(status));
			orderEntity.setEid(ue.getEid().getId());
			orderEntity.setUserId(ue.getId());
			Element itemsElement = orderElement.element("items");
			// 解析订单项
			List<Element> itemElements = itemsElement.elements("item");
			for (int j = 0; j < itemElements.size(); j++) {
				Element itemElement = itemElements.get(j);
				String itemId = itemElement.elementText("id");
				String module_id = itemElement.elementText("module_id");
				String item_type = itemElement.elementText("item_type");
				String logined = itemElement.elementText("isLogined");
				OrderItemsEntity itemEntity = new OrderItemsEntity();
				itemEntity.setId(itemId);
				Calendar startCal = Calendar.getInstance();
				itemEntity.setUseTime(startCal.getTime());
				Calendar deadlineCal = Calendar.getInstance();
				deadlineCal.add(Calendar.DAY_OF_MONTH, config.getOrderTimeRange());
				itemEntity.setDeadline(deadlineCal.getTime());
				itemEntity.setIsLogined(logined);
				itemEntity.setItem_type(Integer.parseInt(item_type));
				itemEntity.setModuleId(module_id);
				itemEntity.setOrderId(orderId);
				orderItems.add(itemEntity);
			}
			orders.add(orderEntity);
		}*/
		parOrderInfoForLic(ue, ordersElement, orderItems, orders);
		for(OrderItemsEntity item : orderItems){
			Calendar deadlineCal = Calendar.getInstance();
			deadlineCal.add(Calendar.DAY_OF_MONTH, config.getOrderTimeRange());
			item.setDeadline(deadlineCal.getTime());
			Calendar startCal = Calendar.getInstance();
			item.setUseTime(startCal.getTime());
		}
	}
	
	private void parOrderInfoForLic(UserEntity ue, Element ordersElement,List<OrderItemsEntity> orderItems,List<OrderEntity> orders) throws ParseException {
		List<Element> orderElements = ordersElement.elements("order");
		for (int i = 0; i < orderElements.size(); i++) {
			Element orderElement = orderElements.get(i);
			OrderEntity orderEntity = new OrderEntity();
			String orderId = orderElement.elementText("id");
			String order_type = orderElement.elementText("order_type");
			String submitTime = orderElement.elementText("submit_time");
			String status = orderElement.elementText("status");
			orderEntity.setId(orderId);
			orderEntity.setOrderType(Integer.parseInt(order_type));
			orderEntity.setSubmitTime(format.parse(submitTime));
			orderEntity.setStatusId(Byte.parseByte(status));
			orderEntity.setEid(ue.getEid().getId());
			orderEntity.setUserId(ue.getId());
			Element itemsElement = orderElement.element("items");
			// 解析订单项
			List<Element> itemElements = itemsElement.elements("item");
			for (int j = 0; j < itemElements.size(); j++) {
				Element itemElement = itemElements.get(j);
				String itemId = itemElement.elementText("id");
				String module_id = itemElement.elementText("module_id");
				String use_time = itemElement.elementText("user_time");
				String deadline = itemElement.elementText("deadline");
				String item_type = itemElement.elementText("item_type");
				String logined = itemElement.elementText("isLogined");
				OrderItemsEntity itemEntity = new OrderItemsEntity();
				itemEntity.setId(itemId);
				itemEntity.setDeadline(format.parse(deadline));
				itemEntity.setIsLogined(logined);
				itemEntity.setItem_type(Integer.parseInt(item_type));
				itemEntity.setModuleId(module_id);
				itemEntity.setOrderId(orderId);
				itemEntity.setUseTime(format.parse(use_time));
				orderItems.add(itemEntity);
			}
			orders.add(orderEntity);
		}
	}
	

	/**
	 * 
	 * <p>Title ：parseUserInfo</p>
	 * Description：		解析用户信息
	 * @param userElement
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-4-24 下午04:39:43
	 * @throws ParseException 
	 */
	private void parseUserInfo(Element userElement,UserEntity userEntity) throws ParseException{
		String userId = userElement.elementText("id");
		String username = userElement.elementText("username");
		String realname = userElement.elementText("realname");
		String mobilephoneOfuserinfo = userElement.elementText("mobilephone");
		String emailOfuserinfo = userElement.elementText("email");
		String qqOfuserinfo = userElement.elementText("qq");
		byte statusId = Byte.valueOf(userElement.elementText("status"));
		userEntity.setId(userId);
		userEntity.setUsername(username);
		userEntity.setRealname(realname);
		userEntity.setMobilephone(mobilephoneOfuserinfo);
		userEntity.setEmail(emailOfuserinfo);
		userEntity.setQq(qqOfuserinfo);
		userEntity.setStratusId(statusId);
		userEntity.setSourceId(Byte.valueOf("4"));
		// 解析企业信息
		ComEntity comEntity = new ComEntity();
		Element comElement = userElement.element("enterprise");
		String comId = comElement.elementText("id");
		String comName = comElement.elementText("name");
		comEntity.setId(comId);
		comEntity.setName(comName);
		comEntity.setCreateTime(new Date());
		comEntity.setEmail(emailOfuserinfo);
		comEntity.setPhone(mobilephoneOfuserinfo);
		userEntity.setEid(comEntity);
		// 解析部门信息
		InfoDeptEntity deptEntity = new InfoDeptEntity();
		Element deptElement = userElement.element("department");
		String departId = deptElement.elementText("id");
		String headname = deptElement.elementText("headname");
		deptEntity.setOperDate(new Date());
		String name = deptElement.elementText("name");
		String deptcode = deptElement.elementText("code");
		String deptType = deptElement.elementText("type");
		String phone = deptElement.elementText("phone");
		deptEntity.setId(departId);
		deptEntity.setHeadName(headname);
		deptEntity.setName(name);
		deptEntity.setCode(deptcode);
		deptEntity.setType(Byte.parseByte(deptType));
		deptEntity.setCreateUserid(userEntity.getId());
		deptEntity.setPhone(phone);
		userEntity.setDeptId(deptEntity);
	}
	


	public Object buildResult(Object map,String callbacks){
		if(!StringUtils.isEmpty(callbacks)){
			JsonMapper jm = new JsonMapper();
			String result = jm.toJsonP(callbacks, map); 
			return result;
		}else{
			return map;
		}
	}
	
	
	/**
	 * 
	 * <p>Title ：uploadLicense</p>
	 * Description：		更新许可
	 * @param licenseFileName
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-4-24 下午04:01:00
	 */
	@RequestMapping(value="/uploadLicense",produces=MediaType.APPLICATION_JSON_VALUE + CHARSET)
	@ResponseBody
	public Object uploadLicense(MultipartFile licenseFile, String callbacks) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (!config.isDeployOffline()) {
				throw new Exception("在线不允许使用离线功能");
			}
			// 解析文件
			InputStream is = licenseFile.getInputStream();
			SAXReader reader = new SAXReader();
			Document document = reader.read(is);
			UserEntity ue = new UserEntity();
			List<OrderEntity> orders = new ArrayList<OrderEntity>();
			List<OrderItemsEntity> orderItems = new ArrayList<OrderItemsEntity>();
			parseLicense(document, ue, orders, orderItems);
			// 调用导入许可服务
			boolean success = this.userService.initUserInfo(ue, orders, orderItems);
			resultMap = buildResult("导入许可" + (success ? "成功" : "失败"), null, success);
		} catch (Exception e) {
			resultMap = buildResult(e.getMessage(), null, false);
		}
		if (!StringUtils.isEmpty(callbacks)) {
			JsonMapper jm = new JsonMapper();
			String result = jm.toJsonP(callbacks, resultMap);
			return result;
		} else {
			return resultMap;
		}
	}
	
	
	/**
	 * 
	 * <p>Title ：parseLicense</p>
	 * Description：		解析License
	 * @param document
	 * @param ue
	 * @param order
	 * Author：Huasong Huang
	 * CreateTime：2015-4-24 下午04:35:00
	 * @throws ParseException 
	 */
	private void parseLicense(Document document,UserEntity ue,List<OrderEntity> orders,List<OrderItemsEntity> orderItems) throws ParseException{
		Element rootElement = document.getRootElement();
		Element userinfoElement = rootElement.element("userinfo");
		// 解析用户信息
		parseUserInfo(userinfoElement,ue);
		// 解析订单信息
		Element ordersElement = rootElement.element("orders");
		parOrderInfoForLic(ue,ordersElement,orderItems,orders);
	}
	
	
	static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 * <p>Title ：buildResult</p>
	 * Description：	构建待JSONP格式的返回结果
	 * @param info
	 * @param result
	 * @param isSuccess
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-5-27 上午10:09:33
	 */
	private Map<String,Object> buildResult(String info,Object result,boolean isSuccess){
		Map<String,Object> resultObj = new HashMap<String,Object>();
		resultObj.put("info", info);
		resultObj.put("result", result);
		resultObj.put("isSuccess", isSuccess);
		return resultObj;
	}
	
	/**
	 * IP定位城市
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-7-28上午11:41:34
	 */
	@RequestMapping("getLocationByIP")
	@ResponseBody
	public Map<String,Object> getLocationByIP(HttpSession session){
		Map<String,Object> map = null;
		try {
			JSONObject json=new JSONObject();
			String url="http://api.map.baidu.com/location/ip?ak=214c94f370aa31822201489ae44e4018";
			json = readJsonFromUrl(url);
			if(null != json){
				map = buildResult(null, json.toString(), true);
			}else{
				map = buildResult("查询结果为空", null, false);
			}
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	 public static String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
	}
	 
	 public static JSONObject readJsonFromUrl(String url) throws Exception {
		    InputStream is = new URL(url).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = readAll(rd);
		      JSONObject json = JSONObject.fromObject(jsonText);;
		      return json;
		    } finally {
		      is.close();
		    }
	}
	 
	/**
	 * 保存用户默认城市设置
	 * @param admincode
	 * @param level
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-24下午1:55:31
	 */
	@RequestMapping("setUserDefaultCity")
	@ResponseBody
	public Map<String, Object> setUserDefaultCity(@RequestParam(required = true)String admincode
			,@RequestParam(required = true)String level,HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> map = null;
		try {
			int clevel=Integer.parseInt(level);
			switch (clevel) {
			case 1:
				admincode=admincode.substring(0, 2)+"0000";
				break;
			case 2:
				admincode=admincode.substring(0, 4)+"00";
				break;
			default:
				break;
			}
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId=user.getId();
			String eid=user.getEid().getId();
			String deptid=user.getDeptId().getId();
			SysDefaultCityEntity defaultcity=new SysDefaultCityEntity();
			defaultcity.setAdmincode(admincode);
			defaultcity.setClevel(level);
			defaultcity.setCreateTime(new Date());
			defaultcity.setUserid(userId);
			defaultcity.setEid(eid);
			defaultcity.setDeptid(deptid);
			
			defaultcity=this.userService.saveUserDefaultCity(defaultcity);//保存用户的默认城市
			if (null != defaultcity) {
				map = buildResult("保存默认城市成功", defaultcity, true);
			} else {
				map = buildResult("保存默认城市失败", null, false);
			}
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 获取用户的默认城市
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-11-24下午1:59:01
	 */
	@RequestMapping("getUserDefaultCity")
	@ResponseBody
	public Map<String, Object> getUserDefaultCity(HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		boolean islogined=(Boolean) session.getAttribute("isLogined");//是否登录过
		boolean templogined=(Boolean) session.getAttribute("tempLogined");//在默认城市上线后是否登录过
		Map<String, Object> map = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId=user.getId();
			String eid=user.getEid().getId();
			String deptid=user.getDeptId().getId();
			//是否顶级用户  总账号
			boolean isTop=(user.getSourceId()==1||user.getSourceId()==2)?true:false;
		
			SysDefaultCityEntity defaultcity=null;
			defaultcity=this.userService.findUserDefaultCity(userId);
			if (null != defaultcity&&defaultcity.getId()!=null) {
				String admincode=defaultcity.getAdmincode();
				if(admincode!=null&&admincode.equals("500000")){
					admincode="500100";
				}
				//Map<String,Object> namemap=(Map<String, Object>) this.geocodingService.getCountyByAdmincode(admincode);
				Map<String,Object> namemap=this.geocodingService.getAdminGeoByCode(admincode, Integer.parseInt(defaultcity.getClevel()));
				if(namemap!=null){
					double x=(Double) namemap.get("x");
					double y=(Double) namemap.get("y");
					BigDecimal bx=new BigDecimal(x);
					BigDecimal by=new BigDecimal(y);
					x=bx.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					y=by.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					defaultcity.setProvince(StringUtils.isEmpty(namemap.get("province"))?"":namemap.get("province").toString());
					defaultcity.setCity(StringUtils.isEmpty(namemap.get("city"))?"":namemap.get("city").toString());
					defaultcity.setCounty(StringUtils.isEmpty(namemap.get("county"))?"":namemap.get("county").toString());
					
					if(defaultcity.getClevel()!=null){
						if(defaultcity.getClevel().equals("1")){
							defaultcity.setDefaultname(defaultcity.getProvince());
						}else if(defaultcity.getClevel().equals("2")){
							defaultcity.setDefaultname(defaultcity.getCity());
						}else if(defaultcity.getClevel().equals("3")){
							defaultcity.setDefaultname(defaultcity.getCounty());
						}
					}
					FieldMap fieldmap=new FieldMap();
					Map<String,Object> citymap=fieldmap.convertBean(defaultcity);
					citymap.put("isTop", isTop);
					citymap.put("x", x);
					citymap.put("y", y);
					citymap.put("dcode", user.getDeptId().getCode());
					map = buildResult("获取默认城市成功", citymap, true);
				}
				
			} else {
				Map<String,Object> loginmap=new HashMap<String,Object>();
				loginmap.put("isLogined", islogined);//以前是否登录过
				loginmap.put("tempLogined", templogined);//默认城市上线后，是否登录过
				loginmap.put("isTop", isTop);
				loginmap.put("x", null);
				loginmap.put("y", null);
				loginmap.put("userid", userId);
				loginmap.put("eid", eid);
				loginmap.put("deptid", deptid);
				loginmap.put("dcode", user.getDeptId().getCode());
				map = buildResult("获取默认城市失败", loginmap, false);
			}
		} catch (Exception e) {
			Map<String,Object> loginmap=null;
			if (null != user) {
				loginmap=new HashMap<String,Object>();
				loginmap.put("userid", user.getId());
				loginmap.put("eid", user.getEid().getId());
				loginmap.put("deptid", user.getDeptId().getId());
				loginmap.put("dcode", user.getDeptId().getCode());
			}
			map = buildResult(e.getMessage(), loginmap, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 判断用户是否阅读了最新的更新日志
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-8-12上午11:29:39
	 */
	@RequestMapping("getIsUserReadLastLogs")
	@ResponseBody
	public Map<String, Object> IsUserReadLastLogs(HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> map = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId=user.getId();
			boolean result=this.userService.findIfUserReadLastLogs(userId);
			map = buildResult(null, result, true);
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	/**
	 * 用户点击进去之后，则保存用户阅读信息，表示已阅
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-8-12上午11:31:05
	 */
	@RequestMapping("saveUserReadInfo")
	@ResponseBody
	public Map<String, Object> saveSysUpdateLogUser(HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> map = null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userId=user.getId();
			this.userService.saveSysUpdateLogUser(userId);
			map = buildResult(null, null, true);
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	@RequestMapping("getDituhuiUserMap")
	@ResponseBody
	public void getDituhuiUserMap(String cuserid,HttpSession session,HttpServletResponse response) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			if(StringUtils.isEmpty(cuserid)){
				throw new Exception("大众版用户ID不能为空");
			}
			String usermapurl=config.getCdituhuiMapURL().replaceAll("#cuserid#", cuserid);
			HttpEntity entity = HttpClientDituhui.getDituhuiUserMap(usermapurl);
			if(entity!=null){
				InputStream is = entity.getContent();
		        OutputStream os = response.getOutputStream();
		        int length = 0;
		        byte[] buff = new byte[1024];
		        while ((length = is.read(buff)) > 0) {
		          os.write(buff, 0, length);
		        }
		        os.flush();
		        os.close();
		        is.close();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取用户子账号列表
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-10-20下午2:12:06
	 */
	@RequestMapping("getChildUsers")
	@ResponseBody
	public List<DeptUserZtree> getChildUsers(@RequestParam(value="parentId",required=false)String pid,HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		List<DeptUserZtree> deptuserlist= null;
		try {
			if (null == user) {
				throw new Exception("用户未登录");
			}
			deptuserlist=this.userService.getDeptUserTree(user.getEid().getId(),pid);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return deptuserlist;
	}
}
