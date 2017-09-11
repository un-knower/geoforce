package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.lbs.dao.TerminalDao;
import com.supermap.egispservice.lbs.entity.Driver;
import com.supermap.egispservice.lbs.entity.Terminal;
import com.supermap.egispservice.lbs.util.BeanTool;

@Transactional
@Service("terminalService")
public class TerminalServiceImpl implements TerminalService {
	
	@Autowired
	TerminalDao terminalDao;
	
	
	@Override
	public int addTerminal(Terminal teminal) {
		this.terminalDao.save(teminal);
		return 1;
	}

	@Override
	public int updateTerminal(Terminal teminal) {
		Terminal teminal2=this.terminalDao.findById(teminal.getId());
		BeanUtils.copyProperties(teminal,teminal2,BeanTool.getNullPropertyNames(teminal));
		this.terminalDao.save(teminal2);
		return 1;
	}

	@Override
	public Terminal getTerminal(String id) {
		return this.terminalDao.findById(id);
	}

	@Override
	public List queryTerminal(HashMap map, boolean carIdIsNull) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hasMobile(String mobile) {
		List list=this.terminalDao.findByMobile(mobile);
		if(list!=null&&list.size()>0){
			return 1;
		}
		else return 0;
	}

	@Override
	public int hasCode(String code) {
		List list=this.terminalDao.findByCode(code);
		if(list!=null&&list.size()>0){
			return 1;
		}
		else return 0;
	}

	@Override
	public int delTerminal(Terminal teminal) {
		this.terminalDao.delete(teminal);
		return 1;
	}

	@Override
	public int delCarId(String carId, String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Terminal getTerminalByCarId(String carId) {
		List<Terminal> list=this.terminalDao.findByCarId(carId);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		else return null;
	}

	@Override
	public Terminal getTerminalByCode(String code) {
		List<Terminal> list=this.terminalDao.findByCode(code);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		else return null;
	}

	@Override
	public int getTerminalCount(HashMap hm) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Terminal> queryTerminal(List list) {
		return this.terminalDao.findByCarIdIn(list);
	}

}
