package com.supermap.egispservice.base.service.impl;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.supermap.egispservice.base.constants.Config;
import com.supermap.egispservice.base.constants.EbossStatusConstants;
import com.supermap.egispservice.base.constants.PortalConstants;
import com.supermap.egispservice.base.constants.StatusConstants;
import com.supermap.egispservice.base.dao.AppkeyDao;
import com.supermap.egispservice.base.dao.ComDao;
import com.supermap.egispservice.base.dao.ComStatusDao;
import com.supermap.egispservice.base.dao.ISysUpdateLogUserDao;
import com.supermap.egispservice.base.dao.InfoDeptDao;
import com.supermap.egispservice.base.dao.OrderDao;
import com.supermap.egispservice.base.dao.OrderItemsDao;
import com.supermap.egispservice.base.dao.SysDefaultCityDao;
import com.supermap.egispservice.base.dao.UserDao;
import com.supermap.egispservice.base.dao.UserSourceDao;
import com.supermap.egispservice.base.dao.UserStatusDao;
import com.supermap.egispservice.base.entity.AppkeyEntity;
import com.supermap.egispservice.base.entity.ComEntity;
import com.supermap.egispservice.base.entity.ComStatusEntity;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.InfoRoleEntity;
import com.supermap.egispservice.base.entity.MenuVO;
import com.supermap.egispservice.base.entity.OrderEntity;
import com.supermap.egispservice.base.entity.OrderItemsEntity;
import com.supermap.egispservice.base.entity.OrderStatusEntity;
import com.supermap.egispservice.base.entity.RoleVO;
import com.supermap.egispservice.base.entity.SysDefaultCityEntity;
import com.supermap.egispservice.base.entity.SysUpdateLogEntity;
import com.supermap.egispservice.base.entity.SysUpdateLogUserEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.entity.UserRoleMapEntity;
import com.supermap.egispservice.base.entity.UserSourceEntity;
import com.supermap.egispservice.base.entity.UserStatusEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseAccessInfo;
import com.supermap.egispservice.base.pojo.BaseUserInfo;
import com.supermap.egispservice.base.pojo.BaseUserListInfo;
import com.supermap.egispservice.base.pojo.BaseUserListPkg;
import com.supermap.egispservice.base.pojo.DeptUserZtree;
import com.supermap.egispservice.base.pojo.UserInfoFieldNames;
import com.supermap.egispservice.base.service.ISysUpdateLogService;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.base.service.InfoRoleService;
import com.supermap.egispservice.base.service.UserRoleMapService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.base.util.BeanTool;
import com.supermap.egispservice.base.util.CommonUtil;
import com.supermap.egispservice.base.util.CommonUtil.DateType;
import com.supermap.egispservice.base.util.EncryptionUtil;
import com.supermap.egispservice.base.util.Md5Util;
import com.supermap.egispservice.base.util.PortalTrustManager;


/**
 * 用户业务服务实现类
 */
@Transactional
@Service("userService")
public class UserServiceImpl implements UserService{
    
	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private InfoRoleService infoRoleService;
    @Autowired
    private UserRoleMapService userRoleMapService;
    @Autowired
    private InfoDeptService infoDeptService;
    @Autowired
    private ComDao comDao;
    @Autowired
    private Config config;
    
    @Autowired
    private InfoDeptDao infoDeptDao;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private OrderItemsDao orderItemDao;
    
    @Autowired
	private SysDefaultCityDao sysDefaultCityDao;
    
    
    @Resource 
    private ISysUpdateLogUserDao sysUpdateLogUserDao;
	
	@Resource
	private ISysUpdateLogService sysUpdateLogService;
	
	@Resource
	private AppkeyDao appkeyDao;
	
	@Resource
	private UserStatusDao userStatusDao;
	
	@Resource
	private ComStatusDao comStatusDao;
	
	@Resource
	private UserSourceDao userSourceDao;

    @Override
    @Transactional
    public Map<String, Object> saveUser(UserEntity user,String password) {
    	Map<String, Object> m=new HashMap<String, Object>();
    	UserEntity tempExist=null;
    	//匹配验证用户名
    	UserEntity u=userDao.findByUsername(user.getUsername().trim());
    	if(u!=null){
    		LOGGER.info("已存在用户名");
    		m.put("flag", "fail");
	    	m.put("info", "注册失败！已存在用户名");
	    	return m;
    	}
    	//匹配验证邮箱
    	UserEntity u2=userDao.findByEmail(user.getEmail().trim());
    	if(u2!=null){
    		if(u2.getSourceId()==3){//用户管理添加的用户
    			LOGGER.info("已存在邮箱名");
        		m.put("flag", "fail");
    	    	m.put("info", "注册失败！已存在邮箱名");
    	    	return m;
    		}
    		if(u2.getSourceId()==1||u2.getSourceId()==2){//通过注册的用户
    			List<OrderEntity> orders=orderDao.queryByUserid(u2.getId());//通过用户id查询订单
        		if(orders!=null&&orders.size()>0){
    	    		LOGGER.info("已存在邮箱名,且有订单");
    	    		m.put("flag", "fail");
    		    	m.put("info", "注册失败！已存在邮箱名，且有订单");
    		    	return m;
        		}else{
        			tempExist=u2;
        		}
    		}
    	}
    	// 如果不是离线部署，则不需要向iportal进行账号注册
    	if(!config.isDeployOffline()){
    		JSONObject obj=remoteRegist(user.getEmail(), password);
    		// 判断远程注册接口调用是否成功
    		if(!obj.getBoolean("status")){
    			String registInfo=obj.getString("info");
    			LOGGER.info(registInfo);
    			if(registInfo.contains("已注册")){
    				if(tempExist!=null){
    					user.setPassword(tempExist.getPassword());
    					user.setId(tempExist.getId());
    				}
    			}else{
    				m.put("flag", "fail");
        			m.put("info", "注册失败！"+registInfo);
        			return m;
    			}
    		}
    	}
    	userDao.save(user);
    	m.put("flag", "ok");
    	m.put("info", "操作成功");
    	return m;
    	
    }

    @Override
    @Transactional(readOnly=true)
    public UserEntity findUserById(String id) {
        return userDao.findById(id);
    }
    
	@Override
	@Transactional
    public void updateUser(UserEntity user) {
		UserEntity u=findUserById(user.getId());
		BeanUtils.copyProperties(user, u,BeanTool.getNullPropertyNames(user));
    	userDao.save(u);
    }
	
	@Override
	@Transactional
	public boolean updatePass(UserEntity user, String oldPass,String newPass) {
		String tempPass = user.getPassword();
		if (!StringUtils.isEmpty(tempPass) && !Md5Util.md5(oldPass).equals(tempPass)) {
			return false;
		}
		UserEntity u = findUserById(user.getId());
		u.setPassword(Md5Util.md5(newPass));
		userDao.save(u);
		return true;
	}

    @Override
    @Transactional
    public void deleteUserById(String id) {
    	if(id.indexOf(",")>0){
    		List<String> idList=new ArrayList<String>();
    		String [] ids=id.split(",");
    		for (String idString : ids) {
    			idList.add(idString);
			}
    		userDao.deleteByIds(idList);
    	}else{
    		userDao.delete(id);
    	}
    	
    }
    /**
     * 分页查询用户信息
     */
    @Override
    public Map<String, Object> getUsers(final String userid,final String eid,final String deptId,final byte sourceId,final String realname,final String mobilephone,final Date startTime,final Date endTime ,int pageNumber, int pageSize,String sortType) {
    	PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
    	Specification<UserEntity> spec = new Specification<UserEntity>() {
    		@Override
    		public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    			List<Predicate> predicateList=new ArrayList<Predicate>();
    			Path<String> userid2=root.get("id");
    			Path<String> realname2=root.get("realname");
    			Path<String> mobilephone2=root.get("mobilephone");
    			Path<Byte> sourceId2=root.get("sourceId");
    			Path<String> eid2=root.get("eid").get("id");
    			Path<String> deptId2=root.get("deptId").get("id");
    			Path<Date> createTime=root.<Date>get("createTime");
    			if (StringUtils.isNoneEmpty(realname)) {
    				Predicate p=builder.like(realname2, "%"+realname+"%");
    				predicateList.add(p);
    			}
    			if (StringUtils.isNoneEmpty(mobilephone)) {
    				Predicate p=builder.like(mobilephone2, "%"+mobilephone+"%");
    				predicateList.add(p);
    			}
    			Predicate idPredicate=builder.notEqual(userid2, userid);
    			predicateList.add(idPredicate);
    			Predicate eidPredicate=builder.equal(eid2, eid);
    			predicateList.add(eidPredicate);
    			
    			List<String> depts=new ArrayList<String>();
    			List<InfoDeptEntity> infoDeptEntities= infoDeptService.getChildDepts(deptId);
    			for (InfoDeptEntity infoDeptEntity : infoDeptEntities) {
    				depts.add(infoDeptEntity.getId());
    			}
    			Predicate deptIdPredicate=deptId2.in(depts);
    			predicateList.add(deptIdPredicate);
    			Predicate sourceIdPredicate=builder.equal(sourceId2, sourceId);
    			predicateList.add(sourceIdPredicate);
    			
    			Predicate p=builder.between(createTime, startTime,endTime);
    			predicateList.add(p);
    			Predicate [] predicates=new Predicate[predicateList.size()];
    			predicateList.toArray(predicates);
    			query.where(predicates);
    			return null;
    		}
    		
    	};
    	Page<UserEntity> page=userDao.findAll(spec, pageRequest);
    	Map<String, Object> m=new HashMap<String, Object>();
    	m.put("total", page.getTotalPages());
    	m.put("page", pageNumber);
    	m.put("records", page.getTotalElements());
    	m.put("rows", page.getContent());
    	return m;
    }
    /**
     * 查找部门下所有用户
     */
    @Override
	public List<UserEntity> getUsersByDept(final String deptId) {
    	final List<String> depts=new ArrayList<String>();
		List<InfoDeptEntity> infoDeptEntities= infoDeptService.getChildDepts(deptId);
		for (InfoDeptEntity infoDeptEntity : infoDeptEntities) {
			/*if(infoDeptEntity.getId().equals(deptId)){
				continue;
			}*/
			depts.add(infoDeptEntity.getId());
		}
		if(depts.size()==0){
			return null;
		}
		
		Specification<UserEntity> spec = new Specification<UserEntity>() {
			@Override
			public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				Path<String> deptId2=root.get("deptId").get("id");
				
				
				Predicate deptIdPredicate=deptId2.in(depts);
				predicateList.add(deptIdPredicate);
				
				Predicate [] predicates=new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				query.where(predicates);
				return null;
			}
			
		};
		return userDao.findAll(spec);
	}
    
    /**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} 

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<UserEntity> buildSpecification( Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<UserEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), UserEntity.class);
		return spec;
	}
	
	
	

	@Override
	public List<MenuVO> getMenu(String userid,String eid,String deptId,Byte sourceId) {
		List<MenuVO> l=new ArrayList<MenuVO>();
		
		List<Object[]> o=new ArrayList<Object[]>();
		if(sourceId==3){//not manager
			List<InfoRoleEntity> lisInfoRoleEntities=infoRoleService.findRolesByEidAndUserid(userid, eid);
			if(lisInfoRoleEntities.size()==0){
				return l;
			}
			List<String> roleIds=new ArrayList<String>();
			for (int i = 0; i < lisInfoRoleEntities.size(); i++) {
				roleIds.add(lisInfoRoleEntities.get(i).getId());
			}
			o=userDao.getMenu(eid, new DateTime().toString("yyyy-MM-dd HH:mm:ss"),roleIds);
		}else{
			o=userDao.getMenuForEnterpriseManager(eid, new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
		}
				
				
		for (Object[] objects : o) {
			MenuVO vo=new MenuVO();
			vo.setId((String)objects[0]);
			vo.setMenuName((String)objects[1]);
			vo.setPid((String)objects[2]);
			vo.setIconUrl((String)objects[3]);
			vo.setUrl((String)objects[4]);
			vo.setStatusId((Byte)objects[5]); 
			vo.setUseTime((new DateTime(((Timestamp)objects[6]).getTime())).toString()); 
			vo.setDeadLine((new DateTime(((Timestamp)objects[7]).getTime())).toString()); 
			vo.setFuncName((String)objects[8]); 
			vo.setDays(((BigInteger)objects[9]).toString()); 
			vo.setIsLogined((String)objects[10]);
			vo.setOrderItemId((String)objects[11]);
			l.add(vo);
		}
		return l;
	}

	@Override
	public List<RoleVO> getCheckedRole(String currentUserId ,String lineUserId,String eid,String deptId) {
		List<InfoRoleEntity> lisInfoRoleEntities=infoRoleService.findByEidAndDepts(currentUserId,eid,deptId);
		List<UserRoleMapEntity> lisRoleMapEntities=userRoleMapService.findByUserId(lineUserId);
		List<RoleVO> lisRoleVOs=new ArrayList<RoleVO>();
		for (InfoRoleEntity infoRoleEntity : lisInfoRoleEntities) {
			RoleVO roleVO=new RoleVO();
			roleVO.setId(infoRoleEntity.getId());
			roleVO.setName(infoRoleEntity.getName());
			for (UserRoleMapEntity userRoleMapEntity : lisRoleMapEntities) {
				if (infoRoleEntity.getId().equals(userRoleMapEntity.getRoleId() )) {
					roleVO.setChecked(true);
					break;
				}
			}
			lisRoleVOs.add(roleVO);
		}
    	return lisRoleVOs;
    }

	/**
	 * 根据用户名获取用户实体，因为infodeptentity有oneTOone的自连接，直接用username查有问题，转换为先查出ID，再用ID查对象
	 */
	@Override
	public UserEntity findByUsername(String username,String eid) {
		List<String> result= null;
		if(!StringUtils.isEmpty(eid)){
			result = userDao.findByUsername(username,eid); 
			if (null != result && result.size()>0) {
				String id=result.get(0);
				return findUserById(id);
			}
		}else{
			List<UserEntity> users  = userDao.findByNameOrEmail(username);
			if(null != users && users.size() > 0){
				return users.get(0);
			}
		}
		return null;
	}

	@Autowired
	private ComStatusDao statusDao;
	
	@Override
	public boolean isComanyAvaliable(String eid) {
		ComEntity comEntity = this.comDao.findOne(eid);
		boolean isAvaliable = false;
		if(null != comEntity){
			Byte comStatusId = comEntity.getStatusId();
			ComStatusEntity statusEntity = statusDao.findOne(comStatusId);
			if(statusEntity.getValue().equals(StatusConstants.COM_STATUS_COMMON)){
				isAvaliable = true;
			}
		}
		return isAvaliable;
	}
	
	/**
	 * 
	 * <p>Title ：remoteRegist</p>
	 * Description：		调用远程接口实现注册
	 * @param email
	 * @param password
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-25 上午09:20:00
	 */
	/*private JSONObject remoteRegist(String email,String password){
		boolean isSuccess = false;
		JSONObject resultObj = new JSONObject();
		
		try {
			URL url = new URL(config.getRegistUrl());
			HttpURLConnection conn = null;
			// 初始化SSLContext
			SSLContext context = SSLContext.getInstance("TLS");//TLS比SSL更高级
			context.init(new KeyManager[]{}, new TrustManager[]{new PortalTrustManager()}, new SecureRandom());
			SSLContext.setDefault(context);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// 等待注册的超时时间
			conn.setReadTimeout(3000);
			StringBuilder paramBuilder = new StringBuilder();
			paramBuilder.append("username=").append(email).append("&password=").append(password);
			OutputStream os = conn.getOutputStream();
			os.write(paramBuilder.toString().getBytes());
			os.flush();
			
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String buffer = null;
			StringBuilder resultBuilder = new StringBuilder();
			while((buffer = br.readLine()) != null){
				resultBuilder.append(new String(buffer));
			}
			String result = resultBuilder.toString();
			LOGGER.info("## 远程注册结果: " + result);
			if(!StringUtils.isEmpty(result)){
				JSONObject obj = JSONObject.fromObject(result);
				 if(obj.containsKey("Status")){
					 String status = obj.getString("Status");
					 if(!StringUtils.isEmpty(status)){
						 if(status.equalsIgnoreCase("success")){
							 resultObj.accumulate("status", true);
						 }else{
							 resultObj.accumulate("status", false);
							 resultObj.accumulate("info", obj.getString("Message"));
						 }
					 }else{
						 throw new Exception("注册发生异常，Status为空");
					 }
				 }else{
					 throw new Exception("注册发生异常，Status不存在");
				 }
			}else{
				throw new Exception("注册发生异常，远程注册接口返回为空");
			}
			os.close();
			br.close();
			conn.disconnect();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultObj.accumulate("status", false);
			resultObj.accumulate("info", e.getMessage());
		}
		return resultObj;

	}*/
	private JSONObject remoteRegist(String email,String password){
		boolean isSuccess = false;
		JSONObject resultObj = new JSONObject();
		
		try {
			URL url = new URL(config.getRegistUrl());
			//URL url = new URL("http://www.dituhui.com/member/UserRegister");
			HttpURLConnection conn = null;
			// 初始化SSLContext
			SSLContext context = SSLContext.getInstance("TLS");//TLS比SSL更高级
			context.init(new KeyManager[]{}, new TrustManager[]{new PortalTrustManager()}, new SecureRandom());
			SSLContext.setDefault(context);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// 等待注册的超时时间
			conn.setReadTimeout(3000);
			StringBuilder paramBuilder = new StringBuilder();
			paramBuilder.append("username=").append(email).append("&password=").append(password).append("&nickname=");
			OutputStream os = conn.getOutputStream();
			os.write(paramBuilder.toString().getBytes());
			os.flush();
			
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String buffer = null;
			StringBuilder resultBuilder = new StringBuilder();
			while((buffer = br.readLine()) != null){
				resultBuilder.append(new String(buffer));
			}
			String result = resultBuilder.toString();
			LOGGER.info("## 远程注册结果: " + result);
			if(!StringUtils.isEmpty(result)){
				JSONObject obj = JSONObject.fromObject(result);
				 if(obj.containsKey("Status")){
					 String status = obj.getString("Status");
					 if(!StringUtils.isEmpty(status)){
						 if(status.equalsIgnoreCase("success")){
							 resultObj.accumulate("status", true);
						 }else{
							 resultObj.accumulate("status", false);
							 resultObj.accumulate("info", obj.getString("Message"));
						 }
					 }else{
						 throw new Exception("注册发生异常，Status为空");
					 }
				 }else{
					 throw new Exception("注册发生异常，Status不存在");
				 }
			}else{
				throw new Exception("注册发生异常，远程注册接口返回为空");
			}
			os.close();
			br.close();
			conn.disconnect();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultObj.accumulate("status", false);
			resultObj.accumulate("info", e.getMessage());
		}
		return resultObj;

	}

	/**
	 * 验证用户是否登陆
	 */
	@Override
	public boolean isLogined(String userid) {
		boolean flag=true;
		UserEntity user= findUserById(userid);
		if(user == null){
			return false;
		}else if(user.getIsLogined()==null || "".equals(user.getIsLogined())){
			flag=false;
			user.setIsLogined("1");
			userDao.save(user);
		}
		return flag;

	}

	@Override
	public boolean updateOrderItemLoginStatus(String id) {
		boolean isUpdateSuccess = true;
		try {
			this.userDao.updateOrderItemLoginStatus(id);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return isUpdateSuccess;

	}

	@Override
	@Transactional
	public String initUserInfoOrOrders(UserEntity userEntity, List<OrderEntity> orders,
			List<OrderItemsEntity> orderItems,String mainModuleId) {
		List<UserEntity> ue = this.userDao.findByNameOrEmail(userEntity.getEmail());
		UserEntity resultEntity = null;
		if(ue== null || ue.size() <= 0){
			InfoDeptEntity ide = userEntity.getDeptId();
			ide.setCode(buildTopDeptCode());
			// 保存部门信息
			InfoDeptEntity ie = this.infoDeptDao.save(userEntity.getDeptId());
			// 保存企业信息
			ComEntity ce = this.comDao.save(userEntity.getEid());
			// 保存用户信息
			userEntity.setEid(ce);
			userEntity.setDeptId(ie);
			resultEntity = this.userDao.save(userEntity);
		}else{
			resultEntity = ue.get(0);
		}
		
		// 如果查询结果不为空，则表示该功能模块已经被导入过了
		List<OrderEntity> orderEntitys = this.orderDao.queryByMainModuleId(mainModuleId, resultEntity.getId());
		if(null != orderEntitys && orderEntitys.size() > 0){
			return resultEntity.getId();
		}
		
		
		for(OrderEntity oe : orders){
//			oe.setUserId(resultEntity.getId());
			oe.setUser(resultEntity);
			oe.setEid(resultEntity.getEid().getId());
			String tempId = oe.getId();
			OrderEntity newOrderEntity = this.orderDao.save(oe);
			for(OrderItemsEntity oie : orderItems){
				if(tempId.equals(oie.getOrderId().getId())){
//					oie.setOrderId(newOrderEntity.getId());
					oie.setOrderId(newOrderEntity);
				}
			}
			
		}
		// 保存订单信息
		this.orderItemDao.save(orderItems);
		
		return resultEntity.getId();
	} 
	
	
	/**
	 * 构建顶级部门code
	 * <p>Title ：buildTopDeptCode</p>
	 * Description：
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-5-16 下午02:02:35
	 */
	public String buildTopDeptCode(){
    	String codeStr = infoDeptDao.queryMaxCode();
		int code = 1000;
		if(!StringUtils.isEmpty(codeStr)){
			code = Integer.parseInt(codeStr);
			code += 1000;
		}else{
			code = 1000;
		}
		String topCode = code + "";
		StringBuilder codeBuilder = new StringBuilder();
		for(int i= topCode.length();i < 8;i++){
			codeBuilder.append("0");
		}
    	return codeBuilder.toString() + topCode;
    }
	@Override
	@Transactional
	public boolean initUserInfo(UserEntity userEntity, List<OrderEntity> orders,List<OrderItemsEntity> orderItems) {
//		UserEntity ue = this.userDao.findById(userEntity.getId());
		List<UserEntity> ue = this.userDao.findByNameOrEmail(userEntity.getEmail());
		if(ue != null && ue.size() > 0){
			throw new NullPointerException("许可不允许重复导入");
		}
		// 保存部门信息
		InfoDeptEntity ie = this.infoDeptDao.save(userEntity.getDeptId());
		// 保存企业信息
		ComEntity ce = this.comDao.save(userEntity.getEid());
		// 保存用户信息
		userEntity.setEid(ce);
		userEntity.setDeptId(ie);
		userEntity = this.userDao.save(userEntity);
		for(OrderEntity oe : orders){
//			oe.setUserId(userEntity.getId());
			oe.setUser(userEntity);
			oe.setEid(ce.getId());
			String oldId = oe.getId();
			oe = this.orderDao.save(oe);
			String newId = oe.getId();
			for(OrderItemsEntity item : orderItems){
				if(item.getOrderId().getId().equals(oldId)){
//					item.setOrderId(newId);
					item.setOrderId(oe);
				}
			}
		}
		// 保存订单信息
		this.orderItemDao.save(orderItems);
		return true;
	}

	@Override
	public boolean isOfflineFirstRun() {
		long count = this.userDao.count();
		LOGGER.info("## 当前用户数量:"+count);
		return count <= 0;
	} 
	
	/**
	 * 根据用户id查找订单表中订单状态是“正式审核通过”的订单数量
	 */
	@Override
	public int findCountByUserStat(String userid) {
		return (int)this.userDao.findCountByUserStat(userid);
	}

	@Override
	public List<UserEntity> getUsersByCurrentDept(final String deptId) {
		Specification<UserEntity> spec = new Specification<UserEntity>() {
			@Override
			public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				Path<String> deptId2=root.get("deptId").get("id");
				//Predicate deptIdPredicate=deptId2.in(depts);
				//predicateList.add(deptIdPredicate);
				predicateList.add(builder.equal(deptId2, deptId));
				Predicate [] predicates=new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				query.where(predicates);
				return null;
			}
		};
		Sort sort = new Sort(Direction.ASC,new String[]{"createTime"});
		return userDao.findAll(spec,sort);
	}
	
	
	@Override
	public SysDefaultCityEntity saveUserDefaultCity(SysDefaultCityEntity s) {
		SysDefaultCityEntity result=s;
		List<SysDefaultCityEntity> list=this.sysDefaultCityDao.findByUserid(s.getUserid());
		SysDefaultCityEntity exsit=null;
		if(list!=null&&list.size()>0){
			exsit=list.get(0);
		}
		if(exsit==null||exsit.getAdmincode()==null){
			result=this.sysDefaultCityDao.save(s);
		}else{
			this.sysDefaultCityDao.updateDefaultCity(s.getEid(), s.getDeptid(), s.getClevel(), 
					s.getAdmincode(), s.getDefaultname(), s.getProvince(), 
					s.getCity(), s.getCounty(), new Date(), s.getUserid());
		}
		
		return result;
	}

	@Override
	public SysDefaultCityEntity findUserDefaultCity(String userid) {
		List<SysDefaultCityEntity> list=this.sysDefaultCityDao.findByUserid(userid);
		SysDefaultCityEntity exsit=null;
		if(list!=null&&list.size()>0){
			exsit=list.get(0);
		}
		return exsit;
	}

	@Override
	public boolean isTempLogined(String userid) {
		boolean flag=true;
		UserEntity user= findUserById(userid);
		if(user == null){
			return false;
		}else if(user.getTempLogined()==null || "".equals(user.getTempLogined())){
			flag=false;
			user.setTempLogined("1");
			userDao.save(user);
		}
		return flag;
	}

	@Override
	public UserEntity findTopUserByEid(String eid) {
		UserEntity user=null;
		List<UserEntity> users=this.userDao.findTopUserByEid(eid);
		if(users!=null&&users.size()>0){
			user=users.get(0);
		}
		return user;
	}

	@Override
	public Map<String,String> findAllUserByEid(String eid) {
		Map<String,String> result=null;
		List<?> objectlist= this.userDao.findAlluserByEid(eid);
		if(objectlist!=null&&objectlist.size()>0){
			result=new HashMap<String,String>();
			for(int i =0,s=objectlist.size();i<s;i++){
				Object obj[]=(Object[]) objectlist.get(i);
				result.put((String)obj[0],(String)obj[1]);
			}
		}
		return result;
	}

	
	@Override
	public boolean findIfUserReadLastLogs(String userid) {
		String logid=null;
		Map<String,Object> parammap=new HashMap<String,Object>();
		parammap.put("pageNo", 1);
		parammap.put("pageSize", 1);
		Map<String,Object> logmap=this.sysUpdateLogService.getLogsByParams(parammap);
		if(logmap!=null&&logmap.get("records")!=null){
			List<SysUpdateLogEntity> loglist=(List<SysUpdateLogEntity>) logmap.get("records");
			if(loglist!=null&&loglist.size()>0){
				logid=loglist.get(0).getId();
			}
		}
		else{
			return true;
		}
		SysUpdateLogUserEntity  loguser=this.sysUpdateLogUserDao.findByUpdateLogidAndUserid(logid, userid);
		if(loguser!=null){
			return true;
		}
		else return false;
	}
	
	

	@Override
	public void saveSysUpdateLogUser(String userid) {
		Map<String,Object> parammap=new HashMap<String,Object>();
		parammap.put("pageNo", 1);
		parammap.put("pageSize", 1);
		Map<String,Object> logmap=this.sysUpdateLogService.getLogsByParams(parammap);
		if(logmap!=null&&logmap.get("records")!=null){
			List<SysUpdateLogEntity> loglist=(List<SysUpdateLogEntity>) logmap.get("records");
			if(loglist!=null&&loglist.size()>0){
				String logid=loglist.get(0).getId();
				SysUpdateLogUserEntity  exsitloguser=this.sysUpdateLogUserDao.findByUpdateLogidAndUserid(logid, userid);
				if(exsitloguser==null){
					SysUpdateLogUserEntity loguser=new SysUpdateLogUserEntity();
					loguser.setReadFlag(1);
					loguser.setCreateTime(new Date());
					loguser.setUserid(userid);
					loguser.setUpdateLogid(logid);
					this.sysUpdateLogUserDao.save(loguser);
				}
			}
		}
	}

	
	@Override
	public void saveUser(UserEntity user, ComEntity ce)
			throws ParameterException {
		// 是否存在该用户名
			exsitUsernameOrEmail(user.getUsername(), user.getEmail());
			// 默认状态为common
			UserStatusEntity statusEntity = userStatusDao.findByValue(EbossStatusConstants.USER_STATUS_COMMON);
			if (statusEntity == null) {
				statusEntity = new UserStatusEntity();
				statusEntity.setValue(EbossStatusConstants.USER_STATUS_WAIT_AUDIT);
				this.userStatusDao.save(statusEntity);
			}
			user.setStratusId(statusEntity.getId());
			// 来源设置为运营支撑系统
			UserSourceEntity userSource = userSourceDao.findByValue(EbossStatusConstants.USER_SOURCE);
			if (null == userSource) {
				userSource = new UserSourceEntity();
				userSource.setValue(EbossStatusConstants.USER_SOURCE);
				this.userSourceDao.save(userSource);
			}
			user.setSourceId(userSource.getId());
			ComStatusEntity comStatusEntity = comStatusDao.findByValue(EbossStatusConstants.COMPANY_STATUS_COMMON);
			ce.setStatusId(comStatusEntity.getId());
			ce.setCreateTime(new Date());
			// 添加企业
			this.comDao.save(ce);
			user.setEid(ce);
			// 对密码进行加密
			String password = user.getPassword();
			password = EncryptionUtil.md5Encry(password, null);
			user.setPassword(password);
			user.setFirstLogin(new Date());
			userDao.save(user);
			// 创建默认部门
			InfoDeptEntity deptEntity = new InfoDeptEntity();
			deptEntity.setAddress(user.getAddress());
			deptEntity.setHeadName(user.getRealname());
			deptEntity.setHeadPhone(user.getTelephone());
			deptEntity.setName(ce.getName());
			deptEntity.setPhone(user.getTelephone());
			deptEntity.setZipcode(user.getZipCode());
			deptEntity.setCreateUserid(user.getId());
			deptEntity.setType(Byte.valueOf("1"));
			String codeStr = this.infoDeptDao.queryMaxCode();
			int code = 1000;
			if(!StringUtils.isEmpty(codeStr)){
				code = Integer.parseInt(codeStr);
				code += 1000;
			}
			deptEntity.setCode(code+"");
			deptEntity.setOperDate(new Date());
			this.infoDeptDao.save(deptEntity);
			user.setDeptId(deptEntity);
			this.userDao.save(user);
	}

	@Override
	public BaseUserInfo findEbossUserById(String id) {
		UserEntity ue = userDao.findById(id);
		BaseUserInfo bui = buildUserInfo(ue);
        return bui;
	}

	@Override
	public void updateUserStatus(String id, UserStatusEntity status,
			String remarks) {
		UserEntity ue = userDao.findById(id);
		UserStatusEntity userStatus = this.userStatusDao.findByValue(status.getValue());
		if(null == userStatus){
			userStatus = new UserStatusEntity();
			userStatus.setValue(status.getValue());
			userStatusDao.save(userStatus);
			if(status.getValue().equals(EbossStatusConstants.USER_STATUS_FORBIDDEN)){
				ComEntity ce = ue.getEid();
				ComStatusEntity cse = this.comStatusDao.findByValue(EbossStatusConstants.COMPANY_STATUS_FORBIDDEN);
				ce.setStatusId(cse.getId());
				ue.setEid(ce);
			}
		}
		ue.setStratusId(userStatus.getId());
		if(!StringUtils.isEmpty(remarks) && !remarks.equals(ue.getRemark())){
			ue.setRemark(remarks);
		}
		userDao.save(ue);
	}

	@Override
	public void addAppKey(String userId, AppkeyEntity appKey) {
		appKey.setUserId(userId);
		this.appkeyDao.save(appKey);
	}

	@Override
	public void deleteAppKeyById(String id) {
		this.appkeyDao.delete(id);
	}

	@Override
	public AppkeyEntity queryAppKeyById(String id) {
		return this.appkeyDao.findById(id);
	}

	@Override
	public boolean existUser(String id) {
		UserEntity ue = userDao.findById(id);
		return ue != null;
	}

	@Override
	public BaseUserListPkg query(String id, final String info,
			UserStatusEntity status, int pageNo, int pageSize,
			final String admincode,  final String businesstype, final Date btime, final Date etime)
			throws ParameterException {
		BaseUserListPkg pkg = new BaseUserListPkg();
		// 如果提供了ID，则根据ID查询
		if(!CommonUtil.isStringEmpty(id)){
			UserEntity ue = this.userDao.findById(id);
			pkg.setTotalCount(1);
			pkg.setCurrentCount(1);
			BaseUserListInfo bui = new BaseUserListInfo();
			bui.setEmail(ue.getEmail());
			bui.setId(ue.getId());
			bui.setMobilephone(ue.getMobilephone());
			bui.setRealName(ue.getRealname());
			bui.setStatus(ue.getStratusId().toString());
			bui.setUsername(ue.getUsername());
			bui.setTelephone(ue.getTelephone());
			bui.setQq(ue.getQq());
			if(null != ue.getFirstLogin()){
				bui.setFirstLogin(CommonUtil.dataConvert(ue.getFirstLogin(), DateType.TIMESTAMP));
			}
			pkg.setUserInfos(new BaseUserListInfo[]{bui});
			
			return pkg;
		}
		
		String statusStr = status==null?null:status.getValue();
		UserStatusEntity userStatus = null;
		if(!StringUtils.isEmpty(statusStr)){
			 userStatus = this.userStatusDao.findByValue(statusStr);
			 statusStr = userStatus.getId()+"";
		}else{
			statusStr = null;
		}
		
		final UserStatusEntity userStatusf=userStatus;
		
		Specification<UserEntity> spec=new Specification<UserEntity>() {
			@Override
			public Predicate toPredicate(Root<UserEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> sourceidpath=root.get("sourceId");
				Path<String> statusidpath=root.get("stratusId");
				Path<String> admincodepath=root.get("eid").get("admincode");
				Path<String> combusinesspath=root.get("eid").get("combusiness");
				Path<String> usermailpath=root.get("email");
				Path<String> usernamepath=root.get("username");
				Path<String> telephonepath=root.get("telephone");
				Path<String> mobilepath=root.get("mobilephone");
				Path<String> realnamepath=root.get("realname");
				Path<String> comnamepath=root.get("eid").get("name");
				Path<Date> creattimepath=root.get("createTime");
				
				List<Predicate> prelist=new ArrayList<Predicate>();
				
				List<String> sourceidlist=new ArrayList<String>();
				sourceidlist.add("1");
				sourceidlist.add("2");
				prelist.add(sourceidpath.in(sourceidlist));
				
				if(userStatusf!=null){
					prelist.add(builder.equal(statusidpath, userStatusf.getId()));
				}
				
				if(!StringUtils.isEmpty(admincode)){
					prelist.add(builder.equal(admincodepath, admincode));
				}
				
				if(!StringUtils.isEmpty(businesstype)){
					prelist.add(builder.equal(combusinesspath, businesstype));
				}
				
				if(btime!=null){
					prelist.add(builder.greaterThanOrEqualTo(creattimepath, btime));
				}
				
				if(etime!=null){
					prelist.add(builder.lessThanOrEqualTo(creattimepath, etime));
				}
				
				if(StringUtils.isNotEmpty(info)){
					List<Predicate> orprelist=new ArrayList<Predicate>();
					orprelist.add(builder.like(comnamepath, "%"+info+"%"));
					orprelist.add(builder.like(usermailpath, "%"+info+"%"));
					orprelist.add(builder.like(usernamepath, "%"+info+"%"));
					orprelist.add(builder.like(telephonepath, "%"+info+"%"));
					orprelist.add(builder.like(mobilepath, "%"+info+"%"));
					orprelist.add(builder.like(realnamepath, "%"+info+"%"));
					Predicate[] orprearr=new Predicate[orprelist.size()];
					orprelist.toArray(orprearr);
					prelist.add(builder.or(orprearr));
				}
				
				Predicate[] prearr=new Predicate[prelist.size()];
				prelist.toArray(prearr);
				query.where(prearr);
				return null;
			}
		};

		if(pageNo<0){//不分页
			List<UserEntity>  userlist=this.userDao.findAll(spec, new Sort(Direction.DESC, new String[]{"createTime","firstLogin"}));
			pkg.setTotalCount(userlist.size());
			pkg.setCurrentCount(userlist.size());
			pkg.setUserInfos(formatBaseUserlist(userlist));
		}else{//分页
			PageRequest page=new PageRequest(pageNo, pageSize, Direction.DESC, new String[]{"createTime","firstLogin"});
			Page<UserEntity>  pageresult=this.userDao.findAll(spec, page);
			if(pageresult!=null&&pageresult.getContent()!=null){
				pkg.setTotalCount((int)pageresult.getTotalElements());
				pkg.setCurrentCount(pageresult.getContent().size());
				pkg.setUserInfos(formatBaseUserlist(pageresult.getContent()));
			}
		}
		return pkg;
	}

	@Override
	public void exsitUsernameOrEmail(String username, String email)
			throws ParameterException {
		UserEntity ue = null;
		ue = this.userDao.findByEmail(email);
		if(null != ue){
			throw new ParameterException(ExceptionType.EXISTED_DATA,"email:"+email);
		}
		
	}
	
	
	private BaseUserInfo buildUserInfo(UserEntity ue ){
    	BaseUserInfo bui = null;
    	if(ue != null){
    		bui = new BaseUserInfo();
    		bui.setUsername(ue.getUsername());
    		bui.setAddress(ue.getAddress());
    		bui.setEmail(ue.getEmail());
    		bui.setFax(ue.getFax());
    		bui.setId(ue.getId());
    		bui.setMobilePhone(ue.getMobilephone());
    		bui.setRealName(ue.getRealname());
    		bui.setSex(ue.getSex()==null?"":ue.getSex().toString());
    		if(ue.getTelephone()==null||ue.getTelephone().trim().equals("")){
				bui.setTelephone(ue.getMobilephone());
			}
			else bui.setTelephone(ue.getTelephone());
    		bui.setZipcode(ue.getZipCode());
    		bui.setStatus(this.userStatusDao.findById(ue.getStratusId()).getValue());
    		bui.setRemarks(ue.getRemark());
    		bui.setQq(ue.getQq());
    		if(null != ue.getFirstLogin()){
				bui.setFirstLogin(CommonUtil.dataConvert(ue.getFirstLogin(), DateType.TIMESTAMP));
			}
    		ComEntity ce = ue.getEid();
    		if(null != ce){
    			bui.setCompanyAddress(ce.getAddress());
    			bui.setCompanyEmail(ce.getEmail());
    			bui.setCompanyName(ce.getName());
    			bui.setCompanyPhone(ce.getPhone());
    			bui.setCompanyRemarks(ce.getRemark());
    			bui.setAdmincode(ce.getAdmincode());
    			bui.setCompanyName(ce.getName());
    			bui.setAdmincode(ce.getAdmincode());
    			bui.setAdminname(ce.getAdminname());
    			//公司的行业信息 
    			if(!StringUtils.isEmpty(ce.getBusinessremark())){
    				bui.setCombusiness(ce.getCombusiness()+":"+ce.getBusinessremark());
    			}else{
    				bui.setCombusiness(ce.getCombusiness());
    			}
    			bui.setBusinessremark(ce.getBusinessremark());
    		}
    	}
    	return bui;
    }
	
	
	public BaseUserListInfo[] formatBaseUserlist(List<UserEntity> userEntitys){
		BaseUserListInfo[] listInfos = new BaseUserListInfo[userEntitys.size()];
		for(int i=0;i<userEntitys.size();i++){
			UserEntity ue = userEntitys.get(i);
			BaseUserListInfo bui = new BaseUserListInfo();
			bui.setEmail(ue.getEmail());
			bui.setId(ue.getId());
			bui.setMobilephone(ue.getMobilephone());
			bui.setRealName(ue.getRealname());
			bui.setStatus(this.userStatusDao.findById(ue.getStratusId()).getValue());
			bui.setUsername(ue.getUsername());
			
			if(ue.getTelephone()==null||ue.getTelephone().trim().equals("")){
				bui.setTelephone(ue.getMobilephone());
			}
			else bui.setTelephone(ue.getTelephone());
			
			if(null != ue.getFirstLogin()){
				bui.setFirstLogin(CommonUtil.dataConvert(ue.getFirstLogin(), DateType.TIMESTAMP));
			}
			
			ComEntity ce = ue.getEid();
    		if(null != ce){
    			bui.setCompanyName(ce.getName());
    			bui.setAdmincode(ce.getAdmincode());
    			bui.setAdminname(ce.getAdminname());
    			bui.setCompanyid(ce.getId());
    			//公司的行业信息 
    			if(!StringUtils.isEmpty(ce.getBusinessremark())){
    				bui.setCombusiness(ce.getCombusiness()+":"+ce.getBusinessremark());
    			}else{
    				bui.setCombusiness(ce.getCombusiness());
    			}
    			bui.setBusinessremark(ce.getBusinessremark());
    		}
			
			listInfos[i] = bui;
		}
		return listInfos;
	}

	@Override
	public void regist(UserEntity user, ComEntity ce) throws ParameterException {
		// 判断用户名或邮箱已经被占用
		exsitUsernameOrEmail(user.getUsername(), user.getEmail());
		JSONObject remoteRegistResult = remoteRegist(user.getEmail(), user.getPassword());
		// 判断远程注册接口调用是否成功
		if(!remoteRegistResult.getBoolean("status")){
			throw new ParameterException(remoteRegistResult.getString("info"));
		}
		ComStatusEntity statusEntity = this.comStatusDao.findByValue(StatusConstants.COM_STATUS_COMMON);
		ce.setStatusId(statusEntity.getId());
		ce.setCreateTime(new Date());
		this.comDao.save(ce);
		user.setEid(ce);
		// 处理状态
		UserStatusEntity userStatusEntity = this.userStatusDao.findByValue(EbossStatusConstants.USER_STATUS_COMMON);
		user.setStratusId(userStatusEntity.getId());
		// 处理来源
		UserSourceEntity sourceEntity = this.userSourceDao.findByValue(PortalConstants.USER_SOURCE_PORTAL);
		user.setSourceId(sourceEntity.getId());
		user = encryPassowrd(user);
		userDao.save(user);
		// 创建顶级部门
		InfoDeptEntity deptEntity = new InfoDeptEntity();
		deptEntity.setAddress(user.getAddress());
		deptEntity.setHeadName(user.getRealname());
		deptEntity.setHeadPhone(user.getTelephone());
		deptEntity.setName(ce.getName());
		deptEntity.setPhone(user.getTelephone());
		deptEntity.setZipcode(user.getZipCode());
		deptEntity.setCreateUserid(user.getId());
		deptEntity.setType(Byte.valueOf("1"));
		String codeStr = this.infoDeptDao.queryMaxCode();
		int code = 1000;
		if(!StringUtils.isEmpty(codeStr)){
			code = Integer.parseInt(codeStr);
			code += 1000;
		}
		deptEntity.setCode(code+"");
		deptEntity.setOperDate(new Date());
		this.infoDeptDao.save(deptEntity);
		user.setDeptId(deptEntity);
		this.userDao.save(user);
	}

	@Override
	public void updateUserInfo(String id, String mobilePhone, String phone,
			String fax, String zipcode, String address, String companyAddres,
			String companyEmail, String companyPhone, String remarks,
			String qq, String realName, String companyName, String admincode,
			String adminname, String business, String businessremark)
			throws ParameterException {
		UserEntity userEntity = this.userDao.findById(id);
		if(null == userEntity){
			throw new ParameterException(ExceptionType.NOT_FOUND,UserInfoFieldNames.ID+":"+id);
		}
		boolean isUpdated = false;
		// 修改手机号码
		if(!CommonUtil.isStringEmpty(mobilePhone) && !mobilePhone.equals(userEntity.getMobilephone())){
			userEntity.setMobilephone(mobilePhone);
			isUpdated = true;
		}
		// 修改座机
		if(!CommonUtil.isStringEmpty(phone) && !phone.equals(userEntity.getTelephone())){
			userEntity.setTelephone(phone);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		// 修改传真
		if(!CommonUtil.isStringEmpty(fax) && !fax.equals(userEntity.getFax())){
			userEntity.setFax(fax);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		
		// 修改邮编
		if(!CommonUtil.isStringEmpty(zipcode) && !zipcode.equals(userEntity.getZipCode())){
			userEntity.setZipCode(zipcode);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		// 修改详细地址
		if(!CommonUtil.isStringEmpty(address) && !address.equals(userEntity.getAddress())){
			userEntity.setAddress(address);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		if (!StringUtils.isEmpty(qq) && !qq.equals(userEntity.getQq())) {
			userEntity.setQq(qq);
			if (!isUpdated) {
				isUpdated = true;
			}
		}
		
		if(!StringUtils.isEmpty(realName) && !realName.equals(userEntity.getRealname())){
			userEntity.setRealname(realName);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		
		ComEntity ce = userEntity.getEid();
		if(null != ce){
			boolean isCompanyNeedUpdate = false;
			if(!CommonUtil.isStringEmpty(companyAddres) && !companyAddres.equals(ce.getAddress())){
				ce.setAddress(companyAddres);
				if(!isCompanyNeedUpdate){
					isCompanyNeedUpdate = true;
				}
			}
			if(!CommonUtil.isStringEmpty(companyPhone) && !companyPhone.equals(ce.getPhone())){
				ce.setPhone(companyPhone);
				if(!isCompanyNeedUpdate){
					isCompanyNeedUpdate = true;
				}
			}
			if(!CommonUtil.isStringEmpty(companyEmail) && !companyEmail.equals(ce.getEmail())){
				ce.setEmail(companyEmail);
				if(!isCompanyNeedUpdate){
					isCompanyNeedUpdate = true;
				}
			}
			
			if(!StringUtils.isEmpty(companyName) && !companyName.equals(ce.getName())){
				ce.setName(companyName);
				if(!isCompanyNeedUpdate){
					isCompanyNeedUpdate = true;
				}
			}
			//
			if(!StringUtils.isEmpty(admincode) && !admincode.equals(ce.getAdmincode())){
				ce.setAdmincode(admincode);
				if(!isCompanyNeedUpdate){
					isCompanyNeedUpdate = true;
				}
			}
			if(!StringUtils.isEmpty(adminname) && !adminname.equals(ce.getAdminname())){
				ce.setAdminname(adminname);
				if(!isCompanyNeedUpdate){
					isCompanyNeedUpdate = true;
				}
			}
			if(!StringUtils.isEmpty(business)){
				ce.setCombusiness(business);
				ce.setBusinessremark(businessremark);
				if(!isCompanyNeedUpdate){
					isCompanyNeedUpdate = true;
				}
			}
			if(isCompanyNeedUpdate){
				ce.setUpdateTime(new Date());
				this.comDao.save(ce);
			}
		}
		
		if(!CommonUtil.isStringEmpty(remarks) && !remarks.equals(userEntity.getRemark())){
			userEntity.setRemark(remarks);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		if(isUpdated){
			userEntity.setUpdateTime(new Date());
			this.userDao.save(userEntity);
		}
		
	}

	@Override
	public void changePassword(String id, String password, String oldPassword)
			throws ParameterException {
		UserEntity ue = this.userDao.findById(id);
		if(null == ue){
			throw new ParameterException(ExceptionType.NOT_FOUND,UserInfoFieldNames.ID+":"+id);
		}
		String tempPassword = EncryptionUtil.md5Encry(oldPassword, null);
		if(!tempPassword.equals(ue.getPassword())){
			throw new ParameterException("旧密码不正确");
		}
		tempPassword = EncryptionUtil.md5Encry(password, null);
		ue.setPassword(tempPassword);
	}

	@Override
	public BaseAccessInfo queryByName(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseAccessInfo login(String loginInfo, String password)
			throws ParameterException {
		BaseAccessInfo info = null;
		UserEntity userEntity = null;
		String[] loginInfoItems = loginInfo.split(",");
		String username = null;
		String tel = null;
		if(null != loginInfoItems && loginInfoItems.length > 1){
			username = loginInfoItems[1].replaceAll("#", "");
			tel = loginInfoItems[2].replaceAll("#", "");
		}
		String email = loginInfoItems[0].replaceAll("#", "");
		userEntity = queryByNameOrEmailOrTel(email, username, tel);
		if (null == userEntity) {
			info = new BaseAccessInfo();
			info.setEmail(email);
			info.setUsername(username);
			info.setTel(tel);
			// 设置来源为-1 表示系统未找到
			info.setSource("-1");
			return info;
			// throw new
			// ParameterException(ExceptionType.NOT_FOUND,UserInfoFieldNames.EMAIL+":"+username);
		}
		
		//如果邮箱、用户名、电话有变更，需要进行更新
		updateUserBaseInfo(username, email, tel, userEntity);
		// }
		// 判断是否门户用户
		UserSourceEntity userSource =this.userSourceDao.findById(userEntity.getSourceId());
		if (userSource != null) {
			String sourceValue = userSource.getValue();
			LOGGER.info("## 用户登录来源["+sourceValue+"]");
			if (!sourceValue.equals(PortalConstants.USER_SOURCE_PORTAL)
					&& !sourceValue.equals(PortalConstants.USER_SOURCE_RSS)
					&& !sourceValue.equals(PortalConstants.USER_SOURCE_ZIZU)
					&& !sourceValue.equals(PortalConstants.USER_SOURCE_OTHERS)) {
				throw new ParameterException("未知用户来源");
			}
		} else {
			throw new ParameterException("用户来源标识有误");
		}
		
		UserStatusEntity statusEntity = this.userStatusDao.findById(userEntity.getStratusId());
		// 检查状态
		if(null == statusEntity){
			throw new ParameterException("用户状态为空，请联系管理员");
		}
		if(!(EbossStatusConstants.USER_STATUS_COMMON.equals(statusEntity.getValue()) || EbossStatusConstants.USER_STATUS_ADV.equals(statusEntity.getValue()))){
			throw new ParameterException("用户状态为["+statusEntity.getValue()+"],不允许登录");
		}
		// 不需要对密码进行验证
//		String tempPsw = EncryptionUtil.md5Encry(password, null);
//		if(!tempPsw.equals(userEntity.getPassword())){
//			throw new ParameterException("密码不正确");
//		}
		info = new BaseAccessInfo();
		info.setRealName(userEntity.getRealname());
		info.setUserId(userEntity.getId());
		info.setEmail(userEntity.getEmail());
		info.setSource(userSource.getValue());
		info.setStatus(statusEntity.getValue());
		ComEntity company = userEntity.getEid();
		if(company != null){
			info.setCompanyId(company.getId());
			info.setCompanyName(company.getName());
		}
		return info;
	}

	@Override
	@Transactional(readOnly=true)
	public String licenseService(String id) throws ParameterException {
		UserEntity userEntity = this.userDao.findById(id);
		LOGGER.info("## start add userinfo["+id+"]...");
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("lic");
		// 构建用户信息
		buildUserInfo(rootElement, userEntity);
		List<OrderEntity> orders = this.orderDao.queryByUserid(id);
		if(orders == null || orders.size() <= 0){
			throw new ParameterException(ExceptionType.NOT_FOUND,"未找到用户订单信息");
		}
		LOGGER.info("## start add orders[size="+orders.size()+"]");
		// 构建订单信息
		buildOrderInfo(rootElement, orders);
		return document.asXML();
	}
	
	private UserEntity encryPassowrd(UserEntity ue){
    	String password = ue.getPassword();
    	password = EncryptionUtil.md5Encry(password, null);
    	ue.setPassword(password);
    	return ue;
    }
	
	
	public UserEntity queryByNameOrEmailOrTel(final String email, final String username, final String tel){
		Specification<UserEntity> spec=new Specification<UserEntity>() {
			@Override
			public Predicate toPredicate(Root<UserEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> emailpath=root.get("email");
				Path<String> usernamepath=root.get("username");
				Path<String> telephonepath=root.get("telephone");
				List<Predicate> prelist=new ArrayList<Predicate>();
				
				if(!StringUtils.isEmpty(email)){
					prelist.add(builder.equal(emailpath, email));
				}
				
				if(!StringUtils.isEmpty(username)){
					prelist.add(builder.equal(usernamepath, username));
				}
				
				if(!StringUtils.isEmpty(tel)){
					prelist.add(builder.equal(telephonepath, tel));
				}
				
				Predicate[] prea=new Predicate[prelist.size()];
				prelist.toArray(prea);
				Predicate orp = builder.or(prea);
			    query.where(orp);
				return null;
			}
		};
		List<UserEntity>  userlist=this.userDao.findAll(spec);
		if(null!=userlist&&userlist.size()>0){
			return userlist.get(0);
		}else return null;
	}
	
	
	@Transactional
	private void updateUserBaseInfo(String username,String email,String tel,UserEntity ue){
		if(!StringUtils.isEmpty(username) && !username.equals(ue.getUsername())){
			ue.setUsername(username);
		}
		if(!StringUtils.isEmpty(email) && !email.equals(ue.getEmail())){
			ue.setEmail(email);
		}
		if(!StringUtils.isEmpty(tel) && !tel.equals(ue.getTelephone())){
			ue.setTelephone(tel);
		}
		this.userDao.save(ue);
	}
	
	
	
	private void buildUserInfo(Element rootElement,UserEntity userEntity)throws ParameterException{
		// 设置主机名
		Element hostnameEle = rootElement.addElement("hostname");
		hostnameEle.setText("");
		// 设置mac地址
		Element macEle = rootElement.addElement("mac");
		macEle.setText("");
		// 设置用户信息
		Element userinfoEle = rootElement.addElement("userinfo");
		Element userIdEle = userinfoEle.addElement("id");
		userIdEle.setText(userEntity.getId());
		Element usernameEle = userinfoEle.addElement("username");
		usernameEle.setText(userEntity.getUsername()==null?"":userEntity.getUsername());
		Element realnameEle = userinfoEle.addElement("realname");
		realnameEle.setText(userEntity.getRealname()==null?"":userEntity.getRealname());
		Element mobileEle = userinfoEle.addElement("mobilephone");
		mobileEle.setText(userEntity.getMobilephone()==null?"":userEntity.getMobilephone());
		Element emailEle = userinfoEle.addElement("email");
		emailEle.setText(userEntity.getEmail());
		Element qqEle = userinfoEle.addElement("qq");
		qqEle.setText(userEntity.getQq()==null?"":userEntity.getQq());
		Element statusEle = userinfoEle.addElement("status");
		statusEle.setText(userEntity.getStratusId()+"");

		// 设置企业信息
		Element comEle = userinfoEle.addElement("enterprise");
		Element comIdEle = comEle.addElement("id");
		comIdEle.setText(userEntity.getEid().getId());
		Element comNameEle = comEle.addElement("name");
		comNameEle.setText(userEntity.getEid().getName());
		
		// 设置部门信息
		Element departEle = userinfoEle.addElement("department");
		Element departIdEle = departEle.addElement("id");
		InfoDeptEntity infoDept = userEntity.getDeptId();
		if(null == infoDept){
			throw new ParameterException(ExceptionType.NOT_FOUND,"未找到部门信息");
		}
		departIdEle.setText(infoDept.getId());
		Element headnameEle = departEle.addElement("headname");
		headnameEle.setText(infoDept.getHeadName()==null?"":infoDept.getHeadName());
		Element operDateEle = departEle.addElement("operate_date");
		operDateEle.setText(CommonUtil.dataConvert(infoDept.getOperDate(), DateType.TIMESTAMP));
		Element departNameEle = departEle.addElement("name");
		departNameEle.setText(infoDept.getName()==null?"":infoDept.getName());
		Element codeEle = departEle.addElement("code");
		codeEle.setText(infoDept.getCode());
		Element phoneEle = departEle.addElement("phone");
		phoneEle.setText(infoDept.getPhone());
		Element typeEle = departEle.addElement("type");
		typeEle.setText(infoDept.getType()+"");
	}
	
	
	@SuppressWarnings("unchecked")
	private void buildOrderInfo(Element rootElement,List<OrderEntity> orders){
		Element ordersElement = rootElement.addElement("orders");
		for(int i=0;i<orders.size();i++){
			OrderEntity orderEntity = orders.get(i);
			OrderStatusEntity ose = orderEntity.getStatus();
			if (!ose.getValue().equals(EbossStatusConstants.ORDER_STATUS_NO_PASS)
					&& !ose.getValue().equals(EbossStatusConstants.ORDER_STATUS_WAIT_AUDIT)) {
				Element orderEle = ordersElement.addElement("order");
				Element idEle = orderEle.addElement("id");
				idEle.setText(orderEntity.getId());
				Element typeEle = orderEle.addElement("order_type");
				typeEle.setText(orderEntity.getOrderType()+"");
				Element submitEle = orderEle.addElement("submit_time");
				submitEle.setText(CommonUtil.dataConvert(orderEntity.getSubmitTime(), DateType.TIMESTAMP));
				Element statusEle = orderEle.addElement("status");
				statusEle.setText(orderEntity.getStatus().getId()+"");
				List<OrderItemsEntity> items = orderEntity.getOrderItems();
				if(null != items && items.size() > 0){
					Element itemsEle = orderEle.addElement("items");
					for(int j=0;j<items.size();j++){
						OrderItemsEntity item = items.get(j);
						Element itemEle = itemsEle.addElement("item");
						Element itemIdEle = itemEle.addElement("id");
						itemIdEle.setText(item.getId());
						Element itemModuleIdEle = itemEle.addElement("module_id");
						itemModuleIdEle.setText(item.getModuleId().getId());
						Element userTimeEle = itemEle.addElement("user_time");
						userTimeEle.setText(CommonUtil.dataConvert(item.getUseTime(), DateType.TIMESTAMP));
						Element deadlineEle = itemEle.addElement("deadline");
						deadlineEle.setText(CommonUtil.dataConvert(item.getDeadline(), DateType.TIMESTAMP));
						Element itemTypeEle = itemEle.addElement("item_type");
						itemTypeEle.setText(item.getItem_type()+"");
						Element isLoginedEle = itemEle.addElement("isLogined");
						isLoginedEle.setText(item.getIsLogined()==null?"":item.getIsLogined());
					}
				}else{
					ordersElement.remove(orderEle);
				}
			}
		}
		List orderss = ordersElement.elements("order");
		if(orderss== null || orderss.size() <= 0){
			throw new NullPointerException("未找到审核通过的订单");
		}
	}
	
	@Override
	public List<DeptUserZtree> getDeptUserTree(String eid,String pid){
		List<DeptUserZtree> resultlist=new ArrayList<DeptUserZtree>();
		if(StringUtils.isEmpty(eid)){
			return null;
		}
		//第一次查询顶级部门
		if(StringUtils.isEmpty(pid)&&StringUtils.isNotEmpty(eid)){
			UserEntity  topuser=this.findTopUserByEid(eid);
			if(null==topuser){
				return null;
			}
			DeptUserZtree deptuser=new DeptUserZtree();
			deptuser.setId(topuser.getDeptId().getId());
			deptuser.setName(topuser.getDeptId().getName());
			deptuser.setIsParent(true);
			deptuser.setIsUser(false);
			resultlist.add(deptuser);
			return resultlist;
		}
		
		//查询子部门及用户 pid为部门id
		if(StringUtils.isNotEmpty(pid)){
			/**
			 * 1、查询部门
			 * 2、查询用户
			 */
			List<UserEntity> childuserlist=this.userDao.findByUserDeptIdAndEidOrderByCreateTimeAsc(pid,eid);
			if(null!=childuserlist&&childuserlist.size()>0){
				for(UserEntity ue:childuserlist){
					DeptUserZtree deptuser=new DeptUserZtree();
					deptuser.setId(ue.getId());
					deptuser.setName(ue.getRealname());
					deptuser.setIsParent(false);
					deptuser.setIsUser(true);
					deptuser.setpId(pid);
					resultlist.add(deptuser);
				}
			}
			List<InfoDeptEntity> childdeptlist=this.infoDeptDao.findByParentIdOrderByOperDateAsc(pid);
			if(null!=childdeptlist&&childdeptlist.size()>0){
				for(InfoDeptEntity dept:childdeptlist){
					DeptUserZtree deptuser=new DeptUserZtree();
					deptuser.setId(dept.getId());
					deptuser.setName(dept.getName());
					deptuser.setIsParent(true);
					deptuser.setIsUser(false);
					deptuser.setpId(pid);
					resultlist.add(deptuser);
				}
			}
		}
		return resultlist;
	}
}
