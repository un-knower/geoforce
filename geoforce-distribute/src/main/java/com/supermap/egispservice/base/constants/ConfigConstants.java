package com.supermap.egispservice.base.constants;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ConfigConstants {
	
	@Value("#{configParam['correct_user_eids']}")
	private String correctEids;

	public String getCorrectEids() {
		return correctEids;
	}

	public void setCorrectEids(String correctEids) {
		this.correctEids = correctEids;
	}
	
	
	/**
	 * 判断用户是否需要使用纠错库（以企业为单位）
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2017-1-16上午9:04:21
	 */
	public boolean isUseCorrectAddr(String eid){
		boolean flag=false;
		if(StringUtils.isNotEmpty(correctEids)){
			String[] eidArray=correctEids.split(",");//以逗号分割的企业id
			if(null!=eidArray&&eidArray.length>0){
				for(String id:eidArray){
					if(id.equals(eid)){//存在对应的企业id
						flag=true;
						break;
					}
				}
			}
		}
		return flag;
	}
	

}
