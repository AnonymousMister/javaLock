package com.redisLock;

import com.redisLock.service.LockCase1;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RedisStart {


    public static  void main(String[] args){
        RedisStart testRedis=new RedisStart();
        testRedis.starts();

    }

    public void starts(){

        ThreadPoolExecutor pool = new ThreadPoolExecutor(0, 10,
                1, TimeUnit.SECONDS,
                new SynchronousQueue<>());

        //添加10个线程获取锁
        for (int i = 0; i < 10; i++) {
            pool.submit(() -> {
                try {
                    JedisShardInfo jedisShardInfo=new JedisShardInfo("118.25.122.254",6379);
                    Jedis jedis=new Jedis(jedisShardInfo);
                    jedis.auth("datadriver");
                    LockCase1 lock = new LockCase1(jedis, "线程");
                    lock.lock();

                    //模拟业务执行15秒
                    lock.sleepBySencond(30);

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
