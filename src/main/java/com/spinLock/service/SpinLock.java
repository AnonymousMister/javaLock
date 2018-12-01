package com.spinLock.service;


import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * java锁自旋锁
 */
public class SpinLock {


    private AtomicReference<Thread> sign = new AtomicReference<>();

    /**
     * 这个实现方法没有锁的重入
     */
   // public void lock() {
     //   Thread current = Thread.currentThread();
    //    while (!sign.compareAndSet(null, current)) {
      //      System.out.println("线程id:"+Thread.currentThread().getId() + "获取锁失败时间:"+ LocalTime.now());
    //    }
        //    System.out.println("线程id:"+Thread.currentThread().getId() + "获取锁成功时间:"+ LocalTime.now());
    //}

    public void lock() {
        Thread current = Thread.currentThread();
        if (sign.compareAndSet(current, current)){
            System.out.println("线程id:"+Thread.currentThread().getId() + "以持有锁获取锁成功时间:"+ LocalTime.now());
            return;
        }
        while (!sign.compareAndSet(null, current)) {
            System.out.println("线程id:"+Thread.currentThread().getId() + "获取锁失败时间:"+ LocalTime.now());
        }
        System.out.println("线程id:"+Thread.currentThread().getId() + "获取锁成功时间:"+ LocalTime.now());
    }



    public void unlock() {

        Thread current = Thread.currentThread();

        sign.compareAndSet(current, null);

        System.out.println("线程id:"+Thread.currentThread().getId() + "解锁成功时间:"+ LocalTime.now());

    }


}
