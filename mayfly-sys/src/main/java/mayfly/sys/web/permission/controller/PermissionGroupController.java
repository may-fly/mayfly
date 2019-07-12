package mayfly.sys.web.permission.controller;

import mayfly.common.enums.BoolEnum;
import mayfly.common.log.MethodLog;
import mayfly.common.permission.Permission;
import mayfly.common.result.Result;
import mayfly.entity.PermissionGroup;
import mayfly.sys.service.permission.PermissionGroupService;
import mayfly.sys.web.form.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-26 2:55 PM
 */
@Permission(code = "permission:group:")
@MethodLog("权限组管理：")
@RestController
@RequestMapping("/sys/v1/permissionGroups")
public class PermissionGroupController {

    @Autowired
    private PermissionGroupService permissionGroupService;

    @MethodLog(value = "获取分页权限组列表", resultLevel = MethodLog.LogLevel.DEBUG)
    @GetMapping
    public Result list(PageForm pageForm) {
        PermissionGroup condition = PermissionGroup.builder().status(BoolEnum.TRUE.getValue()).build();
        return Result.success().with(permissionGroupService.listByCondition(condition, pageForm));
    }

    @MethodLog(value = "获取所有权限组列表", resultLevel = MethodLog.LogLevel.DEBUG)
    @GetMapping("/all")
    public Result all() {
        return Result.success().with(permissionGroupService.listAll());
    }
}
