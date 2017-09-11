package com.supermap.egispservice.reverse.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpTool {

	public static String getContent(String url, String param) throws Exception {
		// 打开连接
		URL objURL = new URL(url);
		URLConnection rulConnection = objURL.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;

		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setUseCaches(false);
		httpUrlConnection.setConnectTimeout(180000);// 3分钟超时
		httpUrlConnection.setRequestMethod("POST");

		// 发送post请求
		OutputStream out = httpUrlConnection.getOutputStream();
		PrintWriter pw = new PrintWriter(out);

		pw.write(param);
		pw.flush();
		pw.close();
		out.close();

		// 接收处理结果反馈
		String resultString = "";
		InputStream in = httpUrlConnection.getInputStream();
		int responseCode = httpUrlConnection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String line = br.readLine();

			while (line != null) {
				resultString += line;
				line = br.readLine();
			}
			br.close();
		} else {
			throw new RuntimeException("服务返回异常:"+responseCode);
		}
		in.close();
		httpUrlConnection.disconnect();

		return resultString;
	}

}
