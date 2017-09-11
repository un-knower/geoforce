package com.supermap.lbsp.provider.service.person;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.bean.JsonZTree;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Person;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStoreForeign;

public interface PersonService {
	
		
		/**
		 * 
		 * @param 增加人员
		 * @return
		 */
		public Person addPerson(Person person);

		/**
		 * 修改人员
		 * 
		 * @param person
		 * @return
		 */

		public int updatePerson(Person person);

		/**
		 * 根据ID获取人员
		 * @param id
		 * @return
		 */

		public Person getPerson(String id);

		/**
		 * 查询人员
		 * @param map
		 * @return
		 */
		public List<Person> queryPersonList(Page page,HashMap hm);
		public Page queryPerson(Page page,HashMap hm);
		
		/**
		 * 
		 * @param 删除车辆
		 * @return
		 */
		public int delPerson(Person person);
		
		/**
		 * 车辆关联门店
		 * 
		 * @param CarDriver
		 * @return
		 */

		public int setPersonStore(PersonStoreForeign personStoreForeign);

		/**
		 * 根据名称查询（判断用户名是否存在）
		 * @param name
		 * @param eid
		 * @return
		 */
		public int hasName(String name, String eid);
		
		/**
		 * （判断终端号是否存在）
		 * @param name
		 * @param eid
		 * @return
		 */
		public int hasTerminal(String termCode, String eid);
		/**
		 * 得到树
		 * @param deptCode
		 * @return
		 */
		public List<JsonZTree> personTree(String deptId,String treeId);
		/**
		 * 根据名称得到人员对象
		 * @param name
		 * @param eid
		 * @return
		 */
		public Person getPersonByName(String name, String eid);
}
