package com.supermap.egispapi.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.supermap.egisp.addressmatch.beans.POIAddressMatchByGeoParam;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispapi.constants.CodeConstants;
import com.supermap.egispapi.constants.CommonConstants;
import com.supermap.egispapi.constants.Config;
import com.supermap.egispapi.constants.TemplateNames;
import com.supermap.egispapi.pojo.AdmincodeQueryParam;
import com.supermap.egispapi.pojo.CoorConvertParam;
import com.supermap.egispapi.pojo.GeocodingParams;
import com.supermap.egispapi.pojo.GeocodingResult;
import com.supermap.egispapi.service.IGeocodingService;
import com.supermap.egispservice.base.pojo.CoorConvertResult;
import com.supermap.egispservice.base.service.ICoorConvertService;

/**
 * 
 * <p>Title: GeocodingAction</p>
 * Description:	通用服务接口
 *
 * @author Huasong Huang
 * CreateTime: 2015-6-10 下午05:02:41
 */
@Controller
@RequestMapping("/cm")
public class GeocodingAction {

	private static Logger LOGGER = Logger.getLogger(GeocodingAction.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private IAddressMatchService addressMatch;
	
	@Autowired
	private Config config;
	
	@Autowired
	@Qualifier("geoService")
	private IGeocodingService geocodingService;
	
	@Autowired
	private ICoorConvertService coorConvertService;
	
	@Autowired
	private com.supermap.egispservice.geocoding.service.IGeocodingService igeocodingService;
	
	/**
	 * 
	 * <p>Title ：geocodingByKeyword</p>
	 * Description：	关键词查询接口
	 * @param param
	 * @param type
	 * @param key
	 * @param session
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-6-10 下午05:02:56
	 */
	@RequestMapping("/geocoding/keyword")
	@ResponseBody
	public Object geocodingByKeyword(@RequestParam(required = true) String param, String type, String key,
			HttpSession session) {
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		LOGGER.info("## start address match[" + param + "]");
		try {
			POIAddressMatchByGeoParam gkp = objectMapper.readValue(param, POIAddressMatchByGeoParam.class);
			if (gkp.getPageSize() > config.getMaxPageSize()) {
				gkp.setPageSize(config.getMaxPageSize());
			}
			Map<String, Object> resultMap = addressMatch.poiSearch(gkp);
			modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, resultMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
			modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
		}
		return modelAndView;
	}
	
	@RequestMapping("geocoding/address")
	@ResponseBody
	public Object addressMatch(@RequestParam(required=true)String param,String type,String key){
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		try {
			GeocodingParams gps = objectMapper.readValue(param, GeocodingParams.class);
			if(null == gps || gps.getAddresses() == null || gps.getAddresses().size() <= 0){
				throw new Exception("未找到待解析的地址信息");
			}
			// 如果返回的坐标类型为空，或不等于经纬度，将其设置为摩卡托
			if(StringUtils.isEmpty(gps.getType()) || !gps.getType().equals(CommonConstants.TYPE_SMLL)){
				gps.setType(CommonConstants.TYPE_SMC);
			}
			
			List<GeocodingResult> grs = geocodingService.addressMatch(gps.getAddresses(), gps.getType());
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("results", grs);
			modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, resultMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
			modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
		}
		return modelAndView;
	}
	
	/**
	 * 
	 * <p>Title ：coordinateConvert</p>
	 * Description：	坐标转换
	 * @param param
	 * @param type
	 * @param key
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-9-22 上午11:41:56
	 */
	@RequestMapping("coor_conv")
	@ResponseBody
	public Object coordinateConvert(@RequestParam(required=true)String param){
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		try {
			CoorConvertParam pa = objectMapper.readValue(param, CoorConvertParam.class);
			if(StringUtils.isEmpty(pa.getCoords())){
				throw new Exception("待解析坐标不允许为空");
			}
			if(StringUtils.isEmpty(pa.getFrom())){
				throw new Exception("参数[from]不允许为空");
			}
			if(StringUtils.isEmpty(pa.getTo())){
				pa.setTo("SMM");
			}
			if(!"SMM".equals(pa.getTo()) && !"SMLL".equals(pa.getTo())){
				throw new Exception("未支持的目标类型[to:"+pa.getTo()+"]");
			}
			CoorConvertResult ccr = this.coorConvertService.coorCovert(pa.getCoords(), pa.getFrom(), pa.getTo());
			if(ccr == null || ccr.getCoords() == null || ccr.getCoords().size() <= 0){
				throw new Exception("坐标转换失败");
			}
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("results", ccr);
			modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, resultMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
			modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
		}
		return modelAndView;
	}
	
	/**
	 * 城市列表
	 * @param param
	 * @param key
	 * @return
	 * @Author Juannyoh
	 * 2016-6-30上午11:32:19
	 */
	@RequestMapping("getAdminElements")
	@ResponseBody
	public Object getAdminElements(@RequestParam(required=true)String param,String key){
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		try {
			AdmincodeQueryParam admincodequery=objectMapper.readValue(param, AdmincodeQueryParam.class);
			
			if(StringUtils.isEmpty(admincodequery.getAdmincode())||StringUtils.isEmpty(admincodequery.getLevelstr())){
				throw new Exception("Admincode或Levelstr不能为空");
			}
			int level =  2;
			try {
				level = Integer.parseInt(admincodequery.getLevelstr());
				if(level<2){
					level=2;
				}
				if(level>3)level=3;
			} catch (Exception e) {
				level = 2;
			}
			List<Map<String,Object>> list = this.igeocodingService.getAdminElement(admincodequery.getAdmincode(), level);
			if(list==null||list.size()<=0){
				throw new Exception("未找到["+admincodequery.getAdmincode()+"]下的城市列表");
			}
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("results", list);
			modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, resultMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
			modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
		}
		return modelAndView;
	}
	
	/**
	 *  查询行政区边界
	 * @param param
	 * @param key
	 * @return
	 * @Author Juannyoh
	 * 2016-7-5上午10:32:38
	 */
	@RequestMapping("searchForAdminGeo")
	@ResponseBody
	public Object searchForAdminGeo(@RequestParam(required=true)String param,String key){
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		try {
			AdmincodeQueryParam admincodequery=objectMapper.readValue(param, AdmincodeQueryParam.class);
			
			if(StringUtils.isEmpty(admincodequery.getAdmincode())||StringUtils.isEmpty(admincodequery.getLevelstr())){
				throw new Exception("Admincode或Levelstr不能为空");
			}
			int level =  1;
			try {
				level = Integer.parseInt(admincodequery.getLevelstr());
				if(level<1){
					level=1;
				}
				if(level>3)level=3;
			} catch (Exception e) {
				level = 1;
			}
			Map<String, Object> result = this.igeocodingService.getAdminGeoByCode(admincodequery.getAdmincode(), level);
			if(result==null){
				throw new Exception("未找到["+admincodequery.getAdmincode()+"]下的城市边界");
			}
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("results", result);
			modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, resultMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
			modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
		}
		return modelAndView;
	}
}
