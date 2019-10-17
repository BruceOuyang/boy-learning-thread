package bruce.bugmakers.club.p02stop;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadStopDemo 线程终止示例
 *
 * @Author Bruce
 * @Date 2019/10/15 23:35
 * @Version 1.0
 **/
@Slf4j
public class ThreadStopDemo {

    public static void main(String [] args) throws InterruptedException {
        test1ThreadStop();
        test2ThreadInterrupt();
        test3ThreadStopWithLogicFlag();
    }

    public static void test1ThreadStop() throws InterruptedException {

        log.info("==================================第 1 种测试：使用 stop 方法终止线程==================================");

        StopThread thread = new StopThread();
        thread.start();

        // 休眠 1 秒，确保 i 值变动
        Thread.sleep(1000L);

        // 终止线程
        thread.stop();

        // 确保线程已经终止
        while (thread.isAlive()) {}

        // 输出结果
        thread.print();

        log.info("");
        log.info("");
    }

    public static void test2ThreadInterrupt() throws InterruptedException {

        log.info("==================================第 2 种测试：使用 interrupt 方法终止线程==================================");
        StopThread thread = new StopThread();
        thread.start();

        // 休眠 1 秒，确保 i 值变动
        Thread.sleep(1000L);

        // 终止线程
        thread.interrupt();

        // 确保线程已经终止
        while (thread.isAlive()) {}

        // 输出结果
        thread.print();

        log.info("");
        log.info("");
    }

    public volatile static boolean flag = true;
    public static void test3ThreadStopWithLogicFlag() throws InterruptedException {

        log.info("==================================第 3 种测试：使用逻辑标识符终止线程==================================");

        Thread thread = new Thread(() -> {
            try{
                int i = 0;
                while(flag) {
                    ++i;
                    log.info("thread 第 {} 次打印...", i);
                    Thread.sleep(1000L);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        log.info("启动 thread，每秒钟打印一次");
        thread.start();

        log.info("5 秒后修改逻辑标识符 flag 为 false");
        Thread.sleep(5000L);
        flag = false;
        log.info("程序运行结束");
    }
}

@Slf4j
class StopThread extends Thread {
    private int i = 0, j = 0;

    @Override
    public void run() {

        // 增加同步锁，确保线程安全
        synchronized (this) {

            // 模拟业务处理 1
            ++i;

            try {
                // 休眠 3 秒，模拟业务处理 1 的耗时操作
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 模拟业务处理 2
            ++j;
        }
    }

    public void print() {
        log.info("i = {}， j = {}", i, j);
    }
}