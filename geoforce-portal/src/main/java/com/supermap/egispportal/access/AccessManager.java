package com.supermap.egispportal.access;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.supermap.egispportal.constants.PortalConstants;
import com.supermap.utils.FileUtil;

/**
 * 
 * <p>Title: AccessManager</p>
 * Description:		管理所有访问接口的状态
 *
 * @author Huasong Huang
 * CreateTime: 2014-9-5 下午04:24:59
 */
public class AccessManager {

	private static Logger LOGGER = Logger.getLogger(AccessManager.class);
	private static Map<String,Boolean> resources = new HashMap<String,Boolean>();
	static{
		load();
	}
	private static void load(){
		List<String> infos = null;
		try{
			File f = new File(PortalConstants.ACCESS_PATH);
			if(null != f && f.exists()){
				infos = FileUtil.readFromFile(PortalConstants.ACCESS_PATH, "UTF-8");
			}else{
				InputStream inStream = AccessManager.class.getClassLoader().getResourceAsStream(PortalConstants.ACCESS_PATH);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,"utf-8"));
				String buffer = null;
				infos = new ArrayList<String>();
				while((buffer = reader.readLine()) != null){
					infos.add(new String(buffer));
				}
				inStream.close();
				reader.close();
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		
		if(null != infos && infos.size() > 0){
			for(String info : infos){
				resources.put(info, Boolean.TRUE);
			}
		}
	}
	
	
	public static boolean isNeedLogin(String uri) {
		Boolean result = resources.get(uri);
		return (result == null) ? false : result;
	}
	
	
}
