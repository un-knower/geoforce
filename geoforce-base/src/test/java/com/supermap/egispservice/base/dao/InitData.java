package com.supermap.egispservice.base.dao;

import java.math.BigDecimal;
import java.util.Date;

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
import com.supermap.egispservice.base.entity.ServiceModuleEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.entity.UserRoleMapEntity;
import com.supermap.egispservice.base.util.Md5Util;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class InitData extends TestCase {
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
	
	@Test
	public void testSaveUser(){
		for (int i = 0; i < 10; i++) {
			UserEntity u=new UserEntity();
			u.setAddress("国青岛海尔路1号海尔工业园内");
			Date d=new Date(System.currentTimeMillis());
			u.setCreateTime(d);
			//u.setCreateUser()
			//u.setDeptId()
			ComEntity c=new ComEntity();
			c.setId("40288e9f48afda050148afda0fc60000");
			u.setEid(c);
			u.setEmail("9999@haier.com");
			u.setFax("4006-999-999");
			u.setMobilephone("13800138000");
			u.setPassword(Md5Util.md5("11111"));
			u.setRealname("海尔集团");
			u.setRemark("备注测试");
			u.setSex('m');//男人
			u.setSourceId((byte)2);//门户注册
			u.setStratusId((byte)1);//正常
			u.setTelephone("4006-999-999");
			u.setUpdateTime(d);
			u.setUsername("haier");
			u.setZipCode("266101");
			userDao.save(u);
		}
		
	}
	
	@Test
	public void testSaveServiceModel(){
		for (int i = 0; i < 1; i++) {
			ServiceModuleEntity o=new ServiceModuleEntity();
			
			
			/*o.setName("司机管理");
			o.setPid("40288e9f48448d8a0148448d90720003");
			o.setCode("0203");
			o.setUrl("#");//菜单图标，暂用这个字段
			o.setPrice(new BigDecimal(900));
			o.setRefUrl("#");
			o.setStatusId((byte)1);
			o.setUpdateTime(new Date());
			o.setAddTime(new Date());
			o.setUseLimit(10000);
			o.setIconUrl("fa-list");*/
			serviceModuleDao.save(o);
		}
		
	}
	
	@Test
	public void testAddMenuForUser(){
		//1
		/*ServiceModuleEntity o=new ServiceModuleEntity();
		o.setName("日志管理");
		o.setPid("40288e9f4844872a014844872fd50000");
		o.setCode("9904");
		o.setUrl("#");//菜单图标，暂用这个字段
		
		o.setPrice(new BigDecimal(900));
		o.setRefUrl("#");
		o.setStatusId((byte)1);
		o.setUpdateTime(new Date());
		o.setAddTime(new Date());
		o.setUseLimit(10000);
		o.setIconUrl("fa-list");
		serviceModuleDao.save(o);
		//2
		OrderItemsEntity oi=new OrderItemsEntity();
		oi.setConsultPrice(new BigDecimal(8000));
		oi.setDeadline(new DateTime(2115,10,4,14,41,42).toDate());
		oi.setModuleId(o.getId());
		oi.setOrderId("40288e9f48447e400148447e4ac80000");
		oi.setUseLimit(9000);
		oi.setUseTime(new Date());*/
//		orderItemsDao.save(oi);
		
	}
	@Test
	public void testSaveOrder(){
		OrderEntity o=new OrderEntity();
		/*o.setAuditRemark("审核备注");
		o.setConsultSum(new BigDecimal(8000));
		o.setEid("40288e9f48afda050148afda0fc60000");
		o.setRemark("订单备注");
		o.setStatusId((byte)1);//激活
		o.setSubmitTime(new Date());
		o.setTotalSum(new BigDecimal(9000));
		o.setUpdateTime(new Date());
		o.setUserId("40288e9f48afdff00148afdffa990000");*/
		orderDao.save(o);
	}
	@Test
	public void testSaveOrderItems(){
			OrderItemsEntity o=new OrderItemsEntity();
			o.setConsultPrice(new BigDecimal(8000));
			o.setDeadline(new DateTime(2115,10,4,14,41,42).toDate());
			
			
			/*o.setModuleId("40288e9f48a123e80148a1240e330004");
			
			
			o.setOrderId("40288e9f48afe5ee0148afe5f84a0000");*/
			o.setUseLimit(9000);
			o.setUseTime(new Date());
			orderItemsDao.save(o);
		
	}
	//测试公司
	@Test
	public void testSaveCOM(){
		ComEntity o=new ComEntity();
		o.setAddress("国青岛海尔路1号海尔工业园内");
		o.setCreateTime(new Date());
		o.setEmail("9999@haier.com");
		o.setName("海尔集团");
		o.setPhone("4006-999-999");
		o.setRemark("海尔账号");
		o.setStatusId((byte)1);
		o.setUpdateTime(new Date());
		
		comDao.save(o);
		
	}
	//测试部门
	@Test
	public void testSaveDept(){
		InfoDeptEntity o=new InfoDeptEntity();
		o.setAddress("国青岛海尔路1号海尔工业园内");
		o.setCode("1001002");
		o.setCreateUserid("40288e9f48afdff00148afdffa990000");
		o.setHeadName("boss？");
		o.setHeadPhone("13800138002");
		o.setName("海尔集团");
		o.setOperDate(new Date());
		o.setPhone("13800138002");
		o.setType((byte)1);
		o.setZipcode("266101");
		//o.setParentId("40288e9f488187790148818781ea0000");
		infoDeptDao.save(o);
		
	}
	//用户角色关联
	@Test
	public void testSaveUserRoleMap(){
		UserRoleMapEntity entity=new UserRoleMapEntity();
		entity.setRoleId("40288e9f4877e2ad014877e2b6ed0000");
		entity.setUserId("40288e9f483f48e501483f48eb060000");
		userRoleMapDao.save(entity);
	}
}
