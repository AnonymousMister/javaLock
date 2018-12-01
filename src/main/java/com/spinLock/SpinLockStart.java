package com.spinLock;

import com.spinLock.service.SpinLock;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SpinLockStart {


    public static  void main(String[] args){
        SpinLockStart testRedis=new SpinLockStart();
        SpinLock lock =new SpinLock();
        testRedis.starts(lock);

    }

    public void starts(   SpinLock lock){
        ThreadPoolExecutor pool = new ThreadPoolExecutor(0, 2,
                1, TimeUnit.SECONDS,
                new SynchronousQueue<>());
        //添加10个线程获取锁
        for (int i = 0; i < 2; i++) {
            pool.submit(() -> {
                try {

                    lock.lock();
                    lock.lock();
                    //模拟业务执行15秒
                    Thread.sleep(30*1000);
                    lock.unlock();
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        }


        //当线程池中的线程数为0时，退出
        while (pool.getPoolSize() != 0) {}
    }
}
