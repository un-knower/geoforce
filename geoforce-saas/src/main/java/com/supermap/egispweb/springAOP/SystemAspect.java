package com.supermap.egispweb.springAOP;


//import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.supermap.egispservice.base.service.SysLogService;


/**
 * 系统管理
 * @author Administrator
 *
 */
@Component
@Aspect
public class SystemAspect {
	
//	private static Logger LOGGER = Logger.getLogger(SystemAspect.class);

	@Autowired
	SysLogService sysLogService;
	
	SysLogUtils sysLogUtils=new SysLogUtils();
	
	/**
     * 系统管理  登录注销
     */
    @Pointcut("execution(* com.supermap.egispweb.action.WelcomeAction.*(..))")
    public void systemMethod() {
    }
    
   /**
    * 系统管理
    * @param jp
    * @param result
    * @Author Juannyoh
    * 2015-12-17上午9:22:27
    */
    @AfterReturning(value = "systemMethod()", returning = "result")
    public void doAfter(JoinPoint jp, Object result) {
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("showIndex")){
    		sysLogUtils.addlog(LOGConstants.STATUS_LOGININ,null,sysLogService);//登录
    	}
    }
    
    /**
     * 注销
     * @param jp
     * @Author Juannyoh
     * 2015-12-18下午2:06:50
     */
    @Before(value = "systemMethod()")
    public void doLogOutBefore(JoinPoint jp){
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("logout")){
    		sysLogUtils.addlog(LOGConstants.STATUS_LOGOUT,null,sysLogService);//注销
    	}
    }

    
    /**
     * 进入网点、区划、分单等模块切面
     * 
     * @Author Juannyoh
     * 2015-12-17上午9:25:12
     */
    @Pointcut("execution(* com.supermap.egispweb.action.OverlayAction.*(..))")
    public void moduleMethod() {
    }
    
    /**
     * 进入某某模块
     * @param jp
     * @param result
     * @Author Juannyoh
     * 2015-12-17上午9:22:34
     */
    @AfterReturning(value = "moduleMethod()", returning = "result")
    public void doModuleAfter(JoinPoint jp, Object result) {
    	sysLogUtils.addlog(LOGConstants.STATUS_ENTER,null,sysLogService,"进入页面");//进入
    }
    
    /**
     * 用户管理
     * 
     * @Author Juannyoh
     * 2015-12-17上午11:20:03
     */
    @Pointcut("execution(* com.supermap.egispweb.action.UserAction.*(..))")
    public void userMethod() {
    }
    
    @AfterReturning(value = "userMethod()", returning = "result")
    public void doUserAfter(JoinPoint jp, Object result) {
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("showIndex")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ENTER,null,sysLogService);//进入
    	}else if(method.equals("saveUser")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ADD,result,sysLogService);//增加
    	}else if(method.equals("updateUser")){
    		sysLogUtils.addlog(LOGConstants.STATUS_UPDATE,result,sysLogService);//修改
    	}else if(method.equals("updateUserInfo")){
    		sysLogUtils.addlog(LOGConstants.STATUS_UPDATESELEFINFO,result,sysLogService);//修改个人资料
    	}else if(method.equals("deleteUser")){
    		sysLogUtils.addlog(LOGConstants.STATUS_DELETE,result,sysLogService);//删除
    	}else if(method.equals("setUserDefaultCity")){
    		sysLogUtils.addlog(LOGConstants.STATUS_DEFAULTCITY,result,sysLogService);//设置默认城市
    	}
    }
    
    /**
     * 角色管理
     * 
     * @Author Juannyoh
     * 2015-12-17上午11:21:08
     */
    @Pointcut("execution(* com.supermap.egispweb.action.InfoRoleAction.*(..))")
    public void roleMethod() {
    }
    
    @AfterReturning(value = "roleMethod()", returning = "result")
    public void doRoleAfter(JoinPoint jp, Object result) {
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("show")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ENTER,null,sysLogService);//进入
    	}else if(method.equals("save")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ADD,result,sysLogService);//增加
    	}else if(method.equals("update")||method.equals("authorize")){
    		sysLogUtils.addlog(LOGConstants.STATUS_UPDATE,result,sysLogService);//修改
    	}else if(method.equals("delete")){
    		sysLogUtils.addlog(LOGConstants.STATUS_DELETE,result,sysLogService);//删除
    	}
    }
    
    /**
     * 部门管理
     * 
     * @Author Juannyoh
     * 2015-12-17上午11:27:01
     */
    @Pointcut("execution(* com.supermap.egispweb.action.sys.DeptAction.*(..))")
    public void deptMethod() {
    }
    
    @AfterReturning(value = "deptMethod()", returning = "result")
    public void doDeptAfter(JoinPoint jp, Object result) {
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("show")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ENTER,null,sysLogService);//进入
    	}else if(method.equals("saveDept")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ADD,result,sysLogService);//增加
    	}else if(method.equals("updateDept")||method.equals("authorize")){
    		sysLogUtils.addlog(LOGConstants.STATUS_UPDATE,result,sysLogService);//修改
    	}else if(method.equals("deleteDept")){
    		sysLogUtils.addlog(LOGConstants.STATUS_DELETE,result,sysLogService);//删除
    	}
    }
}
