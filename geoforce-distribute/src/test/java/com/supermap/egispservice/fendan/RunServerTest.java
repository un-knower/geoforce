package com.supermap.egispservice.fendan;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class RunServerTest {

	

	@Test
	public void testMain() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
        context.start();
        try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
