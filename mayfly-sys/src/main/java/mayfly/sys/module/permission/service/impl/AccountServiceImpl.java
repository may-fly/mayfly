package mayfly.sys.module.permission.service.impl;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.result.Page;
import mayfly.core.util.DigestUtils;
import mayfly.core.util.enums.BoolEnum;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.module.base.form.PageForm;
import mayfly.sys.module.base.service.impl.BaseServiceImpl;
import mayfly.sys.module.permission.controller.form.AccountForm;
import mayfly.sys.module.permission.controller.form.AccountLoginForm;
import mayfly.sys.module.permission.controller.query.AccountQuery;
import mayfly.sys.module.permission.controller.vo.AccountVO;
import mayfly.sys.module.permission.entity.Account;
import mayfly.sys.module.permission.mapper.AccountMapper;
import mayfly.sys.module.permission.service.AccountRoleService;
import mayfly.sys.module.permission.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:57
 */
@Service
public class AccountServiceImpl extends BaseServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountRoleService accountRoleService;

    @Override
    public Page<AccountVO> listByQuery(AccountQuery query, PageForm pageForm) {
        Page<Account> accountPage = listByCondition(BeanUtils.copyProperties(query, Account.class), pageForm);
        List<AccountVO> accountVOS = BeanUtils.copyProperties(accountPage.getList(), AccountVO.class);
        accountVOS.forEach(a -> {
            a.setRoles(accountRoleService.listRoleByAccountId(a.getId()));
        });
        return Page.with(accountPage.getTotal(), accountVOS);
    }

    @Override
    public Account login(AccountLoginForm adminForm) {
        Account condition = Account.builder().username(adminForm.getUsername())
                .password(DigestUtils.md5DigestAsHex(adminForm.getPassword())).build();
        Account account = getByCondition(condition);
        if (account != null) {
            BusinessAssert.state(Objects.equals(account.getStatus(), BoolEnum.TRUE.getValue()), "该账号已被禁用");
        }
        return account;
    }

    @Override
    public void saveAdmin(AccountForm accountForm) {
        BusinessAssert.isNull(getByCondition(Account.builder().username(accountForm.getUsername()).build()),
                "该用户名已存在");
        Account account = BeanUtils.copyProperties(accountForm, Account.class);
        account.setPassword(DigestUtils.md5DigestAsHex(accountForm.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        account.setCreateTime(now);
        account.setUpdateTime(now);
        // 默认启用状态
        account.setStatus(BoolEnum.TRUE.getValue());
        save(account);
    }
}
