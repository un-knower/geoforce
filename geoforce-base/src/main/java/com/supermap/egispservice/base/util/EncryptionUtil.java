package com.supermap.egispservice.base.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 
 * <p>Title: EncryptionUtil</p>
 * Description:  加密工具
 *
 * @author Huasong Huang
 * CreateTime: 2014-8-21 下午04:52:36
 */
public class EncryptionUtil {
	
	
	/**
	 * 
	 * <p>Title ：encry</p>
	 * Description：		使用MD5对文本进行加密
	 * @param src
	 * @param salt
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-21 下午04:53:26
	 */
	public static String md5Encry(String src,String salt){
		String result = new  Md5Hash(src,salt).toString();
		return result;
	}
	/**
	 * 
	 * <p>Title ：base64Encode</p>
	 * Description：		使用Base64进行加密
	 * @param src
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-21 下午05:00:53
	 */
	public static String base64Encode(String src){
		return Base64.encodeToString(src.getBytes());
	}
	/**
	 * 
	 * <p>Title ：base64Decode</p>
	 * Description：使用Base64进行解码
	 * @param src
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-8-21 下午05:03:18
	 */
	public static String base64Decode(String src){
		return Base64.decodeToString(src.getBytes());
	}
	
	
}
