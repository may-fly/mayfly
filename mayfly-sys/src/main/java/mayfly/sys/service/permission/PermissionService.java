package mayfly.sys.service.permission;

import mayfly.entity.Admin;
import mayfly.sys.web.permission.vo.LoginSuccessVO;

/**
 * 权限服务
 * @author: meilin.huang
 * @date: 2018/6/26 上午9:48
 */
public interface PermissionService {
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
     * 若有修改权限code以及状态，重新加载权限code，简单粗暴
     */
    void reloadPermission();

    /**
     * 添加新权限code
     * @param permissionCode
     */
    void addPermission(String permissionCode);

//    void disablePermission(String code);
//
//    void enablePermission(String code);
//
//
//    void delPermission(String permissionCode);
}
