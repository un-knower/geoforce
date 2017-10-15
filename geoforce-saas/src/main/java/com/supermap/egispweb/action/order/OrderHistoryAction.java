package com.supermap.egispweb.action.order;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.IOrderHistoryService;

//@Controller
//@RequestMapping(value = "/orderHistory")
//@SessionAttributes("user")
public class OrderHistoryAction {
	
	@Autowired
	private IOrderHistoryService orderHistoryService;

	@RequestMapping(value = "/show")
	public String show() {
		return "order/orderHistory";
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getHistoryOrders(String number, String batch,
			String admincode, String address, int page, int rows, String sord,
			HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		return orderHistoryService.getHistoryOrders(number, batch, admincode,
				address, user.getDeptId().getId(), page, rows, sord);
	}
}
