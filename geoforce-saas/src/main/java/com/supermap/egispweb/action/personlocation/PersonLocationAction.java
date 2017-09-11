package com.supermap.egispweb.action.personlocation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.common.Constant;
import com.supermap.egispweb.pojo.personlocation.PersonStoreBean;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.info.Dept;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPic;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStore;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStoreType;
/**
 * 
* ClassName：PersonLocationAction   
* 类描述：   巡店人员位置监控
* 操作人：wangshuang   
* 操作时间：2014-11-27 上午10:41:38     
* @version 1.0
 */
@Controller
@RequestMapping("personMonitor")
public class PersonLocationAction extends BaseAction{

	@RequestMapping("index")
	public String init() {
		return "monitor/person/personMonitor";
	}
	/**
	 * 
	* 方法名: personLocation
	* 描述:人员定位
	* @param personIds
	* @return
	 */
	@RequestMapping(value = "locate",method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult personLocation(@RequestParam("personIds") String personIds){
		
		return personLocationConsumer.personLocation(personIds);
	}
	/**
	 * 
	* 方法名: sendNote
	* 描述: 选择人员发送通知（推送消息）
	* @param personIds
	* @param title
	* @param content
	* @return
	 */
	@RequestMapping(value = "sendNote",method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult sendNote(HttpSession session,String personIds,String title,String content){
		UserEntity user = getUserSession(session);
		return noticeConsumer.addNotice(title, content, personIds, user);
	}
	/**
	 * 
	* 方法名: storeList
	* 描述: 地图加载门店信息
	* @return
	 */
	@RequestMapping(value = "storeList",method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult storeList(HttpSession session,Integer currentPageNum){
		UserEntity user = getUserSession(session);
		Page page = new Page();
		if(currentPageNum == null)
			currentPageNum = 1;
		page.setCurrentPageNum(currentPageNum);
		page.setPageSize(200);//一次加载200个门店
		
		page = storeConsumer.getStoreList(null, user.getDeptId().getCode(), user, page);
		List<Object> list = page.getResult();
		if(list == null || list.isEmpty())
			return new AjaxResult((short)0,"无门店信息");
		
		List<PersonStoreBean> beans = getPersonStore(list);
		page.setResult(beans);
		return new AjaxResult((short)1,page);
	}
	/**
	 * 
	* 方法名: getPersonStore
	* 描述:封装成前台的门店bean
	* @param list
	* @return
	 */
	private List<PersonStoreBean> getPersonStore(List<Object> list){
		List<PersonStoreBean> beans = new ArrayList<PersonStoreBean>();
		for(Object obj:list){
			PersonStoreBean bean = new PersonStoreBean();
			PersonStore store = (PersonStore)obj;
			String id = store.getId();
			bean.setId(store.getId());
			bean.setDeptId(store.getDeptId());
			bean.setEid(store.getEid());
			bean.setName(store.getName());
			bean.setAddress(store.getAddress());
			bean.setBdLng(store.getBdLng());
			bean.setBdLat(store.getBdLat());
			bean.setCtLng(store.getCtLng());
			bean.setCtLat(store.getCtLat());
			bean.setShopkeeperName(store.getShopkeeperName());
			bean.setShopkeeperPhone(store.getShopkeeperPhone());
			bean.setTypeId(store.getTypeId());
			bean.setSource(store.getSource());
			bean.setUserId(store.getUserId());
			bean.setIco(store.getIco());
			List<PersonPic> pics  = picConsumer.getPicList("1", id);//type 1表示门店
			if(pics != null){
				for(PersonPic pic:pics){
					if(StringUtils.isBlank(pic.getUrl())){
						pic.setUrl(Constant.PERSON_IMG_PATH+pic.getUrl());
					}
				}
				bean.setPics(pics);
			}
			if(bean.getTypeId() != null){
				PersonStoreType storeType = storeTypeConsumer.getStoreType(bean.getTypeId());

				bean.setTypeName(storeType.getName());
			}
			if(StringUtils.isBlank(bean.getDeptId())){
				Dept dept = deptConsumer.getDept(bean.getDeptId());
				if(dept != null)
					bean.setTypeName(dept.getName());
			}
			beans.add(bean);
		}
		return beans;
	}
}
