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
 * 分单管理
 * @author Administrator
 *
 */
@Component
@Aspect
public class OrderAspect {
	
//	private static Logger LOGGER = Logger.getLogger(OrderAspect.class);

	@Autowired
	SysLogService sysLogService;
	
	SysLogUtils sysLogUtils=new SysLogUtils();
	
	/**
     * 分单
     */
    @Pointcut("execution(* com.supermap.egispweb.action.OrderAction.*(..))")
    public void systemMethod() {
    }
    
    /**
     * 地址纠错进入
     * 
     * @Author Juannyoh
     * 2015-12-17上午10:55:41
     */
    @Pointcut("execution(* com.supermap.egispweb.action.AddressCorrectionAction.*(..))")
    public void addrCorrMethod() {
    }
    
    /**
     * 地址匹配进入
     * 
     * @Author Juannyoh
     * 2015-12-17上午10:56:15
     */
    @Pointcut("execution(* com.supermap.egispweb.action.AddressMatchAction.*(..))")
    public void addrMatcMethod() {
    }
    

    /**
     * 分单
     * @param jp
     * @param result
     * @Author Juannyoh
     * 2015-12-17上午10:08:02
     */
    @AfterReturning(value = "systemMethod()", returning = "result")
    public void doAfter(JoinPoint jp, Object result) {
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("logisticsService")){
    		sysLogUtils.addlog(LOGConstants.STATUS_UPDATE,result,sysLogService);//修改
    	}else if(method.equals("uploadOrder")){
    		sysLogUtils.addlog(LOGConstants.STATUS_IMPORT,null,sysLogService);//导入
    	}else if(method.equals("addressMatchAndStore")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ADD,null,sysLogService);//地址匹配
    	}else if(method.equals("correctAdd")){
    		sysLogUtils.addlog(LOGConstants.STATUS_ADD,result,sysLogService);//地址纠错
    	}else if(method.equals("correctAddressMove")){
    		sysLogUtils.addlog(LOGConstants.STATUS_UPDATE,result,sysLogService);//地址纠错
    	}else if(method.equals("correctAddressDel")){
    		sysLogUtils.addlog(LOGConstants.STATUS_DELETE,result,sysLogService);//地址纠错
    	}
    }
    
    /**
     * 地址纠错进入
     * @param jp
     * @Author Juannyoh
     * 2015-12-17上午10:58:26
     */
    @AfterReturning(value = "addrCorrMethod()")
    public void doAddrCorr(JoinPoint jp){
    	sysLogUtils.addlog(LOGConstants.STATUS_ENTER,null,sysLogService);//进入地址纠错
    }
    
    /**
     * 地址匹配进入
     * @param jp
     * @Author Juannyoh
     * 2015-12-17上午10:58:40
     */
    @AfterReturning(value = "addrMatcMethod()")
    public void doAddrMatc(JoinPoint jp){
    	sysLogUtils.addlog(LOGConstants.STATUS_ENTER,null,sysLogService);//进入地址匹配
    }
}
