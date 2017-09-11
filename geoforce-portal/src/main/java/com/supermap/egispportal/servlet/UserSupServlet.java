package com.supermap.egispportal.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.supermap.egispportal.constants.PortalConstants;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.exception.ParameterException;
import com.supermap.egispservice.base.pojo.BaseAccessInfo;
import com.supermap.egispservice.base.pojo.BaseUserInfo;
import com.supermap.egispservice.base.service.IMailService;
import com.supermap.egispservice.base.service.UserService;

public class UserSupServlet extends BaseServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = Logger.getLogger(UserSupServlet.class);
	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		if(!StringUtils.isEmpty(requestUri)){
			String servicename=requestUri.substring(requestUri.lastIndexOf("/")+1, requestUri.length());
			/*if(requestUri.endsWith("queryUserInfo")){
				queryUserInfo(req, resp);
			}else if(requestUri.endsWith("mailService")){
				sendEmail(req,resp);
			}
			else if(requestUri.endsWith("feedbackmailService")){
				sendFeedbackEmail(req,resp);
			}*/
			if(servicename.equalsIgnoreCase("queryUserInfo")){
				queryUserInfo(req, resp);
			}else if(servicename.equalsIgnoreCase("mailService")){
				sendEmail(req,resp);
			}
			else if(servicename.equalsIgnoreCase("feedbackmailService")){
				sendFeedbackEmail(req,resp);
			}
		}
	}
	
	


	


	/**
	 * 
	 * <p>Title ：sendEmail</p>
	 * Description：发送邮件
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2015-4-30 上午10:54:29
	 */
	private void sendEmail(HttpServletRequest req, HttpServletResponse resp) {
		
		String companyName = req.getParameter("name");
		String phone = req.getParameter("phone");
		try {
			if(StringUtils.isEmpty(companyName) || StringUtils.isEmpty(phone)){
				throw new Exception("信息不全");
			}
			companyName = defaultCharacterConvert(companyName);
			IMailService mailService = (IMailService) getBean("mailService");;
			String subject = "用户咨询";
			StringBuilder sb = new StringBuilder();
			sb.append("公司名称：").append(companyName);
			sb.append("\r\n联系电话:").append(phone);
			boolean isSuccess = mailService.send(PortalConstants.TARGET_MAIL, subject, sb.toString());
			write(req, resp, "发送"+(isSuccess?"成功":"失败"), null, isSuccess);
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}


	/**
	 * 
	 * <p>Title ：queryUserInfo</p>
	 * Description：		查询用户详情
	 * @param req
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-9-4 上午11:35:28
	 */
	private void queryUserInfo(HttpServletRequest req, HttpServletResponse resp) {
		try{
			HttpSession session = req.getSession();
			LOGGER.info("## SESSION ID : ["+session.getId()+"]");
			if(session == null || session.getAttribute(PortalConstants.USER_INFO_CONSTANTS) == null){
				throw new ParameterException("用户未登录");
			}
			BaseAccessInfo accessInfo = (BaseAccessInfo) session.getAttribute(PortalConstants.USER_INFO_CONSTANTS);
			if(null == accessInfo){
				throw new ParameterException("用户信息为空,请登录");
			}
			BaseUserInfo bui = null;
			if(!accessInfo.getSource().equals("-1")){
				String id = accessInfo.getUserId();
				UserService userService = (UserService) getBean("userService");
				 bui = userService.findEbossUserById(id);
			}else{
				bui = new BaseUserInfo();
				bui.setEmail(accessInfo.getEmail());
				bui.setUsername(accessInfo.getUsername());
				bui.setTelephone(accessInfo.getTel());
			}
			write(req, resp, null, bui, true);
		}catch(Exception e){
			write(req, resp, e.getMessage(), null, false);
			LOGGER.warn(e.getMessage());
		}
	}
	
	
	/**
	 * 意见反馈
	 * @param req
	 * @param resp
	 * @Author Juannyoh
	 * 2016-7-21下午1:56:28
	 */
	private void sendFeedbackEmail(HttpServletRequest req, HttpServletResponse resp) {
		
		String modulename = req.getParameter("modulename");
		String type = req.getParameter("type");
		String content = req.getParameter("content");
		String contactinfo = req.getParameter("contactinfo");
		
		//系统已经登录了的就传一个key
		String userkey=req.getParameter("key");
		
		//如果是已经登录的用户，则同时获取登录用户的id 以及电话
		HttpSession session = req.getSession();
		if(session != null && session.getAttribute(PortalConstants.USER_INFO_CONSTANTS) != null){
			BaseAccessInfo accessInfo = (BaseAccessInfo) session.getAttribute(PortalConstants.USER_INFO_CONSTANTS);
			userkey=accessInfo.getUserId();
		}
		
		//获取用户信息
		if(!StringUtils.isEmpty(userkey)){
			UserService userService = (UserService) getBean("userService");
			UserEntity user=userService.findUserById(userkey);
			if(user!=null){
				String tel=(user.getTelephone()!=null&&!user.getTelephone().equals(""))?user.getTelephone():user.getMobilephone();
				contactinfo+="[KEY:"+userkey+",TEL:"+tel+"]";
			}
		}
		
		
		try {
			if(StringUtils.isEmpty(content)){
				throw new Exception("反馈信息为空");
			}
			modulename=defaultCharacterConvert(modulename);
			type = defaultCharacterConvert(type);
			content =  defaultCharacterConvert(content).replaceAll("\\n", "<br>");
			contactinfo =  defaultCharacterConvert(contactinfo);
			
			IMailService mailService = (IMailService) getBean("mailService");
			String subject = "意见反馈";
			String mailcontent=formatFeedbackContent(modulename,type,content,contactinfo);
			boolean isSuccess = mailService.sendHTMLMail(PortalConstants.TARGET_MAIL, subject, mailcontent);
			write(req, resp, "发送"+(isSuccess?"成功":"失败"), null, isSuccess);
		} catch (Exception e) {
			write(req, resp, e.getMessage(), null, false);
		}
	}
	
	public String formatFeedbackContent(String modulename,String type,String content,String contactinfo){
		StringBuilder sb=new StringBuilder();
		sb.append("<html>")
		.append("<body>")
		.append("<table width='60%' align='left' border=1 bordercolor=#000000 style='border-collapse:collapse;word-break:break-all; word-wrap:break-word;'>")
		.append("<tr>")
		.append("<td width='15%' style='font-weight: bold;'>功能模块：</td>")
		.append("<td width='85%'>"+modulename+"</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td width='15%' style='font-weight: bold;'>反馈类别：</td>")
		.append("<td width='85%'>"+type+"</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td width='15%' style='font-weight: bold;'>反馈内容：</td>")
		.append("<td width='85%'>"+content+"</td>")
		.append("</tr>")
		.append("<tr>")
		.append("<td width='15%' style='font-weight: bold;'>联系方式：</td>")
		.append("<td width='85%'>"+contactinfo+"</td>")
		.append("</tr>")
		.append("</table>")
		.append("</body>")
		.append("</html>");
		return sb.toString();
	}

}
