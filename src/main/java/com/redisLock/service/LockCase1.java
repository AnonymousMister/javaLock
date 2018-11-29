
package com.redisLock.service;



import redis.clients.jedis.Jedis;

import java.time.LocalTime;

import static com.redisLock.lockCfg.LockConstants.NOT_EXIST;

import static com.redisLock.lockCfg.LockConstants.MILLISECONDS;

import static com.redisLock.lockCfg.LockConstants.OK;

public class LockCase1 extends RedisLock {



    public LockCase1(Jedis jedis, String name) {
        super(jedis, name);
    }


    @Override
    public void lock() {
        while(true){
            System.out.println(Thread.currentThread().getId()+"正在加锁");
            String result = jedis.set(lockKey, lockValue, NOT_EXIST,MILLISECONDS,10);
            if(OK.equals(result)){
                System.out.println(Thread.currentThread().getId()+"加锁成功!");
                scheduleExpirationRenewal();
                break;
            }
            System.out.println("线程id:"+Thread.currentThread().getId() + "获取锁失败，休眠10秒!时间:"+ LocalTime.now());
            //休眠10秒
            sleepBySencond(10);
        }
    }


    @Override
    public void unlock() {
        //这个解锁方法不具备原子性
        //System.out.println(Thread.currentThread().getId()+"解锁中");
        //String lockValue = jedis.get(lockKey);
        //System.out.println(Thread.currentThread().getId()+"解锁key："+lockValue);
        //if (super.lockValue.equals(lockValue)){
          //  System.out.println(Thread.currentThread().getId()+"解锁成功");
            //jedis.del(lockKey);
        //}
        isOpenExpirationRenewal = false;
        String checkAndDelScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else " +
                "return 0 " +
                "end";
        jedis.eval(checkAndDelScript, 1, lockKey, lockValue);


    }
}
