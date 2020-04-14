package mayfly.sys.module.sys.controller;

import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.module.sys.controller.form.RoleForm;
import mayfly.sys.module.sys.controller.query.RoleQuery;
import mayfly.sys.module.sys.entity.RoleDO;
import mayfly.sys.module.sys.service.RoleResourceService;
import mayfly.sys.module.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-20 9:31 AM
 */
@Permission(code = "role")
@RestController
@RequestMapping("/sys/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceService roleResourceService;

    @GetMapping()
    public Result<?> list(RoleQuery query) {
        return Result.success().with(roleService.listByCondition(BeanUtils.copyProperties(query, RoleDO.class), query));
    }

    @PostMapping()
    public Result<?> save(@Valid @RequestBody RoleForm roleForm) {
        RoleDO role = BeanUtils.copyProperties(roleForm, RoleDO.class);
        role.setStatus(EnableDisableEnum.ENABLE.getValue());
        return Result.success(roleService.insert(role));
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody RoleForm roleForm) {
        roleForm.setId(id);
        roleService.update(BeanUtils.copyProperties(roleForm, RoleDO.class));
        return Result.success();
    }

    @Permission
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}/resourceIds")
    public Result<?> roleResourceIds(@PathVariable Long id) {
        return Result.success(roleResourceService.listResourceId(id));
    }

    @GetMapping("/{id}/resources")
    public Result<?> roleResources(@PathVariable Long id) {
        return Result.success(roleResourceService.listResource(id));
    }

    @Permission
    @PostMapping("/{id}/resources")
    public Result<?> saveResources(@PathVariable Long id, @RequestBody RoleForm roleForm) {
        List<Long> ids;
        try {
            ids = Stream.of(roleForm.getResourceIds().split(",")).map(Long::valueOf)
                    .distinct().collect(Collectors.toList());
        } catch (Exception e) {
            return Result.paramError("资源id列表参数错误！");
        }
        roleResourceService.saveResource(id, ids);
        return Result.success();
    }
}
