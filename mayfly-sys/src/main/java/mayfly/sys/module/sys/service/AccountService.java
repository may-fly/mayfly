package mayfly.sys.module.sys.service;


import mayfly.core.base.model.PageResult;
import mayfly.core.base.service.BaseService;
import mayfly.sys.module.open.controller.form.AccountLoginForm;
import mayfly.sys.module.sys.controller.form.AccountForm;
import mayfly.sys.module.sys.controller.query.AccountQuery;
import mayfly.sys.module.sys.controller.vo.AccountVO;
import mayfly.sys.module.sys.entity.AccountDO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:56
 */
public interface AccountService extends BaseService<Long, AccountDO> {

    PageResult<AccountVO> listByQuery(AccountQuery query);

    AccountDO login(AccountLoginForm adminForm);

    void logout(String token);

    void create(AccountForm accountForm);
}
