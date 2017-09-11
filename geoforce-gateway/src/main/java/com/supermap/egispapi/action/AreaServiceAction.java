package com.supermap.egispapi.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.supermap.egispapi.constants.CodeConstants;
import com.supermap.egispapi.constants.TemplateNames;
import com.supermap.egispapi.pojo.AreaQueryParam;
import com.supermap.egispapi.service.IUserInfoCache;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.geocoding.service.IGeocodingService;

/**
 * 
 * <p>Title: AreaServiceAction</p>
 * Description:		分单服务
 *
 * @author Huasong Huang
 * CreateTime: 2015-6-10 下午05:03:22
 */
@Controller
@RequestMapping("/am")
public class AreaServiceAction {

	private static Logger LOGGER = Logger.getLogger(AreaServiceAction.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private IAreaService areaService;
	
	@Autowired
	private IGeocodingService geocodingService;
	
	@Autowired
	private IUserInfoCache userInfoCache;
	
	/**
	 * 
	 * <p>Title ：query</p>
	 * Description：		查询区划
	 * @param param		查询参数
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-6-10 下午05:12:43
	 */
	@RequestMapping("/query")
	@ResponseBody
	public ModelAndView query(@RequestParam(required = true) String key,@RequestParam(required = true) String param,
			HttpSession session) {
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		AreaQueryParam qParam = null;
		try {
			UserEntity ue = (UserEntity) userInfoCache.findByKey(key);
			String eid = ue.getEid().getId();
			String dcode = ue.getDeptId().getCode();

			qParam = objectMapper.readValue(param, AreaQueryParam.class);
			qParam.setIncludPoints(false);//20170411全部不返回点
			LOGGER.info("## query param["+qParam+"]");
			// 根据ID查询区划
			List<AreaEntity> aes = null;
			if(!StringUtils.isEmpty(qParam.getId()) || !StringUtils.isEmpty(qParam.getNumber())){
				AreaEntity ae = areaService.queryByIdOrNumber(qParam.getId(), qParam.getNumber(), dcode, qParam.isIncludPoints());
				if(null != ae){
					aes = new ArrayList<AreaEntity>();
					aes.add(ae);
				}
			}else if(!StringUtils.isEmpty(qParam.getName())){
				aes = areaService.queryOneByName(qParam.getName(), null, null, eid, dcode, qParam.isIncludPoints());
			}else{
				throw new NullPointerException("name、number、id不允许都为空");
			}
			
			if(null == aes || aes.size() <= 0){
				throw new NullPointerException("未找到指定数据");
			}
			List<Map<String,Object>> list = buildAreaQueryResult(aes);
			Map<String,Object> results = new HashMap<String,Object>();
			results.put("resultType", 1);
			results.put("results", list);
			modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, results);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
			modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
		}
		return modelAndView;
	}
	
	
	/**
	 * 
	 * <p>Title ：buildAreaQueryResult</p>
	 * Description：	构建区划查询结果
	 * @param aes
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-11-2 下午03:42:49
	 */
	private List<Map<String,Object>> buildAreaQueryResult(List<AreaEntity> aes){
		List<Map<String,Object>> areaMaps = new ArrayList<Map<String,Object>>();
		for(AreaEntity ae : aes){
			Map<String,Object> areaMap = new HashMap<String,Object>();
			String admincode = ae.getAdmincode();
			if(!StringUtils.isEmpty(admincode)){
				Map<String,Object> countyMap = this.geocodingService.getCountyByAdmincode(admincode);
				if(null != countyMap){
					areaMap.put("province", countyMap.get("PROVINCE"));
					areaMap.put("city", countyMap.get("CITY"));
					areaMap.put("county", countyMap.get("COUNTY"));
					areaMap.put("admincode", admincode);
				}
			}
			areaMap.put("name", ae.getName());
			areaMap.put("number", ae.getAreaNumber());
			areaMap.put("createTime", ae.getCreate_time());
			areaMap.put("updateTime", ae.getUpdate_time());
			areaMap.put("points", ae.getPoints());
			areaMap.put("parts", ae.getParts());
			areaMap.put("id", ae.getId());
			areaMap.put("status", ae.getArea_status());
			areaMap.put("relationAreaname", ae.getRelation_areaname());
			areaMap.put("relationAreanum", ae.getRelation_areanumber());
			areaMaps.add(areaMap);
		}
		return areaMaps;
	}
	
	
}
