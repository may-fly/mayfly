package mayfly.sys.service.permission;

import mayfly.common.exception.BusinessException;
import mayfly.entity.Menu;
import mayfly.sys.service.base.BaseService;

import java.util.List;

/**
 * @Description: 菜单服务接口
 * @author: hml
 * @date: 2018/6/27 下午3:14
 */
public interface MenuService extends BaseService<Menu> {

    /**
     * 根据用户id获取用户的所有菜单权限
     * @param userId 用户id
     * @return   菜单列表
     */
    List<Menu> getByUserId(Integer userId);

    /**
     * 获取所有菜单
     * @return
     */
    List<Menu> listMenus(Menu condition);

    /**
     * 删除指定菜单，如果是有子节点，也删除
     * @param id
     * @return
     */
    void deleteMenu(Integer id);

    /**
     * 保存菜单
     * @param menu
   @return
     * @throws BusinessException
     */
    Menu saveMenu(Menu menu) throws BusinessException;
}
