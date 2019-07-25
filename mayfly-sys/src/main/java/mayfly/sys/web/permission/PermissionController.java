package mayfly.sys.web.permission;

import mayfly.common.log.MethodLog;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Permission;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.PermissionService;
import mayfly.sys.web.form.PageForm;
import mayfly.sys.web.permission.form.PermissionForm;
import mayfly.sys.web.permission.query.PermissionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-14 5:09 PM
 */
@MethodLog("权限管理：")
@mayfly.common.permission.Permission(code = "permission:")
@RestController
@RequestMapping("/sys")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @MethodLog(value = "获取权限列表", resultLevel = MethodLog.LogLevel.DEBUG)
    @GetMapping("/v1/permissions")
    public Result list(PermissionQuery query, @Valid PageForm pageForm) {
        Permission p = BeanUtils.copyProperties(query, Permission.class);
        return Result.success().with(permissionService.listByCondition(p, pageForm));
    }

    @PostMapping("/v1/permissions")
    public Result save(@RequestBody @Valid PermissionForm permissionForm) {
        return Result.success().with(permissionService.savePermission(BeanUtils.copyProperties(permissionForm, Permission.class)));
    }

    @PutMapping("/v1/permissions/{id}")
    public Result update(@RequestBody @Valid PermissionForm permissionForm, @PathVariable Integer id) {
        Permission permission = BeanUtils.copyProperties(permissionForm, Permission.class);
        permission.setId(id);
        return Result.success().with(permissionService.updatePermission(permission));
    }

    @PutMapping("/v1/permissions/{id}/{status}")
    public Result changeStatus(@PathVariable Integer id, @PathVariable Integer status) {
        return Result.success().with(permissionService.changeStatus(id, status));
    }

    @DeleteMapping("/v1/permissions/{id}")
    public Result del(@PathVariable Integer id) {
        return permissionService.deletePermission(id) ? Result.success() : Result.serverError();
    }
}
