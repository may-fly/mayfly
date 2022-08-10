package mayfly.core.model.result;

import com.github.pagehelper.PageHelper;
import mayfly.core.model.PageQuery;
import mayfly.core.util.bean.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 分页结果对象
 *
 * @author meilin.huang
 * @date 2020-02-21 7:34 下午
 */
public class PageResult<T> {

    /**
     * 数据总数
     */
    private final Long total;

    /**
     * 指定页数数据列表
     */
    private final List<T> list;


    private PageResult(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    /**
     * 转换为Result结果对象
     *
     * @return {@link Result}
     */
    public Result<PageResult<T>> toResult() {
        return Result.success(this);
    }

    /**
     * 自动启动PageHelper分页，并包装返回结果
     *
     * @param pageQuery    分页查询条件
     * @param listSupplier list提供器
     * @param <T>          list元素泛型
     * @return {@link PageResult}
     */
    public static <T> PageResult<T> withPageHelper(PageQuery pageQuery, Supplier<List<T>> listSupplier) {
        return withPageHelper(pageQuery.getPageNum(), pageQuery.getPageSize(), listSupplier);
    }

    /**
     * 自动启动PageHelper分页，并包装返回结果
     *
     * @param pageQuery    分页查询条件
     * @param listSupplier list提供器
     * @param toType       需要转换的对象类型
     * @param <T>          list元素泛型
     * @return {@link PageResult}
     */
    public static <T> PageResult<T> withPageHelper(PageQuery pageQuery, Supplier<List<?>> listSupplier, Class<T> toType) {
        return withPageHelper(pageQuery.getPageNum(), pageQuery.getPageSize(), listSupplier, toType);
    }

    /**
     * 自动启动PageHelper分页，并包装返回结果
     *
     * @param pageNum      页码
     * @param pageSize     页数  每页显示的记录数不允许超过 {@link PageQuery#MAX_PAGE_SIZE}
     * @param listSupplier list提供器
     * @param <T>          list元素泛型
     * @return {@link PageResult}
     */
    public static <T> PageResult<T> withPageHelper(Integer pageNum, Integer pageSize, Supplier<List<T>> listSupplier) {
        if (pageSize == null || pageSize > PageQuery.MAX_PAGE_SIZE) {
            pageSize = PageQuery.MAX_PAGE_SIZE;
        }
        // 启动分页
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), pageSize);
        return withPageHelper(listSupplier.get());
    }

    /**
     * 自动启动PageHelper分页并包装返回结构，并提供List对象转换<br/>
     * 如查询出list对象为UserPO支持转换为UserVO的list
     *
     * @param pageNum      页码
     * @param pageSize     页数 每页显示的记录数不允许超过 {@link PageQuery#MAX_PAGE_SIZE}
     * @param listSupplier list提供器
     * @param toType       需要转换为的类型
     * @param <T>          list元素泛型
     * @return {@link PageResult}
     */
    public static <T> PageResult<T> withPageHelper(Integer pageNum, Integer pageSize, Supplier<List<?>> listSupplier, Class<T> toType) {
        if (pageSize == null || pageSize > PageQuery.MAX_PAGE_SIZE) {
            pageSize = PageQuery.MAX_PAGE_SIZE;
        }
        // 启动分页
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), pageSize);
        return withPageHelper(listSupplier.get(), toType);
    }

    /**
     * 包装PageHelper返回的list的独享为PageResult
     *
     * @param data pagehelper包装的list
     * @param <T>  对象泛型
     * @return 分页对象
     */
    public static <T> PageResult<T> withPageHelper(List<T> data) {
        if (data instanceof com.github.pagehelper.Page) {
            com.github.pagehelper.Page<T> page = (com.github.pagehelper.Page<T>) data;
            return with(page.getTotal(), page.getResult());
        }
        throw new IllegalArgumentException("data参数必须为pagehelper包装后的对象，即获取list前需调用PageHelper.startPage()方法");
    }

    /**
     * 包装PageHelper返回的list对象为PageResult，并提供List对象转换<br/>
     * 如查询出list对象为UserPO支持转换为UserVO的list
     *
     * @param data   pagehelper包装的list
     * @param toType 需要转换为的类型
     * @param <T>    对象泛型
     * @return 分页对象
     */
    public static <T> PageResult<T> withPageHelper(List<?> data, Class<T> toType) {
        if (data instanceof com.github.pagehelper.Page) {
            com.github.pagehelper.Page<?> page = (com.github.pagehelper.Page<?>) data;
            return with(page.getTotal(), page.getResult(), toType);
        }
        throw new IllegalArgumentException("data参数必须为pagehelper包装后的对象，即获取list前需调用PageHelper.startPage()方法");
    }

    /**
     * 分页对象简单工厂
     *
     * @param total 总数量
     * @param data  list
     * @param <T>   对象泛型
     * @return 分页对象
     */
    public static <T> PageResult<T> with(Long total, List<T> data) {
        return new PageResult<>(total, data);
    }

    /**
     * 分页对象简单工厂，并提供List对象拷贝转换<br/>
     * 如查询出list对象为UserPO支持转换为UserVO的list
     *
     * @param total  总数量
     * @param data   list
     * @param toType 需要拷贝转换的对象的类型
     * @param <T>    对象泛型
     * @return 分页对象
     */
    public static <T> PageResult<T> with(Long total, List<?> data, Class<T> toType) {
        return with(total, BeanUtils.copy(data, toType));
    }

    /**
     * 空对象
     *
     * @param <T> 对象泛型
     * @return 空分页对象
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<T>(0L, Collections.emptyList());
    }

    /**
     * 获取分页对象list
     *
     * @return list
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 获取总数
     *
     * @return 总数
     */
    public Long getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
