package com.supermap.egispboss.excel.export;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;

import com.supermap.egispservice.base.pojo.ExportPointBean;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 * 
 * @author leno
 * @version v1.0
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            byte[]表jpg格式的图片数据
 */
public class ExportExcel<T> {
    public void exportExcel(String sheetname,Collection<T> dataset, OutputStream out,Class classes) {
    	export(sheetname, null, dataset, out, "yyyy-MM-dd",classes);
    }

    public void exportExcel(String sheetname,String[] headers, Collection<T> dataset,
            OutputStream out,Class classes) {
    	export(sheetname, headers, dataset, out, "yyyy-MM-dd",classes);
    }

    public void exportExcel(String sheetname,String[] headers, Collection<T> dataset,OutputStream out, String pattern,Class classes) {
    	export(sheetname, headers, dataset, out, pattern,classes);
    }
    
    public void exportPointExcel(String sheetname,String[] headers, Collection<T> dataset,OutputStream out, String pattern) {
    	exportPoint(sheetname, headers, dataset, out, pattern);
    }
    
    public void exportFendanExcel(String sheetname,String[] headers, Collection<T> dataset,OutputStream out, String pattern) {
    	exportFendan(sheetname, headers, dataset, out, pattern);
    }
    
    public void exportUserExcel(String sheetname,String[] headers, Collection<T> dataset,OutputStream out, String pattern) {
    	exportUser(sheetname, headers, dataset, out, pattern);
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     * 
     * @param title
     *            表格标题名
     * @param headers
     *            表格属性列名数组
     * @param dataset
     *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out
     *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern
     *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings("unchecked")
    public void export(String title, String[] headers,
            Collection<T> dataset, OutputStream out, String pattern,Class classes) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.GREY_80_PERCENT.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setColor(HSSFColor.GREY_80_PERCENT.index);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
//        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
//        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//        comment.setAuthor("dituhui");
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell00=row.createCell(0);
        cell00.setCellValue("序号");
        cell00.setCellStyle(style);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i+1);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
    		sheet.autoSizeColumn(i, true);
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
        Field[] fields = classes.getDeclaredFields();
        List<Field> filedlist=new ArrayList<Field>();
        for(short i = 0; i < fields.length; i++){
        	Field field = fields[i];
            String fieldName = field.getName();
            if(fieldName.equals("serialVersionUID")){
            	continue;
            }else{
            	field.setAccessible(true);
            	filedlist.add(field);
            }
        }
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            HSSFCell celli0=row.createCell(0);
            celli0.setCellValue(index);
            celli0.setCellStyle(style2);
            for (short i = 0; i < filedlist.size(); i++) {
            	sheet.autoSizeColumn(i, true);
                HSSFCell cell = row.createCell(i+1);
                cell.setCellStyle(style2);
                Field field = filedlist.get(i);
               /* String fieldName = field.getName();
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);*/
                try {
                    /*Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[] {});
                    Object value = getMethod.invoke(t, new Object[] {});*/
                	Object value =field.get(t);
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if(value!=null){
                    	if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        }else if (value instanceof byte[]) {
                            // 有图片时，设置行高为60px;
                            row.setHeightInPoints(60);
                            // 设置图片所在列宽度为80px,注意这里单位的一个换算
                            sheet.setColumnWidth(i, (short) (35.7 * 80));
                            // sheet.autoSizeColumn(i);
                            byte[] bsValue = (byte[]) value;
                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
                                    1023, 255, (short) 6, index, (short) 6, index);
                            anchor.setAnchorType(2);
                            patriarch.createPicture(anchor, workbook.addPicture(
                                    bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }else{
                            // 其它数据类型都当作字符串简单处理
                            textValue = value.toString();
                        }
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(
                                    textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.GREY_80_PERCENT.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    // 清理资源
                }
            }
        }
    	//隐藏空列
    	row = sheet.getRow(0);
    	Iterator<Cell> itcell=row.cellIterator();
    	while(itcell.hasNext()){
    		Cell cell =itcell.next();
    		String celltitle=cell.getStringCellValue();
    		if(StringUtils.isEmpty(celltitle)){
    			sheet.setColumnHidden(cell.getColumnIndex(), true);
    		}
    	}
    	
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 导出网点
     * @param title
     * @param headers
     * @param dataset
     * @param out
     * @param pattern
     * @Author Juannyoh
     * 2016-10-9下午5:12:02
     */
    public void exportPoint(String title, String[] headers,
            Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.GREY_80_PERCENT.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setColor(HSSFColor.GREY_80_PERCENT.index);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
       
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell00=row.createCell(0);
        cell00.setCellValue("序号");
        cell00.setCellStyle(style);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i+1);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
    		sheet.autoSizeColumn(i, true);
    		sheet.setColumnWidth(i, 5000);
        }
        // 遍历集合数据，产生数据行
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        List<ExportPointBean> list=(List<ExportPointBean>) dataset;
        if(list!=null&&list.size()>0){
        	for(int i=0,s=list.size();i<s;i++){
        		ExportPointBean point=list.get(i);
        		row = sheet.createRow(i+1);
        		HSSFCell cell=row.createCell(0);
        		cell.setCellStyle(style2);
        		cell.setCellValue(i+1);
        		cell=row.createCell(1);cell.setCellStyle(style2);cell.setCellValue(point.getId());
        		cell=row.createCell(2);cell.setCellStyle(style2);cell.setCellValue(point.getName());
        		cell=row.createCell(3);cell.setCellStyle(style2);cell.setCellValue(point.getAddress());
        		cell=row.createCell(4);cell.setCellStyle(style2);cell.setCellValue(point.getCol1());
        		cell=row.createCell(5);cell.setCellStyle(style2);cell.setCellValue(point.getCol2());
        		cell=row.createCell(6);cell.setCellStyle(style2);cell.setCellValue(point.getCol3());
        		cell=row.createCell(7);cell.setCellStyle(style2);cell.setCellValue(point.getCol4());
        		cell=row.createCell(8);cell.setCellStyle(style2);cell.setCellValue(point.getCol5());
        		cell=row.createCell(9);cell.setCellStyle(style2);cell.setCellValue(point.getCol6());
        		cell=row.createCell(10);cell.setCellStyle(style2);cell.setCellValue(point.getCol7());
        		cell=row.createCell(11);cell.setCellStyle(style2);cell.setCellValue(point.getCol8());
        		cell=row.createCell(12);cell.setCellStyle(style2);cell.setCellValue(point.getCol9());
        		cell=row.createCell(13);cell.setCellStyle(style2);cell.setCellValue(point.getCol10());
        		cell=row.createCell(14);cell.setCellStyle(style2);cell.setCellValue(point.getSmx()==null?0.0:point.getSmx().doubleValue());
        		cell=row.createCell(15);cell.setCellStyle(style2);cell.setCellValue(point.getSmy()==null?0.0:point.getSmy().doubleValue());
        		cell=row.createCell(16);cell.setCellStyle(style2);cell.setCellValue(point.getAreaName());
        		cell=row.createCell(17);cell.setCellStyle(style2);cell.setCellValue(point.getUsername());
        		cell=row.createCell(18);cell.setCellStyle(style2);cell.setCellValue(point.getCreateTime()==null?"":sdf.format(point.getCreateTime()));
        		cell=row.createCell(19);cell.setCellStyle(style2);cell.setCellValue(point.getUpdateTime()==null?"":sdf.format(point.getUpdateTime()));
        		cell=row.createCell(20);cell.setCellStyle(style2);cell.setCellValue(point.getGroupname());
        		cell=row.createCell(21);cell.setCellStyle(style2);cell.setCellValue(point.getStatus());
        	}
        }
        for(int j=0;j<22;j++){
			sheet.autoSizeColumn(j, true);
		}
    	//隐藏空列
    	row = sheet.getRow(0);
    	Iterator<Cell> itcell=row.cellIterator();
    	while(itcell.hasNext()){
    		Cell cell =itcell.next();
    		String celltitle=cell.getStringCellValue();
    		if(StringUtils.isEmpty(celltitle)){
    			sheet.setColumnHidden(cell.getColumnIndex(), true);
    		}
    	}
    	
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 导出分单
     * @param title
     * @param headers
     * @param dataset
     * @param out
     * @param pattern
     * @Author Juannyoh
     * 2016-10-9下午5:17:24
     */
    public void exportFendan(String title, String[] headers,
            Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.GREY_80_PERCENT.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setColor(HSSFColor.GREY_80_PERCENT.index);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
       
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell00=row.createCell(0);
        cell00.setCellValue("序号");
        cell00.setCellStyle(style);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i+1);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
    		sheet.autoSizeColumn(i, true);
    		sheet.setColumnWidth(i, 5000);
        }
        // 遍历集合数据，产生数据行
        List<FendanBean> list=(List<FendanBean>) dataset;
        if(list!=null&&list.size()>0){
        	for(int i=0,s=list.size();i<s;i++){
        		FendanBean fendan=list.get(i);
        		row = sheet.createRow(i+1);
        		HSSFCell cell=row.createCell(0);
        		cell.setCellStyle(style2);
        		cell.setCellValue(i+1);
        		cell=row.createCell(1);cell.setCellStyle(style2);cell.setCellValue(fendan.getId());
        		cell=row.createCell(2);cell.setCellStyle(style2);cell.setCellValue(fendan.getOrderNum());
        		cell=row.createCell(3);cell.setCellStyle(style2);cell.setCellValue(fendan.getBatch());
        		cell=row.createCell(4);cell.setCellStyle(style2);cell.setCellValue(fendan.getProvince());
        		cell=row.createCell(5);cell.setCellStyle(style2);cell.setCellValue(fendan.getCity());
        		cell=row.createCell(6);cell.setCellStyle(style2);cell.setCellValue(fendan.getCounty());
        		cell=row.createCell(7);cell.setCellStyle(style2);cell.setCellValue(fendan.getAddress());
        		cell=row.createCell(8);cell.setCellStyle(style2);cell.setCellValue(fendan.getSmx());
        		cell=row.createCell(9);cell.setCellStyle(style2);cell.setCellValue(fendan.getSmy());
        		cell=row.createCell(10);cell.setCellStyle(style2);cell.setCellValue(fendan.getAreaName());
        		cell=row.createCell(11);cell.setCellStyle(style2);cell.setCellValue(fendan.getFailReason());
        		cell=row.createCell(12);cell.setCellStyle(style2);cell.setCellValue(fendan.getOrderStatus());
        		cell=row.createCell(13);cell.setCellStyle(style2);cell.setCellValue(fendan.getImportTime());
        	}
        }
        for(int j=0;j<14;j++){
			sheet.autoSizeColumn(j, true);
		}
    	//隐藏空列
    	row = sheet.getRow(0);
    	Iterator<Cell> itcell=row.cellIterator();
    	while(itcell.hasNext()){
    		Cell cell =itcell.next();
    		String celltitle=cell.getStringCellValue();
    		if(StringUtils.isEmpty(celltitle)){
    			sheet.setColumnHidden(cell.getColumnIndex(), true);
    		}
    	}
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出用户信息
     * @param title
     * @param headers
     * @param dataset
     * @param out
     * @param pattern
     * @Author Juannyoh
     * 2016-10-13下午2:02:51
     */
    public void exportUser(String title, String[] headers,
            Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.GREY_80_PERCENT.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setColor(HSSFColor.GREY_80_PERCENT.index);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
       
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell00=row.createCell(0);
        cell00.setCellValue("序号");
        cell00.setCellStyle(style);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i+1);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
    		sheet.autoSizeColumn(i, true);
    		sheet.setColumnWidth(i, 5000);
        }
        // 遍历集合数据，产生数据行
        List<UserBean> list=(List<UserBean>) dataset;
        if(list!=null&&list.size()>0){
        	for(int i=0,s=list.size();i<s;i++){
        		UserBean user=list.get(i);
        		row = sheet.createRow(i+1);
        		HSSFCell cell=row.createCell(0);
        		cell.setCellStyle(style2);
        		cell.setCellValue(i+1);
        		cell=row.createCell(1);cell.setCellStyle(style2);cell.setCellValue(user.getUserid());
        		cell=row.createCell(2);cell.setCellStyle(style2);cell.setCellValue(user.getUsername());
        		cell=row.createCell(3);cell.setCellStyle(style2);cell.setCellValue(user.getEmail());
        		cell=row.createCell(4);cell.setCellStyle(style2);cell.setCellValue(user.getTelephone());
        		cell=row.createCell(5);cell.setCellStyle(style2);cell.setCellValue(user.getRealname());
        		cell=row.createCell(6);cell.setCellStyle(style2);cell.setCellValue(user.getComname());
        		cell=row.createCell(7);cell.setCellStyle(style2);cell.setCellValue(user.getAdminname());
        		cell=row.createCell(8);cell.setCellStyle(style2);cell.setCellValue(user.getCombusiness());
        		cell=row.createCell(9);cell.setCellStyle(style2);cell.setCellValue(user.getFirstlogin());
        		cell=row.createCell(10);cell.setCellStyle(style2);cell.setCellValue(user.getStatus());
        	}
        }
        for(int j=0;j<14;j++){
			sheet.autoSizeColumn(j, true);
		}
    	//隐藏空列
    	row = sheet.getRow(0);
    	Iterator<Cell> itcell=row.cellIterator();
    	while(itcell.hasNext()){
    		Cell cell =itcell.next();
    		String celltitle=cell.getStringCellValue();
    		if(StringUtils.isEmpty(celltitle)){
    			sheet.setColumnHidden(cell.getColumnIndex(), true);
    		}
    	}
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}