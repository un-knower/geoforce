package com.supermap.egispweb.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.SysLogService;
import com.supermap.egispservice.base.service.UserService;

//@Controller
//@RequestMapping("syslog")
public class SysLogAction {
	
	@Autowired
	SysLogService sysLogService;
	
	@Autowired
	UserService  userService;
	
	@RequestMapping("show")
	public String show(){
		return "sys/operationLogs";
	}
	
	
	@RequestMapping("getAllLogs")
	@ResponseBody
	public Map<String, Object> getAllLogs(String username,
			String deptids,String moduleids,
			@RequestParam(defaultValue = "2014-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date starttime,
			@RequestParam(defaultValue = "2999-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endtime, 
			@RequestParam(defaultValue="1",value="page")int page, @RequestParam(defaultValue="20",value="rows")int rows,HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			if(user==null){
			    throw new Exception("用户未登录");
			}
			
			if(rows>100){
				rows=100;
			}
			
			String userid=user.getId();
			String deptid=user.getDeptId().getId();
			String eid=user.getEid().getId();
			
			List<String> deptidlist=null;
			List<String> moduleidlist=null;
			List<String> useridlist=new ArrayList<String>();
			
			
			//部门列表
			if(deptids!=null&&deptids.equals("")){
				String arr[]=deptids.split(",");
				deptidlist=new ArrayList<String>();
				deptidlist=Arrays.asList(arr);
			}
			//模块列表
			if(moduleids!=null&&!moduleids.equals("")){
				String arr[]=moduleids.split(",");
				moduleidlist=new ArrayList<String>();
				moduleidlist=Arrays.asList(arr);
			}
			
			//该用户所能看到的所有子用户，包括自己
			useridlist.add(userid);
			List<UserEntity> childUser=this.userService.getUsersByDept(deptid);//此时包括了平级部门的其他用户
			if(childUser!=null&&childUser.size()>0){
				for(UserEntity u:childUser){
					String dept=u.getDeptId().getId();
					String uid=u.getId();
					if(dept!=null&&!dept.equals(deptid)&&!uid.equals(userid)){
						useridlist.add(uid);
					}
				}
			}
			
			map = this.sysLogService.findLogsByParam(useridlist, eid, deptidlist, username, moduleidlist,
					starttime, endtime, page, rows, "auto");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
