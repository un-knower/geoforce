package com.supermap.egispweb.service;

import java.util.UUID;

/**
 *
 * <p>Title:GenerateShortUUID</p>
 * <p>Description: </p>
 *    UUID22位工具
 * <p>Company: 成都地图慧科技有限公司</p>
 * @author Yun Zhou
 * @date 2017年4月20日20:00:21
 */
public class GenerateShortUUID {
    private final static char[] DIGITS64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String compressUUID(UUID uuid) {
        return toIDString(uuid.getMostSignificantBits()) + toIDString(uuid.getLeastSignificantBits());
    }

    private static String toIDString(long l) {
        char[] buf = "00000000000".toCharArray(); // 限定11位长度
        int length = 11;
        long least = 61L; // 0x0000003FL
        do {
            buf[--length] = DIGITS64[(int) (l & least)]; // l & least取低6位
            /* 无符号的移位右移
             * 使用“>>>”进行移位
             */
            l >>>= 6;
        } while (l != 0);
        return new String(buf);
    }

    public static UUID convertStr2UUID(String uuid) {
        StringBuilder akBuilder = new StringBuilder();
        akBuilder.append(uuid.substring(0, 8));
        akBuilder.append("-");
        akBuilder.append(uuid.substring(8, 12));
        akBuilder.append("-");
        akBuilder.append(uuid.substring(12, 16));
        akBuilder.append("-");
        akBuilder.append(uuid.substring(16, 20));
        akBuilder.append("-");
        akBuilder.append(uuid.substring(20));
        return UUID.fromString(akBuilder.toString());
    }
    
    public static void main(String[] args) {
    	System.out.println(compressUUID(GenerateShortUUID.convertStr2UUID("59d8f6087dfe467f9ad543d68b7da64a")));
	}
}
