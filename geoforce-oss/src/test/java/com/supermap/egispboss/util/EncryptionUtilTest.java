package com.supermap.egispboss.util;

import org.junit.Test;

public class EncryptionUtilTest {

	@Test
	public void testMd5Encry() {
		String src = "ywr2";
//		src = EncryptionUtil.base64Decode(src);
		System.out.println(src);
		String salt = "map_vcode_secret";
//		salt = EncryptionUtil.base64Encode(salt);
		String encryResult = EncryptionUtil.md5Encry(src, salt);
//		encryResult = EncryptionUtil.md5Encry(encryResult, salt);
		System.out.println(encryResult);
	}
	
	@Test
	public void testBase64Encode(){
		String src = "dXNlcm1hbmFnZXI=";
//		String result = EncryptionUtil.base64Encode(src);
//		System.out.println(result);
//		String decodeResult = EncryptionUtil.base64Decode(result);
		src = EncryptionUtil.base64Decode(src);
		System.out.println(src);
		
		
		
	}

}
