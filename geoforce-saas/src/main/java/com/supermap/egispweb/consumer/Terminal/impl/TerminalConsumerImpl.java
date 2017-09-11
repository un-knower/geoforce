package com.supermap.egispweb.consumer.Terminal.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispservice.lbs.entity.Terminal;
import com.supermap.egispservice.lbs.service.TerminalService;
import com.supermap.egispweb.consumer.Terminal.TerminalConsumer;
import com.supermap.lbsp.provider.common.Page;
/*import com.supermap.egispweb.consumer.Terminal.TerminalConsumer;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Terminal;
import com.supermap.lbsp.provider.service.car.CarService;
//import com.supermap.lbsp.provider.service.terminal.TerminalService;
*/@Component("terminalConsumer")
public class TerminalConsumerImpl implements TerminalConsumer {
	@Reference(version="2.5.3")
	private TerminalService terminalService;
	@Override
	public int addTerminal(Terminal teminal) {
		// TODO Auto-generated method stub
		return terminalService.addTerminal(teminal);
	}

	@Override
	public int delCarId(String carId, String id) {
		// TODO Auto-generated method stub
		return terminalService.delCarId(carId, id);
	}

	@Override
	public int delTerminal(Terminal teminal) {
		// TODO Auto-generated method stub
		return terminalService.delTerminal(teminal);
	}

	@Override
	public Terminal getTerminal(String id) {
		// TODO Auto-generated method stub
		return terminalService.getTerminal(id);
	}

	@Override
	public Terminal getTerminalByCarId(String carId) {
		// TODO Auto-generated method stub
		return terminalService.getTerminalByCarId(carId);
	}

	@Override
	public Terminal getTerminalByCode(String code) {
		// TODO Auto-generated method stub
		return terminalService.getTerminalByCode(code);
	}

	@Override
	public int getTerminalCount(HashMap hm) {
		// TODO Auto-generated method stub
		return terminalService.getTerminalCount(hm);
	}

	@Override
	public int hasCode(String code) {
		// TODO Auto-generated method stub
		return terminalService.hasCode(code);
	}

	@Override
	public int hasMobile(String mobile) {
		// TODO Auto-generated method stub
		return terminalService.hasMobile(mobile);
	}

	@Override
	public List queryTerminal(Page page, HashMap map, boolean carIdIsNull) {
		// TODO Auto-generated method stub
		return terminalService.queryTerminal(map, carIdIsNull);
	}

	@Override
	public List<Terminal> queryTerminal(List list) {
		// TODO Auto-generated method stub
		return terminalService.queryTerminal(list);
	}

	@Override
	public int updateTerminal(Terminal teminal) {
		// TODO Auto-generated method stub
		return terminalService.updateTerminal(teminal);
	}

}
