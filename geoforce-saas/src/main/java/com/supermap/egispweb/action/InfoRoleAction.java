package com.supermap.egispweb.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.supermap.egispservice.base.entity.InfoRoleEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.entity.ZtreeVO;
import com.supermap.egispservice.base.service.InfoRoleFunctionService;
import com.supermap.egispservice.base.service.InfoRoleService;
/**
 * 
 * @description 角色管理
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-9-15
 * @version 1.0
 */
@Controller
@RequestMapping("role")
@SessionAttributes(types = { UserEntity.class }, value = { "user" })
public class InfoRoleAction {
	
	@Autowired
	private InfoRoleService infoRoleService;
	@Autowired
	private InfoRoleFunctionService infoRoleFunctionService;
	
	@RequestMapping("show")
	public String show(){
		return "role";
	}

	@RequestMapping("save")
	@ResponseBody
	public Map<String , Object> save(@RequestParam(value="rolename2") String rolename,String remark,HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		InfoRoleEntity entity=new InfoRoleEntity();
		entity.setCreateUserid(user.getId());
		entity.setEid(user.getEid().getId());
		entity.setName(rolename);
		entity.setOperDate(new Date());
		entity.setRemark(remark);
		infoRoleService.save(entity);
		Map<String , Object> m=new HashMap<String, Object>();
		m.put("flag", "ok");
		return m;
	}
	
	@RequestMapping("update")
	@ResponseBody
	public Map<String , Object> update(String id,@RequestParam(value="rolename2")String rolename,String remark){
		InfoRoleEntity entity=new InfoRoleEntity();
		entity.setId(id);
		entity.setName(rolename);
		entity.setRemark(remark);
		infoRoleService.update(entity);
		Map<String , Object> m=new HashMap<String, Object>();
		m.put("flag", "ok");
		return m;
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Map<String , Object> delete(String id){
		infoRoleService.deleteById(id) ;
		Map<String , Object> m=new HashMap<String, Object>();
		m.put("flag", "ok");
		return m;
	}
	
	@RequestMapping("get")
	@ResponseBody
	public Map<String, Object> query(String rolename,@RequestParam(defaultValue="2014-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date starttime,
			@RequestParam(defaultValue="2114-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date endtime, 
			int page,int rows,HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String, Object>  map=infoRoleService.getRoles(user.getId(),user.getEid().getId(),user.getDeptId().getId(),rolename, starttime, endtime, page, rows, "auto");
		return map;
	}
	
	/**
	 * 根据角色ID获取对应的菜单树信息
	 */
	@RequestMapping("tree")
	@ResponseBody
	public List<ZtreeVO> query(String roleId,HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		List<ZtreeVO> list=infoRoleService.getCheckedMenu(user.getId(),user.getEid().getId(),user.getDeptId().getId(), roleId,user.getSourceId());
		return list;
	}
	
	/**
	 * 授权
	 */
	@RequestMapping("authorize")
	@ResponseBody
	public Map<String , Object> authorize(String checkedMenuIds,String roleId){
		infoRoleFunctionService.authorize(checkedMenuIds, roleId);
		Map<String , Object> m=new HashMap<String, Object>();
		m.put("flag", "ok");
		return m;
	}
	
}
