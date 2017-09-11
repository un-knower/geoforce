package com.supermap.egispweb.httpclient;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientDituhui {

	private static Logger LOGGER=Logger.getLogger(HttpClientDituhui.class);
	
	/**
	 * 地图慧网点同步解析
	 * @param url
	 * @return
	 * @Author Juannyoh
	 * 2016-9-27下午5:31:12
	 */
	public static List<DituhuiPointResult> syncDituhuiPoint(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget=new HttpGet(url);
		List<DituhuiPointResult> resultlist=null;
		try {
			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				String conResult = EntityUtils.toString(response.getEntity());
				JSONArray jsonArray = JSONArray.fromObject(conResult);
				if(jsonArray!=null&&jsonArray.size()>0){
					resultlist=new ArrayList<DituhuiPointResult>();
					for (int i = 0,le=jsonArray.size(); i < le; i++){
						JSONObject jsonObjects = (JSONObject) jsonArray.getJSONObject(i);
						DituhuiPointResult result=new DituhuiPointResult();
						result.setCid(jsonObjects.getString("id"));
						result.setTitle(jsonObjects.getString("title"));
						result.setAttributes(formatAttributes(jsonObjects.getJSONArray("attributes")));
						result.setBdsmx(jsonObjects.getDouble("x"));
						result.setBdsmy(jsonObjects.getDouble("y"));
						resultlist.add(result);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("调用大众版网点接口失败："+e);
		}
		return resultlist;
	}
	
	public static List<PointAttributes> formatAttributes(JSONArray jsonarray){
		List<PointAttributes> resultlist=null;
		if(jsonarray!=null&&jsonarray.size()>0){
			resultlist=new ArrayList<PointAttributes>();
			for(int i=0,s=jsonarray.size();i<s;i++){
				JSONObject jsonObjects = (JSONObject) jsonarray.getJSONObject(i);
				PointAttributes attr=new PointAttributes();
				attr.setId(jsonObjects.getString("id"));
				attr.setKey(jsonObjects.getString("key"));
				attr.setMarkerid(jsonObjects.getString("marker_id"));
				attr.setValue(jsonObjects.getString("value"));
				resultlist.add(attr);
			}
		}
		return resultlist;
	}
	
	/**
	 * 获取大众版用户地图
	 * @param url
	 * @return
	 * @Author Juannyoh
	 * 2016-10-14上午11:42:17
	 */
	public static HttpEntity getDituhuiUserMap(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget=new HttpGet(url);
		HttpEntity conResult =null;
		try {
			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				conResult = response.getEntity();
			}
		} catch (Exception e) {
			LOGGER.info("调用大众版网点接口失败："+e);
		}
		return conResult;
	}
	
}
