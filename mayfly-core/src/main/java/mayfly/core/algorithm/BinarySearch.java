package mayfly.core.algorithm;

import java.util.Arrays;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-09-07 10:14 上午
 */
public class BinarySearch {

    public static int search(int[] values, int value) {
        int low = 0, height = values.length - 1, mid;
        while (low < height) {
            mid = (height + low) / 2;
            int v  = values[mid];
            if (v == value) {
                return mid;
            }
            if (value < v) {
                height = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        throw new IllegalArgumentException("不存在该元素");
    }

    public static int[] quickSort(int[] values) {
        if (values.length < 2) {
            return values;
        }
        int pivot = values[0];
        int[] less = Arrays.stream(values).filter(v -> v < pivot).toArray();
        int[] greater = Arrays.stream(values).filter(v -> v > pivot).toArray();
        return addArr(quickSort(less), pivot, quickSort(greater));
    }

    private static int[] addArr(int[] arr1, int pivot, int[] arr2) {
        int[] result = new int[arr1.length + arr2.length + 1];
        int i;
        for (i = 0; i < arr1.length; i++) {
            result[i] = arr1[i];
        }
        result[i++] = pivot;
        for (int j = 0; j <  arr2.length; j++) {
            result[i++] = arr2[j];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] values = new int[]{12, 21, 80, 4, 2, 5, 21, 92, 10, 11};
//        System.out.println(search(values, 7));
        System.out.println(Arrays.toString(quickSort(values)));
    }
}
