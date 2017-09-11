package com.supermap.egispweb.consumer.Terminal;

import java.util.HashMap;
import java.util.List;

import com.supermap.egispservice.lbs.entity.Terminal;
import com.supermap.lbsp.provider.common.Page;

/*import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Terminal;*/

public interface TerminalConsumer {
	/**
	 * 
	 * @param 添加终端
	 * @return
	 */
	public int addTerminal(Terminal teminal);

	/**
	 * 修改终端
	 * 
	 * @param Teminal
	 * @return
	 */

	public int updateTerminal(Terminal teminal);

	/**
	 * 根据ID获取终端
	 * @param id
	 * @return
	 */

	public Terminal getTerminal(String id);

	/**
	 * 查询终端
	 * @param map
	 * @return
	 */
	public List queryTerminal(Page page,HashMap map,boolean carIdIsNull);

	/**
	 * 判断是否有手机号
	 * @param license
	 * @return
	 */
	public int hasMobile(String mobile);

	/**
	 * 判断是否有终端号
	 * @param license
	 * @return
	 */
	public int hasCode(String code);
	
	/**
	 * 删除终端
	 * @param license
	 * @return
	 */
	public int delTerminal(Terminal teminal);
	/**
	 * 删除carid
	 * @param license
	 * @return
	 */
	public int delCarId(String carId,String id);
	
	/**
	 * 根据车辆ID获取终端
	 * @param id
	 * @return
	 */

	public Terminal getTerminalByCarId(String carId);
	/**
	 *  根据终端号
	 * @param code
	 * @return
	 */
	
	public Terminal getTerminalByCode(String code);
	
	public int getTerminalCount(HashMap hm);
	/**
	 * 根据车辆ID 的集合查询
	 * @param list
	 * @return
	 */
	public List<Terminal> queryTerminal(List list);
}
