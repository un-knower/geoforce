package com.supermap.egispweb.action.lbs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.lbs.service.DataworkService;
import com.supermap.egispservice.lbs.util.Pagination;
import com.supermap.egispweb.common.Constant;
import com.supermap.egispweb.pojo.PageBean;
import com.supermap.egispweb.util.DateUtil;
import com.supermap.egispweb.util.StringUtil;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.DataWordbook;

public class BaseAction{
	
	@Autowired
	protected DataworkService dataworkService;
	
	Pagination page;
	
	static Logger logger = Logger.getLogger(BaseAction.class.getName());
	
	/**
	 * 
	* 方法名: getUserSession
	* 描述: 获取session中的用户信息
	* @param session
	* @return
	 */
	public UserEntity getUserSession(HttpSession session){

		UserEntity user = (UserEntity)session.getAttribute("user");
		return user;
	}
	/**
	 * 按接口返回格式封装pageBean
	* @Title: pageToPageBean
	* @return
	* String
	* @throws
	 */
	protected PageBean pageToPageBean(Page page,List list){
		PageBean bean = new PageBean();
		try {
			bean.setPage(page.getCurrentPageNum());
			bean.setRows(page.getPageSize());
			bean.setRecord((long)page.getTotalNum());
			bean.setTotal((long)page.getPageNum());
			bean.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	/**
	 * 得到page对象
	 * @param request
	 * @return
	 */
	protected Pagination getPage(HttpServletRequest request) {
		page = new Pagination();
		
		String size = request.getParameter("rows");
		if(StringUtils.isBlank(size) || !StringUtil.isInteger(size)){
		}else {
			int pageSize = Integer.parseInt(size);
			if(pageSize>Constant.MAX_PAGE_SIZE){
				page.setPageSize(Constant.MAX_PAGE_SIZE);//每页最多50条记录
			}else if(pageSize <= 0){
				page.setPageSize(1);
			}else {
				page.setPageSize(pageSize);
			}
		}
		String curPage = request.getParameter("page");
		if (StringUtils.isBlank(curPage) || !StringUtil.isInteger(curPage)) {
		}else{
			int currentPage = Integer.parseInt(curPage);
			if(currentPage <= 0)
				currentPage = 1;
			page.setPageNo(currentPage);
		}
		return page;
	}
	/**
	 * 计算两点之间距离
	 * 
	 * @param _lat1
	 *            - start纬度
	 * @param _lon1
	 *            - start经度
	 * @param _lat2
	 *            - end纬度
	 * @param _lon2
	 *            - end经度
	 * @return km(四舍五入)
	 */
	protected  double getDistance(double _lat1, double _lon1, double _lat2,double _lon2) {
		double lat1 = (Math.PI / 180) * _lat1;
		double lat2 = (Math.PI / 180) * _lat2;

		double lon1 = (Math.PI / 180) * _lon1;
		double lon2 = (Math.PI / 180) * _lon2;

		// 地球半径
		double R = 6378.1;

		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.cos(lon2 - lon1))
				* R;

		return new BigDecimal(d).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 写入客户端
	 * @param response
	 * @param json
	 */
	protected void printOut(HttpServletResponse response,String json) {
		try {
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(json); 
			out.flush();
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * 加载报警类型code
	* @Title: loadTypeIds
	* @param alarmName 类型 fence表示围栏 speed超速 line线路
	* @return
	* List<String>
	* @throws
	 */
	public List<String> loadTypeCodes(String alarmCode,HashMap<String, Object> hm){
		List<String> typeCode = new ArrayList<String>();
		
		try {
			if(StringUtils.isNotBlank(alarmCode)){
				typeCode.add(alarmCode);
				return typeCode;
			}
			
			List<com.supermap.egispservice.lbs.entity.DataWordbook> list = this.dataworkService.getDataWordbookList(hm);
			
			if(list == null || list.size()==0){
				return null;
			}
			for (int i = 0; i < list.size(); i++) {
				com.supermap.egispservice.lbs.entity.DataWordbook dataWordbook = list.get(i);
				typeCode.add(dataWordbook.getCode());
			}
			
		} catch (Exception e) {}
		return typeCode;
	}
	
	/**
	 * poi 创建工作薄
	* @Title: createWorkbook
	* @param response
	* @param fileName excel名称
	* @param target excel模板的路径
	* @return
	* HSSFWorkbook
	* @throws
	 */
	public HSSFWorkbook createWorkbook(HttpServletRequest request,HttpServletResponse response,String filename,String targetname){
		HSSFWorkbook workbook = null;
		try {
			filename = new String(filename.getBytes("gb2312"),"ISO8859-1");
			response.setHeader("Content-disposition","attachment; filename=" +filename);
			response.setContentType("application/msexcel");
			
			String directory = request.getSession().getServletContext().getRealPath("resources/excelModel/");

			String target = directory + File.separator + targetname;
			workbook  = new HSSFWorkbook(new FileInputStream(target));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}
	/**
	 * excel基本样式
	* @Title: getExcelStyle
	* @param workbook
	* @return
	* HSSFCellStyle
	* @throws
	 */
	public HSSFCellStyle getExcelStyle(HSSFWorkbook workbook,Short color,Short BoldWeight){
		/**设置样式*/
		//设置字体;    
        HSSFFont font = workbook.createFont();
        //设置字体大小;    
        font.setFontHeightInPoints((short) 12);
        //设置字体名字;    
        font.setFontName("宋体");
        if(BoldWeight != null){        	
        	font.setBoldweight(BoldWeight.shortValue());
        }
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        //设置自动换行;    
        style.setWrapText(false);
        //字体居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
        style.setBorderBottom(HSSFColor.AQUA.index);
        style.setBorderTop(HSSFColor.AQUA.index);
        style.setBorderLeft(HSSFColor.AQUA.index);
        style.setBorderRight(HSSFColor.AQUA.index);
        if(color != null){
        	style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(color.shortValue());
        }
        return style;
	}
	/**
	 * poi 根据行创建cell
	* @Title: createCell
	* @param row
	* @param cell
	* @param style
	* @param index
	* @param value
	* @return
	* HSSFCell
	* @throws
	 */
	public HSSFCell createCell(HSSFRow row,HSSFCell cell,HSSFCellStyle style,int index,Object value){
		
        cell = row.createCell(index);
        if(value instanceof Double){//数字（金额）
        	cell.setCellValue((Double)(value));
        }else if(value instanceof Integer){
        	cell.setCellValue((Integer)(value));
        }else{
        	cell.setCellValue((String)(value));
        }
        cell.setCellStyle(style);
        return cell;
	}
	
	/**
	 * 导出excel
	 * @param list
	 * @param filename
	 * @return
	 */
	public boolean exportExcel(List<?> list,String filename,String targetFileName, String startDate,HttpServletResponse response,HttpServletRequest request){
		try {
			OutputStream os = response.getOutputStream();
			response.reset();
			//String filename = getText("report.wlbjbb")+System.currentTimeMillis()+".xls";
			String targetname = targetFileName+".xls";
			HSSFWorkbook workbook  = createWorkbook(request,response, filename, targetname);
			if(workbook == null){
				return false;
			}
			HSSFCellStyle style = getExcelStyle(workbook,null,null);
			workbook.setSheetName(0, "报表");
			HSSFSheet sheet = workbook.getSheetAt(0);//读取第一个工作簿
			createfenceSheet(list,sheet,style);
			// 主体内容生成结束    
			workbook.write(os); // 写入文件   
			os.flush();
			os.close();
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}

	}
	/**
	 * 生成报表
	* @Title: createFenceExcel
	* @param list
	* @return
	* @throws
	 */
	private void createfenceSheet(List<?> list,HSSFSheet sheet, HSSFCellStyle style){
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			for(int i=0;i<list.size();i++){
				Object obj = list.get(i);
				row = sheet.createRow(i+1);
				int y = 0;
				Field[] fields = obj.getClass().getDeclaredFields();
				for (int j = 0; j < fields.length; j++) {
					Field field = fields[j];
					field.setAccessible(true);
					Object value = field.get(obj);
					cell = row.createCell(y++);
					if(value instanceof String){
						cell.setCellValue((String)value);
					}
					if(value instanceof Date){
						cell.setCellValue((Date)value);
					}
					if(value instanceof Integer){
						cell.setCellValue((Integer)value);
					}
					if(value instanceof Double){
						cell.setCellValue((Double)value);
					}
					
					cell.setCellStyle(style);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 按天 按月 按周查询开始时间和结束时间设置
	* @Title: validDate
	* void
	* @throws
	 */
	public void loadAlarmDate(String type,String startDate,String endDate){
		Date curDate = new Date();
		if(StringUtils.isNotBlank(type)){//按天 按月 按周查询
			if(type.equals("day")){
				String[] days = DateUtil.getToDayTime(curDate,"yyyy-MM-dd");
				startDate = days[0];
				endDate = days[1];
			}else if(type.equals("week")){//本周
				String[] days = DateUtil.getWeekTime(curDate,"yyyy-MM-dd");
				startDate = days[0];
				endDate = days[1];
			}else if(type.equals("month")){//本月
				String[] days = DateUtil.getMonthTime(curDate,"yyyy-MM-dd");
				startDate = days[0];
				endDate = days[1];
			}
		}else {
			if(StringUtils.isBlank(startDate)){
				startDate = DateUtil.formatDateByFormat(curDate, "yyyy-MM-dd");
				startDate = startDate.substring(0, 7)+"-01";//默认每月第一天
			}
			if(StringUtils.isBlank(endDate)){
				endDate = DateUtil.formatDateByFormat(curDate, "yyyy-MM-dd");
			}
			String startMonth = startDate.substring(0, 7);
			String endMonth = endDate.substring(0, 7);
			if(!startMonth.equals(endMonth)){//将结束时间 年月改为开始时间年月
				endDate = startMonth+endDate.substring(7,10);
			}
			Date startTime = DateUtil.formatStringByDate(startDate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			Date endTime = DateUtil.formatStringByDate(endDate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long difTime = DateUtil.diffDates(endTime, startTime);
			if(difTime < 0){//结束时间早于开始时间 默认等于开始时间
				endDate = startDate;
			}
			long difSTime = DateUtil.diffDates(curDate, startTime);
			if(difSTime < 0){//开始时间大于当前时间
				startDate = DateUtil.formatDateByFormat(curDate, "yyyy-MM-dd");
			}
		}
	}
	
	protected HashMap getPageMap(HttpServletRequest request,HashMap m) {
		String size = request.getParameter("rows");
		if(StringUtils.isBlank(size) || !StringUtil.isInteger(size)){
		}else {
			int pageSize = Integer.parseInt(size);
			if(pageSize>Constant.MAX_PAGE_SIZE){
				m.put("pageSize", Constant.MAX_PAGE_SIZE);//每页最多50条记录
			}else if(pageSize <= 0){
				m.put("pageSize", 1);
			}else {
				m.put("pageSize", pageSize);
			}
		}
		String curPage = request.getParameter("page");
		if (StringUtils.isBlank(curPage) || !StringUtil.isInteger(curPage)) {
		}else{
			int currentPage = Integer.parseInt(curPage);
			if(currentPage <= 0)
				currentPage = -1;
			m.put("pageNumber", currentPage);
		}
		return m;
	}
	
	
	/**
	 * 转换统一的返回结果
	 * @param page
	 * @param info
	 * @return
	 * @Author Juannyoh
	 * 2016-2-3下午4:29:17
	 */
	public Map<String,Object> builderResult(Pagination page,String info){
		Map<String,Object> result=null;
		if(page!=null){
			result=new HashMap<String,Object>();
			result.put("page", page.getPageNo());
			result.put("records", page.getTotalCount());
			result.put("rows", page.getResult());
			result.put("total", page.getTotalPage());
			result.put("success", true);
			result.put("info", info);
		}else {
			result=new HashMap<String,Object>();
			result.put("success", false);
			result.put("info", info);
		}
		return result;
	}
	
}
