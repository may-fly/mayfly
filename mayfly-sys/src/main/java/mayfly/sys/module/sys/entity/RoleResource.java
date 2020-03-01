package mayfly.sys.module.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.model.BaseEntity;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:14 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NoColumn(fields = {"updateTime"})
public class RoleResource extends BaseEntity {
    private Integer roleId;

    private Integer resourceId;
}
