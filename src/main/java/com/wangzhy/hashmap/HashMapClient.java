package com.wangzhy.hashmap;

import java.util.HashMap;

/**
 * @author wangzhy
 * @date 2024年05月14日
 */
public class HashMapClient {

  public static void main(String[] args) {
    HashMap<String, String> map = new HashMap<>(7);
    map.put("k", "v");
    map.put("k", "v1");
  }
}
