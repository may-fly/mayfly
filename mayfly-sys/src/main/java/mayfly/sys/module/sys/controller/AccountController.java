package mayfly.sys.module.sys.controller;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.core.util.enums.EnumUtils;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.module.sys.controller.form.AccountForm;
import mayfly.sys.module.sys.controller.form.RoleUserForm;
import mayfly.sys.module.sys.controller.query.AccountQuery;
import mayfly.sys.module.sys.controller.vo.AccountRoleVO;
import mayfly.sys.module.sys.entity.AccountDO;
import mayfly.sys.module.sys.service.AccountRoleService;
import mayfly.sys.module.sys.service.AccountService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 管理员控制器
 *
 * @author hml
 * @date 2018/6/27 下午4:44
 */
@Permission(code = "account")
@RestController
@RequestMapping("/sys/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRoleService accountRoleService;
    @Autowired
    private ResourceService resourceService;

    @GetMapping()
    public Result<?> list(AccountQuery accountQuery) {
        return accountService.listByQuery(accountQuery).toResult();
    }

    @PostMapping()
    public Result<?> save(@Valid @RequestBody AccountForm accountForm) {
        accountService.create(accountForm);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody AccountForm accountForm) {
        accountService.create(accountForm);
        return Result.success();
    }

    @PutMapping("/{id}/{status}")
    public Result<?> changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        BusinessAssert.state(EnumUtils.isExist(EnableDisableEnum.values(), status), "状态值错误");
        AccountDO a = new AccountDO().setStatus(status);
        a.setId(id);
        accountService.updateByIdSelective(a);
        return Result.success();
    }

    @Permission
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        accountService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}/roleIds")
    public Result<?> roleIds(@PathVariable Long id) {
        return Result.success(accountRoleService.listRoleIdByAccountId(id));
    }

    @GetMapping("/{id}/roles")
    public Result<?> roles(@PathVariable Long id) {
        return Result.success(BeanUtils.copyProperties(accountRoleService.listRoleByAccountId(id), AccountRoleVO.class));
    }

    @GetMapping("/{id}/resources")
    public Result<?> resources(@PathVariable Long id) {
        return Result.success(resourceService.listByAccountId(id));
    }

    @Permission
    @PostMapping("/{id}/roles")
    public Result<?> saveRoles(@PathVariable Long id, @RequestBody RoleUserForm adminForm) {
        List<Long> ids;
        try {
            ids = Stream.of(adminForm.getRoleIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            return Result.paramError("roleIds参数错误！");
        }
        accountRoleService.saveRoles(id, ids);
        return Result.success();
    }

    @Permission(requireCode = false)
    @PostMapping("/logout/{token}")
    public Result<?> logout(@PathVariable String token) {
        accountService.logout(token);
        return Result.success();
    }

}
