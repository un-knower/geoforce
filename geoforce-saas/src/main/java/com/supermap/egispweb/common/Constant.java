package com.supermap.egispweb.common;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 业务静态变量
* @ClassName: Constant
* @author WangShuang
* @date 2012-4-5 下午04:47:19
 */
public class Constant {
	private static Properties prop = new Properties();

	/**API接口 分页 每页最大条数  50*/
	public static final int MAX_PAGE_SIZE = 50;
	/**API接口 id集合个数最大个数 100*/
	public static final int MAX_ARRAY_SIZE = 100;
	/**位置服务-调用百度地图的key*/
	public static final String BAIDU_MAP_KEY;
	/**巡店--图片发布路径*/
	public static final String PERSON_IMG_PATH;
	static {
		String file_name = (new Constant()).getClass().getClassLoader()
				.getResource("config.properties").getFile();
		try {
			prop.load(new FileInputStream(file_name));

		} catch (Exception ex) {
			try {
				file_name = file_name.replaceAll("%20", " ");
				prop.load(new FileInputStream(file_name));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		BAIDU_MAP_KEY = prop.getProperty("baidu.map.key");
		PERSON_IMG_PATH = prop.getProperty("person.img.path");
	}
}
