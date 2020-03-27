package mayfly.sys.module.sys.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.base.model.BaseDO;

/**
 * @author meilin.huang
 * @date 2020-03-05 1:23 下午
 */
@Getter
@Setter
@Accessors(chain = true)
@Table("tb_sys_operation_log")
@NoColumn(fields = {BaseDO.UPDATE_TIME, BaseDO.UPDATE_ACCOUNT, BaseDO.UPDATE_ACCOUNT_ID})
public class OperationLogDO extends BaseDO<Integer> {

    private Integer type;

    private String operation;
}
