/*
	卖票系统
*/

package com.test;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Tickes {
	public static void main(String[] args) {
		
		MyTickes3 mt = new MyTickes3();
		Thread t1 = new Thread(mt);
		Thread t2 = new Thread(mt);
		t1.start();
		t2.start();
	}
}

/*
 * 使用普通的synchronized实现 
 * */

class MyTickes1 implements Runnable {

	int tickes = 100;
	
	@Override
	public void run() {
		
		while (tickes > 0) {
			synchronized (this) {
				if (tickes > 0) {
					System.out.println(Thread.currentThread().getName() + "正在卖出第  "  + tickes-- + "张票");
				}
			}
		}
	}
}


/*
 * 使用reentrantlock实现
 * */

class MyTickes2 implements Runnable {

	int tickes = 100;
	ReentrantLock lock = new ReentrantLock(true);
	
	@Override
	public void run() {
		
		while (tickes > 0) {
			lock.lock();
			System.out.println(Thread.currentThread().getName() + "正在卖出第  "  + tickes-- + "张票");
			lock.unlock();
		}
	}
}

/*
	使用ConcurrentLinkedQueue实现

	效率最高
*/

class MyTickes3 implements Runnable {

	int tickes = 100;
	Queue<String> queue = new ConcurrentLinkedQueue<>();
	
	{
		for (int i = 1; i<=tickes; i++) {
			queue.add("" + i);
		}
	}
	
	@Override
	public void run() {
		
		/*
		 * 加了synchronized 取票和out是同步的， 输出是顺序的
		 * 
		 * 不加，输出虽然不是顺序的，但并没有出错，效率会更加高
		 * */
		
		while (tickes > 0) {
			synchronized (this) {
				String s = queue.poll();
				if (s == null) {
					break;
				} else {
					System.out.println(Thread.currentThread().getName() + "正在卖出第  "  + s + "张票");
				}
			}
		}
	}
	
}