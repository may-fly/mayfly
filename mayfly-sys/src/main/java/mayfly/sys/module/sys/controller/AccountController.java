package mayfly.sys.module.sys.controller;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.util.enums.EnumUtils;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.common.base.form.PageForm;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.module.sys.controller.form.AccountForm;
import mayfly.sys.module.sys.controller.form.RoleUserForm;
import mayfly.sys.module.sys.controller.query.AccountQuery;
import mayfly.sys.module.sys.entity.Account;
import mayfly.sys.module.sys.service.AccountRoleService;
import mayfly.sys.module.sys.service.AccountService;
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
 * 管理员控制器
 *
 * @author hml
 * @date 2018/6/27 下午4:44
 */
@Permission(code = "account:")
@RestController
@RequestMapping("/sys/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRoleService accountRoleService;

    @GetMapping()
    public Result<?> list(@Valid PageForm pageForm, AccountQuery accountQuery) {
        return Result.success(accountService.listByQuery(accountQuery, pageForm));
    }

    @PostMapping()
    public Result<?> save(@Valid @RequestBody AccountForm accountForm) {
        accountService.saveAccount(accountForm);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Integer id, @Valid @RequestBody AccountForm accountForm) {
        accountService.saveAccount(accountForm);
        return Result.success();
    }

    @PutMapping("/{id}/{status}")
    public Result<?> changeStatus(@PathVariable Integer id, @PathVariable Integer status) {
        BusinessAssert.state(EnumUtils.isExist(EnableDisableEnum.values(), status), "状态值错误");
        Account build = Account.builder().id(id).status(status).build();
        accountService.updateById(build);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        accountService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}/roles")
    public Result<?> roles(@PathVariable Integer id) {
        return Result.success(accountRoleService.listRoleIdByAccountId(id));
    }

    @PostMapping("/{id}/roles")
    public Result<?> saveRoles(@PathVariable Integer id, @RequestBody RoleUserForm adminForm) {
        List<Integer> ids;
        try {
            ids = Stream.of(adminForm.getRoleIds().split(",")).map(Integer::valueOf).collect(Collectors.toList());
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
