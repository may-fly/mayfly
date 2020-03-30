package mayfly.sys.module.sys.service.impl;

import mayfly.core.base.model.PageResult;
import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BusinessAssert;
import mayfly.core.log.MethodLog;
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
@MethodLog(value = "账号管理:")
@Service
public class AccountServiceImpl extends BaseServiceImpl<AccountMapper, Long, AccountDO> implements AccountService {

    @Autowired
    private AccountRoleService accountRoleService;
    @Autowired
    private PermissionService permissionService;


    @MethodLog(value = "获取账号列表", level = MethodLog.LogLevel.DEBUG)
    @Override
    public PageResult<AccountVO> listByQuery(AccountQuery query) {
        return PageResult.withPageHelper(query, () -> mapper.selectByQuery(query), AccountVO.class);
    }

    @MethodLog(level = MethodLog.LogLevel.NONE)
    @Override
    public AccountDO login(AccountLoginForm adminForm) {
        AccountDO condition = new AccountDO().setUsername(adminForm.getUsername())
                .setPassword(DigestUtils.md5DigestAsHex(adminForm.getPassword()));
        AccountDO account = getByCondition(condition);
        if (account != null) {
            BusinessAssert.equals(account.getStatus(), EnableDisableEnum.ENABLE.getValue(), "该账号已被禁用");
        }
        return account;
    }

    @MethodLog(level = MethodLog.LogLevel.NONE)
    @Override
    public void logout(String token) {
        permissionService.removeToken(token);
    }

    @Override
    public void create(AccountForm accountForm) {
        BusinessAssert.equals(countByCondition(new AccountDO().setUsername(accountForm.getUsername())), 0L,
                "该用户名已存在");
        AccountDO account = BeanUtils.copyProperties(accountForm, AccountDO.class);
        account.setPassword(DigestUtils.md5DigestAsHex(accountForm.getPassword()));
        // 默认启用状态
        account.setStatus(EnableDisableEnum.ENABLE.getValue());
        insert(account);
    }
}
