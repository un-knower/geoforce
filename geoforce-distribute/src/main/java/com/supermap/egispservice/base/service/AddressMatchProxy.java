package com.supermap.egispservice.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.egisp.addressmatch.beans.AddressMatchParam;
import com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult;
import com.supermap.egisp.addressmatch.beans.POIAddressMatchByGeoParam;
import com.supermap.egisp.addressmatch.beans.POIAddressMatchParam;
import com.supermap.egisp.addressmatch.beans.ReverseAddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispservice.base.constants.AddressMatchConstants;
import com.supermap.egispservice.base.entity.AddressEntity;
import com.supermap.egispservice.base.pojo.AddresInfoDetails;
import com.supermap.egispservice.base.pojo.AddressMatchResult;
import com.supermap.egispservice.base.pojo.Geometry4KeywordParam;
import com.supermap.egispservice.base.pojo.Point;
import com.supermap.egispservice.base.pojo.ReverseMatchResult;

@Component("addressMatchProxy")
public class AddressMatchProxy implements IAddressMatch {

	
	@Autowired
	private IAddressMatchService addressMatch;
	
	@Override
	public AddressEntity addrssMatchAndSave(String address, String userId, String eid, String dcode) {
		
		return null;
	}

	@Override
	public Map<String, Object> poiSearch(String filter, List<String> returnFields, int startRecord, int expectCount) {
		POIAddressMatchParam param = new POIAddressMatchParam();
		param.setFilter(filter);
		param.setReturnFields(returnFields);
		param.setStartRecord(startRecord);
		param.setExpectCount(expectCount);
		return this.addressMatch.poiSearch(param);
	}

	@Override
	public Map<String, Object> poiSearch(String filter, int pageNo, int pageSize, Geometry4KeywordParam geo,
			String coorType) {
		POIAddressMatchByGeoParam param = new POIAddressMatchByGeoParam();
		param.setFilter(filter);
		param.setPageNo(pageNo);
		param.setPageSize(pageSize);
		List<Point> lists = geo.getPoints();
		List<com.supermap.egisp.addressmatch.beans.Point> points = new ArrayList<com.supermap.egisp.addressmatch.beans.Point>();
		for(Point p : lists){
			com.supermap.egisp.addressmatch.beans.Point point = new com.supermap.egisp.addressmatch.beans.Point();
			point.setX(p.getX());
			point.setY(p.getY());
			points.add(point);
		}
		param.setPoints(points);
		param.setCoorType(coorType);
		param.setRadius(geo.getRadius());
		return this.addressMatch.poiSearch(param);
	}

	@Override
	public Map<String, Object> queryAddressList(String keyword, int pageNo, int pageSize, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReverseMatchResult reverseMatch(double smx, double smy, int admincode, double range) {
		ReverseAddressMatchResult amr = (ReverseAddressMatchResult) this.addressMatch.addressMatchByCoor(smx, smy, admincode, range);
		ReverseMatchResult rmr = null;
		if(null != amr ){
			rmr = new ReverseMatchResult();
			rmr.setAddress(amr.getAddress());
			rmr.setCity(amr.getCity());
			rmr.setCounty(amr.getCounty());
			rmr.setDistande(amr.getDistande());
			rmr.setName(amr.getName());
			rmr.setPoi_id(amr.getPoiId());
			rmr.setProvince(amr.getProvince());
			rmr.setSmx(amr.getX());
			rmr.setSmy(amr.getY());
		}
		return rmr;
	}

	@Override
	public AddressMatchResult search(String id, String address) {
		AddresInfoDetails amp = new AddresInfoDetails(id,address);
		return search(amp);
	}

	@Override
	public AddressMatchResult search(AddresInfoDetails param) {
		List<AddresInfoDetails> list = new ArrayList<AddresInfoDetails>();
		list.add(param);
		return search(list).get(0);
	}

	@Override
	public List<AddressMatchResult> search(List<AddresInfoDetails> params) {
		return search(params, AddressMatchConstants.SMC);
	}

	@Override
	public List<AddressMatchResult> search(List<AddresInfoDetails> params, String type) {
		List<AddressMatchParam> amps = new ArrayList<AddressMatchParam>();
		for(AddresInfoDetails aid : params){
			AddressMatchParam amp = new AddressMatchParam();
			amp.setAddress(aid.getAddress());
			amp.setAdmincode(aid.getAdmincode());
			amp.setId(aid.getId());
			amps.add(amp);
		}
		List<AddressMatchResult> searchResults = new ArrayList<AddressMatchResult>();
		List<com.supermap.egisp.addressmatch.beans.AddressMatchResult> results = this.addressMatch.addressMatch(amps, type);
		if(null != results && results.size() > 0){
			int i = 0;
			for(com.supermap.egisp.addressmatch.beans.AddressMatchResult result : results){
				com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult dam = (DefaultAddressMatchResult) result;
				AddressMatchResult amr = new AddressMatchResult();
				amr.setAddress(params.get(i++).getAddress());
				amr.setAdmincode(dam.getAdmincode());
				amr.setCity(dam.getCity());
				amr.setCounty(dam.getCounty());
				amr.setFrom(dam.getFrom());
				amr.setId(dam.getId());
				amr.setProvince(dam.getProvince());
				amr.setResultInfo(dam.getResultInfo());
				amr.setResultType(dam.getResultType());
				amr.setSmx(dam.getX());
				amr.setSmy(dam.getY());
				searchResults.add(amr);
			}
		}
		return searchResults;
	}

	@Override
	public Map<String, String> searchForCounty(double smx, double smy) {
		// TODO Auto-generated method stub
		return null;
	}

}
