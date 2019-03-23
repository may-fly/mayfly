package mayfly.sys.web.permission.controller;

import mayfly.common.exception.BusinessException;
import mayfly.common.log.MethodLog;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.common.web.auth.Permission;
import mayfly.entity.Role;
import mayfly.entity.RoleResource;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.RoleService;
import mayfly.sys.web.permission.form.RoleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-20 9:31 AM
 */
@Permission(code = "role:")
@RestController
@RequestMapping("/sys")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @MethodLog(value = "获取角色列表")
    @GetMapping("/v1/roles")
    public Result list() {
        return Result.success().withData(roleService.listAll("create_time DESC"));
    }

    @MethodLog("新增角色")
    @PostMapping("/v1/roles")
    public Result save(@Valid @RequestBody RoleForm roleForm) {
        Role role = BeanUtils.copyProperties(roleForm, Role.class);
        LocalDateTime now = LocalDateTime.now();
        role.setCreateTime(now);
        role.setUpdateTime(now);
        return Result.success().withData(roleService.save(role));
    }

    @MethodLog("获取角色拥有的权限")
    @GetMapping("/v1/roles/{id}/permissions")
    public Result rolePermissions(@PathVariable Integer id) {
        return Result.success().withData(roleService.listResourceId(id, RoleResource.TypeEnum.PERMISSION));
    }

    @MethodLog("保存角色权限")
    @PostMapping("/v1/roles/{id}/permissions")
    public Result savePermission(@PathVariable Integer id, @RequestBody RoleForm roleForm) throws BusinessException {
        List<Integer> ids;
        try {
            ids = Stream.of(roleForm.getResourceIds().split(",")).map(sid -> Integer.valueOf(sid))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Result.paramError("permissionIds参数错误！");
        }

        return Result.success().withData(roleService.saveResource(id, ids, RoleResource.TypeEnum.PERMISSION));
    }

    @MethodLog("获取角色拥有的菜单")
    @GetMapping("/v1/roles/{id}/menus")
    public  Result roleMenus(@PathVariable Integer id) {
        return Result.success().withData(roleService.listResourceId(id, RoleResource.TypeEnum.MENU));
    }

    @MethodLog("保存角色菜单")
    @PostMapping("/v1/roles/{id}/menus")
    public  Result saveMenu(@PathVariable Integer id, @RequestBody RoleForm roleForm) throws BusinessException {
        List<Integer> ids;
        try {
            ids = Stream.of(roleForm.getResourceIds().split(",")).map(sid -> Integer.valueOf(sid))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Result.paramError("menuIds参数错误！");
        }

        return Result.success().withData(roleService.saveResource(id, ids, RoleResource.TypeEnum.MENU));
    }
}
