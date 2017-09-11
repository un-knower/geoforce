package com.supermap.egispservice.base.service.impl;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.supermap.egispservice.base.service.IMailService;


/**
 * 
 * <p>Title: MaiServiceImpl</p>
 * Description:		邮件服务类
 *
 * @author Huasong Huang
 * CreateTime: 2014-10-20 上午10:16:42
 */
@Service("mailService")
public class MailServiceImpl implements IMailService {

	private JavaMailSenderImpl sender;

	private static Logger LOGGER = Logger.getLogger(MailServiceImpl.class);
	
	
	@Override
	public boolean send(String to, String subject, String content) {
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom(sender.getUsername());
		smm.setSubject(subject);
		smm.setText(content);
		smm.setTo(to);
		boolean isSendSuccess = false;
		try{
			this.sender.send(smm);
			isSendSuccess = true;
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return isSendSuccess;
	}

	public JavaMailSenderImpl getSender() {
		return sender;
	}

	public void setSender(JavaMailSenderImpl sender) {
		this.sender = sender;
	}

	@Override
	public boolean sendHTMLMail(String to, String subject, String content) {
		// 建立邮件消息,发送简单邮件和html邮件的区别  
        MimeMessage mailMessage = sender.createMimeMessage();  
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,"utf-8");  
        boolean isSendSuccess = false;
        try {
        	// 设置收件人，寄件人  
            messageHelper.setTo(to);  
            messageHelper.setFrom(sender.getUsername());  
            messageHelper.setSubject(subject);  
            // true 表示启动HTML格式的邮件  
            messageHelper.setText(content,true);
            try{
    			this.sender.send(mailMessage);
    			isSendSuccess = true;
    		}catch (Exception e) {
    			LOGGER.error(e.getMessage(), e);
    		}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return isSendSuccess;
	}
}
