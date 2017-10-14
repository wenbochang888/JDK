# HashMap源代码理解

看了几天的HashMap源代码，感觉对HashMap有了更加深一步的了解，觉得HashMap底层实现起来
真的是太牛了。怕以后忘记了，顺便就记录一下。

# HashMap的JDK

我一般学习一个类都是先从官方的JDK看起的。

可以看出HashMap继承了AbstractMap的抽象类并且实现了Map的接口。
该AbstractMap抽象类实现了大多数方法，包括put，get等方法

他有四个构造方法，用的最多的还是无参的构造方法。Map<String, String> m = new HashMap<String, String>(); 构造一个具有默认初始容量 (16) 和默认加载因子 (0.75) 的空 HashMap。

![Image text](https://github.com/wenbochang888/DiaryRecord/blob/master/img/HashMapJdk.png)



# HashMap的数据结构

HashMap的底层主要是基于数组和链表来实现的，
它之所以有相当快的查询速度主要是因为它是通过计算散列码来决定存储的位置。
HashMap中主要是通过key的hashCode来计算hash值的，只要hashCode相同，计算出来的hash值就一样。
如果存储的对象对多了，就有可能不同的对象所算出来的hash值是相同的，这就出现了所谓的hash冲突。
学过数据结构的同学都知道，解决hash冲突的方法有很多，HashMap底层是通过链表来解决hash冲突的。

简单而言HashMap就是一个数组+链表。
存取几乎O(1)的原因是：每一个key对应一个hashCode(类似于一个地址,或者数组的下表)，存取自然而然就是O(1)了。但内存中并没有那么大的空间，怎么办呢。这就产生了Hash冲突。
最简单的解决办法就是在哈希或者链地址法。

![Image text](https://github.com/wenbochang888/DiaryRecord/blob/master/img/HashMapStruct.jpg)

# HashMap的Entry类

我最开始看的时候并不理解Entry这个内部类的含义，看多了几遍，貌似理解了。

transient Entry[] table;//存储元素的实体数组。Entry是一个链表

<pre><code>
/** Entry是单向链表。    
     * 它是 “HashMap链式存储法”对应的链表。    
     *它实现了Map.Entry 接口，即实现getKey(), getValue(), setValue(V value), equals(Object o), hashCode()这些函数  
    **/  
    static class Entry<K,V> implements Map.Entry<K,V> {    
        final K key;    
        V value;    
        // 指向下一个节点    
        Entry<K,V> next;    
        final int hash;    
   
        // 构造函数。    
        // 输入参数包括"哈希值(h)", "键(k)", "值(v)", "下一节点(n)"    
        Entry(int h, K k, V v, Entry<K,V> n) {    
            value = v;    
            next = n;    
            key = k;    
            hash = h;    
        }    
   
        public final K getKey() {    
            return key;    
        }    
   
        public final V getValue() {    
            return value;    
        }    
   
        public final V setValue(V newValue) {    
            V oldValue = value;    
            value = newValue;    
            return oldValue;    
        }    
   
        // 判断两个Entry是否相等    
        // 若两个Entry的“key”和“value”都相等，则返回true。    
        // 否则，返回false    
        public final boolean equals(Object o) {    
            if (!(o instanceof Map.Entry))    
                return false;    
            Map.Entry e = (Map.Entry)o;    
            Object k1 = getKey();    
            Object k2 = e.getKey();    
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {    
                Object v1 = getValue();    
                Object v2 = e.getValue();    
                if (v1 == v2 || (v1 != null && v1.equals(v2)))    
                    return true;    
            }    
            return false;    
        }    
   
        // 实现hashCode()    
        public final int hashCode() {    
            return (key==null   ? 0 : key.hashCode()) ^    
                   (value==null ? 0 : value.hashCode());    
        }    
   
        public final String toString() {    
            return getKey() + "=" + getValue();    
        }    
   
        // 当向HashMap中添加元素时，绘调用recordAccess()。    
        // 这里不做任何处理    
        void recordAccess(HashMap<K,V> m) {    
        }    
   
        // 当从HashMap中删除元素时，绘调用recordRemoval()。    
        // 这里不做任何处理    
        void recordRemoval(HashMap<K,V> m) {    
        }    
    }
</code></pre>

用Entry定义了一个数组，而Entry本身就是一个链表，这样底层就 已经实现了。

# HashMap的构造方法

<pre><code>
	public HashMap(int initialCapacity, float loadFactor) {
        //确保数字合法
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                              initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                              loadFactor);

        // Find a power of 2 >= initialCapacity
        int capacity = 1;   //初始容量
        while (capacity < initialCapacity)   //确保容量为2的n次幂，使capacity为大于initialCapacity的最小的2的n次幂
            capacity <<= 1;

        this.loadFactor = loadFactor;
        threshold = (int)(capacity * loadFactor);
        table = new Entry[capacity];
       init();
   }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
   }

    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
       init();
    }
</code></pre>

最重要理解为什么初始容量一定为2的n次幂

# 神奇的O(1) put方法

<pre><code>
	public V put(K key, V value) {
     // 若“key为null”，则将该键值对添加到table[0]中。
         if (key == null) 
            return putForNullKey(value);
     // 若“key不为null”，则计算该key的哈希值，然后将其添加到该哈希值对应的链表中。
         int hash = hash(key.hashCode());
     //搜索指定hash值在对应table中的索引
         int i = indexFor(hash, table.length);
     // 循环遍历Entry数组,若“该key”对应的键值对已经存在，则用新的value取代旧的value。然后退出！
         for (Entry<K,V> e = table[i]; e != null; e = e.next) { 
             Object k;
              if (e.hash == hash && ((k = e.key) == key || key.equals(k))) { //如果key相同则覆盖并返回旧值
                  V oldValue = e.value;
                 e.value = value;
                 e.recordAccess(this);
                 return oldValue;
              }
         }
     //修改次数+1
         modCount++;
     //将key-value添加到table[i]处
     addEntry(hash, key, value, i);
     return null;
}
</code></pre>

首先由key值计算出来hash码; int hash = hash(key.hashCode()); 
hashCode调用的是JDK的，hash函数实现如下。
貌似可以保证冲突减少到最小。

<pre><code>
	//计算hash值的方法 通过键的hashCode来计算
    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
</code></pre>

计算出来hash值以后，在通过indexFor这个函数去找数组中寻找对应的下标。
其实也就是对数组的长度取余，而用&就快多了，这也就是为什么容量要为2的幂次方。

<pre><code>
	static int indexFor(int h, int length) { //根据hash值和数组长度算出索引值
        return h & (length-1);  //这里不能随便算取，用hash&(length-1)是有原因的，这样可以确保算出来的索引是在数组大小范围内，不会超出
            }
</code></pre>

然后就判断那个位置是否为null，为null就放进去。复杂度为O(1)，如果不为空那就是解决冲突了。
如果放进去的相同，则新的覆盖老的，如果不相同，则扫描链表。

复杂度理想情况下大致为O(1)

# 结束 2017年8月12日21:19:24

get方法类似就不一一重复了，重点看put方法的实现以及为什么要这样做。

突然感觉HashMap可以实现任何的数据结构了，真的超级强大




