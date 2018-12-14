package mayfly.sys.service;

import mayfly.entity.Operation;
import mayfly.sys.service.base.BaseService;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 1:41 PM
 */
public interface OperationService extends BaseService<Operation> {

    List<Operation> getByUserId(Integer userId);
}
