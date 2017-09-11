package com.supermap.egispservice.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.dao.OrderHistoryDao;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.OrderBaseEntity;
import com.supermap.egispservice.base.service.IOrderHistoryService;
import com.supermap.egispservice.base.service.InfoDeptService;

@Transactional
@Service
public class OrderHistoryServiceImpl implements IOrderHistoryService {

	@Autowired
	private OrderHistoryDao orderHistoryDao;
	@Autowired
	private InfoDeptService infoDeptService;
	
	@Override
	public Map<String, Object> getHistoryOrders(final String number, final String batch,
			final String admincode, final String address, String deptId, int pageNumber,
			int pageSize, String sortType) {
		List<InfoDeptEntity> deptList = infoDeptService.getChildDepts(deptId);
		final List<String> idList = new ArrayList<String>(deptList.size());
		for(int i=0; i<deptList.size(); i++) {
			idList.add(deptList.get(i).getId());
		}
		
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType); 
		Specification<OrderBaseEntity> spec = new Specification<OrderBaseEntity>() {
			@Override
			public Predicate toPredicate(Root<OrderBaseEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList=new ArrayList<Predicate>();
				
				Predicate numberPredicate = null;
				if (StringUtils.isNoneEmpty(number)) {
					Path<String> numberExp = root.get("number");
					numberPredicate = cb.like(numberExp, "%" + number.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0));
					predicateList.add(numberPredicate);
				}
				Predicate batchPredicate = null;
				if (StringUtils.isNoneEmpty(batch)) {
					Path<String> batchExp = root.get("batch");
					batchPredicate = cb.like(batchExp, "%" + batch.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0));
					predicateList.add(batchPredicate);
				}
				//admincode?
				Predicate addressPredicate = null;
				if (StringUtils.isNoneEmpty(address)) {
					Path<String> addressExp = root.get("address");
					addressPredicate = cb.like(addressExp, "%" + address.replaceAll("_", "/_").replaceAll("%", "/%") + "%", "/".charAt(0));
					predicateList.add(addressPredicate);
				}
				Predicate idPredicate = null;
				if(idList != null && idList.size() > 0) {
					Path<String> departmentId = root.get("departmentId");
					idPredicate = departmentId.in(idList);
					predicateList.add(idPredicate);
				}
				Predicate deletePredicate = cb.equal(root.get("deleteFlag"), 0);
				predicateList.add(deletePredicate);
				
				Predicate[] predicateArray = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(predicateArray));
			}
		};
		Page<OrderBaseEntity> page = orderHistoryDao.findAll(spec, pageRequest);
		List<OrderBaseEntity> entityList = page.getContent();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotalPages());
		map.put("page", pageNumber);
		map.put("records", page.getTotalElements());
		map.put("rows", entityList);
		return map;
	}
	
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("asc".equals(sortType)) {
			sort = new Sort(Direction.ASC, "id");
		} else if ("desc".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

}
