package mayfly.sys.module.sys.entity;

import lombok.Data;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.base.model.BaseDO;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 角色
 * @author: hml
 * @date: 2018/6/27 下午2:03
 */
@Data
@Table("tb_role")
public class RoleDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 5311816841875852758L;

    private String name;

    private String remark;

    private Integer status;

    @NoColumn
    private List<ResourceDO> resources;
}
