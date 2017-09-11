package com.supermap.egispservice.base.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.StringUtils;
import com.supermap.egispservice.base.dao.PointExtcolValDao;
import com.supermap.egispservice.base.entity.PointExtcolValEntity;
import com.supermap.egispservice.base.service.PointExtcolValService;
import com.supermap.egispservice.base.util.BeanTool;

@Transactional(rollbackFor=Exception.class)
@Service("pointExtcolValService")
public class PointExtcolValServiceImpl implements PointExtcolValService {

	@Autowired
	PointExtcolValDao pointExtcolValDao;
	
	@PersistenceContext
	private EntityManager em;

	/**
	 * 添加第一个自定义字段值
	 */
	@Override
	public String addPointExtcolVal(String cols,String value,String pointid,String userid) {
		List<PointExtcolValEntity> temprecordlist=this.findByPointidOrUserid(pointid,"");
		String results="";
		if(temprecordlist!=null&&temprecordlist.size()>0){
			results=this.updatePointExtcolVal(cols, value, pointid,"");
		}
		else {
			PointExtcolValEntity record=new PointExtcolValEntity();
			record.setPointid(pointid);
			Field fields[] = PointExtcolValEntity.class.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals(cols)) {
					try {
						field.set(record, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
			record.setUserid(userid);
			record=pointExtcolValDao.save(record);
			results=record.getPointid();
		}
		return results;
	}

	/**
	 * 修改字段的值
	 */
	@Override
	public String updatePointExtcolVal(String cols, String value, String pointid,String userid) {
		String result="";
		List<PointExtcolValEntity> temprecordlist =null;
		List<PointExtcolValEntity> recordlist=new ArrayList<PointExtcolValEntity>();
		Field fields[] = PointExtcolValEntity.class.getDeclaredFields();
		try {
			//如果是批量更新用户的自定义字段值为空
			if((userid!=null&&!userid.equals(""))&&(pointid==null||pointid.equals(""))&&(value==null||value.equals(""))){
				String sql="update biz_point_extcolval set "+cols+"='' where userid=:userid";
				Query query = em.createNativeQuery(sql);
				query.setParameter("userid", userid);
				query.executeUpdate();
			}else{
				temprecordlist=new ArrayList<PointExtcolValEntity>();
				temprecordlist=this.findByPointidOrUserid(pointid,userid);
				if(temprecordlist!=null&&temprecordlist.size()>0){
					for(PointExtcolValEntity temprecord:temprecordlist){
						for (Field field : fields) {
							if (field.getName().equals(cols)) {
								field.set(temprecord, value);
								recordlist.add(temprecord);
								break;
							}
						}
					}
				}
				Iterable<PointExtcolValEntity> ipe=pointExtcolValDao.save(recordlist);
			}
			result="success";	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * 根据网点id查找对应的自定义字段值
	 */
	@Override
	public List<PointExtcolValEntity> findByPointidOrUserid(String pointid,String userid) {
		if(pointid==null||pointid.equals("")){
			//return pointExtcolValDao.findByUserid(userid);
			return findExtvalByUserid(userid);
		}
		else if(userid==null||userid.equals("")){
			//return pointExtcolValDao.findByPointid(pointid);
			return findExtvalByPointid(pointid);
		}
		else return null;
	}

	/**
	 * 删除网点自定义字段的整条记录
	 */
	@Override
	public void deletePointExtcolVal(PointExtcolValEntity record){
		pointExtcolValDao.delete(record);
	}

	/**
	 * 根据网点id删除网点自定义字段记录
	 */
	@Override
	public void deletePointExtcolValByPointid(String pointid) {
		pointExtcolValDao.deleteByPointId(pointid);
	}

	@Override
	public String save(PointExtcolValEntity record) {
		PointExtcolValEntity record2=pointExtcolValDao.findOne(record.getPointid());
		if(record2!=null){
			BeanUtils.copyProperties(record, record2, BeanTool.getNullPropertyNames(record));
			record=pointExtcolValDao.save(record2);
		}else {
			record=pointExtcolValDao.save(record);
		}
		return record.getPointid();
	}
	
	
	/**
	 * 转换
	 * @param list
	 * @return
	 * @Author Juannyoh
	 * 2015-12-10下午5:27:45
	 */
	public List<PointExtcolValEntity> convertList(List list){
		List<PointExtcolValEntity> vallist=null;
		if(list!=null&&list.size()>0){
			vallist=new ArrayList<PointExtcolValEntity>();
			for(int i=0;i<list.size();i++){
				Object obj[]=(Object[])list.get(i);
				PointExtcolValEntity record=new PointExtcolValEntity();
				record.setPointid((String)obj[0]);
				record.setUserid((String)obj[1]);
				record.setCol1(StringUtils.isNullOrEmpty((String)obj[2])?null:(String)obj[2]);
				record.setCol2(StringUtils.isNullOrEmpty((String)obj[3])?null:(String)obj[3]);
				record.setCol3(StringUtils.isNullOrEmpty((String)obj[4])?null:(String)obj[4]);
				record.setCol4(StringUtils.isNullOrEmpty((String)obj[5])?null:(String)obj[5]);
				record.setCol5(StringUtils.isNullOrEmpty((String)obj[6])?null:(String)obj[6]);
				record.setCol6(StringUtils.isNullOrEmpty((String)obj[7])?null:(String)obj[7]);
				record.setCol7(StringUtils.isNullOrEmpty((String)obj[8])?null:(String)obj[8]);
				record.setCol8(StringUtils.isNullOrEmpty((String)obj[9])?null:(String)obj[9]);
				record.setCol9(StringUtils.isNullOrEmpty((String)obj[10])?null:(String)obj[10]);
				record.setCol10(StringUtils.isNullOrEmpty((String)obj[11])?null:(String)obj[11]);
				vallist.add(record);
			}
		}
		return vallist;
	}
	
	/**
	 * 根据网点id查找自定义数据
	 * @param pointid
	 * @return
	 * @Author Juannyoh
	 * 2015-12-10下午5:29:48
	 */
	public List<PointExtcolValEntity> findExtvalByPointid(String pointid){
		List list=this.pointExtcolValDao.findExtvolByPointid(pointid);
		List<PointExtcolValEntity> vallist=convertList(list);
		return vallist;
	}
	/**
	 * 根据用户id查找自定义数据
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-12-10下午5:30:59
	 */
	public List<PointExtcolValEntity> findExtvalByUserid(String userid){
		List list=this.pointExtcolValDao.findExtvolByUserid(userid);
		List<PointExtcolValEntity> vallist=convertList(list);
		return vallist;
	}
}
