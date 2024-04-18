package com.wangzhy.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangzhy
 * @date 2024年04月17日
 */
public class LockClient {

  private static Object lock = new Object();

  private static int count = 0;

  public static void print(){
    synchronized (lock){
      System.out.println(++count);
    }
  }

  public static void main(String[] args) {
    ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 100, 1000, TimeUnit.DAYS,
        new ArrayBlockingQueue<Runnable>(10000),
        Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    for (int i = 0; i < 100; i++) {
        pool.execute(LockClient::print);
    }
  }
}
