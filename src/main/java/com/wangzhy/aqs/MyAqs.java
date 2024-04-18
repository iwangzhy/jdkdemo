package com.wangzhy.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;

/**
 * @author wangzhy
 * @date 2024年04月17日
 */
public class MyAqs {

  private static class Sync extends AbstractQueuedSynchronizer {

    private static final long serialVersionUID = 3648303117670046018L;

    // 报告是否处于占用状态
    protected boolean isHeldExclusively() {
      return getState() == 1;
    }

    // 当状态为 0 的时候获取锁
    public boolean tryAcquire(int acquires) {
      assert acquires == 1; // 限制参数
      if (compareAndSetState(0, 1)) {
        setExclusiveOwnerThread(Thread.currentThread());
        return true;
      }
      return false;
    }

    // 释放锁，将状态设置为 0
    protected boolean tryRelease(int releases) {
      assert releases == 1; // 限制参数
      if (getState() == 0) {
        throw new IllegalMonitorStateException();
      }
      setExclusiveOwnerThread(null);
      setState(0);
      return true;
    }

    // 提供一个条件变量类
    Condition newCondition() {
      return new ConditionObject();
    }
  }

  private final Sync sync = new Sync();

  // 暴露锁的公共方法
  public void lock() {
    sync.acquire(1);
  }

  public boolean tryLock() {
    return sync.tryAcquire(1);
  }

  public void unlock() {
    sync.release(1);
  }

  public boolean isLocked() {
    return sync.isHeldExclusively();
  }

  public boolean hasQueuedThreads() {
    return sync.hasQueuedThreads();
  }

  public void lockInterruptibly() throws InterruptedException {
    sync.acquireInterruptibly(1);
  }

  public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
    return sync.tryAcquireNanos(1, unit.toNanos(timeout));
  }

  public Condition newCondition() {
    return sync.newCondition();
  }
}
