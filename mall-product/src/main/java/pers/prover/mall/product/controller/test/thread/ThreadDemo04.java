package pers.prover.mall.product.controller.test.thread;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 异步编排 - 多任务组合
 *
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/8 9:28
 */
public class ThreadDemo04 {

    // 可以使用 CompletableFuture.completedFuture 创建异步任务，传入的参数就是任务的返回值
    private static final List<CompletableFuture<String>> completableFutures = Arrays.asList(
            CompletableFuture.completedFuture("Saber"),
            CompletableFuture.completedFuture("Archer"),
            CompletableFuture.completedFuture("Saber2"),
            CompletableFuture.completedFuture("Archer2")
    );

    public static void main(String[] args) {
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[]{}))
        .thenRun(() -> {
            // 所有任务都执行完啦，获取任务的返回值
            completableFutures.forEach(future -> {
                try {
                    System.out.println("只有在任务都完成后才会执行哟:" + System.currentTimeMillis() + " - " + future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        });
    }

}
