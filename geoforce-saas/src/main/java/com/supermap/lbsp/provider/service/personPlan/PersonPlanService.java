package com.supermap.lbsp.provider.service.personPlan;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.bean.PersonPlanBean;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPlan;

public interface PersonPlanService {

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
	 * 工作计划修改
	 * @param personPlan
	 * @return
	 */
	public int updatePersonPlan(PersonPlan personPlan);
	
	/**
	 * 根据id查询
	 * @param planId
	 * @return
	 */
	public PersonPlan getPersonPlan(String planId);
	/**
	 * 分页查询
	 * @param page
	 * @param hm
	 * @return
	 */
	public Page pagequeryPersonPlan(Page page,HashMap<String,Object> hm);
	/**
	 * 添加计划
	 * @param personPlan
	 * @return
	 */
	public int addPersonPlan(PersonPlan personPlan);
	
	/**
	 * 删除计划
	 * @param personPlan
	 * @return
	 */
	public int deletePersonPlan(PersonPlan personPlan);
	
	
}
