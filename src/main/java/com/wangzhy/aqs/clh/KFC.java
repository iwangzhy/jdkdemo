package com.wangzhy.aqs.clh;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KFC {

  private final Lock lock = new CLHLock();
  private int i = 0;

  public void takeout() {
    try {
      lock.lock();
      log.info("Thread.currentThread().getName(): {} : 拿了第{}份外卖",
          Thread.currentThread().getName(), ++i);
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }
}
