package pers.prover.mall.product.controller.test.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步编排 - 线程串行化
 *
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/8 9:15
 */
public class ThreadDemo03 {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("主线程执行..." + Thread.currentThread().getId());
        CompletableFuture.supplyAsync(() -> {
            System.out.println("thread-1: " + Thread.currentThread().getId());
            return "thread-1-res";
        }, EXECUTOR_SERVICE).thenApply((res) -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread-2: thread-1-res = " + res);
            System.out.println("thread-2: " + Thread.currentThread().getId());
            return "thread-2-res";
        }).whenComplete((res, err) -> {
            if (err == null) {
                System.out.println("线程1，2任务完，获取任务2的返回结果:" + res);
            } else {
                System.out.println("出异常啦:" + err);
            }
        });
        System.out.println("主线程执行完成..." + Thread.currentThread().getId());

    }

}
