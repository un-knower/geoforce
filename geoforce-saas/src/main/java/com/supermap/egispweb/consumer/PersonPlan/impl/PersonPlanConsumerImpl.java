package com.supermap.egispweb.consumer.PersonPlan.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.PersonPlan.PersonPlanConsumer;
import com.supermap.egispweb.util.DateUtil;
import com.supermap.lbsp.provider.bean.PersonPlanBean;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPlan;
import com.supermap.lbsp.provider.service.personPlan.PersonPlanService;
import com.supermap.lbsp.provider.service.region.RegionService;


@Component("personPlanConsumer")
public class PersonPlanConsumerImpl implements PersonPlanConsumer {

	@Reference(version="2.5.3")
	private PersonPlanService personPlanService;
	
	@Reference(version="2.5.3")
	private RegionService regionService;
	
	
	@Override
	public List<PersonPlanBean> queryPersonPlan(Page page,
			HashMap<String, Object> hm) {
		try {
			return this.personPlanService.queryPersonPlan(page, hm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Page pagequeryPersonPlan(Page page, HashMap<String, Object> hm) {
		try {
			return this.personPlanService.pagequeryPersonPlan(page, hm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public int updatePersonPlan(String userId, String opinion,String planId,String status) {
		PersonPlan personPlan=null;
		int ret = 0;
		try {
			 personPlan=this.getPersonPlan(planId);
			 if(personPlan==null){
				 return ret;
			 }
			 personPlan.setUserId(userId);
			 personPlan.setOpinion(opinion);
			 personPlan.setSureDate(new Date());
			 personPlan.setStatus(Short.parseShort(status));
			 ret=this.personPlanService.updatePersonPlan(personPlan);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}


	@Override
	public PersonPlan getPersonPlan(String planId) {
		// TODO Auto-generated method stub
		return this.personPlanService.getPersonPlan(planId);
	}

}
