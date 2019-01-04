package mayfly.sys.service.permission;

import mayfly.common.exception.BusinessException;
import mayfly.entity.Permission;
import mayfly.sys.service.base.BaseService;

import java.util.List;

/**
 * apiUri服务
 * @author: hml
 * @date: 2018/6/26 上午9:33
 */
public interface ApiService extends BaseService<Permission> {
    /**
     * 保存api
     * @param permission
     * */
    Permission saveApi(Permission permission) throws BusinessException;

    /**
     * 删除指定api
     * @param id
     * @return
     */
    Boolean deleteApi(Integer id);

    /**
     * 根据权限表获取用户可以访问api列表
     * @param userId
     * @return
     */
    List<Permission> listByUserId(Integer userId);

}
