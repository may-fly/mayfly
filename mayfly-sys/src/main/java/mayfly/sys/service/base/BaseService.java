package mayfly.sys.service.base;

import mayfly.core.result.Page;
import mayfly.sys.web.form.PageForm;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-06 2:19 PM
 */
public interface BaseService< E> {

    /**
     * 保存实体
     * @param e
     * @return
     */
    E save(E e);

    /**
     * 根据id更新实体
     * @param e
     * @return
     */
    E updateById(E e);

    /**
     * 删除指定id的实体
     * @param id
     * @return
     */
    Boolean deleteById(Integer id);

    /**
     * 根据条件删除
     * @param e
     * @return
     */
    Boolean deleteByCondition(E e);

    /**
     * 根据条件统计实体数
     * @param e
     * @return
     */
    Long countByCondition(E e);


    /**
     * 获取所有数据
     * @param orderBy  排序条件
     * @return
     */
    List<E> listAll(String orderBy);

    List<E> listAll();

    /**
     * 根据实体条件查询实体列表
     * @param e
     * @return
     */
    List<E> listByCondition(E e);

    Page<E> listByCondition(E e, PageForm pageForm);

    E getById(Integer id);

    /**
     * 根据条件获取单个对象
     * @param e
     * @return
     */
    E getByCondition(E e);
}
