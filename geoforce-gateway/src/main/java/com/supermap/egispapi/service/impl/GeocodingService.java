package com.supermap.egispapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.egisp.addressmatch.beans.AddressMatchParam;
import com.supermap.egisp.addressmatch.beans.AddressMatchResult;
import com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispapi.pojo.GeocodingParam;
import com.supermap.egispapi.pojo.GeocodingResult;
import com.supermap.egispapi.service.IGeocodingService;

/**
 * 
 * <p>Title: GeocodingService</p>
 * Description:	地址解析相关服务
 *
 * @author Huasong Huang
 * CreateTime: 2015-7-13 下午02:23:45
 */
@Component("geoService")
public class GeocodingService implements IGeocodingService {

	private static Logger LOGGER = Logger.getLogger(GeocodingService.class);
	
	@Autowired
	private IAddressMatchService addressMatch;
	
	
	
	@Override
	public List<GeocodingResult> addressMatch(List<GeocodingParam> addresses, String type) {
		LOGGER.info("## start to address match , size["+addresses.size()+"]");
		// 构建地址解析参数
		List<AddressMatchParam> aids = buildAddressInfoDetails(addresses);
		// 调用地址解析服务
		List<AddressMatchResult> amrs = this.addressMatch.addressMatch(aids, type);
		LOGGER.info("## start to return the results ...");
		// 构建地址解析返回结果
		List<GeocodingResult> geoResults = buildAddressMatchResult(amrs, addresses);
		
		return geoResults;
	}
	
	
	/**
	 * 
	 * <p>Title ：buildAddressInfoDetails</p>
	 * Description：
	 * @param addresses
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-7-13 下午02:36:11
	 */
	private List<AddressMatchParam> buildAddressInfoDetails(List<GeocodingParam> addresses){
		List<AddressMatchParam> aids = new ArrayList<AddressMatchParam>();
		for(GeocodingParam gp : addresses){
			AddressMatchParam aid = new AddressMatchParam();
			BeanUtils.copyProperties(gp, aid);
			aid.setLevel(gp.getLevel());
			aid.setQs(gp.getQs());
			aids.add(aid);
		}
		return aids;
	}
	
	/**
	 * 
	 * <p>Title ：buildAddressMatchResult</p>
	 * Description：		构建地址解析返回结果
	 * @param amrs
	 * @param type
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-7-13 下午02:43:13
	 */
	private List<GeocodingResult> buildAddressMatchResult(List<AddressMatchResult> amrs,List<GeocodingParam> addresses){
		List<GeocodingResult> geoResults = new ArrayList<GeocodingResult>();
		for(int i=0;i<amrs.size();i++){
			GeocodingResult gr = new GeocodingResult();
			gr.setId(addresses.get(i).getId());
			gr.setConfidence(-1);
			DefaultAddressMatchResult damr = (DefaultAddressMatchResult) amrs.get(i);
			int resultType = damr.getResultType();
			if(1 == resultType){
				gr.setX(amrs.get(i).getX());
				gr.setY(amrs.get(i).getY());
			}
			gr.setResultType(resultType);
			geoResults.add(gr);
		}
		return geoResults;
		
	}

}
