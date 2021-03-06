package com.supermap.egispservice.reverse;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunServer {
	
	private static final long BLOCK_TIME = 1000 * 60;

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		context.start();
		while (true) {
			try {
				Thread.sleep(BLOCK_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
