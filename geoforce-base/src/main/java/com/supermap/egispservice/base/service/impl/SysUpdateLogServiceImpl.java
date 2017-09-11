package com.supermap.egispservice.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.supermap.egispservice.base.dao.ISysUpdateLogDao;
import com.supermap.egispservice.base.entity.SysUpdateLogEntity;
import com.supermap.egispservice.base.service.ISysUpdateLogService;
import com.supermap.egispservice.base.util.BeanTool;


@Service("sysUpdateLogService")
public class SysUpdateLogServiceImpl implements ISysUpdateLogService {

	@Resource
	ISysUpdateLogDao sysUpdateLogDao;
	
	private static Logger LOGGER = Logger.getLogger(SysUpdateLogServiceImpl.class);
	
	@Override
	public SysUpdateLogEntity saveLog(SysUpdateLogEntity log) {
		return this.sysUpdateLogDao.save(log);
	}

	@Override
	public SysUpdateLogEntity updateLog(SysUpdateLogEntity log) {
		String id = log.getId();
		SysUpdateLogEntity log2 = this.sysUpdateLogDao.findOne(id);
		if(null == log2){
			return null;
		}
		BeanUtils.copyProperties(log, log2, BeanTool.getNullPropertyNames(log));
		log2=this.sysUpdateLogDao.save(log2);
		return log2;
	}

	@Override
	public void deleteLog(String logid) {
		SysUpdateLogEntity log=this.sysUpdateLogDao.findByIdAndDeleteflag(logid, 0);
		if(log!=null){
			log.setDeleteflag(1);
			this.sysUpdateLogDao.save(log);
		}
	}

	@Override
	public Map<String, Object> getLogsByParams(Map<String,Object> parammap) {
		
		Map<String, Object> result=null;
		
		final String btimes=parammap.get("btime")==null?null:parammap.get("btime").toString();//开始时间
		final String etimes=parammap.get("etime")==null?null:parammap.get("etime").toString();//结束时间
		final String versionname=parammap.get("versionname")==null?null:parammap.get("versionname").toString();//版本名称
		
		int pageNo=parammap.get("pageNo")==null?-1:Integer.parseInt(parammap.get("pageNo").toString());
		int pageSize=parammap.get("pageSize")==null?10:Integer.parseInt(parammap.get("pageSize").toString());
		
		
		try {
			final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Specification<SysUpdateLogEntity> spec=new  Specification<SysUpdateLogEntity>() {
				@Override
				public Predicate toPredicate(Root<SysUpdateLogEntity> root,
						CriteriaQuery<?> query, CriteriaBuilder builder) {
					
					Path<Date> onlinetimePath=root.get("onlineTime");//上线时间
					Path<String> versionNamePath=root.get("versionName");//版本名称
					Path<String> deleteflagPath=root.get("deleteflag");//删除标识
					
					List<Predicate> plist=new ArrayList<Predicate>();
					
					plist.add(builder.equal(deleteflagPath, 0));//0表示未删除
					
					if(StringUtils.isNotEmpty(btimes)){
						try {
							Date btime = sdf.parse(btimes);
							 Predicate p=builder.greaterThanOrEqualTo(onlinetimePath, btime);
							 plist.add(p);
						} catch (Exception e) {
							LOGGER.info("开始日期转换异常："+e.getMessage());
						}
					}
					
					if(StringUtils.isNotEmpty(etimes)){
						try {
							Date etime = sdf.parse(etimes);
							Predicate p=builder.lessThanOrEqualTo(onlinetimePath, etime);
							plist.add(p);
						} catch (Exception e) {
							LOGGER.info("开始日期转换异常："+e.getMessage());
						}
					}
					
					if(StringUtils.isNotEmpty(versionname)){
						Predicate p=builder.like(versionNamePath, "%" +versionname.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0));
						plist.add(p);
					}
					Predicate[] predicates = new Predicate[plist.size()];
					plist.toArray(predicates);
					query.where(predicates);
					return null;
				}
			};
			
			if(pageNo!=-1){	//分页
				PageRequest request = buildPageRequest(pageNo, pageSize,null, Direction.DESC,new String[]{"onlineTime","createTime"});
				Page<SysUpdateLogEntity> page=this.sysUpdateLogDao.findAll(spec,request);
				if(page!=null){
					result=new HashMap<String,Object>();
					result.put("totalPages", page.getTotalPages());
					result.put("page", pageNo);
					result.put("totalCount", page.getTotalElements());
					result.put("records", page.getContent());
				}
			}else{	//不分页
				Sort sort = new Sort(Direction.DESC,new String[]{"onlineTime","createTime"});
				List<SysUpdateLogEntity> list=this.sysUpdateLogDao.findAll(spec,sort);
				if(list!=null&&list.size()>0){
					result=new HashMap<String,Object>();
					result.put("records",list);
				}
			}
		} catch (Exception e) {
			LOGGER.info("查询日志信息异常:"+e.getMessage());
		}
		return result;
	}

	@Override
	public SysUpdateLogEntity findById(String logid) {
		return this.sysUpdateLogDao.findByIdAndDeleteflag(logid, 0);
	}

	
	  /**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,String defaltProperty, Direction direction,String[] propteries) {
		Sort sort = null;
		if (!StringUtils.isEmpty(defaltProperty) && "auto".equals(defaltProperty)) {
			sort = new Sort(direction, "id");
		}else{
			sort = new Sort(direction,propteries);
		} 
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
