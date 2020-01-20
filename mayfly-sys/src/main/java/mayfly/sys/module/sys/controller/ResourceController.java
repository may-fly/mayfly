package mayfly.sys.module.sys.controller;

import mayfly.core.log.MethodLog;
import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.module.sys.controller.form.ResourceForm;
import mayfly.sys.module.sys.controller.query.MenuQuery;
import mayfly.sys.module.sys.controller.vo.ResourceDetailVO;
import mayfly.sys.module.sys.entity.Resource;
import mayfly.sys.module.sys.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 2:49 PM
 */
@MethodLog("资源管理：")
@Permission(code = "resource:")
@RestController
@RequestMapping("/sys/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Permission(code = "list")
    @MethodLog(value = "获取菜单列表", resultLevel = MethodLog.LogLevel.DEBUG)
    @GetMapping()
    public Result<?> getAllMenus(MenuQuery queryForm) {
        return Result.success(resourceService.listResource(BeanUtils.copyProperties(queryForm, Resource.class)));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Integer id) {
        return Result.success(BeanUtils.copyProperties(resourceService.getById(id), ResourceDetailVO.class));
    }

    @PostMapping()
    public Result<?> save(@RequestBody @Valid ResourceForm resourceForm) {
        return Result.success(resourceService.saveResource(BeanUtils.copyProperties(resourceForm, Resource.class)));
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Integer id, @RequestBody @Valid ResourceForm resourceForm) {
        Resource resource = BeanUtils.copyProperties(resourceForm, Resource.class);
        resource.setId(id);
        return Result.success(resourceService.updateResource(resource));
    }

    @PutMapping("/{id}/{status}")
    public Result<?> changeStatus(@PathVariable Integer id, @PathVariable Integer status) {
        return Result.success(resourceService.changeStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        resourceService.deleteResource(id);
        return Result.success();
    }
}
