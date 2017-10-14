# ArrayList  Add方法分析

public void add(int index,E element)

将指定的元素插入此列表中的指定位置。向右移动当前位于该位置的元素（如果有）以及所有后续元素（将其索引加 1）。

![Image text](https://github.com/wenbochang888/JDK/blob/master/img/ArrayListAdd.png)

# 1. public void add(int index, E element)

<pre><code>
	public void add(int index, E element) {
    // 判断index是否越界  
    rangeCheckForAdd(index);
     // 扩容
    ensureCapacityInternal(size + 1);  
     // 将elementData从index位置开始，复制到elementData的index+1开始的连续空间
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);
     // 在elementData的index位置赋值element
    elementData[index] = element;
     // ArrayList的大小加一  
    size++;
	}
</code></pre>

第12行rangeCheckForAdd(index) 测试是否超出数组长度

# 2. private void rangeCheck(int index)
<pre><code>
	private void rangeCheck(int index) {
     // 如果下标超过ArrayList的数组长度
    if (index >= size)
         // 抛出IndexOutOfBoundsException异常
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
	}
</code></pre>

第14行就是扩容了ensureCapacityInternal

# 3. private void ensureCapacityInternal(int minCapacity)
<pre><code>
	private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
         // 获取默认的容量和传入参数的较大值
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    ensureExplicitCapacity(minCapacity);
	}

	private void ensureExplicitCapacity(int minCapacity) {
    modCount++;
    // 如果最小需要空间比elementData的内存空间要大，则需要扩容
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
	}
</code></pre>

第51行，主要是调用grow这个方法。

# 4. private void grow(int minCapacity)

<pre><code>
// MAX_VALUE为231-1，MAX_ARRAY_SIZE 就是获取Java中int的最大限制，以防止越界  
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


private void grow(int minCapacity) {
    // 获取到ArrayList中elementData数组的内存空间长度
    int oldCapacity = elementData.length;
    // 扩容至原来的1.5倍
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    // 再判断一下新数组的容量够不够，够了就直接使用这个长度创建新数组， 
    // 不够就将数组长度设置为需要的长度
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    // 判断有没超过最大限制
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // 调用Arrays.copyOf方法将elementData数组指向新的内存空间时newCapacity的连续空间
    // 并将elementData的数据复制到新的内存空间
    elementData = Arrays.copyOf(elementData, newCapacity);
	}
</code></pre>

可知最终还是调用了Arrays.copyOf这个方法。

扩容完成以后，将System.arraycopy(elementData, index, elementData, index + 1, size - index);
即将index以后复制到index+1的位置上。然后插入，size++。

ps. 注意第66行，扩容是扩容到原来的1.5倍，oldCapacity + (oldCapacity >> 1);

这样add方法就完成了。

一个add方法还是包含了很多的小知识点呢，要好好学习分析分析。

学习的步伐永不停息。





