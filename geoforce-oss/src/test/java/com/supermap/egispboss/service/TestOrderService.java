/*package com.supermap.egispboss.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispboss.entity.OrderEntity;
import com.supermap.egispboss.entity.OrderItemEntity;
import com.supermap.egispboss.exception.ParameterException;
import com.supermap.egispboss.pojo.BaseOrderInfoList;
import com.supermap.egispboss.pojo.BaseUserInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestOrderService {

	@Resource
	private IUserService userService;
	@Resource
	private IOrderService orderService;
	
	//@Test
	public void testAddOrder(){
		OrderEntity order = new OrderEntity();
		order.setConsultSum(1000);
		
		BaseUserInfo user = userService.findUserById(1+"");
//		order.setUser(user);
//		orderService.add(order);
	}
	
	//@Test
	public void testAddOrderItem(){
		OrderEntity order = new OrderEntity();
		// 创建订单项
		OrderItemEntity orderItem = new OrderItemEntity();
		orderItem.setConsultPrice(1000);
		orderItem.setLimit(1000);
//		orderItem.setOrder(order);
		// 添加订单项
//		order.addOrderItem(orderItem);
		// 创建第二个订单项
		OrderItemEntity orderItem02 = new OrderItemEntity();
		orderItem02.setConsultPrice(1000);
		orderItem02.setLimit(1000);
//		orderItem02.setOrder(order);
		// 添加订单项
//		order.addOrderItem(orderItem02);
		
		order.setConsultSum(orderItem.getConsultPrice()+orderItem02.getConsultPrice());
//		order.setDeadLine(new Date());
		order.setRemarks("添加多个订单项");
//		order.setUser(userService.findUserById(1));
		orderService.add(order,new OrderItemEntity[]{orderItem,orderItem02});
		
		
	}
	
	
	//@Test
	public void deleteOrder(){
		orderService.deleteById(2+"");
	}
	
	//@Test
	public void testQueryById(){
		OrderEntity orderEntity = orderService.queryById(3+"",true);
		System.out.println("orderId : "+orderEntity.getId());
		List<OrderItemEntity> orderItems = orderEntity.getOrderItems();
		for(OrderItemEntity item : orderItems){
			System.out.println("orderItemId : "+item.getId());
		}
		Assert.assertNotNull(orderEntity);
	}
	//@Test
	public void testDeleteOrderItem(){
		try {
			orderService.delelteOrderItem(3+"");
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}


	@Test
	public void testQueryOrderlist(){
		try {
			BaseOrderInfoList orderList = orderService.queryOrderList(null, null, null, 0, 10);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
*/