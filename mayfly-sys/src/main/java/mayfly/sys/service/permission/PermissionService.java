package mayfly.sys.service.permission;

import mayfly.entity.Admin;
import mayfly.entity.Permission;
import mayfly.sys.service.base.BaseService;
import mayfly.sys.web.permission.vo.LoginSuccessVO;

/**
 * 权限服务
 * @author: meilin.huang
 * @date: 2018/6/26 上午9:48
 */
public interface PermissionService extends BaseService<Permission> {
    /**
     * 保存id以及对应的权限
     * @param admin  管理员信息
     * @return   token
     */
    LoginSuccessVO saveIdAndPermission(Admin admin);

    /**
     * 根据token获取用户id
     * @param token
     * @return  userId
     */
    Integer getIdByToken(String token);

    /**
     * 保存权限
     * @param permission
     * @return
     */
    Permission savePermission(Permission permission);

    /**
     * 更新权限
     * @param permission
     * @return
     */
    Permission updatePermission(Permission permission);

    /**
     * 改变redis中权限状态code
     * @param id 权限code
     * @param status
     * @return
     */
    Permission changeStatus(Integer id, Integer status);

    /**
     * 删除权限，并且删除角色关联的该权限记录
     * @param id
     * @return
     */
    Boolean deletePermission(Integer id);

}
