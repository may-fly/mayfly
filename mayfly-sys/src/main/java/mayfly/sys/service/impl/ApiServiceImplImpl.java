package mayfly.sys.service.impl;

import mayfly.common.enums.StatusEnum;
import mayfly.common.exception.BusinessException;
import mayfly.dao.ApiMapper;
import mayfly.entity.Api;
import mayfly.sys.service.ApiGroupService;
import mayfly.sys.service.ApiService;
import mayfly.sys.service.MenuService;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-23 5:27 PM
 */
@Service
public class ApiServiceImplImpl extends BaseServiceImpl<ApiMapper, Api> implements ApiService {

    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ApiGroupService apiGroupService;

    @Override
    public Api saveApi(Api api) throws BusinessException {
        if (apiGroupService.getById(api.getGroupId()) == null) {
            throw new BusinessException("API组不存在！");
        }
        if (countByCondition(Api.builder().uriPattern(api.getUriPattern()).method(api.getMethod()).build()) != 0) {
            throw new BusinessException("该API已经存在！");
        }
        if (countByCondition(Api.builder().code(api.getCode()).build()) != 0) {
           throw new BusinessException("该API code已存在！");
        }
        LocalDateTime now = LocalDateTime.now();
        api.setCreateTime(now);
        api.setUpdateTime(now);
        api.setStatus(StatusEnum.ENABLE.value());
        return save(api);
    }

    @Transactional
    @Override
    public Boolean deleteApi(Integer id) {
//        List<Menu> menus = menuService.listByCondition(Menu.builder().apiId(id).build());
//        menus.forEach(m -> {
//            //设置菜单为api失效状态
//            m.setStatus(Menu.StatusEnum.INVALID.value());
//            m.setUpdateTime(LocalDateTime.now());
//            menuService.updateById(m);
//        });
        return deleteById(id);
    }

    @Override
    public List<Api> listByUserId(Integer userId) {
        return apiMapper.selectByUserId(userId);
    }
}
