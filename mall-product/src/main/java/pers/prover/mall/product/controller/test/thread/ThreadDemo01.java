package pers.prover.mall.product.controller.test.thread;

import java.util.concurrent.*;

/**
 * 复习线程
 * @author 小丶木曾义仲丶哈牛柚子露丶蛋卷
 * @version 1.0
 * @date 2022/6/7 20:08
 */
public class ThreadDemo01 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("主线程开始执行..." + Thread.currentThread().getId());
        // 方式一：继承 Thread
        // new Thread01().start();
        // 方式二：实现 Runnable
        // new Thread(new Thread02()).start();
        // 方式三：实现 Callable + FutureTask
        // FutureTask<String> futureTask = new FutureTask<>(new Thread03());
        // futureTask.run(); // 执行线程任务
        // System.out.println("线程的执行结果：" + futureTask.get()); // .get() 方法会获取线程任务的返回值，同时也会阻塞当前线程
        // 方式四：线程池-创建方式1
        // new ThreadPoolExecutor();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        threadPool.execute(() -> {
            System.out.println("线程任务开始执行..." + Thread.currentThread().getId());
        });
        System.out.println("主线程执行结束..." + Thread.currentThread().getId());
    }

}

class Thread01 extends Thread {

    @Override
    public synchronized void run() {
        System.out.println("线程任务开始执行..." + Thread.currentThread().getId());
    }
}

class Thread02 implements Runnable {

    @Override
    public void run() {
        System.out.println("线程任务开始执行..." + Thread.currentThread().getId());
    }
}

class Thread03 implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("线程任务开始执行..." + Thread.currentThread().getId());
        return "巴御前天下第一";
    }
}
