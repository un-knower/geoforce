package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.AddressEntity;
import com.supermap.egispservice.base.pojo.AddresInfoDetails;
import com.supermap.egispservice.base.pojo.AddressMatchResult;
import com.supermap.egispservice.base.pojo.Geometry4KeywordParam;
import com.supermap.egispservice.base.pojo.ReverseMatchResult;

/**
 * 
 * <p>Title: IAddressMatch</p>
 * Description:		地址匹配
 *
 * @author Huasong Huang
 * CreateTime: 2014-9-22 上午09:57:49
 */
public interface IAddressMatch {

	/**
	 * 
	 * <p>Title ：search</p>
	 * Description：		单条地址匹配
	 * @param id		ID	
	 * @param address	详细地址信息
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-22 上午10:08:31
	 */
	public AddressMatchResult search(String id,String address);
	
	/**
	 * 
	 * <p>Title ：search</p>
	 * Description：		单条地址匹配，使用AddressMatchParam组装参数，逻辑与search(String id,String address)一致
	 * @param param
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-22 上午10:08:54
	 */
	public AddressMatchResult search(AddresInfoDetails param);
	
	
	/**
	 * 
	 * <p>Title ：search</p>
	 * Description：		批量地址匹配
	 * @param params
	 * @return		默认返回带有摩卡托类型的地址解析
	 * Author：Huasong Huang
	 * CreateTime：2014-9-22 上午10:10:27
	 */
	public List<AddressMatchResult> search(List<AddresInfoDetails>  params);
	
	/**
	 * 
	 * <p>Title ：search</p>
	 * Description：		批量地址解析
	 * @param params
	 * @param type		坐标类型
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-7-13 下午02:44:37
	 */
	public List<AddressMatchResult> search(List<AddresInfoDetails>  params,String type);
	
	/**
	 * 
	 * <p>Title ：reverseMatch</p>
	 * Description：		反向地址解析
	 * @param smx
	 * @param smy
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-25 上午10:49:13
	 */
	public ReverseMatchResult reverseMatch(double smx,double smy,int admincode,double range);
	
	/**
	 * 
	 * <p>Title ：poiSearch</p>
	 * Description：		POI搜索
	 * @param filter	
	 * @param returnFields
	 * @param startRecord
	 * @param expectCount
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-18 下午03:34:33
	 */
	public Map<String,Object> poiSearch(String filter,List<String> returnFields,int startRecord,int expectCount);
	
	public Map<String,Object> poiSearch(String filter,int pageNo,int pageSize,Geometry4KeywordParam geo,String coorType);
	
	/**
	 * 
	 * <p>Title ：searchForCounty</p>
	 * Description：		通过坐标查询区县
	 * @param smx
	 * @param smy
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-19 下午05:08:02
	 */
	public Map<String,String> searchForCounty(double smx,double smy);
	
	
	/**
	 * 
	 * <p>Title ：addrssMatchAndSave</p>
	 * Description：		地址解析并存储
	 * @param address
	 * @param userId
	 * @param eid
	 * @param dcode
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-5-14 上午10:21:19
	 */
	public AddressEntity addrssMatchAndSave(String address,String userId,String eid,String dcode);
	
	/**
	 * 
	 * <p>Title ：queryAddressList</p>
	 * Description：查询地址列表
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-5-14 上午11:44:37
	 */
	public Map<String,Object> queryAddressList(final String keyword,int pageNo,int pageSize,final String userId);
}
