package mayfly.sys.web.permission.controller;

import mayfly.common.exception.BusinessException;
import mayfly.common.log.MethodLog;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.common.permission.Permission;
import mayfly.entity.Menu;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.MenuService;
import mayfly.sys.web.permission.form.MenuForm;
import mayfly.sys.web.permission.form.MenuQueryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 2:49 PM
 */
@Permission(code = "menu:")
@RestController
@RequestMapping("/sys")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Permission(code = "list")
    @MethodLog(value = "获取菜单列表", result = false)
    @GetMapping("/v1/menus")
    public Result getAllMenus(MenuQueryForm queryForm) {
        return Result.success().withData(menuService.listMenus(BeanUtils.copyProperties(queryForm, Menu.class)));
    }

    @MethodLog(value = "新增菜单")
    @PostMapping("/v1/menus")
    public Result save(@RequestBody @Valid MenuForm menuForm) throws BusinessException{
        return Result.success().withData(menuService.saveMenu(BeanUtils.copyProperties(menuForm, Menu.class)));
    }

    @MethodLog("更新菜单")
    @PutMapping("/v1/menus/{id}")
    public Result update(@PathVariable Integer id, @RequestBody @Valid MenuForm menuForm) {
        Menu menu = BeanUtils.copyProperties(menuForm, Menu.class);
        menu.setId(id);
        menu.setUpdateTime(LocalDateTime.now());
        return Result.success().withData(menuService.updateById(menu));
    }

    @MethodLog("删除菜单")
    @DeleteMapping("/v1/menus/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success().withData(menuService.deleteMenu(id));
    }
}
