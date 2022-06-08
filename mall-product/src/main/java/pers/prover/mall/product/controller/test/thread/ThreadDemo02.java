package pers.prover.mall.product.controller.test.thread;

import org.checkerframework.checker.units.qual.A;

import java.util.concurrent.*;

/**
 * 线程池 - 创建异步对象，计算完成时回调方法
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/7 22:14
 */
public class ThreadDemo02 {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);


    public static void main(String[] args) {
        System.out.println("主线程开始执行..." + Thread.currentThread().getId());
        // 从线程池中调度线程执行任务
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("水巴天下第一");
            return "巴御前天下第一";
        }, executorService);
        // 处理计算结果
        completableFuture.whenComplete((res, error) -> {
            if (error == null) {
                System.out.println("线程的返回结果:" + res);
            } else {
                System.out.println("出现异常啦");
            }
        });
        System.out.println("主线程执行结束..." + Thread.currentThread().getId());
    }

}

