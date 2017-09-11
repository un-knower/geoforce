package com.supermap.egispservice.statistic.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.supermap.egispservice.statistic.util.AreaFieldNames;

@Repository
public class AreaQueryDaoImpl implements IAreaQueryDao {
	
	@PersistenceContext(unitName="OracleJPA")
	private EntityManager oracleEM;

	@Override
	public String queryNameByIdOrNumber(String id, String number,String dcode) {
		String areaname=null;
		if(StringUtils.isEmpty(id)&&StringUtils.isEmpty(number)){
			return null;
		}
		StringBuilder sbsql=new StringBuilder();
		sbsql.append(" select name from BIZ_AREA where 1=1 ");
		if(!StringUtils.isEmpty(id)){
			sbsql.append(" and id=:id ");
		}
		else if(!StringUtils.isEmpty(number)){
			sbsql.append(" and area_num=:number ");
			if(!StringUtils.isEmpty(dcode)){
				sbsql.append(" and dcode like :dcode ");
			}
		}
		
		Query query=oracleEM.createNativeQuery(sbsql.toString());
		if(!StringUtils.isEmpty(id)){
			query.setParameter("id",id);
		}
		else if(!StringUtils.isEmpty(number)){
			query.setParameter("number",number);
			if(!StringUtils.isEmpty(dcode)){
				query.setParameter("dcode",dcode+"%");
			}
		}
		
		query.unwrap(SQLQuery.class)
			.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List resultlist=query.getResultList();
		if(resultlist!=null&&resultlist.size()>0){
			Map m=(Map)resultlist.get(0);
			areaname=m.get("NAME")==null?null:m.get("NAME").toString();
		}
		return areaname;
	}

	@Override
	public Map<String, String> findFirstRelationAreaAttrs(String relationareaid) {
		if(StringUtils.isEmpty(relationareaid)){
			return null;
		}
		Map<String,String> map=new HashMap<String,String>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select  ")
				.append(AreaFieldNames.ID+", ")
				.append(AreaFieldNames.NAME+", ")
				.append(AreaFieldNames.AREANUMBER+", to_char(")
				.append(AreaFieldNames.AREA_STATUS+") AREA_STATUS, ")
				.append(AreaFieldNames.RELATION_AREAID+" ")
				.append(" from ")
				.append(" BIZ_AREA ")
				.append(" where id=:relationareaid ");
		
		Query query=oracleEM.createNativeQuery(sqlBuilder.toString());
		if(!StringUtils.isEmpty(relationareaid)){
			query.setParameter("relationareaid",relationareaid);
		}
		query.unwrap(SQLQuery.class)
		.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List resultlist=query.getResultList();
		if(resultlist!=null&&resultlist.size()>0){
			map=(Map<String, String>) resultlist.get(0);
		}else{
			map=null;
		}
		return map;
	}

	@Override
	public Map<String, String> findAreaByNameAndEid(String name, String eid) {
		if(StringUtils.isEmpty(name)||StringUtils.isEmpty(eid)){
			return null;
		}
		Map<String,String> map=new HashMap<String,String>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select  ")
				.append(AreaFieldNames.ID+", ")
				.append(AreaFieldNames.NAME+", ")
				.append(AreaFieldNames.AREANUMBER+", to_char(")
				.append(AreaFieldNames.AREA_STATUS+") AREA_STATUS, ")
				.append(AreaFieldNames.RELATION_AREAID+" ")
				.append(" from ")
				.append(" BIZ_AREA ")
				.append(" where "+AreaFieldNames.NAME+"=:name and ")
				.append(AreaFieldNames.ENTERPRISE_ID+"=:eid ");
		
		Query query=oracleEM.createNativeQuery(sqlBuilder.toString());
		if(!StringUtils.isEmpty(name)){
			query.setParameter("name",name);
		}
		if(!StringUtils.isEmpty(eid)){
			query.setParameter("eid",eid);
		}
		query.unwrap(SQLQuery.class)
		.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List resultlist=query.getResultList();
		if(resultlist!=null&&resultlist.size()>0){
			map=(Map<String, String>) resultlist.get(0);
		}else{
			map=null;
		}
		return map;
	}

}
