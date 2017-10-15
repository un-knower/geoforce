package com.supermap.egispweb.action.personSign;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.action.region.RegionSetAction;
import com.supermap.egispweb.common.Constant;
import com.supermap.egispweb.util.DateUtil;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPic;
/**
 * 门店action
 * @author wang
 *
 */
//@Controller
public class PersonSignAction extends BaseAction {
static Logger logger = Logger.getLogger(RegionSetAction.class.getName());
	/**
	 * 初始化跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/toPersonSign")
	public String toRegion(HttpServletRequest request,HttpServletResponse response){
		logger.info("to personSign.jsp");
		return "personSign/personSign";
	}
	/**
	 * 门店信息查询
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="/com/supermap/personSignList")
	@ResponseBody
	public Page planList(HttpSession session,HttpServletRequest request,HttpServletResponse response) throws ParseException{
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		HashMap<String, Object> hm = new HashMap<String, Object>();
		String name = request.getParameter("name");
		String status=request.getParameter("status");
		String begindate = request.getParameter("begindate");//开始签到时间
		String enddate = request.getParameter("enddate");//结束签到时间
		String deptCode=userEntity.getDeptId().getCode();
		if(StringUtils.isNotBlank(name)){
			hm.put("storeName", name);
		}
		if(StringUtils.isNotBlank(deptCode)){
			hm.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(status)){
			hm.put("status", status);
		}
		if(StringUtils.isNotBlank(begindate)){
			Date startTime = DateUtil.formatStringByDate(begindate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			hm.put("begindate",startTime);
		}
		if(StringUtils.isNotBlank(enddate)){
			Date endTime = DateUtil.formatStringByDate(enddate+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			hm.put("enddate", endTime);		
		}
			Page page = getPage(request);
			page= personSignConsumer.pagePersonSign(page, hm);
			return page;
	}
	/**
	 * 照片查看
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="/com/supermap/personSignPic")
	@ResponseBody
	public  List<PersonPic> personSignPic(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		List<PersonPic> pics=null;
		String id = request.getParameter("id");
		try {
			pics=picConsumer.getPicList("1", id);
			if(pics != null){
				for(PersonPic pic:pics){
					if(StringUtils.isNotBlank(pic.getUrl())){
						pic.setUrl(pic.getUrl());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pics;
	}
}
