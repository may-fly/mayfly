package mayfly.sys.module.sys.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

import java.io.Serializable;
import java.util.List;

/**
 * 资源类
 *
 * @author hml
 * @date 2018/6/27 下午2:01
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table("t_resource")
public class ResourceDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 6346288647519202567L;

    private Long pid;

    private Integer type;

    private Integer weight;

    private String name;

    private String code;

    private Integer status;

    private String meta;

    @NoColumn
    private List<ResourceDO> children;

}
