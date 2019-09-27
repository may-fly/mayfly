package mayfly.core.algorithm;

import sun.reflect.Reflection;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-24 10:46 上午
 */
public class MaxChildArr {
    public static int max(int[] arr) {
        int maxSum = 0;
        int thisSum = 0;

        for (int i = 0; i < arr.length; i++) {

            thisSum += arr[i];

            if (thisSum > maxSum) {
                maxSum = thisSum;
            } else if (thisSum < 0) {
                thisSum = 0;
            }
        }
        return maxSum;
    }

    public static void main(String[] args) throws Exception{
        int x = 1;
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        for(int i = 0; i < 20; i++) {
//            executor.execute(() -> {
//                System.out.println(Thread.currentThread().getName());
//            });
//        }
        System.out.println(Reflection.getCallerClass().getName());
//        executor.shutdown();
    }
//        System.out.println(max(new int[]{-1, -5, -10, -2, -20, -12}));

//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(3000);
//            } catch (Exception e) {
//
//            }
////            int i = 1/0;
//            return 100;
//        });
//        future.thenAccept(v ->  {
//            System.out.println(v + 1);
//        });
////        System.out.println(f.get());
//        System.out.println("future after");
//    }
}
