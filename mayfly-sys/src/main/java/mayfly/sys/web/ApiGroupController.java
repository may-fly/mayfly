package mayfly.sys.web;

import mayfly.common.exception.BusinessException;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.ApiGroup;
import mayfly.sys.common.BeanUtils;
import mayfly.sys.service.ApiGroupService;
import mayfly.sys.web.form.ApiGroupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-13 6:00 PM
 */
@RestController
@RequestMapping("/sys")
public class ApiGroupController {

    @Autowired
    private ApiGroupService apiGroupService;

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
