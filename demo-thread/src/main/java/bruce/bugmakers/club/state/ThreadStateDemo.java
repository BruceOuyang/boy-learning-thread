package bruce.bugmakers.club.state;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadStateDemo 多线程运行状态切换示例
 *
 * @Author Bruce
 * @Date 2019/10/15 22:56
 * @Version 1.0
 **/
@Slf4j
public class ThreadStateDemo {

    public static void main(String [] args) throws InterruptedException {
//        test1New2Runnable2Terminated();
//        test2New2Runnable2Wait2Runnable2Terminated_sleepWay();
        test2New2Runnable2Wait2Runnable2Terminated_waitNotifyWay();
//        test3New2Runnable2Blocked2Runnable2Terminated_sleepWay();
    }

    /**
     * 第一种状态切换：新建 -> 运行 -> 终止
     */
    public static void test1New2Runnable2Terminated() throws InterruptedException {

        log.info("==================================第一种状态切换：新建 -> 运行 -> 终止==================================");

        Thread thread1 = new Thread(() -> {
            log.info("thread1 当前状态：{}", Thread.currentThread().getState().toString());
            log.info("thread1 执行了");
        });

        log.info("未调用 start 方法，thread 当前状态：{}", thread1.getState().toString());

        thread1.start();

        Thread.sleep(2000L);

        log.info("调用 start 方法且等待 2 秒后，thread1 当前状态：{}", thread1.getState().toString());
        log.info("");
        log.info("");
    }

    /**
     * 第二种状态切换：新建 -> 运行 -> 等待(sleep方式) -> 运行 -> 终止
     */
    public static void test2New2Runnable2Wait2Runnable2Terminated_sleepWay() throws InterruptedException {

        log.info("==================================第二种状态切换：新建 -> 运行 -> 等待(sleep方式) -> 运行 -> 终止==================================");

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("thread2 当前状态：{}", Thread.currentThread().getState().toString());
            log.info("thread2 执行了");
        });

        log.info("未调用 start 方法，thread2 当前状态：{}", thread2.getState().toString());

        thread2.start();

        log.info("调用 start 方法，thread2 当前状态：{}", thread2.getState().toString());

        Thread.sleep(200L);

        log.info("调用 start 方法且等待 200 毫秒后，thread2 当前状态：{}", thread2.getState().toString());

        Thread.sleep(3000L);

        log.info("调用 start 方法且再等待 3 秒后，thread2 当前状态：{}", thread2.getState().toString());
        log.info("");
        log.info("");
    }

    /**
     * 第二种状态切换：新建 -> 运行 -> 等待(wait/notify方式) -> 运行 -> 终止
     */
    public static void test2New2Runnable2Wait2Runnable2Terminated_waitNotifyWay() throws InterruptedException {

        log.info("==================================第二种状态切换：新建 -> 运行 -> 等待(wait/notify方式) -> 运行 -> 终止==================================");

        Thread thread2 = new Thread(() -> {
            synchronized (ThreadStateDemo.class) {
                try {
                    ThreadStateDemo.class.wait();
                    log.info("thread2 当前状态：{}", Thread.currentThread().getState().toString());
                    log.info("thread2 执行了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        log.info("未调用 start 方法，thread2 当前状态：{}", thread2.getState().toString());

        thread2.start();

        log.info("调用 start 方法，thread2 当前状态：{}", thread2.getState().toString());

        Thread.sleep(200L);

        log.info("调用 start 方法且等待 200 毫秒后，thread2 当前状态：{}", thread2.getState().toString());

        synchronized (ThreadStateDemo.class) {
            ThreadStateDemo.class.notify();
        }

        log.info("释放锁后，thread2 当前状态：{}", thread2.getState().toString());

        Thread.sleep(1000L);

        log.info("等待 1000 毫秒后，thread2 当前状态：{}", thread2.getState().toString());
        log.info("");
        log.info("");
    }

    /**
     * 第三种状态切换：新建 -> 运行 -> 阻塞 -> 运行 -> 终止
     */
    public static void test3New2Runnable2Blocked2Runnable2Terminated_sleepWay() throws InterruptedException {

        log.info("==================================第三种状态切换：新建 -> 运行 -> 阻塞 -> 运行 -> 终止==================================");

        Thread thread3 = new Thread(() -> {
            synchronized (ThreadStateDemo.class) {
                log.info("thread3 当前状态：{}", Thread.currentThread().getState().toString());
                log.info("thread3 执行了");
            }
        });

        synchronized (ThreadStateDemo.class) {

            log.info("未调用 start 方法，thread3 当前状态：{}", thread3.getState().toString());

            thread3.start();

            log.info("调用 start 方法，thread3 当前状态：{}", thread3.getState().toString());

            Thread.sleep(200L);

            log.info("调用 start 方法且等待 200 毫秒后，thread3 当前状态：{}", thread3.getState().toString());
        }

        log.info("test3 主方法释放 ThreadStateDemo.class 锁");

        Thread.sleep(3000L);

        log.info("调用 start 方法且再等待 3 秒后，thread3 当前状态：{}", thread3.getState().toString());
        log.info("");
        log.info("");
    }
}
