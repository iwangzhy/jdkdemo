package com.wangzhy.aqs.clh;

import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;

/**
 * java 实现 clh 队列锁
 * <p>
 * 获取锁的过程： 当一个线程需要获取锁时，会场景一个 QNode，将其中的 locked 设置为 true ，表示需要获取锁。 tail.getAndSet(node)
 * 。将自己放在队尾，并返回原来的尾巴。 自旋等待上一个节点释放锁。
 * <p>
 * 释放锁： 将当前节点的 locked 设置为  false 表示释放锁。
 *
 * @author wangzhy
 * @date 2024年04月16日
 */
@Slf4j
public class CLHLock implements Lock {

  // 尾巴，是所有线程共有的一个。所有线程进来后，把自己设置为tail
  // 使用 AtomicReference 是为了确保 getAndSet 是原子性的。
  private final AtomicReference<QNode> tail;

  /**
   * 前驱节点，每个线程独有一个。
   * <p>
   * 使用 myPred 是为了防止死锁。
   * ![](https://raw.githubusercontent.com/iwangzhy/picgo/master/20240416145445.png)
   */
  private final ThreadLocal<QNode> myPred;
  private final ThreadLocal<QNode> myNode;

  public CLHLock() {
    // 初始化 tail
    this.tail = new AtomicReference<>(new QNode());
    // 初始化 myNode
    this.myNode = ThreadLocal.withInitial(QNode::new);
    // 初始化 myPred
    this.myPred = new ThreadLocal<>();
  }

  @Override
  public void lock() {
    // 获取当前线程代表的节点
    QNode node = myNode.get();
    // 将自己的状态设置为 true 表示获取锁
    node.locked = true;
    // 将自己放在队列的尾巴，并且返回以前的值。第一次进将获取构造函数中的那个new QNode
    QNode pred = tail.getAndSet(node);
    // 把旧的节点放入前驱节点。
    myPred.set(pred);
    // 在等待前驱节点的 locked 域变为 false，这是一个自旋等待的过程
    while (pred.locked) {
    }
    // 打印myNode、myPred的信息
    peekNodeInfo();
  }

  private void peekNodeInfo() {
    log.info("{} myNode: {} myPred: {}",
        Thread.currentThread().getName(),
        myNode.get(),
        myPred.get()
    );
  }

  @Override
  public void unlock() {
    QNode node = myNode.get();
    // 释放锁
    node.locked = false;
    /**
     * 1. 将当前节点指向前驱节点，lock 方法就获取不到当前节点的引用了。（相当于从队列中删除）
     * 2. 当前线程在 unlock 之后，再次获取锁，需要重新排队。
     */
    myNode.set(myPred.get());
  }
}




