package bruce.bugmakers.club.threadvolatile;

import lombok.extern.slf4j.Slf4j;

/**
 * WordTearingDemo 字分裂示例
 *
 * @Author Bruce
 * @Date 2019/10/22 16:54
 * @Version 1.0
 **/
@Slf4j
public class WordTearingDemo extends Thread {

    public static void main(String [] args) {
        for (int i = 0; i < LENGTH; i++) {
            (threads[i] = new WordTearingDemo(0)).run();
        }
    }

    static final int LENGTH = 8;
    static final int ITERS = 1000000;
    static byte[] counts = new byte[LENGTH];
    static Thread[] threads = new Thread[LENGTH];

    final int id;

    WordTearingDemo(int i){
        id = i;
    }

    public void run(){
        byte v = 0;
        for (int i = 0; i < ITERS; i++) {
            byte v2 = counts[id];
            if (v != v2) {
                log.info("Word-Tearing found: counts[{}] = {}, should be {}", id, v2, v);
                return;
            }
            v++;
            counts[id] = v;
        }
    }
}
