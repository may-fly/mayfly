package mayfly.sys.web.permission;

import mayfly.common.exception.BusinessException;
import mayfly.common.log.MethodLog;
import mayfly.common.permission.Permission;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Resource;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.ResourceService;
import mayfly.sys.web.permission.form.ResourceForm;
import mayfly.sys.web.permission.query.MenuQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 2:49 PM
 */
@MethodLog("资源管理：")
@Permission(code = "resource:")
@RestController
@RequestMapping("/sys")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Permission(code = "list")
    @MethodLog(value = "获取菜单列表", resultLevel = MethodLog.LogLevel.DEBUG)
    @GetMapping("/v1/resources")
    public Result getAllMenus(MenuQuery queryForm) {
        return Result.success(resourceService.listResource(BeanUtils.copyProperties(queryForm, Resource.class)));
    }

    @PostMapping("/v1/resources")
    public Result save(@RequestBody @Valid ResourceForm resourceForm) throws BusinessException{
        return Result.success(resourceService.saveResource(BeanUtils.copyProperties(resourceForm, Resource.class)));
    }

    @PutMapping("/v1/resources/{id}")
    public Result update(@PathVariable Integer id, @RequestBody @Valid ResourceForm resourceForm) {
        Resource resource = BeanUtils.copyProperties(resourceForm, Resource.class);
        resource.setId(id);
        return Result.success(resourceService.updateResource(resource));
    }

    @PutMapping("/v1/resources/{id}/{status}")
    public Result changeStatus(@PathVariable Integer id, @PathVariable Integer status) {
        return Result.success(resourceService.changeStatus(id, status));
    }

    @DeleteMapping("/v1/resources/{id}")
    public Result delete(@PathVariable Integer id) {
        resourceService.deleteResource(id);
        return Result.success();
    }
}
