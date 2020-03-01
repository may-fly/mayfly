package mayfly.sys.module.sys.entity;

import lombok.Data;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.model.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 角色
 * @author: hml
 * @date: 2018/6/27 下午2:03
 */
@Data
public class Role extends BaseEntity implements Serializable {
    private String name;

    private String remark;

    private Integer status;

    @NoColumn
    private List<Resource> resources;
}
