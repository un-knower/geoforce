package com.supermap.egispservice.pathplan;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.pathplan.constant.Config;
import com.supermap.egispservice.pathplan.util.UpdateDataPathAnalystEngine;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestUpdateDataPathAnalystEngine extends TestCase {
	@Autowired
	private UpdateDataPathAnalystEngine updateDataPathAnalystEngine;
	@Autowired
	private Config config;
	
	@Test
	public void testUpdateEngine() throws Exception{
		String cpDatasetName = "CP";
		updateDataPathAnalystEngine.updateTurnTableFromCP(cpDatasetName);
        
	}
	
}
