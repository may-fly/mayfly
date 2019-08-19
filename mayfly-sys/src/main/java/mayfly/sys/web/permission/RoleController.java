package mayfly.sys.web.permission;

import mayfly.common.enums.BoolEnum;
import mayfly.common.exception.BusinessException;
import mayfly.common.log.MethodLog;
import mayfly.common.permission.Permission;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Role;
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
        return Result.success().with(roleService.listAll("create_time DESC"));
    }

    @PostMapping("/v1/roles")
    public Result save(@Valid @RequestBody RoleForm roleForm) {
        Role role = BeanUtils.copyProperties(roleForm, Role.class);
        LocalDateTime now = LocalDateTime.now();
        role.setCreateTime(now);
        role.setUpdateTime(now);
        role.setStatus(BoolEnum.TRUE.getValue());
        return Result.success().with(roleService.save(role));
    }

    @GetMapping("/v1/roles/{id}/resources")
    public  Result roleResources(@PathVariable Integer id) {
        return Result.success().with(roleResourceService.listResourceId(id));
    }

    @PostMapping("/v1/roles/{id}/resources")
    public  Result saveResources(@PathVariable Integer id, @RequestBody RoleForm roleForm) throws BusinessException {
        List<Integer> ids;
        try {
            ids = Stream.of(roleForm.getResourceIds().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            return Result.paramError("menuIds参数错误！");
        }

        return Result.success().with(roleResourceService.saveResource(id, ids));
    }
}
