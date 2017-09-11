package com.supermap.egispservice.base;

import org.springframework.context.support.ClassPathXmlApplicationContext;



public class RunServer {

	private static final long BLOCK_TIME = 1000 * 60;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		context.start();
		//启动的时候，执行线程
		/*PointDao pointdao=(PointDao) context.getBean("pointDao");
		IGeocodingService geocodingService=(IGeocodingService) context.getBean("geocodingService");
		Timer timer = new Timer(); 
		MyPointAdmincodeTask task=new MyPointAdmincodeTask(pointdao,geocodingService);
		timer.schedule(task,1000);*/
		//
		while (true) {
			try {
				Thread.sleep(BLOCK_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
