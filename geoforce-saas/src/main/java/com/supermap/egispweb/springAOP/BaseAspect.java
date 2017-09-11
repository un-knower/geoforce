package com.supermap.egispweb.springAOP;

//import org.apache.log4j.Logger;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.supermap.egispservice.base.service.SysLogService;

public class BaseAspect {
	
	/*private static Logger LOGGER = Logger.getLogger(BaseAspect.class);

	@Autowired
	SysLogService sysLogService;
	
	SysLogUtils sysLogUtils=new SysLogUtils();*/
	  /**
     * 前置通知
     * 
     * @param jp
     */
   /* @Before(value="systemMethod()")
    public void doBefore(JoinPoint jp) {
    	LOGGER.info("===========进入before advice============ ");
    	LOGGER.info("class：" + jp.getTarget().getClass() + "");
    	LOGGER.info("method: "+jp.getSignature().getName() + "'");
    	LOGGER.info("args[]:"+jp.getArgs()[0] + "");
    	LOGGER.info("要进入切入点方法了 \n");
    }*/

    /**
     * 后置通知
     * 
     * @param jp
     *            连接点
     * @param result
     *            返回值
     */
   /* @AfterReturning(value = "systemMethod()", returning = "result")
    public void doAfter(JoinPoint jp, Object result) {
    	LOGGER.info("==========进入after advice=========== ");
    	LOGGER.info("args[]:"+jp.getArgs());
    	LOGGER.info("calss:"+jp.getTarget().getClass());
    	LOGGER.info("method:"+jp.getSignature().getName());
    	LOGGER.info("result:" + result);
    	
    	String method=jp.getSignature().getName();//方法名称
    	if(method.equals("showIndex")){
    		sysLogUtils.addlog(LOGStatusConstant.LOGININ,sysLogService);//登录
    	}
    	else if(method.equals("logout")){
    		sysLogUtils.addlog(LOGStatusConstant.LOGOUT,sysLogService);//注销
    	}
    }
*/
    /**
     * 环绕通知
     * 
     * @param pjp
     *            连接点
     */
 /*   @Around(value = "anyMethod()")
    public void doAround(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("===========进入around环绕方法！=========== \n");

        // 调用目标方法之前执行的动作
        System.out.println("调用方法之前: 执行！\n");

        // 调用方法的参数
        Object[] args = pjp.getArgs();
        // 调用的方法名
        String method = pjp.getSignature().getName();
        // 获取目标对象
        Object target = pjp.getTarget();
        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
        Object result = pjp.proceed();

        System.out.println("输出：" + args[0] + ";" + method + ";" + target + ";" + result + "\n");
        System.out.println("调用方法结束：之后执行！\n");
    }*/

    /**
     * 异常通知
     * 
     * @param jp
     * @param e
     */
  /*  @AfterThrowing(value = "anyMethod()", throwing = "e")
    public void doThrow(JoinPoint jp, Throwable e) {
        System.out.println("删除出错啦");
    }*/
    
    
    
}
