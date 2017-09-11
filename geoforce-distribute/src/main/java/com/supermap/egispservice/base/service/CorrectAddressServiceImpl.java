package com.supermap.egispservice.base.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egisp.addressmatch.beans.AddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.exceptions.AreaException;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.constants.AddressMatchConstants;
import com.supermap.egispservice.base.dao.ICorrectAddressDao;
import com.supermap.egispservice.base.entity.CorrectAddressEntity;
import com.supermap.egispservice.base.pojo.CorrectAddress;


@Service("correctAddress")
public class CorrectAddressServiceImpl implements ICorrectAddressService {

	@Autowired
	private ICorrectAddressDao correctAddressDao;
	
	@Autowired
	private IAddressMatchService addressMatch;
	
	public static final int STATUS_ADD = 0;
	public static final int STATUS_MOVED = 1;
	public static final int STATUS_ALL = 2;
	
	public static final String ADDRTYPE_API="1";//地址来源类型---分单API地址解析失败的
	public static final String ADDRTYPE_WEB="2";//地址来源类型----网页端录入
	
	/**
	 * 批次生成策略
	 */
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Override
	@Transactional
	public boolean addCorrectAddress(String address, String desc, String userId, String eid, String dcode) {
		CorrectAddressEntity correctAddressEntity = new CorrectAddressEntity();
		try {
			boolean flag=exsitAddress(address,eid);
			if(flag){
				return false;
			}
			correctAddressEntity.setAddress(address);
			Date date = new Date();
			correctAddressEntity.setAddTime(date);
			correctAddressEntity.setBatch(SDF.format(date));
			correctAddressEntity.setDcode(dcode);
			correctAddressEntity.setDesc(desc);
			correctAddressEntity.setEid(eid);
			correctAddressEntity.setUserId(userId);
			
			//网页端录入的需要做地址解析和区划查询，API直接放入纠错库
			if(StringUtils.isNotEmpty(desc)&&desc.equals(ADDRTYPE_WEB)){
				// 地址匹配
				AddressMatchResult amr = null;
				try {
					amr = this.addressMatch.addressMatch("", address, AddressMatchConstants.SMC);
					if(null != amr ){
						correctAddressEntity.setX(amr.getX());
						correctAddressEntity.setY(amr.getY());
						correctAddressEntity.setStatus(STATUS_MOVED);//如果地址解析成功了，则是已纠错状态
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				// 查找区域面
				if(correctAddressEntity.getX() > 0 && correctAddressEntity.getY() > 0){
					try {
						AreaEntity ae = areaService.queryAreaByPoint(new com.supermap.egispservice.area.Point2D(correctAddressEntity.getX(),correctAddressEntity.getY()), eid, null);
						if(null != ae && !StringUtils.isEmpty(ae.getAreaNumber())){
							correctAddressEntity.setAreaNum(ae.getAreaNumber());
							correctAddressEntity.setAreaName(ae.getName());
						}
					} catch (AreaException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
			
			correctAddressEntity = this.correctAddressDao.save(correctAddressEntity);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return correctAddressEntity != null;
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> query(final String keyword,final int status,int pageNo,int pageSize, final String userId, final String eid, final String dcode) {
		
		
		PageRequest pageRequest = buildPageRequest(pageNo, pageSize, "time",new String[]{"addTime","correctTime"});
		Specification<CorrectAddressEntity> spec = new Specification<CorrectAddressEntity>(){

			@Override
			public Predicate toPredicate(Root<CorrectAddressEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				if(!StringUtils.isEmpty(userId)){
					Path<String> uId = root.get("userId");
					predicateList.add(builder.equal(uId, userId));
				}else if(!StringUtils.isEmpty(dcode)){
					Path<String> dcode = root.get("dcode");
					predicateList.add(builder.like(dcode, dcode +"%"));
				}else if(!StringUtils.isEmpty(eid)){
					Path<String> eidPath = root.get("eid");
					predicateList.add(builder.equal(eidPath, eid));
				}
				// 只有查询未纠错或已纠错的地址才加入限制条件
				if(STATUS_ADD == status || STATUS_MOVED == status){
					Path<Integer> statusPath = root.get("status");
					predicateList.add(builder.equal(statusPath, status));
				}
				// 关键词不为空
				if(!StringUtils.isEmpty(keyword)){
					// 非数字匹配地址关键字，否则匹配批次前缀
					if(!StringUtils.isNumeric(keyword)){
						Path<String> addressInfo = root.get("address");
						predicateList.add(builder.like(addressInfo, "%" + keyword.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0)));
					}else{
						Path<String> batchInfo = root.get("batch");
						predicateList.add(builder.like(batchInfo,  keyword + "%"));
					}
				}
				Predicate p[] = new Predicate[predicateList.size()];
				predicateList.toArray(p);
				query.where(p);
				return null;
			}
		};
		Page<CorrectAddressEntity> pages = this.correctAddressDao.findAll(spec, pageRequest);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(pages == null || pages.getTotalElements() <= 0){
			resultMap.put("total", 0);
			resultMap.put("pageNo", pageNo);
			resultMap.put("currentCount", 0);
			resultMap.put("records", null);
			resultMap.put("totalPages", 0);
		}else{
			resultMap.put("totalPages", pages.getTotalPages());
			resultMap.put("total",pages.getTotalElements());
			List<CorrectAddressEntity> record = pages.getContent();
			if(null == record || record.size() <= 0){
				resultMap.put("currentCount", 0);
			}else{
				resultMap.put("currentCount", record.size());
			}
			resultMap.put("records", parseResult(record));
			resultMap.put("pageNo", pages.getNumber()+1);
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * <p>Title ：parseResult</p>
	 * Description：将实体结果转换为外部结果
	 * @param list
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-8 下午03:24:56
	 */
	private List<CorrectAddress> parseResult(List<CorrectAddressEntity> list){
		List<CorrectAddress> resultList = new ArrayList<CorrectAddress>();
		for(CorrectAddressEntity cae : list){
			CorrectAddress ca = new CorrectAddress();
			try {
				ca.setId(cae.getId());
				ca.setAddress(cae.getAddress());
				ca.setAddTime(cae.getAddTime());
				ca.setAreaName(cae.getAreaName());
				ca.setAreaNum(cae.getAreaNum());
				ca.setBatch(cae.getBatch());
				ca.setCorrectTime(cae.getCorrectTime());
				ca.setDesc(cae.getDesc());
				ca.setStatus(cae.getStatus());
				ca.setX(cae.getX());
				ca.setY(cae.getY());
				resultList.add(ca);
			} catch (Exception e) {
				throw new NullPointerException(e.getMessage());
			}
		}
		return resultList;
	}
	
	public static PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType,String[] sortStr) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}else if("time".equals(sortType)){
			sort = new Sort(Direction.DESC,sortStr);
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	@Autowired
	private IAreaService areaService;
	
	private static final Logger LOGGER = Logger.getLogger(CorrectAddressServiceImpl.class);
	@Override
	@Transactional
	public boolean moveCorrectAddress(String addressId, double x, double y, String userId,String eid,String dcode) {
		CorrectAddressEntity entity = this.correctAddressDao.findOne(addressId);
		if(null == entity){
			throw new NullPointerException("[id:"+addressId+"]未找到对应的纠错地址");
		}
		try {
			AreaEntity ae = areaService.queryAreaByPoint(new com.supermap.egispservice.area.Point2D(x,y), eid, null);
			if(null != ae && !StringUtils.isEmpty(ae.getAreaNumber())){
				entity.setAreaNum(ae.getAreaNumber());
				entity.setAreaName(ae.getName());
			}
		} catch (AreaException e) {
			LOGGER.error(e.getMessage(), e);
		}
		entity.setX(x);
		entity.setY(y);
		entity.setStatus(STATUS_MOVED);
		entity.setCorrectTime(new Date());
		return this.correctAddressDao.save(entity) != null;
	}

	@Override
	@Transactional
	public boolean delCorrectAddress(String addressId, String userId) {
		if(this.correctAddressDao.exists(addressId)){
			this.correctAddressDao.deleteCorrectAddressByIdAndUserId(addressId, userId);
		}else{
			throw new NullPointerException("未找到待删除的纠错地址");
		}
		return true;
	}
	
	
	/**
	 * 判断是否已存在相同的地址
	 * @param address
	 * @return
	 * @Author Juannyoh
	 * 2017-1-10下午2:45:25
	 */
	@Override
	public boolean exsitAddress(String address,String eid){
		boolean flag=false;
		List<CorrectAddressEntity> addrlist=this.correctAddressDao.findByAddressAndEid(address,eid);
		if(null!=addrlist&&addrlist.size()>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public CorrectAddress findCorrectAddress(String address, String eid) {
		CorrectAddress addr=null;
		List<CorrectAddressEntity> addrlist=this.correctAddressDao.findByAddressAndEidAndStatusOrderByAddTimeDesc(address, eid, STATUS_MOVED);
		if(null!=addrlist&&addrlist.size()>0){
			List<CorrectAddress> addrpojolist= parseResult(addrlist);
			if(null!=addrpojolist&&addrpojolist.size()>0){
				addr=addrpojolist.get(0);
			}
		}
		return addr;
	}

//	@Override
//	public boolean isneedUseCorrectAddr(String eid) {
//		boolean flag=false;
//		long count=this.correctAddressDao.queryCountByEid(eid);
//		if(count>0){
//			flag=true;
//		}
//		return flag;
//	}
	
	

}
