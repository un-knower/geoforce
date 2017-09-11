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
 * 统计分析
 * @author Administrator
 *
 */
@Component
@Aspect
public class StatisticAspect {
	
//	private static Logger LOGGER = Logger.getLogger(StatisticAspect.class);

	@Autowired
	SysLogService sysLogService;
	
	SysLogUtils sysLogUtils=new SysLogUtils();
	
	/**
     * 统计分析
     */
    @Pointcut("execution(* com.supermap.egispweb.action.statistic.*.*(..))")
    public void systemMethod() {
    }

   /**
    * 统计分析
    * @param jp
    * @param result
    * @Author Juannyoh
    * 2015-12-17上午11:31:31
    */
    @AfterReturning(value = "systemMethod()", returning = "result")
    public void doAfter(JoinPoint jp, Object result) {
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("show")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ENTER,null,sysLogService);//进入
    	}
    }

}
