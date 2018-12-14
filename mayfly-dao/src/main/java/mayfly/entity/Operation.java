package mayfly.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.dao.base.annotation.NoColumn;

import java.time.LocalDateTime;

/**
 * 资源操作
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 10:13 AM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Operation {

    private Integer id;

    private Integer menuId;

    private String name;

    private String code;

    private Integer apiId;

    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @NoColumn
    private Api api;
}
