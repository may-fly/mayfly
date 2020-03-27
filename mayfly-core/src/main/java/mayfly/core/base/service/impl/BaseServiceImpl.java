package mayfly.core.base.service.impl;


import mayfly.core.base.mapper.BaseMapper;
import mayfly.core.base.model.BaseDO;
import mayfly.core.base.model.PageQuery;
import mayfly.core.base.model.PageResult;
import mayfly.core.base.service.BaseService;
import mayfly.core.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * @param <M> mapper类型
 * @param <T> 主键类型
 * @param <E> 实体类型
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-06 2:21 PM
 */
public class BaseServiceImpl<M extends BaseMapper<T, E>, T, E extends BaseDO<T>> implements BaseService<T, E> {

    /**
     * 实体Mapper，子服务类无需再注入本实体的Mapper，如需要直接调用本类mapper即可
     */
    @Autowired
    protected M mapper;

    protected void setMapper(M mapper) {
        this.mapper = mapper;
    }

    @Override
    public E getById(T id) {
        if (id == null) {
            return null;
        }
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public List<E> listByIdIn(List<T> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return getMapper().selectByPrimaryKeyIn(ids);
    }

    @Override
    public List<E> listByCondition(E e) {
        return getMapper().selectByCriteria(e);
    }

    @Override
    public PageResult<E> listByCondition(E e, PageQuery pageQuery) {
        return PageResult.withPageHelper(pageQuery, () -> this.listByCondition(e));
    }

    @Override
    public E getByCondition(E e) {
        return getMapper().selectOneByCriteria(e);
    }

    @Override
    public Integer insert(E e) {
        e.autoSetBaseInfo();
        return getMapper().insert(e);
    }

    @Override
    public Integer insertSelective(E e) {
        e.autoSetBaseInfo();
        return getMapper().insertSelective(e);
    }

    @Override
    public Integer batchInsert(List<E> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return 0;
        }
        entities.forEach(E::autoSetBaseInfo);
        return getMapper().batchInsert(entities);
    }

    @Override
    public Integer updateByIdSelective(E e) {
        e.autoSetBaseInfo();
        return getMapper().updateByPrimaryKeySelective(e);
    }

    @Override
    public Integer updateById(E e) {
        e.autoSetBaseInfo();
        return getMapper().updateByPrimaryKey(e);
    }

    @Override
    public Integer deleteById(T id) {
        if (id == null) {
            return 0;
        }
        return getMapper().deleteByPrimaryKey(id);
    }

    @Override
    public Integer fakeDeleteById(T id) {
        if (id == null) {
            return 0;
        }
        return getMapper().fakeDeleteByPrimaryKey(id);
    }

    @Override
    public Integer deleteByCondition(E e) {
        return getMapper().deleteByCriteria(e);
    }

    @Override
    public Long countByCondition(E e) {
        return getMapper().countByCriteria(e);
    }

    @Override
    public List<E> listAll(String orderBy) {
        return getMapper().selectAll(orderBy);
    }

    @Override
    public List<E> listAll() {
        return getMapper().selectAll("id DESC");
    }


    protected BaseMapper<T, E> getMapper() {
        return mapper;
    }
}
