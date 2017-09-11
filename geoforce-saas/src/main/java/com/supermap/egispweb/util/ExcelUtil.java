
package com.supermap.egispweb.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import com.supermap.egisp.addressmatch.beans.AddressMatchParam;
import com.supermap.egispservice.base.entity.PointExtcolEntity;
import com.supermap.egispservice.base.entity.PointExtcolValEntity;
import com.supermap.egispservice.base.entity.PointGroupEntity;
import com.supermap.egispservice.base.service.PointExtcolService;
import com.supermap.egispweb.pojo.netpoint.NetPointBean;
import com.supermap.egispweb.pojo.order.OrderBean;


public class ExcelUtil {

	private static final String EXCEL_2003 = "xls";
	private static final String EXCEL_2007 = "xlsx";

	private ExcelUtil() {
	}

	/**
	 * 订单Excel解析
	 * 
	 * @param is
	 *            Excel文件流
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 */
	public static List<OrderBean> readOrderExcel(InputStream is, String fileName)
			throws IOException {
		if (is == null || fileName == null) {
			throw new IOException("参数错误");
		}

		Workbook workbook = null;
		if (fileName.endsWith(EXCEL_2003)) {
			workbook = new HSSFWorkbook(is);
		} else if (fileName.endsWith(EXCEL_2007)) {
			workbook = new XSSFWorkbook(is);
		} else {
			throw new IOException("格式错误");
		}

		List<OrderBean> result = new ArrayList<OrderBean>();
		// 暂时只考虑第一个sheet
		Sheet sheet = workbook.getSheetAt(0);
		// 从标题列的下一列开始
		try {
			for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
				
					Row row = sheet.getRow(i);
					if (row != null) {
						// 暂时定义第1、2列是必须的，分别代表订单编号、地址
						Cell cell0 = row.getCell(0);
						Cell cell1 = row.getCell(1);
						if (!isCoreCellValid(cell0) || !isCoreCellValid(cell1)) {
							continue;
						}
						String number = getCellValueAsString(cell0);
						String address = getCellValueAsString(cell1);
						
						OrderBean orderbean=new OrderBean(number, address);
						
						//第3、4列分别为开始时间和结束时间   格式为6：00或06：00
						Cell cell2 = row.getCell(2);
						Cell cell3 = row.getCell(3);
						if(isCoreCellValid(cell2)&&isCoreCellValid(cell3)){
							String start=getCellValueAsString(cell2);
							String end=getCellValueAsString(cell3);
							if(isRightRegExp(start)&&isRightRegExp(end)){
								orderbean.setStartTime(start);
								orderbean.setEndTime(end);
							}
							else {
								throw new IOException("start/end 格式错误,正确格式(HH:mm)");
							}
						}
						result.add(orderbean);
					}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * <p>Title ：readPointBean</p>
	 * Description：		读取网点Excel文件
	 * @param inStream	
	 * @param fileName
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-23 上午10:07:27
	 */
	public static  Map<String,Object> readPointExcel(InputStream inStream,String fileName,String topuserid,String userid,PointExtcolService  pointExtcolService,Config config,List<PointGroupEntity> grouplist)throws Exception{
		if(inStream == null || StringUtil.isStringEmpty(fileName)){
			throw new IOException("参数异常");
		}
		/*UserEntity user=(UserEntity)session.getAttribute("user");
		String userId = null;
		if (null != user) {
			userId = user.getId();
		} else {
			throw new IOException("未找到用户信息");
		}*/
		if(StringUtils.isEmpty(topuserid)||StringUtils.isEmpty(userid)){
			throw new IOException("未找到用户信息");
		}

		//int sumgroupcount=0;
		List<String> groupnames=new ArrayList<String>();
		if(grouplist!=null){
			//sumgroupcount=grouplist.size();//已有分组的数量
			for(PointGroupEntity g:grouplist){
				groupnames.add(g.getGroupname());
			}
		}
		
		
		
		Workbook workbook = null;
		if (fileName.endsWith(EXCEL_2003)) {
			workbook = new HSSFWorkbook(inStream);
		} else if (fileName.endsWith(EXCEL_2007)) {
			workbook = new XSSFWorkbook(inStream);
		} else {
			throw new IOException("格式错误");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		List<NetPointBean> result = new ArrayList<NetPointBean>();
		String errorMsg="";
		// 暂时只考虑第一个sheet
		Sheet sheet = workbook.getSheetAt(0);
		//修改 7.15 juannyoh
		if(sheet.getLastRowNum()>1000){
			throw new IOException("每次导入不超过1000条，请检查");
		}
		else{
			//获取表头
			Row firstrow = sheet.getRow(sheet.getFirstRowNum());
			Map<Integer,Object> headerMap=readPointExcelHeader(firstrow);
			String excelHeader[]=new FieldMap().ChangMapToStringArray(headerMap.values());//excel标题
			
			//先把标题行写入到自定义列表中
			String baseCh[]=config.getHeaderBase().split(",");//中文 固定字段
			String base[]=baseCh;//中文
			String baseEn[]=config.getHeaderBaseEn().split(",");//英文 固定字段
			Map<String, Object> extcolheader = null;
//			PointExtcolEntity record=pointExtcolService.findByUserid(userId);//根据用户id查找到
			PointExtcolEntity record=pointExtcolService.findByUserid(topuserid);//根据总账号用户id查找到自定义字段
			
			extcolheader=new FieldMap().ChangeObjectToMap(record);//将查询的表头存在map中，用field、fieldvalue对应
			String extcolnames[] =null;
			if(extcolheader==null){
				extcolnames=new String[0];
			}
			else {
				extcolnames=new FieldMap().ChangMapToStringArray(extcolheader.values());//已添加的字段
			}
			//汇总基础字段和已经添加的字段数据
			String[] allexsitNames = (String[]) ArrayUtils.addAll(base, extcolnames);
			int maxcount= base.length+config.getMaxExtcol();//最多字段数
			
			
			//判断是否是旧模版
			boolean isold=compareIsOldExcel(excelHeader);
			if(isold){
				throw new IOException("模板格式不正确，请下载最新模板");
			}
			
			//判断本身表头中是否有重复字段
			Set<String> set= new HashSet<String>();
			boolean re=compare(excelHeader,set);
			if(re){
				throw new IOException(set+"标题列名称重复，请检查");
			}
			
			//判断列字段长度超出最大限制（20）
			Set<String> set2= new HashSet<String>();
			boolean relong=compareTitleLong(excelHeader,set2);
			if(relong){
				throw new IOException(set2+"标题列字段长度超出最大限制（20），请检查");
			}
			
			//匹配检查是否超出最大字段数量限制
			//Map<String,Object> compresult=isColsnameRepeatForMap(headerMap,userId,pointExtcolService,config);
//			Map<String,Object> compresult=isColsnameRepeatForMap(excelHeader,allexsitNames,maxcount);
//			boolean flag=(Boolean) compresult.get("flag");
			boolean flag=false;
			Map<String,Object> compresult=new HashMap<String,Object>();
			if(topuserid.equals(userid)){//如果当前用户是总账号，则判断
				compresult=isColsnameRepeatForMap(excelHeader,allexsitNames,maxcount);
				flag=(Boolean) compresult.get("flag");
			}
			//超出字段长度，抛出异常到 前台
			if(flag){
				throw new IOException((String)compresult.get("message"));
			}
			else {
				if(topuserid.equals(userid)){//如果当前用户是总账号，则添加到数据库
					//将新增加的字段写入自定义列  数据表中
					for(int i=0;i<excelHeader.length;i++){
						boolean flag_=true;
						for(int j=0;j<allexsitNames.length;j++){
							if(excelHeader[i].equals(allexsitNames[j])){
								flag_=false;
								break;
							}
						}
						if(flag_){
							PointExtcolEntity  pointext=new PointExtcolEntity();
							pointext.setUserid(topuserid);
							pointext.setDefaultcol(excelHeader[i]);
							pointExtcolService.addPointExtcol(pointext);
						}
					}
				}
				
				//获取目前所有的自定义字段
				Map<String, Object> extcolheaderNew = null;
				PointExtcolEntity recordNew=pointExtcolService.findByUserid(topuserid);//根据用户id查找到
				extcolheaderNew=new FieldMap().ChangeObjectToMap(recordNew);//将查询的表头存在map中，用field、fieldvalue对应
				
				//读取标题下一行，开始解析数据
				NetPointBean netBean =null;
				PointExtcolValEntity pointExtcolValEntity=null;
				Row row=null;
				for (int i = sheet.getFirstRowNum() + 1,rowlength=sheet.getLastRowNum(); i <= rowlength; i++) {
						netBean = new NetPointBean();
						pointExtcolValEntity=new PointExtcolValEntity();
						pointExtcolValEntity.setUserid(userid);
						netBean.setPointExtcolValEntity(pointExtcolValEntity);
						row = sheet.getRow(i);//当前行
						try{
						if(row!=null){
							for(int j=0,cellnum=row.getLastCellNum();j<cellnum;j++){
								try{	
									String value = getCellValueAsString(row.getCell(j));//当前列的值
									String headname=headerMap.get(j).toString().trim();//当前列的表头
									String index=isValueInArray(headname, baseCh);//判断是否在基础字段中
									String key=isValueInMap(headname, extcolheaderNew);//判断是否在自定义字段中
									
									if(headname!=null&&headname.equals("所属分组")&&value!=null&&value.length()>20){
										int crei=i;
										int crej=j+1;
										errorMsg+="第("+crei+"行/"+crej+"列)出错，分组字段长度超出限制（20）,请检查！<br>";
										break;
									}
									
									if(value!=null&&value.length()>50){
										int crei=i;
										int crej=j+1;
										errorMsg+="第("+crei+"行/"+crej+"列)出错，字段长度超出限制（50）,请检查！<br>";
										break;
									}
									//如果是基础字段
									if(index!=null&&!index.equals("")){
										String basefield=baseEn[Integer.parseInt(index)];
										Field field=netBean.getClass().getDeclaredField(basefield);//获取field
										field.setAccessible(true);
										//如果是所属分组
										if(headname.equals("所属分组")){
											if(value!=null&&!value.equals("")){
												PointGroupEntity pointGroupEntity=new PointGroupEntity();
												pointGroupEntity.setGroupname(value);
												groupnames.add(value);
												field.set(netBean, pointGroupEntity);
											}
										}
										else{
											if((value==null||value.equals(""))&&(basefield.equals("x")||basefield.equals("y"))){
												continue;
											}
											else if(value!=null&&!value.equals("")&&(basefield.equals("x")||basefield.equals("y"))){
												field.set(netBean, Double.parseDouble(value));
											}
											else{
												field.set(netBean, value);
												continue;
											}
										}
									}else if(key!=null&&!key.equals("")){//如果是自定义字段
										String extfield=key;
										Field field=pointExtcolValEntity.getClass().getDeclaredField(extfield);
										field.setAccessible(true);
										field.set(pointExtcolValEntity, value);
									}
								}catch(Exception e){
									int crei=i;
									int crej=j+1;
									errorMsg+="第"+crei+"行"+crej+"列出错，请检查！<br>";
									break;
								}
							}
						}
					}catch(Exception e){
						errorMsg+="第"+i+"行不符合模板要求，请检查！<br>";
						continue;
					}
					//增加判断，导入的网点数据格式是否正确
					if(netBean.getName()==null||netBean.getName().equals("")||((netBean.getAddress()==null||netBean.getAddress().equals(""))&&(netBean.getX()==0||netBean.getY()==0))){
						errorMsg+="第"+i+"行不符合模板要求，请检查！<br>";
						continue;
					}
					else{
						result.add(netBean);
					}	
				}
				//判断分组个数是否超过10个
				if(topuserid.equals(userid)){//如果当前用户是总账号，则判断
					List<String> listWithoutDup = new ArrayList<String>(new HashSet<String>(groupnames));
					if(listWithoutDup!=null&&listWithoutDup.size()>10){
						map.put("errorMsg", "最多只可创建10个分组，有更多需求请联系商务");
						map.put("result", null);
						map.put("groupnamelist", null);
						return map;
					}else{
						map.put("groupnamelist", listWithoutDup);
					}
				}
			}
		}
		map.put("errorMsg", errorMsg);
		map.put("result", result);
		
		return map;
	}
	
	
	private static boolean isCoreCellValid(Cell cell) {
		return cell != null && !"".equals(cell.toString())
				&& (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_STRING);
	}

	private static String getCellValueAsString(Cell cell) {
		if(null != cell){
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				return "";
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			case Cell.CELL_TYPE_NUMERIC:
				return String.valueOf(cell.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			default:
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	/**
	 * 根据导入的excel获取表头
	 * @param inStream
	 * @param fileName
	 * @return
	 * @throws Exception
	 * @Author Juannyoh
	 * 2015-8-24下午4:20:53
	 */
	public static Map<Integer,Object> readPointExcelHeader(Row row)throws Exception{
		Map<Integer,Object> result=new HashMap<Integer,Object>();
		//从第一行开始读标题
			try {
				if (row != null) {
					int i=1;
					for(int j=0;j<row.getLastCellNum();j++){
						Cell cell=row.getCell(j);
						String value=getCellValueAsString(cell);
						if(value!=null&&!value.equals("")){
							result.put(j, value);
						}else{
							result.put(j, "自定义列-"+i);
							i++;
						}
					}
				}
			} catch (Exception e) {
				throw e;
			}
		return result;
	}
	
	
	/**
	 * 导入的时候，判断字段是否超过最大限制
	 * @param map
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-24下午5:38:39
	 */
	public  static Map<String,Object> isColsnameRepeatForMap(String excelcolnames[],String allexsitNames[],int maxcount){
		Map<String,Object> resultmap=new HashMap<String,Object>();
		
		String message="";
		boolean flag=false;
		int samecount=0;
		int differentcount=0;
		if(excelcolnames.length>maxcount){
			message="导入的扩展字段数超过最大扩展字段数量限制(10个)";
			flag=true;
		}
		else{
			for(int i=0;i<excelcolnames.length;i++){
				for(int j=0;j<allexsitNames.length;j++){
					if(excelcolnames[i]!=null&&excelcolnames[i].equals(allexsitNames[j])){
						samecount++;
						break;
					}
				}
			}
		}
		differentcount=excelcolnames.length-samecount;
		int sumcount=differentcount+allexsitNames.length;//导入的字段+已有的字段数   去重后的数量
		if(sumcount<=maxcount){
			flag=false;
			message="("+sumcount+"<="+maxcount+")";
		}else{
			flag=true;
			message="扩展字段数超过最大扩展字段数量限制(10个)";
		}
		resultmap.put("message", message);
		resultmap.put("flag", flag);
		return resultmap;
	}
	
	/**
	 * 判断某个字符串是否在数组中，是则返回数组下标index
	 * @param value
	 * @param s
	 * @return
	 * @Author Juannyoh
	 * 2015-8-25下午2:24:02
	 */
	public static  String isValueInArray(String value,String s[]){
		boolean result=false;
		String index=null;
		for(int i=0;i<s.length;i++){
			if(value.equals(s[i])){
				result=true;
				index=i+"";
				break;
			}
		}
		return index;
	}
	
	/**
	 * 判断一个字符串是否在map中，是则返回key
	 * @param value
	 * @param map
	 * @return
	 * @Author Juannyoh
	 * 2015-8-25下午2:32:31
	 */
	public static String  isValueInMap(String value,Map<String,Object> map){
		String result=null;
		
		if(map !=null){
			for(String key : map.keySet()){
				if(value.equals(map.get(key))){
					result=key;
					break;
				}
			}
		}
		return result;
	}
	
	
	
	/**
	 * 判断本身导入的excel表头中是否有重复
	 * //写一个方法把数组和set作为参数传过去
	 * @param strs
	 * @param set
	 * @return
	 * @Author Juannyoh
	 * 2015-8-26上午9:54:09
	 */
	public static boolean compare(String[] strs,Set set){
	    boolean result = false;
	   //从第一个元素开始比较元素是不是有相同的出现
	   for(int i=0;i<strs.length;i++){
	        for(int j=i+1;j<strs.length;j++){
	            //如果元素相同，保存到set中
	            if(strs[i]!=null&&strs[j]!=null&&strs[i].equals(strs[j])){
	                 set.add(strs[i]);
	                 result = true;
	            }
	       }
	   }
	return result;
	}
	
	/**
	 * 
	 * @param strs
	 * @param set
	 * @return
	 * @Author Juannyoh
	 * 2015-9-6下午5:20:41
	 */
	public static boolean compareTitleLong(String[] strs,Set set){
	    boolean result = false;
	   //从第一个元素开始比较元素是不是有相同的出现
	    if(strs!=null){
	    	for(String s:strs){
	            if(s!=null&&s.length()>20){
	                 set.add(s);
	                 result = true;
	            }
		   }
	    }
	return result;
	}
	
	/**
	 * 判断两个数组中字符串数组是不是都相等
	 * 判断是否是旧模版
	 * @param s11
	 * @param s22
	 * @return
	 * @Author Juannyoh
	 * 2015-9-17下午2:46:03
	 */
	public static boolean compareIsOldExcel(String s11[]){
		boolean flag=true;
		String s22[]={"number","name","address","latitude","longitude","manager","phone"};
		if(s11!=null&&s22!=null&&s11.length==s22.length){
			for(int i=0;i<s11.length;i++){
				if(s11[i]!=null&&!s11[i].trim().equals(s22[i])){
					flag=false;
					break;
				}
			}
		}else flag=false;
		return flag;
	}
	
	
	/**
	 * 判断订单中开始时间和结束时间是否匹配相应的格式
	 * @param time
	 * @return
	 * @Author Juannyoh
	 * 2015-12-24下午4:50:47
	 */
	public static boolean isRightRegExp(String time){
		boolean flag=false;
		String reg1="^([01]\\d|2[0123]):([0-5]\\d|59)$";//匹配06：00
		String reg2="^((\\d)|(1\\d)|(2[0-3])):([0-5]\\d|59)$";//匹配6：00
		if(time!=null&&(time.matches(reg1)||time.matches(reg2))){
			flag=true;
		}
		return flag;
	}
	
	
	/**
	 * 解析地址excel
	 * @param is
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @Author Juannyoh
	 * 2016-10-13下午3:37:23
	 */
	public static Map<String,Object> readAddressExcel(InputStream is, String fileName)
			throws IOException {
		if (is == null || fileName == null) {
			throw new IOException("参数错误");
		}
		Map<String,Object> reaultmap=new HashMap<String,Object>();
		Workbook workbook = null;
		if (fileName.endsWith(EXCEL_2003)) {
			workbook = new HSSFWorkbook(is);
		} else if (fileName.endsWith(EXCEL_2007)) {
			workbook = new XSSFWorkbook(is);
		} else {
			throw new IOException("格式错误");
		}
		List<OrderBean> result = new ArrayList<OrderBean>();
		List<AddressMatchParam> addresslist=new ArrayList<AddressMatchParam>();
		// 暂时只考虑第一个sheet
		Sheet sheet = workbook.getSheetAt(0);
		// 从标题列的下一列开始
		try {
			for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
					Row row = sheet.getRow(i);
					if (row != null) {
						// 暂时定义第1、2列是必须的，分别代表订单编号、地址
						Cell cell0 = row.getCell(0);
						Cell cell1 = row.getCell(1);
						if (!isCoreCellValid(cell0) || !isCoreCellValid(cell1)) {
							continue;
						}
						String number = getCellValueAsString(cell0);
						String address = getCellValueAsString(cell1);
						
						OrderBean orderbean=new OrderBean(number,address);
						AddressMatchParam addressmc=new AddressMatchParam();
						addressmc.setId(number);
						addressmc.setAddress(address);
						
						//第3、4列分别为开始时间和结束时间   格式为6：00或06：00
						Cell cell2 = row.getCell(2);
						Cell cell3 = row.getCell(3);
						if(isCoreCellValid(cell2)&&isCoreCellValid(cell3)){
							String start=getCellValueAsString(cell2);
							String end=getCellValueAsString(cell3);
							if(isRightRegExp(start)&&isRightRegExp(end)){
								orderbean.setStartTime(start);
								orderbean.setEndTime(end);
							}
							else {
								throw new IOException("start/end 格式错误,正确格式(HH:mm)");
							}
						}
						
						Cell cell4 = row.getCell(4);//需求量
						if(isCoreCellValid(cell4)){
							orderbean.setRequirements(getCellValueAsString(cell4));
						}else{
							orderbean.setRequirements("0");//没有的话默认为0
						}
						result.add(orderbean);
						addresslist.add(addressmc);
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(is!=null){
				is.close();
			}
		}
		reaultmap.put("orderlist", result);
		reaultmap.put("addresslist", addresslist);
		return reaultmap;
	}
	
}

