/*package com.supermap.egispboss.service;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispboss.dao.ISysUpdateLogDao;
import com.supermap.egispboss.entity.SysUpdateLogEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class ISysUpdateLogServiceTest {

	@Resource
	ISysUpdateLogService sysUpdateLogService;

	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
//	@Test
	public void testSaveLog() throws ParseException {
		SysUpdateLogEntity log=new SysUpdateLogEntity();
		log.setCreateTime(new Date());
		log.setCreateUserid("22");
		log.setDeleteflag(0);
		log.setDevelopers("dev2");
		log.setImproveFunctions("imp2");
		log.setNewFunctions("nf2");
		log.setOnlineTime(sdf.parse("2016-08-09 15:00:00"));
		log.setRepairBugs("rg2");
		log.setVersionName("vn2");
		log.setVersionNumber("vnum2");
		log.setWorkHours("2.22");
		log.setUpdateTime(new Date());
		this.sysUpdateLogService.saveLog(log);
	}

//	@Test
	public void testUpdateLog() {
		SysUpdateLogEntity log=new SysUpdateLogEntity();
		log.setId("40288fcd566e005701566e0059bf0000");
		log.setNewFunctions("sfjskdlfj");
		log.setDeleteflag(0);
		this.sysUpdateLogService.updateLog(log);
	}

//	@Test
	public void testFindById() {
		this.sysUpdateLogService.findById("40288fcd566e005701566e0059bf0000");
	}

//	@Test
	public void testDeleteLog() {
		this.sysUpdateLogService.deleteLog("40288fcd566e005701566e0059bf0000");
	}

	@Test
	public void testGetLogsByParams() {
		Map<String,Object> parammap=new HashMap<String,Object>();
		parammap.put("pageNo", 1);
		parammap.put("pageSize", 1);
//		parammap.put("versionname", "n");
//		parammap.put("btime", "2016-08-09 15:00:00");
//		parammap.put("etime", "2016-08-09 15:00:00");
		Map<String, Object>  resultmap=this.sysUpdateLogService.getLogsByParams(parammap);
		System.out.println(resultmap);
	}

}
*/