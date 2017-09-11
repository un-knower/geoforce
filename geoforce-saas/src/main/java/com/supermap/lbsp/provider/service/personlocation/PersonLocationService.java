package com.supermap.lbsp.provider.service.personlocation;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.hibernate.gps.PersonGps;

/**
 * 
* ClassName：PersonLocationService   
* 类描述：   巡店人员位置监控业务处理
* 操作人：wangshuang   
* 操作时间：2014-11-27 下午05:32:50     
* @version 1.0
 */
public interface PersonLocationService {

	public List<PersonGps> personLocation(HashMap<String, Object> hm) throws Exception;
}
