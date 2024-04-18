package com.wangzhy.client;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangzhy
 * @date 2024年04月17日
 */
@Slf4j
public class CasClient {
  public static AtomicInteger race = new AtomicInteger(0);

  private static final int THREADS_COUNT = 20;

  public static void increase() {
    // 原子操作
    race.getAndIncrement();
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
    // 200000
    log.info("race:{}", race);
  }
}
