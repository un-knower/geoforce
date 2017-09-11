package com.supermap.egispweb.consumer.Terminal.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispservice.lbs.entity.TerminalType;
import com.supermap.egispservice.lbs.service.TerminalTypeService;
import com.supermap.egispweb.consumer.Terminal.TerminalTypeConsumer;
import com.supermap.lbsp.provider.common.Page;
/*import com.supermap.egispweb.consumer.Terminal.TerminalTypeConsumer;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.TerminalType;
import com.supermap.lbsp.provider.service.terminal.TerminalService;
import com.supermap.lbsp.provider.service.terminal.TerminalTypeService;*/
@Component("terminalTypeConsumer")
public class TerminalTypeConsumerImpl implements TerminalTypeConsumer{
	@Reference(version="2.5.3")
	private TerminalTypeService terminalTypeService;
	@Override
	public int addTeminalType(TerminalType teminalType) {
		// TODO Auto-generated method stub
		return terminalTypeService.addTeminalType(teminalType);
	}

	@Override
	public int delTeminalType(TerminalType teminalType) {
		// TODO Auto-generated method stub
		return terminalTypeService.delTeminalType(teminalType);
	}

	@Override
	public TerminalType getTeminalByName(String name) {
		// TODO Auto-generated method stub
		return terminalTypeService.getTeminalByName(name);
	}

	@Override
	public TerminalType getTerminalType(String id) {
		// TODO Auto-generated method stub
		return terminalTypeService.getTerminalType(id);
	}

	@Override
	public int hasCode(String code) {
		// TODO Auto-generated method stub
		return terminalTypeService.hasCode(code);
	}

	@Override
	public int hasName(String name) {
		// TODO Auto-generated method stub
		return terminalTypeService.hasName(name);
	}

	@Override
	public List queryTerminalType(Page page, HashMap map) {
		// TODO Auto-generated method stub
		return terminalTypeService.queryTerminalType(map);
	}

	@Override
	public int updateTerminalType(TerminalType teminalType) {
		// TODO Auto-generated method stub
		return terminalTypeService.updateTerminalType(teminalType);
	}

	@Override
	public List queryTerminalType(String type) {
		// TODO Auto-generated method stub
		return terminalTypeService.queryTerminalType(type);
	}

}
