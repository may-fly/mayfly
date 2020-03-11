package mayfly.sys.module.sys.service.impl;

import mayfly.core.base.model.BaseDO;
import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.permission.LoginAccount;
import mayfly.core.thread.GlobalThreadPool;
import mayfly.core.util.ArrayUtils;
import mayfly.core.util.CollectionUtils;
import mayfly.core.util.JsonUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.module.sys.entity.OperationLogDO;
import mayfly.sys.module.sys.enums.LogTypeEnum;
import mayfly.sys.module.sys.mapper.OperationLogMapper;
import mayfly.sys.module.sys.service.OperationLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @date 2020-03-05 1:30 下午
 */
@Service
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLogMapper, OperationLogDO> implements OperationLogService {

    /**
     * 不需要记录变化的字段值
     */
    public static String[] ignoreFields = new String[]{BaseDO.UPDATE_TIME, BaseDO.UPDATE_ACCOUNT_ID,
            BaseDO.UPDATE_ACCOUNT, BaseDO.CREATE_ACCOUNT, BaseDO.CREATE_ACCOUNT_ID};


    @Override
    public void asyncLog(String log, LogTypeEnum type) {
        LoginAccount<Integer> la = LoginAccount.get();
        GlobalThreadPool.execute(() -> {
            OperationLogDO logDO = new OperationLogDO().setOperation(log).setType(type.getValue());
            logDO.autoSetBaseInfo(true, la);
            insert(logDO);
        });
    }

    @Override
    public void asyncUpdateLog(String desc, Object newObj, BaseDO oldObj) {
        LoginAccount<Integer> la = LoginAccount.get();
        GlobalThreadPool.execute(() -> {
            List<String> results = BeanUtils.getFieldValueChangeRecords(newObj, oldObj,
                    field -> !ArrayUtils.contains(ignoreFields, field.getName()))
                    .stream().filter(f -> f.getNewValue() != null)
                    .map(r -> r.getFieldName() + ": " + r.getOldValue() + " -> " + r.getNewValue())
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(results)) {
                return;
            }
            String operation = desc + "(id = " + oldObj.getId() + ") => (" + String.join(", ", results) + ")";
            OperationLogDO logDO = new OperationLogDO().setOperation(operation).setType(LogTypeEnum.UPDATE.getValue());
            logDO.autoSetBaseInfo(true, la);
            insert(logDO);
        });
    }

    @Override
    public void asyncDeleteLog(String desc, Object obj) {
        LoginAccount<Integer> la = LoginAccount.get();
        GlobalThreadPool.execute(() -> {
            OperationLogDO logDO = new OperationLogDO().setOperation(desc + " => " + JsonUtils.toJSONString(obj))
                    .setType(LogTypeEnum.DELETE.getValue());
            logDO.autoSetBaseInfo(true, la);
            insert(logDO);
        });
    }
}
