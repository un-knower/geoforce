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
import com.supermap.egispapi.pojo.NetPointQueryParam;
import com.supermap.egispapi.service.IUserInfoCache;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.PointExtcolEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.pojo.NetPointInfoResult;
import com.supermap.egispservice.base.service.PointExtcolService;
import com.supermap.egispservice.base.service.PointService;

/**
 * 
 * <p>Title: NetPointServiceAction</p>
 * Description:		分单服务
 *
 * @author Huasong Huang
 * CreateTime: 2015-11-03 下午05:03:22
 */
@Controller
@RequestMapping("/np")
public class NetPointServiceAction {

	private static Logger LOGGER = Logger.getLogger(NetPointServiceAction.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private PointService pointService;
	
	@Autowired
	private PointExtcolService pointExtcolService;
	
	@Autowired
	private IAreaService areaService;
	
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
	public ModelAndView query(@RequestParam(required = true) String key,String param,
			HttpSession session) {
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		NetPointQueryParam qParam = null;
		try {
			UserEntity ue = (UserEntity) userInfoCache.findByKey(key);
			String eid = ue.getEid().getId();
			String dcode = ue.getDeptId().getCode();

			if(!StringUtils.isEmpty(param)){
				qParam = objectMapper.readValue(param, NetPointQueryParam.class);
			}else{
				qParam = new NetPointQueryParam();
			}
			LOGGER.info("## query param["+qParam+"]");
			String uid = ue.getId();
			if(qParam.getPageSize() > 50){
				qParam.setPageSize(50);
			}
			
			PointExtcolEntity extcol = this.pointExtcolService.findByUserid(ue.getId());
			
			//根据区划编号查询
			String areanum=qParam.getAreaId();
			String areaid=null;
			if(areanum!=null&&!areanum.equals("")){
				AreaEntity ae = areaService.queryByIdOrNumber(null, areanum, dcode, false);
				if(ae!=null&&ae.getId()!=null){
					areaid=ae.getId();
				}
				else {
					throw new NullPointerException("未查询到指定数据");
				}
			}
			
			Map<String, Object> netPointMapResult = this.pointService.queryAllByPage(uid, qParam.getName(), null,
					qParam.getId(), eid, dcode, qParam.getPageNo(), qParam.getPageSize(), areaid);
			
			if(netPointMapResult == null){
				throw new NullPointerException("未查询到指定数据");
			}
			List<NetPointInfoResult> netInfoResults = (List<NetPointInfoResult>) netPointMapResult.get("records");
			List<Map<String,Object>> map = buildAreaQueryResult(netInfoResults, extcol);
			netPointMapResult.put("records", map);
			Map<String,Object> results = new HashMap<String,Object>();
			results.put("resultType", 1);
			results.put("results", netPointMapResult);
			// 根据ID查询区划
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
	private List<Map<String,Object>> buildAreaQueryResult(List<NetPointInfoResult> npis,PointExtcolEntity pec){
		List<Map<String,Object>> areaMaps = new ArrayList<Map<String,Object>>();
		for(NetPointInfoResult npi : npis){
			Map<String,Object> pointMap = new HashMap<String,Object>();
			pointMap.put("id", npi.getId());
			pointMap.put("name", npi.getName());
			pointMap.put("address", npi.getAddress());
			pointMap.put("x", npi.getSmx());
			pointMap.put("y", npi.getSmy());
			pointMap.put("dutyName", npi.getDutyName());
			pointMap.put("dutyPhone", npi.getDutyPhone());
			pointMap.put("areaId", npi.getAreaId());
			pointMap.put("areaName", npi.getAreaName());
			pointMap.put("createTime", npi.getCreateTime());
			pointMap.put("updateTime", npi.getUpdateTime());
			// 组装扩展信息
			if(null != pec){
				if(!StringUtils.isEmpty(pec.getCol1())){
					pointMap.put(pec.getCol1(), npi.getCol1());
				}
				if(!StringUtils.isEmpty(pec.getCol2())){
					pointMap.put(pec.getCol2(), npi.getCol2());
				}
				if(!StringUtils.isEmpty(pec.getCol3())){
					pointMap.put(pec.getCol3(), npi.getCol3());
				}
				if(!StringUtils.isEmpty(pec.getCol4())){
					pointMap.put(pec.getCol4(), npi.getCol4());
				}
				if(!StringUtils.isEmpty(pec.getCol5())){
					pointMap.put(pec.getCol5(), npi.getCol5());
				}
				if(!StringUtils.isEmpty(pec.getCol6())){
					pointMap.put(pec.getCol6(), npi.getCol6());
				}
				if(!StringUtils.isEmpty(pec.getCol7())){
					pointMap.put(pec.getCol7(), npi.getCol7());
				}
				if(!StringUtils.isEmpty(pec.getCol8())){
					pointMap.put(pec.getCol8(), npi.getCol8());
				}
				if(!StringUtils.isEmpty(pec.getCol9())){
					pointMap.put(pec.getCol9(), npi.getCol9());
				}
				if(!StringUtils.isEmpty(pec.getCol10())){
					pointMap.put(pec.getCol10(), npi.getCol10());
				}
			}
			areaMaps.add(pointMap);
		}
		return areaMaps;
	}
	
	
}
