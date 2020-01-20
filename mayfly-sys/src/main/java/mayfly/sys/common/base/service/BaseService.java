package mayfly.sys.common.base.service;

import mayfly.core.result.Page;
import mayfly.sys.common.base.form.PageForm;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-06 2:19 PM
 */
public interface BaseService<E> {

    /**
     * 保存实体
     *
     * @param e
     * @return
     */
    E save(E e);

    /**
     * 根据id更新实体
     *
     * @param e
     * @return
     */
    E updateById(E e);

    /**
     * 删除指定id的实体
     *
     * @param id
     * @return
     */
    Boolean deleteById(Integer id);

    /**
     * 根据条件删除
     *
     * @param e 实体对象
     * @return  是否成功
     */
    Boolean deleteByCondition(E e);

    /**
     * 根据条件统计实体数
     *
     * @param e
     * @return
     */
    Long countByCondition(E e);


    /**
     * 获取所有数据
     *
     * @param orderBy 排序条件
     * @return
     */
    List<E> listAll(String orderBy);

    /**
     * 获取所有实体数据（默认id倒序查询）
     *
     * @return 实体列表
     */
    List<E> listAll();

    /**
     * 根据id列表查询数据
     * @param ids  id列表
     * @return      list
     */
    List<E> listByIdIn(List<Integer> ids);

    /**
     * 根据实体条件查询实体列表
     *
     * @param e
     * @return
     */
    List<E> listByCondition(E e);

    /**
     * 根据实体对象分页查询数据
     *
     * @param e         实体条件
     * @param pageForm  分页参数
     * @return          分页对象
     */
    Page<E> listByCondition(E e, PageForm pageForm);

    /**
     * 根据主键查询数据
     *
     * @param id  id
     * @return    实体
     */
    E getById(Integer id);

    /**
     * 根据条件获取单个对象
     *
     * @param e
     * @return
     */
    E getByCondition(E e);
}
