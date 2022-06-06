package pers.prover.mall.product.controller.api;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学习 Redisson 读写锁，闭锁，信号量
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/6 15:48
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping("/write")
    public String write() {
        // 获取读写锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("read-write-lock");
        // 获取写锁
        RLock rLock = readWriteLock.writeLock();
        // 抢占写锁
        rLock.lock();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 解锁
        rLock.unlock();
        return "ok";
    }

    @RequestMapping("/read")
    public String read() {
        // 获取读写锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("read-write-lock");
        // 获取读锁
        RLock rLock = readWriteLock.readLock();
        rLock.lock();
        // try {
        //     Thread.sleep(5000);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        System.out.println("读取数据...");
        rLock.unlock();
        return "读取成功";
    }

    @RequestMapping("/release")
    public String release() {
        RSemaphore semaphore = redissonClient.getSemaphore("count");
        semaphore.release();
        // 也可以指定增加多少
        // semaphore.release(2);
        return "release";
    }

    @RequestMapping("acquire")
    public String acquire() {
        RSemaphore semaphore = redissonClient.getSemaphore("count");
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "acquire";
    }

    @RequestMapping("/await")
    public String await() {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("count-down-latch");
        // 设置计数器
        countDownLatch.trySetCount(5);
        // 开始等待
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "阻塞的线程执行完成";
    }

    @RequestMapping("/get")
    public String get() {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("count-down-latch");
        // 减少计数器的次数
        countDownLatch.countDown();
        return "一个任务完成";
    }

}
