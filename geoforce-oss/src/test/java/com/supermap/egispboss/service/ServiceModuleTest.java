/*package com.supermap.egispboss.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispboss.entity.ServiceModuleEntity;
import com.supermap.egispboss.pojo.BaseServiceModule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class ServiceModuleTest {

	@Resource
	private IServiceModule serviceModule;
	
	@Test
	public void testAddModule(){
		ServiceModuleEntity entity = new ServiceModuleEntity();
		entity.setName("分单宝");
		
		ServiceModuleEntity entity01 = new ServiceModuleEntity();
		entity01.setName("区域管理");
		
		ServiceModuleEntity entity02 = new ServiceModuleEntity();
		entity02.setName("分单");
		entity.add(entity01);
		entity.add(entity02);
		
		serviceModule.add(entity);
		
	}

	
	@Test
	public void testRemoveById(){
		serviceModule.deleteById(1+"");
	}
	
	@Test
	public void testQueryById(){
		ServiceModuleEntity sme = serviceModule.queryById(4+"", true);
		System.out.println(sme.getName());
		List<ServiceModuleEntity> entitys = sme.getServiceModules();
		for(ServiceModuleEntity entity : entitys){
			System.out.println(entity.getName());
		}
	}
	
	@Test
	public void testQueryByIdForTree(){
		BaseServiceModule bsm = serviceModule.queryById("1");
		System.out.println(bsm);
		if(bsm.getServiceModule() != null){
			for(BaseServiceModule module : bsm.getServiceModule()){
				System.out.println(module);
			}
		}
	}
	
	
	public IServiceModule getServiceModule() {
		return serviceModule;
	}

	public void setServiceModule(IServiceModule serviceModule) {
		this.serviceModule = serviceModule;
	}
	
	
	
	
}
*/