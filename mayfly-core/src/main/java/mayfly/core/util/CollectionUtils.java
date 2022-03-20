package mayfly.core.util;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

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
     * 创建arraylist
     *
     * @param size size
     * @param ele  元素
     * @param <T>  元素类型
     * @return ArrayList
     */
    @SafeVarargs
    public static <T> List<T> newArrayList(int size, T... ele) {
        List<T> al = new ArrayList<>(size);
        al.addAll(Arrays.asList(ele));
        return al;
    }

    /**
     * 创建arraylist
     *
     * @param ele  元素
     * @param <T>  元素类型
     * @return ArrayList
     */
    @SafeVarargs
    public static <T> List<T> newArrayList(T... ele) {
        return new ArrayList<>(Arrays.asList(ele));
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
     * 使用分隔符将集合元素拼接为字符串
     *
     * @param collection collection
     * @param separator  分隔符
     * @return 字符串
     */
    public static String joinSeparator2String(Collection<?> collection, String separator) {
        return collection.stream().map(String::valueOf).collect(Collectors.joining(separator));
    }

    /**
     * 根据 元素自带的比较器 比较两个collection中哪些是新增的对象以及删除的对象和没有改变的对象
     *
     * @param newCollection 新集合
     * @param oldCollection 旧集合
     * @param <T>           集合元素泛型对象, 该类型必须实现Comparable接口
     * @return 比较结果 {@link CompareResult}
     */
    public static <T extends Comparable<T>>
    CompareResult<T> compare(Collection<T> newCollection, Collection<T> oldCollection) {
        Set<T> newSet = isEmpty(newCollection) ?
                emptySet() : newCollection instanceof Set ?
                (Set<T>) newCollection : new HashSet<>(newCollection);
        Set<T> oldSet = isEmpty(oldCollection) ?
                emptySet() : oldCollection instanceof Set ?
                (Set<T>) oldCollection : new HashSet<>(oldCollection);

        Set<T> addSet, deleteSet, unmodifiedSet;
        if (newSet.isEmpty()) {
            addSet = emptySet();
            deleteSet = oldSet;
            unmodifiedSet = emptySet();
        } else if (oldSet.isEmpty()) {
            addSet = newSet;
            deleteSet = emptySet();
            unmodifiedSet = emptySet();
        } else {
            deleteSet = new HashSet<>();
            unmodifiedSet = new HashSet<>(oldSet.size());
            // 避免直接操作传入集合
            addSet = new HashSet<>(newSet);
            for (T old : oldSet) {
                // 相同元素
                if (addSet.contains(old)) {
                    unmodifiedSet.add(old);
                    // 移除相同元素后剩余的是新增元素
                    addSet.remove(old);
                } else {
                    deleteSet.add(old);
                }
            }
        }
        return new CompareResult<>(addSet, deleteSet, unmodifiedSet);
    }

    /**
     * 根据 指定比较器 比较两个collection中哪些是新增的对象以及删除的对象和没有改变的对象
     *
     * @param newCollection 新集合
     * @param oldCollection 旧集合
     * @param comparator    比较器
     * @param <T>           集合元素泛型对象
     * @return 比较结果 {@link CompareResult}
     */
    public static <T>
    CompareResult<T> compare(Collection<T> newCollection, Collection<T> oldCollection, Comparator<T> comparator) {
        List<T> newList = isEmpty(newCollection) ?
                emptyList() : newCollection instanceof List ?
                (List<T>) newCollection : new ArrayList<>(newCollection);
        List<T> oldList = isEmpty(oldCollection) ?
                emptyList() : oldCollection instanceof List ?
                (List<T>) oldCollection : new ArrayList<>(oldCollection);
        return compare(newList, oldList, comparator);
    }

    /**
     * 根据 指定比较器 比较两个collection中哪些是新增的对象以及删除的对象和没有改变的对象
     *
     * @param newList    新集合
     * @param oldList    旧集合
     * @param comparator 比较器
     * @param <T>        集合元素泛型对象
     * @return 比较结果 {@link CompareResult}
     */
    public static <T> CompareResult<T> compare(List<T> newList, List<T> oldList, Comparator<T> comparator) {
        Set<T> addSet, deleteSet, unmodifiedSet;
        if (isEmpty(newList)) {
            addSet = emptySet();
            deleteSet = isEmpty(oldList) ? emptySet() : new HashSet<>(oldList);
            unmodifiedSet = emptySet();
        } else if (isEmpty(oldList)) {
            addSet = isEmpty(newList) ? emptySet() : new HashSet<>(newList);
            deleteSet = emptySet();
            unmodifiedSet = emptySet();
        } else {
            final int newSize = newList.size(), oldSize = oldList.size();
            int newIdx = 0, oldIdx = 0;
            // 根据比较器排序
            newList.sort(comparator);
            oldList.sort(comparator);
            addSet = new HashSet<>(newSize);
            deleteSet = new HashSet<>();
            unmodifiedSet = new HashSet<>(oldSize);
            // 遍历两个列表长度相等的部分
            while (newIdx < newSize && oldIdx < oldSize) {
                final T newOne = newList.get(newIdx);
                final T oldOne = oldList.get(oldIdx);
                final int compare = comparator.compare(newOne, oldOne);
                // 相等
                if (compare == 0) {
                    unmodifiedSet.add(oldOne);
                    // 跳过相同的元素
                    do {
                        oldIdx++;
                    } while (oldIdx < oldSize && comparator.compare(oldOne, oldList.get(oldIdx)) == 0);
                    do {
                        newIdx++;
                    } while (newIdx < newSize && comparator.compare(newOne, newList.get(newIdx)) == 0);
                } else if (compare < 0) {
                    // 新增元素
                    addSet.add(newOne);
                    newIdx++;
                } else {
                    // 删除元素
                    deleteSet.add(oldOne);
                    oldIdx++;
                }
            }
            // 最后的新增元素
            if (newIdx < newSize) {
                addSet.addAll(newList.subList(newIdx, newSize));
            }
            // 最后的删除元素
            if (oldIdx < oldSize) {
                deleteSet.addAll(oldList.subList(oldIdx, oldSize));
            }
        }
        return new CompareResult<>(addSet, deleteSet, unmodifiedSet);
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
        private final Set<T> addValue;
        /**
         * 删除的对象列表
         */
        private final Set<T> delValue;
        /**
         * 没有改变的对象列表
         */
        private final Set<T> unmodifiedValue;

        public CompareResult(Set<T> addValue, Set<T> delValue, Set<T> unmodifiedValue) {
            this.addValue = addValue;
            this.delValue = delValue;
            this.unmodifiedValue = unmodifiedValue;
        }

        public Set<T> getDelValue() {
            return delValue;
        }

        public Set<T> getAddValue() {
            return addValue;
        }

        public Set<T> getUnmodifiedValue() {
            return unmodifiedValue;
        }
    }
}
