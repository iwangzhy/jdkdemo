package com.wangzhy.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangzhy
 * @date 2024年04月17日
 */
@Slf4j
public class Singleton {

  private static  Singleton instance;

  public static Singleton getInstance(){
    if(instance == null){
      synchronized (Singleton.class){
        if(instance == null){
          instance = new Singleton();
        }
      }
    }
    if(instance == null){
      throw new RuntimeException("创建对象失败！");
    }
    return instance;
  }

  public static void main(String[] args) {
    ThreadPoolExecutor pool = new ThreadPoolExecutor(1000, 10000, 1000, TimeUnit.DAYS,
        new ArrayBlockingQueue<Runnable>(10000),
        Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    for (int i = 0; i < 10000; i++) {
      pool.execute(Singleton::getInstance);
    }
  }
}
