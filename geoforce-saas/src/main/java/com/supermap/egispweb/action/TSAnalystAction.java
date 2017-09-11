package com.supermap.egispweb.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egisp.ts.pojo.Point;
import com.supermap.egisp.ts.pojo.TSAnalystResult;
import com.supermap.egisp.ts.service.TransportationAnalystService;

@Controller
@RequestMapping("tsService")
public class TSAnalystAction {
	
	@Autowired
	private TransportationAnalystService tsService;
	
	
	
	@RequestMapping("getPath")
	@ResponseBody
	public Map<String,Object> getPath(HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String parameter = request.getParameter("parameter");
		try {
			if(StringUtils.isEmpty(parameter)){
				throw new Exception("参数parameter不允许为空");
			}
			JSONObject jsonObject = JSONObject.fromObject(parameter);
			Point startPoint = null;
			Point endPoint = null;
			if(jsonObject.containsKey("startPoint") && jsonObject.containsKey("endPoint")){
				JSONObject startPointObj = jsonObject.getJSONObject("startPoint");
				JSONObject endPointObj = jsonObject.getJSONObject("endPoint");
				if(null != startPointObj && null != endPointObj){
					startPoint = new Point(startPointObj.getDouble("x"),startPointObj.getDouble("y"));
					endPoint = new Point(endPointObj.getDouble("x"),endPointObj.getDouble("y"));
				}else{
					throw new Exception("startPoint和endPoint值不允许为空");
				}
			}else{
				throw new Exception("startPoint 和 endPoint 不允许为空");
			}
			Point[] passPoints = null;
			if(jsonObject.containsKey("passPoints")){
				JSONArray array = jsonObject.getJSONArray("passPoints");
				if(null != array && array.size() > 0){
					passPoints = new Point[array.size()];
					for(int i=0;i<array.size();i++){
						Point p = new Point(array.getJSONObject(i).getDouble("x"),array.getJSONObject(i).getDouble("y"));
						passPoints[i] = p;
					}
				}
			}
			TSAnalystResult result = this.tsService.pathAnalyst(startPoint, endPoint, passPoints);
			resultMap.put("result", result);
			resultMap.put("success", true);
			resultMap.put("info", null);
		} catch (Exception e) {
			resultMap.put("info", e.getMessage());
			resultMap.put("success", false);
			resultMap.put("result", null);
		}
		return resultMap;
	}
	

}
