package mayfly.sys.module.sys.controller;

import mayfly.core.log.MethodLog;
import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.module.sys.controller.form.ResourceForm;
import mayfly.sys.module.sys.controller.query.ResourceQuery;
import mayfly.sys.module.sys.controller.vo.ResourceDetailVO;
import mayfly.sys.module.sys.entity.ResourceDO;
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

    @Permission(requireCode = false)
    @MethodLog(value = "获取资源列表", level = MethodLog.LogLevel.DEBUG)
    @GetMapping()
    public Result<?> list(ResourceQuery queryForm) {
        return Result.success(resourceService.listResource(BeanUtils.copyProperties(queryForm, ResourceDO.class)));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Integer id) {
        return Result.success(BeanUtils.copyProperties(resourceService.getById(id), ResourceDetailVO.class));
    }

    @PostMapping()
    public Result<?> save(@RequestBody @Valid ResourceForm resourceForm) {
        return Result.success(resourceService.create(BeanUtils.copyProperties(resourceForm, ResourceDO.class)));
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Integer id, @RequestBody @Valid ResourceForm resourceForm) {
        ResourceDO resource = BeanUtils.copyProperties(resourceForm, ResourceDO.class);
        resource.setId(id);
        return Result.success(resourceService.update(resource));
    }

    @PutMapping("/{id}/{status}")
    public Result<?> changeStatus(@PathVariable Integer id, @PathVariable Integer status) {
        return Result.success(resourceService.changeStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        resourceService.delete(id);
        return Result.success();
    }
}
