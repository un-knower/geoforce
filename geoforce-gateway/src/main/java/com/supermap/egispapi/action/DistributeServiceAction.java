package com.supermap.egispapi.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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

import com.supermap.convert.impl.BaiduCoordinateConvertImpl;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egispapi.constants.CodeConstants;
import com.supermap.egispapi.constants.Config;
import com.supermap.egispapi.constants.TemplateNames;
import com.supermap.egispapi.pojo.DistributeParam;
import com.supermap.egispapi.pojo.DistributePoint;
import com.supermap.egispapi.pojo.DistributeXYParam;
import com.supermap.egispapi.pojo.LogisticsAPIResultProvice;
import com.supermap.egispapi.pojo.OrderQueryParam;
import com.supermap.egispapi.service.IDistributeService;
import com.supermap.egispapi.service.IUserInfoCache;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.Point2D;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.pojo.LogisticsAPIResult;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.entity.Point;

/**
 * 
 * <p>Title: DistributeServiceAction</p>
 * Description:		分单服务
 *
 * @author Huasong Huang
 * CreateTime: 2015-6-10 下午05:03:22
 */
@Controller
@RequestMapping("/lm")
public class DistributeServiceAction {

	private static Logger LOGGER = Logger.getLogger(DistributeServiceAction.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private IDistributeService distributeService;
	
	@Autowired
	private Config config;
	
	@Resource
	private IAreaService areaService;
	
	
	@Resource
	private IGeocodingService geocodingService;
	
	
	@Autowired
	private IUserInfoCache userInfoCache;
	
	@RequestMapping("/distribute")
	@ResponseBody
	public ModelAndView distribute(@RequestParam(required=true)String param,HttpSession session,String key) {
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		DistributeParam dp = null;
		
		try {
			dp = objectMapper.readValue(param, DistributeParam.class);
			UserEntity ue = (UserEntity) userInfoCache.findByKey(key);
			if(ue==null){
				modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.USER_NOT_EXIST);
				modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"用户不存在");
				return modelAndView;
			}
			
			//判断是否在白名单中，不存在则不允许调用
//			if(!config.isWhiteDistributeKeys(key)){
//				modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.USER_NOT_EXIST);
//				modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"用户不存在");
//				return modelAndView;
//			}
			
			String id = ue.getId();
			String eid = ue.getEid().getId();
			String departId = ue.getDeptId().getId();
			
			List<LogisticsAPIResult> dr = distributeService.batchDistribute(dp,id,eid,departId,dp.isNeedRelArea());
			if(null == dr || dr.size() <= 0){
				modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVER_INTERFACE_EXCEPTION);
				modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"分单结果为空");
				return modelAndView;
			}
			
			
			Map<String,Object> resultMap = new HashMap<String,Object>();
			//如果需要返回省市区，则增加按坐标查询省市区的步骤（20170309）
			if(dp.isNeedProv()&&null!=dr&&dr.size()>0){
				List<LogisticsAPIResultProvice> pList=new ArrayList<LogisticsAPIResultProvice>();
				for(LogisticsAPIResult apiResult:dr){
					LogisticsAPIResultProvice pResult=convertResult(apiResult);
					if(apiResult.getX()>0&&apiResult.getY()>0){
						Map<String,String> countyMap=searchCountyByXY(apiResult.getX(),apiResult.getY(),dp.getType());
						pResult.setProvice(countyMap.get("PROVINCE"));
						pResult.setCounty(countyMap.get("COUNTY"));
						pResult.setCity(countyMap.get("CITY2"));
					}
					pList.add(pResult);
				}
				resultMap.put("results", pList);
			}else{//不需要省市区
				resultMap.put("results", dr);
			}
			modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, resultMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
			modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,""+e.getMessage());
		}
		return modelAndView;
	}
	
	
	/**
	 * 
	 * <p>Title ：query</p>
	 * Description：		查询分单
	 * @param batch		批次
	 * @param pageSizeStr分页大小
	 * @param pageNoStr	分页编号
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-6-10 下午05:12:43
	 */
	@RequestMapping("/query")
	@ResponseBody
	public ModelAndView query(@RequestParam(required = true) String key, @RequestParam(required = true) String param,
			HttpSession session) {
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		OrderQueryParam qParam = null;
		try {
			qParam = objectMapper.readValue(param, OrderQueryParam.class);
			LOGGER.info("## query param["+qParam+"]");
			if(StringUtils.isEmpty(qParam.getBatch())){
				throw new Exception("batch参数不允许为空");
			}
			int pageNo = 1;
			if(qParam.getPageNo() > 1){
				pageNo = qParam.getPageNo();
			}
			
			int pageSize = qParam.getPageSize()>1?qParam.getPageSize():10;
			if (pageSize > config.getMaxPageSize()) {
				pageSize = config.getMaxPageSize();
			}

			UserEntity ue = (UserEntity) userInfoCache.findByKey(key);
			String id = ue.getId();
			String eid = ue.getEid().getId();
			String dcode = ue.getDeptId().getCode();
			Map<String,Object> map = this.distributeService.queryByBatch(id, eid, dcode, qParam.getBatch(), pageNo, pageSize);
			modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, map);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
			modelAndView.addObject(TemplateNames.FINAL_INFO_NAME, "" + e.getMessage());
		}
		return modelAndView;
	}
	
	
	/**
	 * 根据坐标分单
	 * @param param
	 * @param session
	 * @param key
	 * @return
	 * @Author Juannyoh
	 * 2016-12-12下午2:20:22
	 */
	@RequestMapping("/distributeXY")
	@ResponseBody
	public ModelAndView distributeByPoint(@RequestParam(required=true)String param,HttpSession session,String key) {
		ModelAndView modelAndView = new ModelAndView(TemplateNames.JSON_VIEW_NAME);
		DistributeXYParam xyParams = null;
		try {
			xyParams = objectMapper.readValue(param, DistributeXYParam.class);
			UserEntity ue = (UserEntity) userInfoCache.findByKey(key);
			if(ue==null){
				modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.USER_NOT_EXIST);
				modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"用户不存在");
				return modelAndView;
			}
			
			//判断是否在白名单中，不存在则不允许调用
//			if(!config.isWhiteDistributeKeys(key)){
//				modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.USER_NOT_EXIST);
//				modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"用户不存在");
//				return modelAndView;
//			}
			
			String eid = ue.getEid().getId();
			String departCode = ue.getDeptId().getCode();
			
			//坐标点处理
			List<DistributePoint> points=xyParams.getPoints();
			if(null!=points&&points.size()>0){
				Point2D point2d[]=new Point2D[points.size()];
				
				//默认SMLL
				if(StringUtils.isEmpty(xyParams.getType())){
					xyParams.setType("SMLL");
				}
				
				//如果是超图/高德经纬度，需要转成墨卡托去查询面
				if(null!=xyParams.getType()&&xyParams.getType().equalsIgnoreCase("SMLL")){
					Point p =null;
					for(int i=0,s=points.size();i<s;i++){
						p= new Point(points.get(i).getX(),points.get(i).getY());
						p = SuperMapCoordinateConvertImpl.smLL2MC(p);
						point2d[i]=new Point2D(p.getLon(),p.getLat());
					}
				}else if(null!=xyParams.getType()&&xyParams.getType().equalsIgnoreCase("BDLL")){//百度经纬度
					BaiduCoordinateConvertImpl baiduconvert=new BaiduCoordinateConvertImpl();
					Point p =null;
					for(int i=0,s=points.size();i<s;i++){
						p= new Point(points.get(i).getX(),points.get(i).getY());
						p=baiduconvert.convertV2(p);//百度转超图经纬度
						p = SuperMapCoordinateConvertImpl.smLL2MC(p);//超图经纬度转超图墨卡托
						point2d[i]=new Point2D(p.getLon(),p.getLat());
					}
				}else {
					for(int i=0,s=points.size();i<s;i++){
						point2d[i]=new Point2D(points.get(i).getX(),points.get(i).getY());
					}
				}
				//点查面
				List<AreaEntity> arealist = this.areaService.queryAreaByPoint(point2d, eid, departCode);
				List<LogisticsAPIResult> dr =buildApiResult4XY(points,arealist);
				if(null == dr || dr.size() <= 0){
					modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVER_INTERFACE_EXCEPTION);
					modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"分单结果为空");
					return modelAndView;
				}
				Map<String,Object> resultMap = new HashMap<String,Object>();
				
				//如果需要返回省市区，则增加按坐标查询省市区的步骤（20170309）
				if(xyParams.isNeedProv()&&null!=dr&&dr.size()>0){
					List<LogisticsAPIResultProvice> pList=new ArrayList<LogisticsAPIResultProvice>();
					for(int i=0,s=dr.size();i<s;i++){
						LogisticsAPIResultProvice pResult=convertResult(dr.get(i));
						if(pResult.getX()>0&&pResult.getY()>0){
							Map<String,String> countyMap=searchCountyByXY(point2d[i].getX(),point2d[i].getY(),"SMC");
							pResult.setProvice(countyMap.get("PROVINCE"));
							pResult.setCounty(countyMap.get("COUNTY"));
							pResult.setCity(countyMap.get("CITY2"));
						}
						pList.add(pResult);
					}
					resultMap.put("results", pList);
				}else{//不需要省市区
					resultMap.put("results", dr);
				}
				modelAndView.addObject(TemplateNames.FINAL_RESULT_NAME, resultMap);
			}else{
				modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
				modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,"分单结果为空");
				return modelAndView;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			modelAndView.addObject(TemplateNames.FINAL_CODE_NAME, CodeConstants.SERVICE_PARAM_EXCEPTION);
			modelAndView.addObject(TemplateNames.FINAL_INFO_NAME,""+e.getMessage());
		}
		return modelAndView;
	}
	
	
	/**
	 * 点查面结果组装
	 * @param points
	 * @param arealist
	 * @return
	 * @Author Juannyoh
	 * 2016-12-12下午7:05:24
	 */
	public List<LogisticsAPIResult> buildApiResult4XY(List<DistributePoint> points,List<AreaEntity> arealist){
		List<LogisticsAPIResult> result=null;
		if(null!=points&&points.size()>0){
			result=new ArrayList<LogisticsAPIResult>();
			for(int i=0,s=points.size();i<s;i++){
				DistributePoint dpoint=points.get(i);
				LogisticsAPIResult ar=new LogisticsAPIResult();
				ar.setId(dpoint.getId());
				AreaEntity area=arealist.get(i);
				String areaName=area.getName();
				String areaNumber=area.getAreaNumber();
				if (StringUtils.isEmpty(areaName) && StringUtils.isEmpty(areaNumber)) {
					ar.setResultType("2");
				} else {
					ar.setAreaName(areaName);
					ar.setAreaNumber(areaNumber);
					ar.setResultType("1");
//					if(needAreaStatus){
//						ar.setStatus(area.getArea_status());
//						if(area.getArea_status()==1){//如果是停用状态，则去查询关联的区划
//							String relationid=area.getRelation_areaid();
//							if(!StringUtils.isEmpty(relationid)){
//								List<Map<String,String>> relationlist=this.areaService.findRelationAreaAttrs(area.getId(),relationid);
//								if(relationlist!=null&&relationlist.size()>0){
//									ar.setAreaName(relationlist.get(relationlist.size()-1).get("NAME"));
//									ar.setAreaNumber(relationlist.get(relationlist.size()-1).get("AREA_NUM"));
//								}
//							}
//						}
//					}
				}
				ar.setX(dpoint.getX());
				ar.setY(dpoint.getY());
				result.add(ar);
			}
		}
		return result;
	}
	
	
	/**
	 * 根据坐标获取省市区
	 * @param x 坐标x
	 * @param y 坐标y
	 * @param coorType 坐标类型（经纬度、墨卡托）
	 * @return
	 */
	private Map<String,String> searchCountyByXY(double x,double y,String coorType){
		Map<String,String> countyMap=new HashMap<String,String>();
		Point p = new Point(x,y);
		// 如果返回的坐标类型为经纬度，则进行坐标转换。
		if(coorType != null && coorType.equalsIgnoreCase("smll")){
			p = SuperMapCoordinateConvertImpl.smLL2MC(p);
		}
		countyMap=geocodingService.searchAdmincodeForCounty(p.getLon(),p.getLat());
		return countyMap;
	}
	
	
	/**
	 * 转换省市区查询结果
	 * @param apiResult
	 * @return
	 */
	private LogisticsAPIResultProvice convertResult(LogisticsAPIResult apiResult){
		LogisticsAPIResultProvice result=new LogisticsAPIResultProvice();
		result.setAreaName(apiResult.getAreaName());
		result.setAreaNumber(apiResult.getAreaNumber());
		result.setId(apiResult.getId());
		result.setResultType(apiResult.getResultType());
		result.setX(apiResult.getX());
		result.setY(apiResult.getY());
		result.setStatus(apiResult.getStatus());
		result.setRelationAreaname(apiResult.getRelationAreaname());
		result.setRelationAreanum(apiResult.getRelationAreanum());
		return result;
	}
}
