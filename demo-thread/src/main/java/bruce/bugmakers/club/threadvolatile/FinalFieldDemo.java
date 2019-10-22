package bruce.bugmakers.club.threadvolatile;

import lombok.extern.slf4j.Slf4j;

/**
 * FinalFieldDemo final 关键字
 *
 * @Author Bruce
 * @Date 2019/10/22 16:39
 * @Version 1.0
 **/
public class FinalFieldDemo {

    public static void main(String [] args) throws InterruptedException {
        new Thread(()->{FinalFieldExample.write();}).start();
        new Thread(()->{FinalFieldExample.read();}).start();
    }
}

@Slf4j
class FinalFieldExample{

    final int x;
    int y;

    FinalFieldExample() {
        x = 3;
        y = 4;
    }

    static FinalFieldExample f;

    static void write() {
        f = new FinalFieldExample();
    }

    static void read() {
        if (f != null) {
            // i 肯定是 3
            int i = f.x;

            // j 可能是 0
            int j = f.y;

            log.info("i = {}, j = {}", i, j);
        }
    }
}


