package com.supermap.egispboss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.exception.ParameterException.ExceptionType;
import com.supermap.egispservice.base.pojo.BaseOrderInfo;
import com.supermap.egispservice.base.pojo.BaseOrderInfoList;
import com.supermap.egispservice.base.pojo.OrderFieldNames;
import com.supermap.egispservice.base.service.IOrderService;

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
			if(!CommonUtil.isStringEmpty(method)){
				if("addOrder".equals(method)){
					addOrder(req,resp);
				}else if("queryOrderDetails".equals(method)){
					queryOrderDetails(req,resp);
				}else if("updateOrder".equals(method)){
					updateOrderDetails(req,resp);
				}else if("updateOrderItem".equals(method)){
					updateOrderItem(req,resp);
				}else if("deleteOrder".equals(method)){
					deleteOrder(req,resp);
				}else if("deleteOrderItem".equals(method)){
					deleteOrderItem(req,resp);
				}else if("auditOrder".equals(method)){
					auditOrder(req,resp);
				}else if("queryOrderList".equals(method)){
					queryOrderList(req,resp);
				}
			}else{
				throw new ParameterException("参数[method]不允许为空");
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：queryOrderList</p>
	 * Description：查询订单列表
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-18 上午10:12:03
	 */
	private void queryOrderList(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(OrderFieldNames.ORDER_ID);
		String info = req.getParameter("info");
		String status = req.getParameter(OrderFieldNames.ORDER_STATUS);
		String pageSizeStr = req.getParameter("pageSize");
		String pageNoStr = req.getParameter("pageNo");
		try{
			int pageSize = 10;
			if(!CommonUtil.isStringEmpty(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
			}
			int pageNo = 0;
			if(!CommonUtil.isStringEmpty(pageNoStr)){
				pageNo = Integer.parseInt(pageNoStr);
			}
			IOrderService orderService = (IOrderService) getBean("orderService");
			BaseOrderInfoList orderList = orderService.queryOrderList(idStr, info, status, pageNo, pageSize);
			write(req, resp, null, orderList, true);
			
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
		
	}
	
	

	/**
	 * 
	 * <p>Title ：auditOrder</p>
	 * Description：	订单审核
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-13 下午03:52:39
	 */
	private void auditOrder(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(OrderFieldNames.ORDER_ID);
		String statusStr = req.getParameter(OrderFieldNames.ORDER_STATUS);
		String auditRemarks = req.getParameter(OrderFieldNames.AUDIT_REMARKS);
		try{
			if(!CommonUtil.isStringEmpty(auditRemarks)){
				auditRemarks = defaultCharacterConvert(auditRemarks);
			}
			String checkResult = CommonUtil.checkRequredParam(new String[]{
					OrderFieldNames.ORDER_ID,
					OrderFieldNames.ORDER_STATUS
			}, new String[]{
				idStr,
				statusStr
			});
			
			if(!CommonUtil.isStringEmpty(checkResult)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED, checkResult);
			}
			IOrderService orderService = (IOrderService) getBean("orderService");
			orderService.auditOrder(idStr, statusStr, auditRemarks);
			write(req, resp, "审核成功", null, true);
			
		}catch(Exception e){
			write(req, resp, e.getMessage(),null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：deleteOrderItem</p>
	 * Description：删除订单项
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-13 上午11:30:00
	 */
	private void deleteOrderItem(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(OrderFieldNames.ORDER_ID);
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,OrderFieldNames.ORDER_ID);
			}
			IOrderService orderService = (IOrderService) getBean("orderService");
			orderService.delelteOrderItem(idStr);
			write(req, resp, "删除订单项成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, true);
			LOGGER.error(e.getMessage(), e);
		}
		;
	}

	/**
	 * 
	 * <p>Title ：deleteOrder</p>
	 * Description：根据ID删除订单
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-13 上午10:55:32
	 */
	private void deleteOrder(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(OrderFieldNames.ORDER_ID);
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,OrderFieldNames.ORDER_ID);
			}
			IOrderService orderService = (IOrderService) getBean("orderService");
			orderService.deleteById(idStr);
			write(req, resp, "删除订单成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：updateOrderItem</p>
	 * Description：	修改订单项
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-13 上午10:21:09
	 */
	private void updateOrderItem(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(OrderFieldNames.ORDER_ID);
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,OrderFieldNames.ORDER_ID);
			}
			String limitStr = req.getParameter(OrderFieldNames.USE_LIMIT);
			String consultPrice = req.getParameter(OrderFieldNames.CONSULT_PRICE);
			String startUseTime = req.getParameter(OrderFieldNames.USETIME);
			String endUseTime = req.getParameter(OrderFieldNames.DEADLINE);
			
			IOrderService orderService = (IOrderService) getBean("orderService");
			orderService.updateOrderItem(idStr, limitStr, consultPrice, startUseTime, endUseTime);
			write(req, resp, "更新订单项成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		
	}

	/**
	 * 
	 * <p>Title ：updateOrderDetails</p>
	 * Description：	修改订单信息
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-13 上午09:24:54
	 */
	private void updateOrderDetails(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(OrderFieldNames.ORDER_ID);
		try{
			if(CommonUtil.isStringEmpty(idStr)){
				throw new ParameterException(ExceptionType.NULL_NO_NEED,OrderFieldNames.ORDER_ID);
			}
			String consultPrice = req.getParameter(OrderFieldNames.CONSULT_PRICE);
			String remarks = req.getParameter(OrderFieldNames.REMARKS);
			if(!CommonUtil.isStringEmpty(remarks)){
				remarks = defaultCharacterConvert(remarks);
			}
			
			IOrderService orderService = (IOrderService) getBean("orderService");
			orderService.updateOrderDetails(idStr, consultPrice, remarks);
			write(req, resp, "更新订单信息成功", null, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
		}
		
	}


	/**
	 * 
	 * <p>Title ：queryOrderDetails</p>
	 * Description：		查询订单详情
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-12 下午04:24:48
	 */
	private void queryOrderDetails(HttpServletRequest req, HttpServletResponse resp) {
		String idStr = req.getParameter(OrderFieldNames.ORDER_ID);
		try{
			if(!CommonUtil.isStringEmpty(idStr)){
				IOrderService orderService = (IOrderService) getBean("orderService");
				BaseOrderInfo boi = orderService.queryOrderDetails(idStr);
				if(null != boi){
					write(req, resp,null , boi, true);
				}else{
					throw new ParameterException(ExceptionType.NOT_FOUND,OrderFieldNames.ORDER_ID+":"+idStr);
				}
			}else{
				throw new ParameterException(ExceptionType.NULL_NO_NEED,OrderFieldNames.ORDER_ID);
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
		}
	}


	/**
	 * 
	 * <p>Title ：addOrder</p>
	 * Description：	创建订单
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-8-12 上午11:06:06
	 */
	private void addOrder(HttpServletRequest req, HttpServletResponse resp) {
		String parameter = req.getParameter("parameter");
		try{
			if(!CommonUtil.isStringEmpty(parameter)){
				parameter = defaultCharacterConvert(parameter);
				IOrderService orderService = (IOrderService) getBean("orderService");
				String id = orderService.add(JSONObject.fromObject(parameter));
				write(req, resp, "添加订单[id,"+id+"]成功", null, true);
			}else{
				throw new ParameterException(ParameterException.ExceptionType.NULL_NO_NEED,"parameter");
			}
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}
