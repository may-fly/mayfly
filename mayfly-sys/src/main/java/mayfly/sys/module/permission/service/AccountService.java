package mayfly.sys.module.permission.service;

import mayfly.core.result.Page;
import mayfly.sys.module.base.form.PageForm;
import mayfly.sys.module.base.service.BaseService;
import mayfly.sys.module.permission.controller.form.AccountForm;
import mayfly.sys.module.permission.controller.form.AccountLoginForm;
import mayfly.sys.module.permission.controller.query.AccountQuery;
import mayfly.sys.module.permission.controller.vo.AccountVO;
import mayfly.sys.module.permission.entity.Account;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:56
 */
public interface AccountService extends BaseService<Account> {

    Page<AccountVO> listByQuery(AccountQuery query, PageForm pageForm);

    Account login(AccountLoginForm adminForm);

    void saveAdmin(AccountForm accountForm);
}
