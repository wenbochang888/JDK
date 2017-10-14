# 线程池

/**
* 线程池计算1到20亿的和
* 单线程大概500ms
* 多线程250ms左右
*/

<pre><code>
	
package com.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Sum {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println(Runtime.getRuntime().availableProcessors());
		long sum = 0;
		long start = System.currentTimeMillis();
		for (long i = 0; i<2000000000; i++) {
			sum += i;
		}
		long end = System.currentTimeMillis();
		System.out.println(sum);
		System.out.println(end - start);
		System.out.println("-------华丽分割线--------");
		
		ExecutorService service = Executors.newFixedThreadPool(4);
		Future f1 = service.submit(new MyTask(1, 500000000));
		Future f2 = service.submit(new MyTask(500000000, 1000000000));
		Future f3 = service.submit(new MyTask(1000000000, 1500000000));
		Future f4 = service.submit(new MyTask(1500000000, 2000000000));
		sum = 0;
		start = System.currentTimeMillis();
		sum = f1.get() + f2.get() + f3.get() + f4.get();
		end = System.currentTimeMillis();
		System.out.println(sum);
		System.out.println(end - start);
	}
}


class MyTask implements Callable {

	long start = 0;
	long end = 0;
	
	public MyTask(int start, int end) {
		// TODO Auto-generated constructor stub
		this.start = start;
		this.end = end;
	}
	
	@Override
	public Long call() throws Exception {
		// TODO Auto-generated method stub
		long sum = 0;
		for (long i = start; i < end; i++) {
			sum + = a[i];
		}
		return sum;
	}
}

</code></pre>

# 占坑

用ForkJoin实现，代码忘在机房了，mdzz.

ForkJoinPool实现比newFixedThreadPool还要慢，为什么1.7还要加入ForkJoin呢

百思不得其解

2017年9月21日21:18:21