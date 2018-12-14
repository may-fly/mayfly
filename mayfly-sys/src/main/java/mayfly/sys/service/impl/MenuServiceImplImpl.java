package mayfly.sys.service.impl;

import mayfly.common.enums.StatusEnum;
import mayfly.common.exception.BusinessException;
import mayfly.dao.MenuMapper;
import mayfly.entity.Menu;
import mayfly.sys.service.MenuService;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 菜单实现类
 * @author: hml
 * @date: 2018/6/27 下午4:09
 */
@Service
public class MenuServiceImplImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getByUserId(Integer userId) {
        return genTreeByMenus(menuMapper.selectByUserId(userId));
    }

    @Override
    public List<Menu> getMenus() {
        return genTreeByMenus(menuMapper.selectAll("pid ASC, weight DESC"));
    }

    @Override
    public Menu saveMenu(Menu menu) throws BusinessException {
        if (menu.getPid() == null) {
            menu.setPid(0);
        } else {
            if (getById(menu.getPid()) == null) {
                throw new BusinessException("pid不存在！");
            }
        }
        //默认启用
        menu.setStatus(StatusEnum.ENABLE.value());
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        return save(menu);
    }

    private List<Menu> genTreeByMenus(List<Menu> menus) {
        //获取所有根节点
        List<Menu> root = menus.stream().filter(m -> m.getPid().equals(0)).collect(Collectors.toList());
        root.forEach(r -> {
            setChildren(r, menus);
        });
        return root;
    }

    private void setChildren(Menu parent, List<Menu> menus) {
        List<Menu> children = new ArrayList<>();
        menus.forEach(m -> {
            if (m.getPid().equals(parent.getId())) {
                children.add(m);
            }
        });
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
