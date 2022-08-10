package mayfly.sys.module.sys.controller;

import mayfly.core.log.annotation.Log;
import mayfly.core.model.result.PageResult;
import mayfly.core.model.result.Response2Result;
import mayfly.core.exception.BizAssert;
import mayfly.core.permission.Permission;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.module.sys.controller.form.RoleForm;
import mayfly.sys.module.sys.controller.query.RoleQuery;
import mayfly.sys.module.sys.controller.vo.RoleResourceVO;
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
@Response2Result
@Permission(code = "role")
@RestController
@RequestMapping("/sys/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceService roleResourceService;

    @GetMapping()
    public PageResult<RoleDO> list(RoleQuery query) {
        return roleService.listByCondition(BeanUtils.copy(query, RoleDO.class), query);
    }

    @Log("新建角色")
    @PostMapping()
    public int add(@Valid @RequestBody RoleForm roleForm) {
        RoleDO role = BeanUtils.copy(roleForm, RoleDO.class);
        role.setStatus(EnableDisableEnum.ENABLE.getValue());
        return roleService.insert(role);
    }

    @Log("更新角色")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody RoleForm roleForm) {
        roleForm.setId(id);
        roleService.update(BeanUtils.copy(roleForm, RoleDO.class));
    }

    @Log("删除角色")
    @Permission
    @DeleteMapping("/{id}")
    public void del(@PathVariable Long id) {
        roleService.delete(id);
    }

    @GetMapping("/{id}/resourceIds")
    public List<Long> roleResourceIds(@PathVariable Long id) {
        return roleResourceService.listResourceId(id);
    }

    @GetMapping("/{id}/resources")
    public List<RoleResourceVO> roleResources(@PathVariable Long id) {
        return roleResourceService.listResource(id);
    }

    @Permission
    @PostMapping("/{id}/resources")
    @Log("保存角色资源")
    public void saveResources(@PathVariable Long id, @RequestBody RoleForm roleForm) {
        List<Long> ids;
        try {
            ids = Stream.of(roleForm.getResourceIds().split(",")).map(Long::valueOf)
                    .distinct().collect(Collectors.toList());
        } catch (Exception e) {
            throw BizAssert.newException("资源id列表参数错误！");
        }
        roleResourceService.saveResource(id, ids);
    }
}
