package com.supermap.egispservice.base.callables;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.supermap.egisp.addressmatch.beans.AddressMatchParam;
import com.supermap.egisp.addressmatch.beans.AddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispservice.base.constants.AddressMatchConstants;

public class AddressSplitCallable implements Callable<List<AddressMatchResult>> {

	private List<AddressMatchParam> addressMatchParams;
	private String threadNo = null;
	private static Logger LOGGER = Logger.getLogger(AddressSplitCallable.class);
	
	private IAddressMatchService addressMatch;
	
	public AddressSplitCallable(){}
	
	public AddressSplitCallable(List<AddressMatchParam> addressMatchParams,String threadNo,IAddressMatchService addressMatch){
		this.addressMatchParams = addressMatchParams;
		this.threadNo = threadNo;
		this.addressMatch = addressMatch;
	}
	
	@Override
	public List<AddressMatchResult> call() throws Exception {
		LOGGER.info("## thread no [" + threadNo + "] start to addressMatch...[count," + this.addressMatchParams.size()
				+ "]");
		long start = System.currentTimeMillis();
		List<AddressMatchResult> addressMatchResults = null;
		try {
			addressMatchResults = this.addressMatch.addressMatch(this.addressMatchParams, AddressMatchConstants.SMC);	
		} catch (Exception e) {
			LOGGER.error("## thread no["+this.threadNo+"]"+e.getMessage(), e);
		}finally{
			LOGGER.info("## thread no [" + this.threadNo + "] completed...comsume time["
					+ (System.currentTimeMillis() - start) + "]");
		}
		return addressMatchResults;
	}

}
