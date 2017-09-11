package com.supermap.egispservice.lbs.util;


/**
 * GPS数据 方向角解析工具类
* @ClassName: DirectionUtil
* @author WangShuang
* @date 2013-7-2 下午01:57:21
 */
public class DirectionUtil {

	public static String getDirection(double direction) {
		String s = "";
		if (direction >= 0 && direction <= 10) {
			s = "南向北行驶";//"从南往北";
		} else if (direction >= 350 && direction < 360) {
			s = "南向北行驶"; //"从南往北";
		} else if (direction > 10 && direction < 80) {
			s = "东北方向行驶";//"往东北方向";
		} else if (direction >= 80 && direction <= 100) {
			s = "西向东行驶";//"从西往东";
		} else if (direction > 100 && direction < 170) {
			s = "东南方向行驶";//"往东南方向";
		} else if (direction >= 170 && direction <= 190) {
			s = "北向南行驶";//"从北往南";
		} else if (direction > 190 && direction < 260) {
			s = "西南方向行驶";//"往西南方向";
		} else if (direction >= 260 && direction <= 280) {
			s = "东向西行驶";//"从东往西";
		} else if (direction > 280 && direction < 350) {
			s = "西北方向行驶";//"往西北方向";
		}
		return s;
	}
}
