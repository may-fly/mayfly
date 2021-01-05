package mayfly.sys.module.sys.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

/**
 * @author meilin.huang
 * @date 2020-03-05 1:23 下午
 */
@Getter
@Setter
@Accessors(chain = true)
@Table("tb_sys_operation_log")
@NoColumn(fields = {BaseDO.UPDATE_TIME, BaseDO.MODIFIER, BaseDO.MODIFIER_ID})
public class OperationLogDO extends BaseDO {

    private Integer type;

    private String operation;
}
