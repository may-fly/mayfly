package mayfly.core.util;

import java.util.*;

/**
 * @author meilin.huang
 * @date 2018/7/9 下午1:55
 */
public class CollectionUtils {


    /**
     * 判断集合是否为空
     *
     * @param collection 集合对象
     * @return 为空 true 否则false
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合不为空
     *
     * @param collection 集合
     * @return 不为空 ture
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断一个集合中是否存在指定元素
     *
     * @param collection 集合对象
     * @param value      集合元素
     * @param <T>        集合类型
     * @return true:存在 否则不存在
     */
    public static <T> boolean contains(Collection<T> collection, T value) {
        return !isEmpty(collection) && collection.contains(value);
    }

    /**
     * 根据比较器比较两个collection中哪些是新增的对象以及删除的对象和没有改变的对象
     *
     * @param newCollection    新list
     * @param oldCollection    旧list
     * @param comparator 集合对象比较器
     * @param <T>        集合元素泛型对象
     * @return 比较结果 {@link CompareResult}
     */
    public static <T> CompareResult<T> compare(Collection<T> newCollection, Collection<T> oldCollection, Comparator<T> comparator) {
        List<T> unmodifiedValue = new ArrayList<>();

        Iterator<T> newIte = newCollection.iterator();
        while (newIte.hasNext()) {
            T newObj = newIte.next();
            //遍历旧数组
            Iterator<T> oldIte = oldCollection.iterator();
            while (oldIte.hasNext()) {
                //如果新旧数组中的对象相同，则为没有改变的对象
                T oldObj = oldIte.next();
                if (comparator.compare(newObj, oldObj) == 0) {
                    unmodifiedValue.add(oldObj);
                    oldIte.remove();
                    newIte.remove();
                }
            }
        }

        return new CompareResult<T>(new ArrayList<>(newCollection), new ArrayList<>(oldCollection), unmodifiedValue);
    }


    /**
     * 列表比较结果对象
     *
     * @param <T>
     */
    public static class CompareResult<T> {
        /**
         * 新增的对象列表
         */
        private final List<T> addValue;
        /**
         * 删除的对象列表
         */
        private final List<T> delValue;
        /**
         * 没有改变的对象列表
         */
        private final List<T> unmodifiedValue;

        public CompareResult(List<T> addValue, List<T> delValue, List<T> unmodifiedValue) {
            this.addValue = addValue;
            this.delValue = delValue;
            this.unmodifiedValue = unmodifiedValue;
        }

        public List<T> getDelValue() {
            return delValue;
        }

        public List<T> getAddValue() {
            return addValue;
        }

        public List<T> getUnmodifiedValue() {
            return unmodifiedValue;
        }
    }
}
