package com.supermap.egispweb.consumer.PersonPlan;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.bean.PersonPlanBean;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPlan;

public interface PersonPlanConsumer {
	/**
	 * 工作计划查询
	 * @param page
	 * @param hm
	 * @return
	 * personId：计划执行人
	 * eid：企业id
	 * deptId：部门id
	 * status:状态
	 * 
	 */
	public List<PersonPlanBean> queryPersonPlan(Page page, HashMap<String, Object> hm);
	
	/**
	 * 
	 * @param userId 审批人
	 * @param opinion 审批意见
	 * @return
	 */
	public int updatePersonPlan(String userId, String opinion,String planId,String status);
	
	/**
	 * 工作日报分页查询
	 * @param page
	 * @param hm
	 * @return
	 */
	public Page pagequeryPersonPlan(Page page,HashMap<String,Object> hm);
	
	/**
	 * 根据id查询
	 * @param planId
	 * @return
	 */
	public PersonPlan getPersonPlan(String planId);
	
	
	

}
