package com.supermap.egispservice.pathanalysis.constant;

import com.supermap.egispservice.pathanalysis.service.IPathAnalysisService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

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
	private IPathAnalysisService pathAnalysisService;
	private boolean isStarted;
	
	@Override
	public void onApplicationEvent(ApplicationEvent appEvent) {
		if(!isStarted){
			pathAnalysisService.initObjectJava() ;
			isStarted=true;
		}
	}
	

	@Override
	public void destroy() throws Exception {
		if(pathAnalysisService !=null ){
			pathAnalysisService.closeObjectJava();
		}
		
	}
}
