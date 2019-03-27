package mayfly.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 10:15 AM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroup {

    private Integer id;

    private String name;

    private String description;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
