package com.supermap.egispweb.springAOP;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.SysLogService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.egispweb.util.StringUtil;


/**
 * 区划管理
 * @author Administrator
 *
 */
@Component
@Aspect
public class AreaAspect {
	
	private static Logger LOGGER = Logger.getLogger(AreaAspect.class);

	@Resource
	private SysLogService sysLogService;
	
	@Resource
	private IAreaService areaService;
	
	@Resource
	private IGeocodingService geocodingService;
	
	@Resource
	private  UserService userService;
	
	
	SysLogUtils sysLogUtils=new SysLogUtils();
	
	
	private static final String[] areastatuArrs={"正常","停用"};
	
	/**
     * 指定切入点匹配表达式，注意它是以方法的形式进行声明的。
     */
    @Pointcut("execution(* com.supermap.egispweb.action.AreaManagerAction.*(..))")
    public void systemMethod() {
    }
    
    
    /**
     * 修改区划属性切面
     * @param pjp
     * @return
     * @Author Juannyoh
     * 2016-12-20下午7:18:32
     */
    @SuppressWarnings("unchecked")
	@Around(value = "execution(* com.supermap.egispweb.action.AreaManagerAction.updateAttr(..))")
    public Object doAround_updateAttr(ProceedingJoinPoint pjp){
    	 Map<String,Object> result=null;
    	try {
            // 调用方法的参数
            Object[] args = pjp.getArgs();
            String areaid=StringUtil.convertObjectToString(args[0]);
            String areaname=StringUtil.convertObjectToString(args[1]);
            String areaNum=StringUtil.convertObjectToString(args[3]);
            String admincode=StringUtil.convertObjectToString(args[4]);
//            String pointid=StringUtil.convertObjectToString(args[5]);
            int areastatus=StringUtil.convertObjectToInt(args[6]);
            String relationareaid=StringUtil.convertObjectToString(args[7]);
            StringBuilder dataSb=new StringBuilder();
            //查询修改前的区划信息
            AreaEntity oldarea=this.areaService.queryByIdOrNumber(areaid, null, null, false);
            result = (Map<String, Object>) pjp.proceed();
            boolean isSuccess=(Boolean) result.get("isSuccess");
            boolean changename=false;
            if(isSuccess){
            	if(!areaname.equals(oldarea.getName())){
            		changename=true;
            		dataSb.append("修改区划\"").append(oldarea.getName()).append("\"名称：将\"").append(oldarea.getName()).append("\"改为\"").append(areaname).append("\";");
            	}
            	if(!areaNum.equals(oldarea.getAreaNumber())){
            		dataSb.append("修改区划\"").append(oldarea.getName()).append("\"编号：将\"").append(oldarea.getAreaNumber()).append("\"改为\"").append(areaNum).append("\";");
            	}
            	if(!admincode.equals(oldarea.getAdmincode())){
            		dataSb.append("修改区划\"").append(oldarea.getName()).append("\"归属：将\"").append(getNameByAdmincode(oldarea.getAdmincode())).append("\"改为\"").append(getNameByAdmincode(admincode)).append("\";");
            	}
            	if(areastatus!=oldarea.getArea_status()){
            		if(areastatus==0){//正常
            			dataSb.append("修改区划\"").append(oldarea.getName()).append("\"状态：将\"").append(areastatuArrs[oldarea.getArea_status()]).append("\"改为\"").append(areastatuArrs[areastatus]).append("\"");
            			if(!StringUtils.isEmpty(oldarea.getRelation_areaid())){
            				AreaEntity relationarea=this.areaService.queryByIdOrNumber(oldarea.getRelation_areaid(), null, null, false);
            				dataSb.append(",并解除与区划：\"").append(relationarea.getName()).append("\"的绑定;");
            			}else{
            				dataSb.append(";");
            			}
            		}else{
            			dataSb.append("修改区划\"").append(oldarea.getName()).append("\"状态：将\"").append(areastatuArrs[oldarea.getArea_status()]).append("\"改为\"").append(areastatuArrs[areastatus]).append("\";");
            		}
            	}
            	if(areastatus==1&&!relationareaid.equals(oldarea.getRelation_areaid())){
            		AreaEntity relationarea=this.areaService.queryByIdOrNumber(relationareaid, null, null, false);
        			if(changename){
        				dataSb.append("关联区划：将\"").append(areaname).append("\"与\"").append(relationarea.getName()).append("\"关联");
        			}else{
        				dataSb.append("关联区划：将\"").append(oldarea.getName()).append("\"与\"").append(relationarea.getName()).append("\"关联");
        			}
        			if(!StringUtils.isEmpty(oldarea.getRelation_areaid())&&!StringUtils.isEmpty(oldarea.getRelation_areaname())){
        				dataSb.append(",并与\"").append(oldarea.getRelation_areaname()).append("\"解除关联");
        			}
            	}
            	sysLogUtils.addlog(LOGConstants.STATUS_UPDATE,result,sysLogService,dataSb.toString());//修改
            }
		} catch (Exception e) {
			LOGGER.info("修改区划时，保存日志记录失败"+e.getMessage());
		} catch (Throwable e) {
			LOGGER.info("修改区划时，保存日志记录失败"+e.getMessage());
		}
    	return result;
    }
    
    
    /**
     * 删除区划日志切面
     * @param pjp
     * @return
     * @Author Juannyoh
     * 2016-12-20下午7:30:07
     */
	@SuppressWarnings("unchecked")
	@Around(value = "execution(* com.supermap.egispweb.action.AreaManagerAction.deleteArea(..))")
	public Object doAround_deleteArea(ProceedingJoinPoint pjp) {
		Map<String, Object> result = null;
		try {
			// 调用方法的参数
			Object[] args = pjp.getArgs();
			String areaid = StringUtil.convertObjectToString(args[0]);
			StringBuilder dataSb = new StringBuilder();
			// 查询修改前的区划信息
			AreaEntity oldarea = this.areaService.queryByIdOrNumber(areaid,
					null, null, false);
			result = (Map<String, Object>) pjp.proceed();
			boolean isSuccess = (Boolean) result.get("isSuccess");
			if (isSuccess) {
				dataSb.append("删除区划：" + oldarea.getName());
				sysLogUtils.addlog(LOGConstants.STATUS_DELETE, result,
						sysLogService, dataSb.toString());// 删除
			}
		} catch (Exception e) {
			LOGGER.info("删除区划时，保存日志记录失败" + e.getMessage());
		} catch (Throwable e) {
			LOGGER.info("删除区划时，保存日志记录失败" + e.getMessage());
		}
		return result;
	}
	
	/**
	 * 合并区划 日志切面
	 * @param pjp
	 * @return
	 * @Author Juannyoh
	 * 2016-12-20下午8:03:21
	 */
	@SuppressWarnings("unchecked")
	@Around(value = "execution(* com.supermap.egispweb.action.AreaManagerAction.union(..))")
	public Object doAround_union(ProceedingJoinPoint pjp) {
		Map<String, Object> result = null;
		try {
			// 调用方法的参数
			Object[] args = pjp.getArgs();
			String ids = StringUtil.convertObjectToString(args[0]);
			String areaIds[] = ids.split("_");
			StringBuilder dataSb = new StringBuilder();
			// 查询修改前的区划信息
			AreaEntity firstarea = this.areaService.queryByIdOrNumber(areaIds[0],null, null, false);
			String firstname=firstarea.getName();
			dataSb.append("合并\"").append(firstname).append("\"");
			for(int i=1;i<areaIds.length;i++){
				AreaEntity area = this.areaService.queryByIdOrNumber(areaIds[i],null, null, false);
				dataSb.append("和\"" ).append( area.getName()).append("\"");
			}
			dataSb.append("为\"").append(firstname).append("\"");
			result = (Map<String, Object>) pjp.proceed();
			boolean isSuccess = (Boolean) result.get("isSuccess");
			if (isSuccess) {
				sysLogUtils.addlog(LOGConstants.STATUS_AREAMERGE, result,
						sysLogService, dataSb.toString());// 合并
			}
		} catch (Exception e) {
			LOGGER.info("合并区划时，保存日志记录失败" + e.getMessage());
		} catch (Throwable e) {
			LOGGER.info("合并区划时，保存日志记录失败" + e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 修改所属子账号
	 * @param pjp
	 * @return
	 * @Author Juannyoh
	 * 2016-12-20下午8:15:25
	 */
	@SuppressWarnings("unchecked")
	@Around(value = "execution(* com.supermap.egispweb.action.AreaManagerAction.updateOwner(..))")
	public Object doAround_updateOwner(ProceedingJoinPoint pjp) {
		Map<String, Object> result = null;
		try {
			// 调用方法的参数
			Object[] args = pjp.getArgs();
			String areaid = StringUtil.convertObjectToString(args[0]);
			String newuserid=StringUtil.convertObjectToString(args[1]);
			StringBuilder dataSb = new StringBuilder();
			// 查询修改前的区划信息
			AreaEntity oldarea = this.areaService.queryByIdOrNumber(areaid,null, null, false);
			result = (Map<String, Object>) pjp.proceed();
			boolean isSuccess = (Boolean) result.get("isSuccess");
			if (isSuccess) {
				if(!newuserid.equals(oldarea.getUser_id())){
					UserEntity olduser=this.userService.findUserById(oldarea.getUser_id());
					UserEntity newuser=this.userService.findUserById(newuserid);
					String oldname=olduser==null?"":olduser.getRealname();
					String newname=newuser==null?"":newuser.getRealname();
					dataSb.append("子账号归属：将\"")
						.append(oldarea.getName())
						.append("\"所属子账号由\"")
						.append(oldname)
						.append("\"改为\"")
						.append(newname)
						.append("\"");
					sysLogUtils.addlog(LOGConstants.STATUS_UPDATE, result,
							sysLogService, dataSb.toString());// 修改
				}
			}
		} catch (Exception e) {
			LOGGER.info("修改区划所属帐号时，保存日志记录失败" + e.getMessage());
		} catch (Throwable e) {
			LOGGER.info("修改区划所属帐号时，保存日志记录失败" + e.getMessage());
		}
		return result;
	}
	
	/**
	 * 反选行政区划
	 * @param pjp
	 * @return
	 * @Author Juannyoh
	 * 2016-12-20下午8:29:53
	 */
	@SuppressWarnings("unchecked")
	@Around(value = "execution(* com.supermap.egispweb.action.AreaManagerAction.saveReverseSelectionArea(..))")
	public Object doAround_saveReverseSelectionArea(ProceedingJoinPoint pjp) {
		Map<String, Object> result = null;
		try {
			result = (Map<String, Object>) pjp.proceed();
			StringBuilder  dataSb=new StringBuilder();
			boolean isSuccess = (Boolean) result.get("isSuccess");
			if (isSuccess) {
				String id=StringUtil.convertObjectToString(result.get("result"));
				if(!StringUtils.isEmpty(id)){
					AreaEntity area=this.areaService.queryByIdOrNumber(id, null, null, false);
					dataSb.append("行政区划：新增\"")
						.append(area.getName())
						.append("\"");
					sysLogUtils.addlog(LOGConstants.STATUS_ADD, result,
							sysLogService, dataSb.toString());// 新增
				}
			}
		} catch (Exception e) {
			LOGGER.info("修改区划所属帐号时，保存日志记录失败" + e.getMessage());
		} catch (Throwable e) {
			LOGGER.info("修改区划所属帐号时，保存日志记录失败" + e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 修改区划状态
	 * @param pjp
	 * @return
	 * @Author Juannyoh
	 * 2016-12-21上午9:38:23
	 */
	@SuppressWarnings("unchecked")
	@Around(value = "execution(* com.supermap.egispweb.action.AreaManagerAction.updateAreaStatus(..))")
	public Object doAround_updateAreaStatus(ProceedingJoinPoint pjp) {
		Map<String, Object> result = null;
		try {
			// 调用方法的参数
			Object[] args = pjp.getArgs();
			String areaid = StringUtil.convertObjectToString(args[0]);
			int areastatus=StringUtil.convertObjectToInt(args[1]);
			StringBuilder dataSb = new StringBuilder();
			// 查询修改前的区划信息
			AreaEntity oldarea = this.areaService.queryByIdOrNumber(areaid,null, null, false);
			result = (Map<String, Object>) pjp.proceed();
			boolean isSuccess = (Boolean) result.get("isSuccess");
			if (isSuccess) {
				if(areastatus!=oldarea.getArea_status()){
            		if(areastatus==0){//正常
            			dataSb.append("修改区划\"").append(oldarea.getName()).append("\"状态：将\"").append(areastatuArrs[oldarea.getArea_status()]).append("\"改为\"").append(areastatuArrs[areastatus]).append("\"");
            			if(!StringUtils.isEmpty(oldarea.getRelation_areaid())){
            				AreaEntity relationarea=this.areaService.queryByIdOrNumber(oldarea.getRelation_areaid(), null, null, false);
            				dataSb.append(",并解除与区划：\"").append(relationarea.getName()).append("\"的绑定;");
            			}else{
            				dataSb.append(";");
            			}
            		}else{
            			dataSb.append("修改区划\"").append(oldarea.getName()).append("\"状态：将\"").append(areastatuArrs[oldarea.getArea_status()]).append("\"改为\"").append(areastatuArrs[areastatus]).append("\";");
            		}
            	}
					sysLogUtils.addlog(LOGConstants.STATUS_UPDATE, result,
							sysLogService, dataSb.toString());// 修改
			}
		} catch (Exception e) {
			LOGGER.info("修改区划所属帐号时，保存日志记录失败" + e.getMessage());
		} catch (Throwable e) {
			LOGGER.info("修改区划所属帐号时，保存日志记录失败" + e.getMessage());
		}
		return result;
	}
	
	
	

   /**
    * 区划管理
    * @param jp
    * @param result
    * @Author Juannyoh
    * 2015-12-17上午10:08:24
    */
    @AfterReturning(value = "systemMethod()", returning = "result")
    public void doAfter(JoinPoint jp, Object result) {
    	String method=jp.getSignature().getName();//方法名称
    	Object args[]=jp.getArgs();
    	try {
    		if(method.equals("addArea")){
        		HttpServletRequest request=(HttpServletRequest) args[0];
        		String areaname=request.getParameter("areaName");
        		String operType=request.getParameter("operType");//操作类型（1 手动画区；2 沿路画区）
        		StringBuilder sb=new StringBuilder();
        		if(org.apache.commons.lang.StringUtils.isNotEmpty(operType)&&operType.equals("2")){
        			sb.append("沿路画区：新增\"").append(areaname).append("\"");
        		}else{
        			sb.append("自由画区：新增\"").append(areaname).append("\"");
        		}
        		sysLogUtils.addlog(LOGConstants.STATUS_ADD,result,sysLogService,sb.toString());//增加
        	}else if(method.contains("updateRegion")){//修改区划范围
        		HttpServletRequest request=(HttpServletRequest) args[0];
        		String areaid=request.getParameter("id");
        		AreaEntity area=this.areaService.queryByIdOrNumber(areaid, null, null, false);
        		StringBuilder sb=new StringBuilder();
        		sb.append("编辑区划：修改\"").append(area.getName()).append("\"的范围");
        		sysLogUtils.addlog(LOGConstants.STATUS_UPDATE,result,sysLogService,sb.toString());//修改区划范围
        	}else if(method.contains("lineSpilt")){
        		HttpServletRequest request=(HttpServletRequest) args[0];
        		String areaid=request.getParameter("id");
        		String operType=request.getParameter("operType");//操作类型（1 线拆分；2 面拆分，默认为1）
        		AreaEntity area=this.areaService.queryByIdOrNumber(areaid, null, null, false);
        		StringBuilder sb=new StringBuilder();
        		String newname=makeNewNumberTail(area.getName());
        		if(!StringUtils.isEmpty(operType)&&operType.equals("2")){
        			sb.append("面拆分：将\"").append(area.getName()).append("\"拆分成\"").append(area.getName()).append("\"和\"").append(newname).append("\"");
        		}else{
        			sb.append("线拆分：将\"").append(area.getName()).append("\"拆分成\"").append(area.getName()).append("\"和\"").append(newname).append("\"");;
        		}
        		sysLogUtils.addlog(LOGConstants.STATUS_AREASPLIT,result,sysLogService,sb.toString());//线/面拆分
        	}else if(method.contains("import")){
        		sysLogUtils.addlog(LOGConstants.STATUS_IMPORT,result,sysLogService);//导入
        	}else if(method.contains("export")){
        		sysLogUtils.addlog(LOGConstants.STATUS_EXPORT,result,sysLogService);//导出
        	}
		} catch (Exception e) {
			LOGGER.info("区划：记录日志失败" + e.getMessage());
		}
    	
    }
    
    
    /**
     * 根据admincode查询名称
     * @param admincode
     * @return
     * @Author Juannyoh
     * 2016-12-20下午7:53:51
     */
    public String getNameByAdmincode(String admincode){
    	StringBuilder sb=new StringBuilder();
    	Map<String,Object> namemap = geocodingService.getCountyByAdmincode(admincode);
    	if(namemap!=null){
    		if(namemap.get("PROVINCE")!=null){
    			sb.append(StringUtil.convertObjectToString(namemap.get("PROVINCE")));
    		}
    		if(namemap.get("CITY")!=null){
    			sb.append(StringUtil.convertObjectToString(namemap.get("CITY")));
    		}
    		if(namemap.get("COUNTY")!=null){
    			sb.append(StringUtil.convertObjectToString(namemap.get("COUNTY")));
    		}
    		if(namemap.get("TOWN")!=null){
    			sb.append(StringUtil.convertObjectToString(namemap.get("TOWN")));
    		}
    	}
    	return sb.toString();
    }
    
    /**
     * 拆分的面名字规则
     * @param oldNum
     * @return
     * @Author Juannyoh
     * 2016-12-20下午7:54:07
     */
    private static String makeNewNumberTail(String oldNum){
		StringBuilder sb = new StringBuilder();
		if(!StringUtils.isEmpty(oldNum)){
			
			int index = oldNum.lastIndexOf("_");
			int number = 1;
			String prefix = "";
			if(index > 0 && index < oldNum.length() - 1){
				prefix = oldNum.substring(0,index);
				String sub = oldNum.substring(index + 1, oldNum.length());
				try{
					number = Integer.parseInt(sub);
					number ++;
				}catch(Exception e){
					prefix = oldNum;
				}
			}else{
				prefix = oldNum;
			}
			sb.append(prefix).append("_").append(number);
		}else{
			sb.append("01");
		}
		
		return sb.toString();
	}

}
