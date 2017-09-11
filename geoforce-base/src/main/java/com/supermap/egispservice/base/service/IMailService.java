package com.supermap.egispservice.base.service;

public interface IMailService {

	/**
	 * 
	 * <p>Title ：send</p>
	 * Description：		向指定的用户发送邮件
	 * @param to		用户邮箱
	 * @param subject	主题
	 * @param content	内容（纯文本)
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-20 上午10:05:46
	 */
	public boolean send(String to,String subject,String content);
	
	/**
	 * 发送带有html标签的邮件
	 * @param to
	 * @param subject
	 * @param content
	 * @return
	 * @Author Juannyoh
	 * 2016-7-21下午3:30:17
	 */
	public boolean sendHTMLMail(String to,String subject,String content);
}
