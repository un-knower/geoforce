package com.supermap.egispweb.excelview;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.supermap.egispweb.util.StringUtil;

public class FendansExcelView extends AbstractExcelView {
	
	private String[] resultType = {"","分单成功","分单失败-无区划","分单失败-无坐标"};
	
	private String[] areaStatus={"","正常","停用"};//区划状态 0 正常；1 停用

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@Override
	protected void buildExcelDocument(Map<String, Object> obj,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// map的key，在对应的controller中设置
		List list = (List) obj.get("rows");
			HSSFSheet sheet = workbook.createSheet("分单结果");
			sheet.setDefaultColumnWidth((short) 12);
			HSSFCell cell = getCell(sheet, 0, 0);
			setText(cell, "序号");
			cell = getCell(sheet, 0, 1);
			setText(cell, "分单时间");
			cell = getCell(sheet, 0, 2);
			setText(cell, "订单号");
			cell = getCell(sheet, 0, 3);
			setText(cell, "地址");
			cell = getCell(sheet, 0, 4);
			setText(cell, "省");
			cell = getCell(sheet, 0, 5);
			setText(cell, "市");
			cell = getCell(sheet, 0, 6);
			setText(cell, "区");
			cell = getCell(sheet, 0, 7);
			setText(cell, "区划名称");
			cell = getCell(sheet, 0, 8);
			setText(cell, "区划状态");
			cell = getCell(sheet, 0, 9);
			setText(cell, "关联区划");
//			cell = getCell(sheet, 0, 10);
//			setText(cell, "X");
//			cell = getCell(sheet, 0, 11);
//			setText(cell, "Y");
			cell = getCell(sheet, 0, 10);
			setText(cell, "结果类型");
			
			if(list!=null&&list.size()>0){
				int j = 0;
				for (int i = 0; i < list.size(); i++) {
					if(j < 60000) {
						HSSFRow sheetRow = sheet.createRow(j + 1);
						Map<String,Object> map=(Map<String, Object>) list.get(i);
						sheetRow.createCell(0).setCellValue(i+1);
						sheetRow.createCell(1).setCellValue(StringUtil.convertObjectToString(map.get("fendantime")));
						sheetRow.createCell(2).setCellValue(StringUtil.convertObjectToString(map.get("ordernum")));
						sheetRow.createCell(3).setCellValue(StringUtil.convertObjectToString(map.get("address")));
						sheetRow.createCell(4).setCellValue(StringUtil.convertObjectToString(map.get("province")));
						sheetRow.createCell(5).setCellValue(StringUtil.convertObjectToString(map.get("city")));
						sheetRow.createCell(6).setCellValue(StringUtil.convertObjectToString(map.get("county")));
						sheetRow.createCell(7).setCellValue(StringUtil.convertObjectToString(map.get("areaname")));
						sheetRow.createCell(8).setCellValue(areaStatus[StringUtil.convertObjectToInt(map.get("areastatus"))+1]);
						sheetRow.createCell(9).setCellValue(StringUtil.convertObjectToString(map.get("relation_areaname")));
//						sheetRow.createCell(10).setCellValue(StringUtil.convertObjectToString(map.get("smx")));
//						sheetRow.createCell(11).setCellValue(StringUtil.convertObjectToString(map.get("smy")));
						sheetRow.createCell(10).setCellValue(resultType[StringUtil.convertObjectToInt(map.get("resulttype"))]);
						j++;
					} else {
						j = 0;
						sheet = workbook.createSheet("分单结果" + i);
						HSSFRow sheetRow = sheet.createRow(j + 1);
						Map<String,Object> map=(Map<String, Object>) list.get(i);
						sheetRow.createCell(0).setCellValue(i+1);
						sheetRow.createCell(1).setCellValue(StringUtil.convertObjectToString(map.get("fendantime")));
						sheetRow.createCell(2).setCellValue(StringUtil.convertObjectToString(map.get("ordernum")));
						sheetRow.createCell(3).setCellValue(StringUtil.convertObjectToString(map.get("address")));
						sheetRow.createCell(4).setCellValue(StringUtil.convertObjectToString(map.get("province")));
						sheetRow.createCell(5).setCellValue(StringUtil.convertObjectToString(map.get("city")));
						sheetRow.createCell(6).setCellValue(StringUtil.convertObjectToString(map.get("county")));
						sheetRow.createCell(7).setCellValue(StringUtil.convertObjectToString(map.get("areaname")));
						sheetRow.createCell(8).setCellValue(areaStatus[StringUtil.convertObjectToInt(map.get("areastatus"))+1]);
						sheetRow.createCell(9).setCellValue(StringUtil.convertObjectToString(map.get("relation_areaname")));
//						sheetRow.createCell(10).setCellValue(StringUtil.convertObjectToString(map.get("smx")));
//						sheetRow.createCell(11).setCellValue(StringUtil.convertObjectToString(map.get("smy")));
						sheetRow.createCell(10).setCellValue(resultType[StringUtil.convertObjectToInt(map.get("resulttype"))]);
						j++;
					}
				}
			}
			// 设置下载时客户端Excel的名称
			String filename = "分单结果数据-"+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())+ ".xls";
			// 处理中文文件名
			filename = MyExcelEncodeUtils.encodeFilename(filename, request);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ filename);
			OutputStream ouputStream = response.getOutputStream();
			workbook.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
	}

}
