package mayfly.sys.web.permission.controller;

import mayfly.common.exception.BusinessException;
import mayfly.common.log.MethodLog;
import mayfly.common.permission.Permission;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Role;
import mayfly.sys.common.enums.ResourceTypeEnum;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.RoleResourceService;
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
@MethodLog("角色管理：")
@Permission(code = "role:")
@RestController
@RequestMapping("/sys")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceService roleResourceService;

    @GetMapping("/v1/roles")
    public Result list() {
        return Result.success().withData(roleService.listAll("create_time DESC"));
    }

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
        return Result.success().withData(roleResourceService.listResourceId(id, ResourceTypeEnum.PERMISSION));
    }

    @PostMapping("/v1/roles/{id}/permissions")
    public Result savePermission(@PathVariable Integer id, @RequestBody RoleForm roleForm) throws BusinessException {
        List<Integer> ids;
        try {
            ids = Stream.of(roleForm.getResourceIds().split(",")).map(sid -> Integer.valueOf(sid))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Result.paramError("permissionIds参数错误！");
        }

        return Result.success().withData(roleResourceService.saveResource(id, ids, ResourceTypeEnum.PERMISSION));
    }

    @GetMapping("/v1/roles/{id}/menus")
    public  Result roleMenus(@PathVariable Integer id) {
        return Result.success().withData(roleResourceService.listResourceId(id, ResourceTypeEnum.MENU));
    }

    @PostMapping("/v1/roles/{id}/menus")
    public  Result saveMenu(@PathVariable Integer id, @RequestBody RoleForm roleForm) throws BusinessException {
        List<Integer> ids;
        try {
            ids = Stream.of(roleForm.getResourceIds().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            return Result.paramError("menuIds参数错误！");
        }

        return Result.success().withData(roleResourceService.saveResource(id, ids, ResourceTypeEnum.MENU));
    }
}
