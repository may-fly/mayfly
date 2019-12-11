package mayfly.sys.service.base.impl;

import com.github.pagehelper.PageHelper;
import mayfly.core.result.Page;
import mayfly.dao.base.BaseMapper;
import mayfly.sys.common.utils.SpringUtils;
import mayfly.sys.service.base.BaseService;
import mayfly.sys.web.form.PageForm;

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
        throw new RuntimeException("保存失败！");
    }

    @Override
    public E updateById(E e) {
        if (getMapper().updateByPrimaryKeySelective(e) == 1) {
            return e;
        }
        throw new RuntimeException("更新失败！");
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
