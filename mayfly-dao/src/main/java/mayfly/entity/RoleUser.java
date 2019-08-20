package mayfly.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 权限
 * @author: hml
 * @date: 2018/6/27 下午2:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUser {

    private Integer id;

    private Integer userId;

    private Integer roleId;

    private LocalDateTime createTime;
}
