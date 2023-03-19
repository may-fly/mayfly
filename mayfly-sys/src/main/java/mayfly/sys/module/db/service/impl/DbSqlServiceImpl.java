package mayfly.sys.module.db.service.impl;

import jakarta.annotation.Resource;
import mayfly.core.base.mapper.BaseMapper;
import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.sys.module.db.entity.DbSqlDO;
import mayfly.sys.module.db.mapper.DbSqlMapper;
import mayfly.sys.module.db.service.DbSqlService;
import org.springframework.stereotype.Service;

/**
 * @author meilin.huang
 * @date 2021-01-08 4:16 下午
 */
@Service
public class DbSqlServiceImpl extends BaseServiceImpl<DbSqlDO> implements DbSqlService {
    @Resource
    private DbSqlMapper dbSqlMapper;

    @Override
    public BaseMapper<DbSqlDO> getMapper() {
        return dbSqlMapper;
    }
}
