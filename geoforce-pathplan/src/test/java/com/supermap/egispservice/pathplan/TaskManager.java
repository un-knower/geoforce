package com.supermap.egispservice.pathplan;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

/***
 * 
 * 
 * Title 异步的方式执行任务<br>
 * Description <br>
 * 
 * @Company SuperMap Software Co., Ltd.<br>
 * @Copyright Copyright (c) 2013<br>
 * 
 * @version 1.0.0,2014-7-17
 * @since JDK1.6+
 * @author caozhongping
 * 
 */
public class TaskManager {

	public static final LinkedBlockingQueue<MyTask> tasks = new LinkedBlockingQueue<TaskManager.MyTask>();

	public static final ConcurrentHashMap<Long, Object> finished = new ConcurrentHashMap<Long, Object>();

	public static volatile boolean isRunnnig = false;

	private static ExecutorService service;

	/**
	 * 添加任务
	 * 
	 * @param task
	 * @return
	 */
	public static void submit(TaskManager.MyTask task) {
		tasks.add(task);
		start();
	}

	private static void start() {
		if (isRunnnig)
			return;
		isRunnnig = true;
		int threadCount = Runtime.getRuntime().availableProcessors() + 5;
		service = Executors.newFixedThreadPool(threadCount);
		for (int i = 0; i < threadCount; i++) {
			service.execute(new Runnable() {

				@Override
				public void run() {
					while (isRunnnig) {
						try {
							MyTask runnable = tasks.take();
							runnable.setStatus(Status.RUNNING);
							Object obj;
							try {
								obj = runnable.run();
								finished.put(runnable.getId(), obj);
							} catch (Exception e) {
								System.out.println(e);
							}
							runnable.setStatus(Status.FINISHED);
							runnable.getThread().interrupt();
						} catch (InterruptedException e) {
							System.out.println("TaskManager:" + e);
							Thread.interrupted();
						}
					}
				}
			});
		}

	}

	/**
	 * 注销任务线程池
	 */
	public static void stop() {
		isRunnnig = false;
		if (service != null)
			service.shutdown();
		tasks.clear();
		finished.clear();
	}

	/**
	 * 根据指定的超时时间获取结果
	 * 
	 * @param MyTask
	 * @return
	 * @throws TimeoutException
	 */
	public static Object getResult(MyTask task) throws TimeoutException {

		if (finished.get(task.getId()) == null) {
			try {
				task.setThread(Thread.currentThread());
				Thread.sleep(task.getTimeout() * 1000);
				if (task.getStatus() != Status.FINISHED)
					throw new TimeoutException();
				else
					return finished.remove(task.getId());
			} catch (InterruptedException e) {
				Thread.interrupted();
				return finished.remove(task.getId());
			}
		} else {
			return finished.remove(task.getId());
		}
	}

	public static enum Status {
		FINISHED, RUNNING
	}

	public static interface MyTask {

		long getId();

		Status getStatus();

		void setStatus(Status status);

		Object run();

		Thread getThread();

		void setThread(Thread currentThread);

		int getTimeout();
	}
	
	public static abstract class DefaultTask implements MyTask{

		protected volatile Thread thread;
		protected volatile Status status;
		protected volatile long id;
		
		public DefaultTask() {
			id = System.currentTimeMillis();
		}
		
		@Override
		public final long getId() {
			return id;
		}

		@Override
		public Status getStatus() {
			return status;
		}

		@Override
		public void setStatus(Status status) {
			this.status = status;
		}

		@Override
		public Thread getThread() {
			return thread;
		}

		@Override
		public void setThread(Thread currentThread) {
			this.thread = currentThread;
		}

		@Override
		public int getTimeout() {
			return 10;
		}
		
	}
}
