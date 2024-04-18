package com.wangzhy.client;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockClient {

    private static int count = 0;
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        inc();
    }

    public static void inc() {
        lock.lock();
        try {
            Thread.sleep(1);
            count++;
            System.out.println(count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}