package bruce.bugmakers.club.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

/**
 * ThreadPoolDemo 线程池的使用示例
 *
 * @Author Bruce
 * @Date 2019/10/19 12:22
 * @Version 1.0
 **/
@Slf4j
public class ThreadPoolDemo {

    public static void main(String [] args) throws InterruptedException {
//        threadPoolExecutorTest1();
//        threadPoolExecutorTest2();
//        threadPoolExecutorTest3();
//        threadPoolExecutorTest4();
//        threadPoolExecutorTest5();
//        threadPoolExecutorTest6();
        threadPoolExecutorTest7();
    }

    /**
     * 测试：提交 15 个执行时间需要 3 秒的任务，看看线程池的状态
     * @param threadPoolExecutor 传入不同的线程池，看不同的效果
     */
    public static void testCommon(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        for (int i = 0; i < 15; i++) {
            int n = i;
            threadPoolExecutor.submit(() -> {
                log.info("开始执行：{}", n);

                try {
                    Thread.sleep(3000L);
                    log.info("执行结束：{}", n);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            log.info("任务提交成功：{}", n);
        }

        Thread.sleep(500L);

        log.info("查看线程池信息：");
        log.info("当前线程池，线程数量：{}", threadPoolExecutor.getPoolSize());
        log.info("当前线程池，队列等待数量：{}", threadPoolExecutor.getQueue().size());
        log.info("");

        log.info("等待15秒...再次查看(理论上，会被超出核心线程数量的线程自动销毁)");
        Thread.sleep(15000L);
        log.info("");

        log.info("查看线程池信息：");
        log.info("当前线程池，线程数量：{}", threadPoolExecutor.getPoolSize());
        log.info("当前线程池，队列等待数量：{}", threadPoolExecutor.getQueue().size());

        log.info("");
        log.info("");
    }

    /**
     * 测试1：核心线程数5，最大线程数10，无界队列，超出核心线程数量的线程存活时间5秒
     */
    public static void threadPoolExecutorTest1() throws InterruptedException {

        // 定义任务队列：大小不限
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();

        // 定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, queue);

        // 调用测试方法
        testCommon(threadPoolExecutor);
    }

    /**
     * 测试2：核心线程数5，最大线程数10，队列大小3，超出核心线程数量的线程存活时间5秒，指定拒绝策略的
     */
    public static void threadPoolExecutorTest2() throws InterruptedException {

        // 定义任务队列：大小3
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>(3);

        // 定义拒绝策略
        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            log.error(".........................有任务被拒绝执行了");
        };

        // 定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, queue, rejectedExecutionHandler);

        // 调用测试方法
        testCommon(threadPoolExecutor);
    }

    /**
     * 测试3：核心线程数5，最大线程数5，无界队列，超出核心线程数量的线程存活时间5秒
     */
    public static void threadPoolExecutorTest3() throws InterruptedException {

        // 定义任务队列：大小不限
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();

        // 定义线程池
        // 相当于 ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, queue);

        // 调用测试方法
        testCommon(threadPoolExecutor);
    }

    /**
     * 测试4：核心线程数0，最大线程数Integer.MAX_VALUE，同步队列 SynchronousQueue，超出核心线程数量的线程存活时间60秒，适用于任务大小不可控的情况
     */
    public static void threadPoolExecutorTest4() throws InterruptedException {

        // 定义任务队列：同步队列
        // 它实际上不是一个真正的队列，因为它不会为队列中的元素维护存储空间。与其他队列不同的是，他维护一组线程，这些线程在等待着把元素加入或移除队列
        // 客户端向线程池提交任务时，线程池中有没有空闲的线程能够从 SynchronousQueue 队列中取一个任务，那么相应的 offer 方法调用就会失败（即任务没有被存入工作队列）
        // 此时，线程池会创建一个新的线程用于对这个入队列失败的任务进行执行（前提是线程池大小还没打过 maximunPoolSize）
        BlockingQueue<Runnable> queue = new SynchronousQueue<>();

        // 定义线程池
        // 相当于 ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool(5);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, queue);

        // 调用测试方法
        testCommon(threadPoolExecutor);

        Thread.sleep(60000L);

        log.info("60秒后，查看线程池大小：{}", threadPoolExecutor.getPoolSize());
    }

    /**
     * 测试5：定时执行线程池信息，3秒后执行，一次性任务，到点就执行
     *       核心线程数5，最大数Integer.MAX_VALUE，延时队列(DelayedWrokQueue), 超出核心线程数的线程存活时间：0秒
     */
    public static void threadPoolExecutorTest5() throws InterruptedException {

        // 相当于 Executors.newScheduledThreadPool()
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(5);
        threadPoolExecutor.schedule(() -> {
            log.info("任务被执行，当前时间：{}", System.currentTimeMillis());
        }, 3, TimeUnit.SECONDS);

        log.info("定时任务已提交，当前时间：{}", System.currentTimeMillis());

        log.info("当前线程池中，线程数量：{}", threadPoolExecutor.getPoolSize());
    }

    /**
     * 测试6：定时执行线程池信息，3秒后执行，周期任务，到点就执行
     *       核心线程数5，最大数Integer.MAX_VALUE，延时队列(DelayedWrokQueue), 超出核心线程数的线程存活时间：0秒
     */
    public static void threadPoolExecutorTest6() throws InterruptedException {

        // 相当于 Executors.newScheduledThreadPool()
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(5);

//        // 当前任务执行时长超过周期时间时，下次任务立即执行
//        threadPoolExecutor.scheduleAtFixedRate(() -> {
//            try {
//                Thread.sleep(3000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            log.info("任务_1 - 被执行，当前时间：{}", System.currentTimeMillis());
//        }, 2, 1, TimeUnit.SECONDS);

        // 当前任务执行时长超过周期时间时，下次任务需继续等待一个周期时长，然后执行
        threadPoolExecutor.scheduleWithFixedDelay(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("任务_2 - 被执行，当前时间：{}", System.currentTimeMillis());
        }, 2, 1, TimeUnit.SECONDS);
    }

    /**
     * 测试7：终止线程池。
     *       线程池信息：核心线程数5，最大线程数10，队列大小3，超出核心线程数量的线程存活时间5秒，指定拒绝策略
     */
    public static void threadPoolExecutorTest7() throws InterruptedException {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(3), (r, executor) -> {
            log.info("....................有任务被拒绝执行了");
        });

        for (int i = 0; i < 15; i++) {
            int n = i;
            threadPoolExecutor.submit(() -> {
                log.info("开始执行：{}", n);
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("执行结束：{}", n);
            });
            log.info("任务提交成功：{}", n);
        }

        log.info("1 秒后终止线程池...");

        Thread.sleep(1000L);

        // 优雅的关闭
//        threadPoolExecutor.shutdown();

        // 直接关闭所有
        List<Runnable> runnables = threadPoolExecutor.shutdownNow();
        log.info("未完成的任务数：{}", runnables.size());

        Thread.sleep(5000L);

        // 尝试再次提交任务到线程池
        threadPoolExecutor.submit(() -> {
            log.info("。。。。。。。。。。。。。。。。追加执行了....");
        });

    }
}
