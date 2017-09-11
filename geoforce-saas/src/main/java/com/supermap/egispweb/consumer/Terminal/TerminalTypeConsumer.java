package com.supermap.egispweb.consumer.Terminal;

import java.util.HashMap;
import java.util.List;

import com.supermap.egispservice.lbs.entity.TerminalType;
import com.supermap.lbsp.provider.common.Page;

/*import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.TerminalType;*/

public interface TerminalTypeConsumer {
	/**
	 * 
	 * @param 查询终端类型
	 * @return
	 */
	public int addTeminalType(TerminalType teminalType);

	/**
	 * 修改终端类型
	 * 
	 * @param TeminalType
	 * @return
	 */

	public int updateTerminalType(TerminalType teminalType);

	/**
	 * 根据ID获取终端类型
	 * 
	 * @param id
	 * @return
	 */

	public TerminalType getTerminalType(String id);

	/**
	 * 查询终端类型
	 * 
	 * @param map
	 * @return
	 */
	public List queryTerminalType(Page page, HashMap map);
	public List queryTerminalType(String type);

	/**
	 * 判断是否有终端类型名称
	 * 
	 * @param name
	 * @return
	 */
	public int hasName(String name);

	/**
	 * 删除终端类型
	 * 
	 * @param teminalType
	 * @return
	 */
	public int delTeminalType(TerminalType teminalType);

	public TerminalType getTeminalByName(String name);

	/**
	 * 判断是否有终端类型代码
	 * 
	 * @param code
	 * @return
	 */
	public int hasCode(String code);

}
