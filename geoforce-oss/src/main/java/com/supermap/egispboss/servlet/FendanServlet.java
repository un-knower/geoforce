package com.supermap.egispboss.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.supermap.egispboss.excel.export.ExportExcel;
import com.supermap.egispboss.excel.export.FendanBean;
import com.supermap.egispboss.util.CommonUtil;
import com.supermap.egispservice.base.entity.DimOrderStatusEntity;
import com.supermap.egispservice.base.pojo.LogisticsResultInfo;
import com.supermap.egispservice.base.service.ILogisticsOrderService;

/**
 * 区划服务
 * @author Administrator
 */
public class FendanServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(FendanServlet.class);
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if(!CommonUtil.isStringEmpty(method)){
			LOGGER.info("## 收到请求[method,"+method+"]");
			if(method.equals("exportLogisticsOrder")){
				exportLogisticsOrder(req,resp);//导出分单数据
			}else if(method.equals("queryLogisticsOrderCount")){
				queryLogisticsOrderCount(req,resp);//查询条件下的分单数量
			}else if(method.equals("deleteLogisticsOrder")){
				deleteLogisticsOrder(req,resp);//删除条件下的分单结果
			}else if(method.equals("queryBatchs")){
				queryBatchs(req,resp);//查询批次
			}else if(method.equals("queryStatus")){
				queryOrderStatus(req,resp);//查询状态列表
			}
		}else{
			write(req, resp, "参数[method]为空", null, false);
		}
	}
	
	/**
	 * 导出所有分单
	 * @Author Juannyoh
	 * 2016-9-18下午4:08:44
	 */
	public void exportLogisticsOrder(HttpServletRequest req, HttpServletResponse resp){
		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String batch=req.getParameter("batchs");//批次号，多个以逗号分割
		String status=req.getParameter("status");//分单状态 0分单成功；1分单失败
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		try {
			if(StringUtils.isEmpty(eid)||StringUtils.isEmpty(parentuserid)){
				write(req, resp, "总账号信息错误", null, false);
			}
			
			ILogisticsOrderService logisticsOrderService=(ILogisticsOrderService) getBean("logisticsOrderService");
			//查询分单信息
			if(!StringUtils.isEmpty(levels)&&!StringUtils.isEmpty(admincode)){
				if(levels.equals("1")){
					admincode=admincode.substring(0, 2);
				}else if(levels.equals("2")){
					admincode=admincode.substring(0, 4);
				}
			}
			
			List<String> childlist=null;
			List<String> batchlist=null;
			if(!StringUtils.isEmpty(childuserids)){
				String[] childs=childuserids.split(",");
				if(null!=childs&&childs.length>0){
					childlist=new ArrayList<String>();
					for(String s:childs){
						childlist.add(s);
					}
				}
			}
			if(!StringUtils.isEmpty(batch)){
				String[] batchs=batch.split(",");
				if(null!=batchs&&batchs.length>0){
					batchlist=new ArrayList<String>();
					for(String s:batchs){
						batchlist.add(s);
					}
				}
			}

			long start=System.currentTimeMillis();
			Map<String, Object> resultmap=logisticsOrderService.queryExportByBatch(childlist, eid, batchlist, status, admincode, -1, 10);
			long end=System.currentTimeMillis();
			LOGGER.info("查询分单用时："+(end-start)/1000);
			
			if(null!=resultmap&&null!=resultmap.get("records")){
				List<LogisticsResultInfo> results=(List<LogisticsResultInfo>) resultmap.get("records");
				if(results!=null&&results.size()>0){
					List<FendanBean> datasets=new ArrayList<FendanBean>();
					for(LogisticsResultInfo order:results){
						FendanBean bean=new FendanBean();
						bean.setAddress(order.getAddress());
						bean.setAreaName(order.getAreaName());
						bean.setBatch(order.getBatch());
						bean.setCity(order.getCity());
						bean.setCounty(order.getCounty());
						bean.setFailReason(getFailReason(order.getSmx(),order.getSmy(),order.getAreaId()));
						bean.setId(order.getId());
						bean.setImportTime(formatDateTimeFromBatch(order.getBatch()));
						bean.setOrderNum(order.getOrderNum());
						bean.setOrderStatus(order.getOrderStatus());
						bean.setProvince(order.getProvince());
						bean.setSmx(order.getSmx());
						bean.setSmy(order.getSmy());
						datasets.add(bean);
					}
					ExportExcel<FendanBean> ex = new ExportExcel<FendanBean>();
					resp.setHeader("Connection", "close");  
					resp.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");  
					 //防止文件名含有中文乱码
					String filedisplay = "分单信息.xls";
		            filedisplay = new String( filedisplay.getBytes("gb2312"), "ISO8859-1" );
			        resp.setHeader("Content-Disposition", "attachment;filename=" + filedisplay); 
					req.setCharacterEncoding("UTF-8");
					resp.setCharacterEncoding("UTF-8");
		            String[] titles={"ID","运单号","订单批次","省","市","区","订单地址","x坐标","y坐标","所属区划","失败原因","分单状态","导入时间"};
		            OutputStream out = resp.getOutputStream();
		            ex.exportFendanExcel("分单信息",titles, datasets, out,"yyyy-MM-dd HH:mm:ss");
					out.close();
				}
			}
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	
	/**
	 * 查询分单数量
	 * @Author Juannyoh
	 * 2016-9-20上午10:34:50
	 */
	public void queryLogisticsOrderCount(HttpServletRequest req, HttpServletResponse resp){
		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String batch=req.getParameter("batchs");//批次号，多个以逗号分割
		String status=req.getParameter("status");//分单状态 0分单成功；1分单失败
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		try {
			if(StringUtils.isEmpty(eid)||StringUtils.isEmpty(parentuserid)){
				write(req, resp, "总账号信息错误", null, false);
			}
			
			ILogisticsOrderService logisticsOrderService=(ILogisticsOrderService) getBean("logisticsOrderService");
			//查询分单信息
			if(!StringUtils.isEmpty(levels)&&!StringUtils.isEmpty(admincode)){
				if(levels.equals("1")){
					admincode=admincode.substring(0, 2);
				}else if(levels.equals("2")){
					admincode=admincode.substring(0, 4);
				}
			}
			
			List<String> childlist=null;
			List<String> batchlist=null;
			if(!StringUtils.isEmpty(childuserids)){
				String[] childs=childuserids.split(",");
				if(null!=childs&&childs.length>0){
					childlist=new ArrayList<String>();
					for(String s:childs){
						childlist.add(s);
					}
				}
			}
			if(!StringUtils.isEmpty(batch)){
				String[] batchs=batch.split(",");
				if(null!=batchs&&batchs.length>0){
					batchlist=new ArrayList<String>();
					for(String s:batchs){
						batchlist.add(s);
					}
				}
			}
			
			int count=logisticsOrderService.queryExportCountByParams(eid, childlist,batchlist, status, admincode);
			write(req, resp, "查询成功", count, true);
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	
	/**
	 * 删除分单
	 * @Author Juannyoh
	 * 2016-9-20上午10:36:29
	 */
	public void deleteLogisticsOrder(HttpServletRequest req, HttpServletResponse resp){
		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String batch=req.getParameter("batchs");//批次号，多个以逗号分割
		String status=req.getParameter("status");//分单状态 0分单成功；1分单失败
		String admincode=req.getParameter("admincode");//admincode编码
		String levels=req.getParameter("levels");
		try {
			if(StringUtils.isEmpty(eid)||StringUtils.isEmpty(parentuserid)){
				write(req, resp, "总账号信息错误", null, false);
			}
			
			ILogisticsOrderService logisticsOrderService=(ILogisticsOrderService) getBean("logisticsOrderService");
			//查询分单信息
			if(!StringUtils.isEmpty(levels)&&!StringUtils.isEmpty(admincode)){
				if(levels.equals("1")){
					admincode=admincode.substring(0, 2);
				}else if(levels.equals("2")){
					admincode=admincode.substring(0, 4);
				}
			}
			
			List<String> childlist=null;
			List<String> batchlist=null;
			if(!StringUtils.isEmpty(childuserids)){
				String[] childs=childuserids.split(",");
				if(null!=childs&&childs.length>0){
					childlist=new ArrayList<String>();
					for(String s:childs){
						childlist.add(s);
					}
				}
			}
			if(!StringUtils.isEmpty(batch)){
				String[] batchs=batch.split(",");
				if(null!=batchs&&batchs.length>0){
					batchlist=new ArrayList<String>();
					for(String s:batchs){
						batchlist.add(s);
					}
				}
			}
			
			int count=logisticsOrderService.deleteOrdersByParams(eid, childlist,batchlist, status, admincode);
			if(count>=0){
				write(req, resp, null, count, true);
			}else{
				write(req, resp, "删除失败", null, false);
			}
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	
/**
 * 查询批次
 * @param req
 * @param resp
 * @Author Juannyoh
 * 2016-9-23下午2:42:44
 */
	public void queryBatchs(HttpServletRequest req, HttpServletResponse resp){
		String parentuserid=req.getParameter("parentuserid");//总账号id
		String eid=req.getParameter("eid");//企业id
		String childuserids=req.getParameter("childuserids");//子账号id，多个以逗号分割
		String batch=req.getParameter("batchs");//批次匹配
		try {
			if(StringUtils.isEmpty(eid)||StringUtils.isEmpty(parentuserid)){
				write(req, resp, "总账号信息错误", null, false);
			}
			
			ILogisticsOrderService logisticsOrderService=(ILogisticsOrderService) getBean("logisticsOrderService");
			
			List<String> childlist=null;
			if(!StringUtils.isEmpty(childuserids)){
				String[] childs=childuserids.split(",");
				if(null!=childs&&childs.length>0){
					childlist=new ArrayList<String>();
					for(String s:childs){
						childlist.add(s);
					}
				}
			}
			List<String>  batchs=logisticsOrderService.queryBatchsByParams(eid, childlist, batch);
			write(req, resp, "查询成功", batchs, true);
		}catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	
	public String formatDateTimeFromBatch(String batch){
		String result="";
		try {
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtils.isEmpty(batch)){
				result=sdf2.format(sdf1.parse(batch));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getFailReason(double smx,double smy,String areaid){
		if(smx==0||smy==0){
			return "地址解析失败";
		}
		if(smx>0&&smy>0&&StringUtils.isEmpty(areaid)){
			return "未匹配到区划";
		}else return "";
	}
	
	/**
	 * 查询订单状态列表
	 * @param req
	 * @param resp
	 * @Author Juannyoh
	 * 2016-10-10下午3:56:19
	 */
	public void queryOrderStatus(HttpServletRequest req, HttpServletResponse resp){
		try {
			ILogisticsOrderService logisticsOrderService=(ILogisticsOrderService) getBean("logisticsOrderService");
			List<DimOrderStatusEntity>  status=logisticsOrderService.queryAllOrderStatus();
			write(req, resp, "查询成功", status, true);
		}catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
}
