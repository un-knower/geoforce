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

public class AreaExcelView extends AbstractExcelView {
	
	private String[] areaStatus = {"正常","停用"};

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@Override
	protected void buildExcelDocument(Map<String, Object> obj,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// map的key，在对应的controller中设置
		List list = (List) obj.get("rows");
		
			HSSFSheet sheet = workbook.createSheet("区划数据");
			sheet.setDefaultColumnWidth((short) 12);
			HSSFCell cell = getCell(sheet, 0, 0);
			setText(cell, "序号");
			cell = getCell(sheet, 0, 1);
			setText(cell, "区划名称");
			cell = getCell(sheet, 0, 2);
			setText(cell, "区划编号");
			cell = getCell(sheet, 0, 3);
			setText(cell, "区划状态");
			cell = getCell(sheet, 0, 4);
			setText(cell, "关联区划名称");
			cell = getCell(sheet, 0, 5);
			setText(cell, "关联区划编号");
			cell = getCell(sheet, 0, 6);
			setText(cell, "绑定网点");
			cell = getCell(sheet, 0, 7);
			setText(cell, "所属帐号");
			cell = getCell(sheet, 0, 8);
			setText(cell, "创建时间");
			cell = getCell(sheet, 0, 9);
			setText(cell, "修改时间");
			cell = getCell(sheet, 0, 10);
			setText(cell, "省");
			cell = getCell(sheet, 0, 11);
			setText(cell, "市");
			cell = getCell(sheet, 0, 12);
			setText(cell, "区");
			cell = getCell(sheet, 0, 13);
			setText(cell, "乡镇");
			
			
			if(list!=null&&list.size()>0){
				int j = 0;
				for (int i = 0; i < list.size(); i++) {
					if(j < 60000) {
						HSSFRow sheetRow = sheet.createRow(j + 1);
						Map<String,Object> map=(Map<String, Object>) list.get(i);
						sheetRow.createCell(0).setCellValue(i+1);
						sheetRow.createCell(1).setCellValue(StringUtil.convertObjectToString(map.get("name")));
						sheetRow.createCell(2).setCellValue(StringUtil.convertObjectToString(map.get("areaNumber")));
						sheetRow.createCell(3).setCellValue(areaStatus[StringUtil.convertObjectToInt(map.get("area_status"))]);
						sheetRow.createCell(4).setCellValue(StringUtil.convertObjectToString(map.get("relation_areaname")));
						sheetRow.createCell(5).setCellValue(StringUtil.convertObjectToString(map.get("relation_areanumber")));
						sheetRow.createCell(6).setCellValue(StringUtil.convertObjectToString(map.get("pointnames")));
						sheetRow.createCell(7).setCellValue(StringUtil.convertObjectToString(map.get("username")));
						sheetRow.createCell(8).setCellValue(StringUtil.convertObjectToString(map.get("create_time")));
						sheetRow.createCell(9).setCellValue(StringUtil.convertObjectToString(map.get("update_time")));
						sheetRow.createCell(10).setCellValue(StringUtil.convertObjectToString(map.get("PROVINCE")));
						sheetRow.createCell(11).setCellValue(StringUtil.convertObjectToString(map.get("CITY")));
						sheetRow.createCell(12).setCellValue(StringUtil.convertObjectToString(map.get("COUNTY")));
						sheetRow.createCell(13).setCellValue(StringUtil.convertObjectToString(map.get("TOWN")));
						j++;
					} else {
						j = 0;
						sheet = workbook.createSheet("区划数据" + i);
						HSSFRow sheetRow = sheet.createRow(j + 1);
						Map<String,Object> map=(Map<String, Object>) list.get(i);
						sheetRow.createCell(0).setCellValue(i+1);
						sheetRow.createCell(1).setCellValue(StringUtil.convertObjectToString(map.get("name")));
						sheetRow.createCell(2).setCellValue(StringUtil.convertObjectToString(map.get("areaNumber")));
						sheetRow.createCell(3).setCellValue(areaStatus[StringUtil.convertObjectToInt(map.get("area_status"))]);
						sheetRow.createCell(4).setCellValue(StringUtil.convertObjectToString(map.get("relation_areaname")));
						sheetRow.createCell(5).setCellValue(StringUtil.convertObjectToString(map.get("relation_areanumber")));
						sheetRow.createCell(6).setCellValue(StringUtil.convertObjectToString(map.get("pointnames")));
						sheetRow.createCell(7).setCellValue(StringUtil.convertObjectToString(map.get("username")));
						sheetRow.createCell(8).setCellValue(StringUtil.convertObjectToString(map.get("create_time")));
						sheetRow.createCell(9).setCellValue(StringUtil.convertObjectToString(map.get("update_time")));
						sheetRow.createCell(10).setCellValue(StringUtil.convertObjectToString(map.get("PROVINCE")));
						sheetRow.createCell(11).setCellValue(StringUtil.convertObjectToString(map.get("CITY")));
						sheetRow.createCell(12).setCellValue(StringUtil.convertObjectToString(map.get("COUNTY")));
						sheetRow.createCell(13).setCellValue(StringUtil.convertObjectToString(map.get("TOWN")));
						j++;
					}
				}
			}
			// 设置下载时客户端Excel的名称
			String filename = "区划数据-"+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())+ ".xls";
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
