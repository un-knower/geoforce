package com.supermap.egispportal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.supermap.egispportal.constants.StatusConstants;
//import com.supermap.egispportal.entity.OrderFieldNames;
//import com.supermap.egispportal.exception.ParameterException;
//import com.supermap.egispportal.exception.ParameterException.ExceptionType;
//import com.supermap.egispportal.pojo.BaseAccessInfo;
//import com.supermap.egispportal.pojo.BaseOrderInfo;
//import com.supermap.egispportal.pojo.BaseOrderInfoList;
//import com.supermap.egispportal.pojo.BaseUserInfo;
//import com.supermap.egispportal.service.IOrderService;
//import com.supermap.egispportal.service.IUserService;
import com.supermap.egispportal.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseAccessInfo;
import com.supermap.egispservice.base.pojo.BaseOrderInfo;
import com.supermap.egispservice.base.pojo.BaseOrderInfoList;
import com.supermap.egispservice.base.pojo.BaseUserInfo;
import com.supermap.egispservice.base.pojo.OrderFieldNames;
import com.supermap.egispservice.base.service.IOrderService;
import com.supermap.egispservice.base.service.UserService;

public class OrderServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(OrderServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		try{
			if(CommonUtil.isStringEmpty(method)){
				throw new ParameterException(ExceptionType.NOT_FOUND, "method");
			}
			if("addOrder".equals(method)){
				addOrder(req,resp);
			}else if("queryOrderList".equals(method)){
				queryOrderList(req,resp);
			}else if("queryOrderDetails".equals(method)){
				queryOrderDetails(req,resp);
			}else if("deleteOrder".equals(method)){
				deleteOrder(req,resp);
			}
			
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：deleteOrder</p>
	 * Description：		删除订单
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-9-5 下午03:51:56
	 */
	private void deleteOrder(HttpServletRequest req, HttpServletResponse resp) {
		
		try{
			String orderId = req.getParameter(OrderFieldNames.ORDER_ID);
			if(CommonUtil.isStringEmpty(orderId)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,OrderFieldNames.ORDER_ID);
			}
			IOrderService orderService = (IOrderService) getBean("orderService");
			orderService.deleteById(orderId);
			boolean isDeleteSuccess = true;
			write(req, resp, "删除订单"+(isDeleteSuccess?"成功":"失败"), null, isDeleteSuccess);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：queryOrderDetails</p>
	 * Description：查询订单详情
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-9-5 下午03:40:08
	 */
	private void queryOrderDetails(HttpServletRequest req, HttpServletResponse resp) {
		try{
			String orderId = req.getParameter(OrderFieldNames.ORDER_ID);
			IOrderService orderService = (IOrderService) getBean("orderService");
			BaseOrderInfo orderInfo = orderService.queryOrderDetails(orderId);
			write(req, resp, null, orderInfo, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：queryOrderList</p>
	 * Description：
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-9-5 下午01:44:26
	 */
	private void queryOrderList(HttpServletRequest req, HttpServletResponse resp) {
		String pageNoStr = req.getParameter("pageNo");
		String pageSizeStr = req.getParameter("pageSize");
		String statusStr = req.getParameter("status");
		String orderTypeStr = req.getParameter("orderType");
		try{
			BaseAccessInfo userInfo = getUserInfo();
			int pageSize = 10;
			if(!CommonUtil.isStringEmpty(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
				if(pageSize > 10 || pageSize <= 0){
					pageSize = 10;
				}
			}
			
			int pageNo = 0;
			if(!CommonUtil.isStringEmpty(pageNoStr)){
				pageNo = Integer.parseInt(pageNoStr);
				if(pageNo < 0){
					pageNo = -1;
				}
			}
			int orderType = -1;
			if(!StringUtils.isEmpty(orderTypeStr)){
				orderType = Integer.parseInt(orderTypeStr);
			}
			
			IOrderService orderService = (IOrderService) getBean("orderService");
			BaseOrderInfoList infoList = orderService.queryOrderList(userInfo,statusStr,orderType,pageSize,pageNo);
			write(req, resp, null, infoList, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：addOrder</p>
	 * Description：		添加订单
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-9-5 上午11:08:51
	 */
	private void addOrder(HttpServletRequest req, HttpServletResponse resp) {
		String parameter = req.getParameter("parameter");
		try{
			BaseAccessInfo userInfo = getUserInfo();
			parameter = defaultCharacterConvert(parameter);
			JSONObject paramObj = JSONObject.fromObject(parameter);
			if(CommonUtil.isStringEmpty(parameter)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,"parameter");
			}
			IOrderService orderService = (IOrderService) getBean("orderService");
			if (!(StatusConstants.USER_STATUS_ADV.equals(userInfo.getStatus()) || StatusConstants.USER_STATUS_COMMON
					.equals(userInfo.getStatus()))) {
				UserService userService = (UserService) getBean("userService");
				BaseUserInfo bui = userService.findEbossUserById(userInfo.getUserId());
				throw new Exception("用户["+bui.getEmail()+"]权限不允许，请联系商务");
			}
			String id = orderService.add(paramObj, userInfo);
			write(req, resp, "提交订单成功[id,"+id+"]", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
}
