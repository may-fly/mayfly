package mayfly.core.util;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-10-12 10:57 上午
 */
public class CodeByteTest {

    static class Test2 {
        public static String str = "hello world";

        static {
            System.out.println("Test2 init");
        }
    }

    static class Test3 extends Test2 {
        public static final String str = "hello test3";

        static {
            System.out.println("Test3 init");
        }
    }

    public static Method method;

    static {
        try {
            method = Test4.class.getDeclaredMethod("sys");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static class Test4 {
        public static Test2 test2 = new Test2();

        private volatile boolean flag;

        public void sys() {
            System.out.println(Thread.currentThread().getName() + ":" + flag);
        }

        public void change() {
            this.flag = !flag;
        }
    }

    private int i = 100;
    private double j;

    public CodeByteTest(int i, double j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        int j = 11111;
        int k = 1111111;
        int i = 100001010;
        int l = 1;
//        return j;
//        return i + j + k + l;
        if (i > 1) {
            return j;
        } else {
            return k;
        }
    }

    static class VmStacks {
        public long addL(long x, long y) {
            return (++x) + (y++);
        }

        public int directInvoke() {
            return addI(1, 2);
        }

        /**
         * 纯粹为了演示在方法中申明多余的变量与直接将值传递给方法的区别
         */
        public int invokeAdd() {
            int x = 1;
            int y = 2;
            return addI(x, y);
        }

        public int addI(int x, int y) {
            int z = y++;  // 纯粹为了演示  i++与++i之间字节码的区别，无其他意义
            return ++x + y;
        }

        private int i = 0;

        public int getI() {
            return this.i;
        }
    }

    static class ThreadTest implements Runnable {

        private Test4 test4;

        public ThreadTest(Test4 t) {
            this.test4 = t;
        }

        @Override
        public void run() {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(15));
            test4.change();
            test4.sys();
        }
    }

    static class MetaSpace {
        public static void print() {
            int i = 1000000;
            String str = "hello world";
            System.out.println(str);
        }
    }

//    public synchronized static void main(String[] args) throws Exception {
////        CodeByteTest b = new CodeByteTest(1, 2.1);
////        System.out.println(b.i);
////
////        System.out.println(Integer.toBinaryString((1 << 29) - 1));
////        System.out.println(Integer.toBinaryString((1 << 29) - 1).length());
////        System.out.println(Integer.toBinaryString((1 << 29)));
////        System.out.println(Test2.str);
//
////        Test4 test4 = new Test4();
////        for (int i = 0; i < 100000; i++) {
////            new Thread(new ThreadTest(test4), "Test" + i).start();
////        }
//
////        Lock lock = new ReentrantLock();
////        lock.lock();
////        VmStacks vmStacks = new VmStacks();
//////        System.out.println(vmStacks.invokeAdd());
//////        MetaSpace.print();
////        int i = vmStacks.getI();
////        System.out.println(i);
////        System.out.println(i);
////        System.out.println(i);
////        System.out.println(i);
////        System.out.println(i);
////        if (i > 1)
////            System.out.println(0);
//        Method sys = new Test4().getClass().getDeclaredMethod("wait");
//        Method sys2 = new Test4().getClass().getMethod("wait");
//        System.out.println(null == sys2);
//    }
}
