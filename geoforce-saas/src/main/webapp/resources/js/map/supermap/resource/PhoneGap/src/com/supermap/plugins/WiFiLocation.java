package com.supermap.plugins;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.supermap.RequestControl;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.supermap.plugins.Location;



public class WiFiLocation
{
  public static Activity act = RequestControl.act;
  
  public static LocationClient mLocationClient = null;
  public static BDLocationListener myListener = null;
  public static JSONObject locResult = null;
  
  public WiFiLocation()
  {
  }

  public static JSONObject wifiLocation() throws JSONException {
	
	if(mLocationClient == null)
	{
		mLocationClient = ((Location)act.getApplication()).mLocationClient;
		((Location)act.getApplication()).myListener.listeners.add(new MyEventObj());

		//locResult = ((Location)act.getApplication()).fileInfo;
	}
	
	JSONObject location = new JSONObject();
	try {		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		option.setPoiNumber(5);	//最多返回POI个数	
		option.setPoiDistance(1000); //poi查询距离		
		option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		
		mLocationClient.setLocOption(option);
		mLocationClient.start();		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return location;
  }
  
  public static class MyEventObj implements EventObj{
	@Override
	public void func(JSONObject obj) {
		if(locResult == null)
		{
			locResult = new JSONObject();
		}
		
		locResult = obj;		
	}
	  
  }
	
  public static boolean isWiFiActive() {
		Context context = act.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
					return true;
					}
				}
			}
		}
		return false;
		}
	}
