package mayfly.core.util;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-22 11:00 上午
 */
public class TestThread {

    public static void runWork(Worker worker) {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName());
        System.out.println(thread == worker.thread);
    }

    private static class Worker implements Runnable {

        private Thread thread;

        private Worker(Runnable runnable) {
            this.thread = new Thread(this);
        }

        @Override
        public void run() {
            runWork(this);
        }
    }


    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker(() -> System.out.println(Thread.currentThread().getName()));
            worker.thread.start();
        }
    }
}
