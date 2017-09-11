package com.supermap.egispservice.base.callables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.supermap.egisp.addressmatch.beans.AddressMatchParam;
import com.supermap.egisp.addressmatch.beans.AddressMatchResult;
import com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispservice.base.constants.AddressMatchConstants;
import com.supermap.egispservice.base.pojo.CorrectAddress;
import com.supermap.egispservice.base.service.ICorrectAddressService;

public class AddressSplitCallable implements Callable<List<AddressMatchResult>> {

	private List<AddressMatchParam> addressMatchParams;
	private String threadNo = null;
	private static Logger LOGGER = Logger.getLogger(AddressSplitCallable.class);
	
	private IAddressMatchService addressMatch;
	
	private ICorrectAddressService correctAddressService;
	
	private String eid;
	
	public AddressSplitCallable(){}
	
	public AddressSplitCallable(List<AddressMatchParam> addressMatchParams,String threadNo,IAddressMatchService addressMatch,ICorrectAddressService correctAddressService,String eid){
		this.addressMatchParams = addressMatchParams;
		this.threadNo = threadNo;
		this.addressMatch = addressMatch;
		this.correctAddressService=correctAddressService;
		this.eid=eid;
	}
	
	@Override
	public List<AddressMatchResult> call() throws Exception {
		LOGGER.info("## thread no [" + threadNo + "] start to addressMatch...[count," + this.addressMatchParams.size()
				+ "]");
		long start = System.currentTimeMillis();
		List<AddressMatchResult> addressMatchResults = null;
		try {
//			addressMatchResults = this.addressMatch.addressMatch(this.addressMatchParams, AddressMatchConstants.SMC);	
			
			//单条调用，判断是否在纠错库中有数据
			if(null!=this.correctAddressService){
			    long time1 = System.currentTimeMillis();
				LOGGER.info("query user correctAddrs...");
				if(null!=this.addressMatchParams&&this.addressMatchParams.size()>0){
					addressMatchResults=new ArrayList<AddressMatchResult>();
					for(AddressMatchParam param:addressMatchParams){
						CorrectAddress correct=this.correctAddressService.findCorrectAddress(param.getAddress(), this.eid);
						if(null!=correct){//纠错库有，则不用地址解析
							DefaultAddressMatchResult matchresult=new DefaultAddressMatchResult();
							matchresult.setX(correct.getX());
							matchresult.setY(correct.getY());
							matchresult.setId(param.getId());
							matchresult.setResultType(AddressMatchConstants.RESULT_TYPE_SUCCESS);
							addressMatchResults.add(matchresult);
						}else{
							AddressMatchResult result=this.addressMatch.addressMatch(param, AddressMatchConstants.SMC);
							if(null!=result){
								addressMatchResults.add(result);
							}else{
								DefaultAddressMatchResult dmar = new DefaultAddressMatchResult();
								dmar.setId(param.getId());
								addressMatchResults.add(dmar);
							}
						}
					}
				}
				long time2 = System.currentTimeMillis();
				LOGGER.info("query user correctAddrs end...["+(time2-time1)+"ms]");
			}
			else{ //全部走地址解析
			    long time1 = System.currentTimeMillis();
                LOGGER.info("query user addressMatch...");
				addressMatchResults = this.addressMatch.addressMatch(this.addressMatchParams, AddressMatchConstants.SMC);	
				long time2 = System.currentTimeMillis();
                LOGGER.info("query user addressMatch end...["+(time2-time1)+"ms]");
			}
			
		} catch (Exception e) {
			LOGGER.error("## thread no["+this.threadNo+"]"+e.getMessage(), e);
		}finally{
			LOGGER.info("## thread no [" + this.threadNo + "] completed...comsume time["
					+ (System.currentTimeMillis() - start) + "]");
		}
		return addressMatchResults;
	}

}
