package com.supermap.egispapi.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.egispapi.service.IUserInfoCache;
import com.supermap.egispservice.base.service.UserService;

/**
 * 
  * @ClassName: UserInfoCache
  * @Description: 
  *   保存用户的信息到缓存
  * @author huanghuasong
  * @date 2016-7-11 下午2:46:57
  *
 */
@Component
public class UserInfoCache implements IUserInfoCache{

	
	private static Map<String,Object> USER_INFOS = new HashMap<String,Object>();
	
	private static Logger LOGGER = Logger.getLogger(UserInfoCache.class);
	
	@Autowired
	private UserService userService;
	
	private static Calendar currentDay = Calendar.getInstance();
	
	@Override
	public Object findByKey(String key) {
		// 每天的第一次请求进行缓存清空
		Calendar now = Calendar.getInstance();
		if(now.get(Calendar.DAY_OF_MONTH) != currentDay.get(Calendar.DAY_OF_MONTH)){
			USER_INFOS.clear();
			currentDay = now;
		}
		if(USER_INFOS.containsKey(key)){
			LOGGER.debug("## 从缓存中获取到用户信息");
			return USER_INFOS.get(key);
		}
		
		Object obj = null;
		try {
			obj = this.userService.findUserById(key);
			USER_INFOS.put(key, obj);
			LOGGER.info("## 缓存用户信息["+obj+"]");
		} catch (Exception e) {
			LOGGER.error("## 查找用户信息发生异常："+e.getMessage(), e);
		}
		
		return obj;
	}

}
