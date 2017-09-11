package com.supermap.egispservice.base.service;

import com.supermap.egispservice.base.pojo.CoorConvertResult;

public interface ICoorConvertService {

	

	/**
	 * 
	 * <p>Title ：coorCovert</p>
	 * Description：	坐标转换接口
	 * @param coords 待转换坐标
	 * @param from	源坐标类型(支持：SMM,SMLL,GPS,BD)
	 * @param to	目标类型（超图坐标，支持：SMM,SMLL）
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-9-22 下午02:13:48
	 */
	public CoorConvertResult coorCovert(String coords,String from,String to);
	
}
