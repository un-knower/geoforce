package com.supermap.egisp.addressmatch.service;

import java.util.List;
import java.util.Map;

import com.supermap.egisp.addressmatch.beans.AddressMatchParam;
import com.supermap.egisp.addressmatch.beans.AddressMatchResult;

/**
 * 
 * <p>Title: IAddressMatchService</p>
 * Description:		地址匹配服务接口
 *
 * @author Huasong Huang
 * CreateTime: 2015-8-17 下午02:40:30
 */
public interface IAddressMatchService {

	
	/**
	 * 
	 * <p>Title ：addressMatch</p>
	 * Description：		针对单个地址的地址解析服务
	 * @param id
	 * @param address
	 * @param type
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-17 下午02:46:17
	 */
	public AddressMatchResult addressMatch(String id,String address,String type);
	
	/**
	 * 
	 * <p>Title ：addressMatch</p>
	 * Description：		使用指定对象进行地址解析
	 * @param amp
	 * @param type
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-17 下午03:54:16
	 */
	public AddressMatchResult addressMatch(AddressMatchParam amp,String type);
	
	
	/**
	 * 
	 * <p>Title ：addressMatch</p>
	 * Description：		批量地址解析
	 * @param amps
	 * @param type
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-17 下午03:58:21
	 */
	public List<AddressMatchResult> addressMatch(List<AddressMatchParam> amps,String type);
	
	/**
	 * 
	 * <p>Title ：addressMatchByCoor</p>
	 * Description：		通过坐标进行地址解析（反向地址解析）
	 * @param smx
	 * @param smy
	 * @param admincode
	 * @param range
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 上午10:03:35
	 */
	public AddressMatchResult addressMatchByCoor(double smx, double smy,int admincode, double range);
	
	/**
	 * 
	 * <p>Title ：poiSearch</p>
	 * Description：		POI搜索
	 * @param param
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-8-19 上午10:56:14
	 */
	public Map<String,Object> poiSearch(AddressMatchParam param);
	
}
