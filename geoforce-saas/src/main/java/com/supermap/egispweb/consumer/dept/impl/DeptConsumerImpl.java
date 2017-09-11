package com.supermap.egispweb.consumer.dept.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.dept.DeptConsumer;
import com.supermap.lbsp.provider.bean.JsonZTree;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.info.Dept;
import com.supermap.lbsp.provider.service.dept.DeptService;

@Component("deptConsumer")
public class DeptConsumerImpl implements DeptConsumer {
	@Reference(version="2.5.3")
	private DeptService deptService;
	@Override
	public Dept getDept(String id) {
		// TODO Auto-generated method stub
		return deptService.getDept(id);
	}

	@Override
	public Dept getDeptByCode(String code) throws Exception {
		// TODO Auto-generated method stub
		return deptService.getDeptByCode(code);
	}

	@Override
	public int hasName(String id, String name) {
		// TODO Auto-generated method stub
		return deptService.hasName(id, name);
	}

	@Override
	public List queryDept(Page page, HashMap hm) {
		// TODO Auto-generated method stub
		return deptService.queryDept(page, hm);
	}

	@Override
	public List<JsonZTree> deptTree(String deptCode) {
		// TODO Auto-generated method stub
		return deptService.deptTree(deptCode);
	}

}
