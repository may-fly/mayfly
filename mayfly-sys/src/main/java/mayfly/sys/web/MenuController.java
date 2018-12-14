package mayfly.sys.web;

import mayfly.common.exception.BusinessException;
import mayfly.common.log.MethodLog;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Menu;
import mayfly.sys.common.BeanUtils;
import mayfly.sys.service.MenuService;
import mayfly.sys.web.form.MenuForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 2:49 PM
 */
@RestController
@RequestMapping("/sys")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @MethodLog(value = "获取菜单列表", result = false)
    @GetMapping("/v1/menus")
    public Result getAllMenus() {
        return Result.success().withData(menuService.getMenus());
    }

    @MethodLog(value = "保存菜单")
    @PostMapping("/v1/menus")
    public Result saveMenu(@Valid MenuForm menuForm) {
        try {
            return Result.success().withData(menuService.saveMenu(BeanUtils.copyProperties(menuForm, Menu.class)));
        } catch (BusinessException e) {
            return Result.paramError(e.getMessage());
        }
    }
}
