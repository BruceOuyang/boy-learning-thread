package bruce.bugmakers.club.threadlocal;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadLocalDemo 线程封闭之 ThreadLocal 示例
 *
 * @Author Bruce
 * @Date 2019/10/19 11:16
 * @Version 1.0
 **/
@Slf4j
public class ThreadLocalDemo {

    /**
     * threadLocal 变量，每个线程都有一个副本，互不干扰
     */
    public static ThreadLocal<String> value = new ThreadLocal<>();

    public static void main(String [] args) throws InterruptedException {
        testThreadLocal();
    }

    public static void testThreadLocal() throws InterruptedException {

        log.info("==============threadlocal线程封闭示例======================");

        log.info("主线程设置 value 的值：main-thread-set-123");
        value.set("main-thread-set-123");

        log.info("执行 thread1 线程之前，主线程取到的值：{}", value.get());

        Thread thread1 = new Thread(() -> {
            log.info("thread1 取到的值：{}", value.get());

            log.info("thread1 线程设置 value 的值：thread1-set-456");

            value.set("thread1-set-456");

            log.info("设置后，thread1 取到的值：{}", value.get());

            log.info("thread1 执行结束");
        });

        thread1.start();

        Thread.sleep(3000L);

        log.info("执行 thread1 线程之后，主线程取到的值：{}", value.get());
    }
}
