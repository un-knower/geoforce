package com.supermap.egispweb.action.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.supermap.egispweb.consumer.CarSpeeding.CarSpeedingConsumer;
import com.supermap.egispweb.consumer.PersonPlan.PersonPlanConsumer;
import com.supermap.egispweb.consumer.Terminal.TerminalConsumer;
import com.supermap.egispweb.consumer.Terminal.TerminalTypeConsumer;
import com.supermap.egispweb.consumer.alarm.AlarmConsumer;
import com.supermap.egispweb.consumer.alarm.AlarmTypeConsumer;
import com.supermap.egispweb.consumer.car.CarConsumer;
import com.supermap.egispweb.consumer.car.CarDisMsgConsumer;
import com.supermap.egispweb.consumer.carAlarmForegin.CarAlarmForeginConsumer;
import com.supermap.egispweb.consumer.carlocation.CarHistoryConsumer;
import com.supermap.egispweb.consumer.carlocation.CarLocationConsumer;
import com.supermap.egispweb.consumer.carlocation.CarRegSearchConsumer;
import com.supermap.egispweb.consumer.dataWork.DataWorkConsumer;
import com.supermap.egispweb.consumer.dept.DeptConsumer;
import com.supermap.egispweb.consumer.driver.DriverConsumer;
import com.supermap.egispweb.consumer.notice.NoticeConsumer;
import com.supermap.egispweb.consumer.person.PersonConsumer;
import com.supermap.egispweb.consumer.personSign.PersonSignConsumer;
import com.supermap.egispweb.consumer.personlocation.PersonLocationConsumer;
import com.supermap.egispweb.consumer.pic.PicConsumer;
import com.supermap.egispweb.consumer.region.RegionConsumer;
import com.supermap.egispweb.consumer.region.RegionSetConsumer;
import com.supermap.egispweb.consumer.store.StoreConsumer;
import com.supermap.egispweb.consumer.store.StoreTypeConsumer;
import com.supermap.lbsp.provider.common.Page;

public abstract class Action {
	@Autowired
	protected CarConsumer carConsumer;
	@Autowired
	protected DriverConsumer driverComsumer;
	@Autowired
	protected TerminalConsumer terminalConsumer;
	@Autowired
	protected TerminalTypeConsumer terminalTypeConsumer;
	@Autowired
	protected CarDisMsgConsumer carDisMsgConsumer;
	@Autowired
	protected DeptConsumer deptConsumer;
	@Autowired
	protected DataWorkConsumer dataWorkConsumer;
	@Autowired
	protected RegionSetConsumer regionSetConsumer;
	//@Autowired
	//private RegionConsumer regionConsumer;
	@Autowired
	protected CarHistoryConsumer carHistoryConsumer;
	@Autowired
	protected CarLocationConsumer carLocationConsumer;
	@Autowired
	protected AlarmTypeConsumer alarmTypeConsumer;
	@Autowired 
	protected CarAlarmForeginConsumer  carAlarmForeginConsumer;
	@Autowired
	protected RegionConsumer regionConsumer ;
	@Autowired
	protected AlarmConsumer alarmConsumer ;
	@Autowired
	protected CarRegSearchConsumer carRegSearchConsumer;
	@Autowired
	protected  CarSpeedingConsumer carSpeedingConsumer;
	@Autowired
	protected StoreTypeConsumer storeTypeConsumer;
	
	@Autowired
	protected  PersonConsumer personConsumer;
	@Autowired
	protected StoreConsumer storeConsumer;
	@Autowired
	protected NoticeConsumer noticeConsumer;

	@Autowired
	protected  PicConsumer picConsumer;

	@Autowired
	protected PersonSignConsumer personSignConsumer;
	
	@Autowired
	protected PersonPlanConsumer personPlanConsumer;

	@Autowired
	protected PersonLocationConsumer personLocationConsumer;
	
	protected Page page;
}
