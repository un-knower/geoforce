package com.supermap.egispservice.base.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.AppkeyEntity;
import com.supermap.egispservice.base.entity.ComEntity;
import com.supermap.egispservice.base.entity.MenuVO;
import com.supermap.egispservice.base.entity.OrderEntity;
import com.supermap.egispservice.base.entity.OrderItemsEntity;
import com.supermap.egispservice.base.entity.RoleVO;
import com.supermap.egispservice.base.entity.SysDefaultCityEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.entity.UserStatusEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BaseAccessInfo;
import com.supermap.egispservice.base.pojo.BaseUserInfo;
import com.supermap.egispservice.base.pojo.BaseUserListPkg;
import com.supermap.egispservice.base.pojo.DeptUserZtree;


/**
 * 用户业务接口
 */
public interface UserService {
    
	/**
	 * 更新用户登录标志
	 * @param user
	 */
	boolean isLogined(String userid);
    /**
     * 保存用户
     * @param user
     */
	Map<String, Object> saveUser(UserEntity user,String password);
    
    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    UserEntity findUserById(String id);
    
    /**
     * 更新用户
     * @param user
     */
    void updateUser(UserEntity user);
    
    /**
     * 根据ID删除用户
     * @param id
     */
    void deleteUserById(String id);
    /**
     * 分页获取用户信息
     */
    public Map<String, Object> getUsers(final String userid,final String eid,final String deptId,final byte sourceId,final String realname,final String mobilephone,final Date startTime,final Date endTime ,int pageNumber, int pageSize,String sortType);
    /**
     * 获取用户菜单
     */
    public List<MenuVO> getMenu(String userid,String eid,String deptId,Byte sourceId);
    /**
     * 获取用户绑定的角色列表信息
     */
	List<RoleVO> getCheckedRole(String currentUserId ,String lineUserId,String eid,String deptId);
	 /**
     * 查找部门下所有用户
     */
	List<UserEntity> getUsersByDept(String deptId);
	
	public UserEntity findByUsername(String username,String eid);

	/**
	 * 修改密码
	 */
	boolean updatePass(UserEntity user, String oldPass, String newPass);
	/**
	 * 
	 * <p>Title ：isComanyAvaliable</p>
	 * Description：		根据企业ID判断企业是否有效
	 * @param eid
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-20 下午02:22:10
	 */
	public boolean isComanyAvaliable(String eid);
	
	
	/**
	 * 
	 * <p>Title ：updateOrderItemLoginStatus</p>
	 * Description：	更新订单状态登录状态
	 * @param id
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-1-19 下午02:03:30
	 */
	public boolean updateOrderItemLoginStatus(String id);
	
	/**
	 * 
	 * <p>Title ：initUserInfo</p>
	 * Description：
	 * @param userEntity
	 * @param orders
	 * @param orderItems
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-5-16 下午01:23:55
	 */
	public String initUserInfoOrOrders(UserEntity userEntity, List<OrderEntity> orders, List<OrderItemsEntity> orderItems,String mainModuleId);
	
	/**
	 * 
	 * <p>Title ：initUserInfo</p>
	 * Description：	初始化用户信息，用于在离线部署，当用户导入许可，服务端需要对用户信息进行初始化
	 * @param userEntity
	 * @param orders
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-4-27 上午11:29:27
	 */
	public boolean initUserInfo(UserEntity userEntity,List<OrderEntity> orders,List<OrderItemsEntity> orderItems);
	
	/**
	 * 
	 * <p>Title ：isOfflineFirstRun</p>
	 * Description：判断是否为离线部署用户第一次登陆
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-5-26 下午02:24:34
	 */
	public boolean isOfflineFirstRun();
	
	/**
	 * 根据用户id查找订单表中订单状态是“正式审核通过”的订单数量
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-7-29下午2:10:48
	 */
	public int findCountByUserStat(String userid);
	
	/**
	 * 只查询当前部门下的用户，不去查下级
	 * @param deptId
	 * @return
	 * @Author Juannyoh
	 * 2015-7-29下午4:31:58
	 */
	List<UserEntity> getUsersByCurrentDept(String deptId);
	
	/**
	 * 保存用户默认城市
	 * @param s
	 * @return
	 * @Author Juannyoh
	 * 2015-11-23下午3:30:10
	 */
	public SysDefaultCityEntity saveUserDefaultCity(SysDefaultCityEntity s);
	
	/**
	 * 根据用户id查找默认城市
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-23下午3:30:32
	 */
	public SysDefaultCityEntity findUserDefaultCity(String userid);
	
	/**
	 * 用来判断在设置默认城市升级之后的登录标志
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-12-7下午4:06:11
	 */
	boolean isTempLogined(String userid);
	
	/**
	 * 根据企业ID查找企业的顶级用户、即所谓的总账号
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2016-6-2下午2:13:54
	 */
	public UserEntity findTopUserByEid(String eid);
	
	/**
	 * 根据企业ID查找企业下的所有用户名称
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2016-6-13下午5:16:07
	 */
	public Map<String,String> findAllUserByEid(String eid);
	
	
	
	/**
	 * 查找用户是否阅读最新的日志
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2016-8-12上午9:44:28
	 */
	public boolean findIfUserReadLastLogs(String userid);
	
	
	/**
	 * 保存最近的阅读记录
	 * @param userlog
	 * @Author Juannyoh
	 * 2016-8-12上午9:45:58
	 */
	public void saveSysUpdateLogUser(String userid);
	
	
	/**
	 * --------------EBOSS接口
	 */
	
	/**
     * 保存用户
     * @param user
     */
    public void saveUser(UserEntity user,ComEntity ce) throws ParameterException;
    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    BaseUserInfo findEbossUserById(String id);
    
    /**
     * 运营支撑  更新用户状态
     * @param id
     * @param status
     * @param remarks
     * @Author Juannyoh
     * 2016-9-2上午11:22:30
     */
    public void updateUserStatus(String id,UserStatusEntity status ,String remarks);
	
    public void addAppKey(String userId,AppkeyEntity appKey);
    
    public void deleteAppKeyById(String id);
    
    public AppkeyEntity queryAppKeyById(String id);
    
    /**
     * 运营支撑判断用户是否存在
     * @param id
     * @return
     * @Author Juannyoh
     * 2016-9-2上午11:22:51
     */
    public boolean existUser(String id);
    
    /**
     * 运营支撑查询所有用户信息
     * @Author Juannyoh
     * 2016-9-2上午11:23:04
     */
    public BaseUserListPkg query(String id,String info,UserStatusEntity status,int pageNo,int pageSize,String admincode,String businesstype,Date btime,Date etime)throws ParameterException;
    
    public void exsitUsernameOrEmail(String username,String email) throws ParameterException;

    /**
     * ---------------------Portal接口
     */
    
    /**
     * 注册
     * @param user
     * @param ce
     * @throws ParameterException
     * @Author Juannyoh
     * 2016-8-29下午4:05:35
     */
    public void regist(UserEntity user , ComEntity ce)throws ParameterException;

    /**
     * 门户修改用户信息
     * @Author Juannyoh
     * 2016-9-2上午11:23:24
     */
    public void updateUserInfo(String id, String mobilePhone, String phone, String fax, String zipcode, String address,
			String companyAddres, String companyEmail, String companyPhone, String remarks,String qq,String realName,String companyName,String admincode,String adminname,String business,String businessremark) throws ParameterException;
    
    public void changePassword(String id,String password,String oldPassword)throws ParameterException;
    
    public BaseAccessInfo queryByName(String username);
    
    public BaseAccessInfo login(String loginInfo,String password) throws ParameterException;
    
    public String licenseService(String id)throws ParameterException;
    
    /**
     * 部门用户树查找
     * @param eid
     * @param pid
     * @return
     * @Author Juannyoh
     * 2016-10-24下午4:12:37
     */
    public List<DeptUserZtree> getDeptUserTree(String eid,String pid);
    
}