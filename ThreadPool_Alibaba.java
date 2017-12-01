package com.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
   @author Chang-pc
   @date 2017/12/01
   
      缘由：Alibaba的命名规范里面的一段话，今晚刚刚看到
           【强制】线程池不允许使用 Executors 去创建，而是通过
            ThreadPoolExecutor的方式，这样
		    的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
	     	说明： Executors 返回的线程池对象的弊端如下：
		    1） FixedThreadPool 和 SingleThreadPool :
		    允许的请求队列长度为 Integer.MAX_VALUE ，可能会堆积大量的请求，从而导致 OOM 。
		    2） CachedThreadPool 和 ScheduledThreadPool :
		    允许的创建线程数量为 Integer.MAX_VALUE ，可能会创建大量的线程，从而导致 OOM 。
		
   使用了com.google.guava jar包里面创建线程的模板，最后两个使用构造函数的默认值
   public ThreadPoolExecutor(int corePoolSize,
          int maximumPoolSize,
          long keepAliveTime,
          TimeUnit unit,
          BlockingQueue<Runnable> workQueue,
          ThreadFactory threadFactory,
          RejectedExecutionHandler handler) 
 */

/**
	结果：
		1512139714670:Thread name:pool-1-thread-2
		1512139714670:Thread name:pool-1-thread-1
		1512139714670:Thread name:pool-1-thread-4
		1512139714670:Thread name:pool-1-thread-3
		1512139714670:Thread name:pool-1-thread-5
		java.util.concurrent.ThreadPoolExecutor@55f96302[Running, pool size = 5, active threads = 5, queued tasks = 5, completed tasks = 0]
		1512139715670:Thread name:pool-1-thread-3
		1512139715670:Thread name:pool-1-thread-5
		1512139715671:Thread name:pool-1-thread-2
		1512139715671:Thread name:pool-1-thread-1
		1512139715671:Thread name:pool-1-thread-4
	可以看出来线程被复用了，并且超过五个就放进了等待队列里面去了。
*/
public class Main {
	public static void main(String[] args) {
		
		ExecutorService executiorService = 
				new ThreadPoolExecutor(
				5, 10, 0L, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<>(1024));
		for (int i = 0; i < 10; i++) {
			executiorService.execute(new MyTask());
		}
		System.out.println(executiorService);
		executiorService.shutdown();
	}
}

class MyTask implements Runnable {

	@Override
	public void run() {
		System.out.println(System.currentTimeMillis() + ":Thread name:"  
                + Thread.currentThread().getName());  
        try {  
            Thread.sleep(1000);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
	}
}


