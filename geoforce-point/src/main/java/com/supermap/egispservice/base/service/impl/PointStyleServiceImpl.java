package com.supermap.egispservice.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.supermap.egispservice.base.dao.PointStyleCustomDao;
import com.supermap.egispservice.base.dao.PointStyleDao;
import com.supermap.egispservice.base.entity.PointStyleCustomEntity;
import com.supermap.egispservice.base.entity.PointStyleEntity;
import com.supermap.egispservice.base.service.PointStyleService;
import com.supermap.egispservice.base.util.BeanTool;

@Transactional(rollbackFor=Exception.class)
@Service("pointStyleService")
public class PointStyleServiceImpl implements PointStyleService {
	
	@Autowired
	PointStyleDao pointStyleDao;
	
	@Autowired
	PointStyleCustomDao PointStyleCustomDao;

	@Override
	public String addPointStyle(PointStyleEntity entity) {
		String id=null;
		entity=this.pointStyleDao.save(entity);
		id=entity.getId();
		return id;
	}

	@Override
	public String updatePointStyle(PointStyleEntity entity) {
		String id = entity.getId();
		PointStyleEntity pe2 = this.pointStyleDao.findOne(id);
		if(null == pe2){
			return null;
		}
		BeanUtils.copyProperties(entity, pe2, BeanTool.getNullPropertyNames(entity));
		pe2=this.pointStyleDao.save(pe2);
		return pe2.getId();
	}

	@Override
	public void deletePointStyle(String id) {
		this.pointStyleDao.delete(id);
	}

	/*@Override
	public List<PointStyleEntity> findByGroupid(PointGroupEntity groupid) {
		return this.pointStyleDao.findByGroupid(groupid);
	}*/

	@Override
	public List<PointStyleEntity> findStyleByParam(final Map m) {
		List<PointStyleEntity> stylelist=new ArrayList<PointStyleEntity>();
		if(m!=null){
			Specification<PointStyleEntity> spec = new Specification<PointStyleEntity>() {

				@Override
				public Predicate toPredicate(Root<PointStyleEntity> root,
						CriteriaQuery<?> query, CriteriaBuilder builder) {
					Path<String> stylenamepath = root.get("stylename");
					Path<String> idpath = root.get("id");
					Path<String> useridpath = root.get("userid");
					Path<String> dcodepath = root.get("dcode");
					Path<String> eidpath = root.get("eid");
					Path<String> def1path = root.get("def1");//记录是不是子集，0表示不是，1表示是子集
					List<Predicate> predicates = new ArrayList<Predicate>();
					Object id=m.get("id");
					Object stylename=m.get("stylename");
					Object userid=m.get("userid");
					Object eid=m.get("eid");
					Object dcode=m.get("dcode");
					Object def1=m.get("def1");
					if(id!=null&&!id.equals("")){
						predicates.add(builder.equal(idpath,id));
					}
					if(userid!=null&&!userid.equals("")){
						predicates.add(builder.equal(useridpath, userid));
					}
					if(dcode!=null&&!dcode.equals("")){
						predicates.add(builder.equal(dcodepath, dcode));
					}
					if(eid!=null&&!eid.equals("")){
						predicates.add(builder.equal(eidpath, eid));
					}
					if(stylename!=null&&!stylename.equals("")){
						predicates.add(builder.equal(stylenamepath, stylename));
					}
					if(def1!=null&&!def1.equals("")){
						predicates.add(builder.equal(def1path, def1));
					}else
						predicates.add(builder.equal(def1path, "0"));
					
					Predicate[] pres = new Predicate[predicates.size()];
					predicates.toArray(pres);
					query.where(pres);
					return null;
				}
			};
			stylelist=this.pointStyleDao.findAll(spec);
			
			/**
			 * 查找自定义文件
			 *//*
			if(stylelist!=null&&stylelist.size()>0){
				for(PointStyleEntity style:stylelist){
					if(style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
						PointStyleCustomEntity custom=this.PointStyleCustomDao.findById(style.getAppcustom());
						if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
							style.setAppcustom(custom.getFilepath());
							style.setDef1(custom.getWidth()+","+custom.getHeight());
						}
					}
				}
			}*/
		}
		return stylelist;
	}

	@Override
	public PointStyleEntity findById(String id) {
		PointStyleEntity style=new PointStyleEntity();
		style=this.pointStyleDao.findById(id);
		/*//查找自定义文件
		if(style.getAppcustom()!=null&&!style.getAppcustom().equals("")){
			PointStyleCustomEntity custom=this.PointStyleCustomDao.findById(style.getAppcustom());
			if(custom!=null&&custom.getFilepath()!=null&&!custom.getFilepath().equals("")){
				style.setAppcustom(custom.getFilepath());
				style.setDef1(custom.getWidth()+","+custom.getHeight());
			}
		}*/
		return style;
	}

	@Override
	public List<PointStyleCustomEntity> findCustomsByUserid(String userid) {
		return this.PointStyleCustomDao.findByUserid(userid);
	}

	@Override
	public String saveCustomFiles(PointStyleCustomEntity custom) {
		String id=null;
		custom=this.PointStyleCustomDao.save(custom);
		id=custom.getId();
		return id;
	}

	@Override
	public PointStyleCustomEntity findCustomfileByid(String customid) {
		return this.PointStyleCustomDao.findById(customid);
	}

	@Override
	public boolean deleteCustomfileByid(String customid) {
		this.PointStyleCustomDao.delete(customid);
		this.pointStyleDao.updateappCustomBycustomid(customid);
		return true;
	}

	@Override
	public List<PointStyleCustomEntity> findCustomsByparam(Map m) {
		return null;
	}

	@Transactional
	@Override
	public int deleteUnuseStyles(String userid) {
		// TODO Auto-generated method stub
		return this.pointStyleDao.deleteUnuseStyles(userid);
	}

}
