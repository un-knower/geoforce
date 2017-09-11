package com.supermap.egispservice.base.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.dao.IOrderBaseDao;

@Repository
public class OrderBaseDaoImpl implements IOrderBaseDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<String> getDistinctBatchsByParams(String eid, List<String> userids,
			String batch) {
		StringBuilder sbsql=new StringBuilder();
		sbsql.append(" select DISTINCT(p.batch) as batch from order_base p where 1=1 ");
			
			if(StringUtils.isEmpty(eid)){
				return null;
			}else{
				sbsql.append(" and p.enterprise_id=:eid ");
			}
			if(null!=userids&&userids.size()>0){
				sbsql.append(" and p.user_id in (:userids)");
			}
			
			if(StringUtils.isNotEmpty(batch)){
				sbsql.append(" and p.batch like :batch");
			}
			
			sbsql.append(" order by batch asc");
			
			Query query = em.createNativeQuery(sbsql.toString());
			if(StringUtils.isNotEmpty(eid)){
				query.setParameter("eid", eid);
			}
			if(null!=userids&&userids.size()>0){
				query.setParameter("userids", userids);
			}
			
			if(StringUtils.isNotEmpty(batch)){
				query.setParameter("batch", batch+"%");
			}
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List result=query.getResultList();
		return formatResult(result);
	}
	
	public List<String> formatResult(List list){
		List<String> results=null;
		if(list!=null&&list.size()>0){
			results=new ArrayList<String>();
			for(int i=0,s=list.size();i<s;i++){
				Map m=(Map) list.get(i);
				if(m!=null&&m.get("batch")!=null){
					results.add(m.get("batch").toString());
				}
			}
		}
		return results;
	}

	@Override
	public int getOrderCountsByParams(String eid, List<String> userids,
			List<String> batchs, String status, String admincode) {
		int count=-1;
		StringBuilder sbsql=new StringBuilder();
		sbsql.append(" select count(*) sumcount from order_base p where 1=1 ");
		
			List<String> statulist=null;
			if(StringUtils.isNotEmpty(status)){
				String[] statusarr=status.split(",");
				if(null!=statusarr&&statusarr.length>0){
					statulist=new ArrayList<String>();
					for(String s:statusarr){
						statulist.add(s);
					}
				}
			}
		
			if(StringUtils.isEmpty(eid)){
				return -1;
			}else{
				sbsql.append(" and p.enterprise_id=:eid ");
			}
			if(null!=userids&&userids.size()>0){
				sbsql.append(" and p.user_id in (:userids)");
			}
			
			if(null!=batchs&&batchs.size()>0){
				sbsql.append(" and p.batch in (:batchs)");
			}
			
			//分单成功：status=3，5，分单失败：4，10
			if(null!=statulist&&statulist.size()>0){
				sbsql.append(" and p.order_status_id in (:statulist)");
				/*if(status.equals("0")){//成功
					sbsql.append(" and p.order_status_id in(3,5) ");
				}else if(status.equals("1")){//失败
					sbsql.append(" and p.order_status_id in(4,10) ");
				}*/
			}
			
			if(StringUtils.isNotEmpty(admincode)){
				sbsql.append(" and p.admincode like :admincode");
			}
			
			
			Query query = em.createNativeQuery(sbsql.toString());
			if(StringUtils.isNotEmpty(eid)){
				query.setParameter("eid", eid);
			}
			if(null!=userids&&userids.size()>0){
				query.setParameter("userids", userids);
			}
			
			if(null!=batchs&&batchs.size()>0){
				query.setParameter("batchs", batchs);
			}
			if(StringUtils.isNotEmpty(admincode)){
				query.setParameter("admincode", admincode+"%");
			}
			if(null!=statulist&&statulist.size()>0){
				query.setParameter("statulist", statulist);
			}
			
			
			query.unwrap(SQLQuery.class)
			.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List result=query.getResultList();
			if(result!=null&&result.size()>0){
				Map map=(Map)result.get(0);
				count=map.get("sumcount")==null?-1:Integer.parseInt(map.get("sumcount").toString().trim());
			}
		return count;
	}
	
	@Transactional
	public int deleteOrdersByParam(String eid, List<String> userids,
			List<String> batchs, String status, String admincode){
		int count=-1;
		
		List<String> statulist=null;
		if(StringUtils.isNotEmpty(status)){
			String[] statusarr=status.split(",");
			if(null!=statusarr&&statusarr.length>0){
				statulist=new ArrayList<String>();
				for(String s:statusarr){
					statulist.add(s);
				}
			}
		}
		
		StringBuilder ordersql=new StringBuilder();
		StringBuilder fendansql=new StringBuilder();
		ordersql.append(" delete from order_base  where 1=1 ");
		fendansql.append("delete from  order_fendan  where id in(select ob.id from order_base ob where 1=1 ");	
			if(StringUtils.isEmpty(eid)){
				return -1;
			}else{
				ordersql.append(" and enterprise_id=:eid ");
				fendansql.append(" and ob.enterprise_id=:eid ");
			}
			if(null!=userids&&userids.size()>0){
				ordersql.append(" and user_id in (:userids)");
				fendansql.append(" and ob.user_id in (:userids)");
			}
			
			if(null!=batchs&&batchs.size()>0){
				ordersql.append(" and batch in (:batchs)");
				fendansql.append(" and ob.batch in (:batchs)");
			}
			
			//分单成功：status=3，5，分单失败：4，10
			if(null!=statulist&&statulist.size()>0){
				ordersql.append(" and order_status_id in (:statulist) ");
				fendansql.append(" and ob.order_status_id in (:statulist) ");
				/*if(status.equals("0")){//成功
					ordersql.append(" and order_status_id in(3,5) ");
					fendansql.append(" and ob.order_status_id in(3,5) ");
				}else if(status.equals("1")){//失败
					ordersql.append(" and order_status_id in(4,10) ");
					fendansql.append(" and ob.order_status_id in(4,10) ");
				}*/
			}
			
			if(StringUtils.isNotEmpty(admincode)){
				ordersql.append(" and admincode like :admincode");
				fendansql.append(" and ob.admincode like :admincode");
			}
			
			fendansql.append(")");
			
			Query fendanquery = em.createNativeQuery(fendansql.toString());
			Query orderquery = em.createNativeQuery(ordersql.toString());
			
			if(StringUtils.isNotEmpty(eid)){
				fendanquery.setParameter("eid", eid);
				orderquery.setParameter("eid", eid);
			}
			if(null!=userids&&userids.size()>0){
				fendanquery.setParameter("userids", userids);
				orderquery.setParameter("userids", userids);
			}
			
			if(null!=batchs&&batchs.size()>0){
				fendanquery.setParameter("batchs", batchs);
				orderquery.setParameter("batchs", batchs);
			}
			if(StringUtils.isNotEmpty(admincode)){
				fendanquery.setParameter("admincode", admincode+"%");
				orderquery.setParameter("admincode", admincode+"%");
			}
			if(null!=statulist&&statulist.size()>0){
				fendanquery.setParameter("statulist", statulist);
				orderquery.setParameter("statulist", statulist);
			}
			
			fendanquery.executeUpdate();
			count=orderquery.executeUpdate();
			return count;
	}

	@Override
	public List queryExportByBatch(String eid,
			List<String> userids, List<String> batchs, String status,
			String admincode) {
		StringBuilder sbsql=new StringBuilder();
		
		List<String> statulist=null;
		if(StringUtils.isNotEmpty(status)){
			String[] statusarr=status.split(",");
			if(null!=statusarr&&statusarr.length>0){
				statulist=new ArrayList<String>();
				for(String s:statusarr){
					statulist.add(s);
				}
			}
		}
		
		sbsql.append(" select of.smx,of.smy,of.area_id areaid,dos.`value` orderstatus, dfs.`value` fendanstatus ,")
				.append(" ob.id,ob.address,ob.admincode,ob.city,ob.province,ob.county,ob.number,ob.batch,ob.order_status_id orderstatusid from order_base ob")
				.append(" LEFT JOIN order_fendan of ")
				.append(" on ob.id=of.id")
				.append(" left join dim_fendan_status dfs")
				.append(" on of.fendan_status_id = dfs.id")
				.append(" LEFT JOIN dim_order_status dos")
				.append(" on ob.order_status_id=dos.id where 1=1 ");
			
			if(StringUtils.isEmpty(eid)){
				return null;
			}else{
				sbsql.append(" and ob.enterprise_id=:eid ");
			}
			if(null!=userids&&userids.size()>0){
				sbsql.append(" and ob.user_id in (:userids)");
			}
			
			if(null!=batchs&&batchs.size()>0){
				sbsql.append(" and ob.batch in (:batchs)");
			}
			
			//分单成功：status=3，5，分单失败：4，10
			if(null!=statulist&&statulist.size()>0){
				sbsql.append(" and ob.order_status_id in (:statulist) ");
				/*if(status.equals("0")){//成功
					sbsql.append(" and ob.order_status_id in(3,5) ");
				}else if(status.equals("1")){//失败
					sbsql.append(" and ob.order_status_id in(4,10) ");
				}*/
			}
			
			if(StringUtils.isNotEmpty(admincode)){
				sbsql.append(" and ob.admincode like :admincode");
			}
			
			sbsql.append(" order by ob.import_time desc");
			
			Query query = em.createNativeQuery(sbsql.toString());
			if(StringUtils.isNotEmpty(eid)){
				query.setParameter("eid", eid);
			}
			if(null!=userids&&userids.size()>0){
				query.setParameter("userids", userids);
			}
			
			if(null!=batchs&&batchs.size()>0){
				query.setParameter("batchs", batchs);
			}
			if(StringUtils.isNotEmpty(admincode)){
				query.setParameter("admincode", admincode+"%");
			}
			if(null!=statulist&&statulist.size()>0){
				query.setParameter("statulist", statulist);
			}
			query.unwrap(SQLQuery.class)
			.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List result=query.getResultList();
			return result;
	}

}
