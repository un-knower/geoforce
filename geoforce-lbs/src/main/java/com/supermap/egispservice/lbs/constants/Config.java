package com.supermap.egispservice.lbs.constants;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import  com.supermap.egispservice.lbs.dao.AlarmTypeDao;
import com.supermap.egispservice.lbs.entity.AlarmType;

/**
 * 
 * @author Administrator
 *
 */
@Component
public class Config {
	
	public Config() {
		super();
	}
	
	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";//日期格式

	
	

}
