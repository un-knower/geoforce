package com.supermap.egispweb.springAOP;



//import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.supermap.egispservice.base.service.SysLogService;


/**
 * 线路规划
 * @author Administrator
 *
 */
@Component
@Aspect
public class PathPlanAspect {
	
//	private static Logger LOGGER = Logger.getLogger(PathPlanAspect.class);

	@Autowired
	SysLogService sysLogService;
	
	SysLogUtils sysLogUtils=new SysLogUtils();
	
	/**
     * 指定切入点匹配表达式，注意它是以方法的形式进行声明的。
     */
    @Pointcut("execution(* com.supermap.egispweb.action.PathPlanAction.*(..))")
    public void systemMethod() {
    }

    /**
     * 网点管理
     * @param jp
     * @param result
     * @Author Juannyoh
     * 2015-12-17上午10:08:41
     */
    @AfterReturning(value = "systemMethod()", returning = "result")
    public void doAfter(JoinPoint jp, Object result) {
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("show")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ENTER,null,sysLogService);//进入
    	}else if(method.equals("save")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ADD,result,sysLogService);//增加
    	}else if(method.equals("delete")){
    		sysLogUtils.addlog(LOGConstants.STATUS_DELETE,result,sysLogService);//删除
    	}
    }

}
