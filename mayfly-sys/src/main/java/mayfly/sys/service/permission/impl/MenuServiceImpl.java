package mayfly.sys.service.permission.impl;

import mayfly.common.enums.StatusEnum;
import mayfly.common.exception.BusinessException;
import mayfly.dao.MenuMapper;
import mayfly.entity.Menu;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: 菜单实现类
 * @author: hml
 * @date: 2018/6/27 下午4:09
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getByUserId(Integer userId) {
        return genTreeByMenus(menuMapper.selectByUserId(userId));
    }

    @Override
    public List<Menu> listMenus(Menu condition) {
        return genTreeByMenus(menuMapper.selectAll("pid ASC, weight DESC"));
    }

    @Override
    public Menu saveMenu(Menu menu) throws BusinessException {
        if (menu.getPid() == null || menu.getPid().equals(0)) {
            menu.setPid(0);
        } else {
            if (getById(menu.getPid()) == null) {
                throw new BusinessException("pid不存在！");
            }
        }
        //默认启用
        menu.setStatus(StatusEnum.ENABLE.getValue());
        LocalDateTime now = LocalDateTime.now();
        menu.setCreateTime(now);
        menu.setUpdateTime(now);
        return save(menu);
    }

    @Override
    public Boolean deleteMenu(Integer id) {
        List<Integer> deleteIds = getChildrenByPid(id);
        Integer rootId;
        for (Integer i : deleteIds) {
           if (!deleteById(i)) {
               return false;
           }
        }
        return true;
    }

    /**
     * 获取该菜单及其所有子菜单
     * @param id
     * @return
     */
    List<Integer> getChildrenByPid(Integer id) {
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        Integer pid = id;
        a: for (Iterator<Menu> ite = listAll().iterator(); ite.hasNext(); ) {
            Menu menu = ite.next();
            if (menu.getPid().equals(pid)) {
                Integer menuId = menu.getId();
                ids.add(menuId);
                pid = menuId;
                ite.remove();
                break a;
            }
        }

        return ids;
    }

    /**
     * 生成菜单树
     * @param menus
     * @return
     */
    private List<Menu> genTreeByMenus(List<Menu> menus) {
        //获取所有父节点
        List<Menu> roots = new ArrayList<>();
        for (Iterator<Menu> ite = menus.iterator(); ite.hasNext();) {
            Menu menu = ite.next();
            if (menu.getPid().equals(0)) {
                roots.add(menu);
                ite.remove();
            }
        }
        roots.forEach(r -> {
            setChildren(r, menus);
        });
        return roots;
    }

    private void setChildren(Menu parent, List<Menu> menus) {
        List<Menu> children = new ArrayList<>();
        for (Iterator<Menu> ite = menus.iterator(); ite.hasNext();) {
            Menu menu = ite.next();
            if (menu.getPid().equals(parent.getId())) {
                children.add(menu);
                ite.remove();
            }
        }
        //如果孩子为空，则直接返回,否则继续递归设置孩子的孩子
        if (children.isEmpty()) {
            return;
        }
        parent.setChildren(children);
        children.forEach(m -> {
            setChildren(m, menus);
        });
    }

}
