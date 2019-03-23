package mayfly.sys.service.permission.impl;

import mayfly.common.enums.StatusEnum;
import mayfly.common.exception.BusinessException;
import mayfly.dao.ApiMapper;
import mayfly.entity.Permission;
import mayfly.sys.service.permission.ApiGroupService;
import mayfly.sys.service.permission.ApiService;
import mayfly.sys.service.permission.MenuService;
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
public class ApiServiceImpl extends BaseServiceImpl<ApiMapper, Permission> implements ApiService {

    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ApiGroupService apiGroupService;

    @Override
    public Permission saveApi(Permission permission) throws BusinessException {
//        if (apiGroupService.getById(permission.getGroupId()) == null) {
//            throw new BusinessException("API组不存在！");
//        }
//        if (countByCondition(Permission.builder().uriPattern(permission.getUriPattern()).method(permission.getMethod()).build()) != 0) {
//            throw new BusinessException("该API已经存在！");
//        }
//        if (countByCondition(Permission.builder().code(permission.getCode()).build()) != 0) {
//           throw new BusinessException("该API code已存在！");
//        }
        LocalDateTime now = LocalDateTime.now();
        permission.setCreateTime(now);
        permission.setUpdateTime(now);
        permission.setStatus(StatusEnum.ENABLE.getValue());
        return save(permission);
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
    public List<Permission> listByUserId(Integer userId) {
        return apiMapper.selectByUserId(userId);
    }
}
