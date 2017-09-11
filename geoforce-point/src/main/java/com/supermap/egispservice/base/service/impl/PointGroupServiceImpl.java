package com.supermap.egispservice.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.dao.PointGroupDao;
import com.supermap.egispservice.base.entity.PointGroupEntity;
import com.supermap.egispservice.base.entity.PointStyleEntity;
import com.supermap.egispservice.base.service.PointGroupService;
import com.supermap.egispservice.base.util.BeanTool;

@Transactional(rollbackFor=Exception.class)
@Service("pointGroupService")
public class PointGroupServiceImpl implements PointGroupService {

	@Autowired
	PointGroupDao pointgroupdao;
	
	@Override
	public List<PointGroupEntity> queryAllGroups(final String id,final String groupname,final String userid,final String eid,final String dcode) {
		Specification<PointGroupEntity> spec = new Specification<PointGroupEntity>() {
			@Override
			public Predicate toPredicate(Root<PointGroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Path<String> idpath = root.get("id");
				Path<String> groupnamepath = root.get("groupname");
				Path<String> useridpath = root.get("userid");
				Path<String> dcodepath = root.get("dcode");
				Path<String> eidpath = root.get("eid");
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(id!=null&&!id.equals("")){
					predicates.add(builder.equal(idpath,id));
				}
				if(groupname!=null&&!groupname.equals("")){
					predicates.add(builder.equal(groupnamepath, groupname));
				}
				if(userid!=null&&!userid.equals("")){
					predicates.add(builder.equal(useridpath, userid));
				}
				if(dcode!=null&&!dcode.equals("")){
					predicates.add(builder.like(dcodepath, dcode+"%"));
				}
				if(eid!=null&&!eid.equals("")){
					predicates.add(builder.equal(eidpath, eid));
				}
				Predicate[] pres = new Predicate[predicates.size()];
				predicates.toArray(pres);
				query.where(pres);
				return null;
			}
		};
		return this.pointgroupdao.findAll(spec);
	}

	@Override
	public String addGroup(String groupname,String userid,String eid,String dcode) {
		String id=null;
		PointGroupEntity entity=new PointGroupEntity();
		entity.setCreatTime(new Date());
		entity.setDcode(dcode);
		entity.setEid(eid);
		entity.setGroupname(groupname);
		entity.setUserid(userid);
		PointGroupEntity result=this.pointgroupdao.save(entity);
		id=result.getId();
		return id;
	}

	@Override
	public String updateGroup(PointGroupEntity entity) {
		String id = entity.getId();
		PointGroupEntity pe2 = this.pointgroupdao.findOne(id);
		if(null == pe2){
			return null;
		}
		BeanUtils.copyProperties(entity, pe2, BeanTool.getNullPropertyNames(entity));
		if(entity.getStyleid()==null){
			pe2.setStyleid(null);
		}
		pe2=this.pointgroupdao.save(pe2);
		return pe2.getId();
	}

	@Override
	public void deleteGroup(String id) {
		this.pointgroupdao.delete(id);
		this.pointgroupdao.updatePointGroupidtoNUll(id);
	}

	@Override
	public PointGroupEntity findByid(String id) {
		return this.pointgroupdao.findById(id);
	}

	@Override
	public List<String> addPointGroups(List<PointGroupEntity> list) {
		List<String> ids=new ArrayList<String>();
		List<PointGroupEntity> result=(List<PointGroupEntity>) this.pointgroupdao.save(list);
		if(result!=null&&result.size()>0){
			for(PointGroupEntity p:result){
				String id=p.getId();
				ids.add(id);
			}
		}
		return ids;
	}

	@Override
	public PointGroupEntity findByStyleid(String id) {
		PointStyleEntity styleid=new PointStyleEntity();
		styleid.setId(id);
		return this.pointgroupdao.findByStyleid(styleid);
	}

}
