package com.supermap.egispweb.springAOP;

//import java.util.Date;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.apache.log4j.Logger;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.supermap.egispservice.base.entity.SysLogEntity;
//import com.supermap.egispservice.base.entity.UserEntity;
//import com.supermap.egispservice.base.service.SysLogService;


/**
 * 车辆管理  暂时不做
 * @author Administrator
 *
 */
//@Component
//@Aspect
public class CarManageAspect {
	
	/*private static Logger LOGGER = Logger.getLogger(CarManageAspect.class);

	@Autowired
	SysLogService sysLogService;
	
	SysLogUtils sysLogUtils=new SysLogUtils();
	
	*//**
     * 车辆管理
     *//*
    @Pointcut("execution(* com.supermap.egispweb.action.car.*.*(..))")
    public void carMethod() {
    }

    *//**
     *车辆管理
     * @param jp
     * @param result
     * @Author Juannyoh
     * 2015-12-17上午10:08:41
     *//*
    @AfterReturning(value = "carMethod()", returning = "result")
    public void doAfter(JoinPoint jp, Object result) {
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("addCar")){
        	if(result!=null){
        		Map m=(Map) result;
        		if(m!=null&&m.get("flag")!=null){
        			String flag=(String) m.get("flag");
        			if(flag!=null&&flag.equals("ok")){
        				sysLogUtils.addlog(LOGConstants.STATUS_ADD,null,sysLogService);//增加
        			}
        		}
        	}
    	}else if(method.equals("delCar")){
    		if(result!=null){
        		Map m=(Map) result;
        		if(m!=null&&m.get("flag")!=null){
        			String flag=(String) m.get("flag");
        			if(flag!=null&&flag.equals("ok")){
        				sysLogUtils.addlog(LOGConstants.STATUS_DELETE,null,sysLogService);//删除
        			}
        		}
        	}
    	}else if(method.equals("updateCar")){
    		if(result!=null){
        		Map m=(Map) result;
        		if(m!=null&&m.get("flag")!=null){
        			String flag=(String) m.get("flag");
        			if(flag!=null&&flag.equals("ok")){
        				sysLogUtils.addlog(LOGConstants.STATUS_UPDATE,null,sysLogService);//修改
        			}
        		}
        	}
    	}else if(method.contains("Bind")){
    		if(result!=null){
        		Map m=(Map) result;
        		if(m!=null&&m.get("flag")!=null){
        			String flag=(String) m.get("flag");
        			if(flag!=null&&flag.equals("ok")){
        				sysLogUtils.addlog(LOGConstants.STATUS_UPDATE,null,sysLogService);//绑定车辆、取消绑定
        			}
        		}
        	}
    	}
    }*/
}
