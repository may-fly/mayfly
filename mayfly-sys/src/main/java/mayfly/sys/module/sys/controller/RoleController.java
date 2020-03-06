package mayfly.sys.module.sys.controller;

import mayfly.core.exception.BusinessException;
import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.module.sys.controller.form.RoleForm;
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

    @Permission(requireCode = false)
    @GetMapping()
    public Result<?> list() {
        return Result.success().with(roleService.listAll("create_time DESC"));
    }

    @PostMapping()
    public Result<?> save(@Valid @RequestBody RoleForm roleForm) {
        RoleDO role = BeanUtils.copyProperties(roleForm, RoleDO.class);
        role.setStatus(EnableDisableEnum.ENABLE.getValue());
        return Result.success(roleService.insert(role));
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Integer id, @Valid @RequestBody RoleForm roleForm) {
        roleForm.setId(id);
        roleService.update(BeanUtils.copyProperties(roleForm, RoleDO.class));
        return Result.success();
    }

    @Permission
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        roleService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}/resources")
    public Result<?> roleResources(@PathVariable Integer id) {
        return Result.success(roleResourceService.listResourceId(id));
    }

    @Permission
    @PostMapping("/{id}/resources")
    public Result<?> saveResources(@PathVariable Integer id, @RequestBody RoleForm roleForm) throws BusinessException {
        List<Integer> ids;
        try {
            ids = Stream.of(roleForm.getResourceIds().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            return Result.paramError("menuIds参数错误！");
        }

        return Result.success(roleResourceService.saveResource(id, ids));
    }
}
