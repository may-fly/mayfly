package mayfly.core.util;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * 树形结构工具类
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-24 1:57 下午
 */
public class TreeUtils {

    /**
     * 根据所有树节点列表，生成含有所有树形结构的多叉树列表, 列表中每个元素都是顶层根节点
     * <p>如果节点列表中没有顶层根节点, 则父id不在节点列表中也可以算顶层节点</p>
     *
     * @param nodes 树形节点列表
     * @param <T>   节点类型
     * @return 树形结构列表
     */
    public static <Id, T extends TreeNode<Id, T>> List<T> generateTrees(List<T> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return emptyList();
        }
        // 根节点列表
        List<T> roots = new ArrayList<>();
        // 父id 对应的 子节点列表 Map
        Map<Id, List<T>> parentId2ChildrenMap = new HashMap<>(nodes.size() >> 1);
        for (T node : nodes) {
            // 是根节点 或 父id为空 都算根节点
            if (node.root() || node.parentId() == null) {
                roots.add(node);
            } else {
                // 非根节点
                // 根据 父id 合并 子节点
                parentId2ChildrenMap.computeIfAbsent(node.parentId(), k -> new ArrayList<>())
                        .add(node);
            }
        }
        // 如果节点列表中没有顶层根节点, 则父id 不在节点列表中 也可以算顶层节点
        if (roots.isEmpty()) {
            // 所有节点id集合
            final Set<Id> nodeIdSet = nodes.stream().map(T::id).collect(Collectors.toSet());
            for (T node : nodes) {
                if (!nodeIdSet.contains(node.parentId())) {
                    roots.add(node);
                }
            }
        }
        for (T root : roots) {
            // 填充子节点
            setChildren(root, parentId2ChildrenMap);
        }
        return roots;
    }

    /**
     * 设置parent的所有子节点
     *
     * @param parent               父节点
     * @param parentId2ChildrenMap 父id 对应的 子节点列表 Map
     */
    private static <Id, T extends TreeNode<Id, T>> void setChildren(T parent, Map<Id, List<T>> parentId2ChildrenMap) {
        Id parentId = parent.id();
        final List<T> children = parentId2ChildrenMap.getOrDefault(parentId, emptyList());
        // 避免子列表为null
        parent.setChildren(children);
        // 如果孩子为空，则直接返回,否则继续递归设置孩子的孩子
        if (children.isEmpty()) {
            return;
        }
        // 删除已使用的数据
        parentId2ChildrenMap.remove(parentId);
        for (T child : children) {
            // 递归设置子节点
            setChildren(child, parentId2ChildrenMap);
        }
    }

    /**
     * 从所有节点列表中查找并设置parent的所有子节点
     *
     * @param parent 父节点
     * @param nodes  所有树节点列表
     */
    public static <Id, T extends TreeNode<Id, T>> void setChildren(T parent, List<T> nodes) {
        List<T> children = new ArrayList<>();
        Object parentId = parent.id();
        for (Iterator<T> ite = nodes.iterator(); ite.hasNext(); ) {
            T node = ite.next();
            if (Objects.equals(node.parentId(), parentId)) {
                children.add(node);
                // 从所有节点列表中删除该节点，以免后续重复遍历该节点
                ite.remove();
            }
        }
        // 如果孩子为空，则直接返回,否则继续递归设置孩子的孩子
        if (children.isEmpty()) {
            return;
        }
        parent.setChildren(children);
        children.forEach(m -> {
            // 递归设置子节点
            setChildren(m, nodes);
        });
    }

    /**
     * 获取指定树节点下的所有叶子节点
     *
     * @param parent 父节点
     * @param <T>    实际节点类型
     * @return 叶子节点
     */
    public static <Id, T extends TreeNode<Id, T>> List<T> getLeaves(T parent) {
        List<T> leaves = new ArrayList<>();
        fillLeaf(parent, leaves);
        return leaves;
    }

    /**
     * 将parent的所有叶子节点填充至leafs列表中
     *
     * @param parent 父节点
     * @param leaves 叶子节点列表
     * @param <T>    实际节点类型
     */
    public static <Id, T extends TreeNode<Id, T>> void fillLeaf(T parent, List<T> leaves) {
        List<T> children = parent.getChildren();
        // 如果节点没有子节点则说明为叶子节点
        if (CollectionUtils.isEmpty(children)) {
            leaves.add(parent);
            return;
        }
        // 递归调用子节点，查找叶子节点
        for (T child : children) {
            fillLeaf(child, leaves);
        }
    }


    /**
     * 树节点父类，所有需要使用{@linkplain TreeUtils}工具类形成树形结构等操作的节点都需要实现该接口
     *
     * @param <Id> 节点id类型
     */
    public interface TreeNode<Id, E extends TreeNode<Id, E>> {
        /**
         * 获取节点id
         *
         * @return 树节点id
         */
        Id id();

        /**
         * 获取该节点的父节点id
         *
         * @return 父节点id
         */
        Id parentId();

        /**
         * 是否是根节点
         *
         * @return true：根节点
         */
        boolean root();

        /**
         * 设置节点的子节点列表
         *
         * @param children 子节点
         */
        void setChildren(List<E> children);

        /**
         * 获取所有子节点
         *
         * @return 子节点列表
         */
        List<E> getChildren();
    }
}
