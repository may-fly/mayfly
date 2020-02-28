package mayfly.sys.module.sys.service.impl;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.permission.SessionLocal;
import mayfly.core.util.DigestUtils;
import mayfly.sys.common.base.model.PageQuery;
import mayfly.sys.common.base.model.PageResult;
import mayfly.sys.common.base.service.impl.BaseServiceImpl;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.module.open.controller.form.AccountLoginForm;
import mayfly.sys.module.sys.controller.form.AccountForm;
import mayfly.sys.module.sys.controller.query.AccountQuery;
import mayfly.sys.module.sys.controller.vo.AccountVO;
import mayfly.sys.module.sys.entity.Account;
import mayfly.sys.module.sys.mapper.AccountMapper;
import mayfly.sys.module.sys.service.AccountRoleService;
import mayfly.sys.module.sys.service.AccountService;
import mayfly.sys.module.sys.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:57
 */
@Service
public class AccountServiceImpl extends BaseServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountRoleService accountRoleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public PageResult<AccountVO> listByQuery(AccountQuery query, PageQuery pageQuery) {
        PageResult<Account> accountPage = listByCondition(BeanUtils.copyProperties(query, Account.class), pageQuery);
        List<AccountVO> accountVOS = BeanUtils.copyProperties(accountPage.getList(), AccountVO.class);
        accountVOS.forEach(a -> {
            a.setRoles(accountRoleService.listRoleByAccountId(a.getId()));
        });
        return PageResult.with(accountPage.getTotal(), accountVOS);
    }

    @Override
    public Account login(AccountLoginForm adminForm) {
        Account condition = new Account().setUsername(adminForm.getUsername())
                .setPassword(DigestUtils.md5DigestAsHex(adminForm.getPassword()));
        Account account = getByCondition(condition);
        if (account != null) {
            BusinessAssert.equals(account.getStatus(), EnableDisableEnum.ENABLE.getValue(), "该账号已被禁用");
        }
        return account;
    }

    @Override
    public void logout(String token) {
        permissionService.removeToken(token);
        permissionService.removePermissions(SessionLocal.getUserId());
    }

    @Override
    public void saveAccount(AccountForm accountForm) {
        BusinessAssert.isNull(getByCondition(new Account().setUsername(accountForm.getUsername())),
                "该用户名已存在");
        Account account = BeanUtils.copyProperties(accountForm, Account.class);
        account.setPassword(DigestUtils.md5DigestAsHex(accountForm.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        account.setCreateTime(now);
        account.setUpdateTime(now);
        // 默认启用状态
        account.setStatus(EnableDisableEnum.ENABLE.getValue());
        insert(account);
    }
}
