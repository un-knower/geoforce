package com.supermap.egispservice.base.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.dao.PointExportDao;
import com.supermap.egispservice.base.pojo.ExportPointBean;
import com.supermap.egispservice.base.util.StringUtil;

@Repository
public class PointExportDaoImpl implements PointExportDao {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public List queryAllPoint(String eid,
			List<String> childuserid, Date btime, Date etime,
			List<String> groupids, String status, String admincode) {
		StringBuilder sbsql=new StringBuilder();
		sbsql.append(" SELECT p.id,p.name,p.address,")
			.append(" pet.col1,pet.col2,pet.col3,pet.col4,pet.col5,pet.col6,pet.col7,pet.col8,pet.col9,pet.col10,")
			.append(" p.smx,p.smy,p.area_id areaid,'' areaName,eu.username,p.create_time as createTime,p.update_time as updateTime,")
			.append(" g.groupname,p.status FROM biz_point p ")
			.append(" left join biz_point_extcolval  pet on p.id=pet.pointid")
			.append(" left join biz_point_group g on p.groupid=g.id")
			.append(" left join egisp_rss_user eu on p.user_id=eu.id")
			.append(" where 1=1 ");
			
			if(StringUtils.isEmpty(eid)){
				return null;
			}else{
				sbsql.append(" and p.enterprise_id=:eid ");
			}
			if(null!=childuserid&&childuserid.size()>0){
				sbsql.append(" and p.user_id in (:childuserids)");
			}
			if(null!=btime){
				sbsql.append(" and p.create_time >=:btime");
			}
			if(null!=etime){
				sbsql.append(" and p.create_time<=:etime");
			}
			if(null!=groupids&&groupids.size()>0){
				//无分组
				if(groupids.contains("0")){
					sbsql.append(" and (groupid in (:groupids) or groupid is null or groupid='' )");
				}
				else{
					sbsql.append(" and groupid in (:groupids) ");
				}
			}
			if(StringUtils.isNotEmpty(status)){
				sbsql.append(" and p.status=:status");
			}
			if(StringUtils.isNotEmpty(admincode)){
				sbsql.append(" and p.admincode like :admincode" );
			}
			
			//排序
			sbsql.append(" order by p.create_time desc,p.user_id ");
			
			Query query = em.createNativeQuery(sbsql.toString());
			if(StringUtils.isNotEmpty(eid)){
				query.setParameter("eid", eid);
			}
			if(null!=childuserid&&childuserid.size()>0){
				query.setParameter("childuserids", childuserid);
			}
			if(null!=btime){
				query.setParameter("btime", btime,TemporalType.TIMESTAMP);
			}
			if(null!=etime){
				query.setParameter("etime", etime,TemporalType.TIMESTAMP);
			}
			if(null!=groupids&&groupids.size()>0){
				query.setParameter("groupids", groupids);
			}
			if(StringUtils.isNotEmpty(status)){
				query.setParameter("status", status);
			}
			if(StringUtils.isNotEmpty(admincode)){
				query.setParameter("admincode", admincode+"%");
			}
			query.unwrap(SQLQuery.class)
			.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		 return query.getResultList();
	}
	
	
	@Transactional
	@Override
	public int deletePoint(String eid, List<String> childuserid, Date btime,
			Date etime, List<String> groupids, String status, String admincode) {
		
		StringBuilder extvalSql=new StringBuilder();
		StringBuilder pointSql=new StringBuilder();
		extvalSql.append("delete from biz_point_extcolval where pointid in(")
				.append(" select id from biz_point where 1=1 ");
		
		pointSql.append("delete from biz_point where 1=1 ");
		if(StringUtils.isEmpty(eid)){//企业id没有的话，直接返回空，不执行删除
			return -1;
		}else{
			extvalSql.append(" and enterprise_id=:eid ");
			pointSql.append(" and enterprise_id=:eid ");
		}
		if(null!=childuserid&&childuserid.size()>0){
			extvalSql.append(" and user_id in (:childuserids)");
			pointSql.append(" and user_id in (:childuserids)");
		}
		if(null!=btime){
			extvalSql.append(" and create_time >=:btime");
			pointSql.append(" and create_time >=:btime");
		}
		if(null!=etime){
			extvalSql.append(" and create_time<=:etime");
			pointSql.append(" and create_time<=:etime");
		}
		if(null!=groupids&&groupids.size()>0){
			//无分组
			if(groupids.contains("0")){
				pointSql.append(" and (groupid in (:groupids) ");
				pointSql.append(" or groupid is null or groupid='') ");
				extvalSql.append(" and (groupid in (:groupids) ");
				extvalSql.append(" or groupid is null or groupid='') ");
			}
			else{
				pointSql.append(" and groupid in (:groupids)");
				extvalSql.append(" and groupid in (:groupids)");
			}
		}
		if(StringUtils.isNotEmpty(status)){
			extvalSql.append(" and status=:status");
			pointSql.append(" and status=:status");
		}
		if(StringUtils.isNotEmpty(admincode)){
			extvalSql.append(" and admincode like :admincode" );
			pointSql.append(" and admincode like :admincode" );
		}
		extvalSql.append(")");
		
		Query extvalQuery=em.createNativeQuery(extvalSql.toString());
		
		Query pointQuery=em.createNativeQuery(pointSql.toString());
		
		if(StringUtils.isNotEmpty(eid)){
			extvalQuery.setParameter("eid", eid);
			pointQuery.setParameter("eid", eid);
		}
		if(null!=childuserid&&childuserid.size()>0){
			extvalQuery.setParameter("childuserids", childuserid);
			pointQuery.setParameter("childuserids", childuserid);
		}
		if(null!=btime){
			extvalQuery.setParameter("btime", btime,TemporalType.TIMESTAMP);
			pointQuery.setParameter("btime", btime,TemporalType.TIMESTAMP);
		}
		if(null!=etime){
			extvalQuery.setParameter("etime", etime,TemporalType.TIMESTAMP);
			pointQuery.setParameter("etime", etime,TemporalType.TIMESTAMP);
		}
		if(null!=groupids&&groupids.size()>0){
			extvalQuery.setParameter("groupids", groupids);
			pointQuery.setParameter("groupids", groupids);
		}
		if(StringUtils.isNotEmpty(status)){
			extvalQuery.setParameter("status", status);
			pointQuery.setParameter("status", status);
		}
		if(StringUtils.isNotEmpty(admincode)){
			extvalQuery.setParameter("admincode", admincode+"%");
			pointQuery.setParameter("admincode", admincode+"%");
		}
		extvalQuery.executeUpdate();//删除自定义字段值
		int pointcount=pointQuery.executeUpdate();//删除网点
		return pointcount;
	}


	@Override
	public int querydeletePointCount(String eid, List<String> childuserid,
			Date btime, Date etime, List<String> groupids, String status,
			String admincode) {
		int count=-1;
		StringBuilder pointSql=new StringBuilder();
		pointSql.append("select count(*) sumcount from biz_point where 1=1 ");
		if(StringUtils.isEmpty(eid)){//企业id没有的话，直接返回空，不执行
			return -1;
		}else{
			pointSql.append(" and enterprise_id=:eid ");
		}
		if(null!=childuserid&&childuserid.size()>0){
			pointSql.append(" and user_id in (:childuserids)");
		}
		if(null!=btime){
			pointSql.append(" and create_time >=:btime");
		}
		if(null!=etime){
			pointSql.append(" and create_time<=:etime");
		}
		if(null!=groupids&&groupids.size()>0){
			//无分组
			if(groupids.contains("0")){
				pointSql.append(" and (groupid in (:groupids) or groupid is null or groupid='' )");
			}
			else{
				pointSql.append(" and groupid in (:groupids) ");
			}
		}
		
		if(StringUtils.isNotEmpty(status)){
			pointSql.append(" and status=:status");
		}
		if(StringUtils.isNotEmpty(admincode)){
			pointSql.append(" and admincode like :admincode" );
		}
		
		Query query = em.createNativeQuery(pointSql.toString());
		if(StringUtils.isNotEmpty(eid)){
			query.setParameter("eid", eid);
		}
		if(null!=childuserid&&childuserid.size()>0){
			query.setParameter("childuserids", childuserid);
		}
		if(null!=btime){
			query.setParameter("btime", btime,TemporalType.TIMESTAMP);
		}
		if(null!=etime){
			query.setParameter("etime", etime,TemporalType.TIMESTAMP);
		}
		if(null!=groupids&&groupids.size()>0){
			query.setParameter("groupids", groupids);
		}
		if(StringUtils.isNotEmpty(status)){
			query.setParameter("status", status);
		}
		if(StringUtils.isNotEmpty(admincode)){
			query.setParameter("admincode", admincode+"%");
		}
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List result=query.getResultList();
		if(result!=null&&result.size()>0){
			Map map=(Map)result.get(0);
			count=StringUtil.convertObjectToInt(map.get("sumcount"));
		}
		return count;
	}


	@Override
	public List findPointByGroupname(List<String> deptids, String eid,
			String groupname,String pointname,String areaid, int pageNo, int pageSize) {
		//如果企业id没有，直接不查询
		if(StringUtils.isEmpty(eid)){
			return null;
		}
		
		StringBuilder sbsql=new StringBuilder();
		sbsql.append("select bp.id,bp.area_id areaid,bp.address,bp.name,bp.smx,bp.smy,bpg.groupname from biz_point bp ")
			.append(" left JOIN biz_point_group bpg")
			.append(" on bp.groupid=bpg.id")
			.append(" where 1=1 ")
			.append(" and bp.smx>0 and bp.smy>0");
		
		if(StringUtils.isNotEmpty(eid)){
			sbsql.append(" and bp.enterprise_id=:eid");
		}
		if(StringUtils.isNotEmpty(groupname)){
			sbsql.append(" and bpg.groupname=:groupname");
		}
		if(StringUtils.isNotEmpty(pointname)){
			sbsql.append(" and bp.name like :pointname");
		}
		if(deptids!=null&&deptids.size()>0){
			sbsql.append(" and bp.department_id in(:deptids)");
		}
		if(StringUtils.isNotEmpty(areaid)){
			sbsql.append(" and bp.area_id=:areaid");
		}
		
		sbsql.append(" order by bp.create_time desc");
		
		Query query=em.createNativeQuery(sbsql.toString());
		if(pageNo!=-1&&pageNo>0){
			//分页
			int minIndex=(pageNo-1)*pageSize;
			query.setFirstResult(minIndex);
			query.setMaxResults(pageSize);
		}
		
		if(StringUtils.isNotEmpty(eid)){
			query.setParameter("eid", eid);
		}
		if(StringUtils.isNotEmpty(groupname)){
			query.setParameter("groupname", groupname);
		}
		if(StringUtils.isNotEmpty(pointname)){
			query.setParameter("pointname", "%"+pointname.replaceAll("_", "/_").replaceAll("%", "/%")+"%");
		}
		if(deptids!=null&&deptids.size()>0){
			query.setParameter("deptids", deptids);
		}
		if(StringUtils.isNotEmpty(areaid)){
			query.setParameter("areaid", areaid);
		}
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list=query.getResultList();
		return buildExportResult(list);
	}
	
	public List<ExportPointBean> buildExportResult(List resultlist){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ExportPointBean> beanlist=null;
		try {
			if(resultlist!=null&&resultlist.size()>0){
				beanlist=new ArrayList<ExportPointBean>();
				for(int i=0,s=resultlist.size();i<s;i++){
					Map map=(Map) resultlist.get(i);
					ExportPointBean bean=new ExportPointBean();
					bean.setAddress(StringUtil.convertObjectToString(map.get("address")));
					String areaid=StringUtil.convertObjectToString(map.get("areaid"));
					bean.setAreaName(areaid);
					bean.setGroupname(StringUtil.convertObjectToString(map.get("groupname")));
					bean.setId(StringUtil.convertObjectToString(map.get("id")));
					bean.setName(StringUtil.convertObjectToString(map.get("name")));
					bean.setSmx(map.get("smx")==null?null:new BigDecimal(StringUtil.convertObjectToString(map.get("smx"))));
					bean.setSmy(map.get("smy")==null?null:new BigDecimal(StringUtil.convertObjectToString(map.get("smy"))));
					beanlist.add(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return beanlist;
	}


	@Override
	public List findPointByExtCols(String colkey, String colvalue,
			List<String> deptids, String eid, int pageNo, int pageSize) {
		//如果企业id没有，直接不查询
				if(StringUtils.isEmpty(eid)){
					return null;
				}
				if(StringUtils.isEmpty(colkey)){
					return null;
				}
				
				StringBuilder sbsql=new StringBuilder();
				sbsql.append("select bp.id,bp.area_id areaid,bp.address,bp.name,bp.smx,bp.smy,bpg.groupname from biz_point bp ")
					.append(" left JOIN biz_point_group bpg")
					.append(" on bp.groupid=bpg.id")
					.append(" left join biz_point_extcolval bpe")
					.append(" on bpe.pointid=bp.id")
					.append(" where 1=1 ")
					.append(" and bp.smx>0 and bp.smy>0");
				
				if(StringUtils.isNotEmpty(eid)){
					sbsql.append(" and bp.enterprise_id=:eid");
				}
				if(deptids!=null&&deptids.size()>0){
					sbsql.append(" and bp.department_id in(:deptids)");
				}
				
				if(StringUtils.isNotEmpty(colvalue)){
					sbsql.append(" and bpe."+colkey+" like :colvalue");
				}
				
				sbsql.append(" order by bp.create_time desc");
				
				Query query=em.createNativeQuery(sbsql.toString());
				if(pageNo!=-1&&pageNo>0){
					//分页
					int minIndex=(pageNo-1)*pageSize;
					query.setFirstResult(minIndex);
					query.setMaxResults(pageSize);
				}
				
				if(StringUtils.isNotEmpty(eid)){
					query.setParameter("eid", eid);
				}
				if(deptids!=null&&deptids.size()>0){
					query.setParameter("deptids", deptids);
				}
				
				if(StringUtils.isNotEmpty(colvalue)){
					query.setParameter("colvalue", colvalue);
				}
				
				query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List list=query.getResultList();
				return buildExportResult(list);
	} 
	
}
