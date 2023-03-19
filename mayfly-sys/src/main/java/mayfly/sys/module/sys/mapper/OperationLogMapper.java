package mayfly.sys.module.sys.mapper;

import mayfly.core.base.mapper.BaseMapper;
import mayfly.sys.module.sys.entity.OperationLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author meilin.huang
 * @date 2020-03-05 1:27 下午
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLogDO> {
}
