package com.supermap.egispservice.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.supermap.egispservice.base.constants.PortalConstants;
import com.supermap.egispservice.base.service.IPhoneService;


@Component("phoneService")
public class PhoneServiceImpl implements IPhoneService {
	
	private static Logger LOGGER = Logger.getLogger(PhoneServiceImpl.class);
	
	@Override
	public void bindTelephone(String tel, String userId, String captcha) {
		
		//计算UID
		long tempUid = Long.parseLong(userId);
		tempUid =tempUid * 12345 + 54321;
		LOGGER.info("## bind the car["+tempUid+"]");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(PortalConstants.PHONE_BIND_SERVICE);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair sourceParam = new BasicNameValuePair("captcha",captcha);
		params.add(sourceParam);
		NameValuePair tokenParam = new BasicNameValuePair("user_id",tempUid+"");
		params.add(tokenParam);
		NameValuePair phoneParam = new BasicNameValuePair("tel",tel);
		params.add(phoneParam);
		UrlEncodedFormEntity paramEntity =null;
		HttpResponse response = null;
		try {
			paramEntity = new UrlEncodedFormEntity(params);
			post.setEntity(paramEntity);
			response = httpClient.execute(post);
			if(null != response){
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");
				LOGGER.info("## 绑定手机结果:"+result+"]");
				JSONObject obj = JSONObject.fromObject(result);
				if(obj.containsKey("status")){
					int status = obj.getInt("status");
					if(200 != status){
						throw new Exception(obj.getString("message"));
					}
				}
			}
		} catch (Exception e) {
			throw new NullPointerException(e.getMessage());
		}
	}

	
	@Override
	public void getValidationCode(String phone) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(PortalConstants.PHONE_CAPCHAR_SERVICE);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair sourceParam = new BasicNameValuePair("source",PortalConstants.PHONE_CAPCHAR_SOURCE);
		params.add(sourceParam);
		NameValuePair tokenParam = new BasicNameValuePair("verification_token",PortalConstants.PHONE_CAPCHAR_TOKEN);
		params.add(tokenParam);
		NameValuePair phoneParam = new BasicNameValuePair("phone",phone);
		params.add(phoneParam);
		UrlEncodedFormEntity paramEntity =null;
		HttpResponse response = null;
		try {
			paramEntity = new UrlEncodedFormEntity(params);
			post.setEntity(paramEntity);
			response = httpClient.execute(post);
			if(null != response){
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");
				LOGGER.info("## 获取到绑定手机的验证码结果:"+result+"]");
				JSONObject obj = JSONObject.fromObject(result);
				if(obj.containsKey("status")){
					int status = obj.getInt("status");
					if(200 != status){
						throw new Exception(obj.getString("message"));
					}
				}
			}
		} catch (Exception e) {
			throw new NullPointerException(e.getMessage());
		}finally{
		}
		
	}

}
