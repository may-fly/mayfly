package mayfly.sys.service.permission;

import mayfly.common.exception.BusinessException;
import mayfly.common.web.UriPattern;
import mayfly.entity.Permission;
import mayfly.sys.service.base.BaseService;
import mayfly.sys.web.permission.vo.LoginSuccessVO;

import java.util.List;

/**
 * 权限服务
 * @author: meilin.huang
 * @date: 2018/6/26 上午9:48
 */
public interface PermissionService extends BaseService<Permission> {

    /**
     * 保存id以及对应的权限
     * @param id  userId
     * @return   token
     */
    LoginSuccessVO saveIdAndPermission(Integer id);

    /**
     * 根据token获取用户id
     * @param token
     * @return  userId
     */
    Integer getIdByToken(String token);

    /**
     * 根据token获取用户api路径权限
     * @param token
     * @return
     */
    List<UriPattern> getUriPermissionByToken(String token);


    /**
     * 判断用户指定权限code是否存在
     * @param token
     * @param permissionCode
     * @return
     * @throws BusinessException  权限被禁用
     */
    boolean hasPermission(String token, String permissionCode) throws BusinessException;

    /**
     * 保存权限
     * @param permission
     * @return
     */
    Permission savePermission(Permission permission) throws BusinessException;

    /**
     * 删除权限，并且删除角色关联的该权限记录
     * @param id
     * @return
     */
    Boolean deletePermission(Integer id);

}
