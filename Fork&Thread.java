package com.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * 多线程和单线程的比较，开了四个线程，计算范围素数
 *  测试结果
 *  
 *  sum = 148933  time = 296ns
 *  -------华丽分割线--------
 *  sum = 148933  time = 720ns
 *  
 *  效果还是很明显的，两倍多一点吧，达不到四倍，毕竟维护队列要开销的嘛
 * */

public class PrimeSum {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		ExecutorService service = Executors.newFixedThreadPool(4);
		Future<Integer> f1 = service.submit(new PrimeTask(2, 500000));
		Future<Integer> f2 = service.submit(new PrimeTask(500000, 1000000));
		Future<Integer> f3 = service.submit(new PrimeTask(1000000, 1500000));
		Future<Integer> f4 = service.submit(new PrimeTask(1500000, 2000000));
		
		long start = System.currentTimeMillis();
		int sum = f1.get() + f2.get() + f3.get() + f4.get();
		long end = System.currentTimeMillis();
		service.shutdown();
		long time = end-start;
		System.out.println("sum = " + sum + "  time = " + time);
		
		System.out.println("-------华丽分割线--------");
		
		start = System.currentTimeMillis();
		int s = 0;
		for (int i = 2; i<2000000; i++) {
			if (Prime.isPrime(i)) {
				s++;
			}
		}
		end = System.currentTimeMillis();
		time = end-start;
		System.out.println("sum = " + s + "  time = " + time);
	}
}


class PrimeTask implements Callable<Integer> {

	int start = 0;
	int end = 0;
	
	public PrimeTask(int start, int end) {

		this.start = start;
		this.end = end;
	}
	
	@Override
	public Integer call() throws Exception {

		int sum = 0;
		for (int i = start; i<end; i++) {
			if (Prime.isPrime(i)) {
				sum++;
			}
		}
		
		return sum;
	}
	
}

class Prime {
	
	static boolean isPrime(int n) {
		
		/*
		 * 为了测试  可以将 i<=Math.sqrt(n); 这个条件改为 i<n 
		 * 慢了真的不是一个数量级上的
		 * 一个O(n^2)
		 * 一个O(nlgn)
		 * 然而素数筛法更快
		 * */
		for (int i = 2; i<=Math.sqrt(n); i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}
}


/*
占坑

用ForkJoin实现，代码忘在机房了，mdzz.

ForkJoinPool实现比newFixedThreadPool还要慢，为什么1.7还要加入ForkJoin呢

百思不得其解

2017年9月21日21:18:21
*/

/*
	今天看了文章好像ForkJoinPool不停创建子线程

	然后要不停的gc，所以要慢一点

	然后今天我用ForkJoinPool计算1到8000000

	准备测试下ThreadPool和ForkJoinPool的性能，然后我ForkJoinPool的条件是

	end-start < 10 妈的忘记改了

	这样就创建了80W个子线程，然后不出意外

	电脑直接挂了，按什么都没有反应，直接崩了
*/

















