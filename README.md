# jdkdemo

jdk demo

## HashMap

- `hash()`
    - 调用 key 的 hashCode 方法 获得 h ， 然后计算  `h^h>>>16` 作为 hash 值
- `putVal()`
- `resize()`
- 数组长度：初始值 16 ， **2 的 n 次方**（为什么呢？ 便于在计算元素下标的时候更好的进行 `&` 运算，速度更快）
- `final float loadFactor`; 负载因子
- `int threshold`; 阈值 = 数组长度 * 负载因子

### 数组长度为什么要是 2 的 n 次方呢？

因为在计算元素在数组中的位置的时候是进行 `&` 运算的，这样可以提高运算的效率，因为 `&` 运算比较快。

```
i = (n - 1) & hash
```

**在创建 HashMap 的时候指定长度不是 2 的 n 次方，那么创建数组的长度是多少呢？**

创建的数组的大小还会是 2 的 n 次方。 注意：创建数组是在 resize() 方法里面进行的.

具体可以查看源码：java.util.HashMap.HashMap(int, float)

java.util.HashMap.tableSizeFor
```
  static final int tableSizeFor(int cap) {
      int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
      return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
  }
```

### 链表什么时候会转成红黑树呢？ 

满足下面 2 个条件：

1. 链表的数据个数超过 8 个
2. 数组的长度超过 64 

### 扩容

元素个数超过阈值。阈值 = 数组长度 * 负载因子

**链表是如何扩容的？**

首先要知道，链表扩容后，其下边只会在两个地方。
1. 原下标
2. 原下标 + 原数组长度

为什么会出现这个现象呢？
因为数组长度是 2 的 n 次方，下标的计算规则是 `i = (n - 1) & hash`

假设 hashcode 是 0101 0101 ，数组长度是 16 

```
   0101 0101    hashcode
&  0000 1111    数组长度
=  0000 0101
= 5
```
扩容后数组长度变为 32
```
   0101 0101    hashcode
&  0001 1111    数组长度
=  0001 0101
=  21  = 5 + 16 
```
因此，链表扩容后，只会在原下标和原下标 + 原数组长度的地方。

在拆分链表的时候，可以将链表拆分 2 个链表。

loHead、loTail： 低位链表  
hiHead、hiTail： 高位链表  

```
Node<K,V> loHead = null, loTail = null;
Node<K,V> hiHead = null, hiTail = null;
Node<K,V> next;
do {
    next = e.next;
    if ((e.hash & oldCap) == 0) {
        if (loTail == null)
            loHead = e;
        else
            loTail.next = e;
        loTail = e;
    }
    else {
        if (hiTail == null)
            hiHead = e;
        else
            hiTail.next = e;
        hiTail = e;
    }
} while ((e = next) != null);
if (loTail != null) {
    loTail.next = null;
    newTab[j] = loHead;  // 将低位链表放到原下标
}
if (hiTail != null) {
    hiTail.next = null;
    newTab[j + oldCap] = hiHead; // 将高位链表放到 【原下标 + 原数组长度】
}
```

**红黑树是如何扩容的**

红黑树扩容的时候要考虑的问题：扩容之后的红黑树是红黑树？链表？ 

链表的个数小于等于 6 时会退化成链表。`untreeify()` 方法。



