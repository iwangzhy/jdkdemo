package com.wangzhy.client;

/**
 * @author wangzhy
 * @date 2024年04月17日
 */
public class SynchronizedClient {

  /**
   * 锁住当前实例
   */
  public synchronized void test(){

  }
  /**
   * 锁住当前实例
   */
  public void test1(){
    synchronized (this){

    }
  }

  public synchronized static void test2(){

  }
  public static void test3(){
    synchronized (SynchronizedClient.class){

    }
  }
}
