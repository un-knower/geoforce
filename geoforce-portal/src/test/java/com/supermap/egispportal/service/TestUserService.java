/*package com.supermap.egispportal.service;

import java.io.StringReader;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.supermap.egispportal.entity.UserEntity;
import com.supermap.egispportal.exception.ParameterException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestUserService extends TestCase {
	@Autowired
	private IUserService userService;
	
//	@Resource
//	private ITestService testService;
	
	@Test
	public void testSaveUser(){
		UserEntity user=new UserEntity();
//		user.setId(1);
		user.setAddress("四川成都天府软件园");
//		user.setCompanyName("超图软件222");
		user.setEmail("webmaster@supermap.com");
//		user.setMobilephone("13800138000");
		user.setPassword("supermap123");
		user.setRealName("超人");
//		user.setTelephone("028-888888");
		user.setUsername("supermap");
		user.setZipCode("610000");
//		userService.saveUser(user);
	}
	
//	@Test
//	public void testSaveTestEntity(){
//		TestEntity te = new TestEntity();
//		te.setName("hh");
//		this.testService.save(te);
//	}
	
	@Test
	public void testCreateUUID(){
//		UUID uuid = UUID.randomUUID();
//		System.out.println(uuid.toString().replaceAll("-",""));
//		System.out.println(EncryptionUtil.md5Encry("hello world", null));
		
		
	}
	
	@Test
	public void testRequestLicense(){
		try {
			String lic = this.userService.licenseService("f9a8d668490cbe7c01490cc9cf470001");
			System.out.println(lic);
			SAXReader reader = new SAXReader();
			Document document = reader.read(new StringReader(lic));
			Element rootElement = document.getRootElement();
			Element userinfoElement = rootElement.element("userinfo");
			String username = userinfoElement.elementText("username");
			System.out.println(username);
			
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
*/