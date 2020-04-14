package mayfly.sys.module.sys.controller;

import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.util.bean.BeanUtils;
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

import javax.validation.Valid;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 2:49 PM
 */
@Permission(code = "resource")
@RestController
@RequestMapping("/sys/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping()
    public Result<?> list(ResourceQuery queryForm) {
        return Result.success(resourceService.listResource(BeanUtils.copyProperties(queryForm, ResourceDO.class)));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(BeanUtils.copyProperties(resourceService.getById(id), ResourceDetailVO.class));
    }

    @Permission
    @PostMapping()
    public Result<?> save(@RequestBody @Valid ResourceForm resourceForm) {
        resourceService.create(BeanUtils.copyProperties(resourceForm, ResourceDO.class));
        return Result.success();
    }

    @Permission
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody @Valid ResourceForm resourceForm) {
        ResourceDO resource = BeanUtils.copyProperties(resourceForm, ResourceDO.class);
        resource.setId(id);
        resourceService.update(resource);
        return Result.success();
    }

    @PutMapping("/{id}/{status}")
    public Result<?> changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        resourceService.changeStatus(id, status);
        return Result.success();
    }

    @Permission
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        resourceService.delete(id);
        return Result.success();
    }
}
