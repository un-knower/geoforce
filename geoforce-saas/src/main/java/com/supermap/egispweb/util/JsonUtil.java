package com.supermap.egispweb.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * json工具类
 * 
 * @author Juanny oh
 */
public final class JsonUtil {
	/**
     * json转String
     * 
     * @param adata
     * @return String 字符串
     */
    public static String getStringFromJson(JSONObject adata) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("{");  
        for(Object key:adata.keySet()){  
            sb.append("\""+key+"\":\""+adata.get(key)+"\",");  
        }  
        String rtn = sb.toString().substring(0, sb.toString().length()-1)+"}";  
        return rtn;  
    }
    
    /**
     * jsonArray转String
     * 
     * @param adata
     * @return String 
     */
    public static String getStringFromJsonArray(JSONArray adata) {  
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        Object[] objs= adata.toArray();
        if(objs!=null&&objs.length>0){
        	for(Object obj:objs){
        		JSONObject jobj=(JSONObject) obj;
            	sb.append(getStringFromJson(jobj));
            	sb.append(",");
            }
        }
        String rtn = sb.toString()+"]";  
        return rtn;  
    }
    

	/**
     * json转Ojbect数组
     * 
     * @param str json字符串
     * @return Ojbect[] 数组
     */
     public static Object[] getJsonToArray(String str) {
         JSONArray jsonArray = JSONArray.fromObject(str);
         return jsonArray.toArray();
     }
     
     /**
      * String转JSONObject
      * 
      * @param str json字符串
      * @return JSONObject　json对象
      */
     public static JSONObject getJsonObject(String str) {
    	 JSONObject jsonArray = JSONObject.fromObject(str);
         return jsonArray;
     }
}
