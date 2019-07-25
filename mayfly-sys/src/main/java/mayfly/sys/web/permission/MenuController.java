package mayfly.sys.web.permission;

import mayfly.common.exception.BusinessException;
import mayfly.common.log.MethodLog;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.common.permission.Permission;
import mayfly.entity.Menu;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.MenuService;
import mayfly.sys.web.permission.form.MenuForm;
import mayfly.sys.web.permission.query.MenuQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 2:49 PM
 */
@MethodLog("菜单管理：")
@Permission(code = "menu:")
@RestController
@RequestMapping("/sys")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Permission(code = "list")
    @MethodLog(value = "获取菜单列表", resultLevel = MethodLog.LogLevel.DEBUG)
    @GetMapping("/v1/menus")
    public Result getAllMenus(MenuQuery queryForm) {
        return Result.success().with(menuService.listMenus(BeanUtils.copyProperties(queryForm, Menu.class)));
    }

    @PostMapping("/v1/menus")
    public Result save(@RequestBody @Valid MenuForm menuForm) throws BusinessException{
        return Result.success().with(menuService.saveMenu(BeanUtils.copyProperties(menuForm, Menu.class)));
    }

    @PutMapping("/v1/menus/{id}")
    public Result update(@PathVariable Integer id, @RequestBody @Valid MenuForm menuForm) {
        Menu menu = BeanUtils.copyProperties(menuForm, Menu.class);
        menu.setId(id);
        menu.setUpdateTime(LocalDateTime.now());
        return Result.success().with(menuService.updateById(menu));
    }

    @DeleteMapping("/v1/menus/{id}")
    public Result delete(@PathVariable Integer id) {
        menuService.deleteMenu(id);
        return Result.success();
    }
}
