package com.supermap.egispservice.base.util;

import java.security.MessageDigest;
/**
 * 
 * @description MD5加密工具
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-9-4
 * @version 1.0
 */
public class Md5Util
{

	/**
	 * MD5字符串加密方法入口
	 * 
	 * @param string
	 *            原始字符串
	 * @return String MD5加密后的字符串
	 */
	public static String md5(String string)
	{
		char hexDigits[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try
		{
			byte[] bytes = string.getBytes();
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(bytes);
			byte[] updateBytes = messageDigest.digest();
			int len = updateBytes.length;
			char myChar[] = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++)
			{
				byte byte0 = updateBytes[i];
				myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
				myChar[k++] = hexDigits[byte0 & 0x0f];
			}
			return new String(myChar);
		} catch (Exception e)
		{
			return null;
		}
	}

}
