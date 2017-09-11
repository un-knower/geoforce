package com.supermap.egispservice.base.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.supermap.egispservice.base.constants.EbossStatusConstants;
import com.supermap.egispservice.base.dao.IRoleDao;
import com.supermap.egispservice.base.dao.IStaffDao;
import com.supermap.egispservice.base.dao.IStaffStatusDao;
import com.supermap.egispservice.base.entity.RoleEntity;
import com.supermap.egispservice.base.entity.StaffEntity;
import com.supermap.egispservice.base.entity.StaffStatusEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseStaffAccessInfo;
import com.supermap.egispservice.base.pojo.BaseStaffDetails;
import com.supermap.egispservice.base.pojo.BaseStaffList;
import com.supermap.egispservice.base.pojo.BaseStaffListItem;
import com.supermap.egispservice.base.pojo.StaffFieldNames;
import com.supermap.egispservice.base.service.IStaffService;
import com.supermap.egispservice.base.util.EncryptionUtil;


@Component("staffService")
public class StaffServiceImpl implements IStaffService {
	private static Logger LOGGER = Logger.getLogger(StaffServiceImpl.class);
	
	@Resource
	private IStaffDao staffDao;
	@Resource
	private IStaffStatusDao staffStatusDao;
	
	@Override
	@Transactional
	public String addStaff(String username, String password, String realName, String position, String sex,
			String mobilePhone, String phone, String email, String department) throws ParameterException {
		StaffEntity se = null;
		try{
			se = this.staffDao.findByUsername(username);
		}catch(Exception e){
		}
		if(null != se){
			throw new ParameterException(ExceptionType.EXISTED_DATA,"username:"+username);
		}
		StaffStatusEntity staffStatus = this.staffStatusDao.findByValue(EbossStatusConstants.STAFF_STATUS_COMMON);
		if(null == staffStatus){
			staffStatus = new StaffStatusEntity();
			staffStatus.setValue(EbossStatusConstants.STAFF_STATUS_COMMON);
			this.staffStatusDao.save(staffStatus);
		}
		se = new StaffEntity();
		se.setStatus(staffStatus);
		se.setUsername(username);
		se.setDepartment(department);
		se.setEmail(email);
		se.setMobilePhone(mobilePhone);
		// 密码加密
		String salt = EncryptionUtil.base64Encode(se.getUsername());
		password = EncryptionUtil.base64Decode(password);
		password = EncryptionUtil.md5Encry(password, salt);
		se.setSalt(salt);
		se.setPassword(password);
		se.setPhone(phone);
		se.setPosition(position);
		se.setRealName(realName);
		se.setSex(sex);
		this.staffDao.save(se);
		return se.getId();
	}

	
	/**
	 * 
	 * <p>Title ：parseEntity</p>
	 * Description：		解析员工数据
	 * @param staffObj
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-14 下午05:05:27
	 */
	private StaffEntity parseEntity(JSONObject staffObj){
		
		return null;
	}
	
	
	

	public IStaffDao getStaffDao() {
		return staffDao;
	}

	public void setStaffDao(IStaffDao staffDao) {
		this.staffDao = staffDao;
	}

	@Override
	@Transactional(readOnly = true)
	public BaseStaffList queryStaffList(String id, String username,String statusStr,int pageNo,int pageSize) throws ParameterException {
		BaseStaffList staffList = null;
		if(!StringUtils.isEmpty(id)){
			StaffEntity se = this.staffDao.findById(id);
			if(null == se){
				throw new ParameterException(ExceptionType.NOT_FOUND,"id:"+id);
			}
			staffList = new BaseStaffList();
			staffList.setCurrentCount(1);
			staffList.setTotal(1);
			BaseStaffListItem item = entity2Item(se);
			staffList.setItems(new BaseStaffListItem[]{item});
			return staffList;
		}
		
		StaffStatusEntity statusEntity = this.staffStatusDao.findByValue(statusStr);
		
		if(pageNo>=0){
			Page<StaffEntity> pageresult=null;
			PageRequest page=new PageRequest(pageNo, pageSize);
			if(statusEntity!=null||!StringUtils.isEmpty(username)){
				pageresult=this.staffDao.findByUsernameOrStatus(username, statusEntity, page);
			}else{
				pageresult=this.staffDao.findAll(page);
			}
			if(pageresult!=null&&pageresult.getContent()!=null){
				staffList = new BaseStaffList();
				BaseStaffListItem[] items = entitys2Items(pageresult.getContent());
				staffList.setTotal((int)pageresult.getTotalElements());
			    staffList.setCurrentCount(pageresult.getContent().size());
				staffList.setItems(items);
			}
		}
		return staffList;
	}
	
	/**
	 * 
	 * <p>Title ：entitys2Items</p>
	 * Description：		将员工实体列表转换成前端查询的列表
	 * @param staffEntitys
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-18 下午03:29:56
	 */
	private BaseStaffListItem[] entitys2Items(List<StaffEntity> staffEntitys){
		BaseStaffListItem[] items = null;
		if(null != staffEntitys){
			items = new BaseStaffListItem[staffEntitys.size()];
			for(int i=0;i<staffEntitys.size();i++){
				items[i] =entity2Item(staffEntitys.get(i));
			}
		}
		return items;
	}

	/**
	 * 
	 * <p>Title ：entity2Item</p>
	 * Description：		将实体数据转换为列表项
	 * @param se
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-18 下午02:47:00
	 */
	private BaseStaffListItem entity2Item(StaffEntity se){
		BaseStaffListItem item = new BaseStaffListItem();
		item.setDepartment(se.getDepartment());
		item.setEmail(se.getEmail());
		item.setGender(se.getSex());
		item.setId(se.getId());
		item.setPosition(se.getPosition());
		item.setRealName(se.getRealName());
		item.setUsername(se.getUsername());
		item.setStatus(se.getStatus().getValue());
		Set<RoleEntity> roles = se.getRoles();
		String[] roleIds = null;
		if(null != roles){
			roleIds = new String[roles.size()];
			Iterator iterator = roles.iterator();
			int index = 0;
			while(iterator.hasNext()){
				RoleEntity re = (RoleEntity) iterator.next();
				roleIds[index++] = re.getId();
			}
			item.setRoleIds(roleIds);
		}
		return item;
	}
	/**
	 * 
	 * <p>Title ：entity2Details</p>
	 * Description：	将员工实体对象转换为前端详细信息对象
	 * @param se
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-18 下午05:49:18
	 */
	private BaseStaffDetails entity2Details(StaffEntity se){
		BaseStaffDetails details = new BaseStaffDetails();
		details.setDepartment(se.getDepartment());
		details.setEmail(se.getEmail());
		details.setGender(se.getSex());
		details.setId(se.getId());
		details.setPosition(se.getPosition());
		details.setRealName(se.getRealName());
		details.setUsername(se.getUsername());
		details.setStatus(se.getStatus().getValue());
		details.setPhone(se.getPhone());
		details.setMobilePhone(se.getMobilePhone());
		return details;
	}
	

	@Override
	@Transactional(readOnly=true)
	public BaseStaffDetails queryStaffDetails(String id) throws ParameterException {
		StaffEntity staffEntity = this.staffDao.findById(id);
		if(null == staffEntity){
			throw new ParameterException(ExceptionType.NOT_FOUND,StaffFieldNames.ID + ":"+id);
		}
		BaseStaffDetails details =  entity2Details(staffEntity);
		assembleRoles(details, staffEntity);
		return details;
	}
	
	private void assembleRoles(BaseStaffDetails details,StaffEntity staffEntity){
		Set<RoleEntity> roles = staffEntity.getRoles();
		if(roles != null && roles.size() > 0){
			int size = roles.size();
			String roleNames[] = new String[size];
			String roleIds[] = new String[size];
			RoleEntity[] roleEntitys = new RoleEntity[size];
			roles.toArray(roleEntitys);
			List<String> privilegeCodes = new ArrayList<String>();
			List<String> privilegeIds = new ArrayList<String>();
			for(int i=0;i<size;i++){
				roleNames[i] = roleEntitys[i].getName();
				roleIds[i] = roleEntitys[i].getId();
				String[] privilegeIdA = roleEntitys[i].getPrivilegeIds();
				String[] privilegeCodeA = roleEntitys[i].getPrivilegeCodes();
				if(null != privilegeIdA &&  null != privilegeCodeA){
					privilegeIds.addAll(Arrays.asList(privilegeIdA));
					privilegeCodes.addAll(Arrays.asList(privilegeCodeA));
				}
			}
			if(privilegeCodes.size() > 0 && privilegeIds.size() > 0){
				String[] ids = new String[privilegeIds.size()];
				String[] codes = new String[privilegeCodes.size()];
				privilegeIds.toArray(ids);
				privilegeCodes.toArray(codes);
				details.setPrivilegeIds(ids);
				details.setPrivilegeCodes(codes);
			}
			details.setRoleNames(roleNames);
			details.setRoleIds(roleIds);
		}
	}

	@Override
	@Transactional
	public void updateStaffDetails(String id, String mobilePhone, String phone, String email,String department,String position, String remarks)
			throws ParameterException {
		StaffEntity se = this.staffDao.findById(id);
		if(null == se){
			throw new ParameterException(ExceptionType.NOT_FOUND,StaffFieldNames.ID+":"+id);
		}
		boolean isUpdated = false;
		// 修改手机
		if(!StringUtils.isEmpty(mobilePhone) && !mobilePhone.equals(se.getMobilePhone())){
			se.setMobilePhone(mobilePhone);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		// 修改固话
		if(!StringUtils.isEmpty(phone) && !phone.equals(se.getPhone())){
			se.setPhone(phone);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		// 修改邮箱
		if(!StringUtils.isEmpty(email) && !email.equals(se.getEmail())){
			se.setEmail(email);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		// 修改备注
		if(!StringUtils.isEmpty(remarks) && !remarks.equals(se.getRemarks())){
			se.setRemarks(remarks);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		// 修改部门
		if(!StringUtils.isEmpty(department) && !department.equals(se.getDepartment())){
			se.setDepartment(department);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		
		
		// 修改职位
		if(!StringUtils.isEmpty(position) && !position.equals(se.getPosition())){
			se.setPosition(position);
			if(!isUpdated){
				isUpdated = true;
			}
		}
		if(!isUpdated){
			throw new ParameterException("未发现需要更新的内容");
		}
		se.setUpdateTime(new Date());
	}

	@Override
	@Transactional
	public void changePassword(String id, String oldPassword,String password) throws ParameterException {
		StaffEntity se = this.staffDao.findById(id);
		if(null == se){
			throw new ParameterException(ExceptionType.NOT_FOUND,StaffFieldNames.ID+":"+id);
		}
		// 检查旧密码
		if(!StringUtils.isEmpty(oldPassword)){
			LOGGER.info("## 旧密码["+oldPassword+"]");
			oldPassword = EncryptionUtil.base64Decode(oldPassword);
			oldPassword = EncryptionUtil.md5Encry(oldPassword, se.getSalt());
			if(!oldPassword.equals(se.getPassword())){
				throw new ParameterException("旧密码不一致");
			}
		}
		
		password = EncryptionUtil.base64Decode(password);
		String salt = se.getSalt();
		password = EncryptionUtil.md5Encry(password, salt);
		se.setPassword(password);
	}
	
	@Resource
	private IRoleDao roleDao;
	
	@Override
	@Transactional
	public void addRoles(String id, String[] roleIds) throws ParameterException {
		StaffEntity se = this.staffDao.findById(id);
		if(null == se){
			throw new ParameterException(ExceptionType.NOT_FOUND,StaffFieldNames.ID+":"+id);
		}
		for(String roleId : roleIds){
			RoleEntity re = roleDao.findById(roleId);
			if(re == null){
				throw new ParameterException(ExceptionType.NOT_FOUND,"roleId:"+roleId);
			}
			se.addRole(re);
		}
		this.staffDao.save(se);
	}

	@Override
	@Transactional
	public void removeRoles(String id, String[] roleIds) throws ParameterException {
		StaffEntity se = this.staffDao.findById(id);
		if (null == se) {
			throw new ParameterException(ExceptionType.NOT_FOUND, StaffFieldNames.ID + ":" + id);
		}
		for (String roleId : roleIds) {
			RoleEntity re = roleDao.findById(roleId);
			if(null != re){
				se.removeRole(re);
			}
		}
		this.staffDao.save(se);

	}

	@Override
	@Transactional
	public void deleteStaff(String id) throws ParameterException {
		StaffEntity se = this.staffDao.findById(id);
		if(null == se){
			throw new ParameterException(ExceptionType.NOT_FOUND,StaffFieldNames.ID+":"+id);
		}
		this.staffDao.delete(id);
		
	}

	@Override
	@Transactional(readOnly=true)
	public BaseStaffAccessInfo login(String username, String password) throws ParameterException {
		StaffEntity se = null;
		try{
			se = this.staffDao.findByUsername(username);
			if(null == se){
				throw new ParameterException("用户名不存在");
			}
		}catch(Exception e){
			throw new ParameterException("用户名不存在");
		}
		
		// 判断用户状态是否允许登录
		if(EbossStatusConstants.STAFF_STATUS_FORBIDDEN.equals(se.getStatus().getValue())){
			throw new ParameterException("用户状态为禁用");
		}
		String salt = se.getSalt();
		String submitPassword = EncryptionUtil.base64Decode(password);
		submitPassword = EncryptionUtil.md5Encry(submitPassword, salt);
		if(!submitPassword.equals(se.getPassword())){
			throw new ParameterException("密码不正确");
		}
		BaseStaffAccessInfo accessInfo = new BaseStaffAccessInfo();
		accessInfo.setId(se.getId());
		accessInfo.setUsername(se.getUsername());
		Map<String,Map<String,String>> roleInfos = se.getRoleInfos();
		if(null == roleInfos){
			throw new ParameterException("员工["+se.getUsername()+"]无权限信息");
		}
		Set<String> keyNames = roleInfos.keySet();
		List<String> roleNames = new ArrayList<String>();
		Map<String,String> permissions = new HashMap<String,String>();
		Iterator iterator = keyNames.iterator();
		while(iterator.hasNext()){
			String keyName = (String) iterator.next();
			roleNames.add(keyName);
			Map<String,String> map = roleInfos.get(keyName);
			if(null != map){
				permissions.putAll(map);
			}
		}
		accessInfo.setRoleNames(roleNames);
		accessInfo.setPermissions(permissions);
		return accessInfo;
	}

	@Override
	@Transactional
	public void addOrRmRoles(String id, String[] roleIds) throws ParameterException {
		StaffEntity se = this.staffDao.findById(id);
		if(null == se){
			throw new ParameterException(ExceptionType.NOT_FOUND,StaffFieldNames.ID+":"+id);
		}
		se.clearRoles();
		for(String roleId : roleIds){
			RoleEntity re = this.roleDao.findById(roleId);
			if(null != re){
				se.addRole(re);
			}
		}
		this.staffDao.save(se);
	}
	
	
}
