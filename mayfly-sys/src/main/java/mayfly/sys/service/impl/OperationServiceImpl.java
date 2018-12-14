package mayfly.sys.service.impl;

import mayfly.dao.OperationMapper;
import mayfly.entity.Operation;
import mayfly.sys.service.OperationService;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 1:42 PM
 */
@Service
public class OperationServiceImpl extends BaseServiceImpl<OperationMapper, Operation> implements OperationService {

    @Autowired
    private OperationMapper operationMapper;

    @Override
    public List<Operation> getByUserId(Integer userId) {
        return operationMapper.selectByUserId(userId);
    }
}
