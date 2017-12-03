package com.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * java1.8新特性
 * map加了一些常用的函数，使用起来更加简洁
 * 加入了令人期待的lambda表达式
 * 这是一个java1.8新特性的一个小实例
 * 
 * 至于stream感觉用处不大，并且速度是慢的很呀
 * 等以后有需求再去学吧
 * 
 * Lambda给我的感觉是很简洁方便，省去了很多不必要的代码
 * 但我在写的过程中发现有的时候一行要写很多东西。
 * 以至于要换行，这点是我非常不喜欢的地方，
 * 臃肿，一大串让其他人看起来很不舒服。
 * 
 * @date 2017/12/3
 * @author Chang-pc
 */
public class Main {
    public static void main(String[] args) {
    	
    	Map<Character, Integer> map = new HashMap<>();
    	String s = "abdsandkjnwndkajndak";
    	for (int i = 0; i < s.length(); i++) {
    		map.put(s.charAt(i), map.getOrDefault(s.charAt(i), 0) + 1);
    	}
    	map.forEach((x, y) -> System.out.println(x + "  " + y));
    	System.out.println("-------val排序后--------");
    	List<Map.Entry<Character, Integer>> list = 
    		new ArrayList<>(map.entrySet());
    	Collections.sort(list, (x, y) -> x.getValue().compareTo(y.getValue()));
    	list.forEach((x) -> System.out.println
    		(x.getKey() + "  " + x.getValue()));
    }
}













