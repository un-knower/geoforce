package com.supermap.egispservice.pathplan.constant;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.supermap.egispservice.pathplan.service.INavigateAnalystEngineerService;
/**
 * 
 * @description 初始化导航引擎
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-10-10
 * @version 1.0
 */
@Component
public class InitNavigateAnalystEngineer implements ApplicationListener<ApplicationEvent>,DisposableBean {
	@Autowired
	private INavigateAnalystEngineerService navigateAnalystEngineerService;
	private boolean isStarted;
	
	@Override
	public void onApplicationEvent(ApplicationEvent appEvent) {
		if(!isStarted){
			navigateAnalystEngineerService.initObjectJava() ;
			isStarted=true;
		}
	}
	
	public synchronized INavigateAnalystEngineerService getNavigateAnalystEngineer(){
		return navigateAnalystEngineerService;
	}
	
	@Override
	public void destroy() throws Exception {
		if(navigateAnalystEngineerService!=null ){
			navigateAnalystEngineerService.closeObjectJava();
		}
		
	}
}
