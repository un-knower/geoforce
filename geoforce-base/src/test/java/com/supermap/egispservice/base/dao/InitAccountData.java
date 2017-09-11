package com.supermap.egispservice.base.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.entity.ComEntity;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.OrderEntity;
import com.supermap.egispservice.base.entity.OrderItemsEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.util.Md5Util;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class InitAccountData extends TestCase {
	@Autowired
	private UserDao userDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemsDao orderItemsDao;
	@Autowired
	private ServiceModuleDao serviceModuleDao;
	@Autowired
	private ComDao comDao;
	@Autowired
	private InfoDeptDao infoDeptDao;
	@Autowired
	private UserRoleMapDao userRoleMapDao;
	
	/**
	 * 1.创建公司
	 * 2.创建用户
	 * 3.创建部门
	 * 4.修改用户部门
	 * 5.创建用户订单
	 * 6.创建用户订单对应的菜单关系
	 */
	//1
	@Test
	public void testCreateAccount(){
		String addr="上海市青浦区赵巷镇崧辉路100号";
		String email="email@uc56.com";
		String name="优速快递";
		String phone="400-111-1119";
		String mobilephone="13800138000";
		String remark="备注";
		Date now=new Date();
		
		String code="1000";
		String headerName="姓名？";
		String headerPhone="13800138000";
		String zipCode="201703";
		
		String fax="021-31106666";
		String username="uc56_test";
		String pass="111111";
		Character sex='m';
		
		BigDecimal consultSum=new BigDecimal("10000");
		BigDecimal totalSum=new BigDecimal("10000");
		BigDecimal consultPrice=new BigDecimal("10000");
		Date deadline= new DateTime(2115,10,4,14,41,42).toDate();
		Integer useLimit=10000;
		
		List<String> moduleidList=new ArrayList<String>();
		moduleidList.add("40288e9f48a123e80148a1240e330004");
		moduleidList.add("40288e9f48448d8a0148448d90720003");
		moduleidList.add("40288e9f48448d8a0148448d90770004");
		moduleidList.add("40288e9f48a123e80148a1240e210001");
		moduleidList.add("40288e9f48a123e80148a1240e2b0003");
		
		//1.创建公司
		ComEntity com=testSaveCOM(addr, email, name, phone, remark, now);
		//2.先创建用户
		UserEntity user=testSaveUser(addr, now, com, email, fax, mobilephone, pass, name, remark, sex, phone, username, zipCode);
		//3.再创建部门
		InfoDeptEntity dept=testSaveDept(addr, code,user.getId(), headerName, headerPhone, name, now, mobilephone, zipCode);
		//4 修改用户部门
		user.setDeptId(dept);
		userDao.save(user);
		//5
		OrderEntity order=testSaveOrder(remark,consultSum, com.getId(), now, totalSum, user.getId());
		//6
		testSaveOrderItems( consultPrice, deadline, moduleidList, order.getId(), useLimit, now);
	}
	
	public ComEntity testSaveCOM(String addr,String email,String name,String phone,String remark,Date now){
		ComEntity o=new ComEntity();
		o.setAddress(addr);
		o.setCreateTime(now);
		o.setEmail(email);
		o.setName(name);
		o.setPhone(phone);
		o.setRemark(remark);
		o.setStatusId((byte)1);
		o.setUpdateTime(now);
		return comDao.save(o);
	}
	
	//测试部门
	public InfoDeptEntity testSaveDept(String addr,String code,String createUserid,String headerName,String headerPhone,String name,Date now,String mobilephone,String zipCode){
		InfoDeptEntity o=new InfoDeptEntity();
		o.setAddress(addr);
		o.setCode(code);
		o.setCreateUserid(createUserid);
		o.setHeadName(headerName);
		o.setHeadPhone(headerPhone);
		o.setName(name);
		o.setOperDate(now);
		o.setPhone(mobilephone);
		o.setType((byte)1);
		o.setZipcode(zipCode);
		//o.setParentId("40288e9f488187790148818781ea0000");
		return infoDeptDao.save(o);
		
	}
		
	public UserEntity testSaveUser(String addr,Date now,ComEntity com,String email,String fax,String mobilephone,String pass,String realname,String remark,Character sex,String phone,String username,String zipCode){
		UserEntity u=new UserEntity();
		u.setAddress(addr);
		u.setCreateTime(now);
		//u.setCreateUser()
		//u.setDeptId(dept);
		u.setEid(com);
		u.setEmail(email);
		u.setFax(fax);
		u.setMobilephone(mobilephone);
		u.setPassword(Md5Util.md5(pass));
		u.setRealname(realname);
		u.setRemark(remark);
		u.setSex(sex);//男人
		u.setSourceId((byte)2);//门户注册
		u.setStratusId((byte)1);//正常
		u.setTelephone(phone);
		u.setUpdateTime(now);
		u.setUsername(username);
		u.setZipCode(zipCode);
		return userDao.save(u);
	}
	
	public OrderEntity testSaveOrder(String remark,BigDecimal consultSum,String eid,Date now,BigDecimal totalSum,String userid){
		OrderEntity o=new OrderEntity();
		/*o.setAuditRemark(remark);
		o.setConsultSum(consultSum);
		o.setEid(eid);
		o.setRemark(remark);
		o.setStatusId((byte)1);//激活
		o.setSubmitTime(now);
		o.setTotalSum(totalSum);
		o.setUpdateTime(now);
		o.setUserId(userid);*/
		return orderDao.save(o);
	}

	public void testSaveOrderItems(BigDecimal consultPrice,Date deadline,List<String> moduleidList,String orderid,Integer useLimit,Date now){
		for (String moduleid : moduleidList) {
			OrderItemsEntity o=new OrderItemsEntity();
			o.setConsultPrice(consultPrice);
			o.setDeadline(deadline);
			/*o.setModuleId(moduleid);
			o.setOrderId(orderid);*/
			o.setUseLimit(useLimit);
			o.setUseTime(now);
			orderItemsDao.save(o);
		}
	}
	
}
