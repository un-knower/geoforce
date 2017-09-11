/*package com.supermap.egispservice.base.service;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispservice.base.pojo.AddressMatchResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class AddressMatchImplTest {
	
	@Autowired
	private IAddressMatch addressMatch;

	@Test
	public void testSearchStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchAddresInfoDetails() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchListOfAddresInfoDetails() {
		AddressMatchResult amr = addressMatch.search("1", "北京市朝阳区团结湖中路南三条1号楼2门401室");
		System.out.println(amr.getFrom());
	}

}
*/