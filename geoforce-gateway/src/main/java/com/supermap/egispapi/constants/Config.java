package com.supermap.egispapi.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class Config {

	

	@Value("#{configParam['error.process.uri']}")
	private String errorProcessUri;
	
	@Value("#{configParam['max.pagesize']}")
	private int maxPageSize;
	
	@Value("#{configParam['statistic.urls']}")
	private String statistics_urls;
	
	@Value("#{configParam['do.need.validation']}")
	private boolean doNeedValidation=true;
	
	/**
	 * 坐标分单API接口url
	 */
	@Value("#{configParam['statisticXY.urls']}")
	private String statisticsXY_urls;

	public String getErrorProcessUri() {
		return errorProcessUri;
	}

	public void setErrorProcessUri(String errorProcessUri) {
		this.errorProcessUri = errorProcessUri;
	}

	public int getMaxPageSize() {
		return maxPageSize;
	}

	public void setMaxPageSize(int maxPageSize) {
		this.maxPageSize = maxPageSize;
	}

	public String getStatistics_urls() {
		return statistics_urls;
	}

	public void setStatistics_urls(String statisticsUrls) {
		statistics_urls = statisticsUrls;
	}
	
	
	
	public boolean isDoNeedValidation() {
		return doNeedValidation;
	}

	public void setDoNeedValidation(boolean doNeedValidation) {
		this.doNeedValidation = doNeedValidation;
	}
	

	public boolean isNeedStatistics(String uri){
		if(StringUtils.isEmpty(this.getStatistics_urls())){
			return false;
		}else{
			String uris[] = this.getStatistics_urls().split(";");
			for(String uriItem : uris){
				if(uri.endsWith(uriItem)){
					return true;
				}
			}
			return false;
		}
	}
	
	
	public String getStatisticsXY_urls() {
		return statisticsXY_urls;
	}

	public void setStatisticsXY_urls(String statisticsXY_urls) {
		this.statisticsXY_urls = statisticsXY_urls;
	}

	public boolean isNeedStatisticsXY(String uri){
		if(StringUtils.isEmpty(this.getStatisticsXY_urls())){
			return false;
		}else{
			String uris[] = this.getStatisticsXY_urls().split(";");
			for(String uriItem : uris){
				if(uri.endsWith(uriItem)){
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * 调用分单API的用户KEYs白名单
	 */
//	@Value("#{configParam['white.distribute.user.keys']}")
//	private String whiteDistributeUserKeys;
//
//	public String getWhiteDistributeUserKeys() {
//		return whiteDistributeUserKeys;
//	}
//
//	public void setWhiteDistributeUserKeys(String whiteDistributeUserKeys) {
//		this.whiteDistributeUserKeys = whiteDistributeUserKeys;
//	}
	
	
	/**
	 * 判断用户key是否在调用分单API的白名单中
	 * @param userKey 用户key
	 * @return
	 */
//	public boolean isWhiteDistributeKeys(String userKey){
//		boolean flag=false;
//		if(!StringUtils.isEmpty(whiteDistributeUserKeys)){
//			String[] keyArray=whiteDistributeUserKeys.split(",");//以逗号分割的用户keys
//			if(null!=keyArray&&keyArray.length>0){
//				for(String key:keyArray){
//					if(userKey.equals(key)){//存在对应的用户key
//						flag=true;
//						break;
//					}
//				}
//			}else{//如果没有白名单列表，则直接默认不限制
//				flag=true;
//			}
//		}else{//如果没有白名单列表，则直接默认不限制
//			flag=true;
//		}
//		return flag;
//	}
	

}
