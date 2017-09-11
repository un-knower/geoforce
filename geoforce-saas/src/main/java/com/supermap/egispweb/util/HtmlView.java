package com.supermap.egispweb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractUrlBasedView;

import com.alibaba.dubbo.common.utils.IOUtils;

public class HtmlView extends AbstractUrlBasedView {
	
	public HtmlView() {
		setContentType("text/html;charset=utf-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String rootPath = getServletContext().getRealPath("/") + getUrl();
		File file = new File(rootPath);
		
		response.setContentType(getContentType());
		InputStream inputStream = new FileInputStream(file);
		OutputStream outputStream = response.getOutputStream();
		IOUtils.write(inputStream, outputStream);
		
		outputStream.flush();
		if (null!=inputStream) {
			inputStream.close();
		}
		if (null!=outputStream) {
			outputStream.close();
		}
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		String rootPath = getServletContext().getRealPath("/") + getUrl();
		File file = new File(rootPath);
		if (file.exists()) {
			return true;
		}
		
		return false;
	}
}