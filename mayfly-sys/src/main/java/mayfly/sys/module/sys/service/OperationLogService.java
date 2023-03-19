package mayfly.sys.module.sys.service;

import mayfly.core.model.BaseDO;
import mayfly.core.base.service.BaseService;
import mayfly.core.permission.LoginAccount;
import mayfly.sys.module.sys.entity.OperationLogDO;
import mayfly.sys.module.sys.enums.LogTypeEnum;

/**
 * @author meilin.huang
 * @date 2020-03-05 1:29 下午
 */
public interface OperationLogService extends BaseService<OperationLogDO> {

    /**
     * 异步记录日志
     *
     * @param log  日志信息
     * @param type 日志类型
     */
    void asyncLog(String log, LogTypeEnum type);

    void asyncLog(String log, LogTypeEnum type, LoginAccount account);

    /**
     * 异步记录数据修改的日志
     *
     * @param desc   描述
     * @param mewObj 新对象
     * @param oldObj 旧对象
     */
    void asyncUpdateLog(String desc, Object mewObj, BaseDO oldObj);

    /**
     * 异步记录删除对象日志
     *
     * @param desc 描述
     * @param obj  对象
     */
    void asyncDeleteLog(String desc, Object obj);
}
