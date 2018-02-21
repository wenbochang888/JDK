# HashMap源代码理解

看了几天的HashMap源代码，感觉对HashMap有了更加深一步的了解，觉得HashMap底层实现起来
真的是太牛了。怕以后忘记了，顺便就记录一下。

## 2018年2月21日 15：38分

新年闲着无聊，重温了下hashmap，这次看的是JDK1.8版本，发觉和1.6，1.7有很大的区别，这次就索性直接改了。

# HashMap的JDK（JDK1.8）

我一般学习一个类都是先从官方的JDK看起的。

可以看出HashMap继承了AbstractMap的抽象类并且实现了Map的接口。
该AbstractMap抽象类实现了大多数方法，包括put，get等方法

他有四个构造方法，用的最多的还是无参的构造方法。Map<String, String> m = new HashMap<String, String>(); 构造一个具有默认初始容量 (16) 和默认加载因子 (0.75) 的空 HashMap。

![Image text](https://github.com/wenbochang888/JDK/blob/master/img/HashMapJdk.png)



# HashMap的数据结构

HashMap的底层主要是基于数组和链表来实现的，
它之所以有相当快的查询速度主要是因为它是通过计算散列码来决定存储的位置。
HashMap中主要是通过key的hashCode来计算hash值的，只要hashCode相同，计算出来的hash值就一样。
如果存储的对象对多了，就有可能不同的对象所算出来的hash值是相同的，这就出现了所谓的hash冲突。
学过数据结构的同学都知道，解决hash冲突的方法有很多，HashMap底层是通过链表来解决hash冲突的。

简单而言HashMap就是一个数组 + 链表，或者数组 + 链表 + 红黑树。
存取几乎O(1)的原因是：每一个key对应一个hashCode(类似于一个地址,或者数组的下表)，存取自然而然就是O(1)了。但内存中并没有那么大的空间，怎么办呢。这就产生了Hash冲突。
最简单的解决办法就是在哈希或者链地址法。

![Image text](https://github.com/wenbochang888/JDK/blob/master/img/HashMapStruct8.png)

# HashMap的构造方法

    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

首先，其他的构造方法都是调用这个构造方法的。
其他最重要理解为什么初始容量一定为2的n次幂

# 神奇的O(1) put方法

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    //如果 table 还未被初始化，那么初始化它
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    //根据键的 hash 值找到该键对应到数组中存储的索引
    //如果为 null，那么说明此索引位置并没有被占用
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    //不为 null，说明此处已经被占用，只需要将构建一个节点插入到这个链表的尾部即可
    else {
        Node<K,V> e; K k;
        //当前结点和将要插入的结点的 hash 和 key 相同，说明这是一次修改操作
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        //如果 p 这个头结点是红黑树结点的话，以红黑树的插入形式进行插入
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        //遍历此条链表，将构建一个节点插入到该链表的尾部
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    //如果插入后链表长度大于等于 8 ，将链表裂变成红黑树
                    if (binCount >= TREEIFY_THRESHOLD - 1)
                        treeifyBin(tab, hash);
                    break;
                }
                //遍历的过程中，如果发现与某个结点的 hash和key，这依然是一次修改操作 
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        //e 不是 null，说明当前的 put 操作是一次修改操作并且e指向的就是需要被修改的结点
        if (e != null) { 
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    //如果添加后，数组容量达到阈值，进行扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;

首先初始化是在第一次put的时候进行的，不是在构造方法中进行d的。
其他1.8以后多线程的put不会出现cpu飙升至100%，只会出现丢失数据罢了。
声明两对指针，维护两个连链表
依次在末端添加新的元素。（在多线程操作的情况下，无非是第二个线程重复第一个线程一模一样的操作）

首先由key值计算出来hash码; 
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
hashCode调用的是JDK的，hash函数实现如下。
貌似可以保证冲突减少到最小。

计算出来hash值以后，在通过indexFor这个函数去找数组中寻找对应的下标。
其实也就是对数组的长度取余，而用&就快多了，这也就是为什么容量要为2的幂次方。

    if ((p = tab[i = (n - 1) & hash]) == null)

然后就判断那个位置是否为null，为null就放进去。复杂度为O(1)，如果不为空那就是解决冲突了。
如果放进去的相同，则新的覆盖老的，如果不相同，则扫描链表。

复杂度理想情况下大致为O(1)

# 结束 2017年8月12日21:19:24

get方法类似就不一一重复了，重点看put方法的实现以及为什么要这样做。

突然感觉HashMap可以实现任何的数据结构了，真的超级强大


# 结束 2018年2月21日15:52:12

总发觉自己的表达能力不是很强。想说的话总是说不出来，总结一下。
JDK1.8相比于1.6，1.7的变化
1：数组 + 链表 + 红黑树（ >8 ）
2: put方法在多线程不会死锁，只会丢失方法。
3：好像没了，就这样吧。

