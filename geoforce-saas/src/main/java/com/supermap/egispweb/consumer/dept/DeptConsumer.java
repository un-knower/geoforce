package com.supermap.egispweb.consumer.dept;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.bean.JsonZTree;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.info.Dept;

public interface DeptConsumer {
	/**
	 * 根据ID获取 部门
	 * 
	 * @param id
	 * @return
	 */

	public Dept getDept(String id);

	/**
	 * 查询 部门
	 * 
	 * @param map
	 * @return
	 */
	public List queryDept(Page page, HashMap hm);

	/**
	 * 判断是否有部门名称
	 * 
	 * @param name  名称
	 * 	 * @param id 部门ID
	 * @return
	 */
	public int hasName(String id,String name);
	/**
	 * 根据code获取 部门
	 * 
	 * @param id
	 * @return
	 */

	public Dept getDeptByCode(String code)throws Exception;
	
	/**
	 * 获得部门树
	 * @param deptCode
	 * @return
	 */
	public List<JsonZTree> deptTree(String deptCode);
}
