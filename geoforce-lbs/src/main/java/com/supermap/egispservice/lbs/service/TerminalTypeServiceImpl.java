package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.lbs.dao.TerminalTypeDao;
import com.supermap.egispservice.lbs.entity.Driver;
import com.supermap.egispservice.lbs.entity.TerminalType;
import com.supermap.egispservice.lbs.util.BeanTool;

@Transactional
@Service("terminalTypeService")
public class TerminalTypeServiceImpl implements TerminalTypeService {

	@Autowired
	TerminalTypeDao terminalTypeDao;
	
	@Override
	public int addTeminalType(TerminalType teminalType) {
		this.terminalTypeDao.save(teminalType);
		return 1;
	}

	@Override
	public int updateTerminalType(TerminalType teminalType) {
		TerminalType teminalType2=this.terminalTypeDao.findByid(teminalType.getId());
		BeanUtils.copyProperties(teminalType,teminalType2,BeanTool.getNullPropertyNames(teminalType));
		this.terminalTypeDao.save(teminalType2);
		return 1;
	}

	@Override
	public TerminalType getTerminalType(String id) {
		return this.terminalTypeDao.findByid(id);
	}

	@Override
	public List queryTerminalType(HashMap map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List queryTerminalType(String type) {
		Short t=Short.valueOf(type);
		return this.terminalTypeDao.findByType(t);
	}

	@Override
	public int hasName(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delTeminalType(TerminalType teminalType) {
		this.terminalTypeDao.delete(teminalType);
		return 1;
	}

	@Override
	public TerminalType getTeminalByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hasCode(String code) {
		// TODO Auto-generated method stub
		return 0;
	}

}
