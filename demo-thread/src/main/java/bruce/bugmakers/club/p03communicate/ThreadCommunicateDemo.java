package bruce.bugmakers.club.p03communicate;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * ThreadCommunicateDemo
 *
 * @Author Bruce
 * @Date 2019/10/18 21:59
 * @Version 1.0
 **/
@Slf4j
public class ThreadCommunicateDemo {

    public static final String LOCK = "lock";

    public static void main(String [] args) throws InterruptedException {
//        testSuspendResume();
//        testWaitNotify();
        testParkUnpark();
    }

    /**
     * 包子店
     */
    public static Object baozidian = null;

    public static void testSuspendResume() throws InterruptedException {
        log.info("===============suspend/resume=====================");

        // 消费者去买包子
        Thread consumer = new Thread(() -> {
            while (baozidian == null) {
                log.info("来晚了，包子都卖完了，排队等候...");
                Thread.currentThread().suspend();
            }
            log.info("买到包子了，回家");
        });

        consumer.start();

        // 三秒之后生产一个包子
        Thread.sleep(3000L);

        baozidian = new Object();

        log.info("新一笼包子蒸好啦...");

        log.info("消费者状态：{}", consumer.getState().toString());

        consumer.resume();
        log.info("");
        log.info("");
    }

    public static void testWaitNotify() throws InterruptedException {
        log.info("===============wait/notify=====================");

        // 消费者去买包子
        Thread consumer = new Thread(() -> {

            while (baozidian == null) {
                synchronized (LOCK) {
                    try {
                        log.info("来晚了，包子都卖完了，排队等候...");


                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.info("买到包子了，回家");
        });

        consumer.start();

        // 三秒之后生产一个包子
        Thread.sleep(3000L);

        baozidian = new Object();


        log.info("消费者状态：{}", consumer.getState().toString());

        synchronized (LOCK) {
            log.info("新一笼包子蒸好啦...");
            LOCK.notify();
        }
        log.info("");
        log.info("");
    }

    public static void testParkUnpark() throws InterruptedException {
        log.info("===============park/unpark=====================");

        // 消费者去买包子
        Thread consumer = new Thread(() -> {

            while (baozidian == null) {
                log.info("来晚了，包子都卖完了，排队等候...");
                LockSupport.park();
            }
            log.info("买到包子了，回家");
        });

        consumer.start();

        // 三秒之后生产一个包子
        Thread.sleep(3000L);

        baozidian = new Object();


        log.info("消费者状态：{}", consumer.getState().toString());

        log.info("新一笼包子蒸好啦...");
        LockSupport.unpark(consumer);

        log.info("");
        log.info("");
    }
}
