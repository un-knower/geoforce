package com.supermap.lbsp.provider.service.carAlarmForegin;

import java.util.List;

import com.supermap.lbsp.provider.hibernate.lbsp.CarAlarmForeign;

public interface CarAlarmForeginService {
	/**
	 * 新增车辆报警关联记录
	 * 
	 * @param carAlarmForeign
	 * @return
	 */
	public int addCarAlarmForeign(CarAlarmForeign carAlarmForeign);
	/**
	 * 增加或修改关联记录
	* @Title: saveOrUpdateAlarmForeign
	* @param alarmForeign
	* @return
	* int
	* @throws
	 */
	public int saveOrUpdateCarAlarmForeign(CarAlarmForeign carAlarmForeign);

	/**
	 * 修改
	 * 
	 * @param carAlarmForeign
	 * @return
	 */
	public int updateCarAlarmForeign(CarAlarmForeign carAlarmForeign);

	/**
	 * 删除报警设置
	 * @param carAlarmForeign
	 * @return
	 */
	public int delCarAlarmForeign(CarAlarmForeign carAlarmForeign);

	/**
	 * 删除记录
	* @Title: delCarAlarmByForeign
	* @param foreignId
	* @param carId
	* @return
	* int
	* @throws
	 */
	public int delCarAlarmForeign(String foreignId,String carId);
	/**
	 * 根据外键id查询记录
	* @Title: getCarAlarmByForeign
	* @param foreignId
	* @return
	* List<CarAlarmForeign>
	* @throws
	 */
	public List<CarAlarmForeign> getCarAlarmForeigeByForeignId(String foreignId);
	/**
	 * 根据车辆id查询记录
	* @Title: getCarAlarmByCar
	* @param carId
	* @return
	* List<CarAlarmForeign>
	* @throws
	 */
	public List<CarAlarmForeign> getCarAlarmForeignByCarId(String carId);
	/**
	 * 根据条件获取记录数
	* @Title: getCarAlarmForeignCount
	* @param carId
	* @param foreignId
	* @return
	* int
	* @throws
	 */
	public Long getCarAlarmForeignCount(String carId, String foreignId);
}
