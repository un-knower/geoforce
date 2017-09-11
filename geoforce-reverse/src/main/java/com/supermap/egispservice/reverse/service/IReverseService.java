package com.supermap.egispservice.reverse.service;

import com.supermap.egispservice.base.entity.AddressInfo;
import com.supermap.egispservice.base.entity.PointParam;

import java.util.List;

public interface IReverseService{
    List<AddressInfo> pointToAddress(List<PointParam> points,String type) throws Exception;
}