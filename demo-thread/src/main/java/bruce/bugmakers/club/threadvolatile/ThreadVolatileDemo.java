package bruce.bugmakers.club.threadvolatile;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * ThreadVolatileDemo 线程可见性
 *
 * 通过设置JVM的参数，打印出JIT编译的内容（非class内容）
 * -server -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=jit.log
 *
 * @Author Bruce
 * @Date 2019/10/22 13:46
 * @Version 1.0
 **/
@Slf4j
public class ThreadVolatileDemo {

    /**
     * 此处加上 volatile 关键字就可以解决可见性问题
     */
    private boolean flag = true;

    public static void main(String [] args) throws InterruptedException {

        ThreadVolatileDemo demo = new ThreadVolatileDemo();

        Thread thread1 = new Thread(() -> {
            int i = 0;

            // class -> 运行时 jit 编译 -> 汇编指令 -> 重排序
            while (demo.flag) {
                i++;  // 在这里加锁处理也可以解决可见性问题
            }

//            上面的代码在重排序后，会变成如下的样子，使用 -Djava.compiler=NONE 可以关闭指令重排序
//            if (demo.flag) {
//                while (true) {
//                    i++;
//                }
//            }

            log.info("i = {}", i);
        });

        thread1.start();

        Thread.sleep(2000);

        // 此处尝试修改 flag 值来结束 thread1 的执行，但是 main-thread 对 demo.flag 的修改，对thread1 不可见
        demo.flag = false;

        log.info("demo.flag 设置成 false 了");

        log.info("thread1 的状态：{}", thread1.getState().toString());
    }
}


