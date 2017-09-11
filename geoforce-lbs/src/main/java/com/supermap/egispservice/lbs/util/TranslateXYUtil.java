package com.supermap.egispservice.lbs.util;


/**
 * GPS坐标（WGS84）超图坐标(GCJ-02) 百度坐标（DB09）等坐标转换算法
* @ClassName: TranslateXYUtil
* @author WangShuang
* @date 2013-9-24 下午01:47:53
 */
public class TranslateXYUtil {
	private static double bd_x_pi = 3.14159265358979324 * 3000.0 / 180.0;//GCJ02转百度的偏移系数
	
	/**
	 * GCJ02坐标转百度坐标
	* @Title: bdEncrypt
	* @param gcjx
	* @param gcjy
	* @return
	* Double[]
	* @throws
	 */
	public static Double[] bdEncrypt(double gcjx,double gcjy){
	    double z = Math.sqrt(gcjx * gcjx + gcjy * gcjy) + 0.00002 * Math.sin(gcjy * bd_x_pi);  
	    double theta = Math.atan2(gcjy, gcjx) + 0.000003 * Math.cos(gcjx * bd_x_pi);  
	    
	    double bdx = z * Math.cos(theta) + 0.0065;  
	    double bdy = z * Math.sin(theta) + 0.006;
	    Double[] bdXYs = new Double[2];
	    bdXYs[0] = bdx;
	    bdXYs[1] = bdy;
	    return bdXYs;
	}
	/**
	 * 百度坐标转GCJ02
	* @Title: bdDecrypt
	* @param bdx
	* @param bdy
	* @return
	* Double[]
	* @throws
	 */
	public static Double[] bdDecrypt(double bdx,double bdy){
		double x = bdx - 0.0065, y = bdy - 0.006;
	    double z = Math.sqrt(bdx * bdx + bdy * bdy) - 0.00002 * Math.sin(bdy * bd_x_pi);
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * bd_x_pi);
	    double gcjx = z * Math.cos(theta);
	    double gcjy = z * Math.sin(theta);
	    Double[] gcjXYs = new Double[2];
	    gcjXYs[0] = gcjx;
	    gcjXYs[1] = gcjy;
	    return gcjXYs;
	}
	/**
	 * 调用百度API转换
	* @Title: bdTranDemo
	* @param ggx GPS坐标(WGS84)
	* @param ggy
	* @return
	* TestBean
	* @throws
	 */
//	private static Double[] bdTranDemo(double ggx,double ggy) {
//		String path = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=" + ggx + "&y=" + ggy + "";
//		PostMethod post = new PostMethod(path);
//		// 指定请求内容的类型  
//		post.setRequestHeader("Content-type", "text/xml; charset=GBK");
//		
//		HttpClient hc = new HttpClient();
//		HttpClientParams params = hc.getParams();
//		params.setHttpElementCharset("GBK");
//		params.setContentCharset("GBK");
//		TestBean bean = null;
//		Double[] bdxys = new Double[2];
//		try {
//			int ret = hc.executeMethod(post);
//			byte[] responseBody = post.getResponseBody();
//			String json = new String(responseBody);
//			Gson gson = new Gson();
//			bean =  gson.fromJson(json, TestBean.class);
//			byte[] tmpX = Base64.decodeBase64(bean.getX().replaceAll(" ", "").getBytes());
//			byte[] tmpY = Base64.decodeBase64(bean.getY().replaceAll(" ", "").getBytes());
//			String xt = new String(tmpX);
//			String yt = new String(tmpY);
//			
//			bdxys[0] = Double.valueOf(xt);
//			bdxys[1] = Double.valueOf(yt);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		return bdxys;
//	}
	public static void main(String[] args) {
//		
//		DB db = MongoDbHelper.getMongodb();
//		if(db == null)
//			return;
//		
//		DBCollection currentGps = db.getCollection("currentGps");
//		BasicDBObject query = new BasicDBObject();
//		query.put("gpsTime", new BasicDBObject("$lt", DateUtil.formatDateByFormat("2013-09-24 10:00:00", "yyyy-MM-dd HH:mm:ss")));
//		
//		DBCursor cur = currentGps.find(query);
//		List<Double> WGS84Xs = new ArrayList<Double>();
//		List<Double> WGS84Ys = new ArrayList<Double>();
//		List<Double> GCJ02Xs = new ArrayList<Double>();
//		List<Double> GCJ02Ys = new ArrayList<Double>();
//		
//		while (cur.hasNext()) {
//			DBObject obj = cur.next();
//			WGS84Xs.add((Double) obj.get("originalLon"));
//			WGS84Ys.add((Double) obj.get("originalLat"));
//			GCJ02Xs.add((Double) obj.get("longitude"));
//			GCJ02Ys.add((Double) obj.get("latitude"));
//		}
//		
//		for(int i=0;i<WGS84Xs.size();i++){
//			Double[] bd1 = bdTranDemo(WGS84Xs.get(i).doubleValue(), WGS84Ys.get(i).doubleValue());
//			Double[] bd2 = bdEncrypt(GCJ02Xs.get(i).doubleValue(), GCJ02Ys.get(i).doubleValue());
//			System.out.println("百度官方X："+bd1[0]+"百度官方Y："+bd1[1]);
//			System.out.println("自己计算X："+bd2[0]+"自己计算Y："+bd2[1]);
//			System.out.println("--------------------------------------------------");
//		}
		
		
	}
	
}
