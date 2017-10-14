/*
	生产与消费第一种实现方法

	wait + notifyall
*/

package com.test;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class PC {
	public static void main(String[] args) {
		
		MyContainer1 mc = new MyContainer1();
		Consume c = new Consume(mc);
		Produce p = new Produce(mc);
		
		for (int i = 1; i<=10; i++) {
			new Thread(c, "我是消费者线程" + i).start();
		}
		for (int i = 1; i<=2; i++) {
			new Thread(p, "我是生产者线程" + i).start();
		}
	}
}

class MyContainer1 {
	
	List<Integer> list = new ArrayList<>();
	
	synchronized void put(int val) {
		while (list.size() == 10) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		list.add(val);
		this.notifyAll();
	}
	
	synchronized int get(int index) {
		while (list.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int t = list.remove(index);
		this.notifyAll();
		return t;
	}
}

class Consume implements Runnable {

	MyContainer1 mc = null;
	
	public Consume(MyContainer1 mc) {
		
		this.mc = mc;
	}
	
	@Override
	public synchronized void run() {
		
		for (int i = 0; i<2; i++) {
			System.out.println(Thread.currentThread().getName() + "正在消费第  " + mc.get(0) + " 件物品");
		}
	}
}

class Produce implements Runnable {

	MyContainer1 mc = null;
	
	public Produce(MyContainer1 mc) {
		
		this.mc = mc;
	}
	
	@Override
	public synchronized void run() {
		for (int i = 0; i<10; i++) {
			Random r = new Random();
			int val = r.nextInt(100);
			mc.put(val);
			System.out.println(Thread.currentThread().getName() + "正在生产  " +  val + " 物品");
		}
	}
	
}


/*
	生产与消费第二种实现方式

	ReentrantLock + Condition
*/


package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PC2 {
	public static void main(String[] args) {
		
		MyContainer2 mc = new MyContainer2();
		Consume2 c = new Consume2(mc);
		Produce2 p = new Produce2(mc);
		
		for (int i = 1; i<=10; i++) {
			new Thread(c, "我是消费者线程" + i).start();
		}
		for (int i = 1; i<=2; i++) {
			new Thread(p, "我是生产者线程" + i).start();
		}
	
	}
}

class MyContainer2 {
	
	List<Integer> list = new ArrayList<>();
	ReentrantLock lock = new ReentrantLock();
	Condition p = lock.newCondition();
	Condition c = lock.newCondition();
	
	void put(int val) {
		lock.lock();
		while (list.size() == 10) {
			try {
				p.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		list.add(val);
		c.signalAll();
		lock.unlock();
	}
	
	int get(int index) {
		lock.lock();
		int t = 0;
		try {
			while (list.size() == 0) {
				c.await();
			} 
			t = list.remove(index);
			p.signalAll();
		} catch (InterruptedException e) {
		} finally {
			lock.unlock();
		}
		
		return t;
	}
}

class Consume2 implements Runnable {

	MyContainer2 mc = null;
	
	public Consume2(MyContainer2 mc) {
		
		this.mc = mc;
	}
	
	@Override
	public synchronized void run() {
		
		for (int i = 0; i<2; i++) {
			System.out.println(Thread.currentThread().getName() + "正在消费第  " + mc.get(0) + " 件物品");
		}
	}
}

class Produce2 implements Runnable {

	MyContainer2 mc = null;
	
	public Produce2(MyContainer2 mc) {
		
		this.mc = mc;
	}
	
	@Override
	public synchronized void run() {
		for (int i = 0; i<10; i++) {
			Random r = new Random();
			int val = r.nextInt(100);
			mc.put(val);
			System.out.println(Thread.currentThread().getName() + "正在生产  " +  val + " 物品");
		}
	}
	
}



/*
	生产与消费第三种实现方式

	BlockingQueue 非阻塞队列实现

	最简单方便，本身就是线程安全类
*/



package com.test;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PC3 {
	public static void main(String[] args) {
		
		QueueContainer qc = new QueueContainer();
		Consumer3 c = new Consumer3(qc);
		Produce3 p = new Produce3(qc);
		
		for (int i = 1; i<=10; i++) {
			new Thread(c, "我是消费者线程" + i).start();
		}
		for (int i = 1; i<=2; i++) {
			new Thread(p, "我是生产者线程" + i).start();
		}
	}
}

class QueueContainer {
	
	BlockingQueue<Integer> bq = new LinkedBlockingQueue<Integer>();
	
	void put(int val) throws InterruptedException {
		bq.put(val);
	}
	
	int get() throws InterruptedException {
		return bq.take();
	}
}

class Consumer3 implements Runnable {

	QueueContainer qc = null;
	
	public Consumer3(QueueContainer qc) {
		
		this.qc = qc;
	}
	
	@Override
	public synchronized void run() {

		for (int i = 0; i<2; i++) {
			int t = 0;
			try {
				t = qc.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "正在消费第  " + t + " 件物品");
		}
	}
}

class Produce3 implements Runnable {

QueueContainer qc = null;
	
	public Produce3(QueueContainer qc) {
		
		this.qc = qc;
	}
	
	@Override
	public synchronized void run() {

		for (int i = 0; i<10; i++) {
			Random r = new Random();
			int val = r.nextInt(100);
			try {
				qc.put(val);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "正在生产  " +  val + " 物品");
		}
	}
}






























































