package test;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 这是我见过最牛逼的hashmap的排序，没有之一
 * 今天在刷题看到的，记录下来，以免以后会用的到
 * 
 * 首先，如果题目有要求相同的value要按照进入的顺序输出，那么就用LinkedHashMap，否则HashMap即可
 * 
 * 排序过程：这也是最牛逼的地方
 * 建立一个list集合，（LinkedList测试的好像效率比较高？）
 * 泛型为Map.Entry<String, Integer>,然后构造方法把Map.Entry<String, Integer>传进去
 * 然后调用Collections.sort方法，重写compare方法（机房的电脑是1.7，1.8可用lambada表达式）
 * (o1, o2) -> o1.getValue().compareTo(o2.getValue()) myeclipse10好像配了1.8也用不了 玄学
 * 具体看代码一下子就懂了
 * 
 * 这真的是tm的叼，我以前是用list数组来排序的，总感觉浪费空间，这个是真牛逼，相见恨晚。
 * 学到老活到老
 * 
 * @author wenbochang
 * @date 2017/11/19
 */

public class Main {
	public static void main(String[] args) {
		
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("z", 1);
		map.put("d", 1);
		map.put("b", 6);
		map.put("c", 1);
		map.put("a", 1);
		map.put("c", 2);
		map.put("a", 3);
		map.put("f", 1);
		for (Map.Entry<String, Integer> m : map.entrySet()) {
			System.out.println(m.getKey() + "  " + m.getValue());
		}
        
		System.out.println("---------------");
		
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String,Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		for (Map.Entry<String, Integer> m : list) {
			System.out.println(m.getKey() + "  " + m.getValue());
		}
	}
}






