package com.supermap.egispportal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncryptionUtilTest {

	static String testStr = "hello 松,there are some stupid things!";
	@Test
	public void testMd5Encry() {
		String result = EncryptionUtil.md5Encry(testStr, null);
		System.out.println(result);
	}

	@Test
	public void testBase64Encode() {
		fail("Not yet implemented");
	}

	@Test
	public void testBase64Decode() {
		fail("Not yet implemented");
	}

}
