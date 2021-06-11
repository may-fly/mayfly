package mayfly.sys.module.sys.controller;

import mayfly.core.exception.BizAssert;
import mayfly.core.log.MethodLog;
import mayfly.core.model.result.Response2Result;
import mayfly.core.permission.Permission;
import mayfly.core.util.JsonUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.module.sys.controller.form.ResourceForm;
import mayfly.sys.module.sys.controller.query.ResourceQuery;
import mayfly.sys.module.sys.controller.vo.ResourceDetailVO;
import mayfly.sys.module.sys.controller.vo.ResourceListVO;
import mayfly.sys.module.sys.entity.ResourceDO;
import mayfly.sys.module.sys.enums.ResourceTypeEnum;
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
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 2:49 PM
 */
@Response2Result
@Permission(code = "resource")
@RestController
@RequestMapping("/sys/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping()
    public List<ResourceListVO> list(ResourceQuery queryForm) {
        return resourceService.listResource();
    }

    @GetMapping("/{id}")
    public ResourceDetailVO detail(@PathVariable Long id) {
        return BeanUtils.copyProperties(resourceService.getById(id), ResourceDetailVO.class);
    }

    @Permission
    @PostMapping()
    @MethodLog("新增资源")
    public void add(@RequestBody @Valid ResourceForm resourceForm) {
        ResourceDO resource = BeanUtils.copyProperties(resourceForm, ResourceDO.class);
        if (ResourceTypeEnum.MENU.getValue().equals(resourceForm.getType())) {
            BizAssert.notNull(resourceForm.getMeta(), "菜单元数据不能为空");
            resource.setMeta(JsonUtils.toJSONString(resourceForm.getMeta()));
        }
        resourceService.create(resource);
    }

    @Permission
    @MethodLog("更新资源信息")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid ResourceForm resourceForm) {
        ResourceDO resource = BeanUtils.copyProperties(resourceForm, ResourceDO.class);
        if (ResourceTypeEnum.MENU.getValue().equals(resourceForm.getType())) {
            BizAssert.notNull(resourceForm.getMeta(), "菜单元数据不能为空");
            resource.setMeta(JsonUtils.toJSONString(resourceForm.getMeta()));
        }

        resource.setId(id);
        resourceService.update(resource);
    }

    @PutMapping("/{id}/{status}")
    @MethodLog("修改资源状态")
    public void changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        resourceService.changeStatus(id, status);
    }

    @Permission
    @DeleteMapping("/{id}")
    @MethodLog("删除资源")
    public void del(@PathVariable Long id) {
        resourceService.delete(id);
    }
}
