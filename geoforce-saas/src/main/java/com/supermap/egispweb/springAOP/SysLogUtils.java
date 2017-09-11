package com.supermap.egispweb.springAOP;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.supermap.egispservice.base.entity.SysLogEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.SysLogService;

public class SysLogUtils {
	
	SysLogService sysLogService;
	
	public SysLogUtils() {
	}

	public SysLogUtils(SysLogService sysLogService) {
		this.sysLogService=sysLogService;
	}

	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 * @Author Juannyoh
	 * 2015-12-17上午9:56:14
	 */
	public String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	
	/**
	 * 添加日志
	 * @param operdesc
	 * @param sysLogService
	 * @Author Juannyoh
	 * 2015-12-17上午9:56:03
	 */
	@SuppressWarnings("unchecked")
	public void addlog(String operdesc,Object result,SysLogService sysLogService){
		boolean flag=true;
		if(result!=null&&!result.equals("")){
			flag=isSuccessResult(result);
		}
		if(flag){
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
	        ServletRequestAttributes sra = (ServletRequestAttributes)ra;  
	        HttpServletRequest request = sra.getRequest();
	        HttpSession  session = request.getSession();
	        UserEntity user=(UserEntity)session.getAttribute("user");
	        //String moduleid=request.getParameter("moduleId");
	        //获取moduleid
	        String moduleid=null;
	        Map<String, Object[]> params = request.getParameterMap();
	        for (String key : params.keySet()) {
	        	if(key.equalsIgnoreCase("moduleid")){
	        		String[] values = (String[]) params.get(key);
		            if(values!=null&&values.length>0){
		            	moduleid=values[0];
		            }
		            break;
	        	}
	        }
	        
	        if(operdesc.equalsIgnoreCase(LOGConstants.STATUS_LOGININ)){
	        	moduleid="5";
	        }
	        if(operdesc.equalsIgnoreCase(LOGConstants.STATUS_LOGOUT)){
	        	moduleid="6";
	        }
	        
	        SysLogEntity logentity=new SysLogEntity();
	        logentity.setUserId(user);
	        logentity.setModuleId(moduleid);
	        logentity.setDepartmentId(user.getDeptId().getId());
	        logentity.setEnterpriseId(user.getEid().getId());
	        logentity.setOperTime(new Date());
	        logentity.setOperDesc(operdesc);
	        logentity.setIpaddr(getRemoteHost(request));
	        sysLogService.saveSysLogEntity(logentity);
		}
    }
	
	/**
	 * 添加明细数据操作日志
	 * @param operdesc
	 * @param result
	 * @param sysLogService
	 * @param dataDesc
	 * @Author Juannyoh
	 * 2016-12-20下午5:45:34
	 */
	@SuppressWarnings("unchecked")
	public void addlog(String operdesc,Object result,SysLogService sysLogService,String dataDesc){
		boolean flag=true;
		if(result!=null&&!result.equals("")){
			flag=isSuccessResult(result);
		}
		if(flag){
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
	        ServletRequestAttributes sra = (ServletRequestAttributes)ra;  
	        HttpServletRequest request = sra.getRequest();
	        HttpSession  session = request.getSession();
	        UserEntity user=(UserEntity)session.getAttribute("user");
	        //String moduleid=request.getParameter("moduleId");
	        //获取moduleid
	        String moduleid=null;
	        Map<String, Object[]> params = request.getParameterMap();
	        for (String key : params.keySet()) {
	        	if(key.equalsIgnoreCase("moduleid")){
	        		String[] values = (String[]) params.get(key);
		            if(values!=null&&values.length>0){
		            	moduleid=values[0];
		            }
		            break;
	        	}
	        }
	        
	        if(operdesc.equalsIgnoreCase(LOGConstants.STATUS_LOGININ)){
	        	moduleid="5";
	        }
	        if(operdesc.equalsIgnoreCase(LOGConstants.STATUS_LOGOUT)){
	        	moduleid="6";
	        }
	        
	        SysLogEntity logentity=new SysLogEntity();
	        logentity.setUserId(user);
	        logentity.setModuleId(moduleid);
	        logentity.setDepartmentId(user.getDeptId().getId());
	        logentity.setEnterpriseId(user.getEid().getId());
	        logentity.setOperTime(new Date());
	        logentity.setOperDesc(operdesc);
	        logentity.setIpaddr(getRemoteHost(request));
	        logentity.setDataDesc(dataDesc);
	        sysLogService.saveSysLogEntity(logentity);
		}
    }
	
	
	/**
     * 根据返回结果，判断是否成功
     * @param result
     * @return
     * @Author Juannyoh
     * 2015-12-17上午9:54:44
     */
    @SuppressWarnings("rawtypes")
	public boolean isSuccessResult(Object result){
    	boolean flag = false;
    	Map m=(Map) result;
    	if(m!=null&&m.get("isSuccess")!=null){
    		flag=(Boolean) m.get("isSuccess");
    	}
    	return flag;
    }
}
