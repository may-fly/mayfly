package mayfly.sys.service;

import mayfly.common.web.UriPattern;
import mayfly.sys.web.vo.LoginSuccessVO;

import java.util.List;

/**
 * 权限服务
 * @author: meilin.huang
 * @date: 2018/6/26 上午9:48
 */
public interface PermissionService {

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

}
