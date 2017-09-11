package com.supermap.egispapi.service;

import java.util.List;
import java.util.Map;

import com.supermap.egispapi.pojo.DistributeParam;
import com.supermap.egispservice.base.pojo.LogisticsAPIResult;

/**
 * 
 * <p>Title: IDistributeService</p>
 * Description:	分单服务接口
 *
 * @author Huasong Huang
 * CreateTime: 2015-4-9 上午08:57:57
 */
public interface IDistributeService {

	
	/**
	 * 
	 * <p>Title ：batchDistribute</p>
	 * Description：批量分单服务接口
	 * @param param	分单参数
	 * @param key	用户Key
	 * @param needAreaInfo 是否需要返回区划状态等信息
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-4-9 下午02:33:56
	 */
	public List<LogisticsAPIResult> batchDistribute(DistributeParam param, String userId,String eid,String departId,boolean needAreaInfo);
	
	
	/**
	 * 
	 * <p>Title ：queryByBatch</p>
	 * Description：	按批次查询分单
	 * @param userId
	 * @param enterpriseId
	 * @param decode
	 * @param batch
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-6-11 上午09:13:15
	 */
	public Map<String,Object> queryByBatch(final String userId,String enterpriseId,String decode,final String batch,int pageNo,int pageSize);
	
	
}
