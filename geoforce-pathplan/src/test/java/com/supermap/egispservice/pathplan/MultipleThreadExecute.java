package com.supermap.egispservice.pathplan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * 
 * Title 多线程执行框架<br>
 * Description 多线程无锁执行框架,根据指定的线程并行执行任务 <br>
 * 
 * @version 1.0.0,2014-7-2
 * @since JDK1.6+
 * @author caozhongping
 * 
 */
public class MultipleThreadExecute {

	public static void main(String[] args) {
		List<ExecuteTask> tasks = new ArrayList<ExecuteTask>();
		int cout = 100000000;
		int thCt = 10;
		int each = cout / thCt;
		for (int i = 0; i < thCt; i++) {
			int start = i * each;
			int end = (i + 1) * each;
			tasks.add(new Test(start, end));
		}
		List<Object> res = MultipleThreadExecute.execute(thCt, tasks);
		long sum = 0;
		for (Object object : res) {
			sum = sum+((Long)object);
		}
		System.out.println(sum);
	}

	public static class Test implements ExecuteTask {

		private int start;
		private int end;

		public Test(int i, int j) {
			this.start = i;
			this.end = j;
		}

		@Override
		public Object run() {
			long sum = 0;
			for (int i = start; i < end; i++) {
				sum+=i;
			}
			return sum;
		}

		@Override
		public boolean onError(int taskIndex, Throwable error) {
			return false;
		}

	}

	public interface ExecuteTask {

		Object run();

		/**
		 * 处理错误,如果返回FALSE将终止后续任务
		 * 
		 * @param taskIndex
		 * @param error
		 * @return
		 */
		boolean onError(int taskIndex, Throwable error);
	}

	/**
	 * 多线程执行批量任务
	 * 
	 * @param threadCount
	 *            :线程总数
	 * @param tasks
	 *            :所有的任务
	 * @return
	 */
	public static List<Object> execute(int threadCount, List<ExecuteTask> tasks) {

		List<Object> result = new ArrayList<Object>();
		ExecutorService service = Executors.newFixedThreadPool(threadCount);
		List<Future<List<Object>>> futures = new ArrayList<Future<List<Object>>>(
				threadCount);

		for (int i = 0; i < threadCount; i++) {
			futures.add(service.submit(new Run(i, threadCount, tasks)));
		}

		for (Future<List<Object>> future : futures) {
			try {
				result.addAll(future.get());
			} catch (Exception e) {
				throw new RuntimeException("Get result error:", e);
			}
		}
		service.shutdown();
        System.out.println("result:"+result.size());
		return result;
	}

	private static class Run implements Callable<List<Object>> {

		private int threadNumber;
		private int theadCount;
		private List<ExecuteTask> tasks;

		public Run(int threadNumber, int theadCount, List<ExecuteTask> tasks) {
			this.threadNumber = threadNumber;
			this.theadCount = theadCount;
			this.tasks = tasks;
		}

		@Override
		public List<Object> call() throws Exception {
			boolean run = true;
			int i = threadNumber;
			int taskCont = tasks.size();
			List<Object> result = new ArrayList<Object>(taskCont / theadCount);

			while (run && i < taskCont) {
				ExecuteTask task = tasks.get(i);
				try {
					result.add(task.run());
					i += theadCount;
				} catch (Exception e) {
					run = task.onError(i, e);
				}
			}
			return result;
		}

	}
}
