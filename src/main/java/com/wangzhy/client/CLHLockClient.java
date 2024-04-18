package com.wangzhy.client;

import com.wangzhy.aqs.clh.KFC;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author wangzhy
 * @date 2024年04月16日
 */
public class CLHLockClient {

  public static void main(String[] args) {
    final KFC kfc = new KFC();
    Executor executor = Executors.newFixedThreadPool(5);
    for (int i = 1; i <= 35; i++) {
      executor.execute(kfc::takeout);
    }
  }
}
