package com.wangzhy.client;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wangzhy
 * @date 2024年04月17日
 */
@Slf4j
public class VolatileClient {

  public static volatile int race = 0;

  private static final int THREADS_COUNT = 20;

  public static void increase() {
    race++;
  }

  public static void main(String[] args) throws InterruptedException {
    Thread[] threads = new Thread[THREADS_COUNT];
    for (int i = 0; i < THREADS_COUNT; i++) {
      threads[i] = new Thread(new Runnable() {
        @Override
        public void run() {
          for (int i = 0; i < 10000; i++) {
            increase();
          }
        }
      });
      threads[i].start();
    }

    while (Thread.activeCount() > 1) {
      Thread.yield();
    }
    // 75274 < 10000 * 20
    // volatile 只能保证可见性，不能保证原子性。
    log.info("race:{}", race);
  }
}
