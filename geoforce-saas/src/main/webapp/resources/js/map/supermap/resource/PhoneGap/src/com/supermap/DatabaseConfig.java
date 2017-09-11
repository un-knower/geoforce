package com.supermap;

import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseConfig {

	public DatabaseConfig() {
		// TODO Auto-generated constructor stub

	}

	public static JSONObject getConfig(Context ctx) throws JSONException {
		JSONObject config = new JSONObject();

		try {
			HashMap<String, Object> map = queryConfig(ctx);
			config = prepareConfig(map);
		} catch (Exception e) {
			System.out.println("there is a error in DatabaseConfig.getConfig :"
					+ e.getMessage());
		}
		RequestControl.metaconfig = config;

		return config;
	}

	public static HashMap<String, Object> queryConfig(Context ctx) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			DBOpenHelp dbOpenHelper = new DBOpenHelp(ctx, RequestControl.dir
					+ RequestControl.dbName);
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

			String sql = "select name,value from metadata";
			Cursor cursor = db.rawQuery(sql, new String[] {});
			if (cursor.moveToFirst()) {
				do {
					String name = cursor.getString(0);
					String value = cursor.getString(1);
					map.put(name, value);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			dbOpenHelper.close();
		} catch (Exception e) {
			System.out.println("there is a error in DatabaseConfig.getConfig :"
					+ e.getMessage());
		}

		return map;
	}

	public static JSONObject prepareConfig(HashMap<String, Object> map)
			throws JSONException {
		JSONObject config = new JSONObject();

		JSONArray resolutions = StringToArray((String) map.get("resolutions"));
		JSONArray scales = StringToArray((String) map.get("scales"));
		
		for (int i = 0; i < scales.length(); i++) {
			double temp = scales.getDouble(i);
			scales.put(i, 1/temp);
		}
		
		String referResolution = resolutions.getString(0);
		String referScale = scales.getString(0);
		
		String crs_wkt = (String) map.get("crs_wkt");
		String unit = comfirmUnit(crs_wkt);
		
		String dpi = getDPI(referScale, referResolution, unit);
		JSONObject bounds = getBounds((String) map.get("bounds"));
		String compatible = (String) map.get("compatible");

		config.put("scales", scales);
		config.put("dpi", dpi);
		config.put("unit", unit);
		config.put("bounds", bounds);
		config.put("resolutions", resolutions);
		config.put("compatible", compatible);

		return config;
	}

	/*add 2013.3.20*/
	private static String comfirmUnit(String unitStr)
	{
		String unit="";
		if(unitStr.indexOf("METER")>0)
		{
			unit = "meter";
		}
		else if(unitStr.indexOf("DEGREE")>0)
		{
			unit = "degree";
		}
		else if(unitStr.indexOf("DECIMAL_DEGREE")>0)
		{
			unit = "dd";
		}
		else if(unitStr.indexOf("CENTIMETER")>0)
		{
			unit = "centimeter";
		}
		else if(unitStr.indexOf("DECIMETER")>0)
		{
			unit = "decimeter";
		}
		else if(unitStr.indexOf("FOOT")>0)
		{
			unit = "foot";
		}
		else if(unitStr.indexOf("INCH")>0)
		{
			unit = "inch";
		}
		else if(unitStr.indexOf("KILOMETER")>0)
		{
			unit = "kilometer";
		}
		else if(unitStr.indexOf("MILE")>0)
		{
			unit = "mile";
		}
		else if(unitStr.indexOf("MILIMETER")>0)
		{
			unit = "milimeter";
		}
		else if(unitStr.indexOf("MINUTE")>0)
		{
			unit = "minute";
		}
		else if(unitStr.indexOf("RADIAN")>0)
		{
			unit = "radian";
		}
		else if(unitStr.indexOf("SECOND")>0)
		{
			unit = "second";
		}
		else if(unitStr.indexOf("YARD")>0)
		{
			unit = "yard";
		}
		return unit;
	}
	
	public static JSONArray StringToArray(String str) throws JSONException {
		JSONArray arr = new JSONArray();
		String[] strs = str.split(",");
		int length = strs.length;
		double[] doubleArray = new double[length];
		for (int i = 0; i < length; i++) {
			double b = Double.parseDouble(strs[i]);
			doubleArray[i] = b;
		}
		//进行排序,默认是升序排序，我们需要的是降序数组，所以从后往前取
		Arrays.sort(doubleArray);
		
		for (int j = length-1; j >= 0; j--) {
			arr.put(doubleArray[j]);
		}
		
		return arr;
	}

	public static JSONObject getBounds(String str) throws JSONException {
		JSONObject bounds = new JSONObject();

		String[] strs = str.split(",");
		bounds.put("left", Double.parseDouble(strs[0]));
		bounds.put("bottom", Double.parseDouble(strs[1]));
		bounds.put("right", Double.parseDouble(strs[2]));
		bounds.put("top", Double.parseDouble(strs[3]));

		return bounds;
	}

	public static String getDPI(String scale, String resolution, String unit) {
		 //10000 是 0.1毫米与米的转换。DPI的计算公式：Viewer / DPI *  0.0254 * 10000 = ViewBounds * scale ，公式中的10000是为了提高计算结果的精度，以下出现的ratio皆为如此。
		int ratio = 10000;
		//系统默认为6378137米，即WGS84参考系的椭球体长半轴。
	    int datumAxis = 6378137;
		if(unit.equals("degree") || unit.equals("degrees") || unit.equals("dd")){
	        return Double.toString(0.0254*ratio / Double.parseDouble(resolution) / Double.parseDouble(scale) / ((Math.PI * 2 * datumAxis) / 360) / ratio);
		}else{
			return Double.toString(0.0254 / Double.parseDouble(scale)
					/ Double.parseDouble(resolution));
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
