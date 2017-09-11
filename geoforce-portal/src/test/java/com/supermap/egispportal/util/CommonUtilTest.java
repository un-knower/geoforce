package com.supermap.egispportal.util;


import org.junit.Test;

public class CommonUtilTest {

	@Test
	public void testIsTelephone() {
		boolean isTelephone = CommonUtil.isTelephone("18328358235");
		System.out.println(isTelephone);
	}

}
