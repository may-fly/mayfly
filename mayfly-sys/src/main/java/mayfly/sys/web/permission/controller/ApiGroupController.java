package mayfly.sys.web.permission.controller;

import mayfly.common.exception.BusinessException;
import mayfly.common.log.MethodLog;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.common.web.auth.Permission;
import mayfly.entity.ApiGroup;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.ApiGroupService;
import mayfly.sys.web.permission.form.ApiGroupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-13 6:00 PM
 */
@Permission(code = "group:")
@RestController
@RequestMapping("/sys")
public class ApiGroupController {

    @Autowired
    private ApiGroupService apiGroupService;

    @MethodLog("保存api组")
    @GetMapping("/v1/apiGroups")
    public Result getGroupList() {
        return Result.success().withData(apiGroupService.listAll("create_time DESC"));
    }

    @PostMapping("/v1/apiGroups")
    public Result saveGroup(@RequestBody @Valid ApiGroupForm apiGroupForm) {
        try {
            return Result.success().withData(apiGroupService.saveGroup(BeanUtils.copyProperties(apiGroupForm, ApiGroup.class)));
        } catch (BusinessException e) {
            return Result.paramError(e.getMessage());
        }
    }
}
