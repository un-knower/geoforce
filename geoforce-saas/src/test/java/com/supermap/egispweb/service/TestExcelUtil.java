package com.supermap.egispweb.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.supermap.egispweb.pojo.order.OrderBean;
import com.supermap.egispweb.util.ExcelUtil;
import com.supermap.egispweb.util.Md5Util;

public class TestExcelUtil extends TestCase {

	public void testReadOrderExcel() {
		String fileName = "E:\\Desktop\\temp\\test.xlsx";
		List<OrderBean> list = new ArrayList<OrderBean>();
		try {
			list = ExcelUtil.readOrderExcel(new FileInputStream(fileName),
					"test.xlsx");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(list.size() + "\r\n");
		for (OrderBean o : list) {
			System.out.println(o.getNumber() + "\t" + o.getAddress());
		}
	}
	
	@Test
	public void testMd5(){
		System.out.println(Md5Util.md5("relax2015"));
	}
	
}
