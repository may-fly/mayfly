package mayfly.core.base.service;


import mayfly.core.model.BaseDO;
import mayfly.core.model.PageQuery;
import mayfly.core.model.result.PageResult;

import java.util.List;

/**
 * @param <E> 实体类型
 * @param <T> 实体主键类型
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-06 2:19 PM
 */
public interface BaseService<T, E extends BaseDO> {

    /**
     * 根据主键查询数据
     *
     * @param id  id
     * @return    实体
     */
    E getById(T id);

    /**
     * 保存实体同时自动更新基本信息，如创建时间、创建人等 <br/>
     * （如需插入后的实体id，可从实体获取）
     *
     * @param e  实体
     * @return   影响条数
     */
    int insert(E e);

    /**
     * 插入非空字段属性，同时自动更新基本信息，如创建时间、创建人等 <br/>
     * （如需插入后的实体id，可从实体获取）
     *
     * @param e  实体
     * @return   影响条数
     */
    int insertSelective(E e);

    /**
     * 批量插入实体(不自动更新基本信息，如创建时间、创建人等)，<br/>
     * 如需要设置基本信息手动调用{@linkplain BaseDO#autoSetBaseInfo()}
     *
     * @param entities  实体列表
     * @return    影响条数
     */
    int batchInsert(List<E> entities);

    /**
     * 根据id更新实体（只更新字段值不为null），同时自动更新基本字段，如更新时间、更新人等
     *
     * @param e  实体
     * @return  影响条数
     */
    int updateByIdSelective(E e);

    /**
     * 根据id更新实体（null值也会更新到数据库），同时自动更新基本字段，如更新时间、更新人等
     *
     * @param e  实体
     * @return  影响条数
     */
    int updateById(E e);

    /**
     * 删除指定id的实体
     *
     * @param id   id
     * @return  影响条数
     */
    int deleteById(T id);

    /**
     * 伪删除（即将is_deleted更新为1）
     * @param id  实体id
     * @return     影响条数
     */
    int fakeDeleteById(T id);

    /**
     * 根据条件删除
     *
     * @param e 实体对象
     * @return  影响条数
     */
    int deleteByCondition(E e);

    /**
     * 根据条件统计实体数
     *
     * @param e 条件对象
     * @return  总数
     */
    long countByCondition(E e);


    /**
     * 获取所有数据
     *
     * @param orderBy 排序条件
     * @return  实体列表
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
    List<E> listByIdIn(List<T> ids);

    /**
     * 根据实体条件查询实体列表
     *
     * @param e  实体条件
     * @return   列表
     */
    List<E> listByCondition(E e);

    /**
     * 获取分页列表
     *
     * @param e   实体条件
     * @param pageQuery  分页信息
     * @return  分页列表
     */
    PageResult<E> listByCondition(E e, PageQuery pageQuery);

    /**
     * 根据条件获取单个对象
     *
     * @param e  实体条件
     * @return   实体
     */
    E getByCondition(E e);
}
