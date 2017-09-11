package com.supermap.egisp.addressmatch.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.address.entity.AddressStandarlizeParam;
import com.supermap.address.entity.StandardAddressParam;
import com.supermap.address.service.AdministrativeDivisionService;
import com.supermap.address.util.AddressStandard;
import com.supermap.convert.impl.BaiduCoordinateConvertImpl;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egisp.addressmatch.beans.AddressMatchParam;
import com.supermap.egisp.addressmatch.beans.AddressMatchResult;
import com.supermap.egisp.addressmatch.beans.CacheRecord;
import com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult;
import com.supermap.egisp.addressmatch.beans.POIAddressMatchByGeoParam;
import com.supermap.egisp.addressmatch.beans.POIAddressMatchParam;
import com.supermap.egisp.addressmatch.beans.POIQueryFilterInfo;
import com.supermap.egisp.addressmatch.beans.Point;
import com.supermap.egisp.addressmatch.beans.ReverseAddressMatchResult;
import com.supermap.egisp.addressmatch.beans.SearchParamBySM;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egisp.addressmatch.utils.AddressMatchConstants;
import com.supermap.egisp.addressmatch.utils.Config;
import com.supermap.egispservice.redis.pojo.Record4Redis;
import com.supermap.egispservice.redis.pojo.SearchParam4Redis;
import com.supermap.egispservice.redis.pojo.SearchResult4Redis;
import com.supermap.egispservice.redis.service.IRedisService;
import com.supermap.entity.AddressMatchResults;
import com.supermap.entity.CoordinateEntity;
import com.supermap.entity.FieldNamesConstants;
import com.supermap.entity.QueryEntity;
import com.supermap.entity.QueryType;
import com.supermap.entity.RectangularEntity;
import com.supermap.search.ISearchPointByAddress;
import com.supermap.search.SearchByLucene;
import com.supermap.search.entity.SearchConstants;
import com.supermap.search.entity.SearchParam;
import com.supermap.search.impl.SearchByABC;
import com.supermap.search.impl.SearchByBaidu;
import com.supermap.search.impl.SearchByKeywords;

@Component
public class AddressMatchServiceImpl implements IAddressMatchService {

	private static Logger LOGGER = Logger.getLogger(AddressMatchServiceImpl.class);
	
	/**
	 * 地址标准化
	 */
	private static AddressStandard addressStandard = new AddressStandard();
	
	/**
	 *  关键词匹配
	 */
	private static ISearchPointByAddress searchByKeyword = new SearchByKeywords();
	
	@Autowired(required=false)
	private IRedisService redisService;
	
	/**
	 *  百度地址解析
	 */
	private static SearchByBaidu searchByBaidu = new SearchByBaidu();
	
	/**
	 * 高德地址解析
	 */
	private static SearchByABC searchByABC = new SearchByABC();
	
	/**
	 * 	百度坐标转换
	 */
	private static BaiduCoordinateConvertImpl bdConvert = new BaiduCoordinateConvertImpl();
	/**
	 * 计划对象
	 */
	private List<Integer> plan;
	
	@Autowired
	private Config config;
	
	
	@Override
	public AddressMatchResult addressMatch(String id, String address,String type) {
		AddressMatchParam amp = new AddressMatchParam();
		amp.setId(id);
		amp.setAddress(address);
		return addressMatch(amp,type);
	}

	@Override
	public AddressMatchResult addressMatch(AddressMatchParam amp,String type) {
		List<AddressMatchParam> list = new ArrayList<AddressMatchParam>();
		list.add(amp);
		List<AddressMatchResult> resultList = addressMatch(list,type);
		return (resultList == null || resultList.size() <= 0) ? null:resultList.get(0);
	}

	@Override
	public List<AddressMatchResult> addressMatch(List<AddressMatchParam> amps, String type) {
		if (null == amps || config.getBatchMaxSize() < amps.size()) {
			throw new NullPointerException("参数为空或超出了批量提交的最大限制");
		}
		// 地址标准化
		List<StandardAddressParam> saps = addressStandard(amps);

		// 组装地址解析参数
		List<SearchParam> searchParams = assembleParameter(saps, amps);
		for (int i = 0; i < plan.size(); i++) {
			switch (plan.get(i)) {
			case 1:// 关键词匹配
				keywordSearch(searchParams);
				break;
			case 2:// 超图地址匹配
				smSearch(searchParams);
				break;
			case 3:// 百度地址匹配
				bdSearch(searchParams);
				break;
			case 4:// redis
				redisSearch(searchParams);
				break;
			case 5:// 高德
				abcSearch(searchParams);
			}
		}
		// 坐标转换
		coordinateConvert(searchParams, type);
		// 更新缓存
		if(this.plan.contains(4) ){
			updateCache(searchParams);
		}
		// 组装结果
		List<AddressMatchResult> results = buildDefaultResult(searchParams, amps);
		return results;
	}
	
	/**
	 * 
	  * @Title: updateCache
	  * @Description: 
	  * 	更新redis缓存     
	  * @param searchParams       
	  * @author huanghuasong
	  * @date 2016-7-1 下午4:30:24
	 */
	private void updateCache(List<SearchParam> searchParams) {

		try {

			List<Record4Redis> list = new LinkedList<Record4Redis>();
			for (SearchParam sp : searchParams) {
				if (!StringUtils.isEmpty(sp.getFrom()) && SearchConstants.FROM_SUPERMAP.equalsIgnoreCase(sp.getFrom())) {
					String key = buildKey(sp.getAdmincode(), sp.getRoadInfo(), sp.getRoadNumber(), sp.getNameInfo(),
							sp.getQueryStrategy(), sp.getLevel());
					CacheRecord cr = new CacheRecord("", sp.getPoint().getX(), sp.getPoint().getY());
					String cacheValue = JSONObject.fromObject(cr).toString();
					LOGGER.debug("## 加入缓存[key," + key + "][value," + cacheValue + "]");
					Record4Redis r4r = new Record4Redis(key, cacheValue);
					list.add(r4r);
				}
			}
			this.redisService.addRecords(list);
		} catch (Exception e) {
			LOGGER.error("## update cache failed...["+e.getMessage()+"]");
			LOGGER.debug(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <p>Title ：buildResult</p>
	 * Description：		构建结果
	 * @param searchParams
	 * @param params
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 上午09:45:26
	 */
	public List<AddressMatchResult> buildDefaultResult(List<SearchParam> searchParams,List<AddressMatchParam> params){
		List<AddressMatchResult> amrs = new ArrayList<AddressMatchResult>();
		for(int i=0;i<searchParams.size();i++){
			SearchParam searchParam = searchParams.get(i);
			AddressMatchParam aid = params.get(i);
			DefaultAddressMatchResult amr = new DefaultAddressMatchResult();
			amr.setId(aid.getId());
			amr.setFrom(searchParam.getFrom());
			amr.setProvince(searchParam.getProvince());
			amr.setCity(searchParam.getCity());
			amr.setCounty(searchParam.getDistrict());
			amr.setAdmincode(searchParam.getAdmincode());
			if(searchParam instanceof SearchParamBySM){
				SearchParamBySM sp = (SearchParamBySM) searchParam;
				amr.setScore(sp.getScore());
			}
			com.supermap.search.entity.Point2D point = searchParam.getPoint();
			if(null == point || point.getX() <= 0 || point.getY() <= 0){
				amr.setResultType(AddressMatchConstants.RESULT_TYPE_NOT_COOR);
				amr.setResultInfo(AddressMatchConstants.NOT_COOR_INFO);
			}else{
				amr.setX(point.getX());
				amr.setY(point.getY());
				amr.setResultType(AddressMatchConstants.RESULT_TYPE_SUCCESS);
			}
			amrs.add(amr);
		}
		return amrs;
	}
	
	
	/**
	 * 
	 * <p>Title ：keywordSearch</p>
	 * Description：		关键词匹配
	 * @param searchParams
	 * Author：Huasong Huang
	 * CreateTime：2014-9-22 下午04:24:59
	 */
	private void keywordSearch(List<SearchParam> searchParams){
		LOGGER.info("## 开始关键词匹配");
		searchByKeyword.batchSearch(searchParams);
	}
	
	/**
	 * 
	  * @Title: abcSearch
	  * @Description: 
	  *      高德地址解析
	  * @param searchParams       
	  * @author huanghuasong
	  * @date 2016-12-24 上午9:59:04
	 */
	private void abcSearch(List<SearchParam> searchParams) {
		LOGGER.info("## 开始高德地址匹配");
		long start = System.currentTimeMillis();
		searchByABC.batchSearch(searchParams);
		long end = System.currentTimeMillis();
		LOGGER.info("## 高德地址匹配耗时[" + (end - start) + "毫秒]");
	}
	
	
	
	/**
	 * 
	  * @Title: redisSearch
	  * @Description: 
	  *      查询redis
	  * @param searchParams       
	  * @author huanghuasong
	  * @date 2016-7-1 下午2:11:59
	 */
	private void redisSearch(List<SearchParam> searchParams){
		List<SearchParam4Redis> keys = buildRedisKeys(searchParams);
		try {
			List<SearchResult4Redis> results = this.redisService.findAll(keys);
			for(SearchResult4Redis sr4r : results){
				String value = sr4r.getValue();
				if(!StringUtils.isEmpty(value)){
					JSONObject obj = JSONObject.fromObject(value);
					CacheRecord cr = (CacheRecord) JSONObject.toBean(obj, CacheRecord.class);
					int id = sr4r.getId();
					SearchParam sp = searchParams.get(id);
					sp.setPoint(new com.supermap.search.entity.Point2D(cr.getX(),cr.getY()));
					sp.setFrom("缓存");
					LOGGER.debug("## 从缓存获取到数据["+value+"]");
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
				
	}

	
	/**
	 * 
	  * @Title: buildRedisKeys
	  * @Description: 
	  *      构建Redis搜索的Key
	  * @param searchParams
	  * @return       
	  * @author huanghuasong
	  * @date 2016-7-1 下午2:15:17
	 */
	private List<SearchParam4Redis> buildRedisKeys(List<SearchParam> searchParams) {
		List<SearchParam4Redis> keys = new LinkedList<SearchParam4Redis>();
		for(int i=0;i<searchParams.size();i++){
			SearchParam sp = searchParams.get(i);
			if(StringUtils.isEmpty(sp.getFrom()) && !StringUtils.isEmpty(sp.getRoadInfo())){
				SearchParam4Redis sp4r = new SearchParam4Redis(i, buildKey(sp.getAdmincode(), sp.getRoadInfo(),
						sp.getRoadNumber(), sp.getNameInfo(), sp.getQueryStrategy(), sp.getLevel()));
				keys.add(sp4r);
			}
		}
		return keys;
	}
	
	/**
	 * 
	  * @Title: buildKey
	  * @Description: 
	  *      构建Redis的Key
	  * @param admincode
	  * @param roadInfo
	  * @param roadNumber
	  * @param nameInfo
	  * @param qs
	  * @param level
	  * @return       
	  * @author huanghuasong
	  * @date 2016-7-1 下午4:36:42
	 */
	private String buildKey(String admincode,String roadInfo,String roadNumber,String nameInfo,int qs,int level){
		StringBuilder keyBuilder = new StringBuilder();
		if(!StringUtils.isEmpty(admincode)){
			keyBuilder.append(admincode);
		}
		keyBuilder.append("_").append(roadInfo);
		if(!StringUtils.isEmpty(roadNumber)){
			keyBuilder.append("_").append(roadNumber);
		}
		if(!StringUtils.isEmpty(nameInfo)){
			keyBuilder.append("_").append(nameInfo);
		}
		keyBuilder.append("_").append(qs).append("_").append(level);
		return keyBuilder.toString();
	}

	/**
	 * 
	 * <p>Title ：smSearch</p>
	 * Description：		超图地址匹配
	 * @param searchParams
	 * Author：Huasong Huang
	 * CreateTime：2015-8-18 下午03:00:48
	 */
	private void smSearch(List<SearchParam> searchParams) {
		LOGGER.info("## 开始超图匹配");
		for (int i = 0; i < searchParams.size(); i++) {
			SearchParam sp = searchParams.get(i);
			// 如果地址没有被匹配才进行
			if (StringUtils.isEmpty(sp.getFrom()) && !StringUtils.isEmpty(sp.getRoadInfo())) {
				long start = System.currentTimeMillis();

				String roadInfo = sp.getRoadInfo();
				String roadNumber = sp.getRoadNumber();
				String nameInfo = sp.getNameInfo();
				String admincode = sp.getAdmincode();
				int level = sp.getLevel();
				int adminType = sp.getAdminType();
				int strategy = sp.getQueryStrategy();
				com.supermap.entity.AddressMatchResult amr = null;
				// 高精度策略
				if (level == AddressMatchConstants.QUERY_LEVEL_HIGH_ACCURACY) {
					boolean validateAdmin = validateAdmin(adminType, sp.hasProvince(), sp.hasCity(), sp.hasCounty());
					if (validateAdmin) {
						LOGGER.info("## query [level,hign-score]" + "[admincode," + admincode + "][roadInfo," + roadInfo
								+ "][roadNumber:" + roadNumber + "][nameInfo," + nameInfo + "]");
						amr = SearchByLucene.getInstance().searchByAddrName(roadInfo, roadNumber, nameInfo,
								sp.getAdmincode(),"","", true, config.getScoreHign());
					}
				} else if (level == AddressMatchConstants.QUERY_LEVEL_HIGH_MATCH) {
					if(strategy == AddressMatchConstants.QUERY_STRATEGY_KEYWORD){
						roadInfo = sp.getPreProcessAddress();
						roadNumber = null;
						admincode = null;
					}
					amr = searchByStrategy(roadInfo, roadNumber, nameInfo, admincode, adminType,sp.getTown(),
							sp.hasProvince(), sp.hasCity(), sp.hasCounty(), sp.getQueryStrategy(), config.getScoreLow());
				}

				long end = System.currentTimeMillis();
				SearchParamBySM sbs = new SearchParamBySM(sp.getAddress(), sp.getStAddress(), sp.getProvince(),
						sp.getCity(), sp.getDistrict());
				BeanUtils.copyProperties(sp, sbs);
				if (null != amr && amr.getSmx() > 0 && amr.getSmy() > 0) {
					double smx = amr.getSmx();
					double smy = amr.getSmy();
					sbs.setPoint(new com.supermap.search.entity.Point2D(smx, smy));
					sbs.setFrom(SearchConstants.FROM_SUPERMAP);
					sbs.setScore(amr.getScore());
					searchParams.set(i, sbs);
					LOGGER.info("## 超图匹配到：[" + amr.getPoi_id() + "," + amr.getAdmincode() + "," + amr.getPoi_name()
							+ "," + amr.getAddress() + "," + amr.getScore() + "][耗时," + (end - start) + "ms]");
				}else{
					LOGGER.info("## 未匹配到任何结果");
				}
			}else{
				LOGGER.info("## 未进行超图地址匹配[from:"+sp.getFrom()+"][roadInfo:"+sp.getRoadInfo()+"]");
			}
		}
	}
	
	/**
	 * 
	  * @Title: searchByStrategy
	  * @Description: 
	  *      根据策略进行搜索
	  * @param roadInfo
	  * @param roadNumber
	  * @param nameInfo
	  * @param admincode
	  * @param adminType
	  * @param hasProvince
	  * @param hasCity
	  * @param hasCounty
	  * @param queryStrategy
	  * @param score
	  * @return       
	  * @author huanghuasong
	  * @date 2016-6-23 下午4:59:26
	 */
	private com.supermap.entity.AddressMatchResult searchByStrategy(String roadInfo, String roadNumber,
			String nameInfo, String admincode, int adminType,String town, boolean hasProvince, boolean hasCity, boolean hasCounty,
			int queryStrategy, float score) {
		boolean adminRequired = false;
		boolean paramValidation = true;
		String fullAdmin = new String(StringUtils.isEmpty(admincode)?"":admincode);
		boolean adminSuccess = validateAdmin(adminType, hasProvince, hasCity, hasCounty);
		switch (queryStrategy) {
		case AddressMatchConstants.QUERY_STRATEGY_ADMIN_PREFIX:
			adminRequired = true;
			fullAdmin = null;
			break;
		case AddressMatchConstants.QUERY_STRATEGY_ADMIN_FULL:
			if (!adminSuccess) {
				paramValidation = false;
			} else {
				adminRequired = true;
			}
			break;
		case AddressMatchConstants.QUERY_STRATEGY_ADMIN_CITY_MUST:
			if (!adminSuccess) {
				if ((adminType == AddressMatchConstants.ADMIN_TYPE_COMMON) || (adminType > 0 && hasProvince)) {
					paramValidation = true;
					if(adminType > 0){
						admincode = admincode.substring(0,2);
						adminRequired = true;
					}else{
						admincode = admincode.substring(0, 4);
						adminRequired = true;
					}
					
				} else {
					paramValidation = false;
				}
			} else {
				if(adminType  == AddressMatchConstants.ADMIN_TYPE_COMMON){
					admincode = admincode.substring(0, 4);
				}else{
					admincode = admincode.substring(0,2);
				}
				adminRequired = true;
				paramValidation = true;
			}
			break;
		case AddressMatchConstants.QUERY_STRATEGY_ADMIN_SHOULD:
			adminRequired = false;
			break;
		case AddressMatchConstants.QUERY_STRATEGY_KEYWORD:
			fullAdmin = null;
			adminRequired = false;
			break;
		default:
			break;
		}

		com.supermap.entity.AddressMatchResult amr = null;
		if (paramValidation) {
			LOGGER.info("## query [strategy," + queryStrategy + "]" + "[score," + score + "][roadInfo," + roadInfo
					+ "][roadNumber:" + roadNumber + "][nameInfo," + nameInfo + "][admincode,"+ admincode
					+ "][fulladmin," + fullAdmin + "]");
			amr = SearchByLucene.getInstance().searchByAddrName(roadInfo, roadNumber, nameInfo, admincode, fullAdmin,
					town,adminRequired, score);
		} else {
			LOGGER.info("## query params about admin validation is failure ...");
		}
		return amr;
	}
	
	
	
	/**
	 * 
	  * @Title: validateAdmin
	  * @Description: 
	  *      校验行政区划是否有缺失
	  * @param adminType
	  * @param hasProvince
	  * @param hasCity
	  * @param hasCounty
	  * @return       
	  * @author huanghuasong
	  * @date 2016-6-23 下午1:54:23
	 */
	private boolean validateAdmin(int adminType, boolean hasProvince, boolean hasCity, boolean hasCounty) {
		/**
		 * 行政区划类型 0：常规行政区划 1：直辖市 2：自治区直辖县 3：省直辖县 4：特别行政区 (不进行处理)5：无区县级别
		 * 默认为常规行政区划
		 */
		boolean validation = false;
		switch (adminType) {
		case AddressMatchConstants.ADMIN_TYPE_COMMON:
			validation = (hasCity && hasCounty);
			break;
		case AddressMatchConstants.ADMIN_TYPE_MUNIPALITY:
			validation = (hasProvince && hasCounty);
			break;
		case AddressMatchConstants.ADMIN_TYPE_ZI_2_COUNTY:
			validation = (hasProvince && hasCounty);
			break;
		case AddressMatchConstants.ADMIN_TYPE_P_2_COUNTY:
			validation = (hasProvince && hasCounty);
			break;
		case AddressMatchConstants.ADMIN_TYPE_NO_COUNTY:
			validation = hasCity;
			break;

		}
		return validation;
	}
	
	
	/**
	 * 
	 * <p>Title ：bdSearch</p>
	 * Description：		百度地址解析
	 * @param searchParams
	 * Author：Huasong Huang
	 * CreateTime：2015-8-18 下午03:22:54
	 */
	private void bdSearch(List<SearchParam> searchParams){
		LOGGER.info("## 开始百度查询");
		searchByBaidu.batchSearch(searchParams);
	}
	
	
	/**
	 * 
	 * <p>Title ：coordinateConvert</p>
	 * Description：		坐标转换
	 * @param searchParams
	 * @param type
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 上午09:38:37
	 */
	private void coordinateConvert(List<SearchParam> searchParams,String type){
		long convertStart = System.currentTimeMillis();
		for (int i = 0; i < searchParams.size(); i++) {
			SearchParam sp = searchParams.get(i);
			com.supermap.search.entity.Point2D point = sp.getPoint();
			if (point != null && sp.getFrom().equals(SearchConstants.FROM_BAIDU)) {
				LOGGER.info("##待转换原始地址 ： " + point.getX() + "," + point.getY());
				com.supermap.entity.Point coor = new com.supermap.entity.Point(point.getX(), point.getY());
				coor = bdConvert.convertV2(coor);
				if(null != coor){
					if(type.equalsIgnoreCase(AddressMatchConstants.SMC)){
						coor = SuperMapCoordinateConvertImpl.smLL2MC(coor);
					}
					point.setX(coor.getLon());
					point.setY(coor.getLat());
					sp.setPoint(point);
				}
			}else if(point != null && point.getX() > 0 && point.getY() > 0 && sp.getFrom().equals(SearchConstants.FROM_SOUSOU)){
				// 对于系统识别的，需要统一转换为摩卡托坐标
				if(AddressMatchConstants.SMLL.equalsIgnoreCase(type)){
					if(!BaiduCoordinateConvertImpl.isLLPoint(new com.supermap.entity.Point(point.getX(),point.getY()))){
						com.supermap.entity.Point mcP = SuperMapCoordinateConvertImpl.smMCToLatLon(new com.supermap.entity.Point(point.getX(),point.getY()));
						point.setX(mcP.getLon());
						point.setY(mcP.getLat());
						sp.setPoint(point);
					}
				}else{
					if(BaiduCoordinateConvertImpl.isLLPoint(new com.supermap.entity.Point(point.getX(),point.getY()))){
						com.supermap.entity.Point mcP = SuperMapCoordinateConvertImpl.smLL2MC(new com.supermap.entity.Point(point.getX(),point.getY()));
						point.setX(mcP.getLon());
						point.setY(mcP.getLat());
						sp.setPoint(point);
					}
				}
			}
		}
		long convertEnd = System.currentTimeMillis();
		LOGGER.info("## 坐标转换耗时：" + (convertEnd - convertStart));
	}
	
	/**
	 * 
	 * <p>Title ：assembleParameter</p>
	 * Description：		装配地址匹配参数
	 * @param saps
	 * @param params
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-18 下午02:40:18
	 */
	private List<SearchParam> assembleParameter(List<StandardAddressParam> saps,List<AddressMatchParam> amps) {
		List<SearchParam> searchParams = new ArrayList<SearchParam>();
		for (int i = 0; i < saps.size(); i++) {
			StandardAddressParam standardAddress = saps.get(i);
			SearchParam searchParam = new SearchParam(standardAddress.getOrigAddr(), standardAddress
					.getStandardAddress(), standardAddress.getProvince(), standardAddress.getCity(), standardAddress
					.getDistrict());
			searchParam.setTown(standardAddress.getTownship());
			searchParam.setRoadInfo(standardAddress.getRoadInfo());
			searchParam.setNameInfo(standardAddress.getNameInfo());
			searchParam.setRoadNumber(standardAddress.getNumberInfo());
			if(StringUtils.isEmpty(standardAddress.getRoadInfo()) && !StringUtils.isEmpty(standardAddress.getNameInfo())){
				searchParam.setRoadInfo(standardAddress.getNameInfo());
				searchParam.setNameInfo(null);
				searchParam.setRoadNumber(null);
			}
			searchParam.setHasProvince(standardAddress.isHasOrigProvince());
			searchParam.setHasCity(standardAddress.isHasOrigCity());
			searchParam.setHasCounty(standardAddress.isHasOrigCounty());
			searchParam.setAdminType(Integer.parseInt(standardAddress.getAdminType()));
			AddressMatchParam amp = amps.get(i);
			if(0 != amp.getLevel()){
				searchParam.setLevel(amp.getLevel());
			}
			if(0 < amp.getQs()){
				searchParam.setQueryStrategy(amp.getQs());
			}else{
				searchParam.setQueryStrategy(AddressMatchConstants.QUERY_STRATEGY_ADMIN_PREFIX);
			}
			// 直辖市的城市字段为省字段
			if (standardAddress.getAdminType().equals(config.getDefaultStrategy())) {
				searchParam.setCity(standardAddress.getProvince());
			}
			// 关键词匹配将使用预处理之后的地址
			searchParam.setPreProcessAddress(standardAddress.getPreProcessAddress());
			searchParam.setAdmincode(standardAddress.getAdmincode());
			// 处理地址标准化之后的状态信息
//			processStandardAddressState(details, standardAddress);
			searchParam.setAddressKeyword(standardAddress.getAddressKeyInfo());
			searchParams.add(searchParam);
		}

		return searchParams;
	}
	
	/**
	 * 
	 * <p>Title ：processStandardAddressState</p>
	 * Description：		处理地址标准化的状态
	 * @param dop
	 * @param sap
	 * Author：Huasong Huang
	 * CreateTime：2015-8-18 下午02:44:16
	 */
//	private void processStandardAddressState(AddressMatchParam dop,StandardAddressParam sap) {
//		String adminType = sap.getAdminType();
//		// 判断是否源地址中包含了省:放宽对省份的检测
//		/*if (!sap.isHasOrigProvince()) {
//			dop.setAdminLess(true);
//		} else */
//		if (adminType.equals(AdministrativeDivisionService.COMMON_CODE)
//				|| adminType.equals(AdministrativeDivisionService.WITHOUT_DISTRICT)) {
//			// 如果地址中包含了省，并且不是直辖市，则城市字段是必须的
//			if (!sap.isHasOrigCity()) {
//				dop.setAdminLess(true);
//			}
//		}
//		// 判断是否包含了区县
//		if (!adminType.equals(AdministrativeDivisionService.WITHOUT_DISTRICT)) {
//			if(!sap.isHasOrigCounty()){
//				dop.setAdminLess(true);
//			}
//		}
//		
//		if(dop.isAdminLess()){
//			dop.setResultInfo(AddressMatchConstants.NOT_FULL_ADMIN_INFO);
//			dop.setResultType(AddressMatchConstants.RESULT_TYPE_NOT_FULL);
//			// 是否在缺少省市区的情况下，继续分单
//			dop.setNeedAddressMatch(AddressMatchConstants.IS_CONTINUE_DISTRIBUTE);
//		} else {
//			try {
//				// 提取地址关键信息
//				String withoutPrefix = addressStandard.removePrefix(sap);
//				if (withoutPrefix != null) {
//					withoutPrefix = addressStandard.removeSuffix(withoutPrefix);
//				}
//				// 地址关键信息为空，设置地址状态为不完整
//				if (withoutPrefix == null || withoutPrefix.trim().equals("")) {
//					dop.setResultType(AddressMatchConstants.RESULT_TYPE_NOT_FULL);
//					dop.setResultInfo(AddressMatchConstants.NOT_FULL_INFO);
//					dop.setNeedAddressMatch(false);
//				} else if (withoutPrefix.length() <= AddressMatchConstants.SM_LENGTH_NEED_THRESHOLD) {  	// 	地址关键信息过短则不进行分单
//					dop.setResultType(AddressMatchConstants.RESULT_TYPE_NOT_FULL); 
//					dop.setResultInfo(AddressMatchConstants.NEED_MORE_ADDRESS_KEYWORD_INFO);
//					dop.setNeedAddressMatch(false);
//				} else if (withoutPrefix.length() > AddressMatchConstants.SM_LENGTH_MAX_THRESHOLD) {		// 地址信息过长，则需要进行精简
//					withoutPrefix = addressStandard.reduceAddressKeyInfo(withoutPrefix);
//					if (withoutPrefix.length() > AddressMatchConstants.SM_LENGTH_MAX_THRESHOLD) {
//						dop.setResultType(AddressMatchConstants.RESULT_TYPE_NOT_FULL);
//						dop.setResultInfo(AddressMatchConstants.ADDRESS_FUZZY);
//						dop.setNeedAddressMatch(false);
//					}else{
//						sap.setStandardAddress(sap.getPrefix() + withoutPrefix);
//					}
//				}
//				sap.setAddressKeyInfo(withoutPrefix);
//			} catch (IOException e) {
//				LOGGER.error(e, e);
//			}
//		}
//	}
	
	
	/**
	 * 
	 * <p>Title ：addressStandard</p>
	 * Description：		地址标准化
	 * @param params
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-17 下午04:50:16
	 */
	private List<StandardAddressParam> addressStandard(List<AddressMatchParam> params){
		List<AddressStandarlizeParam> list = new ArrayList<AddressStandarlizeParam>();
		for(AddressMatchParam param : params){
			AddressStandarlizeParam asp = new AddressStandarlizeParam(null,null,param.getAddress());
			list.add(asp);
		}
		List<StandardAddressParam>  results = addressStandard.batchAddressStandard(list, false);
		return results;
	}

	public List<Integer> getPlan() {
		return plan;
	}

	public void setPlan(List<Integer> plan) {
		this.plan = plan;
	}

	@Override
	public AddressMatchResult addressMatchByCoor(double smx, double smy, int admincode, double range) {
		com.supermap.entity.Point p = new com.supermap.entity.Point(smx,smy);
		if(BaiduCoordinateConvertImpl.isLLPoint(p)){
			p = SuperMapCoordinateConvertImpl.smLL2MC(p);
		}
		com.supermap.entity.AddressMatchResult amr = SearchByLucene.getInstance().findNearPOI(p.getLon(), p.getLat(), admincode, range);
		ReverseAddressMatchResult rmr = null;
		if(null != amr){
			rmr = new ReverseAddressMatchResult();
			rmr.setAddress(amr.getAddress());
			rmr.setCity(amr.getCity());
			rmr.setCounty(amr.getCounty());
			rmr.setDistande(amr.getDistance());
			rmr.setPoiId(amr.getPoi_id());
			rmr.setProvince(amr.getProvince());
			rmr.setX(amr.getSmx());
			rmr.setY(amr.getSmy());
			rmr.setName(amr.getPoi_name());
		}
		return rmr;
	}

	@Override
	public Map<String, Object> poiSearch(AddressMatchParam param) {
		String queryFilterInfo = null;
		AddressMatchResults amResults = null;
		Map<String, Object> resultMap = null;
		POIQueryFilterInfo filterInfo = null;
		List<String> returnFields = null;
		int startRecord = 0;
		int expectCount = 10;
		int pageSize = 10;
		if (param instanceof POIAddressMatchParam) {
			POIAddressMatchParam poiParam = (POIAddressMatchParam) param;
			returnFields = poiParam.getReturnFields();
			startRecord = poiParam.getStartRecord();
			expectCount = poiParam.getExpectCount();
			queryFilterInfo = poiParam.getFilter();
			// 解析查询过滤条件
			filterInfo = parseQueryFilterInfo(queryFilterInfo);
		} else if (param instanceof POIAddressMatchByGeoParam) {
			POIAddressMatchByGeoParam poiParam = (POIAddressMatchByGeoParam) param;
			pageSize = poiParam.getPageSize();
			startRecord = (poiParam.getPageNo() - 1)* poiParam.getPageSize();
			expectCount = poiParam.getPageSize();
			filterInfo = parseQueryFilterInfo(poiParam.getFilter());
			List<Point> points = poiParam.getPoints();
			// 圆选范围查询
			if (points != null && points.size() == 1) {
				resultMap = circleQuery(filterInfo, poiParam);
				return resultMap;
			} else if (null != points && points.size() > 1) {
				filterInfo.setLeftDownX(points.get(0).getX());
				filterInfo.setLeftDownY(points.get(0).getY());
				filterInfo.setRightUpX(points.get(1).getX());
				filterInfo.setRightUpY(points.get(1).getY());
			}
		}else{
			throw new NullPointerException("未支持的查询类型");
		}
		if(startRecord < 0 ){
			startRecord = 0;
		}
		amResults = keywordQuery(filterInfo, startRecord, expectCount);
		resultMap = new HashMap<String, Object>();
		if (amResults != null && amResults.getCurruntCount() > 0) {
			resultMap.put("totalCount", amResults.getTotalCount());
			resultMap.put("currentCount", amResults.getCurruntCount());
			resultMap.put("page",pageSize);
		} else {
			resultMap.put("totalCount", 0);
			resultMap.put("currentCount", 0);
			resultMap.put("page", 0);
			resultMap.put("results", null);
			return resultMap;
		}
		List<com.supermap.entity.AddressMatchResult> results = amResults.getRecords();
		List<Map<String, Object>> resultInfos = new ArrayList<Map<String, Object>>();

		if(null == returnFields || returnFields.size() < 1){
			returnFields = buildKeywordReturnFields();
		}
		
		for (int ii = 0; ii < results.size(); ii++) {
			Map<String, Object> resultItem = new HashMap<String, Object>();

			for (int iii = 0; iii < returnFields.size(); iii++) {
				if (returnFields.get(iii).equalsIgnoreCase("SMX")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getSmx());
				} else if (returnFields.get(iii).equalsIgnoreCase("SMY")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getSmy());
				} else if (returnFields.get(iii).equalsIgnoreCase("NAME")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getPoi_name());
				} else if (returnFields.get(iii).equalsIgnoreCase("PY")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getPy());
				} else if (returnFields.get(iii).equalsIgnoreCase("POI_ID")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getPoi_id());
				} else if (returnFields.get(iii).equalsIgnoreCase("ADDRESS")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getAddress());
				} else if (returnFields.get(iii).equalsIgnoreCase("ADMINCODE")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getAdmincode());
				} else if (returnFields.get(iii).equalsIgnoreCase("PROVINCE")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getProvince());
				} else if (returnFields.get(iii).equalsIgnoreCase("CITY")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getCity());
				} else if (returnFields.get(iii).equalsIgnoreCase("COUNTY")) {
					resultItem.put(returnFields.get(iii), results.get(ii).getCounty());
				} else {
					resultItem.put(returnFields.get(iii), null);
				}
			}
			resultInfos.add(resultItem);
		}
		resultMap.put("results", resultInfos);
		return resultMap;
	}
	
	
	private List<String> buildKeywordReturnFields(){
		List<String> returnFields = new ArrayList<String>();
		returnFields.add("SMX");
		returnFields.add("SMY");
		returnFields.add("NAME");
		returnFields.add("PY");
		returnFields.add("POI_ID");
		returnFields.add("ADDRESS");
		returnFields.add("ADMINCODE");
		returnFields.add("PROVINCE");
		returnFields.add("CITY");
		returnFields.add("COUNTY");
		return returnFields;
	}
	
	/**
	 * 
	 * <p>Title ：parseQueryFilterInfo</p>
	 * Description：		将filter字符串转换为对象
	 * @param attributeFilter
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 上午11:14:11
	 */
	private POIQueryFilterInfo parseQueryFilterInfo(String queryFilter) {
		POIQueryFilterInfo poiQueryFilterInfo = new POIQueryFilterInfo();
		int leftAdmincode = -1;
		int rightAdmincode = -1;
		LOGGER.info("## 查询条件[" + queryFilter + "]");
		// 该条件查询并不支持OR之类的连接方式
		String filter = queryFilter.replaceAll(" ", "");
		String[] clauses = filter.toUpperCase().split("AND");
		
		double leftDownX = -1;
		double leftDownY = -1;
		double rightUpX = -1;
		double rightUpY = -1;
		for (int j = 0; j < clauses.length; j++) {
			String clausesItem = clauses[j];
			clausesItem = clausesItem.replaceAll(" ", "");
			int startSecnd = clausesItem.indexOf('\'');
			int endSecond = clausesItem.lastIndexOf('\'');
			
			if(clausesItem.startsWith("NAME") || clausesItem.startsWith("CLASS")){
				String keyword = clausesItem.substring(startSecnd +1,endSecond);
				keyword = keyword.replaceAll("%", "");
				if(null != keyword && keyword.trim().length() > 0){
					if(poiQueryFilterInfo.getType() != null){
						poiQueryFilterInfo.setType("MIX");
					}else if(clausesItem.startsWith("NAME")){
						poiQueryFilterInfo.setType("NAME");
						poiQueryFilterInfo.setLikeKeyword(keyword);
					}else if(clausesItem.startsWith("CLASS")){
						poiQueryFilterInfo.setType("CLASS");
						poiQueryFilterInfo.setClassWord(keyword);
					}
				}else{
					// 未提供任何关键词及分类结果
				}
			}else if (clausesItem.startsWith("ADMINCODE")) {
				if (clausesItem.contains(">")) {
					leftAdmincode = Integer.parseInt(clausesItem.substring(startSecnd + 1, endSecond));
				} else if (clausesItem.contains("<")) {
					rightAdmincode = Integer.parseInt(clausesItem.substring(startSecnd + 1, endSecond));
				}
			} else if (clausesItem.startsWith("X")) {
				if (clausesItem.contains(">")) {
					leftDownX = Double.parseDouble(clausesItem.substring(startSecnd + 1, endSecond));
				} else if (clausesItem.contains("<")) {
					rightUpX = Double.parseDouble(clausesItem.substring(startSecnd + 1, endSecond));
				}
			} else if (clausesItem.startsWith("Y")) {
				if (clausesItem.contains(">")) {
					leftDownY = Double.parseDouble(clausesItem.substring(startSecnd + 1, endSecond));
				} else if (clausesItem.contains("Y")) {
					rightUpY = Double.parseDouble(clausesItem.substring(startSecnd + 1, endSecond));
				}
			}
		}
		poiQueryFilterInfo.setLeftAdmincode(leftAdmincode);
		poiQueryFilterInfo.setRightAdmincode(rightAdmincode);
		poiQueryFilterInfo.setLeftDownX(leftDownX);
		poiQueryFilterInfo.setLeftDownY(leftDownY);
		poiQueryFilterInfo.setRightUpX(rightUpX);
		poiQueryFilterInfo.setRightUpY(rightUpY);
		return poiQueryFilterInfo;
	}

	/**
	 * 
	 * <p>Title ：keywordQuery</p>
	 * Description：		根据过滤对象进行关键词查询
	 * @param queryFilterInfo
	 * @param startRecord
	 * @param expectCount
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 上午11:24:35
	 */
	private AddressMatchResults keywordQuery(POIQueryFilterInfo queryFilterInfo, int startRecord, int expectCount) {
		AddressMatchResults amrs = null;
		RectangularEntity rect = new RectangularEntity();
		CoordinateEntity leftDown = new CoordinateEntity();
		leftDown.setX(queryFilterInfo.getLeftDownX());
		leftDown.setY(queryFilterInfo.getLeftDownY());
		rect.setLetfDownPoint(leftDown);
		CoordinateEntity rightUp = new CoordinateEntity();
		rightUp.setX(queryFilterInfo.getRightUpX());
		rightUp.setY(queryFilterInfo.getRightUpY());
		rect.setRightUpPoint(rightUp);

		LOGGER.info("## 查询参数 KEY_WORD = " + queryFilterInfo.getLikeKeyword() + ",class = "
				+ queryFilterInfo.getClassWord() + ",type = " + queryFilterInfo.getType() + ",leftAdmincode = "
				+ queryFilterInfo.getLeftAdmincode() + " , rightAdmincode = " + queryFilterInfo.getRightAdmincode());
		if (queryFilterInfo.getType().equals("NAME")) {
			try {

				// 如果过滤查询未查询到，则进行关键词查询
				if (null == amrs || amrs.getTotalCount() <= 0) {
					// 需要加入超图地址的特殊处理
					amrs = SearchByLucene.getInstance().search(FieldNamesConstants.FULL_ADDRESS,
							queryFilterInfo.getLikeKeyword(), queryFilterInfo.getLeftAdmincode(),
							queryFilterInfo.getRightAdmincode(), startRecord, expectCount,
							SearchByLucene.FUZZY_ADMIN_RANGE_QUERY,
							com.supermap.search.constants.AddressMatchConstants.OSP_QUERY_SCORE, rect);
				}
			} catch (Exception e) {
				LOGGER.error("##搜索发生异常", e);
			}
		} else if (queryFilterInfo.getType().equals("CLASS")) {
			amrs = SearchByLucene.getInstance().searchByPrefix(FieldNamesConstants.POI_CODE,
					queryFilterInfo.getClassWord(), queryFilterInfo.getLeftAdmincode(),
					queryFilterInfo.getRightAdmincode(), rect, startRecord, expectCount);
		} else if (queryFilterInfo.getType().equals("MIX")) {
			List<QueryEntity> qes = buildQueryEntitys(queryFilterInfo);
			amrs = SearchByLucene.getInstance().searchByMix(qes, startRecord, expectCount);
		}
		return amrs;
	}
	
	
	/**
	 * 
	 * <p>Title ：circleQuery</p>
	 * Description：		圆选范围查询
	 * @param queryFilterInfo
	 * @param param
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 下午02:42:12
	 */
	private Map<String, Object> circleQuery(POIQueryFilterInfo queryFilterInfo, POIAddressMatchByGeoParam param) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Point> points = param.getPoints();
		coors2SMC(points);
		// 圆形范围搜索
		List<QueryEntity> querys = buildCircleQueryEntity(queryFilterInfo);
		List<com.supermap.entity.AddressMatchResult> amrList = SearchByLucene.getInstance().queryByCircle(querys,
				points.get(0).getX(), points.get(0).getY(), param.getRadius(), true);

		if (amrList == null || amrList.size() <= 0) {
			resultMap.put("totalCount", 0);
			resultMap.put("currentCount", 0);
			resultMap.put("page", param.getPageNo());
			resultMap.put("pois", null);
			return resultMap;
		}
		LOGGER.info("## 圆选查询到结果数[" + amrList.size() + "]");
		resultMap.put("totalCount", amrList.size());
		int pageNo = param.getPageNo();
		int pageSize = param.getPageSize();
		int size = amrList.size();
		int start = (pageNo - 1) * pageSize;
		int end = start + pageSize;
		if (start >= size || start < 0) {
			start = 0;
			pageNo = 0;
		}
		if (end >= size) {
			end = size;
		}
		amrList = amrList.subList(start, end);
		for (int ii = 0; ii < amrList.size(); ii++) {
			if (!StringUtils.isEmpty(param.getCoorType())
					&& AddressMatchConstants.SMLL.equalsIgnoreCase(param.getCoorType())) {
				com.supermap.entity.Point p = new com.supermap.entity.Point(amrList.get(ii).getSmx(), amrList.get(ii)
						.getSmy());
				p = SuperMapCoordinateConvertImpl.smMCToLatLon(p);
				amrList.get(ii).setSmx(p.getLon());
				amrList.get(ii).setSmy(p.getLat());
			}
		}
		resultMap.put("currentCount", amrList.size());
		resultMap.put("page", pageNo);
		resultMap.put("pois", resultConvert(amrList));
		return resultMap;
	}
	
	/**
	 * 
	 * <p>Title ：resultConvert</p>
	 * Description：		结果转换
	 * @param amrResults
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 下午03:04:34
	 */
	private List<Map<String,Object>> resultConvert(List<com.supermap.entity.AddressMatchResult> amrResults){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(com.supermap.entity.AddressMatchResult amr : amrResults){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("poi_id", amr.getPoi_id());
			map.put("x", amr.getSmx());
			map.put("y", amr.getSmy());
			map.put("name", amr.getPoi_name());
			map.put("address", amr.getAddress());
			map.put("admincode", amr.getAdmincode());
			map.put("province", amr.getProvince());
			map.put("city", amr.getCity());
			map.put("county", amr.getCounty());
			map.put("code", amr.getPoi_typename());
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 
	 * <p>Title ：coors2SMC</p>
	 * Description：		将所有坐标转换为摩卡托
	 * @param points
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 下午02:57:25
	 */
	private void coors2SMC(List<Point> points){
		for (int i = 0; i < points.size(); i++) {
			com.supermap.entity.Point point = new com.supermap.entity.Point(points.get(i).getX(), points.get(i).getY());
			if (point.getLon() > -180 && point.getLon() < 180 && point.getLat() > -90 && point.getLat() < 90) {
				point = SuperMapCoordinateConvertImpl.smLL2MC(point);
			}
			points.get(i).setX(point.getLon());
			points.get(i).setY(point.getLat());
		}
	}
	
	/**
	 * 
	 * <p>Title ：buildCircleQueryEntity</p>
	 * Description：		构建圆选查询实体
	 * @param queryFilterInfo
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 下午02:45:49
	 */
	private List<QueryEntity> buildCircleQueryEntity(POIQueryFilterInfo queryFilterInfo) {
		String likeKeyword = queryFilterInfo.getLikeKeyword();
		List<QueryEntity> qes = new ArrayList<QueryEntity>();
		if (null != likeKeyword && !"".equals(likeKeyword) && !"all".equalsIgnoreCase(likeKeyword)) {
			QueryEntity queryEntity = new QueryEntity();
			queryEntity.setFieldName(FieldNamesConstants.POI_NAME_ST);
			queryEntity.setType(QueryType.PHRASE);
			queryEntity.setParam(new String[] { likeKeyword });
			qes.add(queryEntity);
		}
		String classWord = queryFilterInfo.getClassWord();
		if (null != classWord && !"".equals(classWord) && !"all".equalsIgnoreCase(classWord)) {
			QueryEntity queryEntity = new QueryEntity();
			queryEntity.setFieldName(FieldNamesConstants.POI_CODE);
			queryEntity.setType(QueryType.PREFIX);
			queryEntity.setParam(new String[] { classWord });
			qes.add(queryEntity);
		}
		return qes;
	}
	
	/**
	 * 
	 * <p>Title ：buildQueryEntitys</p>
	 * Description：		构建关键词查询对象
	 * @param queryFilterInfo
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 上午11:24:01
	 */
	private List<QueryEntity> buildQueryEntitys(POIQueryFilterInfo queryFilterInfo){
		List<QueryEntity> qes = new ArrayList<QueryEntity>();
		// 构建关键词查询
		String nameKeword = queryFilterInfo.getLikeKeyword();
		if(null != nameKeword && !"".equals(nameKeword)){
			QueryEntity qeKeyword = new QueryEntity();
			qeKeyword.setFieldName(FieldNamesConstants.POI_NAME_ST);
			qeKeyword.setType(QueryType.PHRASE);
			qeKeyword.setParam(new String[]{nameKeword});
			qes.add(qeKeyword);
		}
		// 构建分类搜索
		String poiCode = queryFilterInfo.getClassWord();
		if(null != poiCode && !"".equals(poiCode)){
			QueryEntity classCode = new QueryEntity();
			classCode.setFieldName(FieldNamesConstants.POI_CODE);
			classCode.setType(QueryType.PREFIX);
			classCode.setParam(new String[]{poiCode});
			qes.add(classCode);
		}
		// 构建Admincode查询
		int leftAdmincode = queryFilterInfo.getLeftAdmincode();
		int rightAdmincode = queryFilterInfo.getRightAdmincode();
		if(leftAdmincode > 0 && rightAdmincode > 0 && leftAdmincode <= rightAdmincode){
			QueryEntity adminRangeQuery = new QueryEntity();
			adminRangeQuery.setFieldName(FieldNamesConstants.ADMINCODE);
			adminRangeQuery.setType(QueryType.RANGE);
			adminRangeQuery.setParamType(Integer.class);
			adminRangeQuery.setParam(new String[]{leftAdmincode+"",rightAdmincode+""});
			qes.add(adminRangeQuery);
		}
		// 构建X范围的分类查询
		double leftDownX = queryFilterInfo.getLeftDownX();
		double rightUpX = queryFilterInfo.getRightUpX();
		if(leftDownX > 0 && rightUpX > 0 && leftDownX < rightUpX){
			QueryEntity xRangeQuery = new QueryEntity();
			xRangeQuery.setFieldName(FieldNamesConstants.SMX);
			xRangeQuery.setType(QueryType.RANGE);
			xRangeQuery.setParamType(Double.class);
			xRangeQuery.setParam(new String[]{leftDownX+"",rightUpX+""});
			qes.add(xRangeQuery);
			
		}
		// 构建Y范围内的分类查询
		double leftDownY = queryFilterInfo.getLeftDownY();
		double rightUpY = queryFilterInfo.getRightUpY();
		if(leftDownY > 0 && rightUpY > 0 && leftDownY < rightUpY){
			QueryEntity yRangeQuery = new QueryEntity();
			yRangeQuery.setFieldName(FieldNamesConstants.SMY);
			yRangeQuery.setType(QueryType.RANGE);
			yRangeQuery.setParamType(Double.class);
			yRangeQuery.setParam(new String[]{leftDownY+"",rightUpY+""});
			qes.add(yRangeQuery);
		}
		
		return qes;
	}
	
	
}
