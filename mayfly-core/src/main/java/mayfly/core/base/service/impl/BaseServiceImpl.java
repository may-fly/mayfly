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
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-06 2:21 PM
 */
public class BaseServiceImpl<M extends BaseMapper<Integer, E>, E extends BaseDO> implements BaseService<E> {

    /**
     * Mapper
     */
    @Autowired
    protected M mapper;

    protected void setMapper(M mapper) {
        this.mapper = mapper;
    }

    /**
     * 设置BaesMapper
     */
    protected void setBaseMapper() {
    }

    @Override
    public E getById(Integer id) {
        if (id == null) {
            return null;
        }
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public List<E> listByIdIn(List<Integer> ids) {
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
    public Integer deleteById(Integer id) {
        if (id == null) {
            return 0;
        }
        return getMapper().deleteByPrimaryKey(id);
    }

    @Override
    public Integer fakeDeleteById(Integer id) {
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


    protected BaseMapper<Integer, E> getMapper() {
        return mapper;
    }
}
