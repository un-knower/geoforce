package com.supermap.egispservice.base.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.dao.PointExtcolDao;
import com.supermap.egispservice.base.entity.PointExtcolEntity;
import com.supermap.egispservice.base.service.PointExtcolService;
import com.supermap.egispservice.base.service.PointExtcolValService;
import com.supermap.egispservice.base.util.BeanTool;

@Transactional(rollbackFor=Exception.class)
@Service("pointExtcolService")
public class PointExtcolServiceImpl implements PointExtcolService {

	
	@Autowired
	PointExtcolDao pointExtcolDao;
	
	@Autowired
	PointExtcolValService pointExtcolValService;
	
	final String stringSplit="!,";
	
	/**
	 * 第一次添加单个自定义字段
	 * @param record
	 * @return
	 * @Author Juannyoh
	 * 2015-8-17上午10:39:33
	 */
	@Override
	public String addPointExtcol(PointExtcolEntity record) {
		System.out.println(record.getUserid());
		List<PointExtcolEntity> pointextlist=pointExtcolDao.findByUserid(record.getUserid());
		int count=pointextlist==null?0:pointextlist.size();
		String id =null;
		if(count>0){
			id=this.update(record);//有该用户的自定义字段记录时，就修改该条记录
		}
		else {
			record.setCol1(record.getDefaultcol());
			record=updateColOrder(null,record.getDefaultcol(),record);//记录顺序
			record=pointExtcolDao.save(record);//没有该用户的自定义字段时，增加一条用户字段记录
			id=record.getUserid();
		}
		return id;
	}

	/**
	 * 添加自定义字段
	 * @param record
	 * @return
	 * @Author Juannyoh
	 * 2015-8-17上午10:40:01
	 */
	public String update(PointExtcolEntity record) {
		String userid=record.getUserid();
		//PointExtcolEntity oldrecord=pointExtcolDao.findByUserid(userid).get(0);//根据用户id在数据库查到的记录
		PointExtcolEntity oldrecord=this.findByUserid(userid);
		PointExtcolEntity record2=oldrecord;
		if(record2==null){
			return null;
		}
		BeanUtils.copyProperties(oldrecord, record2, BeanTool.getNullPropertyNames(oldrecord));
		oldrecord.setDefaultcol(record.getDefaultcol());
		record2=copyNewPointExtcolEntity(oldrecord);
		record2=updateColOrder(null,record2.getDefaultcol(),record2);//记录顺序
		record2=pointExtcolDao.save(record2);
		return record2.getUserid();
	}

	/**
	 * 根据用户id查找到自定义字段
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-17上午10:40:15
	 */
	@Override
	public PointExtcolEntity findByUserid(String userid) {
		if(pointExtcolDao.findByUserid(userid)!=null&&pointExtcolDao.findByUserid(userid).size()>0){
			return pointExtcolDao.findByUserid(userid).get(0);
		}
		else return null;
	}

	/**
	 * 删除用户自定义字段整条记录
	 * @param record
	 * @Author Juannyoh
	 * 2015-8-17上午10:54:13
	 */
	@Override
	public void deletePointExtcol(PointExtcolEntity record) {
		pointExtcolDao.delete(record);
	}
	
	
	/**
	 * 依次查找空白字段，然后将新增的字段写入其中
	 * @param tclass
	 * @param record
	 * @return
	 * @Author Juannyoh
	 * 2015-8-17下午3:05:48
	 */
	public PointExtcolEntity copyNewPointExtcolEntity(PointExtcolEntity record){
		PointExtcolEntity record2=record;
		try {
			//获得该类的所有属性
	        Field[] fields = record.getClass().getDeclaredFields();
	        for(Field field:fields){
	        	field.setAccessible(true);
	        	if((field.get(record)==null||field.get(record).equals(""))&&!field.getName().equals("defaultcol")){
						field.set(record2, record.getDefaultcol());
						break;
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return record2;
	}

	/**
	 * 根据用户ID，修改自定义字段描述
	 * @param cols
	 * @param value
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-17下午4:49:20
	 */
	@Override
	public String updatePointExtcol(String cols, String desc, String userid) {
		//List<PointExtcolEntity> collist=this.findByUserid(userid);
		PointExtcolEntity record=this.findByUserid(userid);
		
		record=updateColOrder(cols,desc,record);//记录顺序
		
		Field[] fields = record.getClass().getDeclaredFields();
		try {
			for(Field field:fields){
	        	if(field.getName().equals(cols)){
						field.set(record, desc);
						break;
				}
	        }
			
			if(desc.equals("")){
				//同时需要把已选择的默认字段去掉
				String configcols=record.getConfigcols();
				if(!StringUtils.isEmpty(configcols)){
					if(configcols.indexOf(cols)>-1){
						if(configcols.indexOf(cols)>0){
							configcols=configcols.replaceAll(","+cols, "");
						}else if(configcols.indexOf(cols)==0){
							configcols=configcols.replaceAll(cols+",", "");
						}
						record.setConfigcols(configcols);
					}
				}
			}
			pointExtcolDao.save(record);
			
			//删除字段的时候，desc传“”，这时需要同步更新val表
			if(desc.equals("")){
				pointExtcolValService.updatePointExtcolVal(cols, "", "", userid);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * list转成String
	 * @param stringList
	 * @return
	 * @Author Juannyoh
	 * 2015-9-15下午4:34:07
	 */
	public  String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(stringSplit);
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }
	
	/**
	 * 字段顺序
	 * @param col
	 * @param desc
	 * @param record
	 * @return
	 * @Author Juannyoh
	 * 2015-9-15下午5:04:55
	 */
	public PointExtcolEntity updateColOrder(String col,String desc,PointExtcolEntity record){
		PointExtcolEntity result=record;
		String colorder=record.getColorder();//获取顺序
		List<String> collist=new ArrayList<String>();
		if(colorder!=null&&!colorder.equals("")){
			//collist=Arrays.asList(colorder.split(stringSplit));
			Collections.addAll(collist, colorder.split(stringSplit));
		}
		//添加列的时候
		if(col==null||col.equals("")){
			//添加顺序
	        collist.add(desc+stringSplit);
		}
		//修改列的时候
		else if(col!=null&&!col.equals("")&&desc!=null&&!desc.equals("")){
			try {
				Field field=PointExtcolEntity.class.getDeclaredField(col);
				String oldvalue=(String) field.get(record);
				if(collist!=null&&collist.size()>0){
					collist.set(collist.indexOf(oldvalue), desc);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//删除列的时候
		else if(col!=null&&!col.equals("")&&(desc==null||desc.equals(""))){
			try {
				Field field=PointExtcolEntity.class.getDeclaredField(col);
				String oldvalue=(String) field.get(record);
				if(collist!=null&&collist.size()>0){
					collist.remove(oldvalue);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		colorder=listToString(collist);
        result.setColorder(colorder);
        return result;
	}

	@Override
	public PointExtcolEntity saveConfigcols(PointExtcolEntity record) {
		if(record!=null&&record.getUserid()!=null){
			record=this.pointExtcolDao.save(record);
			return record;
		}
		else return null;
	}
}
