package mayfly.sys.common.base.service.impl;

import com.github.pagehelper.PageHelper;
import mayfly.core.exception.BusinessRuntimeException;
import mayfly.core.result.Page;
import mayfly.sys.common.utils.SpringUtils;
import mayfly.sys.common.base.form.PageForm;
import mayfly.sys.common.base.mapper.BaseMapper;
import mayfly.sys.common.base.service.BaseService;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-06 2:21 PM
 */
public class BaseServiceImpl<M extends BaseMapper<E>, E> implements BaseService<E> {

    @Override
    public E save(E e) {
        if (getMapper().insert(e) == 1) {
            return e;
        }
        throw new BusinessRuntimeException("保存失败！");
    }

    @Override
    public E updateById(E e) {
        if (getMapper().updateByPrimaryKeySelective(e) == 1) {
            return e;
        }
        throw new BusinessRuntimeException("更新失败！");
    }

    @Override
    public Boolean deleteById(Integer id) {
        return getMapper().deleteByPrimaryKey(id) == 1;
    }

    @Override
    public Boolean deleteByCondition(E e) {
        return getMapper().deleteByCriteria(e) != 0;
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

    @Override
    public List<E> listByIdIn(List<Integer> ids) {
        return getMapper().selectByPrimaryKeyIn(ids);
    }

    @Override
    public List<E> listByCondition(E e) {
        return getMapper().selectByCriteria(e);
    }

    @Override
    public Page<E> listByCondition(E e, PageForm pageForm) {
        PageHelper.startPage(pageForm.getPageNum(), pageForm.getPageSize());
        List<E> result = listByCondition(e);
        if (result instanceof com.github.pagehelper.Page) {
            com.github.pagehelper.Page<E> pageResult = (com.github.pagehelper.Page<E>) result;
            return Page.with(pageResult.getTotal(), pageResult.getResult());
        }
        return Page.empty();
    }

    @Override
    public E getById(Integer id) {
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public E getByCondition(E e) {
        return getMapper().selectOneByCriteria(e);
    }


    private M getMapper() {
        // mapper class对象
        Class<?> mapperClazz = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return (M) SpringUtils.getBean(mapperClazz);
    }
}
