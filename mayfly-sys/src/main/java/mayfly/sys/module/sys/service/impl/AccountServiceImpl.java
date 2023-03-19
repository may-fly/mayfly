package mayfly.sys.module.sys.service.impl;

import jakarta.annotation.Resource;
import mayfly.core.base.mapper.BaseMapper;
import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BizAssert;
import mayfly.core.model.result.PageResult;
import mayfly.core.util.DigestUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.module.open.controller.form.AccountLoginForm;
import mayfly.sys.module.sys.controller.form.AccountForm;
import mayfly.sys.module.sys.controller.query.AccountQuery;
import mayfly.sys.module.sys.controller.vo.AccountVO;
import mayfly.sys.module.sys.entity.AccountDO;
import mayfly.sys.module.sys.mapper.AccountMapper;
import mayfly.sys.module.sys.service.AccountRoleService;
import mayfly.sys.module.sys.service.AccountService;
import mayfly.sys.module.sys.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:57
 */
@Service
public class AccountServiceImpl extends BaseServiceImpl<AccountDO> implements AccountService {

    @Resource
    private AccountMapper accountMapper;
    @Autowired
    private AccountRoleService accountRoleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public BaseMapper<AccountDO> getMapper() {
        return accountMapper;
    }

    @Override
    public PageResult<AccountVO> listByQuery(AccountQuery query) {
        return PageResult.withPageHelper(query, () -> accountMapper.selectByQuery(query), AccountVO.class);
    }

    @Override
    public AccountDO login(AccountLoginForm adminForm) {
        AccountDO condition = new AccountDO().setUsername(adminForm.getUsername())
                .setPassword(DigestUtils.md5DigestAsHex(adminForm.getPassword()));
        AccountDO account = getByCondition(condition);
        BizAssert.notNull(account, "用户名或密码错误！");
        BizAssert.equals(account.getStatus(), EnableDisableEnum.ENABLE.getValue(), "该账号已被禁用");

        return account;
    }

    @Override
    public void logout(String token) {
        permissionService.removeToken(token);
    }

    @Override
    public void create(AccountForm accountForm) {
        BizAssert.equals(countByCondition(new AccountDO().setUsername(accountForm.getUsername())), 0L,
                "该用户名已存在");
        AccountDO account = BeanUtils.copy(accountForm, AccountDO.class);
        account.setPassword(DigestUtils.md5DigestAsHex(accountForm.getPassword()));
        // 默认启用状态
        account.setStatus(EnableDisableEnum.ENABLE.getValue());
        insert(account);
    }
}
